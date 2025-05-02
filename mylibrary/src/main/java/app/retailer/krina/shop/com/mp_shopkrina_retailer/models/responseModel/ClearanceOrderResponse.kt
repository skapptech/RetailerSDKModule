package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceShoppingCart
import com.google.gson.annotations.SerializedName

class ClearanceOrderResponse {
    @SerializedName("ClearanceShoppingCart")
    var shoppingCart: ClearanceShoppingCart? = null

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("IsSuccess")
    var isSuccess = false

    @SerializedName("GrossAmount")
    var grossAmount = 0.0

    @SerializedName("CreatedDate")
    var createdDate: String? = null

    @SerializedName("OrderId")
    var orderId = 0
}