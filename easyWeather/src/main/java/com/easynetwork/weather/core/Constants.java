package com.easynetwork.weather.core;

/**
 * 常量
 *
 * @author WenYF
 */
public class Constants {
    // TODO: 2016/8/16 更换查询接口
    public static final String WEATHER_DATA_URL = "http://mobile.pingyijinren.com/weather/weather/index?v=1.0";
    public static final String WEATHER_DATA_URL1 = "http://192.168.10.222/weather/weather/index?v=1.0";
    public static final String FAMILY_LIST_URL = "http://mobile.pingyijinren.com/weather/childweather?";
    public static final String LIST_DATA_FILENAME = "list_filename";
    public static final String DATA_FILENAME_SUFFIX = ".bat";
    public static final String WEATHER_REMIND_FOLDER = "remind_image";
    public static final String WEATHER_PHOTO_URL = "http://mobile.pingyijinren.com/family/photo/addWeather?v=1.0";
    public static final String WEATHER_REMIND_STATE_URL = "http://mobile.pingyijinren.com/family/photo/getWeatherRcd?v=1.0";
    /**
     * SD缓存文件夹名称
     */
    public final static String SD_FOLDER_PATH = "/pingyijinren/EasyWeather/";

    public static final String SD_CITY = "sdCity";
    public static final String SD_JSON = "sdJson";
    public static final String SD_STAMP = "sdStamp";
    public static final String SD_LATITUDE = "sdLatitude";
    public static final String SD_LONGITUDE = "sdLongitude";

    public static final long refreshInterval = 3 * 60 * 60;
}
