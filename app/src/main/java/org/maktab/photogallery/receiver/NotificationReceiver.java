package org.maktab.photogallery.receiver;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import org.maktab.photogallery.utilities.Services;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "PGNotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received boot broadcast intent: " + intent.getAction());
        if (getResultCode() != Activity.RESULT_OK) {
            Toast.makeText(
                    context,
                    "The notification has been canceled!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Notification notification = intent.getParcelableExtra(Services.EXTRA_NOTIFICATION);
        int notificationId = intent.getIntExtra(Services.EXTRA_NOTIFICATION_ID, 0);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationId, notification);
    }
}