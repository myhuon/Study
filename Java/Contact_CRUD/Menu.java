import java.util.Scanner;

public class Menu extends Contact implements Listable{
	String reEnter;
	int index;
	static int cnt = 0;
	Contact phoneNum[] = new Contact[100];
	
	Scanner sc = new Scanner(System.in);
	
	public Menu() {}
	public Menu(String name, String phoneNumber) {
		super(name, phoneNumber);
	}
	@Override
	public void list() {
		// TODO Auto-generated method stub
		System.out.println(list);
	}
	public int find (String name) {
		for(int i = 0; i < cnt; i++) 
			if(name.compareTo(phoneNum[i].getName()) == 0) return i;
		return -1;
	}
	public void addNumber(String name, String phoneNumber) {
		phoneNum[cnt] = new Contact();
		phoneNum[cnt].setName(name);
		phoneNum[cnt].setPhoneNumber(phoneNumber);
		cnt ++;
	}
	public void search(String name) {
		index = find(name);
		if( index >= 0)
			System.out.println(phoneNum[index].getName()+"의 번호는 "+phoneNum[index].getPhoneNumber()+"입니다.");
		else 
			System.out.println("전화번호부에 없는 사람입니다.");
	}
	public void changeNumber(String name) {
		index = find(name);
		if( index >= 0) {
			System.out.print("변경할 번호를 입력해주세요 : ");
			reEnter = sc.next();
			phoneNum[index].setPhoneNumber(reEnter);
		}
		else
			System.out.println("전화번호부에 없는 사람입니다.");
	}
	public void removeNumber(String name) {
		index = find(name);
		if( index >= 0) {
			for(int i = cnt - 1; i > index; i--) {
				phoneNum[i - 1] = phoneNum[i]; 
			}
			cnt --;
			System.out.println("번호가 삭제되었습니다.");
		}
		else
			System.out.println("전화번호부에 없는 사람입니다.");
	}
}
