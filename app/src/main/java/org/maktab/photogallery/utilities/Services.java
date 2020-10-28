package org.maktab.photogallery.utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.maktab.photogallery.R;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.view.activity.PhotoGalleryActivity;

import java.util.List;

public class Services {

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
            //show notification
            showNotification(context);
            Log.d(tag, "show notification");
        } else {
            Log.d(tag, "do nothing");
        }

        QueryPreferences.setLastResultId(context, lastResultId);
    }

    private static void showNotification(Context context) {
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

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }
}
