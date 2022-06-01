package mobile.example.alarmtest;

import static mobile.example.alarmtest.MainActivity.CHANNEL_ID;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Build;
import android.util.Log;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "MyBroadcastReceiver";

    public static final String CHANNEL_ID = "100";

    // context : 알람 받은 activity, intent : ?
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "one time!", Toast.LENGTH_LONG).show();




		// 알람 받으면 activity 띄우기
        Intent i = new Intent(context, SubActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
		try {
			pendingIntent.send();
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
		}

		// Notification 출력
		/*NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_stat_name)
				.setContentTitle("기상시간")
				.setContentText("일어나!공부할 시간이야!")
               // .setContentIntent(pendingIntent)
				.setAutoCancel(true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());*/


		// context 속성 보기
		StringBuilder sb = new StringBuilder();
		sb.append("Action: " + intent.getAction() + "\n");
		sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
		String log = sb.toString();
		Log.d(TAG, log);
		Toast.makeText(context, log, Toast.LENGTH_SHORT).show();
	}


}