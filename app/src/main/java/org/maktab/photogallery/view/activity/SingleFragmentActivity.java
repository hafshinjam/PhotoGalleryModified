package org.maktab.photogallery.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.maktab.photogallery.R;
import org.maktab.photogallery.databinding.ActivitySingleFragmentBinding;

/**
 * If we have activity that has only one fullscreen fragement we must
 * extend this Activity
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    public static final String FRAGMENT_TAG = "FragmentActivity";

    ActivitySingleFragmentBinding mBinding;

    public abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, createFragment(), FRAGMENT_TAG)
                    .commit();
        }
    }
}