package com.easynetwork.weather.core;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easynetwork.weather.tools.Log;
import com.easynetwork.weather.tools.network.NetworkUtil;
import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.tools.StatusBarUtils;
import com.easynetwork.weather.tools.TextSpeakControl;
import com.easynetwork.weather.tools.ToastUtil;
import com.easynetwork.weather.tools.ViewUtil;
import com.easynetwork.weather.tools.network.Network;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.User;
import com.easynetwork.weather.core.menu.menu_left.MenuLeft;
import com.easynetwork.weather.core.menu.menu_right.MenuRight;
import com.easynetwork.weather.tools.JsonUtil;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.easynetwork.weather.view.SimpleWeatherView;
import com.easynetwork.weather.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import me.tangke.slidemenu.SlideMenu;

/**
 * 作者是一个很懒的小伙伴，很多界面布局和数值都是直接给，没有通过xml来索引。<br>
 * 如果你无法适应这样做，请不要修改代码，除非你很了解里面的数字代表的意思。<br>
 * 很多界面以及布局都是在代码里面直接写，这一点你必须了解。<br>
 * 开发模式是单例管理和MVC的混合模式<br>
 * 数据来源采用有则显示，没有用默认。数据改变，界面立即改变<br>
 * {@link WeatherManager}承担着一切数据管理角色，这里activity则是一个可以观察的窗口来和用户进行交互。<br>
 * 如果有任何不明白，请发邮件到 {@email bludawn@foxmail.com}<br>
 *
 * @author WenYF
 * @date 2015/9/10
 */
