package org.maktab.photogallery.view.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.maktab.photogallery.R;
import org.maktab.photogallery.databinding.FragmentPhotoPageBinding;

public class PhotoPageFragment extends Fragment {

    private static final String ARG_PHOTO_URI = "photoUri";

    private FragmentPhotoPageBinding mBinding;
    private Uri mPhotoUri;

    public PhotoPageFragment() {
        // Required empty public constructor
    }

    public static PhotoPageFragment newInstance(Uri uri) {
        PhotoPageFragment fragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhotoUri = getArguments().getParcelable(ARG_PHOTO_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_photo_page,
                container,
                false);

        mBinding.photoPageProgressBar.setMax(100);
        mBinding.photoWebView.getSettings().setJavaScriptEnabled(true);

        mBinding.photoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return false;
            }
        });

        mBinding.photoWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mBinding.photoPageProgressBar.setVisibility(View.GONE);
                } else {
                    mBinding.photoPageProgressBar.setVisibility(View.VISIBLE);
                    mBinding.photoPageProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }
        });

        mBinding.photoWebView.loadUrl(mPhotoUri.toString());

        return mBinding.getRoot();
    }
}