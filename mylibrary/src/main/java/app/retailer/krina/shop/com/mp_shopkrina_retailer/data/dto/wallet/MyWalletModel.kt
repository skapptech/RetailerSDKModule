package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet

import com.google.gson.annotations.SerializedName

class MyWalletModel {
    @SerializedName("OrderId")
    val orderId = 0

    @SerializedName("NewAddedWAmount")
    val newAddedWAmount = 0.0

    @SerializedName("NewOutWAmount")
    val newOutWAmount = 0

    @SerializedName("Through")
    val through: String? = null

    @SerializedName("TransactionDate")
    val transactionDate: String? = null

}
