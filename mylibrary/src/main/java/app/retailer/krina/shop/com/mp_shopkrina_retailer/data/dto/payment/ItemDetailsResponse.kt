package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ItemDetailsResponse : Serializable {
    @SerializedName("ItemId")
    var itemId = 0

    @SerializedName("UnitPrice")
    var unitPrice: String? = null

    @SerializedName("NewUnitPrice")
    var newUnitPrice = 0.0

    @SerializedName("IsSuccess")
    var isSuccess = false

    @SerializedName("Message")
    var message: String? = null
}
