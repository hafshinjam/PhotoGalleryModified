package org.maktab.photogallery.data.model;

public class GalleryItem {
    private String mId;
    private String mCaption;
    private String mUrl;
    private String mOwner;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public GalleryItem() {
    }

    public GalleryItem(String id, String caption, String url, String owner) {
        mId = id;
        mCaption = caption;
        mUrl = url;
        mOwner = owner;
    }
}
