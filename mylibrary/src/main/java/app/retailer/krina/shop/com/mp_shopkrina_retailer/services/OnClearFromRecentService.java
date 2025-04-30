package app.retailer.krina.shop.com.mp_shopkrina_retailer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;

public class OnClearFromRecentService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
//        SharePrefs.getInstance(this).putBoolean(SharePrefs.REDIRECT_TO_NOTIFICATION,false);
        stopSelf();
    }

}
