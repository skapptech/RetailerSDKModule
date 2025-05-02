package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel;

public class CheckScratchCardResponse {
    @SerializedName("ScratchBillDiscount")
    private BillDiscountModel billDiscount;
    @SerializedName("Status")
    private boolean Status;
    @SerializedName("Message")
    private String Message;

    public BillDiscountModel getBillDiscount() {
        return billDiscount;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}