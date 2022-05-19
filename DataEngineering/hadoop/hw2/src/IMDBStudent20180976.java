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
	public class MovieRating {
		public int userId;
		public int movieId;
		public double rating;
		public String title;

		public MovieRating(int _userId, int _movieId, double _rating, String _title) 
		{
			this.userId = _userId;
			this.movieId = _movieId;
			this.rating = _rating;
			this.title = _title;
		}

		public MovieRating(String title, double rating)
		{
			this.rating = rating;
			this.title = title;
		}


		public String getString()	// use in map output
		{
			return title + " " + rating.toString();
		}
	}


	public static class RatingComparator implements Comparator<MovieRating> 
	{
		public int compare(MovieRating x, MovieRating y) 
		{
			if ( x.rating > y.rating ) return 1;
			if ( x.rating < y.rating ) return -1;
			
			return 0;
		}
	}

	public static void insertMovieRating(PriorityQueue q, String title, double rating, int topK) 
	{
		MovieRating mv_head = (MovieRating) q.peek();
		if ( q.size() < topK || mv_head.rating < rating )
		{
			MovieRating mv = new MovieRating(title, rating);
			q.add( mv );
			if( q.size() > topK ) q.remove();
		}
	}

	public static class ReduceSideJoinMapper extends Mapper<LongWritable, Text, Text, Text>		// join map
	{
		boolean fileA = true;

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		{
		    //Write map code
		   String line = value.toString();
		   StringTokenizer itr = new StringTokenizer(line, "::");
		   Text outputKey = new Text();
		   Text outputValue = new Text();
		   String joinKey = "";
		   String o_value = "";
		   if(fileA){
		  	String movieId = itr.nextToken();
			String title = itr.nextToken();
			joinKey = movieId;
			o_value = "A," + title;
		   } 
		   else{
			String userId = itr.nextToken();
			String movieId = itr.nextToken();
			String rating = itr.nextToken();
		  	joinKey = movieId;
			o_value = "B," + rating;
		   }

		   outputKey.set(joinKey);
		   outputValue.set(o_value);
		   context.write(outputKey, outputValue);
		}

		protected void setup(Context context) throws IOException, InterruptedException
		{
			String filename = ((FileSplit)context.getInputSplit()).getPath().getName();

			if(filename.indexOf("movies.txt") != -1) fileA = true;
			else fileA = false;
		}
	}

	public static class ReduceSideJoinReducer extends Reducer<Text,Text,Text,Text>		// join reduce
	{
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
		{
			Text reduce_key = new Text();
			Text reduce_result = new Text();
			String rating = "";
			int rating; 
			
			double sum = 0;
			int num = 0;
			for (Text val : values) {
				String file_type;
				StringTokenizer itr = new StringTokenizer(val.toString(), ",");
				file_type = itr.nextToken();
				
				if( file_type.equals( "A" ) ) {
					title = itr.nextToken();
				}
				else {
					rating = Integer.parseInt(itr.nextToken());
					sum += rating;	
					num++;
				}
			}
			double avg = sum / num;
			reduce_key.set(title);
			reduce_result.set(avg.toString());

			context.write(reduce_key, reduce_result);
		}
	}

	public static class TopKMapper extends Mapper<Text, Text, Text, NullWritable>		// topK map 
	{
		private PriorityQueue<Emp> queue ;
		private Comparator<MovieRating> comp = new RatingComparator();
		private int topK;

		public void map(Text key, Text value, Context context) throws IOException, InterruptedException 
		{
			//StringTokenizer itr = new StringTokenizer(value.toString(), "::");
			//int emp_id = Integer.parseInt( itr.nextToken().trim());
			//String dept_id = itr.nextToken().trim();
			//int salary = Integer.parseInt( itr.nextToken().trim() );
			//String emp_info = itr.nextToken().trim();
			String title = key.toString();
			double avgRating = Double.parseDouble(value.toString());

			insertMovieRating(queue, title, avgRating, topK);
		}
	
		protected void setup(Context context) throws IOException, InterruptedException 
		{
			Configuration conf = context.getConfiguration();
			topK = conf.getInt("topK", -1);
			queue = new PriorityQueue<MovieRating>( topK , comp);
		}
		
		protected void cleanup(Context context) throws IOException, InterruptedException 
		{
			while( queue.size() != 0 ) 
			{
				MovieRating mv = (MovieRating) queue.remove();
				context.write( new Text( mv.getString() ), NullWritable.get() );
			}
		}
	}

	public static class TopKReducer extends Reducer<Text,NullWritable,Text,NullWritable> 		// topK reduce
	{
		private PriorityQueue<MovieRating> queue ;
		private Comparator<MovieRating> comp = new RatingComparator();
		private int topK;

		public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
		{
			StringTokenizer itr = new StringTokenizer(key.toString(), " ");
			//int emp_id = Integer.parseInt( itr.nextToken().trim());
			//String dept_id = itr.nextToken().trim();
			//int salary = Integer.parseInt( itr.nextToken().trim() );
			//String emp_info = itr.nextToken().trim();
			String title = itr.nextToken();
			double avgRating = Double.parseDouble(itr.nextToken());

			insertMovieRating(queue, title, avgRating, topK);
		}

		protected void setup(Context context) throws IOException, InterruptedException 
		{
			Configuration conf = context.getConfiguration();
			topK = conf.getInt("topK", -1);
			queue = new PriorityQueue<MovieRating>( topK , comp);
		}

		protected void cleanup(Context context) throws IOException, InterruptedException 
		{
			while( queue.size() != 0 ) 
			{
				MovieRating mv = (MovieRating) queue.remove();
				context.write( new Text( mv.getString() ), NullWritable.get() );
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		int topK;

		if (otherArgs.length != 2) {
			System.err.println("Usage: TopK <in> <out>"); System.exit(2);
		}

		Job job = new Job(conf, "Join");
		job.setJarByClass(IMDBStudent20180976.class);
		job.setMapperClass(ReduceSideJoinMapper.class);
		job.setReducerClass(ReduceSideJoinReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path("output_join"));
		FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);
		job.waitForCompletion(true);

		Job job2 = new Job(conf, "TopK");
		job2.setJarByClass(IMDBStudent20180976.class);
		job2.setMapperClass(TopKMapper.class);
		job2.setReducerClass(TopKReducer.class);
		job2.setNumOfReduce(1);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(NullWritable.class);
		FileInputFormat.addInputPath(job2, new Path("output_join"));
		FileOutputFormat.setOutputPath(job2, new Path(otherArgs[1]));
		topK = Integer.parseInt(otherArgs[2]);		// topK setting
		conf.setInt("topK", topK);

		FileSystem.get(job2.getConfiguration()).delete( new Path(otherArgs[1]), true);
		System.exit(job2.waitForCompletion(true) ? 0 : 1);
	}
}

