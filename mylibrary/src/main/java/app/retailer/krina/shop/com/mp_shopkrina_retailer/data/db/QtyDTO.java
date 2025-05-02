package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db;

public class QtyDTO {
    int totalQuantity = 0;
    int quantity = 0;

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}