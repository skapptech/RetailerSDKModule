package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem

import com.google.gson.annotations.SerializedName

class SearchFilterModel {
    @SerializedName("Basecats")
    val baseCatList: ArrayList<BasecategoryModel>? = null

    @SerializedName("Categories")
    val categoriesList: ArrayList<BasecategoryModel>? = null

    @SerializedName("SubCategories")
    val subCategoriesModelList: ArrayList<BasecategoryModel>? = null

    @SerializedName("SubSubCategories")
    val subSubCategoriesModelList: ArrayList<BasecategoryModel>? = null
}