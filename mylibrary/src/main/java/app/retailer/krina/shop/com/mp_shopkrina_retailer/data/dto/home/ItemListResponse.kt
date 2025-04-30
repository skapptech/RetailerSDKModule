package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

import com.google.gson.annotations.SerializedName

class ItemListResponse {
    @SerializedName("Status")
    val isStatus = false

    @SerializedName("TotalItem")
    var totalItem: String? = null

    @SerializedName("ItemMasters")
    var itemMasters: ArrayList<ItemListModel>? = null

    @SerializedName("SubsubCategories")
    var subsubCategories: ArrayList<subsubcatModel>? = null

    inner class subsubcatModel {
        @SerializedName("SubsubCategoryid")
        var SubsubCategoryid: String? = null

        @SerializedName("SubsubcategoryName")
        var SubsubcategoryName: String? = null

        @SerializedName("Categoryid")
        var Categoryid: String? = null

        @SerializedName("SubCategoryId")
        var SubCategoryId: String? = null

        @SerializedName("ItemMasters")
        var ItemMasters: String? = null
    }
}