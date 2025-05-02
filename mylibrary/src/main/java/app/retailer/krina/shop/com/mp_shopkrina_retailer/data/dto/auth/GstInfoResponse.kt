package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GstInfoResponse {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("Status")
    var status = false

    @Expose
    @SerializedName("custverify")
    var custverify: CustverifyEntity? = null

    class CustverifyEntity {
        @Expose
        @SerializedName("Active")
        var active: String? = null

        @Expose
        @SerializedName("Name")
        var name: String? = null

        @Expose
        @SerializedName("ShopName")
        var shopname: String? = null

        @Expose
        @SerializedName("id")
        var id = 0

        @Expose
        @SerializedName("RefNo")
        var refNo: String? = null

        @Expose
        @SerializedName("BillingAddress")
        var billingAddress: String? = null

        @Expose
        @SerializedName("ShippingAddress")
        var shippingAddress: String? = null

        @Expose
        @SerializedName("LandMark")
        var landMark: String? = null

        @Expose
        @SerializedName("Country")
        var country: String? = null

        @Expose
        @SerializedName("State")
        var state: String? = null

        @Expose
        @SerializedName("Cityid")
        var cityid = 0

        @Expose
        @SerializedName("City")
        var city: String? = null

        @Expose
        @SerializedName("CreatedDate")
        var createdDate: String? = null

        @Expose
        @SerializedName("Zipcode")
        var zipcode: String? = null

        @Expose
        @SerializedName("lat")
        var lat: String? = null

        @Expose
        @SerializedName("lg")
        var lg: String? = null
    }
}
