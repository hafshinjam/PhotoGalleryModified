package org.maktab.photogallery.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import org.maktab.photogallery.utilities.Services;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "PGNotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received boot broadcast intent: " + intent.getAction());
        //check custom permission

        Notification notification = intent.getParcelableExtra(Services.EXTRA_NOTIFICATION);
        int notificationId = intent.getIntExtra(Services.EXTRA_NOTIFICATION_ID, 0);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationId, notification);
    }
}