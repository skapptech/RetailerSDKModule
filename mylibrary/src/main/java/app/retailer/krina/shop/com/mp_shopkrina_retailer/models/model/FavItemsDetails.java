package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "WishList")
public class FavItemsDetails {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ItemId")
    private int itemId;

    public FavItemsDetails(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}