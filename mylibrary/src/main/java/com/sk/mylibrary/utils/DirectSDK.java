package com.sk.mylibrary.utils;

import android.app.Activity;
import android.content.Context;


public class DirectSDK {
    private static DirectSDK mInstance;
    public static Context context;
    public String otp = "1234";
    public Activity activity;


    public static synchronized DirectSDK getInstance() {
        if (mInstance == null) {
            mInstance = new DirectSDK();
        }
        return mInstance;
    }

    public static void initialize(Context context1) {
        context = context1;
        mInstance = new DirectSDK();
    }

}