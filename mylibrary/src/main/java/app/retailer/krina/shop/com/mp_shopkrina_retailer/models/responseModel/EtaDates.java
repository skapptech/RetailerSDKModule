package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EtaDates {
    @SerializedName("ETADates")
    public ArrayList<ETADate> etaDates;

    public static class ETADate {
        public String ETADate;

        public String getETADate() {
            return ETADate;
        }
    }
}
