package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category

import com.google.gson.annotations.SerializedName

class CategoriesResponse {
    @SerializedName("BaseCategoryId")
    var baseCategoryId = 0

    @SerializedName("Categoryid")
    var categoryid = 0

    @SerializedName("CategoryName")
    var categoryName: String? = null

    @SerializedName("LogoUrl")
    var logoUrl: String? = null
}