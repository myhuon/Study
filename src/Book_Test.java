import java.util.Scanner;

public class Book_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num;
		int cnt = 0;
		String title, author;
		
		System.out.print("책의 권수>>");
		Scanner sc = new Scanner(System.in);
		num = sc.nextInt();
		sc.nextLine();
		
		Book[] bk = new Book[num];
		
		for(int i = 0; i < num; i++) {		
			bk[i] = new Book();
			
			System.out.print("제목>>");			
			title = sc.nextLine();
			System.out.print("저자>>");
			author = sc.nextLine();
			
			bk[i].setTitle(title);
			bk[i].setAuthor(author);
		}
		for(Book obj: bk) {
			System.out.println(obj);
		}
		
		System.out.print("찾으려는 책의 제목은>>");
		title = sc.nextLine();
		
		for(int i = 0; i < num; i++) {
			if(title.equals(bk[i].title)) {
				System.out.println("저자는 : "+bk[i].getAuthor());
				break;
			}
			cnt++;
		}
		if(cnt == num) {
			System.out.println("찾으려는 책이 없습니다.");
		}
	}

}
