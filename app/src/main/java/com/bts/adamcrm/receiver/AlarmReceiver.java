package com.bts.adamcrm.receiver;

import static android.media.AudioAttributes.USAGE_NOTIFICATION;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.bts.adamcrm.R;
import com.bts.adamcrm.activity.MainActivity;

public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("junior", "intent : " + intent.getAction());
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(PARTIAL_WAKE_LOCK, "");
        newWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        newWakeLock.release();
        showNotificationMessage(context);
    }

    private void showNotificationMessage(Context context) {
        Log.e("junior", "showNotificationMessage");
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        String string = context.getResources().getString(R.string.default_notification_channel_id);
        Uri defaultUri = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(string, "T-CRM", importance);
            notificationChannel.setSound(defaultUri, new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION).build());
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder contentIntent = new NotificationCompat.Builder(context, string)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultUri)
                .setContentTitle("T-CRM Reminder")
                .setContentText("please check your application for reminders.")
                .setDefaults(5)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE ))
                .setVibrate(new long[]{800, 500, 600, 300});
        TaskStackBuilder create = TaskStackBuilder.create(context);
        create.addNextIntent(intent);
        contentIntent.setContentIntent(create.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE ));
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("T-CRM Reminder");
        bigTextStyle.bigText("please check your application for reminders.");
        contentIntent.setStyle(bigTextStyle);
        notificationManager.notify((int) System.currentTimeMillis(), contentIntent.build());
    }
}
