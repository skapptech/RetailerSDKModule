package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.SerializedName

class ClearanceOfferResponse {
    @SerializedName("Message")
    var message = ""

    @SerializedName("IsSuccess")
    var isSuccess = false

    @SerializedName("BillDiscount")
    var billDiscount = 0.0

    @SerializedName("BillDiscountOffers")
    var offer: MutableList<BillDiscountOffers>? = null

    @SerializedName("cart")
    var itemDetails: List<ClearanceIDetailDc>? = null

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

        @SerializedName("IsSuccess")
        var isSuccess = false

        @SerializedName("Message")
        var message = ""
    }

    class BillDiscountOffers {
        var OfferId = 0
        var DiscountAmount = 0.0
        var NewBillingWalletPoint = 0.0
    }
}