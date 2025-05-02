package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class GetLokedCusResponse {
    @SerializedName("Status")
    var status: String? = null

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("customers")
    var customers: CustomerResponse? = null
}
