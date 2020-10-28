package org.maktab.photogallery.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.maktab.photogallery.data.model.GalleryItem;
import org.maktab.photogallery.data.remote.NetworkParams;
import org.maktab.photogallery.data.repository.PhotoRepository;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PhotoItemViewModel extends AndroidViewModel {

    private GalleryItem mGalleryItem;
    private PhotoRepository mRepository;

    public GalleryItem getGalleryItem() {
        return mGalleryItem;
    }

    public void setGalleryItem(GalleryItem galleryItem) {
        mGalleryItem = galleryItem;
    }

    public PhotoItemViewModel(@NonNull Application application) {
        super(application);

        mRepository = PhotoRepository.getInstance();
    }

    public void onPhotoClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, NetworkParams.getPhotoUri(mGalleryItem));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}
