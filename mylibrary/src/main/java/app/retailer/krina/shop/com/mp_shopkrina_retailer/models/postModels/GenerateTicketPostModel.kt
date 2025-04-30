package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.SerializedName

class GenerateTicketPostModel {

    @SerializedName("Id")
    var id = 0

    @SerializedName("OrderNo")
    var OrderNo = ""

    constructor(id: Int, OrderNo: String) {
        this.id = id
        this.OrderNo = OrderNo
    }
}