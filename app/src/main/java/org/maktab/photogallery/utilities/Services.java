package org.maktab.photogallery.utilities;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.greenrobot.eventbus.EventBus;
import org.maktab.photogallery.R;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.eventbus.NotificationEvent;
import org.maktab.photogallery.view.activity.PhotoGalleryActivity;
import org.maktab.photogallery.worker.PollWorker;

import java.util.List;

public class Services {

    public static final String ACTION_SHOW_NOTIFICATION =
            "org.maktab.photogallery.SHOW_NOTIFICATION";
    public static final String EXTRA_NOTIFICATION =
            "org.maktab.photogallery.EXTRA_NOTIFICATION";
    public static final String EXTRA_NOTIFICATION_ID =
            "org.maktab.photogallery.EXTRA_NOTIFICATION_ID";
    public static final String PERM_PRIVATE_NOTIFICATION =
            "org.maktab.photogallery.PRIVATE_NOTIFICATION";

    private static final int REQUEST_CODE_NOTIFICATION = 1;
    private static final int NOTIFICATION_ID = 0;

    public static void pollFromServerAndNotify(Context context, String tag) {
        PhotoRepository repository = PhotoRepository.getInstance();

        List<GalleryItem> items;
        String query = QueryPreferences.getSearchQuery(context);
        if (query == null) {
            items = repository.getPopularItemsSync();
        } else {
            items = repository.getSearchItemSync(query);
        }

        if (items == null || items.size() == 0)
            return;

        String lastResultIdPref = QueryPreferences.getLastResultId(context);
        String lastResultId = items.get(0).getId();
        Log.d(tag, "lastResultIdPref: " + lastResultIdPref);
        Log.d(tag, "lastResultId: " + lastResultId);

        if (!lastResultId.equals(lastResultIdPref)) {
            sendNotificationEvent(context);
            Log.d(tag, "send notification event");
        } else {
            Log.d(tag, "do nothing");
        }

        QueryPreferences.setLastResultId(context, lastResultId);
    }

    private static void sendNotificationEvent(Context context) {
        Log.d(PollWorker.TAG, "sendNotificationEvent");

        String channelId = context.getResources().getString(R.string.channel_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE_NOTIFICATION,
                PhotoGalleryActivity.newIntent(context),
                0);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(context.getResources().getString(R.string.new_pictures_title))
                .setContentText(context.getResources().getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationEvent notificationEvent = new NotificationEvent(NOTIFICATION_ID, notification);
        EventBus.getDefault().post(notificationEvent);
    }

    /*private static void sendNotificationEvent(Context context) {
        Log.d(PollWorker.TAG, "sendNotificationEvent");

        String channelId = context.getResources().getString(R.string.channel_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE_NOTIFICATION,
                PhotoGalleryActivity.newIntent(context),
                0);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(context.getResources().getString(R.string.new_pictures_title))
                .setContentText(context.getResources().getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(EXTRA_NOTIFICATION, notification);
        intent.putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID);
//        context.sendBroadcast(intent, PERM_PRIVATE_NOTIFICATION);
        context.sendOrderedBroadcast(
                intent,
                PERM_PRIVATE_NOTIFICATION,
                null,
                null,
                Activity.RESULT_OK,
                null,
                null);
    }*/
}
