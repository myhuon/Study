package mobile.example.alarmtest;

import android.app.AlarmManager;
import android.content.*;
import android.widget.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RepeatReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Hi all!", Toast.LENGTH_SHORT).show();

		// notification 생성

	}
}
