package com.easynetwork.weather.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherBean;
import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.core.Constants;
import com.easynetwork.weather.bean.AirQualityData;
import com.easynetwork.weather.bean.DailyWeatherData;
import com.easynetwork.weather.bean.NowWeatherData;
import com.google.gson.Gson;

/**
 * 下载天气数据的loader<br>
 *
 * @author WenYF
 */
public class DataLoader {
    private final static String TAG = "WeatherLoader";

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
        WeatherBean bean = gson.fromJson(jsonResult, WeatherBean.class);
        if (!bean.getErrNum().equals("200")) {
            return null;
        }
        if (city != null) {
            bean.setCity(city);
        }
        String jsonString = gson.toJson(bean);
        String stamp = System.currentTimeMillis() / 1000 + "";
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_JSON, jsonString);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_LATITUDE, latitude);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_LONGITUDE, longitude);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_STAMP, stamp);
        SharedPreUtil.saveSimpleData(WeatherApplication.context, Constants.SD_CITY, city);
        android.util.Log.e(TAG, "getSimpleWeatherData: saved");
        data = new SimpleWeatherData(bean);
        return data;
    }

    private boolean useURL1;

    /**
     * 请求一个url，并得到json数据
     *
     * @param httpUrl url
     * @return 数据
     */
    private String request(String httpUrl) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
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
            Log.w(TAG, connection.getResponseCode() + "   " + connection.getResponseMessage());

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

    public interface OnWeatherLoadedListener {
        void weatherLoaded();
    }

    private OnWeatherLoadedListener mListener;

    public void setListener(OnWeatherLoadedListener mListener) {
        this.mListener = mListener;
    }

    public OnWeatherLoadedListener getListener() {
        return mListener;
    }
}
