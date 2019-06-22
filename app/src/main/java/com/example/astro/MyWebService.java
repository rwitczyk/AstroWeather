package com.example.astro;

import com.example.astro.data.Data;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MyWebService {
    @GET("/data/2.5/weather") // deklarujemy endpoint oraz metodę
    void getDataByCoords(@Query("lat") Double lat,@Query("lon") Double lon, @Query("appid") String appid, Callback<Data> pResponse);
}
