package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public  class ChangePasswordModel {
    @SerializedName("newpassword")
    private String newpassword;
    @SerializedName("currentpassword")
    private String currentpassword;
    @SerializedName("CustomerId")
    private int CustomerId;
    public ChangePasswordModel(String newpassword, String currentpassword, int customerId) {
        this.newpassword = newpassword;
        this.currentpassword = currentpassword;
        CustomerId = customerId;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }
}
