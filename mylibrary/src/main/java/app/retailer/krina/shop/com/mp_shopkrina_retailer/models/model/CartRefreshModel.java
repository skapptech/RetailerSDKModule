package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostOfferItemsList;

public class CartRefreshModel {

    @SerializedName("CustomerId")
    public int CustomerId;
    @SerializedName("WarehouseId")
    public int WarehouseId;
    @SerializedName("Message")
    public boolean Message;
    @SerializedName("OfferItems")
    public ArrayList<PostOfferItemsList> OfferItems;


    public CartRefreshModel(int CustomerId, int WarehouseId, ArrayList<PostOfferItemsList> OfferItems) {
        this.CustomerId = CustomerId;
        this.WarehouseId = WarehouseId;
        this.OfferItems = OfferItems;
    }


    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        WarehouseId = warehouseId;
    }

    public boolean isMessage() {
        return Message;
    }

    public void setMessage(boolean message) {
        Message = message;
    }

    public ArrayList<PostOfferItemsList> getOfferItems() {
        return OfferItems;
    }

    public void setOfferItems(ArrayList<PostOfferItemsList> offerItems) {
        OfferItems = offerItems;
    }
}
