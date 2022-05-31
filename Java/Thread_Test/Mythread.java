
public class Mythread extends Thread{

	Counter sharedCounter;
	
	public Mythread(Counter c) {
		this.sharedCounter = c;
	}
	public void run() {
		int i = 0;
		while(i < 20000) {
			sharedCounter.increment();
			sharedCounter.decrement();
			if(i % 40 == 0)
				sharedCounter.printCounter();
			try {
				sleep((int)(Math.random() * 2));//���� �ð���ŭ ����
			}catch(InterruptedException e) {
				
			}
			i++;
		}
	}
}
