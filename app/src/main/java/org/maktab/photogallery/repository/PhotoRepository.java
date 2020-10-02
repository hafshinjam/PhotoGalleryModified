package org.maktab.photogallery.repository;

import org.maktab.photogallery.model.GalleryItem;

import java.util.List;

public class PhotoRepository {

    private static PhotoRepository sInstance;
    private List<GalleryItem> mItems;

    public static PhotoRepository getInstance() {
        if (sInstance == null)
            sInstance = new PhotoRepository();

        return sInstance;
    }

    public List<GalleryItem> getItems() {
        return mItems;
    }

    public void setItems(List<GalleryItem> items) {
        mItems = items;
    }

    private PhotoRepository() {

    }
}
