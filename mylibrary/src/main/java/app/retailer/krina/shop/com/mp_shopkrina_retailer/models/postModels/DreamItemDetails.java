package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class DreamItemDetails {
    @SerializedName("ItemId")
    private final int ItemId;
    @SerializedName("OrderQty")
    private final int OrderQty;

    public DreamItemDetails(int itemId, int orderQty) {
        ItemId = itemId;
        OrderQty = orderQty;
    }
}