package org.maktab.photogallery.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.maktab.photogallery.utilities.Services;

public abstract class VisibleFragment extends Fragment {

    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();

        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(Services.ACTION_SHOW_NOTIFICATION);
        intentFilter.setPriority(0);
        getActivity().registerReceiver(
                mOnNotificationReceived,
                intentFilter,
                Services.PERM_PRIVATE_NOTIFICATION,
                null);
    }

    @Override
    public void onStop() {
        super.onStop();

        //unregister broadcast receiver
        getActivity().unregisterReceiver(mOnNotificationReceived);
    }

    private final BroadcastReceiver mOnNotificationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received boot broadcast intent: " + intent.getAction());

            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
