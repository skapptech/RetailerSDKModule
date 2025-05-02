package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.SerializedName

class PaymentRequestModel {
    @SerializedName("customerId")
    var customerId = 0

    @SerializedName("amount")
    var amount = 0.0

    @SerializedName("comment")
    var comment: String? = ""

    @SerializedName("GatewayRequest")
    var GatewayRequest: String? = ""

    @SerializedName("PaymentFrom")
    var PaymentFrom: String? = ""
    var CustomerType: String? = ""
    var AppType: String? = ""

    constructor()
    constructor(
        customerId: Int,
        amount: Double,
        comment: String?,
        gatewayRequest: String?,
        paymentFrom: String?,
        CustomerType: String?,
        AppType: String?
    ) {
        this.customerId = customerId
        this.amount = amount
        this.comment = comment
        GatewayRequest = gatewayRequest
        PaymentFrom = paymentFrom
        this.CustomerType = CustomerType
        this.AppType = AppType
    }
}