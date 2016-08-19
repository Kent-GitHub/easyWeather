package com.easynetwork.weather.tools;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast 显示工具
 *
 * @author funben
 */
public class ToastUtil {

    public static void showText(Context context, String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
