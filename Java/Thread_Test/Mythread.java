
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
				sleep((int)(Math.random() * 2));//난수 시간만큼 중지
			}catch(InterruptedException e) {
				
			}
			i++;
		}
	}
}
