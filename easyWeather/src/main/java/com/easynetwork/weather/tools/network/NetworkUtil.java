package com.easynetwork.weather.tools.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.easynetwork.weather.tools.Log;

/**几个网络相关的工具
 * @author WenYF
 *
 */
public class NetworkUtil {
	private final static String TAG = "NetworkUtil";
	
	public static final String HTTP_DOMAIN = "http://mobile.pingyijinren.com";
	
	/**得到当前网络类型
	 * @param context 上下文
	 * @return 返回当前网络
	 */
	public static Network getCurrentNetwork(Context context){
		ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
		
		if(cm == null){
			Log.w(TAG, "ConnectivityManager is null");
			return Network.NT_NONE; 
		}
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if(networkInfo != null){
			int type = networkInfo.getType();
			int subtype = networkInfo.getSubtype();
			return getNetworkType(type, subtype);
		}else{
			Log.w(TAG, "networkInfo is null");
			return Network.NT_NONE;
		}
	}
	
	
	/**是否连接的是
	 * @param type
	 * @param subType
	 * @return
	 */
	private static Network getNetworkType(int type, int subType){
        if (type == ConnectivityManager.TYPE_WIFI) {
            return Network.NT_WIFI;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
	            case TelephonyManager.NETWORK_TYPE_1xRTT:
	                return Network.NT_2G; // ~ 50-100 kbps
	            case TelephonyManager.NETWORK_TYPE_CDMA:
	                return Network.NT_2G; // ~ 14-64 kbps
	            case TelephonyManager.NETWORK_TYPE_EDGE:
	                return Network.NT_2G; // ~ 50-100 kbps
	            case TelephonyManager.NETWORK_TYPE_GPRS:
	                return Network.NT_2G; // ~ 100 kbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                return Network.NT_3G; // ~25 kbps 
	            case TelephonyManager.NETWORK_TYPE_LTE:
	                return Network.NT_4G; // ~ 400-1000 kbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                return Network.NT_3G; // ~ 600-1400 kbps
	            case TelephonyManager.NETWORK_TYPE_HSDPA:
	                return Network.NT_3G; // ~ 2-14 Mbps
	            case TelephonyManager.NETWORK_TYPE_HSPA:
	                return Network.NT_3G; // ~ 700-1700 kbps
	            case TelephonyManager.NETWORK_TYPE_HSUPA:
	                return Network.NT_3G; // ~ 1-23 Mbps
	            case TelephonyManager.NETWORK_TYPE_UMTS:
	                return Network.NT_3G; // ~ 400-7000 kbps
	            case TelephonyManager.NETWORK_TYPE_EHRPD:
	                return Network.NT_3G; // ~ 1-2 Mbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_B:
	                return Network.NT_3G; // ~ 5 Mbps
	            case TelephonyManager.NETWORK_TYPE_HSPAP:
	                return Network.NT_3G; // ~ 10-20 Mbps
	            case TelephonyManager.NETWORK_TYPE_IDEN:
	                return Network.NT_2G; // ~ 10+ Mbps
	            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
	            default:
	                return Network.NT_NONE;
            }
        } else {
        	return Network.NT_NONE;
        }
    }
	
	/** 判断网络是否激活
	 * @param context 上下文
	 * @return 有网络返回true，没有返回false
	 */
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {  
        	Log.i(TAG, "cm = null");
        	return false;
        } else {
        	NetworkInfo ni = cm.getActiveNetworkInfo();
        	if(ni == null){
        		Log.i(TAG, "ni = null");
        		return false;
        	}else{
        		return ni.isAvailable();
        	}
        }
	}
	
	/** 判断网络是否连接
	 * @param context 上下文
	 * @return 连接返回true，没有返回false
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connManager.getAllNetworkInfo();
		for(NetworkInfo networkInfo:networkInfos){
			if(networkInfo.isConnected()){
				return true;
			}
		}
		return false;
	}
}
