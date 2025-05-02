package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils

class OfferCheck(
    private val discountModel: BillDiscountModel,
    private val cartList: List<ItemListModel>,
    cartTotal: Double
) {
    var cartTotal = 0.0
    var itemTotal = 0.0
    var totalamount = 0.0
    var orderLineItems = 0

    //
    var requiredTotal = 0.0
    var requiredItems = 0.0

    init {
        this.cartTotal = cartTotal
    }

    fun checkCoupon(): Boolean {
        var isApplicable = false
        try {
            if (cartTotal > 0) {
                if (TextUtils.isNullOrEmpty(discountModel.billDiscountType)) {
                    discountModel.billDiscountType = "BillDiscount"
                }

                if (discountModel.billDiscountType.equals("category", ignoreCase = true)) {
                    for (cartItem in cartList) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.categoryid == offerItems.id && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartList) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.isNotEmpty()) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        itemTotal = totalamount
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
                    for (cartItem in cartList) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subCategoryId == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                                break
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartList) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems++
                                }
                            }
                        }
                    }
                    itemTotal = totalamount
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.size > 0) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
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
                    for (cartItem in cartList) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subsubCategoryid == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.subCategoryId == offerItems.subCategoryId && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartList) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems--
                                }
                            }
                        }
                    }
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.isNotEmpty()) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        itemTotal = totalamount
                        if (lineItemValueItemExists.size < discountModel.offerLineItemValueDcs!!.size) {
                            totalamount = 0.0
                        }
                    }
                    if (totalamount >= discountModel.billAmount && (discountModel.lineItem == 0 || discountModel.lineItem <= orderLineItems)) {
                        isApplicable = true
                    }
                } else if (discountModel.billDiscountType.equals("brand", ignoreCase = true)) {
                    for (cartItem in cartList) {
                        for (offerItems in discountModel.offerBillDiscountItems!!) {
                            if (cartItem.subsubCategoryid == offerItems.id && cartItem.categoryid == offerItems.categoryId && cartItem.subCategoryId == offerItems.subCategoryId && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                        for (cartItem in cartList) {
                            for (offerItems in discountModel.offerItemsList) {
                                if (cartItem.itemId == offerItems.itemId && !offerItems.isInclude && cartItem.qty > 0) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems--
                                }
                            }
                        }
                    }
                    itemTotal = totalamount
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.isNotEmpty()) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
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
                    for (cartItem in cartList) {
                        for (offerItems in discountModel.offerItemsList!!) {
                            if (cartItem.itemId == offerItems.itemId && offerItems.isInclude && cartItem.qty > 0) {
                                totalamount += cartItem.unitPrice * cartItem.qty
                                orderLineItems++
                            }
                        }
                    }
                    itemTotal = totalamount
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.isNotEmpty()) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
                            for (itemValue in discountModel.offerLineItemValueDcs!!) {
                                if (!lineItemValueItemExists.contains(cartItem.itemId) && cartItem.unitPrice * cartItem.qty >= itemValue.itemValue && cartItem.qty > 0) {
                                    lineItemValueItemExists.add(cartItem.itemId)
                                    break
                                }
                            }
                        }
                        itemTotal = totalamount
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
                    itemTotal = cartTotal
                    val isRequiredItemExists = true
                    if (discountModel.offerBillDiscountItems!!.size > 0 || discountModel.offerItemsList!!.size > 0) {
                        orderLineItems = cartList.size
                        for (cartItem in cartList) {
                            for (offerItems in discountModel.offerBillDiscountItems) {
                                if (cartItem.categoryid == offerItems.id && cartItem.qty > 0 && !offerItems.isInclude) {
                                    totalamount -= cartItem.unitPrice * cartItem.qty
                                    orderLineItems--
                                }
                            }
                        }
                        if (discountModel.offerItemsList != null && discountModel.offerItemsList.size > 0) {
                            for (cartItem in cartList) {
                                for (offerItems in discountModel.offerItemsList) {
                                    if (cartItem.itemId == offerItems.itemId && cartItem.qty > 0 && !offerItems.isInclude) {
                                        totalamount -= cartItem.unitPrice * cartItem.qty
                                        orderLineItems--
                                    }
                                }
                            }
                        }
                        itemTotal = totalamount
                    } else {
                        for (info in cartList) {
                            if (info.qty > 0) {
                                orderLineItems++
                            }
                        }
                    }

                    // cart item
                    if (discountModel.offerLineItemValueDcs != null && discountModel.offerLineItemValueDcs!!.isNotEmpty()) {
                        val lineItemValueItemExists: MutableList<Int> = ArrayList()
                        for (cartItem in cartList) {
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
                if (discountModel.requiredItemsList != null && discountModel.requiredItemsList!!.isNotEmpty()) {
                    var isRequiredItemExists = true
                    for (reqitem in discountModel.requiredItemsList!!) {
                        var totalT = 0.0
                        if (reqitem.objectType.equals("Item", ignoreCase = true)) {
                            if (reqitem.valueType.equals("Qty", ignoreCase = true)) {
                                for (cartItem in cartList) {
                                    if (reqitem.objectId!!.contains(cartItem.itemMultiMRPId.toString()) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.qty
                                    }
                                }
                                requiredItems += totalT
                            } else if (reqitem.valueType.equals("Value", ignoreCase = true)) {
                                for (cartItem in cartList) {
                                    if (reqitem.objectId!!.contains(cartItem.itemMultiMRPId.toString()) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.unitPrice * cartItem.qty
                                    }
                                }
                                requiredTotal += totalT
                            }
                            if (reqitem.objectValue > totalT) {
                                isRequiredItemExists = false
                            }
                        } else if (reqitem.objectType.equals("brand", ignoreCase = true)) {
                            if (reqitem.valueType.equals("Qty", ignoreCase = true)) {
                                for (cartItem in cartList) {
                                    if (reqitem.objectId!!.contains(cartItem.subsubCategoryid.toString()) && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")) {
                                        totalT += cartItem.qty
                                    }
                                }
                            } else if (reqitem.valueType.equals("Value", ignoreCase = true)) {
                                for (cartItem in cartList) {
                                    if (reqitem.objectId!!.contains(cartItem.subsubCategoryid.toString())
                                        && reqitem.objectId!!.contains(cartItem.subCategoryId.toString())
                                        && reqitem.objectId!!.contains(cartItem.categoryid.toString())
                                        && !(cartItem.isOffer && cartItem.offerType == "FlashDeal")
                                    ) {
                                        totalT += cartItem.unitPrice * cartItem.qty
                                    }
                                }
                                requiredTotal += totalT
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
                if (discountModel.billAmount > totalamount) {
                    totalamount = 0.0
                }
                if (discountModel.lineItem > 0 && discountModel.lineItem > orderLineItems) {
                    totalamount = 0.0
                }
                if (discountModel.offerOn == "ScratchBillDiscount" && !discountModel.isScratchBDCode) {
                    totalamount = 0.0
                }
                if (totalamount > 0) {
                    //Code for offer applicable or not
                    isApplicable = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isApplicable
    }
}