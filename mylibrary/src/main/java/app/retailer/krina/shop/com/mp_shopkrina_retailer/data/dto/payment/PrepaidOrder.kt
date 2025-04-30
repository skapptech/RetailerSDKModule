package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PrepaidOrder : Serializable {
    @SerializedName("Status")
    var isStatus = false

    @SerializedName("message")
    var message: String? = null

    @SerializedName("CustomizedPrepaidOrders")
    val prepaidOrderModel: PrepaidOrderModel? = null
}
