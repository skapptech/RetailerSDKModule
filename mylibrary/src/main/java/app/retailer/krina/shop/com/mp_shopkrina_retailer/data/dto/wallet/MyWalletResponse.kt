package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet

import com.google.gson.annotations.SerializedName

class MyWalletResponse {
    @SerializedName("CustomerWalletHistory")
    val list: ArrayList<MyWalletModel>? = null

    @SerializedName("ExpiringPoints")
    val expiringPoints = 0.0

    @SerializedName("HowToUseWalletPoints")
    val howToUseWalletPoints: String? = null

    @SerializedName("totalCount")
    val totalCount = 0

    @SerializedName("ExpiredPoints")
    val expiredPoints = 0.0

    @SerializedName("TransactionDate")
    val transactionDate: String? = null

    @SerializedName("UpcomingPoints")
    val upcomingPoints = 0.0

    @SerializedName("TotalUsedPoints")
    val totalUsedPoints = 0.0

    @SerializedName("TotalEarnPoint")
    val totalEarnPoint = 0.0
}
