package FileReadWrite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileReadWrite {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		try(BufferedReader in = new BufferedReader(new FileReader("in.txt"));
				PrintWriter out = new PrintWriter(new FileWriter("out.txt"))){
			String line;
			int lineNumber = 1;
			while((line = in.readLine()) != null) {//한 줄 단위 입출력
				out.write(" " + lineNumber + ": ");
				out.println(line);
				lineNumber++;
			}
		}
	}
}
