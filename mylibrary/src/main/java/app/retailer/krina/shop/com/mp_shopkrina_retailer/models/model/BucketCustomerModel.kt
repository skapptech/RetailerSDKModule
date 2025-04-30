package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class BucketCustomerModel {
    @SerializedName("CustomerName")
    var customerName: String? = null

    @SerializedName("ShopName")
    var shopName: String? = null

    @SerializedName("Level")
    var level = 0

    @SerializedName("Reward")
    var reward = 0

    @SerializedName("Image")
    var image: String? = null
}