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

public class YouTubeStudent20180976
{
	public static class YouTubeRating{
                public String category;
                public double rating;

                public YouTubeRating(String category, double rating)
                {
                        this.rating = rating;
                        this.category = category;
                }

                public String getCategory()
                {
                        return category;
                }

                public double getRating()
                {
                        return rating;
                }


                public String getString()       // use in map output
                {
                        return category + "|" + Double.toString(rating);
                }
	}

	// category & rating  
        public static class YouTubeMapper extends Mapper<LongWritable, Text, Text, Text>         
        {
                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    
			String line = value.toString();
                   	StringTokenizer itr = new StringTokenizer(line, "|");
                   	
			String category = "";
			String rating = "";
			String idle = "";
			for(int i = 0; i < 7; i++){
				if(i == 3) category = itr.nextToken();
				else if(i == 6) rating = itr.nextToken(); 
				else idle = itr.nextToken();
			}

                   	context.write(new Text(category), new Text(rating));
		}

        }

        public static class YouTubeReducer extends Reducer<Text,Text,NullWritable,Text>  
        {
                public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
                {
                        double sum = 0;
                        int num = 0;
		
			for (Text value : values) {
                                sum += Double.parseDouble(value.toString());
				num++;
                        }

                       	double avg = sum / num;
                       	String result = key.toString() + "|" + Double.toString(avg);

                       	context.write(NullWritable.get(), new Text(result));
                }
        }

	// topK
        public static class RatingComparator implements Comparator<YouTubeRating>
        {
                public int compare(YouTubeRating x, YouTubeRating y)
                {
                        if ( x.rating > y.rating ) return 1;
                        if ( x.rating < y.rating ) return -1;

                        return 0;
                }
        }

        public static void insertYouTubeRating(PriorityQueue q, String category, double rating, int topK)
        {
                YouTubeRating mv_head = (YouTubeRating) q.peek();
                if ( q.size() < topK || mv_head.rating < rating )
                {
                        YouTubeRating yt = new YouTubeRating(category, rating);
                        q.add( yt );
                        if( q.size() > topK ) q.remove();
                }
        }

        public static class TopKMapper extends Mapper<Object, Text, Text, NullWritable>           // topK map 
        {
                private PriorityQueue<YouTubeRating> queue ;
                private Comparator<YouTubeRating> comp = new RatingComparator();
               	int topK;
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
                {
			StringTokenizer itr = new StringTokenizer(value.toString(), "|");
                        String category = itr.nextToken();
                        double avgRating = Double.parseDouble(itr.nextToken());

                        insertYouTubeRating(queue, category, avgRating, topK);
                }

                protected void setup(Context context) throws IOException, InterruptedException
                {
                        Configuration conf = context.getConfiguration();
                        topK = conf.getInt("topK", -1);
                        queue = new PriorityQueue<YouTubeRating>( topK , comp);
                }

                protected void cleanup(Context context) throws IOException, InterruptedException
                {
			while( queue.size() != 0 )
                        {
                                YouTubeRating yt = (YouTubeRating) queue.remove();
				context.write(new Text(yt.getString()), NullWritable.get());
                        }
                }
        }

        public static class TopKReducer extends Reducer<Text,NullWritable,Text,Text>            // topK reduce
        {
                private PriorityQueue<YouTubeRating> queue ;
                private Comparator<YouTubeRating> comp = new RatingComparator();
                private int topK;
	
                public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
                {
			StringTokenizer itr = new StringTokenizer(key.toString(), "|");
                        String title = itr.nextToken().trim();
                        double avgRating = Double.parseDouble(itr.nextToken().trim());

                        insertYouTubeRating(queue, title, avgRating, topK);
                }

                protected void setup(Context context) throws IOException, InterruptedException
                {
                        Configuration conf = context.getConfiguration();
                        topK = conf.getInt("topK", -1);
                        queue = new PriorityQueue<YouTubeRating>( topK , comp);
                }

                protected void cleanup(Context context) throws IOException, InterruptedException
                {
                        while( queue.size() != 0 )
                        {
                                YouTubeRating yt = (YouTubeRating) queue.remove();
				context.write(new Text(yt.getCategory()), new Text(Double.toString(yt.getRating())));
                        }
                }
	}

        public static void main(String[] args) throws Exception
        {
                Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                
                String firstOutput = "youtube_first";		    // YouTubeReducer output fileName
		int topK = Integer.parseInt(otherArgs[2]);          // topK setting
                conf.setInt("topK", topK);

                if (otherArgs.length != 3) {
                        System.err.println("Usage: YouTubeStudent20180976 <in> <out> <topK>"); System.exit(2);
                }

                Job job = new Job(conf, "YouTUbe");
                job.setJarByClass(YouTubeStudent20180976.class);
                job.setMapperClass(YouTubeMapper.class);
                job.setReducerClass(YouTubeReducer.class);
		job.setOutputKeyClass(NullWritable.class);
                job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileOutputFormat.setOutputPath(job, new Path(firstOutput));
                FileSystem.get(job.getConfiguration()).delete( new Path(firstOutput), true);
                job.waitForCompletion(true);

                Job job2 = new Job(conf, "TopK");
                job2.setJarByClass(YouTubeStudent20180976.class);
                job2.setMapperClass(TopKMapper.class);
                job2.setReducerClass(TopKReducer.class);
                job2.setNumReduceTasks(1);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(NullWritable.class);
                job2.setOutputKeyClass(Text.class);
                job2.setOutputValueClass(Text.class);
                FileInputFormat.addInputPath(job2, new Path(firstOutput));
                FileOutputFormat.setOutputPath(job2, new Path(otherArgs[1]));

                FileSystem.get(job2.getConfiguration()).delete( new Path(otherArgs[1]), true);
                System.exit(job2.waitForCompletion(true) ? 0 : 1);
        }
}

