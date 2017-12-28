package com.example.news.retrofit.response.news;

import com.example.news.retrofit.model.news.Source;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Влад on 28.12.2017.
 */

public class SourceResponse extends BaseResponse {

    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
