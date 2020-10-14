package org.maktab.photogallery.repository;

import org.maktab.photogallery.model.GalleryItem;

import java.util.List;

public interface IRepository {
    void getItemsAsync();
    List<GalleryItem> getItemsSync();
}
