package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class SignupResponse {
    @SerializedName("Status")
    var isStatus = false

    @SerializedName("Message")
    var message: String? = null

    @JvmField
    @SerializedName("customers")
    var customers: CustomerResponse? = null

    @SerializedName("CriticalInfoMissingMsg")
    var criticalInfoMissingMsg: String? = null
}
