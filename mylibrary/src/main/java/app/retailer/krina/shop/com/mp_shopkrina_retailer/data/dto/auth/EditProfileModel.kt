package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EditProfileModel : Serializable {
    @SerializedName("CustomerId")
    var customerId = 0

    @SerializedName("Emailid")
    var emailid: String? = ""

    @SerializedName("Mobile")
    var mobile: String? = null

    @SerializedName("DOB")
    var dOB: String? = ""

    @SerializedName("Cityid")
    var cityid: Int = 0

    @SerializedName("UploadProfilePichure")
    var profilePicture: String? = ""

    @SerializedName("lat")
    var lat: String? = ""

    @SerializedName("lg")
    var lg: String? = ""

    @SerializedName("AreaName")
    var areaName: String? = ""

    @SerializedName("ZipCode")
    var zipCode: String? = ""

    @SerializedName("RefNo")
    var refNo: String? = ""

    @SerializedName("ShippingAddress")
    var shippingAddress: String? = ""

    @SerializedName("ShippingAddress1")
    var shippingAddress1: String? = ""

    @SerializedName("LicenseNumber")
    var licenseNumber: String? = ""

    @SerializedName("UploadRegistration")
    var uploadRegistration: String? = ""

    @SerializedName("Shopimage")
    var shopimage: String? = ""

    @SerializedName("UploadGSTPicture")
    var uploadGSTPicture: String? = ""

    @SerializedName("ShopName")
    var shopName: String? = ""

    @SerializedName("Name")
    var name: String? = ""

    @SerializedName("AnniversaryDate")
    var anniversaryDate: String? = ""

    @SerializedName("WhatsappNumber")
    var whatsappNumber: String? = ""

    @SerializedName("LandMark")
    var landMark: String? = ""

    @SerializedName("AadharNo")
    var aadharNo: String? = ""

    @SerializedName("PanNo")
    var panNo: String? = ""

    @SerializedName("NameOnGST")
    var nameOnGST: String? = ""

    @SerializedName("City")
    var city: String? = ""

    @SerializedName("BillingAddress")
    var billingAddress: String? = ""

    @SerializedName("BillingCity")
    var billingCity: String? = ""

    @SerializedName("BillingState")
    var billingState: String? = ""

    @SerializedName("BillingZipCode")
    var billingZipCode: String? = ""

    @SerializedName("State")
    var state: String? = ""

    @SerializedName("CustomerDocTypeMasterId")
    var customerDocTypeId = 0

    @SerializedName("Shoplat")
    var shopLat = 0.0

    @SerializedName("Shoplg")
    var shopLng = 0.0

    @SerializedName("LicenseExpiryDate")
    var licenseExpiryDate: String? = ""

    @SerializedName("CustomerAddress")
    var addressModel: CustomerAddressModel? = null

    constructor() {}
    constructor(
        custId: Int,
        editEmailId: String?,
        scontactNumber: String?,
        dob: String?,
        cityId: Int,
        city: String?,
        editRefNo: String?,
        editShippingaddress: String?,
        shippingAddress1: String?,
        editShopName: String?,
        editName: String?,
        fProfile: String?,
        editBillingaddress: String?,
        editAreaName: String?,
        editZipCode: String?,
        lat: String?,
        lag: String?,
        editAnniversaryDate: String?,
        editwhatsappno: String?,
        LicenseNumber: String?,
        UploadLicensePichure: String?,
        Shopimage: String?,
        UploadGSTPicture: String?,
        landMark: String?,
        AadharNo: String?,
        PanNo: String?,
        NameOnGST: String?,
        billAdd: String?,
        BillingCity: String?,
        BillingState: String?,
        BillingZipCode: String?,
        customerDocTypeId: Int,
        licenseExpiryDate: String?
    ) {
        customerId = custId
        emailid = editEmailId
        mobile = scontactNumber
        dOB = dob
        this.cityid = cityId
        this.city = city
        refNo = editRefNo
        shippingAddress = editShippingaddress
        this.shippingAddress1 = shippingAddress1
        shopName = editShopName
        name = editName
        profilePicture = fProfile
        billingAddress = editBillingaddress
        areaName = editAreaName
        zipCode = editZipCode
        this.lat = lat
        lg = lag
        anniversaryDate = editAnniversaryDate
        whatsappNumber = editwhatsappno
        shopimage = Shopimage
        uploadRegistration = UploadLicensePichure
        licenseNumber = LicenseNumber
        uploadGSTPicture = UploadGSTPicture
        this.landMark = landMark
        aadharNo = AadharNo
        panNo = PanNo
        nameOnGST = NameOnGST
        billingAddress = billAdd
        billingCity = BillingCity
        billingState = BillingState
        billingZipCode = BillingZipCode
        this.customerDocTypeId = customerDocTypeId
        this.licenseExpiryDate = licenseExpiryDate
    }
}