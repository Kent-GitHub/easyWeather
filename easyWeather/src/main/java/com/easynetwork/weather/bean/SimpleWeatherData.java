package com.easynetwork.weather.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yanming on 2016/8/17.
 */
public class SimpleWeatherData {
    private String date;
    private String location;
    private String weatherCode;
    private String describe;
    private String dayDescribe;
    private String rtTmp;
    private String tmpRange;
    private String[] prediction;
    private long timeStamp;

    public SimpleWeatherData() {
    }

    public SimpleWeatherData(WeatherWrapper weatherData) {
        if (!weatherData.done) {
            return;
        }
        timeStamp = System.currentTimeMillis() / 1000;
        DailyWeatherData today = weatherData.getToday();
        NowWeatherData now = weatherData.getNowWeatherData();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        StringBuilder dateString = new StringBuilder();
        dateString.append(new SimpleDateFormat("MM.dd").format(date) + "/");
        dateString.append(formatWeekday(c.get(Calendar.DAY_OF_WEEK)));
        setDate(dateString.toString());
        setLocation(weatherData.nUser.city);
        setDescribe(now.txt);
        setDayDescribe(today.describe);
        setRtTmp(now.tmp);
        setTmpRange(today.minTmp + "~" + today.maxTmp + "°C");
        setWeatherCode(now.code);
        DailyWeatherData tomorrow = weatherData.getTomorrow();
        DailyWeatherData afterTomorrow = weatherData.getAfterTomorrow();
        prediction = new String[]{today.getString(), tomorrow.getString(), afterTomorrow.getString()};
    }

    public SimpleWeatherData(WeatherBean bean){
        bean.getData().getBasic().getUpdate();
    }

    public String getSpeakText() {
        String speak;
        String temR;
        if (tmpRange == null) {
            temR = "未知";
        } else {
            temR = tmpRange.replace("C", "");
        }
        speak = "当前城市" + replaceEmpty(location) +
                ",今天天气为" + replaceEmpty(dayDescribe) +
                ",气温" + temR+
                ",当前实时天气为" + replaceEmpty(describe) +
                ",实时温度为" + replaceEmpty(rtTmp)+"°";
        return speak;
    }

    private String replaceEmpty(String s) {
        if (s == null || "".equals(s)) {
            return "未知";
        }
        return s;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDayDescribe() {
        return dayDescribe;
    }

    public void setDayDescribe(String dayDescribe) {
        this.dayDescribe = dayDescribe;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getRtTmp() {
        return rtTmp;
    }

    public void setRtTmp(String rtTmp) {
        this.rtTmp = rtTmp;
    }

    public String getTmpRange() {
        return tmpRange;
    }

    public void setTmpRange(String tmpRange) {
        this.tmpRange = tmpRange;
    }

    public String[] getPrediction() {
        return prediction;
    }

    public void setPrediction(String[] prediction) {
        this.prediction = prediction;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String formatWeekday(int day) {
        switch (day) {
            case 1:
                return "周天";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "周八";
        }
    }
}
