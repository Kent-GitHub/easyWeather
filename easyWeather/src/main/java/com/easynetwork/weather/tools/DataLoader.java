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

    public WeatherWrapper getWeatherData(String latitude, String longitude) {
        long time = System.currentTimeMillis() / 1000;
        WeatherWrapper wrapper = new WeatherWrapper();

        String jsonResult = "";
        // TODO: 2016/8/16 更换查询接口
        String httpUrl;
        if (useURL1) {
            httpUrl = Constants.WEATHER_DATA_URL1 + "&w=" + latitude + "&j=" + longitude + "&t=" + time;
        } else {
            httpUrl = Constants.WEATHER_DATA_URL + "&w=" + latitude + "&j=" + longitude + "&t=" + time;
        }
        android.util.Log.d(TAG, "getWeatherData_httpUrl: " + httpUrl);
        jsonResult = request(httpUrl);
        if (jsonResult == null) {
            return wrapper;
        }
        JSONObject json;
        try {
            int start = jsonResult.indexOf("{");
            if (start == -1) return wrapper;
            jsonResult = jsonResult.substring(start);
            json = new JSONObject(jsonResult);
            android.util.Log.d(TAG, "getWeatherData_json.toString(): " + json.toString());
            parseWeatherData(wrapper, json);
        } catch (JSONException e) {
            Log.e(TAG, e.toString(), e);
        }

        return wrapper;
    }

    public SimpleWeatherData getSimpleWeatherData(String latitude, String longitude) {
        return getSimpleWeatherData(null, latitude, longitude);
    }

    public SimpleWeatherData getSimpleWeatherData(String city, String latitude, String longitude) {
        SimpleWeatherData data = null;
        long time = System.currentTimeMillis() / 1000;
        String jsonResult = "";
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
        Gson gson = new Gson();
        WeatherBean bean = gson.fromJson(jsonResult, WeatherBean.class);
        if (!bean.getErrNum().equals("200")) {
            return null;
        }
        if (city != null) {
            bean.setCity(city);
        }
        String jsonString = gson.toJson(bean);
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

    /**
     * 解析天气数据
     *
     * @param wrapper 数据解析到
     * @param json
     * @throws JSONException
     */
    private void parseWeatherData(WeatherWrapper wrapper, JSONObject json) throws JSONException {
        String errNum = json.getString("errNum");
        if (!"200".equals(errNum)) {
            return;
        }

        json = json.getJSONObject("data");

        // 得到basic的json数据
        JSONObject basicJson = json.getJSONObject("basic");
        if (basicJson != null) {
            wrapper.nUser.city = basicJson.getString("city");
            wrapper.nUpdate = basicJson.getString("update");
        }
        // 天气日预报
        JSONArray dailyArray = json.getJSONArray("daily_forecast");
        for (int i = 0; i < dailyArray.length(); i++) {
            JSONObject dailyJson = (JSONObject) dailyArray.get(i);
            JSONObject condJson = dailyJson.getJSONObject("cond");
            JSONObject tmpJson = dailyJson.getJSONObject("tmp");

            DailyWeatherData dailyWeatherData = new DailyWeatherData();
            dailyWeatherData.code = condJson.getString("code");
            dailyWeatherData.maxTmp = tmpJson.getString("max");
            dailyWeatherData.minTmp = tmpJson.getString("min");
            dailyWeatherData.txt = condJson.getString("day");
            if (useURL1) {
                dailyWeatherData.describe = condJson.getString("day");
            } else {
                dailyWeatherData.describe = condJson.getString("abstract");
            }
            dailyWeatherData.date = dailyJson.getString("date");
            if (i == 0) {
                wrapper.setToday(dailyWeatherData);
            }

            if (i == 1) {
                wrapper.setTomorrow(dailyWeatherData);
            }

            if (i == 2) {
                wrapper.setAfterTomorrow(dailyWeatherData);
            }
        }

        JSONObject aqiJson = null;
        if (!json.getString("aqi").equals("empty")) {
            try {
                aqiJson = json.getJSONObject("aqi");
            } catch (JSONException e) {
                Log.w(TAG, e.toString(), e);
            }
        }
        // 得到空气质量的json
        AirQualityData airQualityData = new AirQualityData();
        if (aqiJson != null) {
            airQualityData.txt = aqiJson.getString("qlty");
            airQualityData.aqi = aqiJson.getString("aqi");
        }
        wrapper.nAirQuality = airQualityData;

        // 实时天气
        JSONObject nowJson = json.getJSONObject("now");
        NowWeatherData nowWeather = new NowWeatherData();
        if (nowJson != null) {
            JSONObject condJson = nowJson.getJSONObject("cond");
            nowWeather.tmp = nowJson.getString("tmp");
            nowWeather.code = condJson.getString("code");
            nowWeather.txt = condJson.getString("txt");
        }
        wrapper.nNow = nowWeather;

        wrapper.nCompareTmp = json.getString("highlow");

        wrapper.nTime = json.getString("timestamp");

        // 解析没有错误，顺利完成
        wrapper.done = true;

    }

    /**
     * 得到背景编码测试数据
     *
     * @return
     */
    public String getBackTestData() {
        Random random = new Random();
        int count = random.nextInt(60);
        count = count == 0 ? 1 : count;
        String countStr = count < 10 ? "0" + String.valueOf(count) : String.valueOf(count);
        String httpUrl = "http://mobile.pingyijinren.com/weather/test/index?count=" + countStr;
        String jsonResult = request(httpUrl);
        if (jsonResult == null) {
            return null;
        }
        return jsonResult;
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
