package mobile.example.alarmtest;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends Activity {
	
	PendingIntent sender = null;
	AlarmManager alarmManager = null;

	Calendar calendar;
	public static final String CHANNEL_ID = "100";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_test);
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	}

	public void createNotificationChannel(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			CharSequence name = getString(R.string.channel_name);
			String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;

			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);

			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			// 채널 생성
			notificationManager.createNotificationChannel(channel);
		}
	}

	public void mOnClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {
		case R.id.onetime:
			// 예약에 의해 호출될 BR 지정
			intent = new Intent(this, MyBroadcastReceiver.class);
			sender = PendingIntent.getBroadcast(this, 0, intent, 0);

			// noti를 띄우기 위해서는 notification channel을 먼저 생성해줘야함.
			createNotificationChannel();

			// 알람 시간. 10초후
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis()); // RTC : 세계 표준시(UTC)를 기준으로 알람 시간 지정
			// calendar.setTimeInMillis(System.elapsedRealtime()); // 부팅 후 경과시간을 기준으로 알람 시간 지정
			calendar.add(Calendar.SECOND, 3);

			// 알람 등록
			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
			// AlarmManager.RTC_WAKEUP : sleep 상태에서 장비 깨움 (장비 활성화)
			// AlarmManager.ELAPSED_REALTIME_WAKEUP
			break;
		case R.id.repeat:
			intent = new Intent(this, RepeatReceiver.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

			// 알람시간 되면 액티비티 띄우기
			sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// 알람시간 되면 브로드캐스트 띄우기
			//sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//			60초당 한번 알람 등록 --> 최소 1분 정도로 반복을 설정하여야 함
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + 3000, 10000 * 6, sender);

//			정확도가 떨어지는 반복 알람 설정 시		
//			alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3000,
//					AlarmManager.INTERVAL_FIFTEEN_MINUTES, sender);
            break;
		case R.id.stop:
			intent = new Intent(this, RepeatReceiver.class);
			sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			if (sender != null) alarmManager.cancel(sender);
			break;
		case R.id.load_activity:
			intent = new Intent(this, SubActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

			// 알람시간 되면 액티비티 띄우기
			sender = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 5);

			// 알람 시간. 10초후
			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);

			break;
		}
	}





//	일정 시간 간격으로 작업을 반복하고자 할 때는 Handler 를 사용할 수도 있음
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.arg1 != 5) {
//                Toast.makeText(AlarmTestActivity.this, "Alarm!!! " + msg.arg1, Toast.LENGTH_SHORT).show();
//                Message newMsg = handler.obtainMessage();
//                newMsg.arg1 = msg.arg1 + 1;
//                this.sendMessageDelayed(newMsg, 3000);
//            }
//        }
//    };

}

