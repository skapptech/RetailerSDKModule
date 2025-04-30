package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.SerializedName

class PostUPIPaymentResponse(
    @field:SerializedName("id") var id: String,
    @field:SerializedName("CustomerId") var customerId: Int,
    @field:SerializedName("GatewayTransId") var gatewayTransId: String?,
    @field:SerializedName("GatewayRequest") var gatewayRequest: String?,
    @field:SerializedName("GatewayResponse") var gatewayResponse: String?,
    @field:SerializedName("PaymentFrom") var paymentFrom: String?,
    @field:SerializedName("status") var status: String?
)