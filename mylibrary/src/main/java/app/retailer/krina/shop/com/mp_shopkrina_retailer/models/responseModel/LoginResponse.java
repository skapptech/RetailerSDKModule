package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse;

public class LoginResponse {
    @SerializedName("Status")
    private String Status;

    @SerializedName("NotAuthorize")
    private boolean NotAuthorize;

    @SerializedName("Message")
    private String Message;

    @SerializedName("customers")
    private CustomerResponse customers;

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

    public CustomerResponse getCustomers() {
        return customers;
    }

    public void setCustomers(CustomerResponse customers) {
        this.customers = customers;
    }

    public boolean isNotAuthorize() {
        return NotAuthorize;
    }
}
