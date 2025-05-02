package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GiftItemDcs implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("itemid")
    private int itemid;
    @SerializedName("ItemName")
    private String ItemName;
    @SerializedName("Qty")
    private int Qty;

    public int getId() {
        return id;
    }

    public int getItemid() {
        return itemid;
    }

    public String getItemName() {
        return ItemName;
    }

    public int getQty() {
        return Qty;
    }
}
