package com.bts.adamcrm.receiver;

import static android.media.AudioAttributes.USAGE_NOTIFICATION;
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

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.bts.adamcrm.R;
import com.bts.adamcrm.activity.MainActivity;

public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(PARTIAL_WAKE_LOCK, "");
        newWakeLock.acquire(10*60*1000L /*10 minutes*/);
        newWakeLock.release();
        showNotificationMessage(context, "TCRM Reminder", "please check your application for reminders.");
    }

    private void showNotificationMessage(Context context, String strTile, String strtext) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String string = context.getResources().getString(R.string.default_notification_channel_id);
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(string, "Channel Name", 4);
            notificationChannel.setSound(defaultUri, new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION).build());
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder contentIntent = new NotificationCompat.Builder(context, string).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setSound(defaultUri).setContentTitle(strTile).setContentText(strtext).setDefaults(5).setContentIntent(PendingIntent.getActivity(context, 0, intent, 134217728));
        TaskStackBuilder create = TaskStackBuilder.create(context);
        create.addNextIntent(intent);
        contentIntent.setContentIntent(create.getPendingIntent(0, 134217728));
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(strTile);
        bigTextStyle.bigText(strtext);
        contentIntent.setStyle(bigTextStyle);
        notificationManager.notify((int) System.currentTimeMillis(), contentIntent.build());
    }
}
