package com.example.news.application.enums;

import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 28.12.2017.
 *
 */

public enum City {
    KIEV(R.string.kiev, 50.4858426f, 30.4329697f, Country.UKRAINE),
    KHARKOV(R.string.kharkov, 49.994507f, 36.1457415f, Country.UKRAINE),
    DNEPR(R.string.dnepr, 48.4622135f, 34.860273f, Country.UKRAINE),
    LVIV(R.string.lviv, 49.8326679f, 23.9421958f, Country.UKRAINE),

    VARSHAVA(R.string.varshava, 52.232855f, 20.9211113f, Country.POLAND),
    KRAKOV(R.string.krakov, 50.0466814f, 19.8647899f, Country.POLAND),
    LODZ(R.string.lodz, 51.7730347f, 19.3405097f, Country.POLAND),
    POZNAN(R.string.poznan, 52.4004458f, 16.7615831f, Country.POLAND),

    BERLIN(R.string.berlin, 52.5065133f, 13.1445526f, Country.GERMANY),
    MUNHEN(R.string.munhen, 48.1548256f, 11.4017523f, Country.GERMANY),
    KELN(R.string.keln, 50.9576191f, 6.8272393f, Country.GERMANY),
    GAMBURG(R.string.gamburg, 53.5582446f ,9.6476432f, Country.GERMANY);

    private int nameResId;
    private float lat;
    private float lon;
    private Country country;

    City(int nameResId, float lat, float lon, Country country) {
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public static List<City> getByCountry(Country pCountry){
        List<City> cities = new ArrayList<>();
        for (City city :
                City.values()) {
            if (city.getCountry() == pCountry)
                cities.add(city);
        }
        return cities;
    }

    @Override
    public String toString() {
        return "City{" +
                "nameResId=" + nameResId +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country=" + country +
                '}';
    }
}
