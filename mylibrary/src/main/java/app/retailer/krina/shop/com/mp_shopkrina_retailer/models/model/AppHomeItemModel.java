package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;

public class AppHomeItemModel {
    @SerializedName("Status")
    private boolean Status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("ItemMasters")
    private ArrayList<ItemListModel> itemListModels;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<ItemListModel> getItemListModels() {
        return itemListModels;
    }

    public void setItemListModels(ArrayList<ItemListModel> itemListModels) {
        this.itemListModels = itemListModels;
    }
}
