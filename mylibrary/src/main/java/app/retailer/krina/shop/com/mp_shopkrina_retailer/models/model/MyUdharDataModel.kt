package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class MyUdharDataModel {
    @SerializedName("Status")
    var isStatus = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("Myudhcust")
    var myudhcust: MyudhcustClass? = null

    inner class MyudhcustClass {
        @SerializedName("MyudharId")
        var myudharId = 0

        @SerializedName("CustomerId")
        var customerId = 0

        @SerializedName("Termcondition")
        var isTermcondition = false

        @SerializedName("PanCardNo")
        var panCardNo: String? = null

        @SerializedName("PanCardurl")
        var panCardurl: String? = null

        @SerializedName("AddressProofUrl")
        var addressProofUrl: String? = null

        @SerializedName("BackImageUrl")
        var backImageUrl: String? = null

        @SerializedName("Gender")
        var gender: String? = null

        @SerializedName("othercustdoc")
        var othercustdoc: String? = null
    }
}