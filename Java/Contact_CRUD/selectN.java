
public class selectN extends Menu{
	int returnN = 1;
	
	public int select(int choseN) {
		switch(choseN) {
			case 1: 
				System.out.print("�߰��� �̸��� �Է��ϼ��� : ");
				name = sc.next();
				System.out.print("�߰��� ��ȣ�� �Է��ϼ��� : ");
				phoneNumber = sc.next();
				addNumber(name, phoneNumber);
				break;
			case 2:
				System.out.print("�˻��� �̸��� �Է��ϼ��� : ");
				name = sc.next();
				search(name);
				break;
			case 3:
				System.out.print("��ȣ�� ������ �̸��� �Է��ϼ���: ");
				name = sc.next();
				changeNumber(name);
				break;
			case 4:
				System.out.print("��ȣ�� ������ �̸��� �Է��ϼ��� : ");
				name = sc.next();
				removeNumber(name);
				break;
			case 5:
				System.out.print("��ȭ��ȣ�� ����� �����մϴ�.");
				returnN = 0;
				break;
			default:
				System.out.println("��ȣ�� �ٽ� �Է��ϼ���.");
				break;
		}
		return returnN;
	}
}
