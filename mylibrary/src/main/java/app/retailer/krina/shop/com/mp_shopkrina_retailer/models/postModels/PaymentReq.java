package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class PaymentReq {

    @SerializedName("EncString")
    private String EncString;

    public PaymentReq(String encString) {
        EncString = encString;
    }
}