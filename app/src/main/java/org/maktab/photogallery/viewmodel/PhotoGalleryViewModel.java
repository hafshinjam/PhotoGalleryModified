package org.maktab.photogallery.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.maktab.photogallery.R;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.remote.NetworkParams;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.utilities.QueryPreferences;
import org.maktab.photogallery.worker.PollWorker;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

    public List<GalleryItem> getItems() {
        String query = QueryPreferences.getSearchQuery(getApplication());

        List<GalleryItem> items;
        if (query == null)
            items = mPopularItemsLiveData.getValue();
        else
            items = mSearchItemsLiveData.getValue();

        return items == null ? new ArrayList<>() : items;
    }

    public String getSearchQueryFromPref() {
        return QueryPreferences.getSearchQuery(getApplication());
    }

    public void saveSearchQueryInPref(String query) {
        QueryPreferences.setSearchQuery(getApplication(), query);
    }

    public void togglePollService() {
        boolean isOn = PollWorker.isWorkScheduled(getApplication());
        PollWorker.scheduleWork(getApplication(), !isOn);
    }

    public int getTogglePollingTitle() {
        if (PollWorker.isWorkScheduled(getApplication()))
            return R.string.stop_polling;
        else
            return R.string.start_polling;
    }

    public void onPhotoClick(int position) {
        if (getItems() == null)
            return;

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                NetworkParams.getPhotoUri(getItems().get(position)));

        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}
