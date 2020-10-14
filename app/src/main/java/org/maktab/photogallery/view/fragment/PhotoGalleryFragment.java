package org.maktab.photogallery.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import org.maktab.photogallery.R;
import org.maktab.photogallery.adapters.PhotoAdapter;
import org.maktab.photogallery.databinding.FragmentPhotoGalleryBinding;
import org.maktab.photogallery.model.GalleryItem;
import org.maktab.photogallery.repository.PhotoRepository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends Fragment {

    public static final int SPAN_COUNT = 3;
    public static final String TAG = "PGF";
    private FragmentPhotoGalleryBinding mFragmentPhotoGalleryBinding;
    private PhotoAdapter mAdapter;

    private PhotoRepository mRepository;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = PhotoRepository.getInstance();

        mRepository.setListeners(new PhotoRepository.Listeners() {
            @Override
            public void onRetrofitResponse(List<GalleryItem> items) {
                setupAdapter(items);
            }
        });

        mRepository.getItemsAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentPhotoGalleryBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_photo_gallery,
                container,
                false);

        initViews();

        return mFragmentPhotoGalleryBinding.getRoot();
    }

    private void initViews() {
        mFragmentPhotoGalleryBinding.photoGalleryRecyclerview
                .setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    private void setupAdapter(List<GalleryItem> items) {
        mAdapter = new PhotoAdapter(getContext(), items);
        mFragmentPhotoGalleryBinding.photoGalleryRecyclerview.setAdapter(mAdapter);
    }
}