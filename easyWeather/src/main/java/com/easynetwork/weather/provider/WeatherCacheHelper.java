package com.easynetwork.weather.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.easynetwork.weather.bean.WeatherCache;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class WeatherCacheHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "weatherCache.db";

    private WeatherCacheHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, WeatherCache.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "DatabaseHelper";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    private static WeatherCacheHelper instance;

    public static synchronized WeatherCacheHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (WeatherCacheHelper.class) {
                if (instance == null)
                    Log.e(TAG, "getHelper DatabaseHelper new instance ");
                instance = new WeatherCacheHelper(context);
            }
        }

        return instance;
    }

    public Dao<WeatherCache, Integer> getWeatherDao() {
        try {
            return getDao(WeatherCache.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
