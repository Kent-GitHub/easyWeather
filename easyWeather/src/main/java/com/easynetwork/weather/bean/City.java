package com.easynetwork.weather.bean;

import java.io.Serializable;

/**
 * Created by yanming on 2016/8/17.
 */
public class City implements Serializable {
    String city;
    double latitude;
    double longitude;

    public City() {
    }

    public City(String city) {
        this.city = city;
    }

    public City(String city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public final String getString() {
        return city + "、" + latitude + "、" + longitude;
    }

    public City(String[] values) {
        city = values[0];
        latitude = Double.valueOf(values[1]);
        longitude = Double.valueOf(values[2]);
    }

}
