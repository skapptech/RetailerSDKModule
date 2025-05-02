package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ShopingCartItemDetailsResponse : Serializable {
    @SerializedName("DeliveryCharges")
    var deliveryCharges = 0.0

    @SerializedName("ConvenienceFees")
    var convenienceFees = 0.0

    @SerializedName("IsConvenienceFees")
    var isConvenienceFee = false

    @SerializedName("PlatformFees")
    var platformFees = 0.0

    @SerializedName("PlatformFeesFinalAmt")
    var platformFeeFinalAmt = 0.0

    @SerializedName("IsPlatformFees")
    var isPlatformFee = false

    @SerializedName("Salespersonvisitcharges")
    var salesPersonVisitCharges = 0.0

    @SerializedName("Isvisitcharges")
    var isVisitCharges = false

    @SerializedName("HikeCharges")
    var hikeCharges = 0.0

    @SerializedName("IsHikeCharges")
    var isHikeCharge = false

    @SerializedName("OrderProcessCharge")
    var orderProcessCharge = 0.0

    @SerializedName("OrderProcessChargeFinalAmt")
    var orderProcessAmt = 0.0

    @SerializedName("IsOrderProcessCharge")
    var isOrderProcessCharge = false

    @SerializedName("SalespersonvisitChargeFinalAmt")
    var salesPersonVisitAmt = 0.0

    @SerializedName("SmallCartCharge")
    var smallCartCharge = 0.0

    @SerializedName("SmallCartChargeFinalAmt")
    var smallCartAmt = 0.0

    @SerializedName("IsSmallCartCharge")
    var isSmallCartCharge = false

    @SerializedName("CartTotalAmt")
    var cartTotalAmt = 0.0

    @SerializedName("GrossTotalAmt")
    var grossTotalAmt = 0.0

    @SerializedName("TotalTaxAmount")
    var totalTaxAmount = 0.0

    @SerializedName("TotalDiscountAmt")
    var totalDiscountAmt = 0.0

    @SerializedName("WalletAmount")
    var walletAmount = 0.0

    @SerializedName("DeamPoint")
    var deamPoint = 0

    @SerializedName("NewBillingWalletPoint")
    val newBillingWalletPoint = 0

    @SerializedName("GeneratedOrderId")
    var generatedOrderId: String? = null

    @SerializedName("ApplyOfferId")
    var applyOfferId: String? = null

    @SerializedName("StoreName")
    private val StoreName: String? = null

    @SerializedName("StoreId")
    private val StoreId = 0

    @SerializedName("TotalQty")
    var totalQty = 0

    @SerializedName("WheelCount")
    var wheelCount = 0

    @SerializedName("TotalSavingAmt")
    var totalSavingAmt = 0.0

    @SerializedName("TCSPercent")
    val tcsPercent = 0.0

    @SerializedName("PreTotalDispatched")
    var preTotalDispatched = 0.0

    @SerializedName("TCSLimit")
    var tCSLimit = 0.0

    @SerializedName("ShoppingCartItemDcs")
    val shoppingCartItemDcs: ArrayList<ItemListModel>? = null

    @SerializedName("DiscountDetails")
    var discountDetails: List<DiscountDetails>? = null

    class DiscountDetails : Serializable {
        @SerializedName("OfferId")
        val offerId = 0

        @SerializedName("DiscountAmount")
        val discountAmount = 0.0
    }
}