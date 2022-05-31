import java.util.*;

public class PhoneMain{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int choseN;
		
		selectN s = new selectN();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("========= 연락처 리스트 관리 ==========");
			System.out.println("1. 번호 추가");
			System.out.println("2. 검색");
			System.out.println("3. 번호 변경");
			System.out.println("4. 번호 삭제");
			System.out.println("5. 종료");
			System.out.println("================================");
			System.out.print("메뉴를 선택하세요 : ");
			
			choseN = sc.nextInt();
			
			if(s.select(choseN) == 0) break;
		}
	}
}
