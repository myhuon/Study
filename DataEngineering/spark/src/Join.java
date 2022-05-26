import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.util.StringTokenizer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class Join {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: JavaWordCount <in-file> <out-file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession
            .builder()
            .appName("JavaWordCount")
            .getOrCreate();

        JavaRDD<String> products = spark.read().textFile(args[0]).javaRDD();
	PairFunction<String, String, Product> pfA = new PairFunction<String, String, Product>(){
		public Tuple2<String, Product> call(String s){
			StringTokenizer st = new StringTokenizer(s, "|");
			String id = st.nextToken();
			String price = st.nextToken();
			String joinKey = st.nextToken();

			Product product = new Product(id, price, joinKey);
			Tuple2<String, Product> tuple = new Tuple2<String, Product>(joinKey, product);

			return tuple;
		}
	
	};
	JavaPairRDD<String, Product> pTuples = products.mapToPair(pfA);

        JavaRDD<String> codes = spark.read().textFile(args[1]).javaRDD();
	PairFunction<String, String, Code> pfB = new PairFunction<String, String, Code>(){
		public Tuple2<String, Code> call(String s){
			StringTokenizer st = new StringTokenizer(s, "|");
			String joinKey = st.nextToken();
			String description = st.nextToken();

			Code code = new Code(joinKey, description);
			Tuple2<String, Code> tuple = new Tuple2<String, Code>(joinKey, code);

			return tuple;
		}
	
	};
	JavaPairRDD<String, Code> cTuples = codes.mapToPair(pfB);
	
	JavaPairRDD<String, Tuple2<Product, Code>> joined = pTuples.join(cTuples);
	JavaPairRDD<String, Tuple2<Product, Optional<Code>>> leftOuterJoined = pTuples.leftOuterJoin(cTuples);
	JavaPairRDD<String, Tuple2<Optional<Product>, Code>> rightOuterJoined = pTuples.rightOuterJoin(cTuples);
	JavaPairRDD<String, Tuple2<Optional<Product>, Optional<Code>>> fullOuterJoined = pTuples.fullOuterJoin(cTuples);
        
		
	joined.saveAsTextFile(args[args.length - 1] + "_join");
	leftOuterJoined.saveAsTextFile(args[args.length - 1] + "_leftOuterJoin");
	rightOuterJoined.saveAsTextFile(args[args.length - 1] + "_rightOuterJoin");
	fullOuterJoined.saveAsTextFile(args[args.length - 1] + "_fullOuterJoin");

        spark.stop();
    }
}
