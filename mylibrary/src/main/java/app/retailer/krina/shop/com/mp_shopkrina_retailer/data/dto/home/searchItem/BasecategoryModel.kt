package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem

import com.google.gson.annotations.SerializedName

class BasecategoryModel {
    @SerializedName("BaseCategoryId")
    var baseCategoryId = 0

    @SerializedName("BaseCategoryName")
    var baseCategoryName: String? = null

    @SerializedName("Categoryid")
    var categoryid = 0

    @SerializedName("SubCategoryId")
    var subCategoryId = 0

    @SerializedName("CategoryName")
    var categoryName: String? = null

    @SerializedName("SubcategoryName")
    var subcategoryName: String? = null

    @SerializedName("SubSubCategoryId")
    var subSubCategoryId = 0

    @SerializedName("SubSubcategoryName")
    var subSubcategoryName: String? = null
    var isChecked = false
}