package org.maktab.photogallery.services;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class ThumbnailDownloader extends HandlerThread {

    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;

    public static final String TAG = "ThumbnailDownloader";

    public ThumbnailDownloader() {
        super(TAG);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mRequestHandler = new Handler();
    }

    public void queueThumbnailMessage(String url) {
        //1. create a message with a handler (what, obj, target)
        //2. send message to queue
        Message message = mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, url);
        message.sendToTarget();
    }
}
