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
    KIEV(1, R.string.kiev, 50.4858426f, 30.4329697f, ECountry.UKRAINE),
    KHARKOV(2, R.string.kharkov, 49.994507f, 36.1457415f, ECountry.UKRAINE),
    DNEPR(3, R.string.dnepr, 48.4622135f, 34.860273f, ECountry.UKRAINE),
    LVIV(4, R.string.lviv, 49.8326679f, 23.9421958f, ECountry.UKRAINE),

    VARSHAVA(5, R.string.varshava, 52.232855f, 20.9211113f, ECountry.POLAND),
    KRAKOV(6, R.string.krakov, 50.0466814f, 19.8647899f, ECountry.POLAND),
    LODZ(7, R.string.lodz, 51.7730347f, 19.3405097f, ECountry.POLAND),
    POZNAN(8, R.string.poznan, 52.4004458f, 16.7615831f, ECountry.POLAND),

    BERLIN(9, R.string.berlin, 52.5065133f, 13.1445526f, ECountry.GERMANY),
    MUNHEN(10, R.string.munhen, 48.1548256f, 11.4017523f, ECountry.GERMANY),
    KELN(11, R.string.keln, 50.9576191f, 6.8272393f, ECountry.GERMANY),
    GAMBURG(12, R.string.gamburg, 53.5582446f ,9.6476432f, ECountry.GERMANY);

    private int id;
    private int nameResId;
    private float lat;
    private float lon;
    private ECountry country;

    ECity(int id, int nameResId, float lat, float lon, ECountry country) {
        this.id = id;
        this.nameResId = nameResId;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static ECity getCitiyById(int id){
        for (ECity city:
                ECity.values()) {
            if (city.getId() == id)
                return city;
        }
        return null;
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
