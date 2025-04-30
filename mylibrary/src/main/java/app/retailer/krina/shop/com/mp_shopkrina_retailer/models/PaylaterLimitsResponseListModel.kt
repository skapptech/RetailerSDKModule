package app.retailer.krina.shop.com.mp_shopkrina_retailer.models

import com.google.gson.annotations.SerializedName

data class PaylaterLimitsResponseListModel(
    @SerializedName("StoreName") var StoreName: String? = null,
    @SerializedName("CreditLimit") var CreditLimit: Double? = null,
    @SerializedName("AvailableRemainingAmount") var AvailableRemainingAmount: Double? = null
)
