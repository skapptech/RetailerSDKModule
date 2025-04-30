package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store

import com.google.gson.annotations.SerializedName

class StoreItemModel {
    @SerializedName("Logo")
    var logo: String? = null

    @SerializedName("SubCategoryName")
    var subCategoryName: String? = null

    @SerializedName("SubCategoryId")
    var subCategoryId = 0
}
