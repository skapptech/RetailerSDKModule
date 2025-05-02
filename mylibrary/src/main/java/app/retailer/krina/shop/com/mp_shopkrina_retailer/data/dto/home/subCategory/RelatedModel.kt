package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory

import com.google.gson.annotations.SerializedName

class RelatedModel {
    @SerializedName("Status")
    var isStatus = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("relatedItemSearch")
    var relatedItemSearch: ArrayList<RelatedItemsModel>? = null
}
