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

	public static class MovieRating {
                public String userId;
                public String movieId;
                public double rating;
                public String title;

                public MovieRating(String _userId, String _movieId, double _rating, String _title)
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

                public String getTitle()
                {
                        return title;
                }

                public double getRating()
                {
                        return rating;
                }


                public String getString()       // use in map output
                {
                        return title + "::" + Double.toString(rating);
                }
	}

	// join
        public static class ReduceSideJoinMapper extends Mapper<LongWritable, Text, Text, Text>         // join map
        {
                boolean fileA = true;

                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    
			String line = value.toString();
                   	StringTokenizer itr = new StringTokenizer(line, "::");
                   	
			Text outputKey = new Text();
                   	Text outputValue = new Text();
                  
			String joinKey = "";
                   	String o_value = "";
			String genres = "";
                   	if(fileA){
                        	String movieId = itr.nextToken();
                        	String title = itr.nextToken();
				genres = itr.nextToken();
				joinKey = movieId;
                        	o_value = "A::" + title + "::" + genres;
                   	}
                   	else {
                        	String userId = itr.nextToken();
                        	String movieId= itr.nextToken();
                        	String rating = itr.nextToken();
				joinKey = movieId;
                        	o_value = "B::" + rating;
                   	}


                   	outputKey.set(joinKey);
                   	outputValue.set(o_value);
                   	context.write(outputKey, outputValue);

		}

                protected void setup(Context context) throws IOException, InterruptedException
                {
                        String filename = ((FileSplit)context.getInputSplit()).getPath().getName();

                        if(filename.indexOf("movies.dat") != -1) fileA = true;
                        else fileA = false;
                }
        }

        public static class ReduceSideJoinReducer extends Reducer<Text,Text,Text,Text>  // join reduce
        {
                public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
                {
                        Text reduce_key = new Text();
                        Text reduce_result = new Text();
                        
			int rating = 0;
                        String file_type = "";
			String title = "";
			
			String fileA = "A";
			String fileB = "B";

                        double sum = 0;
                        int num = 0;
			boolean isFantasy = false;
                        for (Text val : values) {
                                StringTokenizer itr = new StringTokenizer(val.toString(), "::");
                                file_type = itr.nextToken();

                               if( file_type.equals(fileA) ) {
                                        title = itr.nextToken();
					String genres = itr.nextToken();
					StringTokenizer itr2 = new StringTokenizer(genres, "|");
			
					while(itr2.hasMoreTokens()){
						String genre = itr2.nextToken().toUpperCase();
						if(genre.equals("FANTASY")){
							isFantasy = true;
						}
					}
                                }
                                else{
                                        rating = Integer.parseInt(itr.nextToken());
                                        sum += rating;
                                        num++;
                                }
                        }
			
			if(isFantasy && num != 0){
                        	double avg = sum / num;
                        	String result = "::" + Double.toString(avg);
                      	  	reduce_result.set(result);
				reduce_key.set(title);

                        	context.write(reduce_key, reduce_result);
			}
                }
        }

	// topK
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

        public static class TopKMapper extends Mapper<Object, Text, Text, NullWritable>           // topK map 
        {
                private PriorityQueue<MovieRating> queue ;
                private Comparator<MovieRating> comp = new RatingComparator();
               	int topK;
		
		private Text topk_result_key;
		private	Text topk_result_value;
                
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
                {
			StringTokenizer itr = new StringTokenizer(value.toString(), "::");
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
				context.write(new Text(mv.getString()), NullWritable.get());
                        }
                }
        }

        public static class TopKReducer extends Reducer<Text,NullWritable,Text,Text>            // topK reduce
        {
                private PriorityQueue<MovieRating> queue ;
                private Comparator<MovieRating> comp = new RatingComparator();
                private int topK;
	
		private Text topk_result_key;
		private	Text topk_result_value;

                public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
                {
			StringTokenizer itr = new StringTokenizer(key.toString(), "::");
                        String title = itr.nextToken().trim();
                        double avgRating = Double.parseDouble(itr.nextToken().trim());

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
				context.write(new Text(mv.getTitle()), new Text(Double.toString(mv.getRating())));
                        }
                }
	}

        public static void main(String[] args) throws Exception
        {
                Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                
                String firstOutput = "output_join";		    // ReduceSideJoinReducer output fileName
		int topK = Integer.parseInt(otherArgs[2]);          // topK setting
                conf.setInt("topK", topK);

                if (otherArgs.length != 3) {
                        System.err.println("Usage: IMDBStudent20180976 <in> <out> <topK>"); System.exit(2);
                }

                Job job = new Job(conf, "IMDB");
                job.setJarByClass(IMDBStudent20180976.class);
                job.setMapperClass(ReduceSideJoinMapper.class);
                job.setReducerClass(ReduceSideJoinReducer.class);
                //job.setNumReduceTasks(0);
		job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
                FileOutputFormat.setOutputPath(job, new Path(firstOutput));
                FileSystem.get(job.getConfiguration()).delete( new Path(firstOutput), true);
                job.waitForCompletion(true);

                Job job2 = new Job(conf, "TopK");
                job2.setJarByClass(IMDBStudent20180976.class);
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

