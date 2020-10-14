package org.maktab.photogallery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.maktab.photogallery.model.GalleryItem;
import org.maktab.photogallery.repository.PhotoRepository;

import java.util.List;

public class PhotoGalleryViewModel extends ViewModel {

    private PhotoRepository mRepository;
    private LiveData<List<GalleryItem>> mItemsLiveData;

    public LiveData<List<GalleryItem>> getItemsLiveData() {
        return mItemsLiveData;
    }

    public PhotoGalleryViewModel() {
        mRepository = PhotoRepository.getInstance();
        mItemsLiveData = mRepository.getItemsLiveData();
    }
}
