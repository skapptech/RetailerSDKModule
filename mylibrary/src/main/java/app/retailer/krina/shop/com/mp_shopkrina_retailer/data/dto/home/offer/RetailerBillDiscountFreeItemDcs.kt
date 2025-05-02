package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RetailerBillDiscountFreeItemDcs : Serializable {
    @SerializedName("ItemId")
    var itemId = 0

    @SerializedName("ItemName")
    var itemName: String? = null

    @SerializedName("Qty")
    var qty = 0
}