package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("Password")
    private String Password;
    @SerializedName("fcmId")
    private String fcmId;
    @SerializedName("Skcode")
    private String Skcode;
    @SerializedName("CurrentAPKversion")
    private String CurrentAPKversion;
    @SerializedName("PhoneOSversion")
    private String PhoneOSversion;
    @SerializedName("UserDeviceName")
    private String UserDeviceName;
    @SerializedName("IMEI")
    private String IMEI;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("TrueCustomer")
    private boolean TrueCustomer;

    public LoginModel(String mobile, String password, String fcmId, String currentAPKversion, String phoneOSversion, String userDeviceName, String IMEI, String deviceId, boolean autoNumberhendle)
    {
        this.Mobile = mobile;
        this. Password = password;
        this.fcmId = fcmId;
        this.CurrentAPKversion = currentAPKversion;
        this.PhoneOSversion = phoneOSversion;
        this.UserDeviceName = userDeviceName;
        this.IMEI = IMEI;
        this.deviceId = deviceId;
        this.TrueCustomer = autoNumberhendle;
    }

}
