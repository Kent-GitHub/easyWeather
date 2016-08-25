package com.easynetwork.weather.core;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easynetwork.weather.tools.Log;
import com.easynetwork.weather.tools.network.NetworkUtil;
import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.tools.StatusBarUtils;
import com.easynetwork.weather.tools.TextSpeakControl;
import com.easynetwork.weather.tools.ToastUtil;
import com.easynetwork.weather.tools.ViewUtil;
import com.easynetwork.weather.tools.network.Network;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.core.menu.MenuLeft;
import com.easynetwork.weather.core.menu.MenuRight;
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
import android.media.AudioManager;
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
 * 如果有任何不明白，请发邮件到 {bludawn@foxmail.com}<br>
 *
 * @author WenYF
 */
public class WeatherActivity extends Activity implements WeatherManager.DataLoadListener,
        SlideMenu.OnSlideStateChangeListener, BDLocationListener, View.OnClickListener {

    private final static String TAG = "WeatherActivity";
    private final static boolean log2file = true;
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
    /**
     * 语音播报实例
     */
    private TextSpeakControl ttsControl;

    private boolean isSilence;

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
        //初始化&监听情景模式
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audio.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            isSilence = true;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mRingerModeReceiver, intentFilter);

        ttsControl = new TextSpeakControl(this);
        // 初始化管理器
        nWeatherManager = WeatherManager.createManager(this);
        nWeatherManager.setLoadListener(this);

        //加载本地数据或请求网络数据
        initWeather();

        //初始化定位
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();

    }

    /**
     * 定位成功后按定位地点进行刷新
     */
    private boolean refreshAfterLocated;
    /**
     * 定位成功后的City
     */
    private City locatedCity;

    private void initWeather() {
        String city;
        long timeStamp;
        double latitude;
        double longitude;
        String json;
        City currentCity;
        try {
            city = SharedPreUtil.getSimpleData(WeatherApplication.context, Constants.SD_CITY);
            json = SharedPreUtil.getSimpleData(WeatherApplication.context, Constants.SD_JSON);
            if (city == null || city.equals("") || json == null || json.equals("")) {
                requestAfterLocated();
                return;
            }
            timeStamp = Long.valueOf(SharedPreUtil.getSimpleData(WeatherApplication.context, Constants.SD_STAMP));
            latitude = Double.valueOf(SharedPreUtil.getSimpleData(WeatherApplication.context, Constants.SD_LATITUDE));
            longitude = Double.valueOf(SharedPreUtil.getSimpleData(WeatherApplication.context, Constants.SD_LONGITUDE));
        } catch (Exception e) {
            e.printStackTrace();
            requestAfterLocated();
            return;
        }


        long nowStamp = System.currentTimeMillis() / 1000;

        currentCity = new City(city, latitude, longitude);
        if (nowStamp - timeStamp > Constants.refreshInterval) {
            showTipsView();
            nWeatherManager.requestData(currentCity);
            android.util.Log.e(TAG, "Loaded weather data is out of time,request weather data again.");
            return;
        }
        SimpleWeatherData data = JsonUtil.jsonToSWD(json);
        mWeatherView.setSimpleWeatherData(data);
        mMenuLeft.setSimpleDatas(data);
        if (ttsOn && !isSilence) {
            ttsControl.speakAfterTTSReady(data.getSpeakText());
        }
        android.util.Log.e(TAG, "Load weather data from local succeeded.");
    }

    /**
     * 已定位则直接请求数据，未定位设置标志位并等待定位成功后刷新
     */
    private void requestAfterLocated() {
        showTipsView();
        if (!isLocated) {
            refreshAfterLocated = true;
            android.util.Log.e(TAG, "should request weather after located.");
            return;
        }
        nWeatherManager.requestData(locatedCity);
        android.util.Log.e(TAG, "request weather ,it's located already.");
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

    private RelativeLayout contentView;

    @Override
    public void onWeatherDataStartLoad() {

    }

    @Override
    public void onWeatherDataLoaded(SimpleWeatherData data) {
        if (data == null || mWeatherView == null) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            mWeatherView.reset();
            hideTipsView();
            return;
        }
        refreshAfterLocated = false;
        mWeatherView.setSimpleWeatherData(data);
        mMenuLeft.setSimpleDatas(data);
        if (mWeatherView.getParent() == null) {
            contentView.removeAllViews();
            contentView.addView(mWeatherView);
        }
        hideTipsView();
        if (ttsOn && !isSilence) {
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


        if (nWeatherManager != null) {
            nWeatherManager.destroy();
        }

        if (log2file) {
            Log.close();
        }
        if (ttsControl != null) {
            ttsControl.closeTts();
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mRingerModeReceiver != null) {
            unregisterReceiver(mRingerModeReceiver);
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
        //定位失败处理
        if (locType != 61 && locType != 66 && locType != 161) {
            android.util.Log.e(TAG, "onReceiveLocation: local failed with code:" + locType);
            ToastUtil.showText(this, "定位失败");
            if (refreshAfterLocated && NetworkUtil.isNetworkAvailable(this)) {
                hideTipsView();
            }
            return;
        }
        //定位成功处理
        isLocated = true;
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        String currentCity = bdLocation.getAddress().city;
        android.util.Log.e(TAG, "onReceiveLocation succeed located to :" + currentCity);
        if (currentCity != null && !"".equals(currentCity)) {
            if (currentCity.endsWith("市") && !currentCity.equals("沙市") && !currentCity.equals("津市")) {
                currentCity = currentCity.substring(0, currentCity.length() - 1);
            }
            locatedCity = new City(currentCity, latitude, longitude);
            WeatherApplication.setLocatedCity(locatedCity);
            if (refreshAfterLocated && NetworkUtil.isNetworkAvailable(this)) {
                nWeatherManager.requestData(locatedCity);
                android.util.Log.e(TAG, "located already,request weather date now.");
            }
        }
    }

    private void updateWeatherByCity(City city) {
        if (city.getCity() == null) {
            requestAfterLocated();
        }
        showTipsView();
        nWeatherManager.requestData(city);
        android.util.Log.e(TAG, "request weather data form an intent click.");
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

    private BroadcastReceiver mRingerModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                final int ringerMode = am.getRingerMode();
                switch (ringerMode) {
                    case AudioManager.RINGER_MODE_NORMAL:
                        isSilence = false;
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        isSilence = true;
                        break;
                    case AudioManager.RINGER_MODE_SILENT:
                        isSilence = true;
                        break;
                }
            }
        }
    };
}
