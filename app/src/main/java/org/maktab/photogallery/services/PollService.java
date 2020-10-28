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
import org.maktab.photogallery.utilities.Services;
import org.maktab.photogallery.view.activity.PhotoGalleryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {

    public static final String TAG = "PollService";
    private static final int REQUEST_CODE = 0;
    private static final long INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(1);

    private PhotoRepository mRepository = PhotoRepository.getInstance();

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableOrConnected())
            return;

        Log.d(TAG, "onHandleIntent: " + intent.toString());
        Services.pollFromServerAndNotify(this, TAG);
    }

    public boolean isNetworkAvailableOrConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
            return true;

        return false;
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
}