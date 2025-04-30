package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OfferItems : Serializable {
    @SerializedName("itemId")
    val itemId = 0

    @SerializedName("IsInclude")
    val isInclude = false
}