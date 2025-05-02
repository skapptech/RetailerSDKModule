package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SalesReturnRequestListModel : Serializable {
    @SerializedName("RequestId")
    var requestId = 0

    @SerializedName("OrderId")
    var orderId = 0

    @SerializedName("OrderValue")
    var orderValue = 0.0

    @SerializedName("OrderDate")
    var orderDate :String?=null

    @SerializedName("Status")
    var status :String?=null
}