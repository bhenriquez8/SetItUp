package com.example.bjarne.setitup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by bjarne on 10/15/15.
 */
public class AlertReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        createNotification(context, "Some", "String");
    }

    public void createNotification(Context context, String msg, String msgText) {

        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setContentTitle(msg)
                        .setContentText(msgText)
                        .setSmallIcon(R.drawable.ghost)
                        .setTicker("Quiz Ready!")
                        .setWhen(System.currentTimeMillis());

        mBuilder.setContentIntent(notificIntent);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, mBuilder.build());
    }
}
