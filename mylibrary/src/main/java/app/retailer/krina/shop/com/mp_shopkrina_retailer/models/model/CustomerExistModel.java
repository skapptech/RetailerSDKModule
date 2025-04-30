package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

public class CustomerExistModel {

    @SerializedName("Id")
    private String Id;


    @SerializedName("Mobile")
    private String Mobile;


    @SerializedName("Name")
    private String Name;


    @SerializedName("Email")
    private String Email;


    @SerializedName("Address")
    private String Address;

    @SerializedName("CustId")
    private int CustId;

    @SerializedName("City")
    private String City;


    @SerializedName("State")
    private String State;

    @SerializedName("Country")
    private String Country;

    @SerializedName("BusinessType")
    private String BusinessType;

    @SerializedName("Isregistered")
    private boolean Isregistered;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getCustId() {
        return CustId;
    }

    public void setCustId(int custId) {
        CustId = custId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public boolean isIsregistered() {
        return Isregistered;
    }

    public void setIsregistered(boolean isregistered) {
        Isregistered = isregistered;
    }
}
