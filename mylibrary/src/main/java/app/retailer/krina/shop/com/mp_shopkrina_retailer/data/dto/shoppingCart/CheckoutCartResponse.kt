package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart

import com.google.gson.annotations.SerializedName

class CheckoutCartResponse {
    @SerializedName("Cart")
    var shoppingCartItemDetailsResponse: ShopingCartItemDetailsResponse? = null

    @SerializedName("Status")
    val status = false

    @SerializedName("Message")
    val message: String? = null
}