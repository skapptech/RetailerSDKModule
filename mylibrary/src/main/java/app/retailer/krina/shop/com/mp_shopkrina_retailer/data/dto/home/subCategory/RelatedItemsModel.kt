package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory

import com.google.gson.annotations.SerializedName

class RelatedItemsModel {
    @SerializedName("itemname")
    var itemname: String? = null

    @SerializedName("LogoUrl")
    var logoUrl: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("SubCategoryId")
    var subCategoryId: String? = null

    @SerializedName("SubsubCategoryid")
    var subsubCategoryid: String? = null

    @SerializedName("Categoryid")
    var categoryid: String? = null

    @SerializedName("BaseCategoryId")
    var baseCategoryId: String? = null

    @SerializedName("marginPoint")
    var marginPoint: String? = null
}
