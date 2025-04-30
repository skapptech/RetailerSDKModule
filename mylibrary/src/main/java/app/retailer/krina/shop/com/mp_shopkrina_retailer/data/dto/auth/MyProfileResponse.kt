package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class MyProfileResponse {
    @SerializedName("Emailid")
    private val Emailid: String? = null

    @SerializedName("ShippingAddress")
    private val ShippingAddress: String? = null

    @SerializedName("Status")
    var isStatus = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("IsUdharOverDue")
    var isUdharOverDue: Boolean? = null

    @SerializedName("CriticalInfoMissingMsg")
    var criticalInfoMissingMsg: String? = null

    @SerializedName("LogOutFromThisDevice")
    var isLogOutFromThisDevice = false

    @SerializedName("customers")
    var customers: CustomerResponse? = null
}