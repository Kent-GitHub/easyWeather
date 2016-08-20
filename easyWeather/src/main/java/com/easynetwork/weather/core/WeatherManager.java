package com.easynetwork.weather.core;

import java.util.HashMap;

import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.tools.Log;
import com.easynetwork.weather.bean.User;
import com.easynetwork.weather.tools.DataLoader;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.easynetwork.weather.view.SimpleWeatherView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

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

    public static WeatherManager getInstance() {
        return instance;
    }

    public static WeatherManager createManager(Context context) {
        if (instance != null) {
            return instance;
        }

        instance = new WeatherManager(context);

        return instance;
    }

    /**
     * 上下文
     */
    private Context nContext;
    /**
     * 本地的账号
     */
    private User nLocalUser;
    /**
     * 数据下载成功通知
     */
    private DataLoadListener nDataLoadListener;
    /**
     * 数据请求工具
     */
    private DataLoader nWeatherLoader;
    /**
     * 在下载的时候，界面层是不知道数据是否下载完成或者成功。<br>
     * 有时候进入界面的时候，要去询问是否正在下载，因此，这里保存这个值
     */
    private HashMap<String, Boolean> nIsLoadingFlags;

    /**
     * 构造函数，初始化，检测文件过期，注册监听
     *
     * @param context
     */
    @SuppressLint("ServiceCast")
    public WeatherManager(Context context) {
        this.nContext = context;
        nWeatherLoader = new DataLoader();
        nIsLoadingFlags = new HashMap<String, Boolean>();
    }

    /**
     * 注销，必须，因为有监听
     */
    public void destory() {
        instance = null;
    }


    public void requestData(City city) {
        WeatherDateDownloadTask wTask = new WeatherDateDownloadTask(city);
        wTask.execute();
    }

    /**
     * 数据下载监听器
     *
     * @param listener
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
        void onWeatherDataStartLoad(User user);

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
            data = nWeatherLoader.getSimpleWeatherData(mCity.getCity(), mCity.getLatitude() + "", mCity.getLongitude() + "");
            return data;
        }

        @Override
        protected void onPostExecute(SimpleWeatherData simpleWeatherData) {
            nDataLoadListener.onWeatherDataLoaded(simpleWeatherData);
        }
    }
}
