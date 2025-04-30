package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class TrackOrdersDetails {

    @SerializedName("OrderId")
    public int OrderId;
    @SerializedName("OrderAmount")
    public double OrderAmount;
    @SerializedName("PayableAmount")
    public double payableAmount;
    @SerializedName("IsPaid")
    public boolean IsPaid;
    @SerializedName("OTP")
    public int deliveryOtp;


    public int getOrderId() {
        return OrderId;
    }

    public double getOrderAmount() {
        return OrderAmount;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public boolean isPaid() {
        return IsPaid;
    }
}
