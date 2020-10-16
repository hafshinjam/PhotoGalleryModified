package org.maktab.photogallery.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class LocalService extends Service {

    public static Intent newIntent(Context context) {
        return new Intent(context, LocalService.class);
    }

    public LocalService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isFinish = intent.getBooleanExtra("isFinish", false);
        if (isFinish)
            stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}