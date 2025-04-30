package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class GullakModel {
    @SerializedName("Id")
    var Id : Long? = null
    @SerializedName("CreatedDate")
    var createdDate: String? = null

    @SerializedName("CustomerId")
    var customerId: Int? = null

    @SerializedName("Amount")
    var amount: Double? = null

    @SerializedName("ObjectType")
    var objectType: String? = null

    @SerializedName("ObjectId")
    var objectId: String? = null

    @SerializedName("Comment", alternate = ["comment"])
    var comment: String? = ""

    @SerializedName("RefNo")
    var refNo: String? = ""
}