package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category

import com.google.gson.annotations.SerializedName

class BasCategoryResponse {
    @SerializedName("BaseCategoryId")
    var baseCategoryId = 0

    @SerializedName("BaseCategoryName")
    var baseCategoryName: String? = null

    @SerializedName("LogoUrl")
    var logoUrl: String? = null
}