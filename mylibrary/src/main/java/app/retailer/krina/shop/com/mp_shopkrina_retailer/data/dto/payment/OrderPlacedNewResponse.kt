package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName

class OrderPlacedNewResponse {
    @SerializedName("IsSuccess")
    var isSuccess = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("NotServicing")
    var isNotServicing = false

    @SerializedName("OrderMaster")
    var orderMaster: OrderMaster? = null

    @SerializedName("cart")
    var cart: Cart? = null

    @SerializedName("KisanDaanAmount")
    var kisanDaanAmount = 0.0

    @SerializedName("EarnWalletPoint")
    var earnWalletPoint = 0
}
