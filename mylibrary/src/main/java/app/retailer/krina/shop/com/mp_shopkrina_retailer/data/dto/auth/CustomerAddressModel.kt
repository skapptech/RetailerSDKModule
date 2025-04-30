package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CustomerAddressModel: Serializable {
    @SerializedName("customerId")
    var customerId = 0

    @SerializedName("CityPlaceId")
    var cityPlaceId: String? = null

    @SerializedName("CityName")
    var cityName: String? = null

    @SerializedName("cityId")
    var cityId = 0

    @SerializedName("AreaPlaceId")
    var areaPlaceId: String? = null

    @SerializedName("AreaText")
    var areaText: String? = null

    @SerializedName("AreaLat")
    var areaLat = 0.0

    @SerializedName("AreaLng")
    var areaLng = 0.0

    @SerializedName("AddressPlaceId")
    var addressPlaceId: String? = null

    @SerializedName("AddressText")
    var addressText: String? = null

    @SerializedName("AddressLat")
    var addressLat = 0.0

    @SerializedName("AddressLng")
    var addressLng = 0.0

    @SerializedName(value = "ZipCode", alternate = ["Zipcode"])
    var zipCode: String? = null

    @SerializedName("AddressLineOne")
    var addressLineOne: String? = null

    @SerializedName("AddressLineTwo")
    var addressLineTwo: String? = null

    @SerializedName("state")
    var state: String? = null
}