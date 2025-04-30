package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import com.google.gson.annotations.SerializedName

class SearchItemHistoryModel {
    @SerializedName("RecentSearchItem")
    val recentSearchItem: ArrayList<ItemListModel>? = null

    @SerializedName("CustFavoriteItem")
    val custFavoriteItem: ArrayList<ItemListModel>? = null

    @SerializedName("RecentPurchase")
    val recentPurchase: ArrayList<ItemListModel>? = null

    @SerializedName("MostSellingProduct")
    val mostSellingProduct: ArrayList<ItemListModel>? = null
}