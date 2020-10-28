package org.maktab.photogallery.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import org.maktab.photogallery.R;
import org.maktab.photogallery.adapters.PhotoAdapter;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.databinding.FragmentPhotoGalleryBinding;
import org.maktab.photogallery.viewmodel.PhotoGalleryViewModel;

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

    private PhotoGalleryViewModel mViewModel;

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
        setHasOptionsMenu(true);

        mViewModel = new ViewModelProvider(this).get(PhotoGalleryViewModel.class);

        registerObservers();
        mViewModel.fetchItems();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem togglePollingItem = menu.findItem(R.id.toggle_polling);
        MenuItem searchViewMenuItem = menu.findItem(R.id.menu_item_search_view);
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();

        togglePollingItem.setTitle(mViewModel.getTogglePollingTitle());
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mViewModel.getSearchQueryFromPref();
                searchView.setQuery(query, false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.fetchSearchItemsLiveDataApi(query);
                mViewModel.saveSearchQueryInPref(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                mViewModel.saveSearchQueryInPref(null);
                mViewModel.fetchItems();
                return true;
            case R.id.toggle_polling:
                mViewModel.togglePollService();
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        mFragmentPhotoGalleryBinding.photoGalleryRecyclerview
                .setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    private void registerObservers() {
        //this observer is declared in main thread
        mViewModel.getPopularItemsLiveData().observe(this, new Observer<List<GalleryItem>>() {
            @Override
            public void onChanged(List<GalleryItem> items) {
                setupAdapter(items);
            }
        });

        mViewModel.getSearchItemsLiveData().observe(this, new Observer<List<GalleryItem>>() {
            @Override
            public void onChanged(List<GalleryItem> items) {
                setupAdapter(items);
            }
        });
    }

    private void setupAdapter(List<GalleryItem> items) {
        mAdapter = new PhotoAdapter(getContext(), this, items);
        mFragmentPhotoGalleryBinding.photoGalleryRecyclerview.setAdapter(mAdapter);
    }
}