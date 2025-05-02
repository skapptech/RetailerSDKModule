package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class AllBrandsModel {
    @SerializedName("SubsubcategoryName")
    public String SubsubcategoryName;
    @SerializedName("LogoUrl")
    public String LogoUrl;
    @SerializedName("SubsubCategoryid")
    public int SubsubCategoryid;
    @SerializedName("Categoryid")
    public int categoryId;


    public String getSubsubcategoryName() {
        return SubsubcategoryName;
    }

    public void setSubsubcategoryName(String subsubcategoryName) {
        SubsubcategoryName = subsubcategoryName;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public int getSubsubCategoryid() {
        return SubsubCategoryid;
    }

    public void setSubsubCategoryid(int subsubCategoryid) {
        SubsubCategoryid = subsubCategoryid;
    }

    public int getCategoryid() {
        return categoryId;
    }

    public void setCategoryid(int categoryid) {
        categoryId = categoryid;
    }

}
