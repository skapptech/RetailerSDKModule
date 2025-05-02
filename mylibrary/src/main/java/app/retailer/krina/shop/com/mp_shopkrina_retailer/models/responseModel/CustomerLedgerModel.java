package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerLedgerModel {

    @SerializedName("LadgerItem")
    private LadgerItemModel ladgerItemModel;
    @SerializedName("ClosingBalance")
    private double ClosingBalance;
    @SerializedName("LadgerEntryList")
    private ArrayList<LadgerEntryListModel> LadgerEntryListModel;
    private double OpeningBalance;

    public double getClosingBalance() {
        return ClosingBalance;
    }

    public double getOpeningBalance() {
        return OpeningBalance;
    }

    public LadgerItemModel getLadgerItemModel() {
        return ladgerItemModel;
    }

    public ArrayList<LadgerEntryListModel> getLadgerEntryListModel() {
        return LadgerEntryListModel;
    }
}