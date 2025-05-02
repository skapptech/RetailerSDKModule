package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueTicketsModel

interface AddIssueInterface {
    fun loadOptions(list: IssueTicketsModel,categoryAnsware: String)
    fun doneBtnClick(list: IssueTicketsModel,categoryAnsware: String)
    fun RadioBtnClick(sRadio:String)
}