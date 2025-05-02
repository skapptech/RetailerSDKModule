package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

class IssueThreadModel {
    var message: String = ""
    var isSelected: Boolean = false
    var time: String = ""

    constructor(message: String, isSelected: Boolean, time: String) {
        this.message = message
        this.isSelected = isSelected
        this.time = time
    }
}