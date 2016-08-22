package com.easynetwork.weather.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherBean;
import com.easynetwork.weather.tools.Log;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 单例模式<br>
 * 每次网络变化和构造都会请求天气，携带参数是本地经纬度以及服务器下发的时间<br>
 * 管理天气数据，亲友和本地数据
 *
 * @author WenYF
 */
public class WeatherManager {
    private final static String TAG = "WeatherManager";

    /**
     * 实例，通过createManager(context)来构建
     */
    private static WeatherManager instance;

    public static WeatherManager createManager(Context context) {
        if (instance != null) {
            return instance;
        }

        instance = new WeatherManager(context);

        return instance;
    }

    private Context mContext;

    /**
     * 数据下载成功通知
     */
    private DataLoadListener nDataLoadListener;

    /**
     * 构造函数，初始化，检测文件过期，注册监听
     */
    @SuppressLint("ServiceCast")
    public WeatherManager(Context context) {
        mContext = context;
    }

    /**
     * 注销，必须，因为有监听
     */
    public void destroy() {
        instance = null;
    }


    public void requestData(City city) {
        WeatherDateDownloadTask wTask = new WeatherDateDownloadTask(city);
        wTask.execute();
    }

    /**
     * 数据下载监听器
     */
    public void setLoadListener(DataLoadListener listener) {
        nDataLoadListener = listener;
    }

    /**
     * 数据下载监听接口
     *
     * @author WenYF
     */
    interface DataLoadListener {
        void onWeatherDataStartLoad();

        void onWeatherDataLoaded(SimpleWeatherData data);
    }

    class WeatherDateDownloadTask extends AsyncTask<City, Object, SimpleWeatherData> {

        private City mCity;

        public WeatherDateDownloadTask(City city) {
            mCity = city;
        }

        @Override
        protected SimpleWeatherData doInBackground(City... cities) {
            SimpleWeatherData data;
            data = getSimpleWeatherData(mCity.getCity(), mCity.getLatitude() + "", mCity.getLongitude() + "");
            return data;
        }

        @Override
        protected void onPostExecute(SimpleWeatherData simpleWeatherData) {
            nDataLoadListener.onWeatherDataLoaded(simpleWeatherData);
        }
    }

    public SimpleWeatherData getSimpleWeatherData(String city, String latitude, String longitude) {
        SimpleWeatherData data;
        long time = System.currentTimeMillis() / 1000;
        String jsonResult;
        // TODO: 2016/8/16 更换查询接口
        String httpUrl;
        if (useURL1) {
            httpUrl = Constants.WEATHER_DATA_URL1 + "&w=" + latitude + "&j=" + longitude + "&t=" + time;
        } else {
            httpUrl = Constants.WEATHER_DATA_URL + "&w=" + latitude + "&j=" + longitude + "&t=" + time;
        }
        android.util.Log.d(TAG, "getSimpleWeatherData_httpUrl: " + httpUrl);
        jsonResult = request(httpUrl);
        if (jsonResult == null) {
            return null;
        }

        int start = jsonResult.indexOf("{");
        if (start == -1) return null;
        jsonResult = jsonResult.substring(start);


        Gson gson = new Gson();
        WeatherBean bean;
        try {
            bean = gson.fromJson(jsonResult, WeatherBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        if (!bean.getErrNum().equals("200")) {
            android.util.Log.e(TAG, "getSimpleWeatherData_ErrNum(): " + bean.getErrNum());
            return null;
        }
        if (city != null) {
            bean.setCity(city);
        }
        String jsonString;

        jsonString = gson.toJson(bean);

        data = new SimpleWeatherData(bean);
        data.setResultCode(resultCode);
        String stamp = System.currentTimeMillis() / 1000 + "";
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_JSON, jsonString);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_LATITUDE, latitude);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_LONGITUDE, longitude);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_STAMP, stamp);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_CITY, city);
        android.util.Log.e(TAG, "getSimpleWeatherData: saved");
        return data;
    }

    private boolean useURL1 = false;

    private int resultCode;

    /**
     * 请求一个url，并得到json数据
     *
     * @param httpUrl url
     * @return 数据
     */
    private String request(String httpUrl) {
        BufferedReader reader;
        String result;
        StringBuilder sbf = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            resultCode = connection.getResponseCode();
            android.util.Log.e(TAG, "request result with code:" + resultCode + ",describe:" + connection.getResponseMessage());
            if (connection.getResponseCode() == 304) {
                return null;
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            result = null;
            Log.e(TAG, "request error : " + e.toString(), e);
        }
        return result;
    }

}
