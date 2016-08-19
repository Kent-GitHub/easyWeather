package com.easynetwork.weather.bean;

import java.io.Serializable;

/**亲友的天气数据结构
 * @author WenYF
 *
 */
public class EasyWeatherWrapper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3966920950396198803L;
	public boolean done = false;
	public User nUser;
	public NowWeatherData nWeather;
}	
