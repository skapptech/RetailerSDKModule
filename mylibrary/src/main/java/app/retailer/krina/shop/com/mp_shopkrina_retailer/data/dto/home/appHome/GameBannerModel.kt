package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GameBannerModel {
    @SerializedName("IndividualRewardValue")
    @Expose
    var individualRewardValue: Double? = null

    @SerializedName("OutofRewardValue")
    @Expose
    var outofRewardValue: Double? = null

    @SerializedName("StreakRewardValue")
    @Expose
    var streakRewardValue: Double? = null

    @SerializedName("WalletRewardValue")
    @Expose
    var walletRewardValue: Double? = null

    @SerializedName("IndividualDaysLeft")
    @Expose
    var individualDaysLeft = 0

    @SerializedName("IndividualOrderPunch")
    @Expose
    var individualOrderPunch: Boolean? = null
}
