package com.easynetwork.weather.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.tools.Log;
import com.easynetwork.weather.bean.User;
import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.tools.BitmapUtil;
import com.easynetwork.weather.tools.DataLoader;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.easynetwork.weather.tools.UploadFileLoader;
import com.easynetwork.weather.view.SimpleWeatherView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
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


    private SimpleWeatherView mWeatherView;

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

        //默认值
        User user = new User();
        user.uid = "";
        user.token = "";
        setLocalUser(user);

        //得到本地user
        nLocalUser = getLocalUser();

        Log.v(TAG, "uid==" + nLocalUser.toString());

        // 默认的有本地的，以及列表是肯定需要下载的
        nIsLoadingFlags.put(nLocalUser.uid, false);
        nIsLoadingFlags.put(Constants.LIST_DATA_FILENAME, false);

    }

    /**
     * 注销，必须，因为有监听
     */
    public void destory() {
        instance = null;
    }

    /**
     * 请求所有数据，外部可以调用，如果把握不好，不推荐
     *
     * @param localFirst 先去下载本地数据，在去下载列表和亲友数据
     */
    public void requestData(boolean localFirst) {

        requestWeatherData(nLocalUser);
    }

    public void requestData(double latitude, double longitude) {
        User user = new User();
        user.lon = longitude + "";
        user.lat = latitude + "";

        requestWeatherData(user);
    }

    /**
     * 请求天气数据，外部可以调用，如果把握不好，不推荐
     */
    public void requestWeatherData(User user) {
        WeatherDataLoadTask wTask = new WeatherDataLoadTask(user);
        wTask.execute();
    }

    /**
     * 保存绘制提醒图片
     */
    public boolean saveRemindBitmap(String uId, HashMap<String, Bitmap> bitmapMap) {
        boolean bool = false;
        String filesDir = nContext.getFilesDir() + Constants.WEATHER_REMIND_FOLDER + File.separator;
        if (!TextUtils.isEmpty(uId)) {
            filesDir = nContext.getFilesDir() + Constants.WEATHER_REMIND_FOLDER + File.separator + uId + File.separator;
        }
        if (bitmapMap != null && !bitmapMap.isEmpty()) {
            for (String key : bitmapMap.keySet()) {
                bool = BitmapUtil.saveBitmap2file(bitmapMap.get(key), filesDir + key);
                //bool = BitmapUtil.saveBitmap2file(bitmapMap.get(key), new File("/sdcard/tq/"+key));
                if (!bool) {
                    break;
                }
            }
        }
        return bool;
    }

    /**
     * 上传提醒状态
     */
    public void getUploadRemindState(String uId) {
        RemindStateTask rsTask = new RemindStateTask();
        rsTask.execute(uId);
    }

    /**
     * 上传提醒图片
     */
    public void uploadRemindBitmap(String uId) {
        RemindBitmapTask rbTask = new RemindBitmapTask();
        rbTask.execute(uId);
    }

    /**
     * @return 得到本地的用户
     */
    public User getLocalUser() {
        if (nLocalUser == null) {
            nLocalUser = new User();
        }
        nLocalUser.name = SharedPreUtil.getGlobalVar(nContext, "user_name", "local");
        nLocalUser.city = SharedPreUtil.getGlobalVar(nContext, "user_city", "未知");
        nLocalUser.uid = SharedPreUtil.getGlobalVar(nContext, "user_uid", "local");
        // TODO: 2016/8/13 指定了位置
        nLocalUser.lon = SharedPreUtil.getGlobalVar(nContext, "user_lon", "114.05");
        nLocalUser.lat = SharedPreUtil.getGlobalVar(nContext, "user_lat", "22.53");
        nLocalUser.location = SharedPreUtil.getGlobalVar(nContext, "user_location", "");
        return nLocalUser;
    }

    /**
     * 设置本地的用户
     */
    public void setLocalUser(User user) {
        if (nLocalUser == null) {
            nLocalUser = new User();
        }
        String userId = "local";
        String userToken = "";
        // TODO: 2016/8/13  注释部分代码
//		try {
//			EasyUserManager easyUserManager = (EasyUserManager) nContext.getSystemService(Context.EASY_USER_SERVICE);
//			EasyUser easyUser = easyUserManager.getCurrentUser();
//			if(easyUser.getAccount().isActive){
//				userId = easyUser.getAccount().uid;
//				userToken = easyUser.getAccount().token;
//			}
//		} catch (NoClassDefFoundError e) {
//
//		}
        user.uid = userId;
        user.token = userToken;

        if (user != null) {
            if (!TextUtils.isEmpty(user.name)) {
                SharedPreUtil.setGlobalVar(nContext, "user_name", user.name);
                nLocalUser.name = user.name;
            }
            if (!TextUtils.isEmpty(user.city) && !"未知".equals(user.city)) {
                SharedPreUtil.setGlobalVar(nContext, "user_city", user.city);
                nLocalUser.city = user.city;
            }
            if (!TextUtils.isEmpty(user.uid)) {
                SharedPreUtil.setGlobalVar(nContext, "user_uid", user.uid);
                nLocalUser.uid = user.uid;
            }
            if (!TextUtils.isEmpty(user.lon)) {
                SharedPreUtil.setGlobalVar(nContext, "user_lon", user.lon);
                nLocalUser.lon = user.lon;
            }
            if (!TextUtils.isEmpty(user.lat)) {
                SharedPreUtil.setGlobalVar(nContext, "user_lat", user.lat);
                nLocalUser.lat = user.lat;
            }
            if (!TextUtils.isEmpty(user.token)) {
                SharedPreUtil.setGlobalVar(nContext, "user_token", user.token);
                nLocalUser.token = user.token;
            }
            if (!TextUtils.isEmpty(user.location)) {
                SharedPreUtil.setGlobalVar(nContext, "user_location", user.location);
                nLocalUser.location = user.location;
            }
        }
    }


    /**
     * 保存天气数据
     *
     * @param filename       文件名
     * @param weatherWrapper 数据
     */
    private void saveWeatherData(String filename, WeatherWrapper weatherWrapper) {
        // 如果数据为空或者解析没有完成
        if (weatherWrapper == null || !weatherWrapper.done) {
            return;
        }
    }


    /**
     * /**
     * 得到当前的日期，1221表示12月21号
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String getLocalDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMdd");
        return format.format(date);
    }

    /**
     * 数据下载监听器
     *
     * @param listener
     */
    public void setLoadListener(DataLoadListener listener) {
        nDataLoadListener = listener;
    }

    public boolean isLoading(User user) {
        return nIsLoadingFlags.get(user.uid) == null ? false : nIsLoadingFlags.get(user.uid);
    }

    public boolean isListLoading() {
        return nIsLoadingFlags.get(Constants.LIST_DATA_FILENAME);
    }


    /**
     * 数据下载监听接口
     *
     * @author WenYF
     */
    interface DataLoadListener {
        public void onWeatherDataStartLoad(User user);

        public void onWeatherDataLoaded(WeatherWrapper data);

        public void onListDataStartLoad();

        public void onListDataLoaded();

        public void onUploadRemindState(String result);

        public void onUploadRemindBitmaped(String result);

        /**背景测试*/
        //public void onBackTested(String result);
    }

    class WeatherDateDownloadTask extends AsyncTask<City, Object, SimpleWeatherData> {

        private City mCity;

        @Override
        protected SimpleWeatherData doInBackground(City... cities) {
            SimpleWeatherData data;
//            data=mwe
            return null;
        }
    }

    /**
     * 下载天气数据异步任务
     *
     * @author WenYF
     */
    class WeatherDataLoadTask extends AsyncTask<User, String, WeatherWrapper> {

        /**
         * 目前没有任何意义
         */
        private boolean nnIsSuccess = false;
        private User nnUser;

        public WeatherDataLoadTask(User user) {
            nnUser = user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (nDataLoadListener != null) {
                nDataLoadListener.onWeatherDataStartLoad(nnUser);
            }
        }

        @Override
        protected WeatherWrapper doInBackground(User... paras) {
            // 这里下载数据

            // 如果本地有数据，得到服务器下发的时间标记
            WeatherWrapper weatherWrapper;
            String timeFlag = "-1";

            timeFlag = System.currentTimeMillis() / 1000 + "";

            // 请求服务器获取数据
            //weatherWrapper = nWeatherLoader.getWeatherData(nnUser, timeFlag);
            weatherWrapper = nWeatherLoader.getWeatherData(nnUser.lat, nnUser.lon);

            // 没有意义
            nnIsSuccess = true;

            // 保存本地数据
            saveWeatherData(nnUser.uid, weatherWrapper);

            return weatherWrapper;
        }

        @Override
        protected void onPostExecute(WeatherWrapper data) {
            super.onPostExecute(data);
            if (!nnIsSuccess) {
                //
            }

            nIsLoadingFlags.put(nnUser.uid, false);

            // 通知
            if (nDataLoadListener != null && nnIsSuccess) {
                nDataLoadListener.onWeatherDataLoaded(data);
            }

        }
    }


    class RemindStateTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("sid", params[0]);
            param.put("uid", SharedPreUtil.getGlobalVar(nContext, "user_uid"));
            param.put("token", SharedPreUtil.getGlobalVar(nContext, "user_token"));
            UploadFileLoader upLoader = new UploadFileLoader();
            String result = upLoader.getRemindState(param);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (nDataLoadListener != null) {
                nDataLoadListener.onUploadRemindState(result);
            }
        }
    }

    class RemindBitmapTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String userid = params[0];
            String filePath = nContext.getFilesDir() + Constants.WEATHER_REMIND_FOLDER + File.separator;
            if (!TextUtils.isEmpty(userid)) {
                filePath = filePath + userid + File.separator;
            }
            Map<String, String> param = new HashMap<String, String>();
            param.put("sid", userid);
            param.put("token", SharedPreUtil.getGlobalVar(nContext, "user_token"));
            UploadFileLoader up = new UploadFileLoader();
            String result = up.UploadFileSync(Constants.WEATHER_PHOTO_URL, BitmapUtil.getBmpFiles(filePath), param);
            return result;
        }

        @Override
        protected void onPostExecute(String datas) {
            super.onPostExecute(datas);
            // 通知
            if (nDataLoadListener != null) {
                nDataLoadListener.onUploadRemindBitmaped(datas);
            }
        }
    }


    /**
     * 背景测试数据
     */
    /*public void requestBTestData(){
        BackTestTask tTask = new BackTestTask();
		tTask.execute();
	}
	
	class BackTestTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			return nWeatherLoader.getBackTestData();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 通知
			if (nDataLoadListener != null) {
				nDataLoadListener.onBackTested(result);
			}
		}
	}*/
}
