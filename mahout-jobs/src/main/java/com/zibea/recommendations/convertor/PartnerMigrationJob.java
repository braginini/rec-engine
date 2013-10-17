package com.zibea.recommendations.convertor;

import com.zibea.recommendations.common.hbase.Schema;
import com.zibea.recommendations.common.hbase.proto.EventParser;
import com.zibea.recommendations.common.hbase.proto.EventParserException;
import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.common.model.event.ItemViewEvent;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This job use HBase table 'event', family 'iv' as input and partition files by
 * partner ids
 *
 * @author Mikhail Bragin
 */
public class PartnerMigrationJob extends AbstractTableJob {

    public static final String output = "migration";

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new PartnerMigrationJob(), args);
    }

    @Override
    public int run(String[] args) throws Exception {


        addOption("inputTable", true, "the name of an input table", true);
        addOption("inputFamily", true, "the column family name of an event table", true);
        addOption("eventType", true, "the type of the event. Short value", true);
        addOption("output", true, "the output directory", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs.isEmpty()) {
            return -1;
        }

        Path outputPath = getPath(getOption("output"));

        short eventType = validateEventType();

        Scan scan = getScan(getOption("inputFamily"), eventType);
        String table = getOption("inputTable");

        Job job = prepareTableInputJob(outputPath,
                PartnerMapper.class,
                PartnerReducer.class,
                LongWritable.class,
                Text.class,
                TextOutputFormat.class,
                PartnerMigrationJob.class,
                scan,
                table);


        //delete output folder if exists
        prepareOutputFolder(outputPath, job);

        job.getConfiguration().set("mapreduce.output.textoutputformat.separator", AbstractTableJob.SEPARATOR);

        boolean succeeded = job.waitForCompletion(true);

        if (!succeeded) {
            return -1;
        }

        return 0;
    }

    private short validateEventType() {
        short eventType = Short.valueOf(getOption("eventType"));

        if (Event.EventType.lookup(eventType) == null)
            throw new IllegalArgumentException("Event type" + eventType + " was not found");

        return eventType;
    }

    @Override
    protected void prepareOutputFolder(Path outPath, Job job) throws IOException {
        if (outPath.getFileSystem(job.getConfiguration()).exists(outPath)) {
            outPath.getFileSystem(job.getConfiguration()).delete(outPath, true);
        }
    }

    private Scan getScan(String family, short eventType) {
        Scan scan = new Scan();

        scan.setFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryPrefixComparator(Bytes.toBytes(eventType))));

        scan.setCaching(1000);
        scan.setMaxVersions(Integer.MAX_VALUE);
        scan.addFamily(Bytes.toBytes(family));
        return scan;
    }

    /**
     * output: key -> partner id; value -> userId,itemId,occurrences as a String
     */
    //todo create new Writable class which will keep ruId, itemId and preference
    public static class PartnerMapper extends TableMapper<LongWritable, Text> {

        @Override
        protected void map(ImmutableBytesWritable key, Result result, Context context) throws IOException, InterruptedException {
            byte[] rowKey = key.get();

            String ruId = Bytes.toString(Bytes.tail(rowKey, rowKey.length - Schema.BYTES_IN_LONG));
            LongWritable partnerId = new LongWritable(Bytes.toLong(Bytes.head(rowKey, Schema.BYTES_IN_LONG)));

            if (result != null && !result.isEmpty()) {

                MapWritable itemMap = prepareItemOccurrencesMap(result);


                //transform the result item map to a string -> ruId,itemId,occurrences
                for (Map.Entry<Writable, Writable> entry : itemMap.entrySet()) {

                    StringBuilder sb = new StringBuilder(ruId);
                    sb.append(SEPARATOR);

                    LongWritable itemId = (LongWritable) entry.getKey();
                    IntWritable occurrences = (IntWritable) entry.getValue();

                    sb.append(itemId.get());
                    sb.append(SEPARATOR);
                    sb.append(occurrences.get());

                    context.write(partnerId, new Text(sb.toString()));
                }
            }
        }

        //select all user items and calc how many times did user click on it
        //Map<LongWritable, IntWritable>  --> <itemId, number of occurrences>
        private MapWritable prepareItemOccurrencesMap(Result result) throws IOException {

            MapWritable itemMap = new MapWritable();

            for (KeyValue kv : result.list()) {

                short type = Bytes.toShort(Bytes.head(kv.getQualifier(), Schema.BYTES_IN_SHORT));

                try {

                    Event event = EventParser.decode(type, kv.getValue());

                    switch (event.getType()) {

                        case ITEM_VIEW:

                            LongWritable itemId = new LongWritable(((ItemViewEvent) event).getParameter());
                            checkItemOccurrence(itemMap, itemId);

                        case PURCHASE:
                            continue;
                            //throw new UnsupportedOperationException("not implemented yet");
                    }
                } catch (EventParserException e) {
                    throw new IOException("Error while parsing event", e);
                }
            }

            return itemMap;
        }

        //check if we have already put this item into result map
        private void checkItemOccurrence(MapWritable itemMap, LongWritable itemId) {

            if (itemMap.containsKey(itemId)) {
                int numberOfOccurrences = ((IntWritable) itemMap.get(itemId)).get();
                itemMap.put(itemId, new IntWritable(++numberOfOccurrences));
            } else
                itemMap.put(itemId, new IntWritable(1));
        }

    }

    public static class PartnerReducer extends Reducer<LongWritable, Text, NullWritable, Text> {

        private MultipleOutputs output;

        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            long nextId = 0;

            //map that keep ruId as a key and corresponding user id
            Map<String, Long> ruIdMap = new HashMap<String, Long>();
            //we keep here the partnerId key and print it to output

            long normalKey = Bytes.toLong(revert(Bytes.toBytes(key.get())));

            for (Text value : values) {

                //first we need to convert ruId to long userIds as the next job will use only longs
                String[] ruIdUserIdPref = value.toString().split(SEPARATOR);
                String ruId = ruIdUserIdPref[0];

                if (!ruIdMap.containsKey(ruId)) {
                    ruIdMap.put(ruId, nextId);
                    nextId++;
                }

                long userId = ruIdMap.get(ruId);

                StringBuilder sb = new StringBuilder(String.valueOf(userId));
                sb.append(SEPARATOR);
                sb.append(ruIdUserIdPref[1]);
                sb.append(SEPARATOR);
                sb.append(ruIdUserIdPref[2]);

                value = new Text(sb.toString());

                //prepare name of the output
                String partnerIdString = String.valueOf(normalKey);
                sb = new StringBuilder(partnerIdString);
                sb.append("/");
                sb.append(partnerIdString);

                //output to /<outputdir>/partner_id/ folder
                output.write(NullWritable.get(), value, sb.toString());
            }
        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            output = new MultipleOutputs(context);
        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            output.close();
        }
    }


}
