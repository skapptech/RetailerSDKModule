package com.retailer.retailermodule;

import android.app.Application;

import com.sk.mylibrary.utils.DirectSDK;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DirectSDK.initialize(this);
    }
}
