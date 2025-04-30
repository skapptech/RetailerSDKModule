package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel;

public class ScratchCardListResponse {
    @SerializedName("ScratchBillDiscount")
    private ArrayList<BillDiscountModel> billDiscountList;
    @SerializedName("Status")
    private boolean Status;
    @SerializedName("Message")
    private String Message;

    public ArrayList<BillDiscountModel> getBillDiscountList() {
        return billDiscountList;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}