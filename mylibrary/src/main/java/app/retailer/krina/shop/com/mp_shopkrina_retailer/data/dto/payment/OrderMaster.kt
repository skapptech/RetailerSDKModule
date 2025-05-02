package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderMaster : Serializable {
    @SerializedName("ETADates")
    var etaDateList: List<ETADate>? = null

    @SerializedName("ExpectedETADate")
    var expectedETADate: String? = null

    @SerializedName("PlayedWheelCount")
    var playedWheelCount = 0

    @SerializedName("lang")
    var lang: String? = null

    @SerializedName("DialEarnigPoint")
    var dialearnigpoint = 0

    @SerializedName("WheelList")
    var wheelList: ArrayList<Int> = ArrayList()

    @SerializedName("WheelCount")
    var wheelcount = 0

    @SerializedName("TotalAmount")
    val totalamount = 0.0

    @SerializedName("OrderId")
    var orderid = 0

    @SerializedName("IsDefaultDeliveryChange")
    var isDefaultDeliveryChange = false

    @SerializedName("CustomerId")
    var customerId = 0

    class ETADate : Serializable {
        @SerializedName("ETADate")
        var etaDate: String? = null
    }
}