package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.freebies

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import com.google.gson.annotations.SerializedName

class OfferDataModel {
    @SerializedName("Status")
    var isStatus = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("ItemMasters")
    var itemListModels: ArrayList<ItemListModel>? = null
}