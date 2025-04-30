package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName

class ScaleUpResponse {
    @SerializedName("status")
    val status = false

    @SerializedName("message")
    val message: String? = null

    @SerializedName("TransactionId")
    val transactionId = ""

    @SerializedName("WebViewUrl")
    val url = ""

    @SerializedName("BaiseUrl")
    val BaiseUrl = ""
}
