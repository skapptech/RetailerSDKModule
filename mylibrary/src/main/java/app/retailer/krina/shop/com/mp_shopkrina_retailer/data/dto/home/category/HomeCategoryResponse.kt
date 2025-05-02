package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category

import com.google.gson.annotations.SerializedName

class HomeCategoryResponse {
    @SerializedName("Basecats")
    val basecatlist: ArrayList<BasCategoryResponse>? = null

    @SerializedName("Categories")
    val categoriesList: ArrayList<CategoriesResponse>? = null

    @SerializedName("SubCategories")
    val subCategoriesModelList: List<SubCategoriesResponse>? = null
}