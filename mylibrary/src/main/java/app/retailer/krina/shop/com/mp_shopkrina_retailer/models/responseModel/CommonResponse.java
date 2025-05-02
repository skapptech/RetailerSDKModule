package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {
    @SerializedName("Status")
    public boolean status;
    @SerializedName("Message")
    private String message;
}
