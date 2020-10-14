package org.maktab.photogallery.network;

import org.maktab.photogallery.model.network.FllickrResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface FlickrService {

    @GET(".")
    Call<FllickrResponse> listItems(@QueryMap Map<String, String> options);
}
