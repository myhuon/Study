import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.util.StringTokenizer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Set;

public final class MatrixComputation{

    public static void main(String[] args) throws Exception {

        if (args.length < 6) {
            System.err.println("Usage: MatrixComputation <matrix A> <matrix B> <m> <k> <n> <output-file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession
            .builder()
            .appName("MatrixComputation")
            .getOrCreate();

	
	JavaRDD<String> mat1 = spark.read().textFile(args[0]).javaRDD();
	JavaRDD<String> mat2 = spark.read().textFile(args[1]).javaRDD();
	int m = Integer.parseInt(args[2]);
	int k = Integer.parseInt(args[3]);
	int n = Integer.parseInt(args[4]);


	JavaPairRDD<String, Integer> m1elements = mat1.flatMapToPair(new PairFlatMapFunction<String, String, Integer>() {
		public Iterator<Tuple2<String, Integer>> call(String s) {
			// return할 element들을 담을 ArrayList 만들기
			List<Tuple2<String, Integer>> arr = new ArrayList<Tuple2<String, Integer>>();
			for(int i = 0; i < n; i++) { 	// mat2's column num = n
				// matrix_a에 맞는 적절한 index 만들어서 ArrayList에 add 하기
				// ArrayList의 iterator를 return
				StringTokenizer st = new StringTokenizer(s, ",");
				String row = st.nextToken();
				String column = st.nextToken();
				String value = st.nextToken();
				
				String joinKey = row + "|" + i + "|" + column;
                        	Tuple2<String, Integer> tuple = new Tuple2<String, Integer>(joinKey, Integer.parseInt(value));
				arr.add(tuple);
			}

			return arr.iterator();
		}
	});

	JavaPairRDD<String, Integer> m2elements = mat2.flatMapToPair(new PairFlatMapFunction<String, String, Integer>() {
		public Iterator<Tuple2<String, Integer>> call(String s) {
			// return할 element들을 담을 ArrayList 만들기
			List<Tuple2<String, Integer>> arr = new ArrayList<Tuple2<String, Integer>>();
			for (int i = 0; i < m; i++){	 // mat1's row num = m
				// matrix_b에 맞는 적절한 index 만들어서 ArrayList에 add 하기
				// ArrayList의 iterator를 return
				StringTokenizer st = new StringTokenizer(s, ",");
				String row = st.nextToken();
				String column = st.nextToken();
				String value = st.nextToken();
				
				String joinKey = row + "|" + i + "|" + column;
                        	Tuple2<String, Integer> tuple = new Tuple2<String, Integer>(joinKey, Integer.parseInt(value));
				arr.add(tuple);
			}

			return arr.iterator();

		}
	});


	// 두 JavaPairRDD를 하나의 JavaPairRDD로 합치기
	JavaPairRDD<String, Integer> elements = m1elements.union(m2elements);

	
	Function2<Integer, Integer, Integer> multipleFunc = new Function2<Integer, Integer, Integer>(){
		public Integer call(Integer x, Integer y){
			return x * y;
		}	
	};	
	JavaPairRDD<String, Integer> mul = elements.reduceByKey(multipleFunc);	


	JavaPairRDD<String, Integer> changeKey = mul.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {
		public Tuple2<String, Integer> call(Tuple2<String, Integer> tp) {
			// key를 새롭게 만들어서 return
			// tip. Tuple2에서 key는 Tuple2._1, value는 Tuple2._2를 사용하여 꺼낼 수 있음
			String key = tp._1;
			Integer value = tp._2;

			StringTokenizer st = new StringTokenizer(key, "|");
			String result_key = st.nextToken() + "|" + st.nextToken();

			Tuple2<String, Integer> result = new Tuple2<String, Integer>(result_key, value);

			return result;
		}
	});


	Function2<Integer, Integer, Integer> plusFunc = new Function2<Integer, Integer, Integer>(){
		public Integer call(Integer x, Integer y){
			return x + y;
		}	
	};	
	JavaPairRDD<String, Integer> rst = changeKey.reduceByKey(plusFunc);
	
	rst.saveAsTextFile(args[args.length - 1]);

        spark.stop();
    }
}
