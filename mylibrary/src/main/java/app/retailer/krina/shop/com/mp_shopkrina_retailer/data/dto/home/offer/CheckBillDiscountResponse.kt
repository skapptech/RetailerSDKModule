package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName

class CheckBillDiscountResponse {
    @SerializedName("BillDiscount")
    val billDiscount: List<BillDiscount>? = null

    @SerializedName("Status")
    val isStatus = false

    class BillDiscount {
        @SerializedName("OfferId")
        val offerId = 0

        @SerializedName("Valid")
        val isValid = false

        @SerializedName("Message")
        val message: String? = null
    }
}