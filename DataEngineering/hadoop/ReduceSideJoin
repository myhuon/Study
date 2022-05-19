import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.GenericOptionsParser;

public class ReduceSideJoin
{

        public static class ReduceSideJoinMapper extends Mapper<Object, Text, Text, Text>
        {
                boolean fileA = true;

                public void map(Object key, Text value, Context context) throws IOException, InterruptedException
                {
                    //Write map code
                   String line = value.toString();
                   StringTokenizer itr = new StringTokenizer(line, "|");
                   Text outputKey = new Text();
                   Text outputValue = new Text();
                   String joinKey = "";
                   String o_value = "";
                   if(fileA){
                        String id = itr.nextToken();
                        String price = itr.nextToken();
                        joinKey = itr.nextToken();
                        o_value = "A," + id + "," + price;
                   }
                   else{
                        joinKey = itr.nextToken();
                        String description = itr.nextToken();
o_value = "B," + description;
                   }

                   outputKey.set(joinKey);
                   outputValue.set(o_value);
                   context.write(outputKey, outputValue);
                }

                protected void setup(Context context) throws IOException, InterruptedException
                {
                        String filename = ((FileSplit)context.getInputSplit()).getPath().getName();

                        if(filename.indexOf("relation_a") != -1) fileA = true;
                        else fileA = false;
                }
        }

        public static class ReduceSideJoinReducer extends Reducer<Text,Text,Text,Text>
        {
                public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
                {
                        Text reduce_key = new Text();
                        Text reduce_result = new Text();
                        String description = "";
                        ArrayList<String> buffer = new ArrayList<String>();

                        for (Text val : values) {
                                String file_type;

                                StringTokenizer itr = new StringTokenizer(val.toString(), ",");
                                file_type = itr.nextToken();

                                if( file_type.compareTo("B")==0 ){
                                        description = itr.nextToken();
                                }
                                else {
String id = itr.nextToken();
                                        String price = itr.nextToken();

                                        if ( description.length() == 0 ) {
                                                buffer.add(id+","+price);
                                        }
                                        else {
                                                String result = price + " " + description;

                                                reduce_key.set(id);
                                                reduce_result.set(result);
                                                context.write(reduce_key, reduce_result);
                                        }
                                }
                        }

                        for(int i = 0; i < buffer.size(); i++){

                                StringTokenizer itr = new StringTokenizer(buffer.get(i).toString(), ",");
                                String id = itr.nextToken();
                                String price = itr.nextToken();
                                String result = price + " " + description;

                                reduce_key.set(id);
                                reduce_result.set(result);

                                context.write(reduce_key, reduce_result);
                        }
                }
        }

        public static void main(String[] args) throws Exception
        {
                Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                if (otherArgs.length != 3)
                {
                        System.err.println("Usage: reducesidejoin <in> <out>");
                        System.exit(2);
                }

                Job job = new Job(conf, "ReduceSideJoin");

                job.setJarByClass(ReduceSideJoin.class);
                job.setMapperClass(ReduceSideJoinMapper.class);
                job.setReducerClass(ReduceSideJoinReducer.class);
//              job.setNumReduceTasks(0);     // map debugging

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(Text.class);

                FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileInputFormat.addInputPath(job, new Path(otherArgs[1]));
                FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));

                FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[2]), true);

                System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}




