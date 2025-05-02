package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

/**
 * Created on : Dec 22, 2019
 * Author     : Dev
 */
public interface OtpReceivedInterface {
    void onOtpReceived(String otp);

    void onOtpTimeout();
}