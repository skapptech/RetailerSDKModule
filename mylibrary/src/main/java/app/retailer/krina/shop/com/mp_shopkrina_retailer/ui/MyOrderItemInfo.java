package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui;

public class MyOrderItemInfo {
    String orderDate,deliveryDate,amount,items,status,is_cancelled,orderNumber,deliveryCharges,updateddate,ReadytoDispatchedDate;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getReadytoDispatchedDate() {
        return ReadytoDispatchedDate;
    }

    public void setReadytoDispatchedDate(String readytoDispatchedDate) {
        ReadytoDispatchedDate = readytoDispatchedDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(String is_cancelled) {
        this.is_cancelled = is_cancelled;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }
}
