package org.maktab.photogallery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.utilities.QueryPreferences;

import java.util.List;

public class PhotoGalleryViewModel extends AndroidViewModel {

    private PhotoRepository mRepository;
    private LiveData<List<GalleryItem>> mPopularItemsLiveData;
    private LiveData<List<GalleryItem>> mSearchItemsLiveData;

    public LiveData<List<GalleryItem>> getPopularItemsLiveData() {
        return mPopularItemsLiveData;
    }

    public LiveData<List<GalleryItem>> getSearchItemsLiveData() {
        return mSearchItemsLiveData;
    }

    public PhotoGalleryViewModel(@NonNull Application application) {
        super(application);

        mRepository = PhotoRepository.getInstance();
        mPopularItemsLiveData = mRepository.getPopularItemsLiveData();
        mSearchItemsLiveData = mRepository.getSearchItemsLiveData();
    }

    public void fetchPopularItemsLiveDataApi() {
        mRepository.fetchPopularItemsLiveDataApi();
    }

    public void fetchSearchItemsLiveDataApi(String query) {
        mRepository.fetchSearchItemsLiveDataApi(query);
    }

    public void fetchItems() {
        String query = QueryPreferences.getSearchQuery(getApplication());
        if (query == null)
            fetchPopularItemsLiveDataApi();
        else
            fetchSearchItemsLiveDataApi(query);
    }

    public void saveQueryInPref(String query) {
        QueryPreferences.setSearchQuery(getApplication(), query);
    }
}
