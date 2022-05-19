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
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.util.HashMap;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class UBERStudent20180976
{

        public static class UBERStudent20180976Mapper extends Mapper<LongWritable, Text, Text, Text>
        {
                private Text result = new Text();
                private Text word = new Text();
                private String[] dayOfWeek = {"MON", "TUE", "WED", "THR", "FRI", "SAT", "SUN"};

                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    //Write map code
                    String line = value.toString();
                    StringTokenizer itr = new StringTokenizer(line, ",");

                    String number = itr.nextToken().trim();
                    String d = itr.nextToken().trim();
                    String vehicles = itr.nextToken().trim();
                    String trips = itr.nextToken().trim();

                    StringTokenizer st = new StringTokenizer(d, "/");
                    int month = Integer.parseInt(st.nextToken());
                    int day = Integer.parseInt(st.nextToken());
                    int year = Integer.parseInt(st.nextToken());

                    LocalDate ld = LocalDate.of(year, month, day);

	            int dow = ld.getDayOfWeek().getValue();
		    
        	    String k = number + "," + dayOfWeek[dow-1];
                    word.set(k);
                    String v = trips + "," + vehicles;
                    result.set(v);

                    context.write(word, result);
                 }
        }

        public static class UBERStudent20180976Reducer extends Reducer<Text,Text,Text,Text>
        {
                private Text result = new Text();

public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
                {
                    //Write reduce code

                        String tNv;
                        int sumTrips = 0;
                        int sumVehicles = 0;
                        for(Text val : values){
                            StringTokenizer onePair = new StringTokenizer(val.toString(), ",");
                            sumTrips += Integer.parseInt(onePair.nextToken());
                            sumVehicles += Integer.parseInt(onePair.nextToken());
                    }

                    tNv = Integer.toString(sumTrips) + "," + Integer.toString(sumVehicles);

                    result.set(tNv);
                    context.write(key, result);
                }
        }

        public static void main(String[] args) throws Exception{
Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                if (otherArgs.length != 2)
                {
                        System.err.println("Usage: uber <in> <out>");
                        System.exit(2);
                }

                Job job = new Job(conf, "uber count");

                job.setJarByClass(UBERStudent20180976.class);
                job.setMapperClass(UBERStudent20180976Mapper.class);
                job.setCombinerClass(UBERStudent20180976Reducer.class);
                job.setReducerClass(UBERStudent20180976Reducer.class);

                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(Text.class);

                FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

                FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);

                System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}
