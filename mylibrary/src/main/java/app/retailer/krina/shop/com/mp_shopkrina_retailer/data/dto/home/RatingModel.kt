package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RatingModel : Serializable {
    @SerializedName("UserId")
    var userId = 0

    @SerializedName("AppType")
    var appType = 0

    @SerializedName("Rating")
    var rating = 0

    @SerializedName("OrderId")
    var orderId = 0

    @SerializedName("ShopVisited")
    var shopVisited: String? = null

    @SerializedName("RatingDetails")
    var ratingDetails: List<UserRatingDetailDc>? = null

    @SerializedName("DisplayName")
    var displayName: String? = null

    @SerializedName("ProfilePic")
    var profilePic: String? = null

    @SerializedName("OrderedDate")
    var orderedDate: String? = null

    @SerializedName("IsRemoveFront")
    var isRemoveFront = false

    @SerializedName("DBoyName")
    var dBoyName: String? = null

    inner class UserRatingDetailDc : Serializable {
        @JvmField
        var Detail: String? = null

        @JvmField
        var isSelect = false
    }
}