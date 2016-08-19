package com.easynetwork.weather.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**对一天的天气进行包装
 * @author WenYF
 *
 */
public class WeatherWrapper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8439285689788268818L;

	public boolean done = false;
	/** 天气数据获取时间戳*/
	public String nUpdate = "";
	/**
	 * 本地下载的时间戳
	 */
	public String nLoadDate = "";
	/**
	 * 更新的时间戳
	 */
	public String nTime = "-1";
	/**
	 * "马化腾"用户
	 */
	public User nUser;
	/**
	 * 昨天的温度比较
	 */
	public String nCompareTmp = "--";
	/**
	 * 现在的天气
	 */
	public NowWeatherData nNow;
	/**
	 * 天气警报<br>
	 * 初始化的时候不实例一个，因为下载的时候也不一定有
	 */
	public WeatherAlarm nAlarm;
	public List<WeatherAlarm> nAlarmList;
	/**
	 * 当天的空气质量
	 */
	public AirQualityData nAirQuality;
	/**
	 * 今天的小时天气
	 */
	public HashMap<Integer, HourlyWeatherData> nHourlyWeathers;
	/**
	 * 今天得到的以后的天气
	 */
	public HashMap<Integer, DailyWeatherData> nDailyWeathers;
	
	public DailyWeatherData getToday(){
		return nDailyWeathers.get(0);
	}
	
	public void setToday(DailyWeatherData data){
		nDailyWeathers.put(0, data);
	}
	
	public DailyWeatherData getTomorrow(){
		return nDailyWeathers.get(1);
	}
	
	public DailyWeatherData getAfterTomorrow(){
		return nDailyWeathers.get(2);
	}


	public NowWeatherData getNowWeatherData() {
		return nNow;
	}

	public void setTomorrow(DailyWeatherData data){
		nDailyWeathers.put(1, data);
	}
	
	public void setAfterTomorrow(DailyWeatherData data){
		nDailyWeathers.put(2, data);
	}
	
	public WeatherWrapper() {
		nUser = new User();
		nNow = new NowWeatherData();
		nAirQuality = new AirQualityData();
		nHourlyWeathers = new HashMap<Integer, HourlyWeatherData>();
		nDailyWeathers = new HashMap<Integer, DailyWeatherData>();
		for(int i = 0; i < 3; i++){
			DailyWeatherData data = new DailyWeatherData();
			nDailyWeathers.put(i, data);
		}
	}

	@Override
	public String toString() {
		return "WeatherWrapper{" +
				"done=" + done +
				", nUpdate='" + nUpdate + '\'' +
				", nLoadDate='" + nLoadDate + '\'' +
				", nTime='" + nTime + '\'' +
				", nUser=" + nUser +
				", nCompareTmp='" + nCompareTmp + '\'' +
				", nNow=" + nNow +
				", nAlarm=" + nAlarm +
				", nAlarmList=" + nAlarmList +
				", nAirQuality=" + nAirQuality +
				", nHourlyWeathers=" + nHourlyWeathers +
				", nDailyWeathers=" + nDailyWeathers +
				'}';
	}
}
