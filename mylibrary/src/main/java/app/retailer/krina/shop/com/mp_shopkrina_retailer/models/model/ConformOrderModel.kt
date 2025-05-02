package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyorderPaymentDetails
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ConformOrderModel : Serializable {
    @SerializedName("OrderId")
    var orderId: Int = 0

    @SerializedName("CreatedDate")
    val createdDate: String? = null

    @SerializedName("UpdatedDate")
    val updatedDate: String? = null

    @SerializedName("ReadytoDispatchedDate")
    val readytoDispatchedDate: String? = null

    @SerializedName("Deliverydate")
    val deliverydate: String? = null

    @SerializedName("GrossAmount")
    var grossAmount: Double = 0.0

    @SerializedName("RemainingAmount")
    var remainingAmount: Double = 0.0

    @SerializedName("CustomerName")
    val customerName: String? = null

    @SerializedName("ShippingAddress")
    val shippingAddress: String? = null

    @SerializedName("BillingAddress")
    val billingAddress: String? = null

    @SerializedName(value = "status", alternate = ["Status"])
    val status: String? = null

    @SerializedName("EnablePayNowButton")
    var enablePayNowButton: Boolean = false

    @SerializedName("PayNowOption")
    var payNowOption: String? = null

    @SerializedName("IsPlayWeel")
    val isPlayWheel: Boolean = false

    @SerializedName("WheelCount")
    val wheelCount: Int = 0

    @SerializedName("WheelList")
    val wheelList: ArrayList<Int>? = null

    @SerializedName("OrderPayments")
    val orderPayments: ArrayList<MyorderPaymentDetails>? = null

    @SerializedName("itemDetails")
    val itemDetails: ArrayList<OrderItemDetails>? = null

    @SerializedName("StoreName")
    val storeName: String? = null

    @SerializedName("DeliveryOtp")
    var deliveryOtp: Int = 0

    @SerializedName("Rating")
    var rating: Float = 0f

    @SerializedName("SalesPerson")
    var salesPerson: String? = null

    @SerializedName("SalesPersonMobile")
    var salesPersonMobile: String? = null

    @SerializedName("SalesPersonProfilePic")
    var salesPersonProfilePic: String? = null

    @SerializedName("DeliveryPerson")
    var deliveryPerson: String? = null

    @SerializedName("DeliveryPersonMobile")
    var deliveryPersonMobile: String? = null

    @SerializedName("DboyProfilePic")
    var dBoyProfilePic: String? = null

    @SerializedName("RebookOrder")
    val isRebookOrder: Boolean = false

    @SerializedName("IsCustomerRaiseConcern")
    val isCustomerRaiseConcern: Boolean = false

    @SerializedName("OrderType")
    var orderType: Int = 0

    @SerializedName("IsPayLater")
    var isPayLater: Boolean = false


    constructor()

    constructor(
        orderId: Int,
        grossAmount: Double,
        remainingAmount: Double,
        enablePayNowButton: Boolean,
        payNowOption: String?
    ) {
        this.orderId = orderId
        this.grossAmount = grossAmount
        this.remainingAmount = remainingAmount
        this.enablePayNowButton = enablePayNowButton
        this.payNowOption = payNowOption
    }
}
