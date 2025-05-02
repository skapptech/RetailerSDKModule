package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel;

public class SubCategoryModel {
    @SerializedName("categoryDC")
    private List<CategoryDetailsModel> categories;

    @SerializedName("subCategoryDC")
    ArrayList<SubCategoriesModel> subCategoryDC;

    @SerializedName("subsubCategoryDc")
    ArrayList<SubSubCategoriesModel> subsubCategoryDc;


    public List<CategoryDetailsModel> getCategories() {
        return categories;
    }

    public ArrayList<SubCategoriesModel> getSubCategoryDC() {
        return subCategoryDC;
    }

    public ArrayList<SubSubCategoriesModel> getSubsubCategoryDc() {
        return subsubCategoryDc;
    }
}
