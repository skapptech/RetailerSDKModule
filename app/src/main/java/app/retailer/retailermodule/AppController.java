package app.retailer.retailermodule;

import android.app.Application;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // DirectSDK.initialize(this);
    }
}
