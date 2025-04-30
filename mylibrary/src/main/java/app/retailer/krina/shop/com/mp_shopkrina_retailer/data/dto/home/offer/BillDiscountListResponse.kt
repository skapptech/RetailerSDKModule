package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName

class BillDiscountListResponse {
    @JvmField
    @SerializedName("offer")
    val billDiscountList: ArrayList<BillDiscountModel>? = null

    @SerializedName("Status")
    val isStatus = false

    @SerializedName("Message")
    val message: String? = null
}