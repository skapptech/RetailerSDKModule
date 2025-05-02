package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class DialEarningResponse {
    @SerializedName("Status")
    public boolean Status;
    @SerializedName("Message")
    public String Message;
    @SerializedName("WheelDetails")
    public WheelDetails wheelDetails;


    public String getMessage() {
        return Message;
    }

    public boolean isStatus() {
        return Status;
    }

    public WheelDetails getWheelDetails() {
        return wheelDetails;
    }
}
