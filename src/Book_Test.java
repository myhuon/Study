import java.util.Scanner;

public class Book_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num;
		int cnt = 0;
		String title, author;
		
		System.out.print("å�� �Ǽ�>>");
		Scanner sc = new Scanner(System.in);
		num = sc.nextInt();
		sc.nextLine();
		
		Book[] bk = new Book[num];
		
		for(int i = 0; i < num; i++) {		
			bk[i] = new Book();
			
			System.out.print("����>>");			
			title = sc.nextLine();
			System.out.print("����>>");
			author = sc.nextLine();
			
			bk[i].setTitle(title);
			bk[i].setAuthor(author);
		}
		for(Book obj: bk) {
			System.out.println(obj);
		}
		
		System.out.print("ã������ å�� ������>>");
		title = sc.nextLine();
		
		for(int i = 0; i < num; i++) {
			if(title.equals(bk[i].title)) {
				System.out.println("���ڴ� : "+bk[i].getAuthor());
				break;
			}
			cnt++;
		}
		if(cnt == num) {
			System.out.println("ã������ å�� �����ϴ�.");
		}
	}

}
