package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse;

public class ShoppingCartResponse implements Serializable {
    @SerializedName("Customerphonenum")
    private String Customerphonenum;

    @SerializedName("Trupay")
    private boolean Trupay;

    @SerializedName("Skcode")
    private String Skcode;

    @SerializedName("TotalAmount")
    private double TotalAmount;

    @SerializedName("paymentThrough")
    private String paymentThrough;

    @SerializedName("CreatedDate")
    private String CreatedDate;

    @SerializedName("itemDetails")
    private ArrayList<ShopingCartItemDetailsResponse> shopCartItemDetails;

    public String getCustomerphonenum() {
        return Customerphonenum;
    }

    public void setCustomerphonenum(String customerphonenum) {
        Customerphonenum = customerphonenum;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public boolean isTrupay() {
        return Trupay;
    }

    public void setTrupay(boolean trupay) {
        Trupay = trupay;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getPaymentThrough() {
        return paymentThrough;
    }

    public void setPaymentThrough(String paymentThrough) {
        this.paymentThrough = paymentThrough;
    }

    public ArrayList<ShopingCartItemDetailsResponse> getShopCartItemDetails() {
        return shopCartItemDetails;
    }

    public void setShopCartItemDetails(ArrayList<ShopingCartItemDetailsResponse> shopCartItemDetails) {
        this.shopCartItemDetails = shopCartItemDetails;
    }
}
