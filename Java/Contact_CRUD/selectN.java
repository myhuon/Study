
public class selectN extends Menu{
	int returnN = 1;
	
	public int select(int choseN) {
		switch(choseN) {
			case 1: 
				System.out.print("추가할 이름을 입력하세요 : ");
				name = sc.next();
				System.out.print("추가할 번호를 입력하세요 : ");
				phoneNumber = sc.next();
				addNumber(name, phoneNumber);
				break;
			case 2:
				System.out.print("검색할 이름을 입력하세요 : ");
				name = sc.next();
				search(name);
				break;
			case 3:
				System.out.print("번호를 변경할 이름을 입력하세요: ");
				name = sc.next();
				changeNumber(name);
				break;
			case 4:
				System.out.print("번호를 삭제할 이름을 입력하세요 : ");
				name = sc.next();
				removeNumber(name);
				break;
			case 5:
				System.out.print("전화번호부 사용을 종료합니다.");
				returnN = 0;
				break;
			default:
				System.out.println("번호를 다시 입력하세요.");
				break;
		}
		return returnN;
	}
}
