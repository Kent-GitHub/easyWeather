package com.easynetwork.weather.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easynetwork.weather.core.WeatherActivity;
import com.easynetwork.weather.core.CodeToValues;
import com.easynetwork.weather.R;
import com.easynetwork.weather.bean.SimpleWeatherData;

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
        location = (TextView) findViewById(R.id.title_location);
        RTTmp = (TextView) findViewById(R.id.rt_tmp);
        TextPaint paint = RTTmp.getPaint();
        paint.setFakeBoldText(true);
        RTTvDescribe = (TextView) findViewById(R.id.tv_weather_describe);
        tmpRange = (TextView) findViewById(R.id.tv_tmp_range);
        ivDescribe = (ImageView) findViewById(R.id.rt_describe);
        icon = (ImageView) findViewById(R.id.iv_weather_icon);
    }

    private static final String TAG = "SimpleWeatherViewMYMY";

    public void setWeatherData(SimpleWeatherData data) {
        if (this.date != null) {
            this.date.setText(data.getDate());
        }
        if (this.location != null) {
            this.location.setText(data.getLocation());
        }
        if (RTTvDescribe != null) {
            RTTvDescribe.setText(data.getRtDescribe());
        }
        if (RTTmp != null) {
            RTTmp.setText(data.getRtTmp());
        }
        if (tmpRange != null) {
            tmpRange.setText(data.getTmpRange());
        }
        try {
            int code = Integer.parseInt(data.getRtWeatherCode());
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

    public void reset() {
        if (this.location != null) {
            this.location.setText("未知");
        }
        if (RTTvDescribe != null) {
            RTTvDescribe.setText("未知");
        }
        if (RTTmp != null) {
            RTTmp.setText("未知");
        }
        if (tmpRange != null) {
            tmpRange.setText("未知");
        }
        if (ivDescribe != null) {
            ivDescribe.setImageResource(R.drawable.dcr_weather_na);
        }
        if (icon != null) {
            icon.setImageResource(R.drawable.icon_na);
        }
        setBackgroundColor(Color.parseColor("#2684e4"));
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

    public void setSimpleWeatherData(SimpleWeatherData data) {
        if (this.date != null) {
            this.date.setText(data.getDate());
        }
        if (this.location != null) {
            this.location.setText(data.getLocation());
        }
        if (RTTvDescribe != null) {
            RTTvDescribe.setText(data.getRtDescribe());
        }
        if (RTTmp != null) {
            RTTmp.setText(data.getRtTmp());
        }
        if (tmpRange != null) {
            tmpRange.setText(data.getTmpRange());
        }
        try {
            int code = Integer.parseInt(data.getRtWeatherCode());
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
}
