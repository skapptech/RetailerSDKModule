package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class OrderItemDetails  implements Serializable,Comparable<OrderItemDetails> {
    public static final int HEADER = 0;
    public static final int VIEW = 1;
    @SerializedName("ItemId")
    private int ItemId;
    @SerializedName("qty")
    private int qty;
    @SerializedName("UnitPrice")
    private double UnitPrice;
    @SerializedName("ItemName")
    private String ItemName;
    @SerializedName("LogoUrl")
    private String LogoUrl;

    @SerializedName("StoreName")
    private String StoreName;


    public void setToSectionHeader() {
        isSectionHeader = true;
    }

    public boolean isSectionHeader() {
        return isSectionHeader;
    }
    private boolean isSectionHeader;


    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String LogoUrl) {
        this.LogoUrl = LogoUrl;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int ItemId) {
        this.ItemId = ItemId;
    }

    @Override
    public int compareTo(OrderItemDetails orderItemDetails) {
        return orderItemDetails.getStoreName().compareTo(getStoreName());
    }
}
