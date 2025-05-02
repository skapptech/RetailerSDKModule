package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyFavModel {
    @SerializedName("customerId")
    private int customerId;
    @SerializedName("WarehouseId")
    private int warehouseId;
    @SerializedName("lang")
    private String lang;
    @SerializedName("items")
    private ArrayList<FavItemsDetails> favItems;


    public MyFavModel(int customerId, int warehouseId, String lang, ArrayList<FavItemsDetails> favItems) {
        this.customerId = customerId;
        this.warehouseId = warehouseId;
        this.lang = lang;
        this.favItems = favItems;
    }

    public List<FavItemsDetails> getFavItems() {
        return favItems;
    }

    public void setFavItems(ArrayList<FavItemsDetails> favItems) {
        this.favItems = favItems;
    }
}
