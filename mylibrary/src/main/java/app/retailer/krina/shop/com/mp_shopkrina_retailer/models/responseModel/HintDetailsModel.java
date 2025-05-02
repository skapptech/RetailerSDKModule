package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class HintDetailsModel {
    @SerializedName("itemname")
    private String itemname;

    @SerializedName("LogoUrl")
    private String LogoUrl;

    @SerializedName("price")
    private String price;

    @SerializedName("SubCategoryId")
    private String SubCategoryId;

    @SerializedName("SubsubCategoryid")
    private String SubsubCategoryid;

    @SerializedName("Categoryid")
    private String Categoryid;

    @SerializedName("BaseCategoryId")
    private String BaseCategoryId;

    @SerializedName("marginPoint")
    private String marginPoint;

    @SerializedName("Number")
    private String Number;

    @SerializedName("MinOrderQty")
    private int MinOrderQty;

    @SerializedName("ItemId")
    private int ItemId;

    @SerializedName("UnitPrice")
    private double UnitPrice;

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    private boolean Checked;

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public int getMinOrderQty() {
        return MinOrderQty;
    }

    public void setMinOrderQty(int minOrderQty) {
        MinOrderQty = minOrderQty;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubsubCategoryid() {
        return SubsubCategoryid;
    }

    public void setSubsubCategoryid(String subsubCategoryid) {
        SubsubCategoryid = subsubCategoryid;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String categoryid) {
        Categoryid = categoryid;
    }

    public String getBaseCategoryId() {
        return BaseCategoryId;
    }

    public void setBaseCategoryId(String baseCategoryId) {
        BaseCategoryId = baseCategoryId;
    }

    public String getMarginPoint() {
        return marginPoint;
    }

    public void setMarginPoint(String marginPoint) {
        this.marginPoint = marginPoint;
    }
}
