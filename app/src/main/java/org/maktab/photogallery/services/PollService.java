package org.maktab.photogallery.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.maktab.photogallery.R;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.utilities.QueryPreferences;
import org.maktab.photogallery.view.activity.PhotoGalleryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {

    public static final String TAG = "PollService";
    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_NOTIFICATION = 1;
    private static final long INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final int NOTIFICATION_ID = 0;

    private PhotoRepository mRepository = PhotoRepository.getInstance();

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                REQUEST_CODE,
                intent,
                0);

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    INTERVAL_MILLIS,
                    pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);

            //very very very important to notice
            pendingIntent.cancel();
        }
    }

    public static boolean isServiceOn(Context context) {
        Intent intent = newIntent(context);

        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableOrConnected())
            return;

        Log.d(TAG, "onHandleIntent: " + intent.toString());

        List<GalleryItem> items;
        String query = QueryPreferences.getSearchQuery(this);
        if (query == null) {
            items = mRepository.getPopularItemsSync();
        } else {
            items = mRepository.getSearchItemSync(query);
        }

        if (items == null || items.size() == 0)
            return;

        String lastResultIdPref = QueryPreferences.getLastResultId(this);
        String lastResultId = items.get(0).getId();
        Log.d(TAG, "lastResultIdPref: " + lastResultIdPref);
        Log.d(TAG, "lastResultId: " + lastResultId);

        if (!lastResultId.equals(lastResultIdPref)) {
            //show notification
            showNotification();
            Log.d(TAG, "show notification");
        } else {
            Log.d(TAG, "do nothing");
        }

        QueryPreferences.setLastResultId(this, lastResultId);
    }

    public boolean isNetworkAvailableOrConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;

        return false;
    }

    private void showNotification() {
        String channelId = getResources().getString(R.string.channel_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                REQUEST_CODE_NOTIFICATION,
                PhotoGalleryActivity.newIntent(this),
                0);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(getResources().getString(R.string.new_pictures_title))
                .setContentText(getResources().getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }
}