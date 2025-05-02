package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TargetConditionsModel;

public class NewTargetModel implements Serializable {
    @SerializedName("TargetMonth")
    public String targetMonth;
    @SerializedName("CompanyId")
    public int companyId;
    @SerializedName("StoreId")
    public int storeId;
    @SerializedName("StoreName")
    public String storeName;
    @SerializedName("BrandNames")
    public String brandNames;
    @SerializedName("id")
    private int id;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("SKCode")
    private String Skcode;
    @SerializedName("AchivePercent")
    private double AchievePercent;
    @SerializedName("Target")
    private double Target;
    @SerializedName("IsClaimed")
    private boolean IsClaimed;
    @SerializedName("IsCompleted")
    private boolean IsCompleted;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("IsTargetExpire")
    private boolean IsTargetExpire;
    @SerializedName("valueType")
    private String valueType;
    @SerializedName("WalletValue")
    private int WalletValue;
    @SerializedName("SlabLowerLimit")
    private int SlabLowerLimit;
    @SerializedName("SlabUpperLimit")
    private int SlabUpperLimit;
    @SerializedName("SubCatId")
    private int subCatId;
    @SerializedName("NoOfLineItem")
    private int noOfLineItem;
    @SerializedName("RequiredNoOfLineItem")
    private int RequiredNoOfLineItem;
    @SerializedName("CompanyName")
    private String CompanyName;
    @SerializedName("CompanyLogoUrl")
    private String CompanyLogoUrl;
    @SerializedName("StartDate")
    private String StartDate;
    @SerializedName("EndDate")
    private String EndDate;
    @SerializedName("TargetDetailId")
    private int TargetDetailId;
    @SerializedName("TargetCustomerBrandDcs")
    private ArrayList<TargetCustomerBrandDcs> mTargetCustomerBrandDcs;
    @SerializedName("TargetCustomerItemDcs")
    private ArrayList<TargetCustomerItemDcs> mTargetCustomerItemDcs;
    @SerializedName("Value")
    private double Value;
    @SerializedName("LeftDays")
    private int LeftDays;
    @SerializedName("targetConditions")
    private ArrayList<TargetConditionsModel> model;
    @SerializedName("StoreUrl")
    private String StoreUrl;
    @SerializedName("Type")
    private String type;
    @SerializedName("GiftImage")
    private String GiftImage;
    @SerializedName("GiftItemName")
    private String GiftItemName;
    @Expose
    @SerializedName("OfferDesc")
    public String offerDesc;


    public NewTargetModel(String targetMonth, int companyId, String brandNames, double achivePercent, double target, String companyName, String valueType, boolean IsClaimed, double value, int leftDays, ArrayList<TargetConditionsModel> targetConditionsModels, String storeUrl, String type, String giftImage, String giftItemName, String offerDesc) {
        this.targetMonth = targetMonth;
        this.companyId = companyId;
        this.brandNames = brandNames;
        AchievePercent = achivePercent;
        Target = target;
        CompanyName = companyName;
        this.valueType = valueType;
        this.IsClaimed = IsClaimed;
        this.Value = value;
        this.LeftDays = leftDays;
        this.model = targetConditionsModels;
        this.StoreUrl = storeUrl;
        this.type = type;
        this.GiftImage = giftImage;
        this.GiftItemName = giftItemName;
        this.offerDesc = offerDesc;
    }

    public int getRequiredNoOfLineItem() {
        return RequiredNoOfLineItem;
    }

    public void setRequiredNoOfLineItem(int requiredNoOfLineItem) {
        RequiredNoOfLineItem = requiredNoOfLineItem;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getShopName() {
        return ShopName;
    }

    public String getSkcode() {
        return Skcode;
    }

    public double getAchivePercent() {
        return AchievePercent;
    }

    public double getTarget() {
        return Target;
    }

    public boolean isClaimed() {
        return IsClaimed;
    }

    public boolean isCompleted() {
        return IsCompleted;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public boolean isTargetExpire() {
        return IsTargetExpire;
    }

    public String getValueType() {
        return valueType;
    }

    public int getWalletValue() {
        return WalletValue;
    }

    public int getSlabLowerLimit() {
        return SlabLowerLimit;
    }

    public int getSlabUpperLimit() {
        return SlabUpperLimit;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public int getNoOfLineItem() {
        return noOfLineItem;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCompanyLogoUrl() {
        return CompanyLogoUrl;
    }

    public String getBrandNames() {
        return brandNames;
    }

    public String getStartDate() {
        return StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public int getTargetDetailId() {
        return TargetDetailId;
    }

    public ArrayList<TargetCustomerBrandDcs> getmTargetCustomerBrandDcs() {
        return mTargetCustomerBrandDcs;
    }

    public ArrayList<TargetCustomerItemDcs> getmTargetCustomerItemDcs() {
        return mTargetCustomerItemDcs;
    }


    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public int getLeftDays() {
        return LeftDays;
    }

    public void setLeftDays(int leftDays) {
        LeftDays = leftDays;
    }


    public ArrayList<TargetConditionsModel> getModel() {
        return model;
    }

    public void setModel(ArrayList<TargetConditionsModel> model) {
        this.model = model;
    }

    public String getStoreUrl() {
        return StoreUrl;
    }

    public void setStoreUrl(String storeUrl) {
        StoreUrl = storeUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGiftImage() {
        return GiftImage;
    }

    public void setGiftImage(String giftImage) {
        GiftImage = giftImage;
    }

    public String getGiftItemName() {
        return GiftItemName;
    }

    public void setGiftItemName(String giftItemName) {
        GiftItemName = giftItemName;
    }
}