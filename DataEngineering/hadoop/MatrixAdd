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

public class MatAdd20180976
{

        public static class MatrixAddMapper extends Mapper<LongWritable, Text, Text, IntWritable>
        {
                private final static IntWritable i_value = new IntWritable(1);
                private Text word = new Text();

                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    String line = value.toString();
                    StringTokenizer itr = new StringTokenizer(line);
                    int row_id = Integer.parseInt(itr.nextToken().trim());
                    int col_id = Integer.parseInt(itr.nextToken().trim());
                    int m_value = Integer.parseInt(itr.nextToken().trim());

                    word.set(row_id + "," + col_id);
                    i_value.set(m_value);

                    context.write(word, i_value);
                }
        }

        public static class MatrixAddReducer extends Reducer<Text,IntWritable,Text,IntWritable>
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
                        System.err.println("Usage: matrixAdd <in> <out>");
                        System.exit(2);
                }

                Job job = new Job(conf, "matrix add");

                job.setJarByClass(MatAdd20180976.class);
                job.setMapperClass(MatrixAddMapper.class);
                job.setCombinerClass(MatrixAddReducer.class);
                job.setReducerClass(MatrixAddReducer.class);

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);

                FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

                FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);

                System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}
