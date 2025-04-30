package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnOrderItemModel {
    @Expose
    @SerializedName("IsReplaceable")
    public boolean isReplaceable;
    @Expose
    @SerializedName("IsReturnReplaced")
    public boolean isReturnReplaced;
    @Expose
    @SerializedName("RequestType")
    public int requestType;
    @Expose
    @SerializedName("Qty")
    public int qty;
    @Expose
    @SerializedName("price")
    public double price;
    @Expose
    @SerializedName("ItemName")
    public String itemName;
    @Expose
    @SerializedName("Itempic")
    public String itemPic;
    @Expose
    @SerializedName("ItemId")
    public String itemId;
    @Expose
    @SerializedName("Cust_Comment")
    public String comment;
    @Expose
    @SerializedName("OrderDetailsId")
    public int orderDetailsId;
    public int returnQty;
    public boolean isSelected;


    public boolean isReplaceable() {
        return isReplaceable;
    }

    public boolean isReturnReplaced() {
        return isReturnReplaced;
    }

    public int getRequestType() {
        return requestType;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public String getItemPic() {
        return itemPic;
    }

    public String getItemId() {
        return itemId;
    }

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}