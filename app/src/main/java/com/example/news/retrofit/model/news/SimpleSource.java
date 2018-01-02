package com.example.news.retrofit.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * SimpleSource model
 *
 */

public class SimpleSource implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    protected SimpleSource(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SimpleSource> CREATOR = new Creator<SimpleSource>() {
        @Override
        public SimpleSource createFromParcel(Parcel in) {
            return new SimpleSource(in);
        }

        @Override
        public SimpleSource[] newArray(int size) {
            return new SimpleSource[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}