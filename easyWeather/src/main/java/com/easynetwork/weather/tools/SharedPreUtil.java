package com.easynetwork.weather.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.bean.SimpleWeatherData;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences 存储工具类
 */
public class SharedPreUtil {
    private static String GLOBAL = "GLOBAL";
    private static String CITIES = "CITIES";
    private static String SimpleData = "Simple_data";

    /**
     * 获取全局变量
     *
     * @param context 上下文
     * @param key
     * @return
     */
    public static String getGlobalVar(Context context, String key) {
        SharedPreferences storage = context.getSharedPreferences(GLOBAL, Context.MODE_PRIVATE);
        return storage.getString(key, null);

    }

    /**
     * 获取全局变量
     *
     * @param context 上下文
     * @param key     键
     * @param str     默认值
     * @return
     */
    public static String getGlobalVar(Context context, String key, String str) {
        SharedPreferences storage = context.getSharedPreferences(GLOBAL, Context.MODE_PRIVATE);
        return storage.getString(key, str);
    }

    /**
     * 设置全局变量
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void setGlobalVar(Context context, String key, String value) {
        SharedPreferences.Editor storage = context.getSharedPreferences(GLOBAL, Context.MODE_PRIVATE).edit();
        storage.putString(key, value);
        storage.commit();
    }


    public static void addCity(Context context, City city) {
        List<City> cities = getCities(context, "");
        for (City c : cities) {
            if (c.getCity().equals(city.getCity())) {
                cities.remove(c);
                cities.add(city);
                return;
            }
        }
        cities.add(city);
        setCities(context, cities);
    }

    public static List<City> getCities(Context context, String str) {
        List<City> cities = new ArrayList<>();
        SharedPreferences storage = context.getSharedPreferences(CITIES, Context.MODE_PRIVATE);
        String result = storage.getString(CITIES, str);
        String[] cityString = result.split("_");
        for (String s : cityString) {
            if (s.length() == 0) continue;
            City c = new City(s.split("、"));
            if (c.getCity() != null && !c.getCity().equals("")) {
                cities.add(c);
            }
        }
        return cities;
    }

    private static void setCities(Context context, List<City> cities) {
        SharedPreferences.Editor edit = context.getSharedPreferences(CITIES, Context.MODE_PRIVATE).edit();
        StringBuilder sb = new StringBuilder();
        for (City c : cities) {
            sb.append("_");
            sb.append(c.getString());
        }
        edit.putString(CITIES, sb.toString()).commit();
    }

    public static void clearCities(Context context) {
        SharedPreferences.Editor storage = context.getSharedPreferences(CITIES, Context.MODE_PRIVATE).edit();
        storage.putString(CITIES, "");
        storage.commit();
    }

    public static void saveWeatherData(Context context, WeatherWrapper data) {
        if (!data.done) return;
        SimpleWeatherData simpleData = new SimpleWeatherData(data);
        SharedPreferences.Editor storage = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE).edit();
        storage.putString("simpleData", simpleData.getDate());
        storage.putString("simpleLocation", simpleData.getLocation());
        storage.putString("simpleCode", simpleData.getRtWeatherCode());
        storage.putString("simpleDescribe", simpleData.getRtDescribe());
        storage.putString("simpleDayDescribe", simpleData.getDayDescribe());
        storage.putString("simpleRTTMP", simpleData.getRtTmp());
        storage.putString("simpleTMPR", simpleData.getTmpRange());
        storage.putLong("simpleTimeStamp", simpleData.getTimeStamp());
        String[] predictions = simpleData.getPrediction();
        storage.putString("simplePrediction0", predictions[0]);
        storage.putString("simplePrediction1", predictions[1]);
        storage.putString("simplePrediction2", predictions[2]);
        storage.commit();
    }

    public static void saveSimpleData(Context context, String key, String data) {
        SharedPreferences.Editor storage = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE).edit();
        storage.putString(key, data).commit();
    }

    public static String getSimpleData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static SimpleWeatherData getWeatherData(Context context) {
        SharedPreferences storage = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE);
        long timeStamp = storage.getLong("simpleTimeStamp", -1);
        if (timeStamp == -1) return null;
        SimpleWeatherData date = new SimpleWeatherData();
        date.setTimeStamp(timeStamp);
        date.setDate(storage.getString("simpleData", ""));
        date.setLocation(storage.getString("simpleLocation", ""));
        date.setRtWeatherCode(storage.getString("simpleCode", ""));
        date.setRtDescribe(storage.getString("simpleDescribe", ""));
        date.setDayDescribe(storage.getString("simpleDayDescribe", ""));
        date.setRtTmp(storage.getString("simpleRTTMP", ""));
        date.setTmpRange(storage.getString("simpleTMPR", ""));
        String prediction0 = storage.getString("simplePrediction0", "");
        String prediction1 = storage.getString("simplePrediction1", "");
        String prediction2 = storage.getString("simplePrediction2", "");
        String[] predictions = new String[]{prediction0, prediction1, prediction2};
        date.setPrediction(predictions);
        return date;
    }

}
