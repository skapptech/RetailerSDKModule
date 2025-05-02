package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class ReferralConfigModel {
    public int OnOrder;
    public String OrderCount;
    public String orderStatus;
    @SerializedName("OnDeliverd")
    public int onDelivered;
    @SerializedName("ReferralWalletPoint")
    public double referralWalletPoint;
    @SerializedName("CustomerWalletPoint")
    public double customerWalletPoint;
}