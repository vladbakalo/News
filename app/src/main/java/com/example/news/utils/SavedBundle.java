package com.example.news.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Влад on 31.12.2017.
 */

public class SavedBundle implements Parcelable {

    private String mClassName;
    private Bundle mBundle;

    public SavedBundle(Parcel parcel) {
        mClassName = parcel.readString();
        mBundle = parcel.readBundle();
    }

    private SavedBundle(Class pClass, Bundle pBundle){
        setClassName(pClass);
        setBundle(pBundle);
    }

    public static SavedBundle fromIntent(Intent pIntent) {
        return pIntent.getParcelableExtra(SavedBundle.class.getName());
    }

    public void store(Intent pIntent) {
        pIntent.putExtra(this.getClass().getName(), this);
    }

    public static SavedBundle create(Class pClass, Bundle pBundle){
        return new SavedBundle(pClass, pBundle);
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(Class pClass) {
        this.mClassName = pClass.getName();
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle pBundle) {
        this.mBundle = pBundle;
    }

    public static final Creator<SavedBundle> CREATOR = new Creator<SavedBundle>() {
        @Override
        public SavedBundle createFromParcel(Parcel in) {
            return new SavedBundle(in);
        }

        @Override
        public SavedBundle[] newArray(int size) {
            return new SavedBundle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mClassName);
        parcel.writeBundle(mBundle);
    }
}
