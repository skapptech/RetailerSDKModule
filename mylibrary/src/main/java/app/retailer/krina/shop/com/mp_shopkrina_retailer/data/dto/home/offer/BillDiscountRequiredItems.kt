package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BillDiscountRequiredItems : Serializable {
    var offerId = 0

    @JvmField
    @SerializedName("ObjectType")
    var objectType: String? = null

    @JvmField
    @SerializedName("ObjectId")
    var objectId: String? = null

    @SerializedName("ObjectText")
    var objectText: String? = null

    @JvmField
    @SerializedName("ValueType")
    var valueType: String? = null

    @JvmField
    @SerializedName("ObjectValue")
    var objectValue = 0.0

    @SerializedName("SubCategoryId")
    var subCategoryId = 0

    @SerializedName("CategoryId")
    var categoryId = 0
}