package 기말고사_과제1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class 기말고사_과제1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num, point;
		String name;
		Map<String, Costomer> m = new HashMap<>();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("**포인트 관리 프로그램입니다**");
		do {
			int sum = 0, flg = 0;
			System.out.print("1.입력	2.삭제	3.출력	4.종료	선택한 번호는>>");
			num = sc.nextInt();
		
			switch(num) {
				case 1:
					System.out.print("이름과 포인트 입력>>");
					name = sc.next();
					point = sc.nextInt();
					
					for(Map.Entry<String, Costomer> s : m.entrySet()) {
						if(name.equals(s.getKey())) {//같은 이름이 있는지 판별하기
							sum = Integer.parseInt(s.getValue().toString());//Costomer 타입인 value를 String으로 변환한 뒤 int타입으로 변환한다
							sum += point;//누적시키기
							flg = 1;
							break;
						}
					}
					if(flg == 0)//같은 이름이 없으면 hashMap에 저장
						m.put(name, new Costomer(point));
					else//같은 이름이 있으면 누적
						m.put(name, new Costomer(sum));
					break;
				case 2:
					System.out.print("삭제하려는 이름은>>");
					name = sc.next();
					flg = search(name, m);
					if(flg == 1) {
						m.remove(name);
						System.out.println(name + "은(는) 삭제되었습니다.");
					}
					else
						System.out.println(name + "은(는) 등록되지 않은 사람입니다.");
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
					System.out.println("종료하였습니다.");
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
