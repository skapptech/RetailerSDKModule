package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TargetCustomerDC implements Serializable {
    private int id;
    private String ItemName;
    private double Target;
    private double currentTarget;
    private String BrandName;
    private boolean isBrand;

    public TargetCustomerDC(int id, String itemName, double target, double currentTarget, String brandName, boolean isBrand) {
        this.id = id;
        ItemName = itemName;
        Target = target;
        this.currentTarget = currentTarget;
        BrandName = brandName;
        this.isBrand = isBrand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getTarget() {
        return Target;
    }

    public void setTarget(double target) {
        Target = target;
    }

    public double getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(double currentTarget) {
        this.currentTarget = currentTarget;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public boolean isBrand() {
        return isBrand;
    }

    public void setBrand(boolean brand) {
        isBrand = brand;
    }
}
