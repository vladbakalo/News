package com.example.news.application.enums;

import android.content.Context;

import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 28.12.2017.
 */

public enum ECountry {
    UKRAINE(R.string.ukraine, "ua"),
    POLAND(R.string.poland, "pl"),
    GERMANY(R.string.germany, "de");

    private int nameResId;

    //ISO 3166
    private String isoCode;

    ECountry(int nameResId, String isoCode) {
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

    public static List<String> getCountry(Context context){
        List<String> countries = new ArrayList<>();
        for (ECountry country:
                ECountry.values()) {
            countries.add(context.getString(country.getNameResId()));
        }
        return countries;
    }

    @Override
    public String toString() {
        return "ECountry{" +
                "nameResId=" + nameResId +
                '}';
    }
}
