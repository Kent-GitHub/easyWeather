package com.easynetwork.weather.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.easynetwork.weather.bean.City;

public class WeatherApplication extends Application {

    private static int screenWidth;
    private static int screenHeight;
    private static String currentCity = "未知";
    private static City selectedCity;
    private static City locatedCity;


    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static String getCurrentCity() {
        return currentCity;
    }

    public static void setCurrentCity(String currentCity) {
        if (currentCity==null)currentCity="未知";
        WeatherApplication.currentCity = currentCity;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }

    public static City getSelectedCity() {
        return selectedCity;
    }

    public static void setSelectedCity(City selectedCity) {
        WeatherApplication.selectedCity = selectedCity;
    }

    public static City getLocatedCity() {
        return locatedCity;
    }

    public static void setLocatedCity(City locatedCity) {
        WeatherApplication.locatedCity = locatedCity;
    }
}
