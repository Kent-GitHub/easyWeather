package com.easynetwork.weather.tools;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by yanming on 2016/8/17.
 */
public class MyLocationListener implements BDLocationListener {

    private static final String TAG = "MyLocationListener";

    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        location.getLatitude();
        location.getLongitude();
    }

}
