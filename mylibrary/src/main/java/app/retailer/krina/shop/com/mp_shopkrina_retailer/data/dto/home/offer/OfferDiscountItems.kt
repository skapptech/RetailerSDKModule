package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OfferDiscountItems : Serializable {
    @SerializedName("Id")
    var id: Int = 0

    @JvmField
    @SerializedName("CategoryId")
    var categoryId: Int = 0

    @JvmField
    @SerializedName("SubCategoryId")
    var subCategoryId: Int = 0

    @SerializedName("IsInclude")
    var isInclude: Boolean = false
}