package com.zibea.recommendations.convertor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.util.GSetByHashMap;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikhail Bragin
 */
public class MigrateViewsToHDFSJob {

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();

        JobConf jobConf = new JobConf(conf);
        jobConf.set("mapreduce.output.textoutputformat.separator", ",");
        Job job = Job.getInstance(jobConf);
        job.setJarByClass(MigrateViewsToHDFSJob.class);

        // Outputs to HDFS text file
        job.setOutputFormatClass(TextOutputFormat.class);
        Scan scan = new Scan();
        scan.setCaching(1000);
        scan.setMaxVersions(Integer.MAX_VALUE);
        scan.addFamily(Bytes.toBytes("iv"));

        // Configures mappers
        TableMapReduceUtil.initTableMapperJob("event",
                scan,
                MyMapper.class,
                LongWritable.class,
                MapWritable.class,
                job);

        // Configures reducers
        //job.setMapperClass(PartnerMapper.class);
        job.setReducerClass(MyReducer.class);    // reducer class
        job.setNumReduceTasks(1);
        TextOutputFormat.setOutputPath(job, new Path("/migration/"));

        boolean b = job.waitForCompletion(true);
        if (!b) {
            throw new IOException("error with job!");
        }

    }

    public static class MyMapper extends TableMapper<LongWritable, MapWritable> {

        @Override
        protected void map(ImmutableBytesWritable key, Result result, Context context) throws IOException, InterruptedException {
            byte[] rowKey = key.get();
            LongWritable userId = new LongWritable(Bytes.toLong(Bytes.tail(rowKey, 8)));

            //Map<LongWritable, IntWritable>  --> <itemId, number of occurrences>
            MapWritable itemMap = new MapWritable();

            if (result != null && !result.isEmpty()) {

                for (KeyValue kv : result.list()) {
                    context.getCounter(COUNTER.KV_COUNTER).increment(1);
                    //get item id from qualifier
                    LongWritable itemId = new LongWritable(Bytes.toLong(kv.getQualifier()));

                    //check if we have already put this item into result map
                    if (itemMap.containsKey(itemId)) {
                        int numberOfOccurrences = ((IntWritable) itemMap.get(itemId)).get();
                        itemMap.put(itemId, new IntWritable(++numberOfOccurrences));
                    } else
                        itemMap.put(itemId, new IntWritable(1));
                }
                context.write(userId, itemMap);
            }
        }

    }

    public static class MyReducer extends Reducer<LongWritable, MapWritable, LongWritable, Text> {

        @Override
        protected void reduce(LongWritable key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {

            for (MapWritable val : values) {  //values are Map<itemId, occurrences>

                for (Map.Entry<Writable, Writable> entry : val.entrySet()) {
                    LongWritable itemId = (LongWritable) entry.getKey();
                    IntWritable occurrences = (IntWritable) entry.getValue();

                    StringBuilder sb = new StringBuilder();
                    sb.append(itemId.get());
                    sb.append(",");
                    sb.append(occurrences.get());
                    context.write(key, new Text(sb.toString()));
                }
            }
        }
    }

    private static enum COUNTER {
        ZERO_COUNTER, KV_COUNTER
    }

}
