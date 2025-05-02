package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CheckBookData {
    @SerializedName("data")
    private CheckBookCustomerLimit checkBookCustomerLimit;

    public CheckBookCustomerLimit getCheckBookCustomerLimit() {
        return checkBookCustomerLimit;
    }

    public void setCheckBookCustomerLimit(CheckBookCustomerLimit checkBookCustomerLimit) {
        this.checkBookCustomerLimit = checkBookCustomerLimit;
    }
}
