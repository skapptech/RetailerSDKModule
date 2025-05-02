package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import java.io.Serializable

class OfferLineItemValue : Serializable {
    var Id = 0
    var offerId = 0

    @JvmField
    var itemValue = 0.0
}