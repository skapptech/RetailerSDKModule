package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class PostPaymentResponse {

    @SerializedName("encdata")
    String encdata;
    @SerializedName("Status")
    boolean Status;
    @SerializedName("Message")
    String Message;

    public String getEncdata() {
        return encdata;
    }

    public void setEncdata(String encdata) {
        this.encdata = encdata;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}