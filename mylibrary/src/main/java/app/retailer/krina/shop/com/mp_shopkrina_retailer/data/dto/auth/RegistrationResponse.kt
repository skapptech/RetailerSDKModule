package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class RegistrationResponse {
    @SerializedName("Active")
    var isActive = false

    @SerializedName("Mobile")
    var mobile: String? = null

    @SerializedName("Skcode")
    var skcode: String? = null

    @SerializedName("CompanyId")
    var companyid = 0

    @SerializedName("CustomerId")
    var customerid = 0

    @SerializedName("IsSignup")
    var isSignup = false

    @SerializedName("RegisteredApk")
    var registeredApk: UserAuth? = null
}
