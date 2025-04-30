package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class UserAuth {
    @SerializedName("Password")
    var password: String? = null

    @SerializedName("UserName")
    var userName: String? = null
}
