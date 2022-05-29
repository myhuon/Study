import java.io.IOException;
import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.GenericOptionsParser;

public class ReduceSideJoin_CompositeKey 
{
	public static class DoubleString implements WritableComparable
	{
		String joinKey = new String();
		String tableName = new String();

		public DoubleString() {}
		public DoubleString( String _joinKey, String _tableName )
		{
			joinKey = _joinKey;
			tableName = _tableName;
		}

		public void readFields(DataInput in) throws IOException
		{
			joinKey = in.readUTF();
			tableName = in.readUTF();
		}

		public void write(DataOutput out) throws IOException 
		{
			out.writeUTF(joinKey);
			out.writeUTF(tableName);
		}

		public int compareTo(Object o1)
		{
			DoubleString o = (DoubleString) o1;
			int ret = joinKey.compareTo( o.joinKey );
	
			if (ret!=0) return ret;
			return -1*tableName.compareTo( o.tableName);
		}
	
		public String toString() 
		{
		       	return joinKey + " " + tableName; 
		}
	}


	public static class CompositeKeyComparator extends WritableComparator 
	{
		protected CompositeKeyComparator() 
		{
			super(DoubleString.class, true);
		}

		public int compare(WritableComparable w1, WritableComparable w2) 
		{
			DoubleString k1 = (DoubleString)w1;
			DoubleString k2 = (DoubleString)w2;
	
			int result = k1.joinKey.compareTo(k2.joinKey);
			if(0 == result) 
			{
				result = -1* k1.tableName.compareTo(k2.tableName);
			}
			
			return result;
		}
	}

	public static class FirstPartitioner extends Partitioner<DoubleString, Text>
	{
		public int getPartition(DoubleString key, Text value, int numPartition)
		{
			return key.joinKey.hashCode()%numPartition;
		}
	}

	public static class FirstGroupingComparator extends WritableComparator 
	{
		protected FirstGroupingComparator()
		{
			super(DoubleString.class, true);
		}

		public int compare(WritableComparable w1, WritableComparable w2) 
		{
			DoubleString k1 = (DoubleString)w1;
			DoubleString k2 = (DoubleString)w2;

			return k1.joinKey.compareTo(k2.joinKey);
		}
	}


	// map 
        public static class ReduceSideJoin2Mapper extends Mapper<LongWritable, Text, DoubleString, Text>         
        {
                boolean fileA = true;

                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
                {
                    //Write map code
                   String line = value.toString();
                   StringTokenizer itr = new StringTokenizer(line, "|");
                   Text outputKey = new Text();
                   Text outputValue = new Text();
                   String joinKey = "";
                   String o_value = "";
                   
		   DoubleString result_key;
		   if(fileA){
                 	String id = itr.nextToken();
                        String price = itr.nextToken();
                        joinKey = itr.nextToken();
                        o_value = id + "," + price;

			result_key = new DoubleString(joinKey, "A");
                   }
                   else{
                        joinKey = itr.nextToken();
                        String description = itr.nextToken();
                        o_value = description;

			result_key = new DoubleString(joinKey, "B");
                   }

                   outputKey.set(result_key.toString());
                   outputValue.set(o_value);
                   context.write(result_key, outputValue);
                }

                protected void setup(Context context) throws IOException, InterruptedException
                {
			String filename = ((FileSplit)context.getInputSplit()).getPath().getName();

                        if(filename.indexOf("relation_a") != -1) fileA = true;
                        else fileA = false;
                }
        }

	// reduce
        public static class ReduceSideJoin2Reducer extends Reducer<DoubleString,Text,Text,Text>
        {
                public void reduce(DoubleString key, Iterable<Text> values, Context context) throws IOException, InterruptedException
                {
                        Text reduce_key = new Text();
                        Text reduce_result = new Text();
                        String description = "";
			DoubleString ds = new DoubleString();


			boolean isFirst = true;
			for (Text val : values) {                                
				if(isFirst){
                                        description = val.toString();
                                	isFirst = false;
				}
                                else {
                                	StringTokenizer itr = new StringTokenizer(val.toString(), ",");
                                	
					String id = itr.nextToken();
                                        String price = itr.nextToken();

                                        String result = price + " " + description;

                                        reduce_key.set(id);
                                        reduce_result.set(result);
                                        context.write(reduce_key, reduce_result);
                                }
                        }
		}

        }

        public static void main(String[] args) throws Exception
        {
                Configuration conf = new Configuration();
                String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
                
                if (otherArgs.length != 2) {
                        System.err.println("Usage: ReduceSideJoin_CompositeKey <in> <out> "); System.exit(2);
                }

                Job job = new Job(conf, "Composite");
                job.setJarByClass(ReduceSideJoin_CompositeKey.class);
		job.setMapperClass(ReduceSideJoin2Mapper.class);
		job.setReducerClass(ReduceSideJoin2Reducer.class);
		//job.setNumReduceTasks(0);
	
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
	
		job.setMapOutputKeyClass(DoubleString.class); 	// map output key class 
		job.setMapOutputValueClass(Text.class);
	
		job.setPartitionerClass(FirstPartitioner.class);	// partitioner
		job.setGroupingComparatorClass(FirstGroupingComparator.class);		// grouping
		job.setSortComparatorClass(CompositeKeyComparator.class);	// sort comparator
	
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}

