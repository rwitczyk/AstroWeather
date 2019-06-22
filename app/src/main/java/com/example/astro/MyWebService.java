package com.example.astro;

import com.example.astro.data.Data;

import retrofit.Callback;
import retrofit.http.GET;

public interface MyWebService {
    @GET("/data/2.5/weather?lat=50&lon=20&appid=46f94484a5750d7cd295671f61987bb9") // deklarujemy endpoint oraz metodę
    void getDataByCoords(Callback<Data> pResponse);
}
