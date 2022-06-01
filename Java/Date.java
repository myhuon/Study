import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Date {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date d = new Date();
		System.out.println(d);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdate = new SimpleDateFormat("yyyy³â MM¿ù ddÀÏ");
		String date = sdate.format(cal.getTime());
		System.out.println(date);
		
		Calendar cal1 = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String date1 = year+"³â"+month+"¿ù"+day+"ÀÏ";
		System.out.println(date1);
		
		Date d1 = new Date();
		SimpleDateFormat sdate1 = new SimpleDateFormat("yyyy/MM/dd");
		String date2 = sdate1.format(d1);
		System.out.println(date2);
	}

}
