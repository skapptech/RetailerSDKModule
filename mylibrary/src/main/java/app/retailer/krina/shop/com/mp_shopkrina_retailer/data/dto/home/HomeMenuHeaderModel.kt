package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

class HomeMenuHeaderModel {
    var title: String
    var list: List<String>? = null
    var isOpen = false

    constructor(headername: String, isOpen: Boolean) {
        title = headername
        this.isOpen = isOpen
    }

    constructor(title: String, list: List<String>?) {
        this.title = title
        this.list = list
    }
}
