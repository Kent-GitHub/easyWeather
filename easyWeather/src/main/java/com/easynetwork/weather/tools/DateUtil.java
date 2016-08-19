package com.easynetwork.weather.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**时间转换工具
 * @author Funben
 */
public class DateUtil {

	public static final String FORMAT = "yyyy-MM-dd";
	public static final String FORMAT_HMS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 获取中文格式时间 
	 * @param dateString
	 * @return 格式 2012年12月12日 上午3:30
	 */
	public static String parserTime2ChinaDateYMDP(String dateString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat(FORMAT_HMS);
		try
		{
			Date datetime = ft.parse(dateString);
			dtstr += (datetime.getYear() + 1900) + "年";
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日 ";
			int hour = datetime.getHours();
			String timeString = "";
			if (hour < 12)
			{// 上午
				timeString = "上午" + hour;
			} else if (hour == 12)
			{ // 中午
				timeString = "中午" + hour;
			} else
			{// 下午
				timeString = "下午" + (hour - 12);
			}
			int minute = datetime.getMinutes();
			String minuteStr = "";
			if (minute < 10)
			{
				minuteStr = "0" + minute;
			} else
			{
				minuteStr = minute + "";
			}
			dtstr += timeString + ":" + minuteStr;
		} catch (Exception e){
			Log.e("", e.toString());
		}
		return dtstr;
	}
	
	/**
	 * 获取中文格式时间 
	 * @param dateString
	 * @return 格式 12月12日
	 */
	public static String parserTime2ChinaDateMD(String dateString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat(FORMAT);
		try
		{
			Date datetime = ft.parse(dateString);
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日 ";
		} catch (Exception e){
			Log.e("", e.toString());
		}
		return dtstr;
	}
}
