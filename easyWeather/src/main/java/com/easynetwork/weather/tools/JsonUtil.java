package com.easynetwork.weather.tools;

import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherBean;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    /**
     * getJSON(将用json格式字符串转JSONObject对象)
     *
     * @param jsonString
     * @return
     */
    public static JSONObject getJSON(String jsonString) {
        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * getJsonArray(将用json格式字符串转JSONArray对象)
     *
     * @param jsonString
     * @return
     */
    public static JSONArray getJsonArray(String jsonString) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    /**
     * getMap(将用json格式字符串转换为 map对象)
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject = getJSON(jsonString);
        Map<String, Object> valueMap = new HashMap<String, Object>();
        try {
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return valueMap;
    }

    /**
     * getList(把json 转换为 ArrayList 形式)
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getList(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = getJsonArray(jsonString);
        try {
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据参数,值封装成 json字符串
     *
     * @param title   json头
     * @param params  键
     * @param params2 值
     * @return json字符串
     * @throws JSONException json封装不匹配
     */
    public static String createJSON(String title, String[] params, Object[] params2) {
        JSONObject titleNode = new JSONObject();
        JSONObject paramsNode = new JSONObject();

        try {
            for (int i = 0; i < params.length; i++) {
                paramsNode.put(params[i], params2[i]);
                titleNode.put(title, paramsNode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titleNode.toString();
    }

    public static SimpleWeatherData jsonToSWD(String json) {
        Gson gson = new Gson();
        WeatherBean bean = gson.fromJson(json, WeatherBean.class);
        return new SimpleWeatherData(bean);
    }

}
