package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class RBLResponse {
    @SerializedName("Status")
    private String Status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("RBLCustomerInformation")
    private RBLCustomerInformation RBLCustomerInformation;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public RBLCustomerInformation getRBLCustomerInformation() {
        return RBLCustomerInformation;
    }

    public void setRBLCustomerInformation(RBLCustomerInformation RBLCustomerInformation) {
        this.RBLCustomerInformation = RBLCustomerInformation;
    }
}
