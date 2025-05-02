package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn
import com.google.gson.annotations.SerializedName
class SaleReturnRequestResponseModel {
    @SerializedName("Status")
    var status: Boolean? = null

    @SerializedName("Message")
    var msg: String? = null
}
