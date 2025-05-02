package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet

import com.google.gson.annotations.SerializedName

class WalletResponse {
    @SerializedName("conversion")
    var conversion: Conversion? = null

    @SerializedName("Wallet")
    var wallet: Wallet? = null


    class Conversion {
        var rupee = 0
        var point = 0
    }
    class Reward {
        var updatedDate: String? = null
        var deleted = false
        var transactionDate: String? = null
        var createdDate: String? = null
        var milestonePoint = 0
        var earningPoint = 0
        var totalPoint = 0
        var customerId = 0
        var id = 0
    }

    class Wallet {
        @SerializedName("TotalAmount")
        var totalAmount: String? = null
    }
}