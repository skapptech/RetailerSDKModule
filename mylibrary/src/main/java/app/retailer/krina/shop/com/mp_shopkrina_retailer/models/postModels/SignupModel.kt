package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel
import com.google.gson.annotations.SerializedName

class SignupModel {
    @SerializedName("CustomerId")
    private var customerId: Int? = 0
    @SerializedName("Mobile")
    private var Mobile: String? = null

    @SerializedName("Name")
    private var Name: String? = null

    @SerializedName("ShopName")
    private var ShopName: String? = null

    @SerializedName("ShippingAddress")
    private var Address: String? = null

    @SerializedName("AreaName")
    private var areaName: String? = null

    @SerializedName("RefNo")
    private var FSAAI: String? = null

    @SerializedName("Password")
    private var Password: String? = null

    @SerializedName("Cityid")
    private var cityId = 0

    @SerializedName("City")
    private var city: String? = null

    @SerializedName("Skcode")
    private var Skcode: String? = null

    @SerializedName("CurrentAPKversion")
    private var CurrentAPKversion: String? = null

    @SerializedName("PhoneOSversion")
    private var PhoneOSversion: String? = null

    @SerializedName("UserDeviceName")
    private var UserDeviceName: String? = null

    @SerializedName("IMEI")
    private var IMEI: String? = null

    @SerializedName("deviceId")
    private var deviceId: String? = null

    @SerializedName("lat")
    private var lat = 0.0

    @SerializedName("lg")
    private var lg = 0.0

    @SerializedName("fcmId")
    private var fcmId: String? = null

    @SerializedName("LandMark")
    private var landMark: String? = null

    @SerializedName("ZipCode")
    private var zipCode: String? = null

    @SerializedName("CustomerAddress")
    private var addressModel: CustomerAddressModel? = null

    constructor() {}

    constructor(
        customerId: Int?,
        mobile: String?,
        name: String?,
        shopName: String?,
        address: String?,
        areaName: String?,
        password: String?,
        cityId: Int,
        skcode: String?,
        currentAPKversion: String?,
        phoneOSversion: String?,
        userDeviceName: String?,
        IMEI: String?,
        deviceId: String?,
        city: String?,
        lat: Double,
        lg: Double,
        fcmId: String?,
        landMark: String?,
        zipCode: String?,
        addressModel: CustomerAddressModel?
    ) {
        this.customerId = customerId
        Mobile = mobile
        Name = name
        ShopName = shopName
        Address = address
        this.areaName = areaName
        FSAAI = FSAAI
        Password = password
        this.cityId = cityId
        Skcode = skcode
        CurrentAPKversion = currentAPKversion
        PhoneOSversion = phoneOSversion
        UserDeviceName = userDeviceName
        this.IMEI = IMEI
        this.deviceId = deviceId
        this.city = city
        this.lat = lat
        this.lg = lg
        this.fcmId = fcmId
        this.landMark = landMark
        this.zipCode = zipCode
        this.addressModel = addressModel
    }
}