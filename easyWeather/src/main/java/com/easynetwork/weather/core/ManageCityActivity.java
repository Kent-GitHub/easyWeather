package com.easynetwork.weather.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import com.easynetwork.ad.bean.AdPlatform;
//import com.easynetwork.ad.bean.AdListener;
//import com.easynetwork.ad.manager.AdBannerManager;
import com.easynetwork.weather.R;
import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.easynetwork.weather.tools.StatusBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageCityActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mCityList;

    private List<City> mDatas;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);
        findViewById(R.id.add_city).setOnClickListener(this);
        StatusBarUtils.setWindowStatusBarColor(this, Color.parseColor("#e96e13"));
        mCityList = (ListView) findViewById(R.id.manage_list);
//        ViewGroup bannerC = (ViewGroup) findViewById(R.id.banner_container);
//        bannerC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AdBannerManager.getInstance().performAdClick("manageBanner");
//            }
//        });
//        AdBannerManager.getInstance().setScaleFix(true).request(this, bannerC, "manageBanner", this);
        initDatas();
    }

    private static final String TAG = "ManageCityActivity";

    private void initDatas() {
        City city = WeatherApplication.getLocatedCity();
        String nCity = "未知";
        if (city != null) {
            nCity = city.getCity();
        }
        mDatas = new ArrayList<>();
        List<City> cities = SharedPreUtil.getCities(this, "");
        mDatas.addAll(cities);
        Collections.reverse(mDatas);
        mDatas.add(0, new City("当前城市(" + nCity + ")"));
        mAdapter = new MyAdapter();
        mCityList.setAdapter(mAdapter);
        mCityList.setOnItemClickListener(this);
        mCityList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, LocateActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("city", mDatas.get(i));
        if (i == 0) {
            City locatedCity = WeatherApplication.getLocatedCity();
            if (locatedCity != null) {
                intent.putExtra("city", locatedCity);
            } else {
                return;
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

//    @Override
//    public void onAdClick(AdPlatform adPlatform, String s) {
//
//    }
//
//    @Override
//    public void onAdDisplay(AdPlatform adPlatform, String s) {
//
//    }
//
//    @Override
//    public void onAdFailed(AdPlatform adPlatform, String s) {
//
//    }
//
//    @Override
//    public void onAdReady(AdPlatform adPlatform, String s) {
//
//    }
//
//    @Override
//    public void onAdDismiss(AdPlatform adPlatform, String s) {
//
//    }
//
//    @Override
//    public void onLeftApplication(AdPlatform adPlatform, String s) {
//
//    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ManageCityActivity.this);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(ManageCityActivity.this, 62));
            textView.setPadding(10, 0, 10, 0);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
            textView.setText(mDatas.get(i).getCity());
            return textView;
        }
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        //return (int) (dipValue * scale + 0.5f);
        return (int) (dipValue * scale);
    }

}
