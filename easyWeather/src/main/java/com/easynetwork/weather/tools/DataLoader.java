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

import android.text.TextUtils;

import com.easynetwork.common.util.log.Log;
import com.easynetwork.weather.bean.EasyWeatherWrapper;
import com.easynetwork.weather.bean.FamilyList;
import com.easynetwork.weather.bean.User;
import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.core.Constants;
import com.easynetwork.weather.bean.AirQualityData;
import com.easynetwork.weather.bean.DailyWeatherData;
import com.easynetwork.weather.bean.NowWeatherData;

/**
 * 下载天气数据的loader<br>
 * {@link #getFamilyList(String)} 得到亲简单数据的列表<br>
 * {@link #getWeatherData(User, String)} 得到天气数据<br>
 *
 * @author WenYF
 */
public class DataLoader {
    private final static String TAG = "WeatherLoader";

    /**
     * 通过本地的uid得到用户亲友所有的简单天气数据列表
     *
     * @param localUid
     * @return
     */
    public FamilyList getFamilyList(String localUid) {
        FamilyList listDatas = new FamilyList();
        if (TextUtils.isEmpty(localUid) || localUid.equals("local")) {
            return listDatas;
        }
        String httpUrl = Constants.FAMILY_LIST_URL + "puid=" + localUid;
        String jsonResult = request(httpUrl);

        if (jsonResult == null) {
            return listDatas;
        }

        try {
            JSONObject json = new JSONObject(jsonResult);
            parseFamilyListData(listDatas, json);
        } catch (JSONException e) {
            Log.e(TAG, e.toString(), e);
        }

        return listDatas;
    }

    /**
     * 解析亲友列表数据
     *
     * @param datas 解析到
     * @param json  数据源
     * @throws JSONException 任何json解析错误抛出异常处理
     */
    private void parseFamilyListData(FamilyList datas, JSONObject json)
            throws JSONException {
        String errNum = json.getString("errNum");
        if (!"200".equals(errNum)) {
            return;
        }

        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            JSONObject dataJson = (JSONObject) array.get(i);
            User user = new User();
            NowWeatherData weather = new NowWeatherData();

            if (!dataJson.getString("weather").equals("empty")) {
                JSONObject weatherJson = dataJson.getJSONObject("weather");
                //JSONObject condJson = weatherJson.getJSONObject("cond");

                user.city = weatherJson.getString("city");

                //weather.code = condJson.getString("code");
                //weather.txt = condJson.getString("txt");
                weather.tmp = weatherJson.getString("tmp");
                weather.code = weatherJson.getString("cond");
            }

            user.name = dataJson.getString("name");
            user.uid = dataJson.getString("uid");

            user.lon = dataJson.getString("j").equals("empty") ? "" : dataJson.getString("j");
            user.lat = dataJson.getString("w").equals("empty") ? "" : dataJson.getString("w");

            EasyWeatherWrapper dataWrapper = new EasyWeatherWrapper();
            dataWrapper.nUser = user;
            dataWrapper.nWeather = weather;

            datas.add(dataWrapper);
        }

        // 解析没有错误，顺利完成
        datas.done = true;

    }

    /**
     * 得到用户的天气数据
     *
     * @param user 用户
     * @param time 上次请求，服务器下发的time stamp
     * @return 该用户的天气数据
     */
    public WeatherWrapper getWeatherData(User user, String time) {
        WeatherWrapper wrapper = new WeatherWrapper();
        wrapper.nUser = user;
        if (TextUtils.isEmpty(user.lat) || TextUtils.isEmpty(user.lon)) {
            return wrapper;
        }
        String jsonResult = "";
        // TODO: 2016/8/16 更换查询接口
        String httpUrl;
        if (useURL1) {
            httpUrl = Constants.WEATHER_DATA_URL1 + "&w=" + user.lat + "&j=" + user.lon + "&location=" + user.location + "&t=" + time;
        } else {
            httpUrl = Constants.WEATHER_DATA_URL + "&w=" + user.lat + "&j=" + user.lon + "&location=" + user.location + "&t=" + time;
        }
        android.util.Log.e(TAG, "getWeatherData: " + httpUrl);
        //String httpUrl = "http://mobile.pingyijinren.com/weather/weather/index?v=1.0&w=36.19&j=117.67&t=-1";
        jsonResult = request(httpUrl);
        if (jsonResult == null) {
            return wrapper;
        }

        Log.i(TAG, jsonResult);
        JSONObject json;
        Log.w(TAG, "wrapper.nUser = " + wrapper.nUser.uid);
        try {
            int start = jsonResult.indexOf("{");
            if (start == -1) return wrapper;
            jsonResult = jsonResult.substring(start);
            json = new JSONObject(jsonResult);
            android.util.Log.e(TAG, "getWeatherData: json.toString()" + json.toString());
            parseWeatherData(wrapper, json);
        } catch (JSONException e) {
            Log.e(TAG, e.toString(), e);
        }

        return wrapper;
    }


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
        android.util.Log.e(TAG, "getWeatherData: " + httpUrl);
        //String httpUrl = "http://mobile.pingyijinren.com/weather/weather/index?v=1.0&w=36.19&j=117.67&t=-1";
        jsonResult = request(httpUrl);
        if (jsonResult == null) {
            return wrapper;
        }

        Log.i(TAG, jsonResult);
        JSONObject json;
        Log.w(TAG, "wrapper.nUser = " + wrapper.nUser.uid);
        try {
            int start = jsonResult.indexOf("{");
            if (start == -1) return wrapper;
            jsonResult = jsonResult.substring(start);
            json = new JSONObject(jsonResult);
            android.util.Log.e(TAG, "getWeatherData: json.toString()" + json.toString());
            parseWeatherData(wrapper, json);
        } catch (JSONException e) {
            Log.e(TAG, e.toString(), e);
        }

        return wrapper;
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
        // 如果获取状态不是ok，返回false
        if (!"200".equals(errNum)) {
            return;
        }

        json = json.getJSONObject("data");

        // 得到basic的json数据
        JSONObject basicJson = json.getJSONObject("basic");
        if (basicJson != null) {
            wrapper.nUser.city = basicJson.getString("city");
            android.util.Log.e(TAG, "parseWeatherData: nUser.city" + basicJson.getString("city"));
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


        // 得到警报的json
//        List<WeatherAlarm> alarmList = null;
//        if (!json.getString("alarms").equals("empty")) {
//            JSONArray alarmArray = null;
//            try {
//                alarmArray = json.getJSONArray("alarms");
//            } catch (JSONException e) {
//                Log.w(TAG, e.toString(), e);
//            }
//
//            alarmList = new ArrayList<WeatherAlarm>();
//            for (int i = 0; i < alarmArray.length(); i++) {
//                JSONObject alarmJson = (JSONObject) alarmArray.get(i);
//                WeatherAlarm alarm = null;
//                if (alarmJson != null) {
//                    alarm = new WeatherAlarm();
//                    alarm.level = alarmJson.getString("level");
//                    //alarm.stat = alarmJson.getString("stat");
//                    //alarm.title = alarmJson.getString("title");
//                    alarm.txt = alarmJson.getString("txt");
//                    alarm.type = alarmJson.getString("type");
//                    alarmList.add(alarm);
//                }
//            }
//
//        }
//        wrapper.nAlarmList = alarmList;

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
