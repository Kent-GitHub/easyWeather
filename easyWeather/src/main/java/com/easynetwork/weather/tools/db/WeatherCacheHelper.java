package com.easynetwork.weather.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherBean;
import com.google.gson.Gson;

/**
 * Created by yanming on 2016/8/18.
 */
public class WeatherCacheHelper {

    private WeatherCacheHelper mInstance;

    private Context mContext;

    private MyDBHelper mDBHelper;

    private WeatherCacheHelper(Context context) {
        mContext = context;
        mDBHelper = new MyDBHelper(context);
    }

    public WeatherCacheHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WeatherCacheHelper.class) {
                if (mInstance == null) {
                    mInstance = new WeatherCacheHelper(context);
                }
            }
        }
        return mInstance;
    }

    public SimpleWeatherData getDateByCity(String city) {
        SimpleWeatherData date = null;
        Cursor cursor = mDBHelper.getReadableDatabase().rawQuery("select * from weather where city like '" + city + "%'", null);
        if (cursor.moveToNext()) {
            date = new SimpleWeatherData();
            String jsonString = cursor.getString(cursor.getColumnIndex("jsonText"));
            Gson gson = new Gson();
            WeatherBean weatherBean = gson.fromJson(jsonString, WeatherBean.class);
        }
        return date;
    }

    class MyDBHelper extends SQLiteOpenHelper {
        private static final String TABLE_NAME = "weather";

        private static final String DB_NAME = "weatherCache.db";

        public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public MyDBHelper(Context context) {
            this(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + "(" +
                    "_id integer primary key autoincrement," +
                    "city text," +
                    "timeStamp text," +
                    "jsonText text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }
    }
}
