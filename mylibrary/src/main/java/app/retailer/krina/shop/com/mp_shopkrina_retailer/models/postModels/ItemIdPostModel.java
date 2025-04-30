package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class ItemIdPostModel {

    @SerializedName("Number")
    private String number;

    @SerializedName("ItemMultiMRPId")
    private int itemMultiMRPId;

    @SerializedName("WarehouseId")
    private int warehouseId;

    public ItemIdPostModel(String number, int itemMultiMRPId, int warehouseId) {
        this.number = number;
        this.itemMultiMRPId = itemMultiMRPId;
        this.warehouseId = warehouseId;
    }
}