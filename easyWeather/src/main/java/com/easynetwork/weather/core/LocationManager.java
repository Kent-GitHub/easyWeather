package com.easynetwork.weather.core;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.tools.ToastUtil;
import com.easynetwork.weather.tools.network.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Kent ↗↗↗ on 2016/9/5.
 */
public class LocationManager implements BDLocationListener {

    private static LocationManager mInstance;

    private Context mContext;

    private LocationManager(Context context) {
        mContext = context;
    }

    public LocationClient mLocationClient = null;

    public static LocationManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocationManager.class) {
                if (mInstance == null) {
                    mInstance = new LocationManager(context);
                }
            }
        }
        return mInstance;
    }

    public void initLocation() {
        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();
    }

    private static final String TAG = "LocationManager";

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        int locType = bdLocation.getLocType();
        //定位失败处理
        if (locType != 61 && locType != 66 && locType != 161) {
            android.util.Log.e(TAG, "onReceiveLocation: local failed with code:" + locType);
            EventBus.getDefault().post(new City());
            return;
        }

        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        String currentCity = bdLocation.getAddress().city;
        android.util.Log.e(TAG, "onReceiveLocation succeed located to :" + currentCity);
        if (currentCity != null && !"".equals(currentCity)) {
            if (currentCity.endsWith("市") && !currentCity.equals("沙市") && !currentCity.equals("津市")) {
                currentCity = currentCity.substring(0, currentCity.length() - 1);
            }
            City locatedCity = new City(currentCity, latitude, longitude);
            WeatherApplication.setLocatedCity(locatedCity);
            EventBus.getDefault().post(new City(currentCity, latitude, longitude));
            return;
        }
        EventBus.getDefault().post(new City());
    }
}
