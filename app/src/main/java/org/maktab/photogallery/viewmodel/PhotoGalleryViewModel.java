package org.maktab.photogallery.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.maktab.photogallery.R;
import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.repository.PhotoRepository;
import org.maktab.photogallery.services.PollJobService;
import org.maktab.photogallery.services.PollService;
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

    public String getSearchQueryFromPref() {
        return QueryPreferences.getSearchQuery(getApplication());
    }

    public void saveSearchQueryInPref(String query) {
        QueryPreferences.setSearchQuery(getApplication(), query);
    }

    public void startPollService() {
        getApplication().startService(PollService.newIntent(getApplication()));
    }

    public void schedulePollService(Activity activity) {
        PollService.setServiceAlarm(activity, true);
    }

    public void togglePollService(Activity activity) {
        boolean isOn = isServiceOnOrScheduled(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //schedule PollService in Alarm Manager
            PollService.setServiceAlarm(activity, !isOn);
        } else {
            //scheduler PollJobService in Job Scheduler
            PollJobService.scheduleJob(activity, !isOn);
        }
    }

    public int getTogglePollingTitle(Activity activity) {
        if (isServiceOnOrScheduled(activity))
            return R.string.stop_polling;
        else
            return R.string.start_polling;
    }

    public boolean isServiceOnOrScheduled(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return PollService.isServiceOn(activity);
        } else {
            return PollJobService.isJobScheduled(activity);
        }
    }
}
