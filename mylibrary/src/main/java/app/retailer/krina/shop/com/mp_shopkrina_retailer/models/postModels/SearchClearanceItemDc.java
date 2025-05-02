package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class SearchClearanceItemDc {
    @SerializedName("Skip")
    public int skip;
    @SerializedName("take")
    public int take;
    @SerializedName("keyword")
    public String keyword;
    @SerializedName("WarehouseId")
    public int warehouseId;
    @SerializedName("CategoryId")
    public int categoryId;
    @SerializedName("Customerid")
    public int customerId;
    @SerializedName("lang")
    public String lang;
}
