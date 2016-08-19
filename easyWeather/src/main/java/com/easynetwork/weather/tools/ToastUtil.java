package com.easynetwork.weather.tools;

import com.easynetwork.common.widget.CommonToast;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast 显示工具
 * @author funben
 */
public class ToastUtil {
	private static CommonToast commonToast;
	/**
	 * 显示消息
	 * @param context
	 * @param text
	 */
	public static void showText(Context context, String text){
		if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
		if(commonToast==null)
			commonToast = new CommonToast(context,text,Toast.LENGTH_LONG);
		else 
			commonToast.setText(text);
		commonToast.show();
	} 

	public static void showText(Context context, String text,int duration){
		if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
		if(commonToast==null)
			commonToast = new CommonToast(context,text,duration);
		else 
			commonToast.setText(text);
		commonToast.show();
	} 
}
