package com.zibea.recommendations.mahoutjobs;

import com.zibea.recommendations.convertor.AbstractTableJob;
import com.zibea.recommendations.convertor.PartnerMigrationJob;
import com.zibea.recommendations.util.OptionUtil;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.similarity.item.ItemSimilarityJob;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class ItemSimilarityCFJob extends AbstractTableJob {

    private String inputTable;
    private String inputFamily;

    public static void main(String[] args) throws Exception {

        int exitCode = ToolRunner.run(new ItemSimilarityCFJob(), args);
        System.exit(exitCode);
    }

    @Override
    protected void prepareOutputFolder(Path outPath, Job job) throws IOException {

    }

    @Override
    public int run(String[] args) throws Exception {

        //add job options
        addOption("outputTable", true, OptionUtil.OUTPUT_TABLE_DESCR, true);
        addOption("outputFamily", true, OptionUtil.OUTPUT_FAMILY_DESCR, true);
        addOption("similarityClassname", true, OptionUtil.SIMILARITY_CLASS_DESCR, true);
        addOption("inputTable", true, OptionUtil.INPUT_TABLE_DESCR, true);
        addOption("inputFamily", true, OptionUtil.INPUT_FAMILY_DESCR, true);
        addOption("eventType", true, "event type", true);
        addFlag("clearAfter", OptionUtil.CLEAN_AFTER_DESCR, false);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null || parsedArgs.isEmpty()) {
            return -1;
        }

        inputTable = getOption("inputTable");
        inputFamily = getOption("inputFamily");
        tempDir = OptionUtil.TEMP_DIR_VALUE;

        //we change the migration (partitioning) job output directory by adding the event type
        Path migrationOutPath = getTempPathUnder(
                new Path(PartnerMigrationJob.output, "eventType" + getOption("eventType")).toString());

        //run partition job before to prepare data
        ToolRunner.run(new PartnerMigrationJob(), new String[]{
                "--inputTable", inputTable,
                "--inputFamily", inputFamily,
                "--eventType",  getOption("eventType"),
                "--output", migrationOutPath.toString()});


        FileSystem fs = DistributedFileSystem.get(getConf());

        if (fs.exists(migrationOutPath)) {

            //iterate over output directories (partner directories)
            for (FileStatus status : fs.listStatus(migrationOutPath)) {

                if (status.isDirectory()) {

                    //create the output dir for a partner based on base output directory
                    Path similarityOutputPath = getTempPathUnder(OptionUtil.SIMILARITY_RESULT_DIR_VALUE);
                    Path similarityPartnerOutPath = new Path(similarityOutputPath, new Path(status.getPath().getName()));

                    //prepare temp directory for each partner
                    Path similarityPartnerTempDir = getTempPathUnder(status.getPath().getName());

                    ToolRunner.run(new ItemSimilarityJob(), new String[]{
                            "--similarityClassname", getOption("similarityClassname"),
                            "--input", status.getPath().toString(),
                            "--output", similarityPartnerOutPath.toString(),
                            "--tempDir", similarityPartnerTempDir.toString()});

                    //run the job to import prepared similarities to HBase

                    Job importResultsJob = prepareTableOutputJob(similarityPartnerOutPath,
                            ImportMapper.class,
                            null,
                            ImmutableBytesWritable.class,
                            Writable.class,
                            ItemSimilarityCFJob.class,
                            getOption("outputTable"));

                    long partnerId = Long.valueOf(status.getPath().getName());

                    importResultsJob.getConfiguration().set("conf.partnerid", String.valueOf(partnerId));
                    importResultsJob.getConfiguration().set("conf.family", getOption("outputFamily"));


                    boolean succeeded = importResultsJob.waitForCompletion(true);

                    if (!succeeded) {
                        return -1;
                    }

                }
            }

        } else
            return -1;

        if (hasOption("cleanAfter")) {
            Path tempPath = new Path(getOption("tempDir"));
            if (fs.exists(tempPath))
                fs.delete(tempPath, true);
        }


        return 0;
    }

    static class ImportMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Writable> {
        private byte[] family;
        private Long partnerId;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            String stringFamily = context.getConfiguration().get("conf.family");
            family = Bytes.toBytes(stringFamily);
            partnerId = Long.valueOf(context.getConfiguration().get("conf.partnerid"));
        }

        @Override
        protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
            try {
                String[] splitLine = line.toString().split("\\s+");
                long itemA = Long.valueOf(splitLine[0]);
                long itemB = Long.valueOf(splitLine[1]);
                double sim = Double.valueOf(splitLine[2]);

                byte[] rowKey = Bytes.add(revert(Bytes.toBytes(partnerId)), Bytes.toBytes(itemA));
                Put put = new Put(rowKey)
                        .add(family, Bytes.toBytes(itemB), Bytes.toBytes(sim));
                context.write(new ImmutableBytesWritable(rowKey), put);

                //write also same similarity for itemB
                rowKey = Bytes.add(revert(Bytes.toBytes(partnerId)), Bytes.toBytes(itemB));
                put =  new Put(rowKey)
                        .add(family, Bytes.toBytes(itemA), Bytes.toBytes(sim));
                context.write(new ImmutableBytesWritable(rowKey), put);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
