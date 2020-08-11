package com.example.githubuserapp.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.githubuserapp.R;
import com.example.githubuserapp.view.FavoriteUser;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private final int ID_REPEATING = 101;
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, ID_REPEATING);
    }

    public void setRepeatingReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null){

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            Toast.makeText(context, "Repeating alarm set up ", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Repeating Alarm canceled", Toast.LENGTH_SHORT).show();
        }

    }

    private void showNotification(Context context, int notifId){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent = new Intent(context, FavoriteUser.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_NAME)
                .setSmallIcon(R.drawable.baseline_schedule_black_48dp)
                .setContentTitle("Info Daftar User Favorite")
                .setContentText("Buruan lihat apa saja favorite user kamu")
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null){
            notificationManager.notify(notifId,notification);
        }
    }
}
