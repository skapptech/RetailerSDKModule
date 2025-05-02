package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CategoryDetailsModel {
    @SerializedName("CategoryName")
    public String CategoryName;
    @SerializedName("LogoUrl")
    public String LogoUrl;
    @SerializedName("Categoryid")
    public int Categoryid;
    @SerializedName("BaseCategoryId")
    public int BaseCategoryId;
    @SerializedName("itemcount")
    public int itemcount;

    public int getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(int categoryid) {
        Categoryid = categoryid;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public int getBaseCategoryId() {
        return BaseCategoryId;
    }

    public void setBaseCategoryId(int baseCategoryId) {
        BaseCategoryId = baseCategoryId;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }
}
