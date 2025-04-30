package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerTargetResponse {
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private boolean Status;
    @Expose
    @SerializedName("GetTargetData")
    private GetTargetDataBean GetTargetData;

    public String getMessage() {
        return Message;
    }

    public boolean getStatus() {
        return Status;
    }

    public GetTargetDataBean getGetTargetData() {
        return GetTargetData;
    }

    public class GetTargetDataBean {
        @Expose
        @SerializedName("LeftDays")
        private int LeftDays;
        @Expose
        @SerializedName("AchivePercent")
        private double AchivePercent;
        @Expose
        @SerializedName("TotalPurchaseAmount")
        private double TotalPurchaseAmount;
        @Expose
        @SerializedName("TargetAmount")
        private double TargetAmount;
        @Expose
        @SerializedName("GiftImage")
        private String GiftImage;

        @Expose
        @SerializedName("GiftItemName")
        private String GiftItemName;

        @Expose
        @SerializedName("SKCode")
        private String SKCode;
        @Expose
        @SerializedName("Type")
        private String Type;
        @Expose
        @SerializedName("Level")
        private String Level;
        @Expose
        @SerializedName("Value")
        private double Value;
        @Expose
        @SerializedName("IsClaimed")
        private boolean IsClaimed;
        @Expose
        @SerializedName("TotalPendingPurchaseAmount")
        private double totalPendingPurchaseAmount;
        @Expose
        @SerializedName("OfferDesc")
        public String offerDesc;
        @Expose
        @SerializedName("OfferValue")
        public String offerValue;
        @Expose
        @SerializedName("OfferType")
        public int offerType;
        @SerializedName("targetConditions")
        private ArrayList<TargetConditionsModel> model;
        @Expose
        @SerializedName("StoreUrl")
        private String StoreUrl;
        @Expose
        @SerializedName("BrandNames")
        public String brandNames;

        @SerializedName("CompanyId")
        public int companyId;
        @SerializedName("TargetMonth")
        public String targetMonth;

        public String getStoreUrl() {
            return StoreUrl;
        }

        public void setStoreUrl(String storeUrl) {
            StoreUrl = storeUrl;
        }

        public String getLevel() {
            return Level;
        }

        public boolean isClaimed() {
            return IsClaimed;
        }

        public int getLeftDays() {
            return LeftDays;
        }

        public double getAchivePercent() {
            return AchivePercent;
        }

        public double getTotalPurchaseAmount() {
            return TotalPurchaseAmount;
        }

        public double getTargetAmount() {
            return TargetAmount;
        }

        public String getGiftImage() {
            return GiftImage;
        }

        public String getGiftItemName() {
            return GiftItemName;
        }

        public String getType() {
            return Type;
        }

        public double getValue() {
            return Value;
        }

        public double getTotalPendingPurchaseAmount() {
            return totalPendingPurchaseAmount;
        }

        public ArrayList<TargetConditionsModel> getModel() {
            return model;
        }

        public void setModel(ArrayList<TargetConditionsModel> model) {
            this.model = model;
        }
    }
}