public class WeatherActivity extends Activity implements WeatherManager.DataLoadListener,
        SlideMenu.OnSlideStateChangeListener, BDLocationListener, View.OnClickListener {

    private final static String TAG = "WeatherActivityMYMY";
    private final static boolean log2file = true;
    private int RESULT_PHONE_BAND_CODE = 1;

    private final static int Status_Left = 0x101;
    private final static int Status_Right = 0x102;
    private final static int Status_Normal = 0x103;
    private int layoutStatus = Status_Normal;


    public LocationClient mLocationClient = null;

    /**
     * 天气数据下载等管理器
     */
    private WeatherManager nWeatherManager;

    /**
     * 一个本地的数据
     */
    @SuppressWarnings("unused")
    private WeatherWrapper nWeatherWrapper;

    /**
     * 正在显示谁的天气数据
     */
    private User nCurrentUser;
    /**
     * 当前是不是显示的list
     */
    @SuppressWarnings("unused")
    private boolean nIsFamilyList;

    /**
     * list界面回来保持scroll位置不变
     */
    @SuppressWarnings("unused")
    private Point nListPosition;

    /**
     * 左边抽屉
     */
    private MenuLeft mMenuLeft;
    /**
     * 右边抽屉
     */
    private MenuRight mMenuRight;
    /**
     * 显示天气状况的View
     */
    private SimpleWeatherView mWeatherView;
    /**
     * 主界面以及两边抽屉
     */
    private SlideMenu mSlideMenu;

    private TextSpeakControl ttsControl;

    /**
     * 是否正在提醒
     */
    private boolean isReminding = false;

    // 网络变化变化时的广播接收器
    private BroadcastReceiver nNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w(TAG, "onReceive");

            // 收到的广播是否是CONNECTIVITY_ACTION
            if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.w(TAG, "网络状态已经改变");
                if (nWeatherManager == null) {
                    return;
                }
                if (nCurrentUser != null) {

                }
                // 得到目前的网络类型
                Network type = NetworkUtil.getCurrentNetwork(context);
                Log.w(TAG, "network type = " + type.name());

                // 目前有网络，请求数据
                if (type.ordinal() < Network.NT_NONE.ordinal() && nCurrentUser != null) {
                    if (SharedPreUtil.getGlobalVar(WeatherActivity.this, "user_uid", "local").equals(nCurrentUser.uid)) {//本身用户
                        Intent locationIntent = new Intent("com.pingyijinren.location.ACTION_GET_LOCATION");
                        // 加上标志，避免定位应用安装后从来没有启动过和被用户手动强制停止后无法接收到
                        locationIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        sendBroadcast(locationIntent);
                    } else {//亲友用户
                        nWeatherManager.requestWeatherData(nCurrentUser);
                    }
                }
            }
        }
    };

    BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.pingyijinren.location.ACTION_RETURN_LOCATION")) {
                Bundle data = intent.getExtras();
                //1表示定位成功，2表示定位失败:定位失败则只有errorCode和errorMsg信息，不会有其他信息；定位成功则返回全部信息
                int errorCode = data.getInt("errorCode");
                String errorMsg = data.getString("errorMsg");//错误信息
                android.util.Log.e(TAG, "onReceive_errorCode: " + errorCode);
                double longitude = data.getDouble("longitude", 113.7582310001);//经度
                double latitude = data.getDouble("latitude", 23.0269970000);//纬度

                String city = data.getString("city");//城市
                String province = data.getString("province");
                String district = data.getString("district");

                if (errorCode == 1 && nWeatherManager != null) {
                    User user = new User();
                    user.city = city;
                    user.lon = String.valueOf(longitude);
                    user.lat = String.valueOf(latitude);
                    String[] params = {"city", "province", "district",};
                    String[] params2 = {city, province, district};
                    user.location = JsonUtil.createJSON("location", params, params2);
                    nWeatherManager.setLocalUser(user);

                    //更新当前位置数据
                    nWeatherManager.requestData();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化资源索引
        if (log2file) {
            Log.initFile(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        StatusBarUtils.setWindowStatusBarColor(this, Color.parseColor("#2684e4"));
        contentView = (RelativeLayout) findViewById(R.id.weather_content_view);
        mWeatherView = (SimpleWeatherView) findViewById(R.id.simple_weather);
        findViewById(R.id.title_LBtn).setOnClickListener(this);
        findViewById(R.id.btn_RBtn).setOnClickListener(this);

        //初始化SlideMenu
        mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
        mMenuLeft = new MenuLeft(this);
        mMenuRight = new MenuRight(this);
        mSlideMenu.addView(mMenuLeft, new SlideMenu.LayoutParams((int) (WeatherApplication.getScreenWidth() * 0.8),
                SlideMenu.LayoutParams.MATCH_PARENT, SlideMenu.LayoutParams.ROLE_PRIMARY_MENU));
        mSlideMenu.addView(mMenuRight, new SlideMenu.LayoutParams((int) (WeatherApplication.getScreenWidth() * 0.8),
                SlideMenu.LayoutParams.MATCH_PARENT, SlideMenu.LayoutParams.ROLE_SECONDARY_MENU));
        mSlideMenu.setOnSlideStateChangeListener(this);

        // 设置天气界面按钮监听
        ttsControl = new TextSpeakControl(this);
        // 初始化管理器
        nWeatherManager = WeatherManager.createManager(this);
        nCurrentUser = nWeatherManager.getLocalUser();
        nListPosition = new Point();
        City city;
        if (getIntent() != null && (city = (City) getIntent().getSerializableExtra("city")) != null) {
            updateWeatherByCity(city);
        } else {
            //获取缓存天气数据或从网络获取数据
            initWeather();
        }
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();

        // 网络时间广播监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(nNetReceiver, filter);

        // 有网络执行定位
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.pingyijinren.location.ACTION_RETURN_LOCATION");
        // 注册广播接收器
        registerReceiver(locationReceiver, intentFilter);

        nIsFamilyList = false;
        nWeatherManager.setLoadListener(this);

    }

    private boolean refreshAfterLocated;

    private void initWeather() {
        SimpleWeatherData weatherData = SharedPreUtil.getWeatherData(this);
        if (weatherData != null && !weatherData.getLocation().equals("")) {
            mWeatherView.setWeatherData(weatherData);
            mMenuLeft.setDatas(weatherData);
            long rtTimeStamp = System.currentTimeMillis() / 1000;
            long timeStamp = weatherData.getTimeStamp();
            if (rtTimeStamp - timeStamp > 3 * 60 * 60) {
                nWeatherManager.requestData();
            } else {
                if (ttsOn) {
                    ttsControl.speakAfterTTSReady(weatherData.getSpeakText());
                }
            }
        } else {
            android.util.Log.e(TAG, "initWeather: " + (weatherData == null));
            if (weatherData != null) {
                android.util.Log.e(TAG, "initWeather: " + weatherData.getLocation());
            }
            refreshAfterLocated = true;
            showTipsView();
            //updateWeatherAfterLocated();
        }
    }

    private void initLocation() {
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
    }

    private void showListView() {
        Log.w(TAG, "showListView()");

        //nRootView.setBackgroundColor(Color.WHITE);
//        nScrollView.scrollTo(0, 0);
//        nScrollView.removeAllViews();
//        nScrollView.addView(nListView);

        nCurrentUser = null;
        nIsFamilyList = true;
    }

    private RelativeLayout contentView;

    @Override
    public void onWeatherDataStartLoad(User user) {

    }


    @Override
    public void onWeatherDataLoaded(WeatherWrapper data) {
        // 有weather数据下载
        if (nCurrentUser == null || !data.done) {
            return;
        }
        refreshAfterLocated = false;
        mWeatherView.setWeatherData(data);
        mMenuLeft.setDatas(data);
        SharedPreUtil.saveWeatherData(this, data);
        if (mWeatherView.getParent() == null) {
            contentView.removeAllViews();
            contentView.addView(mWeatherView);
        }
        hideTipsView();
        if (ttsOn) {
            ttsControl.speak("天气刷新成功," + new SimpleWeatherData(data).getSpeakText());
        }


    }

    @Override
    public void onSimpleWeatherDataLoaded(SimpleWeatherData data) {
        if (data == null || mWeatherView == null) return;
        refreshAfterLocated = false;
        mWeatherView.setSimpleWeatherData(data);
        mMenuLeft.setSimpleDatas(data);
        if (mWeatherView.getParent() == null) {
            contentView.removeAllViews();
            contentView.addView(mWeatherView);
        }
        hideTipsView();
        if (ttsOn) {
            ttsControl.speak("天气刷新成功," + data.getSpeakText());
        }
    }


    private void showTipsView() {
        StatusBarUtils.setWindowStatusBarColor(this, Color.parseColor("#2684e4"));

        if (NetworkUtil.getCurrentNetwork(this) == Network.NT_NONE) {
            // 显示没有网络
            ViewUtil.setNoNetworkView(this, contentView);
        } else {
            ViewUtil.setLoadingView(this, contentView);
        }

//        Log.i(TAG, " showTipsView nCurrentUser = " + nCurrentUser);
//        // 数据不完整
//        if (nWeatherManager.isLoading(nCurrentUser)) {
//            // 正在下载，显示loding
//            Log.w(TAG, "isLoading = " + true);
//            if (NetworkUtil.getCurrentNetwork(this) == Network.NT_NONE) {
//                ViewUtil.setNoNetworkView(this, contentView);
//            } else {
//                ViewUtil.setLoadingView(this, contentView);
//            }
//        } else {
//            Log.w(TAG, "isLoading = " + false);
//            if (NetworkUtil.getCurrentNetwork(this) == Network.NT_NONE) {
//                // 显示没有网络
//                ViewUtil.setNoNetworkView(this, contentView);
//            } else {
//                // 显示数据下载失败
//                ViewUtil.setNoConnectView(this, contentView, new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 点击重试动作
//                        nWeatherManager.requestWeatherData(nCurrentUser);
//                    }
//                });
//            }
//        }
    }

    public void setStatusBarColor(int color) {
        StatusBarUtils.setWindowStatusBarColor(this, color);
    }

    private void hideTipsView() {
        contentView.removeAllViews();
        contentView.addView(mWeatherView);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        City c = (City) intent.getSerializableExtra("city");
        if (c != null) {
            updateWeatherByCity(c);
            mSlideMenu.close(false);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ttsControl != null && ttsControl.isSpeak()) {
            ttsControl.stopSpeak();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(nNetReceiver);
            unregisterReceiver(locationReceiver);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        if (nWeatherManager != null) {
            nWeatherManager.destory();
        }

        if (log2file) {
            Log.close();
        }
        if (ttsControl != null) {
            ttsControl.closeTts();
        }
    }

    @Override
    public void onSlideStateChange(int slideState) {
        switch (slideState) {
            case SlideMenu.STATE_CLOSE:
                layoutStatus = Status_Normal;
                break;
            case SlideMenu.STATE_OPEN_LEFT:
                layoutStatus = Status_Left;
                break;
            case SlideMenu.STATE_OPEN_RIGHT:
                layoutStatus = Status_Right;
                break;
        }
    }

    @Override
    public void onSlideOffsetChange(float offsetPercent) {

    }

    private void openSlideMenu(boolean isLeft) {
        if (mSlideMenu == null) return;
        if (layoutStatus == Status_Left && isLeft) return;
        if (layoutStatus == Status_Right && !isLeft) return;
        mSlideMenu.open(isLeft, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            mMenuRight.getFocus();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            openSlideMenu(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && ttsControl != null && ttsControl.isSpeak()) {
            ttsControl.stopSpeak();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean ttsOn = false;

    public void setTtsOn(boolean ttsOn) {
        this.ttsOn = ttsOn;
    }

    /**
     * 是否已经定位
     */
    private boolean isLocated = false;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        int locType = bdLocation.getLocType();
        if (locType != 61 && locType != 66 && locType != 161) {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
            return;
        }
        android.util.Log.e(TAG, "onReceiveLocation: succeed");
        isLocated = true;
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        String currentCity = bdLocation.getAddress().city;
        WeatherApplication.setCurrentCity(currentCity);
        if (currentCity != null && !"".equals(currentCity)) {
            WeatherApplication.setLocatedCity(new City(currentCity, latitude, longitude));
            if (refreshAfterLocated && NetworkUtil.isNetworkAvailable(this)) {
                nWeatherManager.requestData(latitude, longitude);
            }
        }
        SharedPreUtil.setGlobalVar(this, "user_lon", longitude + "");
        SharedPreUtil.setGlobalVar(this, "user_lat", latitude + "");
        SharedPreUtil.setGlobalVar(this, "user_city", bdLocation.getAddress().city);
    }

    private void updateWeatherByCity(City city) {
        if (city.getCity() == null) {
            showTipsView();
            refreshAfterLocated = true;
            //updateWeatherAfterLocated();
        }
        android.util.Log.e(TAG, "updateWeatherByCity: " + city.getCity() + "_" + city.getLatitude() + "_" + city.getLongitude());
        SharedPreUtil.setGlobalVar(this, "user_lon", city.getLongitude() + "");
        SharedPreUtil.setGlobalVar(this, "user_lat", city.getLatitude() + "");
        SharedPreUtil.setGlobalVar(this, "user_city", city.getCity());
        showTipsView();
        //nWeatherManager.requestData(true);
        nWeatherManager.requestData(city.getCity(), city.getLatitude(), city.getLongitude());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_LBtn:
                openSlideMenu(false);
                break;
            case R.id.btn_RBtn:
                openSlideMenu(true);
                break;
        }
    }
}
