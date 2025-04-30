package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment

import com.google.gson.annotations.SerializedName

class CreditLimit {
    @SerializedName("CreditLimit")
    var creditLimit = 0.0

    @SerializedName("IsBlock")
    var isBlock = false

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("IsSuccess")
    var isSuccess = false

    @SerializedName("DynamicData")
    var dynamicData: DynamicDataEntity? = null

    class DynamicDataEntity {
        @SerializedName("Amount")
        var amount = 0.0

        @SerializedName("AccountId")
        var accountId = 0

        @SerializedName("UniqueCode")
        var uniqueCode: String? = null

        @SerializedName("ShowHideLimit")
        var showHideLimit = false
    }
}