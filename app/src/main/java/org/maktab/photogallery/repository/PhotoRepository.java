package org.maktab.photogallery.repository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.maktab.photogallery.controller.fragment.PhotoGalleryFragment;
import org.maktab.photogallery.model.GalleryItem;
import org.maktab.photogallery.model.network.FllickrResponse;
import org.maktab.photogallery.model.network.PhotoItem;
import org.maktab.photogallery.network.FlickrService;
import org.maktab.photogallery.network.RetrofitInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoRepository {

    private static PhotoRepository sInstance;
    private List<GalleryItem> mItems = new ArrayList<>();

    private FlickrService mFlickrService;

    public static PhotoRepository getInstance() {
        if (sInstance == null)
            sInstance = new PhotoRepository();

        return sInstance;
    }

    private PhotoRepository() {
        Retrofit retrofit = RetrofitInstance.getInstance();
        mFlickrService = retrofit.create(FlickrService.class);
    }

    public List<PhotoItem> getItems() {
        Call<FllickrResponse> call = mFlickrService.listItems(RetrofitInstance.QUERY_OPTIONS);
        try {
            Response<FllickrResponse> response = call.execute();
            return response.body().getPhotos().getPhoto();
        } catch (IOException e) {
            Log.e(PhotoGalleryFragment.TAG, e.getMessage(), e);
        }
        return null;

        /*String url = FlickrFetcher.generateUrl();

        FlickrFetcher flickrFetcher = new FlickrFetcher();
        String jsonBodyString = null;
        try {
            jsonBodyString = flickrFetcher.getString(url);
            Log.d(PhotoGalleryFragment.TAG, jsonBodyString);
            JSONObject jsonBody = new JSONObject(jsonBodyString);
            mItems = parseJson(jsonBody);
        } catch (Exception e) {
            Log.e(PhotoGalleryFragment.TAG, e.getMessage(), e);
        }
        return mItems;*/
    }

    /*public void setItems(List<GalleryItem> items) {
        mItems = items;
    }*/

    /*private List<GalleryItem> parseJson(JSONObject jsonBody) throws JSONException {
        List<GalleryItem> items = new ArrayList<>();

        JSONObject photosObject = jsonBody.getJSONObject("photos");
        JSONArray photoArray = photosObject.getJSONArray("photo");
        for (int i = 0; i < photoArray.length(); i++) {
            JSONObject photoObject = photoArray.getJSONObject(i);
            if (!photoObject.has("url_s"))
                continue;

            String id = photoObject.getString("id");
            String caption = photoObject.getString("title");
            String url = photoObject.getString("url_s");

            GalleryItem item = new GalleryItem(id, caption, url);
            items.add(item);
        }

        return items;
    }*/
}
