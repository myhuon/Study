
public class Thread_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Counter c = new Counter();
		
		new Mythread(c).start();
	}

}
