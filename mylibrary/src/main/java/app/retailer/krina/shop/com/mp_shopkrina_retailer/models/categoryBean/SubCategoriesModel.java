package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Krishna on 30-12-2016.
 */
public class SubCategoriesModel implements Serializable {
    public boolean isChecked = false;
    @SerializedName("SubCategoryId")
    private int subcategoryid;
    @SerializedName("Categoryid")
    private int categoryid;
    @SerializedName("SubcategoryName")
    private String subcategoryname;
    @SerializedName("LogoUrl")
    private String logourl;
    @SerializedName("itemcount")
    private int itemcount;
    @SerializedName("StoreBanner")
    public String storeBanner;

    public SubCategoriesModel(boolean isChecked, int subcategoryid, int categoryid, String subcategoryname, String logourl, int itemcount) {
        this.isChecked = isChecked;
        this.subcategoryid = subcategoryid;
        this.categoryid = categoryid;
        this.subcategoryname = subcategoryname;
        this.logourl = logourl;
        this.itemcount = itemcount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getSubcategoryid() {
        return subcategoryid;
    }

    public void setSubcategoryid(int subcategoryid) {
        this.subcategoryid = subcategoryid;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }
}
