package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class PrimePaymentResponse {
    @SerializedName("TransId")
    private int TransId;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;

    public int getTransId() {
        return TransId;
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}