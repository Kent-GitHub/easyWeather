package com.easynetwork.weather.core;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.easynetwork.weather.R;
import com.easynetwork.weather.application.WeatherApplication;
import com.easynetwork.weather.bean.City;
import com.easynetwork.weather.tools.SharedPreUtil;
import com.easynetwork.weather.tools.StatusBarUtils;
import com.easynetwork.weather.tools.db.CityDbHelper;

import java.util.ArrayList;
import java.util.List;

public class LocateActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher {


    private EditText mSearchEt;
    private ListView mListView;
    private List<City> mDatas;
    private MyAdapter mAdapter;
    private CityDbHelper mCityDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        StatusBarUtils.setWindowStatusBarColor(this, Color.parseColor("#e96e13"));
        mSearchEt = (EditText) findViewById(R.id.locate_input);
        mListView = (ListView) findViewById(R.id.locate_listView);
        mSearchEt.addTextChangedListener(this);
        mCityDbHelper = new CityDbHelper(this);
        initDatas();
    }


    private void initDatas() {
        mDatas = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        clear();
    }


    private static final String TAG = "LocateActivityMYMY";

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("city", mDatas.get(i));
        if (i != 0) {
            SharedPreUtil.addCity(this, mDatas.get(i));
        } else {
            City locatedCity = WeatherApplication.getLocatedCity();
            if (locatedCity != null) {
                SharedPreUtil.addCity(this, locatedCity);
                intent.putExtra("city", locatedCity);
            } else {
                intent.putExtra("city", new City());
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String result = editable.toString();
        if (result.equals("")) {
            clear();
            return;
        }
        queryCity(editable.toString());
    }

    private void clear() {
        mDatas.clear();
        mDatas.add(new City("自动定位"));
        mDatas.add(new City("北京", 39.92, 116.46));
        mDatas.add(new City("上海", 31.22, 121.48));
        mDatas.add(new City("广州", 23.16, 113.23));
        mDatas.add(new City("深圳", 22.62, 114.07));
        mDatas.add(new City("杭州", 30.26, 120.19));
        mAdapter.notifyDataSetChanged();
    }

    private void queryCity(String s) {
        Cursor cursor = mCityDbHelper.getChooseCity(s);
        if (cursor == null) {
            clear();
            return;
        }
        mDatas.clear();
        mDatas.add(new City("自动定位"));
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String latitude = cursor.getString(cursor.getColumnIndex("longitude"));
            String longitude = cursor.getString(cursor.getColumnIndex("latitude"));
            try {
                mDatas.add(new City(city, Double.valueOf(latitude), Double.valueOf(longitude)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            City city = mDatas.get(i);
            TextView tv = new TextView(LocateActivity.this);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WeatherApplication.dip2px(LocateActivity.this, 62));
            tv.setLayoutParams(lp);
            tv.setPadding(10, 0, 10, 0);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setText(city.getCity());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
            if (i == 0) {
                Drawable drawable = getResources().getDrawable(R.drawable.icon_locate);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);
            }
            return tv;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCityDbHelper != null) {
            mCityDbHelper.close();
            mCityDbHelper = null;
        }
    }
}
