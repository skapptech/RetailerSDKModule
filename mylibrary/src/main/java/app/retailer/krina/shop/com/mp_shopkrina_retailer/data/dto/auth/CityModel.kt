package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class CityModel {
    @SerializedName("Cityid")
    var cityid = 0

    @SerializedName("CityName")
    var cityName: String? = null

    @SerializedName("CityLatitude")
    var lat = 0.0

    @SerializedName("CityLongitude")
    var lng = 0.0
}