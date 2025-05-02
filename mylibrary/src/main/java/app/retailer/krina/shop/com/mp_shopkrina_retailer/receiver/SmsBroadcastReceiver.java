package app.retailer.krina.shop.com.mp_shopkrina_retailer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OtpReceivedInterface;

/**
 * Created on : Dec 22, 2019
 * Author     : Dev
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "SmsBroadcastReceiver";
    private OtpReceivedInterface otpReceiveInterface = null;

    public void setOnOtpListeners(OtpReceivedInterface otpReceiveInterface) {
        this.otpReceiveInterface = otpReceiveInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (mStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents'
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, "onReceive: success " + message);
                    if (otpReceiveInterface != null) {
                        String pattern = "[^0-9]";
                        otpReceiveInterface.onOtpReceived(message.replaceAll(pattern, ""));
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    Log.d(TAG, "onReceive: failure");
                    if (otpReceiveInterface != null) {
                        otpReceiveInterface.onOtpTimeout();
                    }
                    break;
            }
        }
    }
}