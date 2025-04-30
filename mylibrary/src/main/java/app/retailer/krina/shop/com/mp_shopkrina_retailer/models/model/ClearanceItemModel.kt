package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class ClearanceItemModel : java.io.Serializable {
    @SerializedName("totalcount")
    val totalCount = 0

    @SerializedName("CategoryId")
    val categoryId = 0

    @SerializedName("MRP")
    val MRP = 0.0

    @SerializedName("ImageUrl")
    val imageUrl: String? = null

    @SerializedName("ShelfLife")
    val shelfLife = 0

    @SerializedName("MOQ")
    val moq = 0

    @SerializedName("ItemName")
    val ItemName: String? = null

    @SerializedName("UnitPrice")
    val unitPrice = 0.0

    @SerializedName("RemainingStockQty")
    val remainingStockQty = 0

    @SerializedName("LiveStockQty")
    val liveStockQty = 0

    @SerializedName("Id")
    val id = 0

    var qty = 0
}