package com.easynetwork.weather.bean;

import java.io.Serializable;

/**实时天气数据
 * @author WenYF
 *
 */
public class NowWeatherData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9187004183310534883L;
	/**
	 * 实时天气的分数， 状况代码
	 */
	public String code = "-1";	
	/**
	 * 实时天气的描述"晴"、"小雨"等 
	 */
	public String txt = "--";	
	/**
	 * 实时气温
	 */
	public String tmp = "--";

	@Override
	public String toString() {
		return "NowWeatherData{" +
				"code='" + code + '\'' +
				", txt='" + txt + '\'' +
				", tmp='" + tmp + '\'' +
				'}';
	}
}
