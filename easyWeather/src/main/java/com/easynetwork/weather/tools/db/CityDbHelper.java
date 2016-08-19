package com.easynetwork.weather.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easynetwork.weather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CityDbHelper {

    private SQLiteDatabase connCity = null;
    Context context;

    public CityDbHelper(Context context) {
        this.context = context;
        initDBCity();
        if ((this.connCity == null) || (!this.connCity.isOpen()))
            this.connCity = context.openOrCreateDatabase("geo_coordinate.db", 0, null);
    }

    public void close() {
        if (this.connCity != null)
            this.connCity.close();
    }

    public Cursor getChooseCity(String paramString) {
        initDBCity();
        if ((this.connCity == null) || (!this.connCity.isOpen()))
            this.connCity = this.context.openOrCreateDatabase("geo_coordinate.db", 0, null);
        String str = "select * from city where city like '" + paramString + "%'";
        return this.connCity.rawQuery(str, null);
    }

    private static final String TAG = "CityDbHelper";

    public void initDBCity() {
        File localFile1 = this.context.getDatabasePath("geo_coordinate.db");
        File localFile2 = new File(localFile1.toString().substring(0, localFile1.toString().lastIndexOf("/")));
        if (!localFile2.exists())
            localFile2.mkdir();
        if (!localFile1.exists())
            return;
        InputStream localInputStream = this.context.getResources().openRawResource(R.raw.geo_coordinate);

        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(localFile1);
            byte[] arrayOfByte = new byte[8192];
            while (true) {
                int i = localInputStream.read(arrayOfByte);
                if (i <= 0) {
                    localFileOutputStream.close();
                    localInputStream.close();
                    return;
                }
                localFileOutputStream.write(arrayOfByte, 0, i);
            }
        } catch (Exception localException) {
        }
    }

}
