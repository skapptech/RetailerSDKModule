package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class OrderSummaryModel {

    @SerializedName("TotalAmount")
    private double TotalAmount;
    @SerializedName("Id")
    private int Id;
    @SerializedName("TotalOrder")
    private int TotalOrder;
    @SerializedName("Comment")
    private String Comment;
    @SerializedName("status")
    private String status;

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        TotalOrder = totalOrder;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
