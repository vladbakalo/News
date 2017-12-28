package com.example.news.application.enums;

import com.example.news.R;

/**
 * Created by Влад on 28.12.2017.
 */

public enum Country {
    UKRAINE(R.string.ukraine, "ua"),
    POLAND(R.string.poland, "pl"),
    GERMANY(R.string.germany, "de");

    private int nameResId;

    //ISO 3166
    private String isoCode;

    Country(int nameResId, String isoCode) {
        this.nameResId = nameResId;
        this.isoCode = isoCode;
    }

    public int getNameResId() {
        return nameResId;
    }

    public void setNameResId(int nameResId) {
        this.nameResId = nameResId;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "nameResId=" + nameResId +
                '}';
    }
}
