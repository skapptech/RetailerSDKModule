package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class OTPResponse {
    @SerializedName("Status")
    val isStatus = false

    @SerializedName("Message")
    val message: String? = null

    @SerializedName("OtpNo")
    val otpNo: String? = null
}
