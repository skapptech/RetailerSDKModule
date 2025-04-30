package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class EPayPartnerModel {
    @SerializedName("CustomerId")
    public int CustomerId;
    @SerializedName("PartnerFristName")
    private String PartnerFristName;
    @SerializedName("PartnerLastName")
    private String PartnerLastName;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("WhatsAppNumber")
    private String WhatsAppNumber;
    @SerializedName("Email")
    public String Email;
    @SerializedName("DOB")
    private String DOB;
    @SerializedName("PartnerPan_No")
    private String PartnerPan_No;
    @SerializedName("Country")
    private String Country;
    @SerializedName("State")
    public String State;
    @SerializedName("City")
    public String City;
    @SerializedName("PostalCode")
    private String PostalCode;

    public EPayPartnerModel(int customerId, String partnerFristName, String partnerLastName, String gender, String mobile, String whatsAppNumber, String email, String DOB, String partnerPan_No, String country, String state, String city, String postalCode) {
        CustomerId = customerId;
        PartnerFristName = partnerFristName;
        PartnerLastName = partnerLastName;
        Gender = gender;
        Mobile = mobile;
        WhatsAppNumber = whatsAppNumber;
        Email = email;
        this.DOB = DOB;
        PartnerPan_No = partnerPan_No;
        Country = country;
        State = state;
        City = city;
        PostalCode = postalCode;
    }
}