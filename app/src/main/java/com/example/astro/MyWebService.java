package com.example.astro;

import com.example.astro.data.Data;
import com.example.astro.forecast.DataForecast;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MyWebService {
    @GET("/data/2.5/weather") // deklarujemy endpoint oraz metodę
    void getDataByCoords(@Query("lat") Double lat,@Query("lon") Double lon, @Query("units") String units, @Query("appid") String appid, Callback<Data> pResponse);

    @GET("/data/2.5/weather")
    void getDataByName(@Query("q") String cityName,@Query("units") String units, @Query("appid") String appid, Callback<Data> pResponse);

    @GET("/data/2.5/forecast")
    void getForecastByCoords(@Query("lat") Double lat,@Query("lon") Double lon, @Query("units") String units, @Query("appid") String appid, Callback<DataForecast> pResponse);

    @GET("/data/2.5/forecast")
    void getForecastByName(@Query("q") String cityName,@Query("units") String units, @Query("appid") String appid, Callback<DataForecast> pResponse);
}
