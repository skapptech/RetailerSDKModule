package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import com.google.gson.annotations.SerializedName

class ImageResponse {
    @SerializedName("status")
    var status = false

    @SerializedName("Name")
    var name: String? = null
}
