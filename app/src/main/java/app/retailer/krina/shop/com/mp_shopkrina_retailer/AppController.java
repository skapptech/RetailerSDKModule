package app.retailer.krina.shop.com.mp_shopkrina_retailer;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        RetailerSDKApp.initialize(this);
    }
}
