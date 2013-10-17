package com.zibea.recommendations.convertor;

import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public abstract class AbstractTableJob extends Configured implements Tool {

    protected static final String SEPARATOR = ",";

    private final Options options;

    protected Map<String, String> argMap = null;

    protected String tempDir;

    protected AbstractTableJob() {
        this.options = new Options();
    }

    /**
     * used to make some preparations before job start
     * e.g. clean output folder
     *
     * @param outPath
     * @param job
     * @throws IOException
     */
    protected abstract void prepareOutputFolder(Path outPath, Job job) throws IOException;

    /**
     * Prepares Table input job.
     * input - HBase, output - HDFS
     * Uses {@link FileOutputFormat}
     *
     * @param outputPath
     * @param mapper
     * @param reducer
     * @param reducerKey
     * @param reducerValue
     * @param outputFormat
     * @param jarClass
     * @return
     * @throws IOException
     */
    protected Job prepareTableInputJob(Path outputPath,
                                       Class<? extends TableMapper> mapper,
                                       Class<? extends Reducer> reducer,
                                       Class<? extends WritableComparable> reducerKey,
                                       Class<? extends Writable> reducerValue,
                                       Class<? extends FileOutputFormat> outputFormat,
                                       Class<?> jarClass,
                                       Scan scan,
                                       String table) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        JobConf jobConf = new JobConf(conf);
        jobConf.set(FileOutputFormat.OUTDIR, outputPath.toString());
        Job job = Job.getInstance(jobConf);
        job.setJarByClass(jarClass);
        job.setReducerClass(reducer);    // reducer class
        job.setOutputFormatClass(outputFormat);
        job.setJobName(getCustomJobName(jarClass.getSimpleName(), job, mapper, reducer));

        // Configures mappers
        TableMapReduceUtil.initTableMapperJob(table,
                scan,
                mapper,
                reducerKey,
                reducerValue,
                job);

        return job;
    }

    /**
     * Prepares Table output job.
     * input - file, output - HBase table
     * Uses {@link FileOutputFormat}
     * If reducer class is null, sets number of reducer tasks to zero
     *
     * @param inputPath
     * @param mapper
     * @param reducer
     * @param reducerKey
     * @param reducerValue
     * @param jarClass
     * @param outputTable
     * @return
     * @throws IOException
     */
    protected Job prepareTableOutputJob(Path inputPath,
                                        Class<? extends Mapper> mapper,
                                        Class<? extends Reducer> reducer,
                                        Class<? extends WritableComparable> reducerKey,
                                        Class<? extends Writable> reducerValue,
                                        Class<?> jarClass,
                                        String outputTable) throws IOException {

        JobConf jobConf = new JobConf(HBaseConfiguration.create());
        FileInputFormat.addInputPath(jobConf, inputPath);
        Job job = Job.getInstance(jobConf);

        job.setJarByClass(jarClass);
        job.setMapperClass(mapper);

        if (reducer == null) {
            job.setNumReduceTasks(0);
        } else
            job.setReducerClass(reducer);

        job.setOutputFormatClass(TableOutputFormat.class);
        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, outputTable);
        job.setOutputKeyClass(reducerKey);
        job.setOutputValueClass(reducerValue);
        job.setJobName(getCustomJobName(jarClass.getSimpleName(), job, mapper, reducer));

        return job;
    }

    protected Path getPath(String path) {
        return new Path(path);
    }

    protected Path getTempPath() {
        return new Path(tempDir);
    }

    protected Path getTempPathUnder(String path) {
        return new Path(tempDir, path);
    }

    public static byte[] revert(byte[] array) {
        if (array == null) return null;

        byte[] ret = new byte[array.length];
        int i = 0;
        for (byte b : array) {
            ret[array.length - 1 - i] = b;
            i++;
        }
        return ret;
    }

    protected void addOption(String option, boolean hasArg, String description, boolean required) {
        Option op = new Option(option, hasArg, description);
        op.setRequired(required);
        options.addOption(op);
    }

    protected void addFlag(String option, String description, boolean required) {
        this.addOption(option, false, description, required);
    }

    protected Map<String, String> parseArguments(String[] args) {
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage() + "\n");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Job options" + " ", options, true);
            System.exit(-1);
        }

        for (Option o : cmd.getOptions()) {
            if (argMap == null)
                argMap = new HashMap<String, String>();
            argMap.put(o.getOpt(), o.getValue());
        }

        return argMap;
    }

    protected String getOption(String option) {
        return argMap.get(option);
    }

    protected boolean hasOption(String option) {
        return getOption(option) != null;
    }

    public static String getCustomJobName(String className, JobContext job,
                                          Class<? extends Mapper> mapper,
                                          Class<? extends Reducer> reducer) {
        StringBuilder name = new StringBuilder(100);
        String customJobName = job.getJobName();
        if (customJobName == null || customJobName.trim().isEmpty()) {
            name.append(className);
        } else {
            name.append(customJobName);
        }
        if (mapper != null)
            name.append('-').append(mapper.getSimpleName());
        if (reducer != null)
            name.append('-').append(reducer.getSimpleName());
        return name.toString();
    }

}
