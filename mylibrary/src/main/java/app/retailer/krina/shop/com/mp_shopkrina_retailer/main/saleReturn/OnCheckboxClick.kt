package com.sk.user.agent.ui.component.returnOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnOrderBatchItemModel

interface OnCheckboxClick {
    fun onClick(isAddMoreImage:Boolean,model: ReturnOrderBatchItemModel,isSelectedChecked:Boolean)
}