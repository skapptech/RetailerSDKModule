package app.retailer.retailermodule;

import android.app.Application;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DirectSDK;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DirectSDK.initialize(this);
    }
}
