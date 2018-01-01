package com.example.news.entity;

import java.util.List;

/**
 * Created by Влад on 31.12.2017.
 */

public class User {

    private String uId;
    private String firstName;
    private String lastName;
    private int countryId;
    private int cityId;
    private int    telephone;
    private long   birthDay;
    private String photoPath;

    private List<Source> newsSources;

    public User() {

    }

    public User(String uId, String firstName, String lastName, int countryId, int cityId, int telephone, long birthDay, String photoPath, List<Source> newsSources) {
        this.uId = uId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryId = countryId;
        this.cityId = cityId;
        this.telephone = telephone;
        this.birthDay = birthDay;
        this.photoPath = photoPath;
        this.newsSources = newsSources;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public List<Source> getNewsSources() {
        return newsSources;
    }

    public void setNewsSources(List<Source> newsSources) {
        this.newsSources = newsSources;
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }
}
