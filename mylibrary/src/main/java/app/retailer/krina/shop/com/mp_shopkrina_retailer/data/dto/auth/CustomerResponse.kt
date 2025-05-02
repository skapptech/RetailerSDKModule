package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName

class CustomerResponse {
    @JvmField
    @SerializedName("CustomerId")
    var customerId = 0

    @SerializedName("Skcode")
    var skcode: String? = null

    @SerializedName("ShopName")
    var shopName: String? = null

    @SerializedName("CustomerVerify")
    val customerVerify: String? = null

    @SerializedName("DOB")
    val dOB: String? = null

    @SerializedName("Warehouseid")
    var warehouseid = 0

    @SerializedName("Mobile")
    var mobile: String? = null

    @SerializedName("WarehouseName")
    private val WarehouseName: String? = null

    @SerializedName("Name")
    var name: String? = null

    @SerializedName("Password")
    var password: String? = null

    @SerializedName("CustomerType")
    var customerType: String? = null

    @SerializedName("CompanyId")
    var companyId = 0

    @SerializedName("TypeOfBuissness")
    private val TypeOfBuissness: String? = null

    @SerializedName("ShippingAddress")
    val shippingAddress: String? = null

    @SerializedName("ShippingAddress1")
    val shippingAddress1: String? = null

    @SerializedName("LandMark")
    val landMark: String? = null

    @SerializedName("State")
    var state: String? = null

    @SerializedName("Cityid")
    var cityid = 0

    @SerializedName("City")
    var city: String? = null

    @SerializedName("ZipCode")
    var zipCode: String? = null

    @SerializedName("RefNo")
    val refNo: String? = null

    @SerializedName("UploadGSTPicture")
    val uploadGSTPicture: String? = null

    @SerializedName("LicenseNumber")
    val licenseNumber: String? = null

    @SerializedName("Emailid")
    val emailid: String? = null

    @SerializedName("Active")
    var active = false

    @SerializedName("IsSignup")
    val isSignup = false

    @SerializedName("UploadProfilePichure")
    val uploadProfilePichure: String? = null

    @SerializedName("AreaName")
    val areaName: String? = null

    @SerializedName("AnniversaryDate")
    val anniversaryDate: String? = null

    @SerializedName("WhatsappNumber")
    val whatsappNumber: String? = null

    @SerializedName("UploadRegistration")
    val uploadLicensePicture: String? = null

    @SerializedName("Shopimage")
    val shopimage: String? = null

    @JvmField
    @SerializedName("lat")
    var lat: String? = null

    @JvmField
    @SerializedName("lg")
    var lg: String? = null

    @SerializedName("IsResetPasswordOnLogin")
    val isResetPasswordOnLogin = false

    @SerializedName("imei")
    private val imei: String? = null

    @SerializedName("ClusterId")
    val clusterId: String? = null

    @JvmField
    @SerializedName("APKType")
    val registeredApk: UserAuth? = null

    @JvmField
    @SerializedName("IsPrimeCustomer")
    val isPrimeCustomer = false

    @SerializedName("PrimeStartDate")
    val primeStartDate: String? = null

    @JvmField
    @SerializedName("PrimeEndDate")
    val primeEndDate: String? = null

    @SerializedName("IsKPP")
    val isKPP = false

    @SerializedName("AadharNo")
    val aadharNo: String? = null

    @SerializedName("PanNo")
    val panNo: String? = null

    @SerializedName("NameOnGST")
    val nameOnGST: String? = null

    @SerializedName("BillingAddress")
    val billingAddress: String? = null

    @SerializedName("BillingState")
    val billingState: String? = null

    @SerializedName("BillingCity")
    val billingCity: String? = null

    @SerializedName("BillingZipCode")
    val billingZipCode: String? = null

    @SerializedName("IsContactsRead")
    val isContactsRead = false

    @SerializedName(value = "customerDocTypeId", alternate = ["CustomerDocTypeMasterId"])
    var customerDocTypeId = 0

    @SerializedName("IsRequiredLocation")
    var isRequiredLocation = false

    @SerializedName("LicenseExpiryDate")
    var licenseExpiryDate: String? = null

    @SerializedName("IsWarehouseLive")
    var isWarehouseLive = false

    @SerializedName("IsSelleravailable")
    var isSellerAvailable = false
}