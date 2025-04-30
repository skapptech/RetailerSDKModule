package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReturnItemModel : Serializable {
    @SerializedName("ItemName")
    var itemname: String? = null

    @SerializedName("CustomerId")
    var customerId = 0

    @SerializedName("ItemMultiMRPId")
    var itemMultiMRPId = 0

}