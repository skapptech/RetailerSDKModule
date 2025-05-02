package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TargetCustomerBrandDcs implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("BrandName")
    private String BrandName;
    @SerializedName("Target")
    private double Target;
    @SerializedName("currentTarget")
    private double currentTarget;

    public int getId() {
        return id;
    }

    public String getBrandName() {
        return BrandName;
    }

    public double getTarget() {
        return Target;
    }

    public double getCurrentTarget() {
        return currentTarget;
    }
}
