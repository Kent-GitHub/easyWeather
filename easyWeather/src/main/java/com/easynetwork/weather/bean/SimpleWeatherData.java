package com.easynetwork.weather.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SimpleWeatherData {
    private String date;
    private String location;
    private String rtWeatherCode;
    private String dayWeatherCode;
    private String rtDescribe;
    private String dayDescribe;
    private String rtTmp;
    private String tmpRange;
    private String[] prediction;
    private long timeStamp;

    private List<DailyWeatherData> days = new ArrayList<>();

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
        String dateString = (new SimpleDateFormat("MM.dd").format(date) + "/") +
                formatWeekday(c.get(Calendar.DAY_OF_WEEK));
        setDate(dateString);
        setLocation(weatherData.nUser.city);
        setRtDescribe(now.txt);
        setDayDescribe(today.describe);
        setRtTmp(now.tmp);
        setTmpRange(today.minTmp + "~" + today.maxTmp + "°C");
        setRtWeatherCode(now.code);
        DailyWeatherData tomorrow = weatherData.getTomorrow();
        DailyWeatherData afterTomorrow = weatherData.getAfterTomorrow();
        prediction = new String[]{today.getString(), tomorrow.getString(), afterTomorrow.getString()};
    }

    public SimpleWeatherData(WeatherBean bean) {
        //设置date// 08.19/周五
        Date date = bean.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dateString = (new SimpleDateFormat("MM.dd").format(date) + "/") +
                formatWeekday(c.get(Calendar.DAY_OF_WEEK));
        setDate(dateString);
        //设置location
        setLocation(bean.getCity());
        //设置rtWeatherCode
        setRtWeatherCode(bean.getRtCode());
        //设置rtDescribe
        setRtDescribe(bean.getRtDescribe());
        //设置rtTmp
        setRtTmp(bean.getRtTmp());
        //设置dayWeatherCode
        setDayWeatherCode(bean.getDayCode(0));
        //设置DayDescribe
        setDayDescribe(bean.getDayDescribe(0));
        //设置tmpRange
        setTmpRange(bean.getTmpRange(0));
        //设置days
        days.add(bean.getDailyDate(0));
        days.add(bean.getDailyDate(1));
        days.add(bean.getDailyDate(2));
    }

    public List<DailyWeatherData> getDays() {
        return days;
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
                ",气温" + temR +
                ",当前实时天气为" + replaceEmpty(rtDescribe) +
                ",实时温度为" + replaceEmpty(rtTmp) + "°";
        return speak;
    }

    private String replaceEmpty(String s) {
        if (s == null || "".equals(s)) {
            return "未知";
        }
        return s;
    }

    public String getDayWeatherCode() {
        return dayWeatherCode;
    }

    public void setDayWeatherCode(String dayWeatherCode) {

        this.dayWeatherCode = dayWeatherCode;
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

    public String getRtDescribe() {
        return rtDescribe;
    }

    public void setRtDescribe(String rtDescribe) {
        this.rtDescribe = rtDescribe;
    }

    public String getDayDescribe() {
        return dayDescribe;
    }

    public void setDayDescribe(String dayDescribe) {
        this.dayDescribe = dayDescribe;
    }

    public String getRtWeatherCode() {
        return rtWeatherCode;
    }

    public void setRtWeatherCode(String rtWeatherCode) {
        this.rtWeatherCode = rtWeatherCode;
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
