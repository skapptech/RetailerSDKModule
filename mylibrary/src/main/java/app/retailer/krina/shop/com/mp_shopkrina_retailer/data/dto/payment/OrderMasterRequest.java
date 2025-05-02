package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderMasterRequest implements Serializable {
    @SerializedName("PlayedWheelCount")
    private int PlayedWheelCount;
    @SerializedName("WarehouseId")
    private final int warehouseid;
    @SerializedName("lang")
    public String lang;
    @SerializedName("CustomerId")
    private final int customerid;
    @SerializedName("Skcode")
    private final String skcode;
    @SerializedName("DialEarnigPoint")
    private int dialearnigpoint;
    @SerializedName("WheelList")
    private final ArrayList<Integer> WheelList;
    @SerializedName("WheelAmountLimit")
    private final double wheelamountlimit;
    @SerializedName("WheelCount")
    private final int wheelcount;
    @SerializedName("TotalAmount")
    private final double totalamount;
    @SerializedName("OrderId")
    private final int orderid;

    public OrderMasterRequest(int warehouseid, int customerid, String skcode, int dialearnigpoint, double wheelamountlimit, int wheelcount, ArrayList<Integer> wheelList, double totalamount, int orderid, int playedWheelCount) {
        this.warehouseid = warehouseid;
        this.customerid = customerid;
        this.skcode = skcode;
        this.dialearnigpoint = dialearnigpoint;
        this.wheelamountlimit = wheelamountlimit;
        this.wheelcount = wheelcount;
        WheelList = wheelList;
        this.totalamount = totalamount;
        this.orderid = orderid;
        PlayedWheelCount = playedWheelCount;
    }
}