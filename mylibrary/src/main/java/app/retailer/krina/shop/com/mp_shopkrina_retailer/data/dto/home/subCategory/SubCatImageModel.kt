package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory

import com.google.gson.annotations.SerializedName

class SubCatImageModel {
    @JvmField
    @SerializedName("CategoryImg")
    var categoryimg: String? = null

    @JvmField
    @SerializedName("CategoryImageId")
    var categoryimageid: Int = 0
}
