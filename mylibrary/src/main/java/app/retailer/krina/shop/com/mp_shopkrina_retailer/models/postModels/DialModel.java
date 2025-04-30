package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DialModel implements Serializable {
    @SerializedName("WarehouseId")
    private int warehouseid;
    @SerializedName("CustomerId")
    private int customerid;
    @SerializedName("Skcode")
    private String skcode;
    @SerializedName("DialEarnigPoint")
    private int dialearnigpoint;
    @SerializedName("WheelAmountLimit")
    private double wheelamountlimit;
    @SerializedName("WheelCount")
    private int wheelcount;
    @SerializedName("TotalAmount")
    private double totalamount;
    @SerializedName("OrderId")
    private int orderid;
    @SerializedName("PlayedWheelCount")
    private int PlayedWheelCount;

    public DialModel(int warehouseid, int customerid, String skcode, int dialearnigpoint, double wheelamountlimit, int wheelcount, double totalamount, int orderid, int playedWheelCount) {
        this.warehouseid = warehouseid;
        this.customerid = customerid;
        this.skcode = skcode;
        this.dialearnigpoint = dialearnigpoint;
        this.wheelamountlimit = wheelamountlimit;
        this.wheelcount = wheelcount;
        this.totalamount = totalamount;
        this.orderid = orderid;
        PlayedWheelCount = playedWheelCount;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getSkcode() {
        return skcode;
    }

    public void setSkcode(String skcode) {
        this.skcode = skcode;
    }

    public int getDialearnigpoint() {
        return dialearnigpoint;
    }

    public void setDialearnigpoint(int dialearnigpoint) {
        this.dialearnigpoint = dialearnigpoint;
    }

    public double getWheelamountlimit() {
        return wheelamountlimit;
    }

    public void setWheelamountlimit(double wheelamountlimit) {
        this.wheelamountlimit = wheelamountlimit;
    }

    public int getWheelcount() {
        return wheelcount;
    }

    public void setWheelcount(int wheelcount) {
        this.wheelcount = wheelcount;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getPlayedWheelCount() {
        return PlayedWheelCount;
    }

    public void setPlayedWheelCount(int playedWheelCount) {
        PlayedWheelCount = playedWheelCount;
    }
}
