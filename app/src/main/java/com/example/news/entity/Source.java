package com.example.news.entity;

/**
 * Created by Влад on 31.12.2017.
 */

public class Source {

    private String mSourceName;

    public Source(String mSourceName) {
        this.mSourceName = mSourceName;
    }

    public String getmSourceName() {
        return mSourceName;
    }

    public void setmSourceName(String mSourceName) {
        this.mSourceName = mSourceName;
    }

    @Override
    public String toString() {
        return mSourceName;
    }
}
