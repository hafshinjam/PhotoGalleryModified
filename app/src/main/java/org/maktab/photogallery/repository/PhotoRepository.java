package org.maktab.photogallery.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.reflect.TypeToken;

import org.maktab.photogallery.model.GalleryItem;
import org.maktab.photogallery.network.FlickrService;
import org.maktab.photogallery.network.GetGalleryItemsDeserializer;
import org.maktab.photogallery.network.NetworkParams;
import org.maktab.photogallery.network.RetrofitInstance;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoRepository {

    private static PhotoRepository sInstance;

    private FlickrService mFlickrService;
//    private Listeners mListeners;

    private MutableLiveData<List<GalleryItem>> mItemsLiveData = new MutableLiveData<>();

    public static PhotoRepository getInstance() {
        if (sInstance == null)
            sInstance = new PhotoRepository();

        return sInstance;
    }

    private PhotoRepository() {
        Type type = new TypeToken<List<GalleryItem>>(){}.getType();
        Object typeAdapter = new GetGalleryItemsDeserializer();

        Retrofit retrofit = RetrofitInstance.getInstance(type, typeAdapter);
        mFlickrService = retrofit.create(FlickrService.class);
    }

    /*public Listeners getListeners() {
        return mListeners;
    }*/

    /*public void setListeners(Listeners listeners) {
        mListeners = listeners;
    }*/

    public MutableLiveData<List<GalleryItem>> getItemsLiveData() {
        Call<List<GalleryItem>> call = mFlickrService.listItems(NetworkParams.QUERY_OPTIONS);
        call.enqueue(new Callback<List<GalleryItem>>() {
            @Override
            public void onResponse(Call<List<GalleryItem>> call, Response<List<GalleryItem>> response) {
                mItemsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<GalleryItem>> call, Throwable t) {

            }
        });

        return mItemsLiveData;
    }

    public void setItemsLiveData(MutableLiveData<List<GalleryItem>> itemsLiveData) {
        mItemsLiveData = itemsLiveData;
    }

    /*
    public void getItemsAsync() {
        Call<List<GalleryItem>> call = mFlickrService.listItems(NetworkParams.QUERY_OPTIONS);
        call.enqueue(new Callback<List<GalleryItem>>() {
            @Override
            public void onResponse(Call<List<GalleryItem>> call, Response<List<GalleryItem>> response) {
                List<GalleryItem> items = response.body();
                mListeners.onRetrofitResponse(items);
            }

            @Override
            public void onFailure(Call<List<GalleryItem>> call, Throwable t) {

            }
        });
    }*/

    /*
    public List<GalleryItem> getItemsSync() {
        Call<List<GalleryItem>> call = mFlickrService.listItems(NetworkParams.QUERY_OPTIONS);
        try {
            Response<List<GalleryItem>> response = call.execute();
            return response.body();
        } catch (IOException e) {
            Log.e(PhotoGalleryFragment.TAG, e.getMessage(), e);
        }
        return null;
    }*/
}
