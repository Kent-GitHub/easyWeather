package com.easynetwork.weather.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherCache;
import com.easynetwork.weather.core.LocationManager;
import com.easynetwork.weather.core.WeatherManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kent ↗↗↗ on 2016/9/5.
 */
public class WeatherService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private WeatherCacheHelper mHelper;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: WeatherService" );
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, WeatherService.class), 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), 6 * 60 * 60 * 1000, pi);
    }

    private static final String TAG = "WeatherService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: WeatherService" );
        mHelper = WeatherCacheHelper.getHelper(this);
        checkWeather();
        startAlarm();
        new RetryThread().start();
        return super.onStartCommand(intent, flags, startId);
    }


    private void checkWeather() {
        try {
            needToUpdate = true;
            List<WeatherCache> weatherCaches = mHelper.getWeatherDao().queryForAll();
            if (weatherCaches != null && weatherCaches.size() != 0) {
                WeatherCache weatherCache = weatherCaches.get(0);
                String date = weatherCache.getDate().substring(0, 10);
                String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                City currentCity = new City(weatherCache.getCity(),
                        weatherCache.getLatitude(), weatherCache.getLongitude());
                if (now.equals(date)) {
                    needToUpdate = false;
                    requestAfterLocated = false;
                } else {
                    requestWeather(currentCity);
                }
            } else {
                requestLocation();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            requestLocation();
        }
    }

    private void requestLocation() {
        Log.e(TAG, "requestLocation: ");
        requestAfterLocated = true;
        LocationManager.getInstance(this).initLocation();
    }

    private void requestWeather(City city) {
        WeatherManager.createManager(this).requestData(city);
    }

    @Subscribe
    public void onLocated(City city) {
        Log.e(TAG, "onLocated: in WeatherService");
        if (city.getCity() == null || !requestAfterLocated) {
            return;
        }
        Log.e(TAG, "onLocated: " + city.toString());
        requestWeather(city);
    }

    @Subscribe
    public void onWeatherLoaded(SimpleWeatherData data) {
        if (data != null) {
            needToUpdate = false;
            requestAfterLocated = false;
        }
    }

    private boolean needToUpdate = true;
    private boolean requestAfterLocated = true;

    private class RetryThread extends Thread {
        @Override
        public void run() {
            int count = 0;
            int threeMinutes = 3 * 60 * 1000;
            int tenMinutes = 10 * 60 * 1000;
            while (count < 5 && needToUpdate) {
                try {
                    if (count <= 3) {
                        Thread.sleep(threeMinutes);
                    } else if (count <= 5) {
                        Thread.sleep(tenMinutes);
                    }
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkWeather();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
