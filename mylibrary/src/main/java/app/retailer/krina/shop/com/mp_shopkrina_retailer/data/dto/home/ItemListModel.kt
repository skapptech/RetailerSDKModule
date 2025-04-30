package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "ShoppingCart")
class ItemListModel : Serializable, Comparable<ItemListModel> {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ItemId")
    var itemId = 0

    @SerializedName(value = "active", alternate = ["Active"])
    var active = false

    @JvmField
    @SerializedName("isChecked")
    var isChecked = false

    @JvmField
    @SerializedName("IsItemLimit")
    var isItemLimit = false

    @SerializedName("ItemlimitQty")
    var itemLimitQty = 0

    @SerializedName("BillLimitQty")
    var billLimitQty = 0

    @SerializedName("ItemNumber")
    var itemNumber: String? = null

    @SerializedName("CompanyId")
    var companyId = 0

    @SerializedName("WarehouseId")
    var warehouseId = 0

    @JvmField
    @SerializedName("itemname")
    var itemname: String? = null

    @JvmField
    @SerializedName("price")
    var price = 0.0

    @SerializedName("UnitPrice")
    var unitPrice = 0.0

    @SerializedName("LogoUrl")
    var logoUrl: String? = null

    @SerializedName("MinOrderQty")
    var minOrderQty = 0

    @SerializedName("TotalTaxPercentage")
    var totalTaxPercentage: String? = null

    @JvmField
    @SerializedName("marginPoint")
    var marginPoint: String? = null

    @JvmField
    @SerializedName("dreamPoint")
    var dreamPoint = 0

    @JvmField
    @SerializedName("IsOffer")
    var isOffer = false

    @JvmField
    @SerializedName("IsFlashDealUsed")
    var isFlashDealUsed = false

    @SerializedName("ItemMultiMRPId")
    var itemMultiMRPId = 0

    @SerializedName("OfferCategory")
    var offerCategory = 0

    @SerializedName("OfferStartTime")
    var offerStartTime: String? = null

    @SerializedName("NoPrimeOfferStartTime")
    var noPrimeOfferStartTime: String? = null

    @SerializedName("CurrentStartTime")
    var currentStartTime: String? = null

    @JvmField
    @SerializedName("IsFlashDealStart")
    var isFlashDealStart = false

    @SerializedName("OfferEndTime")
    var offerEndTime: String? = null

    @SerializedName("OfferQtyAvaiable")
    var offerQtyAvaiable = 0

    @SerializedName("OfferQtyConsumed")
    var offerQtyConsumed: String? = null

    @SerializedName("OfferId")
    var offerId = 0

    @SerializedName("OfferType")
    var offerType: String? = null

    @SerializedName("OfferWalletPoint")
    var offerWalletPoint: Double? = null

    @SerializedName("OfferFreeItemId")
    var freeItemId = 0

    @SerializedName("OfferFreeItemName")
    var offerFreeItemName: String? = null

    @SerializedName("OfferFreeItemImage")
    var offerFreeItemImage: String? = null

    @SerializedName("OfferFreeItemQuantity")
    var offerFreeItemQuantity: String? = null

    @SerializedName("OfferMinimumQty")
    var offerMinimumQty = 0

    @SerializedName("FlashDealSpecialPrice")
    var flashDealSpecialPrice = 0.0

    @SerializedName("FlashDealMaxQtyPersonCanTake")
    var flashDealMaxQtyPersonCanTake = 0

    @SerializedName("Categoryid")
    var categoryid = 0

    @SerializedName("BaseCategoryId")
    var baseCategoryId: String? = null

    @SerializedName("SubCategoryId")
    var subCategoryId = 0

    @SerializedName("SubsubCategoryid")
    var subsubCategoryid = 0

    @SerializedName("TotalFreeWalletPoint")
    var totalFreeWalletPoint = 0.0

    @SerializedName("TotalFreeItemQty")
    var totalFreeItemQty = 0

    @SerializedName("CartUnitPrice")
    var cartUnitPrice: String? = null

    @JvmField
    @SerializedName("NewUnitPrice")
    var newUnitPrice: String? = null

    @JvmField
    @SerializedName("IsSuccess")
    var isSuccess = false

    @JvmField
    @SerializedName("qty")
    var qty = 0

    @SerializedName("Message")
    var message: String? = null

    @JvmField
    @SerializedName("IsPrimeItem")
    var isPrimeItem = false

    @SerializedName("PrimePrice")
    var primePrice = 0.0

    @SerializedName("StoreName")
    var storeName: String? = null

    @SerializedName("StoreLogo")
    var storeLogo: String? = null

    @SerializedName("StoreId")
    var storeId = 0

    @SerializedName("Scheme")
    var scheme: String? = null

    @JvmField
    @SerializedName("isSectionHeader")
    var isSectionHeader = false

    @SerializedName("NotifyDesable")
    var isNotifyDesable = false

    @JvmField
    @SerializedName("IsDealItem")
    var isDealItem = false

    @JvmField
    @SerializedName("MessageKey")
    var messageKey: String? = null

    @JvmField
    @SerializedName("totalQuantity")
    var totalQuantity = 0

    @JvmField
    @Ignore
    var moqList = ArrayList<ItemListModel>()

    constructor()

    constructor(storeName: String?) {
        this.storeName = storeName
    }

    fun setToSectionHeader() {
        isSectionHeader = true
    }

    override fun toString(): String {
        return itemNumber!!
    }

    override operator fun compareTo(o: ItemListModel): Int {
        return o.storeName!!.compareTo(storeName!!)
    }
}