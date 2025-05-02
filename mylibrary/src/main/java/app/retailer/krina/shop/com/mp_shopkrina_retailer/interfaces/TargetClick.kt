package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NewTargetModel

interface TargetClick {
    fun onClaimButtonClicked(id: Int, target: String?)
    fun moreInfoDialog(model: NewTargetModel)
}