package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NotifyTb")
public class NotifyModel {

    @PrimaryKey(autoGenerate = true)
    private int ItemId;

    public NotifyModel(int itemId) {
        ItemId = itemId;
    }

    public NotifyModel() {
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}