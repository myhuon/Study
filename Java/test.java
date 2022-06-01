import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date d = new Date();
		System.out.println(d);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdate = new SimpleDateFormat("yyyy년 MM월 dd일");
		String date = sdate.format(cal.getTime());
		System.out.println(date);
		
		Calendar cal1 = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String date1 = year+"년"+month+"월"+day+"일";
		System.out.println(date1);
		
		Date d1 = new Date();
		SimpleDateFormat sdate1 = new SimpleDateFormat("yyyy/MM/dd");
		String date2 = sdate1.format(d1);
		System.out.println(date2);
	}

}
