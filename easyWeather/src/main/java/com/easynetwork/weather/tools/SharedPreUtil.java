package com.easynetwork.weather.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.*;

import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.SimpleWeatherData;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
                break;
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

    public static void removeCity(Context context, City city) {
        List<City> cities = getCities(context, "");
        for (City c : cities) {
            if (c.getCity().equals(city.getCity())) {
                cities.remove(c);
                break;
            }
        }
        setCities(context, cities);
    }

    public static void saveSimpleData(Context context, String key, String data) {
        SharedPreferences.Editor storage = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE).edit();
        storage.putString(key, data).commit();
    }

    public static String getSimpleData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SimpleData, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

}
