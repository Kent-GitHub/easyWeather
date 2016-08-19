package com.easynetwork.weather.core.menu.menu_left;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easynetwork.weather.bean.DailyWeatherData;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.bean.WeatherWrapper;

import java.util.ArrayList;
import java.util.List;

public class MenuLeft extends LinearLayout {

    private Context mContext;
    private ListView mListView;
    private List<DailyWeatherData> mDatas;
    private MyAdapter mAdapter;

    public MenuLeft(Context context) {
        this(context, null);
    }

    public MenuLeft(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuLeft(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(com.easynetwork.weather.R.layout.menu_left, this, true);
        mListView = (ListView) view.findViewById(com.easynetwork.weather.R.id.stWeather_list);
        initDatas();
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
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
            DailyWeatherData data = mDatas.get(i);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(com.easynetwork.weather.R.layout.st_weather_item, null);
                viewHolder.day = (TextView) convertView.findViewById(com.easynetwork.weather.R.id.item_day);
                viewHolder.describe = (TextView) convertView.findViewById(com.easynetwork.weather.R.id.item_describe);
                viewHolder.tmp = (TextView) convertView.findViewById(com.easynetwork.weather.R.id.item_tmp);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.day.setText(data.date);
            viewHolder.describe.setText(data.describe);
            viewHolder.tmp.setText(data.minTmp + "~" + data.maxTmp + "°");
            return convertView;
        }


        private static final String TAG = "MyAdapterMYMY";

        class ViewHolder {
            TextView day, describe, tmp;
        }
    }

    public void setDatas(WeatherWrapper data) {
        mDatas.clear();
        data.getToday().date = "今天";
        data.getTomorrow().date = "明天";
        data.getAfterTomorrow().date = "后天";
        mDatas.add(data.getToday());
        mDatas.add(data.getTomorrow());
        mDatas.add(data.getAfterTomorrow());
        mAdapter.notifyDataSetChanged();
    }

    public void setDatas(SimpleWeatherData data) {
        if (mDatas == null) return;
        mDatas.clear();
        String[] predictions = data.getPrediction();
        DailyWeatherData today = new DailyWeatherData(predictions[0]);
        DailyWeatherData tomorrow = new DailyWeatherData(predictions[1]);
        DailyWeatherData afterTomorrow = new DailyWeatherData(predictions[2]);
        mDatas.add(today);
        mDatas.add(tomorrow);
        mDatas.add(afterTomorrow);
        mAdapter.notifyDataSetChanged();
    }
}
