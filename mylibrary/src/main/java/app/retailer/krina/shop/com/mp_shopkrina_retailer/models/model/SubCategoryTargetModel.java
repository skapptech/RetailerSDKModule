package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TargetConditionsModel;

public class SubCategoryTargetModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("SKCode")
    private String Skcode;
    @SerializedName("CurrentMonthSales")
    private double CurrentMonthSales;
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
    private int SubCatId;

    @SerializedName("NoOfLineItem")
    private int NoOfLineItem;
    @SerializedName("RequiredNoOfLineItem")
    private int RequiredNoOfLineItem ;
    @SerializedName("CompanyName")
    private String CompanyName;
    @SerializedName("CompanyLogoUrl")
    private String CompanyLogoUrl;
    @SerializedName("BrandNames")
    private String BrandNames;
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
    @SerializedName("GiftImage")
    private ArrayList<GiftItemDcs> mGiftItemDcs;

    @SerializedName("Value")
    private double Value;

    @SerializedName("LeftDays")
    private int LeftDays;

    @SerializedName("targetConditions")
    private ArrayList <TargetConditionsModel>model;

    @SerializedName("StoreUrl")
    private String StoreUrl;

    @SerializedName("Type")
    private String Type;



    public SubCategoryTargetModel(double currentMonthSales, double target, String companyName, String valueType, boolean IsClaimed, double value, int leftDays, ArrayList<TargetConditionsModel> targetConditionsModels, String storeUrl,String type) {
        CurrentMonthSales = currentMonthSales;
        Target = target;
        CompanyName = companyName;
        this.valueType = valueType;
        this.IsClaimed = IsClaimed;
        this.Value = value;
        this.LeftDays = leftDays;
        this.model = targetConditionsModels;
        this.StoreUrl =storeUrl;
        this.Type =type;
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

    public double getCurrentMonthSales() {
        return CurrentMonthSales;
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
        return SubCatId;
    }

    public int getNoOfLineItem() {
        return NoOfLineItem;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCompanyLogoUrl() {
        return CompanyLogoUrl;
    }

    public String getBrandNames() {
        return BrandNames;
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

    public ArrayList<GiftItemDcs> getmGiftItemDcs() {
        return mGiftItemDcs;
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
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
