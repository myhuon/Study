package �⸻���_����1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class �⸻���_����1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num, point;
		String name;
		Map<String, Costomer> m = new HashMap<>();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("**����Ʈ ���� ���α׷��Դϴ�**");
		do {
			int sum = 0, flg = 0;
			System.out.print("1.�Է�	2.����	3.���	4.����	������ ��ȣ��>>");
			num = sc.nextInt();
		
			switch(num) {
				case 1:
					System.out.print("�̸��� ����Ʈ �Է�>>");
					name = sc.next();
					point = sc.nextInt();
					
					for(Map.Entry<String, Costomer> s : m.entrySet()) {
						if(name.equals(s.getKey())) {//���� �̸��� �ִ��� �Ǻ��ϱ�
							sum = Integer.parseInt(s.getValue().toString());//Costomer Ÿ���� value�� String���� ��ȯ�� �� intŸ������ ��ȯ�Ѵ�
							sum += point;//������Ű��
							flg = 1;
							break;
						}
					}
					if(flg == 0)//���� �̸��� ������ hashMap�� ����
						m.put(name, new Costomer(point));
					else//���� �̸��� ������ ����
						m.put(name, new Costomer(sum));
					break;
				case 2:
					System.out.print("�����Ϸ��� �̸���>>");
					name = sc.next();
					flg = search(name, m);
					if(flg == 1) {
						m.remove(name);
						System.out.println(name + "��(��) �����Ǿ����ϴ�.");
					}
					else
						System.out.println(name + "��(��) ��ϵ��� ���� ����Դϴ�.");
					break;
				case 3:
					Set<Map.Entry<String, Costomer>> entrySet = m.entrySet();
					Iterator<Map.Entry<String, Costomer>> entryIterator = entrySet.iterator();
					
					while(entryIterator.hasNext()) {
						Map.Entry<String, Costomer> entry = entryIterator.next();
						String key = entry.getKey();
						Costomer value = entry.getValue();
						System.out.print( "(" + key + "," + value + ")" + " ");
					}
					System.out.println();
					break;
				case 4:
					System.out.println("�����Ͽ����ϴ�.");
					break;
			}
		}while(num != 4);
		
		sc.close();
	}
	public static int search(String name, Map<String, Costomer> m) {
		int flg = 0;
		
		for(Map.Entry<String, Costomer> s : m.entrySet()) {
			if(name.equals(s.getKey())) {
				flg = 1;
				break;
			}
		}
		return flg;
	}
}
