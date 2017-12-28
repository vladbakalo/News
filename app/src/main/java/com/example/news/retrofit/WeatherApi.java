package com.example.news.retrofit;

import com.example.news.retrofit.response.weather.WeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Влад on 28.12.2017.
 */

public interface WeatherApi {

    @GET("weather")
    WeatherResponse getWeather(@Query("appid") String apiKey, @Query("lat") float lat, @Query("long") float lon);
}
