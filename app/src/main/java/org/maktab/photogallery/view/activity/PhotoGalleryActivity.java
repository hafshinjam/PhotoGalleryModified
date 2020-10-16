package org.maktab.photogallery.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import org.maktab.photogallery.services.LocalService;
import org.maktab.photogallery.view.fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

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
            }
        });
    }

    @Override
    public Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}