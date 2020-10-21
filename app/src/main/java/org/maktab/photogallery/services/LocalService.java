package org.maktab.photogallery.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalService extends Service {

    private IBinder mBinder = new LocalBinder();
    private Random mGenerateRandom = new Random();

    public static Intent newIntent(Context context) {
        return new Intent(context, LocalService.class);
    }

    public LocalService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isFinish = intent.getBooleanExtra("isFinish", false);
        if (isFinish)
            stopSelf();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getRandomNumber() {
        return mGenerateRandom.nextInt(100);
    }

    public class LocalBinder extends Binder {
        public LocalService getLocalService() {
            return LocalService.this;
        }
    }
}