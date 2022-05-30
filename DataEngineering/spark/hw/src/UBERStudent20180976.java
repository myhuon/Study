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
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.DayOfWeek;

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
			String result="";
			for(int i = 1; itr.hasNext(); i++)
			{
				token = itr.next();

				if(i % 2 == 0 && i % 4 != 0){
					date = token;
					StringTokenizer st = new StringTokenizer(token.trim(), "/");
					month = Integer.parseInt(st.nextToken());
					day = Integer.parseInt(st.nextToken());
					year = Integer.parseInt(st.nextToken());

					ld = LocalDate.of(year, month, day);
					dow = ld.getDayOfWeek().getValue();
					token = dayOfWeek[dow-1];
				}

				result += token;

				if(i % 4 != 0){
					result += ",";
				}
				else{
					arr2.add(result);
				}
				
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
			String id = st.nextToken().trim();
			String date = st.nextToken().trim();
			String trips = st.nextToken().trim();
			String vehicles = st.nextToken().trim();

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
			String vehicles = st.nextToken().trim();
			String trips = st.nextToken().trim();
			
			StringTokenizer st2 = new StringTokenizer(y, ",");
			String vehicles2 = st2.nextToken().trim();
			String trips2 = st2.nextToken().trim();
		
			int vSum = Integer.parseInt(vehicles) + Integer.parseInt(vehicles2);
			int tSum = Integer.parseInt(trips) + Integer.parseInt(trips);
			
			String result_value = Integer.toString(vSum) + "," + Integer.toString(tSum);
			return result_value;
		}
	};
	JavaPairRDD<String, String> counts = ones.reduceByKey(f2);

	counts.saveAsTextFile(args[1]);

        spark.stop();
    }
}
