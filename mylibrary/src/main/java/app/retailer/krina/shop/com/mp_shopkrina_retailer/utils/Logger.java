package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.util.Log;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;

public class Logger {
    public static void log(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void logD(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String message) {
        Log.i("Texture", message);
    }
}