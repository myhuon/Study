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

public class IMDBStudent20180976
{
        public static class IMDBStudent20180976Mapper extends Mapper<LongWritable, Text, Text, IntWritable>
        {
                private final static IntWritable i_value = new IntWritable(1);
                private Text word = new Text();

                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    //Write map code
                    String line = value.toString();
                    StringTokenizer itr = new StringTokenizer(line, "::");

                    String token="";
                    while(itr.hasMoreTokens()){
                        token  = itr.nextToken().trim();
                    }

                    StringTokenizer st = new StringTokenizer(token, "|");
                    while(st.hasMoreTokens()){
                        word.set(st.nextToken());
                        context.write(word, i_value);
                    }
                 }
	}

        public static class IMDBStudent20180976Reducer extends Reducer<Text,IntWritable,Text,IntWritable>
        {
                private IntWritable sumWritable = new IntWritable();

public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
                {
                    //Write reduce code
                    int sum = 0;
                    for(IntWritable val : values){
                            sum += val.get();
                    }

                    sumWritable.set(sum);
                    context.write(key, sumWritable);
                }
        }

        public static void main(String[] args) throws Exception
        {
                Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                if (otherArgs.length != 2)
                {
                        System.err.println("Usage: wordcount <in> <out>");
                        System.exit(2);
                }

                Job job = new Job(conf, "genre count");

                job.setJarByClass(IMDBStudent20180976.class);
                job.setMapperClass(IMDBStudent20180976Mapper.class);
                job.setCombinerClass(IMDBStudent20180976Reducer.class);
                job.setReducerClass(IMDBStudent20180976Reducer.class);

                job.setOutputKeyClass(Text.class);

job.setOutputValueClass(IntWritable.class);

                FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

                FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);

                System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}
