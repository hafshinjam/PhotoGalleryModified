package org.maktab.photogallery;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.maktab.photogallery.eventbus.NotificationEvent;
import org.maktab.photogallery.utilities.Services;

public class PhotoGalleryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onNotificationEvent(NotificationEvent notificationEvent) {
        Log.d("EventBus", "Application show notification");

        //show notification
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(
                notificationEvent.getNotificationId(),
                notificationEvent.getNotification());
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String id = getResources().getString(R.string.channel_id);
            String name = getResources().getString(R.string.channel_name);
            String description = getResources().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
