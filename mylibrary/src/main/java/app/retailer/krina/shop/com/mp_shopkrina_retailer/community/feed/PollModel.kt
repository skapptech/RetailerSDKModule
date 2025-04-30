package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PollModel : Serializable {
    var answers: String? = ""

    @SerializedName("_id")
    var id: String = ""
    var optionValue: String? = ""
    var createdDate: String? = ""
    var answerCount: Int? = 0
    var answerPercentage: Double = 0.0

    @SerializedName("IsAnswered")
    var isAnswered: Boolean = false
}