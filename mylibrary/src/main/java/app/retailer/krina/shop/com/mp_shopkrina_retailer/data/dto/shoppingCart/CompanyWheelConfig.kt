package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CompanyWheelConfig : Serializable {
    @SerializedName("LineItemCount")
    var lineItemCount = 0

    @SerializedName("OrderAmount")
    var orderAmount = 0.0

    @SerializedName("IsKPPRequiredWheel")
    var isKPPRequiredWheel = false
}
