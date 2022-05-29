import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.StringTokenizer;
import java.time.LocalDate;

public final class UBERStudent20180976 implements Serializable {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: JavaWordCount <in-file> <out-file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession
            .builder()
            .appName("UBER")
            .getOrCreate();

        JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

        FlatMapFunction<String, String> fmf = new FlatMapFunction<String, String>()
	{
		
		public Iterator<String> call(String s)
		{
		String[] dayOfWeek = {"MON", "TUE", "WED", "THR", "FRI", "SAT", "SUN"};
			Iterator<String> itr = Arrays.asList(s.split(",")).iterator();
			ArrayList<String> arr2 = new ArrayList<String>();
			String token = "";

			String id = "";
			String date = "";
			String vehicles = "";
			String trips = "";
			StringTokenizer st2;

			int month = 0;
			int day = 0;
			int year = 0;
			LocalDate ld;
			int dow;
			for(int i = 1; itr.hasNext(); i++)
			{
				token = itr.next();

				/*if(i % 1 == 0){
					id = token;	
				}
				else 
				
				*/
				if(i % 2 == 0){
					date = token;
					StringTokenizer st = new StringTokenizer(date, "/");
					month = Integer.parseInt(st.nextToken());
					day = Integer.parseInt(st.nextToken());
					year = Integer.parseInt(st.nextToken());

					ld = LocalDate.of(year, month, day);
					dow = ld.getDayOfWeek().getValue();
					token = dayOfWeek[dow-1];
					//token = Integer.toString(dow-1);
				}
				/*else if(i % 3 == 0)
				{
					vehicles = token;
				}
				else{
					trips = token;
				}*/

				if(i % 4 != 0){
					token += ",";
				}
				
				arr2.add(token);
			}

			return  arr2.iterator();
		}
	};
        JavaRDD<String> words = lines.flatMap(fmf);

	PairFunction<String, String, String> pf = new PairFunction<String, String, String>()
	{
		public Tuple2<String, String> call(String s)
		{
			StringTokenizer st = new StringTokenizer(s, ",");
			String id = st.nextToken();
			String date = st.nextToken();
			String trips = st.nextToken();
			String vehicles = st.nextToken();

			String result_key = id + "," + date;
			String result_value = vehicles + "," + trips;

			return new Tuple2(result_key, result_value);
		}
	};
	JavaPairRDD<String, String> ones = words.mapToPair(pf);

	Function2<String, String, String> f2 = new Function2<String, String, String>()
	{
		public String call(String x, String y)
		{
			StringTokenizer st = new StringTokenizer(x, ",");
			String vehicles = st.nextToken();
			String trips = st.nextToken();

			StringTokenizer st2 = new StringTokenizer(y, ",");
			String vehicles2 = st.nextToken();
			String trips2 = st.nextToken();

			int vSum = Integer.parseInt(vehicles) + Integer.parseInt(vehicles2);
			int tSum = Integer.parseInt(trips) + Integer.parseInt(trips);
			
			String result_value = Integer.toString(vSum) + "," + Integer.toString(tSum);
			return result_value;
		}
	};
	JavaPairRDD<String, String> counts = ones.reduceByKey(f2);

	words.saveAsTextFile(args[1]);

        spark.stop();
    }
}
