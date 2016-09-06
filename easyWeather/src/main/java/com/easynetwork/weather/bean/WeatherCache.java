package com.easynetwork.weather.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kent ↗↗↗ on 2016/9/5.
 */
@DatabaseTable(tableName = "WeatherCache")
public class WeatherCache {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "city")
    private String city;

    @DatabaseField(columnName = "latitude")
    private double latitude;

    @DatabaseField(columnName = "longitude")
    private double longitude;

    @DatabaseField(columnName = "date")
    private String date;

    @DatabaseField(columnName = "rtTmp")
    private String rtTmp;

    @DatabaseField(columnName = "minTmp")
    private String minTmp;

    @DatabaseField(columnName = "maxTmp")
    private String maxTmp;

    @DatabaseField(columnName = "rtDrc")
    private String describe;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setRtTmp(String rtTmp) {
        this.rtTmp = rtTmp;
    }

    public String getRtTmp() {
        return rtTmp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMinTmp() {
        return minTmp;
    }

    public void setMinTmp(String minTmp) {
        this.minTmp = minTmp;
    }

    public String getMaxTmp() {
        return maxTmp;
    }

    public void setMaxTmp(String maxTmp) {
        this.maxTmp = maxTmp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
