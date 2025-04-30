package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class AddCustomerModel {

    @SerializedName("LoginCustId")
    public String LoginCustId;

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Type")
    public String Type;

    public AddCustomerModel(String loginCustId, String mobile, String name, String type) {
        LoginCustId = loginCustId;
        Mobile = mobile;
        Name = name;
        Type = type;
    }
}
