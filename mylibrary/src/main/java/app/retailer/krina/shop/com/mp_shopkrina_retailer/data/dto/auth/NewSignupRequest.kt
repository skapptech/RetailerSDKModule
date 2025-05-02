package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class NewSignupRequest(
    @SerializedName("CustomerId")
    var CustomerId: Int,
    @SerializedName("Name")
    var Name: String? = "",
    @SerializedName("UploadProfilePichure")
    var profilePic: String? = "",
    @SerializedName("Mobile")
    var Mobile: String? = "",
    @SerializedName("Emailid")
    var Emailid: String? = "",
    @SerializedName("ShippingAddress")
    var Address: String? = "",
    @SerializedName("AreaName")
    var areaName: String? = "",
    @SerializedName("ShippingAddress1")
    var Address1: String? = "",
    @SerializedName("LandMark")
    var LandMark: String? = "",
    @SerializedName("ZipCode")
    var ZipCode: String? = "",
    @SerializedName("Password")
    var Password: String? = "",
    @SerializedName("Cityid")
    var cityId: Int,
    @SerializedName("City")
    var city: String? = "",
    @SerializedName("Skcode")
    var Skcode: String? = "",
    @SerializedName("RefNo")
    var FSAAI: String? = "",
    @SerializedName("UploadGSTPicture")
    var UploadGSTPicture: String? = "",
    @SerializedName("ShopName")
    var ShopName: String? = "",
    @SerializedName("Shopimage")
    var Shopimage: String? = "",
    @SerializedName("CurrentAPKversion")
    var CurrentAPKversion: String? = "",
    @SerializedName("PhoneOSversion")
    var PhoneOSversion: String? = "",
    @SerializedName("UserDeviceName")
    var UserDeviceName: String? = "",
    @SerializedName("imei")
    var IMEI: String? = "",
    @SerializedName("deviceId")
    var deviceId: String? = "",
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lg")
    var lg: Double,
    @SerializedName("LicenseNumber")
    var LicenseNumber: String?,
    @SerializedName("UploadRegistration")
    var UploadRegistration: String?, //Trade New Changes
    @SerializedName("BankName")
    var BankName: String?,
    @SerializedName("AccountNumber")
    var AccountNumber: String?,
    @SerializedName("IfscCode")
    var IfscCode: String?,
    @SerializedName("ReferralSkCode")
    var ReferralSkCode: String?,
    @SerializedName("IsReferral")
    var IsReferral: Boolean?,
    @SerializedName("fcmId")
    var fcmId: String?,
    @SerializedName("AadharNo")
    var AadharNo: String?,
    @SerializedName("PanNo")
    var PanNo: String?,
    @SerializedName("NameOnGST")
    var NameOnGST: String?,
    @SerializedName("BillingAddress")
    var BillingAddress: String?,
    @SerializedName("BillingCity")
    var BillingCity: String?,
    @SerializedName("BillingState")
    var BillingState: String?,
    @SerializedName("BillingZipCode")
    var BillingZipCode: String?,
    @SerializedName("CustomerDocTypeMasterId")
    var customerDocTypeId: Int?,
    @SerializedName("Shoplat")
    var shopLat: Double,
    @SerializedName("Shoplg")
    var shopLng: Double,
    @SerializedName("LicenseExpiryDate")
    var licenseExpiryDate: String?,
    @SerializedName("CustomerAddress")
    var addressModel: CustomerAddressModel?,
    @SerializedName("Channel")
    var channel: String
)