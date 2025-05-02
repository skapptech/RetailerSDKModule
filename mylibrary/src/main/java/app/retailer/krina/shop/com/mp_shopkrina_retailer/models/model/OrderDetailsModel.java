package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyorderPaymentDetails;

public class OrderDetailsModel {

    @SerializedName("orderid")
    public int orderid;
    @SerializedName("Deliverydate")
    public String Deliverydate;
    @SerializedName("GrossAmount")
    public double GrossAmount;
    @SerializedName("PayableAmount")
    public double PayableAmount;
    @SerializedName("ItemName")
    public String ItemName;
    @SerializedName("qty")
    public int qty;
    @SerializedName("ItemAmount")
    public double ItemAmount;
    @SerializedName("OrderTakenSalesPersonId")
    public int OrderTakenSalesPersonId;
    @SerializedName("OrderTakenBy")
    public String OrderTakenBy;
    @SerializedName("DeliveredBy")
    public String DeliveredBy;
    @SerializedName("ItemCount")
    public int ItemCount;
    @SerializedName("TotalQty")
    public int TotalQty;
    @SerializedName("OrderTakenSalesPersonMobile")
    public String OrderTakenSalesPersonMobile;
    @SerializedName("OrderTakenSalesPersonProfilePic")
    public String OrderTakenSalesPersonProfilePic;
    @SerializedName("DeliveryPersonMobile")
    public String DeliveryPersonMobile;
    @SerializedName("DboyProfilePic")
    public String DboyProfilePic;
    @SerializedName("OrderTakenRating")
    public String OrderTakenRating;
    @SerializedName("DeliveryBoyRating")
    public String DeliveryBoyRating;
    @SerializedName("IsETAEnable")
    public boolean isEta;
    @SerializedName("Rating")
    public float rating;
    @SerializedName("IsPlayWeel")
    private boolean IsPlayWheel;
    @SerializedName("WheelCount")
    private int wheelCount;
    @SerializedName("WheelList")
    private ArrayList<Integer> wheelList;
    @SerializedName("OrderPaymentDCs")
    private ArrayList<MyorderPaymentDetails> orderPayments;
    @SerializedName("OrderChargesDc")
    public ArrayList<OrderCharges> orderCharges;


    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getDeliverydate() {
        return Deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        Deliverydate = deliverydate;
    }

    public double getGrossAmount() {
        return GrossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        GrossAmount = grossAmount;
    }

    public double getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(double itemAmount) {
        ItemAmount = itemAmount;
    }

    public int getOrderTakenSalesPersonId() {
        return OrderTakenSalesPersonId;
    }

    public void setOrderTakenSalesPersonId(int orderTakenSalesPersonId) {
        OrderTakenSalesPersonId = orderTakenSalesPersonId;
    }

    public String getOrderTakenBy() {
        return OrderTakenBy;
    }

    public void setOrderTakenBy(String orderTakenBy) {
        OrderTakenBy = orderTakenBy;
    }

    public String getDeliveredBy() {
        return DeliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        DeliveredBy = deliveredBy;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public int getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(int totalQty) {
        TotalQty = totalQty;
    }

    public String getOrderTakenSalesPersonMobile() {
        return OrderTakenSalesPersonMobile;
    }

    public void setOrderTakenSalesPersonMobile(String orderTakenSalesPersonMobile) {
        OrderTakenSalesPersonMobile = orderTakenSalesPersonMobile;
    }

    public String getOrderTakenSalesPersonProfilePic() {
        return OrderTakenSalesPersonProfilePic;
    }

    public void setOrderTakenSalesPersonProfilePic(String orderTakenSalesPersonProfilePic) {
        OrderTakenSalesPersonProfilePic = orderTakenSalesPersonProfilePic;
    }

    public String getDeliveryPersonMobile() {
        return DeliveryPersonMobile;
    }

    public void setDeliveryPersonMobile(String deliveryPersonMobile) {
        DeliveryPersonMobile = deliveryPersonMobile;
    }

    public String getDboyProfilePic() {
        return DboyProfilePic;
    }

    public void setDboyProfilePic(String dboyProfilePic) {
        DboyProfilePic = dboyProfilePic;
    }

    public String getOrderTakenRating() {
        return OrderTakenRating;
    }

    public void setOrderTakenRating(String orderTakenRating) {
        OrderTakenRating = orderTakenRating;
    }

    public String getDeliveryBoyRating() {
        return DeliveryBoyRating;
    }

    public void setDeliveryBoyRating(String deliveryBoyRating) {
        DeliveryBoyRating = deliveryBoyRating;
    }

    public boolean isEta() {
        return isEta;
    }

    public float getRating() {
        return rating;
    }

    public boolean isPlayWheel() {
        return IsPlayWheel;
    }

    public int getWheelCount() {
        return wheelCount;
    }

    public ArrayList<Integer> getWheelList() {
        return wheelList;
    }

    public ArrayList<MyorderPaymentDetails> getOrderPayments() {
        return orderPayments;
    }

    public class OrderCharges{
        public String ChargesType;
        public double Amount;
    }
}
