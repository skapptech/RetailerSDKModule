package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SalesReturnRequestListDetailsModel : Serializable {

    @SerializedName("ItemName")
    var itemName: String? = null

    @SerializedName("Qty")
    var qty = 0

    @SerializedName("Rate")
    var rate = 0.0

    @SerializedName("TotalValue")
    var totalValue = 0.0
}