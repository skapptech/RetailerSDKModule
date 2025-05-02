package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DreamModel {
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("Skcode")
    private String Skcode;
    @SerializedName("WalletPoint")
    private int WalletPoint;
    @SerializedName("DreamItemDetails")
    private final ArrayList<DreamItemDetails> DeamItemList;

    public int getWalletPoint() {
        return WalletPoint;
    }

    public void setWalletPoint(int walletPoint) {
        WalletPoint = walletPoint;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }


    public DreamModel(String skcode, int customerId, int walletPoint, ArrayList<DreamItemDetails> deamItemList) {
        WalletPoint = walletPoint;
        CustomerId = customerId;
        Skcode = skcode;
        DeamItemList = deamItemList;
    }
}