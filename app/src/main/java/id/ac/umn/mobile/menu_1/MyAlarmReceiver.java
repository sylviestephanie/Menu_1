package id.ac.umn.mobile.menu_1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;


/**
 * Created by abdelhaq on 01-Dec-16.
 */

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("abdel","diterima");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar firingCal= Calendar.getInstance();

            firingCal.set(Calendar.HOUR_OF_DAY, 10); // At the hour you wanna fire
            firingCal.set(Calendar.MINUTE, 00); // Particular minute
            firingCal.set(Calendar.SECOND, 0); // particular second

            Long intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,intendedTime,1000 * 60 * 60,broadcast);

            SharedPreferences.Editor prefEdit= context.getSharedPreferences("LOGIN_PREFERENCES",context.MODE_PRIVATE).edit();
            prefEdit.putBoolean("ALARMSET", true);
            prefEdit.commit();
        }
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("Hello there!")
                .setContentText("Dont forget to study hard today :)")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 }).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
