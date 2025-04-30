package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketStreak
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GameStreakConfigData
import com.google.gson.annotations.SerializedName

class BucketGameResponse {
    @SerializedName("CurrentBucket")
    var currentBucket = 0

    @SerializedName("LevelUpBucket")
    var levelUpBucket = 0

    @SerializedName("NextOrderDay")
    var nextOrderDay = 0

    @SerializedName("CurrentStreakBucket")
    var currentStreakBucket = 0

    @SerializedName("GameBucket")
    var list: ArrayList<BucketModel>? = null

    @SerializedName("CustomerStreakDataList")
    var streakList: ArrayList<BucketStreak>? = null

    @SerializedName("StreakConfigDataList")
    var streakConfigList: ArrayList<GameStreakConfigData>? = null
}