package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class GenerateFinBoxUser {
    @SerializedName("customerID")
    public String customerID;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("isCreditLine")
    private boolean isCreditLine;

    public GenerateFinBoxUser(String customerID, String mobile) {
        this.customerID = customerID;
        this.mobile = mobile;
    }

    public GenerateFinBoxUser(String customerID, String mobile, boolean isCreditLine) {
        this.customerID = customerID;
        this.mobile = mobile;
        this.isCreditLine = isCreditLine;
    }
}
