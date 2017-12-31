package com.example.news.application.enums;

import android.content.Context;

import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 28.12.2017.
 *
 */

public enum ECity {
    KIEV(R.string.kiev, 50.4858426f, 30.4329697f, ECountry.UKRAINE),
    KHARKOV(R.string.kharkov, 49.994507f, 36.1457415f, ECountry.UKRAINE),
    DNEPR(R.string.dnepr, 48.4622135f, 34.860273f, ECountry.UKRAINE),
    LVIV(R.string.lviv, 49.8326679f, 23.9421958f, ECountry.UKRAINE),

    VARSHAVA(R.string.varshava, 52.232855f, 20.9211113f, ECountry.POLAND),
    KRAKOV(R.string.krakov, 50.0466814f, 19.8647899f, ECountry.POLAND),
    LODZ(R.string.lodz, 51.7730347f, 19.3405097f, ECountry.POLAND),
    POZNAN(R.string.poznan, 52.4004458f, 16.7615831f, ECountry.POLAND),

    BERLIN(R.string.berlin, 52.5065133f, 13.1445526f, ECountry.GERMANY),
    MUNHEN(R.string.munhen, 48.1548256f, 11.4017523f, ECountry.GERMANY),
    KELN(R.string.keln, 50.9576191f, 6.8272393f, ECountry.GERMANY),
    GAMBURG(R.string.gamburg, 53.5582446f ,9.6476432f, ECountry.GERMANY);

    private int nameResId;
    private float lat;
    private float lon;
    private ECountry country;

    ECity(int nameResId, float lat, float lon, ECountry country) {
        this.nameResId = nameResId;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public int getNameResId() {
        return nameResId;
    }

    public void setNameResId(int nameResId) {
        this.nameResId = nameResId;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public ECountry getCountry() {
        return country;
    }

    public void setCountry(ECountry country) {
        this.country = country;
    }

    public static List<ECity> getByCountry(ECountry pCountry){
        List<ECity> cities = new ArrayList<>();
        for (ECity city :
                ECity.values()) {
            if (city.getCountry() == pCountry)
                cities.add(city);
        }
        return cities;
    }

    public static List<String> getCities(List<ECity> pCities , Context context){
        List<String> cities = new ArrayList<>();
        for (ECity city:
                pCities) {
            cities.add(context.getString(city.getNameResId()));
        }
        return cities;
    }

    @Override
    public String toString() {
        return "ECity{" +
                "nameResId=" + nameResId +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country=" + country +
                '}';
    }
}
