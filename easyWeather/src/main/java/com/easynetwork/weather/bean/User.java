package com.easynetwork.weather.bean;

import java.io.Serializable;

/**用户结构
 * @author WenYF
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4540331788179705016L;
	public String lat;
	public String lon;
	public String name;
	public String city = "未知";
	public String token;
	public String location;
	
	// 用来保存文件时候的唯一性
	public String uid;
	public User() {
	}
	@Override
	public String toString() {
		return "User [lat=" + lat + ", lon=" + lon + ", name=" + name
				+ ", city=" + city + ", uid=" + uid + ", token=" + token + ", location=" + location + "]";
	}
}
