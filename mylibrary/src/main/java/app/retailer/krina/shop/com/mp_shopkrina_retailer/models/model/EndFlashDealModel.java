package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;


public class EndFlashDealModel {
    public boolean status ;
    public int itemId;

    public EndFlashDealModel(boolean status, int itemId) {
        this.status = status;
        this.itemId = itemId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
