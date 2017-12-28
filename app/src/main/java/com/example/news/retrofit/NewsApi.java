package com.example.news.retrofit;

import com.example.news.retrofit.response.news.NewsResponse;
import com.example.news.retrofit.response.news.SourceResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Влад on 24.12.2017.
 */

public interface NewsApi {

    @GET("/v2/top-headlines")
    Observable<NewsResponse> getNews(@Query("apiKey") String apiKey, @Query("sources") String sources);

    @GET("/v2/sources")
    Observable<SourceResponse> getSources(@Query("apiKey") String apiKey);

}
