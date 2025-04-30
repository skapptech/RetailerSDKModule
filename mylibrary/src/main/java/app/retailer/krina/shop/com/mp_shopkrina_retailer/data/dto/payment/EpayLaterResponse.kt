package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName

class EpayLaterResponse {
    @SerializedName("id")
    var id = 0

    @SerializedName("amount")
    var amount = 0.0

    @SerializedName("currencyCode")
    var currencyCode: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("paylater")
    var isPaylater = false

    @SerializedName("status")
    var status: String? = null

    @SerializedName("marketplaceOrderId")
    var marketplaceOrderId: String? = null
}