package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

public class AppHomeItemPurchaseModel {
   /* @SerializedName("Status")
    private boolean Status;

    @SerializedName("Message")
    private String Message;
*/
    @SerializedName("ItemDataDCs")
    private ArrayList<ItemListModel> itemListModels;


    public ArrayList<ItemListModel> getItemListModels() {
        return itemListModels;
    }

    public void setItemListModels(ArrayList<ItemListModel> itemListModels) {
        this.itemListModels = itemListModels;
    }
}
