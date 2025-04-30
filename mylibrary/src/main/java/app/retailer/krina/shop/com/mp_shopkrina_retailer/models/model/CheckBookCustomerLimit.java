package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CheckBookCustomerLimit {
    @SerializedName("message")
    public String message;

    @SerializedName("results")
    public CheckBookCustomerResult checkBookCustomerResult;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CheckBookCustomerResult getCheckBookCustomerResult() {
        return checkBookCustomerResult;
    }

    public void setCheckBookCustomerResult(CheckBookCustomerResult checkBookCustomerResult) {
        this.checkBookCustomerResult = checkBookCustomerResult;
    }
}
