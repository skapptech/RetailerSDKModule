package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.OfferLineItemValue
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse
import java.util.Collections

class OfferCheck(
    private val discountModel: BillDiscountModel,
    private val cartResponse: ShopingCartItemDetailsResponse
) {
    private var cartTotal = 0.0
    private var totalamount = 0.0
    private var orderLineItems = 0

    @JvmField
    var message = ""

    init {
        cartTotal = cartResponse.grossTotalAmt
    }

    fun checkCoupon(): Boolean {
        var isApplicable = false
        try {
            if (cartTotal >= 1) {
                if (TextUtils.isNullOrEmpty(
                        discountModel.billDiscountType
                    )
                ) {
                    discountModel.billDiscountType = "BillDiscount"
                }
                if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                    Collections.sort(cartResponse.shoppingCartItemDcs, AmtAscComparator())
                    Collections.sort(discountModel.offerLineItemValueDcs, ValAscComparator())
                }
                if (discountModel.billDiscountType.equals("category", ignoreCase = true)) {
                    for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.categoryid == offerItems.id && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems)) {
                        isApplicable = true
                    }
                } else if (discountModel.billDiscountType.equals(
                        "subcategory",
                        ignoreCase = true
                    )
                ) {
                    for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subCategoryId == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.qty > 0) {
                                totalamount = totalamount + cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                                break
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount) {
                        if (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems) {
                            isApplicable = true
                        }
                    }
                } else if (discountModel.billDiscountType.equals(
                        "subsubcategory",
                        ignoreCase = true
                    )
                ) {
                    for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subsubCategoryid == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.subCategoryId == offerItems.subCategoryId && cartItem.qty > 0) {
                                totalamount = totalamount + cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems)) {
                        isApplicable = true
                    }
                } else if (discountModel.billDiscountType.equals("brand", ignoreCase = true)) {
                    for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subsubCategoryid == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.subCategoryId == offerItems.subCategoryId && cartItem.qty > 0) {
                                totalamount = totalamount + cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems)) {
                        isApplicable = true
                    }
                } else if (discountModel.billDiscountType.equals("items", ignoreCase = true)) {
                    for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                        for (offerItems in discountModel.offerItemsList!!) {
                            if (cartItem.itemId == offerItems.itemId && offerItems.isInclude && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && discountModel.lineItem > 0 && discountModel.lineItem > orderLineItems) {
                        isApplicable = true
                    }
                } else if (discountModel.billDiscountType.equals(
                        "BillDiscount",
                        ignoreCase = true
                    )
                ) {
                    totalamount = cartTotal
                    val isRequiredItemExists = true
                    if (discountModel.offerBillDiscountItems!!.size > 0 || discountModel.offerItemsList!!.size > 0) {
                        for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                            for (offerItems in discountModel.offerBillDiscountItems) {
                                if (cartItem.categoryid == offerItems.id && cartItem.qty > 0 && !offerItems.isInclude) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                }
                                orderLineItems++
                            }
                        }
                        if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                            for (cartItem in cartResponse.shoppingCartItemDcs) {
                                for (offerItems in discountModel.offerItemsList) {
                                    if (cartItem.itemId == offerItems.itemId && cartItem.qty > 0 && !offerItems.isInclude) {
                                        totalamount -= cartItem.unitPrice * cartItem.qty
                                        orderLineItems++
                                    }
                                }
                            }
                        }
                    } else {
                        for (info in cartResponse.shoppingCartItemDcs!!) {
                            if (info.qty > 0) {
                                orderLineItems++
                            }
                        }
                    }

                    // cart item
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartResponse.shoppingCartItemDcs) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && isRequiredItemExists) {
                        if (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems) {
                            isApplicable = true
                        }
                    }
                }
                if (discountModel.requiredItemsList != null && discountModel.requiredItemsList!!.size > 0) {
                    var isRequiredItemExists = true
                    for (reqitem in discountModel.requiredItemsList!!) {
                        var totalT = 0.0
                        if (reqitem.objectType.equals("Item", ignoreCase = true)) {
                            if (reqitem.valueType.equals("Qty", ignoreCase = true)) {
                                for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                                    if (reqitem.objectId!!.contains(cartItem.itemMultiMRPId.toString()) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.qty
                                    }
                                }
                            } else if (reqitem.valueType.equals("Value", ignoreCase = true)) {
                                for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                                    if (reqitem.objectId!!.contains(cartItem.itemMultiMRPId.toString()) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.unitPrice * cartItem.qty
                                    }
                                }
                            }
                            if (reqitem.objectValue > totalT) {
                                isRequiredItemExists = false
                            }
                        } else if (reqitem.objectType.equals("brand", ignoreCase = true)) {
                            if (reqitem.valueType.equals("Qty", ignoreCase = true)) {
                                val ids = reqitem.objectId!!.split(",".toRegex()).toTypedArray()
                                for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                                    if (ids.contains("" + cartItem.subsubCategoryid + " " + cartItem.subCategoryId + " " + cartItem.categoryid) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.qty
                                    }
                                }
                            } else if (reqitem.valueType.equals("Value", ignoreCase = true)) {
                                val ids = reqitem.objectId!!.replace(",", " ").split(" ".toRegex())
                                    .toTypedArray()
                                for (cartItem in cartResponse.shoppingCartItemDcs!!) {
                                    if (ids.contains(cartItem.subsubCategoryid.toString())
                                        && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")
                                    ) {
                                        totalT += cartItem.unitPrice * cartItem.qty
                                    }
                                }
                            }
                            if (reqitem.objectValue > totalT) {
                                isRequiredItemExists = false
                            }
                        }
                    }
                    if (!isRequiredItemExists) {
                        totalamount = 0.0
                    }
                }
                isApplicable = false
                //                if (discountModel.MaxBillAmount > 0 && totalamount > discountModel.MaxBillAmount) {
//                    totalamount = discountModel.MaxBillAmount;
//                } else
                if (discountModel.billAmount > totalamount) {
                    totalamount = 0.0
                    message += RetailerSDKApp.getInstance().noteRepository.getString(R.string.add_item_worth_rs) + (discountModel.billAmount - totalamount) + RetailerSDKApp.getInstance().noteRepository.getString(
                        R.string.to_unlock
                    )
                }
                if (discountModel.lineItem > 0 && discountModel.lineItem > orderLineItems) {
                    totalamount = 0.0
                    message += "\n" + RetailerSDKApp.getInstance().noteRepository.getString(R.string.add) +
                            (discountModel.lineItem - orderLineItems) +
                            RetailerSDKApp.getInstance().noteRepository.getString(R.string.more_line_items_to_unlock)
                }
                if (discountModel.offerOn == "ScratchBillDiscount" && !discountModel.isScratchBDCode) {
                    totalamount = 0.0
                    message =
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.scratch_the_card_first)
                }
                if (totalamount > 0) {
                    // Code for offer applicable or not
                    isApplicable = true
                    message = ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return isApplicable
        }
        return isApplicable
    }

    inner class ValAscComparator : Comparator<OfferLineItemValue> {
        override fun compare(lhs: OfferLineItemValue, rhs: OfferLineItemValue): Int {
            return java.lang.Double.compare(lhs.itemValue, rhs.itemValue)
        }
    }

    inner class AmtAscComparator : Comparator<ItemListModel> {
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            return java.lang.Double.compare(lhs.unitPrice * lhs.qty, rhs.unitPrice * lhs.qty)
        }
    }
}