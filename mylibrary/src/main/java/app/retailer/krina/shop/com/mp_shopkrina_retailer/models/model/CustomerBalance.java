package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerBalance implements Serializable {

    @SerializedName("CrCount")
    public int CrCount;

    @SerializedName("DrCount")
    public int DrCount;

    @SerializedName("CrAmount")
    public double CrAmount;

    @SerializedName("DrAmount")
    public double DrAmount;

    @SerializedName("Balance")
    public double Balance;

    public int getCrCount() {
        return CrCount;
    }

    public void setCrCount(int crCount) {
        CrCount = crCount;
    }

    public int getDrCount() {
        return DrCount;
    }

    public void setDrCount(int drCount) {
        DrCount = drCount;
    }

    public double getCrAmount() {
        return CrAmount;
    }

    public void setCrAmount(double crAmount) {
        CrAmount = crAmount;
    }

    public double getDrAmount() {
        return DrAmount;
    }

    public void setDrAmount(double drAmount) {
        DrAmount = drAmount;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }
}