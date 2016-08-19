package com.easynetwork.weather.bean;

import java.util.ArrayList;


/**亲友数据集合
 * @author WenYF
 *
 */
public class FamilyList extends ArrayList<EasyWeatherWrapper>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5235481943358512051L;
	
	
	/**
	 * 如果下载数据的时候，下载解析成功，done为true，这样就能保存到本地文件。<br>
	 * 否则不保存数据到本地文件<br>
	 * 因此，如果或得到的数据为nil，那么表示数据下载或解析失败，侧面表示网络请求异常<br>
	 * 如果是size为空，那么表示没有亲友
	 */
	public boolean done = false;
	
	
	/**
	 * 数据下载的本地时间戳
	 */
	public String nLoadDate = "";
	
}
