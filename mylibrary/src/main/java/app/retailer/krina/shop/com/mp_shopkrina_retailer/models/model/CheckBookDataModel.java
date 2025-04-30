package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CheckBookDataModel {

    @SerializedName("message")
    public String message;

    @SerializedName("results")
    public CheckBookResult checkBookResult;
}
