package com.easynetwork.weather.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Kent ↗↗↗ on 2016/9/5.
 */
public class WeatherProvider extends ContentProvider {

    private WeatherCacheHelper mHelper;

    @Override
    public boolean onCreate() {
        matcher.addURI("com.easynetwork.weatherData", "WeatherCache", 1);
        mHelper = WeatherCacheHelper.getHelper(getContext());
        return false;
    }

    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (matcher.match(uri) == 1) {
            return mHelper.getReadableDatabase().rawQuery("select * from WeatherCache", new String[]{});
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
