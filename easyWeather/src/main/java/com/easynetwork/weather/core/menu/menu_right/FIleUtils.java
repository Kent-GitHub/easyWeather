package com.easynetwork.weather.core.menu.menu_right;

import android.content.Context;

import com.easynetwork.weather.tools.FileUtil;

import java.util.List;

/**
 * Created by yanming on 2016/8/15.
 */
public class FIleUtils {

    private static FIleUtils mInstance;

    private Context mContext;

    private FIleUtils(Context context) {
        mContext = context;
    }

    public static FIleUtils getInstance(Context context) {

        if (mInstance == null) {
            synchronized (FileUtil.class) {
                if (mInstance == null) {
                    mInstance = new FIleUtils(context);
                }
            }
        }

        return mInstance;
    }


    public List<String> getHistory(){
        return null;
    }


    public void addHistory(String s){

    }
}
