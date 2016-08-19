package com.easynetwork.weather.tools;

import com.easynetwork.common.util.screen.ScreenUtil;

import android.app.Activity;
import android.content.Context;

/**
 *屏幕比例工具
 */
public class RatioUtil {
	/**得到屏幕的宽的比例  以(720*1280为基准)
	 * @param activity 
	 * @return
	 */
	public static float getScreenRatioWidth(Context context) {
		int screenWidth  =ScreenUtil.getScreenWidth((Activity) context);
		float wRatio = screenWidth/720.f;
		return wRatio;
	}
	
	/**得到屏幕的宽的比例  以(720*1280为基准)
	 * @param activity 
	 * @return
	 */
	public static float getScreenRatioHeight(Context context) {
		int screenHeight  =ScreenUtil.getScreenHeight((Activity) context);
		float hRatio = screenHeight/1280.f;
		return hRatio;
	}
	
	/**得到屏幕的单位dp像素的比例  以(720*1280为基准 320)
	 * @param activity 
	 * @return
	 */
	public static float getUnitDpRatio(Context context) {
		int dpi  = context.getResources().getDisplayMetrics().densityDpi;
		float dpiRatio = dpi/320.f;
		return dpiRatio;
	}
}
