package org.maktab.photogallery.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static Retrofit getInstance(Type type, Object typeAdapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkParams.BASE_PATH)
                .addConverterFactory(createGsonConverter(type, typeAdapter))
                .build();

        return retrofit;
    }

    public static Converter.Factory createGsonConverter(Type type, Object typeAdapter) {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(type, typeAdapter);

        Gson gson = builder.create();
        return GsonConverterFactory.create(gson);
    }
}
