package com.easynetwork.weather.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easynetwork.weather.bean.WeatherWrapper;
import com.easynetwork.weather.core.WeatherActivity;
import com.easynetwork.weather.core.menu.menu_right.CodeToValues;
import com.easynetwork.weather.tools.StatusBarUtils;
import com.easynetwork.weather.tools.ToastUtil;
import com.easynetwork.weather.R;
import com.easynetwork.weather.bean.DailyWeatherData;
import com.easynetwork.weather.bean.NowWeatherData;
import com.easynetwork.weather.bean.SimpleWeatherData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleWeatherView extends LinearLayout {
    /**
     * 时间
     */
    private TextView date;
    /**
     * 城市
     */
    private TextView location;
    /**
     * 实时温度
     */
    private TextView RTTmp;
    /**
     * 实时文字描述
     */
    private TextView RTTvDescribe;
    /**
     * 温度范围
     */
    private TextView tmpRange;
    /**
     * 文字图片描述
     */
    private ImageView ivDescribe;
    /**
     * 天气图标
     */
    private ImageView icon;

    private int weatherCode;

    private Context mContext;

    public SimpleWeatherView(Context context) {
        this(context, null);
    }

    public SimpleWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.weather_view, this, true);
        mContext = context;
        init();
    }

    private void init() {
        date = (TextView) findViewById(R.id.title_date);
        date.setTextSize(24);
        location = (TextView) findViewById(R.id.title_location);
        location.setTextSize(24);
        RTTmp = (TextView) findViewById(R.id.rt_tmp);
        TextPaint paint = RTTmp.getPaint();
        paint.setFakeBoldText(true);
        RTTvDescribe = (TextView) findViewById(R.id.tv_weather_describe);
        RTTvDescribe.setTextSize(24);
        tmpRange = (TextView) findViewById(R.id.tv_tmp_range);
        tmpRange.setTextSize(24);
        ivDescribe = (ImageView) findViewById(R.id.rt_describe);
        icon = (ImageView) findViewById(R.id.iv_weather_icon);
    }

    private static final String TAG = "SimpleWeatherViewMYMY";

    public void setWeatherData(WeatherWrapper weatherData) {
        if (!weatherData.done) {
            ToastUtil.showText(mContext, "天气数据加载失败。。。");
            return;
        }
        String location = weatherData.nUser.city;
        DailyWeatherData today = weatherData.getToday();
        NowWeatherData now = weatherData.getNowWeatherData();
        Log.e(TAG, "setWeatherData: " + today.toString());
        Log.e(TAG, "setWeatherData: " + location + now.toString());
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        StringBuilder dateString = new StringBuilder();
        dateString.append(new SimpleDateFormat("MM.dd").format(date) + "/");
        dateString.append(formatWeekday(c.get(Calendar.DAY_OF_WEEK)));
        if (this.date != null) {
            this.date.setText(dateString.toString());
        }
        if (this.location != null) {
            this.location.setText(location);
        }
        if (RTTvDescribe != null) {
            RTTvDescribe.setText(now.txt);
        }
        if (RTTmp != null) {
            RTTmp.setText(now.tmp);
        }
        if (tmpRange != null) {
            tmpRange.setText(today.minTmp + "~" + today.maxTmp + "°C");
        }
        try {
            if (ivDescribe != null) {
                ivDescribe.setImageResource(CodeToValues.getImageDescribe(Integer.parseInt(now.code)));
            }
            if (icon != null) {
                icon.setImageResource(CodeToValues.getIconDescribe(Integer.parseInt(now.code)));
            }
            setBackgroundColor(CodeToValues.getColorByCode(Integer.parseInt(now.code)));
            if (mContext instanceof WeatherActivity) {
                ((WeatherActivity) mContext).setStatusBarColor(CodeToValues.getColorByCode(Integer.parseInt(now.code)));
            }
        } catch (Exception e) {
            Log.e(TAG, "setWeatherData: weatherCodeError");
        }

    }

    public void setWeatherData(SimpleWeatherData data) {
        if (this.date != null) {
            this.date.setText(data.getDate());
        }
        if (this.location != null) {
            this.location.setText(data.getLocation());
        }
        if (RTTvDescribe != null) {
            RTTvDescribe.setText(data.getDescribe());
        }
        if (RTTmp != null) {
            RTTmp.setText(data.getRtTmp());
        }
        if (tmpRange != null) {
            tmpRange.setText(data.getTmpRange());
        }
        try {
            int code = Integer.parseInt(data.getWeatherCode());
            if (ivDescribe != null) {
                ivDescribe.setImageResource(CodeToValues.getImageDescribe(code));
            }
            if (icon != null) {
                icon.setImageResource(CodeToValues.getIconDescribe(code));
            }
            setBackgroundColor(CodeToValues.getColorByCode(code));
            if (mContext instanceof WeatherActivity) {
                ((WeatherActivity) mContext).setStatusBarColor(CodeToValues.getColorByCode(code));
            }
        } catch (Exception e) {
            Log.e(TAG, "setWeatherData: weatherCodeError");
        }
    }

    private String formatWeekday(int day) {
        switch (day) {
            case 1:
                return "周天";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "周八";
        }
    }
}
