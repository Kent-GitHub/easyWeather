package com.easynetwork.weather.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Kent ↗↗↗ on 2016/9/5.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: BOOT_COMPLETED" );
        Intent i = new Intent(context, WeatherService.class);
        context.startService(i);
    }
}
