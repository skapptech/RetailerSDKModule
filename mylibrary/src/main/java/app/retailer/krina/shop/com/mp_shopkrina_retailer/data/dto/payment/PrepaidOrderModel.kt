package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PrepaidOrderModel : Serializable {
    @SerializedName("OrderAmount")
    val orderAmount = 0.0

    @SerializedName("OnlineParcentage")
    val onlinePercentage = 0.0

    @SerializedName("CashParcentage")
    val cashPercentage = 0.0
}