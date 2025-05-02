package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

import com.google.gson.annotations.SerializedName

class BottomCall {
    @SerializedName("Id")
    var id = 0

    @SerializedName("Type")
    var type = 0

    @SerializedName("RelativeUrl")
    var relativeUrl: String? = null
}
