package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.SerializedName

class ClearanceShoppingCart {
    @SerializedName("CustomerId")
    var customerId = 0

    @SerializedName("TotalAmount")
    var totalAmount = 0.0

    @SerializedName("GullakAmount")
    var gullakAmount = 0.0

    @SerializedName("CreatedDate")
    var createdDate: String? = null

    @SerializedName("OrderId")
    var orderId = 0

    @SerializedName("Lat")
    var lat = 0.0

    @SerializedName("Lng")
    var lng = 0.0

    @SerializedName("BillDiscountOfferId")
    var offer: MutableList<Int> = ArrayList()

    @SerializedName("itemDetails")
    var itemDetails: List<ClearanceIDetailDc>? = null

    @SerializedName("MOP")
    var mop: String? = null

    class ClearanceIDetailDc {
        @SerializedName("Id")
        var id = 0

        @SerializedName("qty")
        var qty = 0

        @SerializedName("UnitPrice")
        var unitPrice = 0.0

        @SerializedName("NewUnitPrice")
        var newUnitPrice = 0.0

        @SerializedName("NewRemainingStockQty")
        var newRemainingStockQty = 0
    }
}