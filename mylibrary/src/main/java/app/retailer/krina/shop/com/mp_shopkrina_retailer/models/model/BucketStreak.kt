package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class BucketStreak {
    @SerializedName("GameStreakConfigData")
    val gameStreakConfigData: List<GameStreakConfigData>? = null

    @SerializedName("StreakIdTo")
    val streakIdTo = 0

    @SerializedName("StreakIdFrom")
    val streakIdFrom = 0

    @SerializedName("RewardValue")
    val rewardValue = ""

    @SerializedName("RewardCredited")
    val rewardCredited = 0.0

    @SerializedName("IndivitualRewardValue")
    var individualRewardValue: String? = null

    @SerializedName("Status")
    var status: String? = null

    @SerializedName("DaysLeft")
    val daysLeft = 0

    @SerializedName("StreakId")
    val streakId = 0

    @SerializedName("BucketNo")
    val bucketNo = 0
}