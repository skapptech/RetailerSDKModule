package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class EpayLaterDetail {
    @SerializedName("Id")
    private int Id;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("SkCode")
    private String SkCode;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("FirmType")
    private String FirmType;
    @SerializedName("ProprietorFirstName")
    private String ProprietorFirstName;
    @SerializedName("ProprietorLastName")
    private String ProprietorLastName;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("WhatsAppNumber")
    private String WhatsAppNumber;
    @SerializedName("Email")
    private String email;
    @SerializedName("DOB")
    private String DOB;
    @SerializedName("PAN_No")
    private String PANNo;
    @SerializedName("Country")
    private String Country;
    @SerializedName("State")
    private String State;
    @SerializedName("City")
    private String City;
    @SerializedName("PostalCode")
    private String PostalCode;
    @SerializedName("LicenseImagePath")
    private String LicenseImagePath;
    @SerializedName("GSTImagePath")
    private String GSTImagePath;
    @SerializedName("FSSAIImagePath")
    private String FSSAIImagePath;
    @SerializedName("GovtRegNumberImagePath")
    private String GovtRegNumberImagePath;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lg")
    private String lg;

    public EpayLaterDetail(int customerId, String skCode, String shopName, String firmType, String proprietorFirstName, String proprietorLastName, String gender, String mobile, String whatsAppNumber, String email, String DOB, String PANNo, String country, String state, String city, String postalCode, String licenseImagePath, String GSTImagePath, String FSSAIImagePath, String govtRegNumberImagePath, String lat, String lg) {
        CustomerId = customerId;
        SkCode = skCode;
        ShopName = shopName;
        FirmType = firmType;
        ProprietorFirstName = proprietorFirstName;
        ProprietorLastName = proprietorLastName;
        Gender = gender;
        Mobile = mobile;
        WhatsAppNumber = whatsAppNumber;
        this.email = email;
        this.DOB = DOB;
        this.PANNo = PANNo;
        Country = country;
        State = state;
        City = city;
        PostalCode = postalCode;
        LicenseImagePath = licenseImagePath;
        this.GSTImagePath = GSTImagePath;
        this.FSSAIImagePath = FSSAIImagePath;
        GovtRegNumberImagePath = govtRegNumberImagePath;
        this.lat = lat;
        this.lg = lg;
    }

    public int getId() {
        return Id;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getSkCode() {
        return SkCode;
    }

    public String getShopName() {
        return ShopName;
    }

    public String getFirmType() {
        return FirmType;
    }

    public String getProprietorFirstName() {
        return ProprietorFirstName;
    }

    public String getProprietorLastName() {
        return ProprietorLastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getWhatsAppNumber() {
        return WhatsAppNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDOB() {
        return DOB;
    }

    public String getPANNo() {
        return PANNo;
    }

    public String getCountry() {
        return Country;
    }

    public String getState() {
        return State;
    }

    public String getCity() {
        return City;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getLicenseImagePath() {
        return LicenseImagePath;
    }

    public String getGSTImagePath() {
        return GSTImagePath;
    }

    public String getFSSAIImagePath() {
        return FSSAIImagePath;
    }

    public String getGovtRegNumberImagePath() {
        return GovtRegNumberImagePath;
    }

    public String getLat() {
        return lat;
    }

    public String getLg() {
        return lg;
    }
}