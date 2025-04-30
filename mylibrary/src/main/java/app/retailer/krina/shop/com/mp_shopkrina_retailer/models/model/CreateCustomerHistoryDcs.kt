package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class CreateCustomerHistoryDcs {

    @SerializedName("CustomerId")
    val customerId: Int = 0

    @SerializedName("GameBucketNo")
    val gameBucketNo: Int = 0

    @SerializedName("BucketNo")
    val bucketNo: Int = 0

    @SerializedName("ForRewardStrack")
    val forRewardStrack: String? = null

    @SerializedName("GameBucketRewardId")
    val gameBucketRewardId: Int = 0

    @SerializedName("GameStreakLevelConfigMasterId")
    val gameStreakLevelConfigMasterId: Int = 0

    @SerializedName("GameStreakLevelConfigDetailId")
    val gameStreakLevelConfigDetailId: Int = 0

    @SerializedName("StreakIdFrom")
    val streakIdFrom: Int = 0

    @SerializedName("StreakIdTo")
    val streakIdTo: Int = 0

    @SerializedName("RewardValue")
    val rewardValue: Double = 0.0

    @SerializedName("RewardStatus")
    val rewardStatus: String? = null

    @SerializedName("RewardStatusDate")
    val rewardStatusDate: String? = null

    @SerializedName("BucketStartDate")
    val bucketStartDate: String? = null

    @SerializedName("BucketEndDate")
    val bucketEndDate: String? = null

    @SerializedName("GameType")
    val gameType: String? = null

    @SerializedName("GameCondition")
    val gameCondition: String? = null

}