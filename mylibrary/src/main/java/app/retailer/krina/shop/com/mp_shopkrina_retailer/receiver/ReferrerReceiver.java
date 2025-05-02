package app.retailer.krina.shop.com.mp_shopkrina_retailer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;

public class ReferrerReceiver extends BroadcastReceiver {
    public final String TAG = this.getClass().getSimpleName();
    public final String ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Google Analytics
        new CampaignTrackingReceiver().onReceive(context, intent);
        Bundle extras = intent.getExtras();
        String referrerString = null;

        Log.e(TAG, " " + intent.getExtras());

        try {
            if (extras != null && extras.containsKey("referrer")) {
                referrerString = extras.getString("referrer");
                Log.e(TAG, " " + extras.getString("referrer"));
            }

            if (referrerString != null) {
                SharePrefs.getInstance(context).putString(SharePrefs.REFERRAL_BY, referrerString);
            }
            // Register that the install event now has been sent
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_UPDATE_DATA).putExtra("referrer", referrerString));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error in INSTALL_REFERRER receiver. Install event will not be sent. " + e.getMessage());
        }
    }
}