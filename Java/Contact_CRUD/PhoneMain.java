import java.util.*;

public class PhoneMain{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int choseN;
		
		selectN s = new selectN();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("========= ����ó ����Ʈ ���� ==========");
			System.out.println("1. ��ȣ �߰�");
			System.out.println("2. �˻�");
			System.out.println("3. ��ȣ ����");
			System.out.println("4. ��ȣ ����");
			System.out.println("5. ����");
			System.out.println("================================");
			System.out.print("�޴��� �����ϼ��� : ");
			
			choseN = sc.nextInt();
			
			if(s.select(choseN) == 0) break;
		}
	}
}
