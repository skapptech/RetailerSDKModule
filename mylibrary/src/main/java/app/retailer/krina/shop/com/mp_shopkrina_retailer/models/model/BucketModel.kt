package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class BucketModel {
    @SerializedName("GameBucketRewardConditions")
    val gameBucketRewardConditions: ArrayList<GameBucketRewardConditionsEntity>? = null

    @SerializedName("RewardCredited")
    val rewardCredited = 0

    @SerializedName("Status")
    val status: String? = null

    @SerializedName("RewardValue")
    val rewardValue = 0

    @SerializedName("RewardType")
    val rewardType: String? = null

    @SerializedName("TotalCustomers")
    val totalCustomers = ""

    @SerializedName("CRMBucketNo")
    val cRMBucketNo = 0

    @SerializedName("BucketNo")
    val bucketNo = 0

    class GameBucketRewardConditionsEntity {
        @SerializedName("AchiveValue")
        val achieveValue = 0

        @SerializedName("Value")
        val value = 0

        @SerializedName("AppDesc")
        val appDesc: String? = null

        @SerializedName("ConditionId")
        val conditionId = 0
    }
}