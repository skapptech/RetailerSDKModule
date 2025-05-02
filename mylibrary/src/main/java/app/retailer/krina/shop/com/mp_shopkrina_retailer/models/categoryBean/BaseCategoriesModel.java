package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel;

public  class BaseCategoriesModel implements Serializable {
    @SerializedName("categoryDC")
    ArrayList<CategoriesModel> categoryDC;

    @SerializedName("subCategoryDC")
    ArrayList<SubCategoriesModel> subCategoryDC;

    @SerializedName("subsubCategoryDc")
    ArrayList<SubSubCategoriesModel> subsubCategoryDc;

    public ArrayList<CategoriesModel> getCategoryDC() {
        return categoryDC;
    }

    public void setCategoryDC(ArrayList<CategoriesModel> categoryDC) {
        this.categoryDC = categoryDC;
    }

    public ArrayList<SubCategoriesModel> getSubCategoryDC() {
        return subCategoryDC;
    }

    public void setSubCategoryDC(ArrayList<SubCategoriesModel> subCategoryDC) {
        this.subCategoryDC = subCategoryDC;
    }

    public ArrayList<SubSubCategoriesModel> getSubsubCategoryDc() {
        return subsubCategoryDc;
    }

    public void setSubsubCategoryDc(ArrayList<SubSubCategoriesModel> subsubCategoryDc) {
        this.subsubCategoryDc = subsubCategoryDc;
    }
}
