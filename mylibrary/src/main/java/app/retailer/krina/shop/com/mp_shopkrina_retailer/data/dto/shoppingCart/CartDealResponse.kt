package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import com.google.gson.annotations.SerializedName

class CartDealResponse {
    @SerializedName("ItemDataDCs")
    val itemDataDCs: ArrayList<ItemListModel>? = null

    @SerializedName("TotalItem")
    val totalItem = 0
}
