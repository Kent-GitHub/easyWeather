package com.easynetwork.weather.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * 每天的天气
 *
 * @author WenYF
 */
public class DailyWeatherData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 684770246667227554L;
    public String date = "--/abstract--";
    public String txt = "----";
    public String code = "-1";
    public String describe = "--";
    public String minTmp = "--";
    public String maxTmp = "--";


    public String getTmpRange() {
        return minTmp + "~" + maxTmp + "°C";
    }

    public final String getString() {
        return date + "、" + txt + "、" + code + "、" + describe + "、" + minTmp + "、" + maxTmp;
    }

    public DailyWeatherData() {
    }

    @Override
    public String toString() {
        return "DailyWeatherData{" +
                "date='" + date + '\'' +
                ", txt='" + txt + '\'' +
                ", code='" + code + '\'' +
                ", describe='" + describe + '\'' +
                ", minTmp='" + minTmp + '\'' +
                ", maxTmp='" + maxTmp + '\'' +
                '}';
    }

    public DailyWeatherData(String s) {
        String[] strings = s.split("、");
        if (strings.length == 6) {
            date = strings[0];
            txt = strings[1];
            code = strings[2];
            describe = strings[3];
            minTmp = strings[4];
            maxTmp = strings[5];
        }
    }
}
