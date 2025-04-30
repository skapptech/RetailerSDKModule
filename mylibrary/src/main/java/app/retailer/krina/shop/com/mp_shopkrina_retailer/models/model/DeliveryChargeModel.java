package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryChargeModel {
    @SerializedName("id")
    public int id;

    @SerializedName("min_Amount")
    public int min_Amount;

    @SerializedName("max_Amount")
    public int max_Amount;

    @SerializedName("del_Charge")
    public double del_Charge;

    @SerializedName("IsActive")
    public String IsActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin_Amount() {
        return min_Amount;
    }

    public void setMin_Amount(int min_Amount) {
        this.min_Amount = min_Amount;
    }

    public double getMax_Amount() {
        return max_Amount;
    }

    public void setMax_Amount(int max_Amount) {
        this.max_Amount = max_Amount;
    }

    public double getDel_Charge() {
        return del_Charge;
    }

    public void setDel_Charge(double del_Charge) {
        this.del_Charge = del_Charge;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
