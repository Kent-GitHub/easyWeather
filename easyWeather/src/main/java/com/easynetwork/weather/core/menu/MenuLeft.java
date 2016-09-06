package com.easynetwork.weather.core.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.easynetwork.ad.bean.AdPlatform;
//import com.easynetwork.ad.bean.AdListener;
//import com.easynetwork.ad.manager.AdBannerManager;
import com.easynetwork.weather.bean.DailyWeatherData;
import com.easynetwork.weather.bean.SimpleWeatherData;
import com.easynetwork.weather.tools.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class MenuLeft extends LinearLayout {

    private Context mContext;
    private ListView mListView;
    private List<DailyWeatherData> mDatas;
    private MyAdapter mAdapter;

    private RelativeLayout bannerContainer;

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
        bannerContainer = new RelativeLayout(context);
        bannerContainer.setGravity(Gravity.CENTER);
//        AdBannerManager.getInstance().setScaleFix(true).request(context, bannerContainer, "bannerLeft", this);
//        bannerContainer.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AdBannerManager.getInstance().performAdClick("bannerLeft");
//            }
//        });
        setFocusable(true);
        initDatas();
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private boolean adReady;

//    /**
//     * 广告被点击
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onAdClick(AdPlatform channel, String describe) {
//        Log.e("MenuLeft", "onAdClick bannerLeftOnClick");
//    }
//
//    /**
//     * 广告显示
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onAdDisplay(AdPlatform channel, String describe) {
//        adReady = true;
//        mAdapter.notifyDataSetChanged();
//        Log.e(TAG, "onAdDisplay: ");
//    }
//
//    private static final String TAG = "MenuLeft";
//
//    /**
//     * 广告加载失败
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onAdFailed(AdPlatform channel, String describe) {
//
//    }
//
//    /**
//     * 广告已成功DownLoad
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onAdReady(AdPlatform channel, String describe) {
//
//    }
//
//    /**
//     * 广告消失
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onAdDismiss(AdPlatform channel, String describe) {
//
//    }
//
//    /**
//     * 广告被点击后离开应用
//     *
//     * @param channel  平台
//     * @param describe 类型
//     */
//    @Override
//    public void onLeftApplication(AdPlatform channel, String describe) {
//
//    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size()
//                    + (adReady ? 1 : 0)
                    ;
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
//            if (i == mDatas.size()) {
//                int height = ScreenUtil.dip2px(mContext, 62);
//                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//                bannerContainer.setLayoutParams(lp);
//                return bannerContainer;
//            }
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

    public void setSimpleDatas(SimpleWeatherData data) {
        mDatas.clear();
        data.getDays().get(0).date = "今天";
        data.getDays().get(1).date = "明天";
        data.getDays().get(2).date = "后天";
        mDatas.add(data.getDays().get(0));
        mDatas.add(data.getDays().get(1));
        mDatas.add(data.getDays().get(2));
        mAdapter.notifyDataSetChanged();
    }

}
