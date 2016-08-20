package com.easynetwork.weather.core.menu.menu_right;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.easynetwork.weather.core.WeatherActivity;
import com.easynetwork.weather.core.WeatherManager;
import com.easynetwork.weather.R;

import java.util.ArrayList;
import java.util.List;

public class MenuRight extends LinearLayout implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private List<ListItem> mDatas;

    private Context mContext;

    TextView title;

    private SharedPreferences pref;

    private boolean ttsOn;

    public MenuRight(Context context) {
        this(context, null);
    }

    public MenuRight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.menu_right, this, true);
        mListView = (ListView) findViewById(R.id.ffListView);
        title = (TextView) findViewById(R.id.tv_setting);
        mContext = context;
        pref = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
        ttsOn = pref.getBoolean("ttsOn", false);
        if (mContext instanceof WeatherActivity) {
            ((WeatherActivity) mContext).setTtsOn(ttsOn);
        }
        initDatas();
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new ListItem(R.drawable.icon_locate, "更改城市"));
        mDatas.add(new ListItem(R.drawable.icon_voice, "语音播报"));
        //mDatas.add(new ListItem(R.drawable.icon_locate, "天气测试"));
        MyAdapter mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

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
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.f_item, null);
                viewHolder.icon = (ImageView) view.findViewById(R.id.f_icon);
                viewHolder.text = (TextView) view.findViewById(R.id.f_text);
                viewHolder.button = (Switch) view.findViewById(R.id.f_toggle);
                view.setTag(viewHolder);
                ViewGroup.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 62));
                view.setLayoutParams(lp);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.icon.setImageResource(mDatas.get(i).iconId);
            viewHolder.text.setText(mDatas.get(i).content);
            if (i == 1) {
                viewHolder.button.setVisibility(View.VISIBLE);
                viewHolder.button.setFocusable(false);
                viewHolder.button.setChecked(ttsOn);
                viewHolder.button.setOnCheckedChangeListener(this);
            }
            return view;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            buttonCLickSwitch(b);
        }


        class ViewHolder {
            ImageView icon;
            TextView text;
            Switch button;
        }

    }

    private void buttonCLickSwitch(boolean checked) {
        ttsOn = checked;
        ((WeatherActivity) mContext).setTtsOn(ttsOn);
        pref.edit().putBoolean("ttsOn", ttsOn).commit();
    }

    private void buttonSwitch(Switch button) {
        ttsOn = !ttsOn;
        if (button != null) {
            Log.e(TAG, "setSelected: " + ttsOn);
            button.setChecked(ttsOn);
        }
        ((WeatherActivity) mContext).setTtsOn(ttsOn);
        pref.edit().putBoolean("ttsOn", ttsOn).commit();
    }

    class ListItem {
        public ListItem() {
        }

        public ListItem(int iconId, String content) {
            this.iconId = iconId;
            this.content = content;
        }

        int iconId;
        String content;
    }

    private static final String TAG = "MenuRightMYMY";

    public void getFocus() {
        if (mListView == null) return;
        title.setFocusable(true);
        title.requestFocus();
        mListView.getChildAt(0).requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG, "onItemClick: ");
        if (i == 0) {
            Intent intent = new Intent(mContext, ManageCityActivity.class);
            mContext.startActivity(intent);
        } else if (i == 1) {
            buttonSwitch((Switch) view.findViewById(R.id.f_toggle));
        } else if (i == 2) {
            WeatherManager weatherManager = WeatherManager.getInstance();
            if (weatherManager != null) {
                weatherManager.requestData();
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }

}
