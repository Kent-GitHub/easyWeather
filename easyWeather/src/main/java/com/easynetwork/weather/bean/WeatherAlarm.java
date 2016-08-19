package com.easynetwork.weather.bean;

import java.io.Serializable;

/**天气报警
 * @author WenYF
 *
 */
public class WeatherAlarm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5028432302016955351L;
	/**
	 * 标题
	 */
	public String title = "--";
	/**
	 * 类型
	 */
	public String type = "";
	/**
	 * 等级
	 */
	public String level = "";
	/**
	 * 状态
	 */
	public String stat = "";
	/**
	 * 描述
	 */
	public String txt = "----";
	
}
