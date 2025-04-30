package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category

import com.google.gson.annotations.SerializedName

class SubCategoriesResponse {
    @SerializedName("SubCategoryId")
    var subCategoryId = 0

    @SerializedName("Categoryid")
    var categoryid: String? = null

    @SerializedName("SubcategoryName")
    var subcategoryName: String? = null
}
