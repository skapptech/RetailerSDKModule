package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class ReturnOrderStatusModel {
    @SerializedName("KKRequestId")
    private int id;
    @SerializedName("Status")
    private String status;
    @SerializedName("Message")
    private String message;
    @SerializedName("CreatedDate")
    private String date;

    public ReturnOrderStatusModel() {
    }

    public ReturnOrderStatusModel(int id, String status, String message, String date) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
