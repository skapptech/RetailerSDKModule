package app.retailer.krina.shop.com.mp_shopkrina_retailer.models

import com.google.gson.annotations.SerializedName

data class PaylaterLimitsResponseModel(
    @SerializedName("Message") var Message: String? = null,
    @SerializedName("Status") var Status: Boolean = false,
    @SerializedName("payLaterCollectionLimitDCs") var payLaterCollectionLimitDCs: ArrayList<PaylaterLimitsResponseListModel> = arrayListOf()
)


