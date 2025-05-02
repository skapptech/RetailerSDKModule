package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class GameStreakConfigData {
    @SerializedName("Id")
    var id = 0

    @SerializedName("Type")
    var type: String? = null

    @SerializedName("Condition")
    var condition: String? = null

    @SerializedName("RewardTypeName")
    var rewardTypeName: String? = null

    @SerializedName("Reward")
    var reward: String? = null

    @SerializedName("StreakDescr")
    var streakDescr: String? = null

    @SerializedName("DayLeft")
    var dayLeft: Int = 0

    @SerializedName("StreakLevelValueFirst")
    var streakLevelRequired = 0

    @SerializedName("StreakLevelValueLast")
    var streakLevelTotal = 0

    @SerializedName("StreakAchivedCount")
    var streakAchievedCount = 0

    var viewType = 0
}