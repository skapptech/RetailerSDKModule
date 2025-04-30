package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryConcern {
    @SerializedName("CustComment")
    public String custComment;
    @SerializedName("ModifiedDate")
    public String modifiedDate;
    @SerializedName("Status")
    public String status;
    @SerializedName("CDComment")
    public String cDComment;
    @SerializedName("Amount")
    public double amount;
    @SerializedName("Deliverydate")
    public String deliveryDate;
    @SerializedName("OrderDate")
    public String orderDate;
    @SerializedName("OrderId")
    public int orderId;
    @SerializedName("ShopName")
    public String shopName;
    @SerializedName("Skcode")
    public String skcode;
}
