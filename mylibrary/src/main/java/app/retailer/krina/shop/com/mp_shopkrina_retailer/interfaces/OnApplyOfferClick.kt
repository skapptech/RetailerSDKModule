package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse

interface OnApplyOfferClick {
    fun onApplyOfferClick(jsonObject: CheckoutCartResponse?)
}