package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Cart : Serializable {
    @SerializedName("Trupay")
    var isTrupay = false

    @SerializedName("TotalAmount")
    var totalAmount = 0.0

    @SerializedName("CreatedDate")
    var createdDate: String? = null

    @SerializedName("paymentThrough")
    var paymentThrough: String? = null

    @SerializedName("itemDetails")
    var itemDetails: ArrayList<ItemDetailsResponse>? = null
}
