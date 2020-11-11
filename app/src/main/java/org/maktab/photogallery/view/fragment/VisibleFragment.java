package org.maktab.photogallery.view.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.maktab.photogallery.eventbus.NotificationEvent;

public abstract class VisibleFragment extends Fragment {

    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();

        //register broadcast receiver
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        //unregister broadcast receiver
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
    public void onNotificationEvent(NotificationEvent notificationEvent) {
        //cancel the notification
        Log.d("EventBus", "VisibleFragment cancel notification");
        EventBus.getDefault().cancelEventDelivery(notificationEvent);
    }
}
