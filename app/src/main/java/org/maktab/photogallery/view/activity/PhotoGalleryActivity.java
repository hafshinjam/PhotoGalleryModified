package org.maktab.photogallery.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.maktab.photogallery.services.LocalService;
import org.maktab.photogallery.view.fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    private LocalService mLocalService;
    private boolean mBound = false;
    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding.buttonCheckService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LocalService.newIntent(PhotoGalleryActivity.this);

                if (number == 2)
                    intent.putExtra("isFinish", true);

                startService(intent);
                number++;

                if (mBound) {
                    int random = mLocalService.getRandomNumber();
                    Toast.makeText(
                            PhotoGalleryActivity.this,
                            "random: " + random,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = LocalService.newIntent(PhotoGalleryActivity.this);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (!(service instanceof LocalService.LocalBinder)) {
                Log.e("Service", "This is wrong service");
                return;
            }

            mBound = true;
            LocalService.LocalBinder localBinder = (LocalService.LocalBinder) service;
            mLocalService = localBinder.getLocalService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}