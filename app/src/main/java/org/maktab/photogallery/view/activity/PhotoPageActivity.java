package org.maktab.photogallery.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import org.maktab.photogallery.view.fragment.PhotoPageFragment;

public class PhotoPageActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri uri) {
        Intent intent = new Intent(context, PhotoPageActivity.class);
        intent.setData(uri);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        Uri photoUri = getIntent().getData();
        return PhotoPageFragment.newInstance(photoUri);
    }
}