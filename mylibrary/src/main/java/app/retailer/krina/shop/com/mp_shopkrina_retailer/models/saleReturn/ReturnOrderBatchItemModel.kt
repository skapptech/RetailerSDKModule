package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReturnOrderBatchItemModel : Serializable {
    @SerializedName("OrderId")
    var orderId = 0

    @SerializedName("Qty")
    var qty = 0

    @SerializedName("UnitPrice")
    var unitPrice = 0.0

    @SerializedName("BatchCode")
    var batchCode = ""

    @SerializedName("BatchMasterId")
    var batchMasterId = 0

    @SerializedName("ItemMultiMRPId")
    var itemMultiMRPId = 0

    @SerializedName("OrderDetailsId")
    var orderDetailsId = 0

    @SerializedName("ReturnableQty")
    var returnableQty = 0

    var returnQty = 0L

    var isChecked  = false

    var mImageList : ArrayList<String> = ArrayList()

}