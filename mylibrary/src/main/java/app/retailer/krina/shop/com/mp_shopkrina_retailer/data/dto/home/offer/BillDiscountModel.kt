package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BillDiscountModel : Serializable {
    @SerializedName("OfferId")
    var offerId: Int = 0

    @SerializedName("OfferName")
    val offerName: String? = null

    @SerializedName("OfferCode")
    val offerCode: String? = null

    @SerializedName("OfferOn")
    val offerOn: String? = null

    @SerializedName("end")
    val end: String? = null

    @SerializedName("DiscountPercentage")
    val discountPercentage: Double = 0.0

    @SerializedName("BillAmount")
    var billAmount: Double = 0.0

    @SerializedName("LineItem")
    var lineItem: Int = 0

    @SerializedName("Description")
    val description: String? = null

    @SerializedName("BillDiscountOfferOn")
    val billDiscountOfferOn: String? = null

    @SerializedName("BillDiscountWallet")
    var billDiscountWallet: Double = 0.0

    @SerializedName("IsScratchBDCode")
    var isScratchBDCode: Boolean = false

    @SerializedName("BillDiscountType")
    var billDiscountType: String? = null

    @SerializedName("ApplyOn")
    var applyOn: String? = null

    @SerializedName("ApplyType")
    var applyType: String? = null

    @SerializedName("WalletType")
    var walletType: String? = null

    @SerializedName("IsUseOtherOffer")
    var isUseOtherOffer: Boolean = false

    @SerializedName("IsBillDiscountFreebiesItem")
    var isBillDiscountFreebiesItem: Boolean = false

    @SerializedName("offerminorderquantity")
    var offerMinOrderQty = 0

    @SerializedName("IsBillDiscountFreebiesValue")
    var isBillDiscountFreebiesValue: Boolean = false

    @SerializedName("offeritemname")
    var offeritemname: String? = null

    @SerializedName("OfferBillDiscountItems")
    val offerBillDiscountItems: ArrayList<OfferDiscountItems>? = null

    @SerializedName("OfferItems")
    val offerItemsList: ArrayList<OfferItems>? = null

    @SerializedName("RetailerBillDiscountFreeItemDcs")
    var retailerBillDiscountFreeItemDcs: ArrayList<RetailerBillDiscountFreeItemDcs>? = null

    @SerializedName("BillDiscountRequiredItems")
    var requiredItemsList: List<BillDiscountRequiredItems>? = null

    @SerializedName("OfferLineItemValueDcs")
    var offerLineItemValueDcs: List<OfferLineItemValue>? = null

    @SerializedName("ImagePath")
    var ImagePath: String? = null

    @SerializedName("ColorCode")
    var colorCode: String? = null

    // local usecase
    @JvmField
    var isSelected = false

    @JvmField
    var isApplicable = false
    var message = ""
}