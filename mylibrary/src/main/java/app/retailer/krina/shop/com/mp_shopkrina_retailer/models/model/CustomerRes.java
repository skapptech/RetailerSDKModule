package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerRes implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Password")
    public String Password;

    @SerializedName("FCMID")
    public String FCMID;

    @SerializedName("CustId")
    public String CustId;

    @SerializedName("Type")
    public String Type;

    @SerializedName("LastTrancAmount")
    public String LastTrancAmount;

    @SerializedName("LastTrancDate")
    public String LastTrancDate;

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFCMID() {
        return FCMID;
    }

    public void setFCMID(String FCMID) {
        this.FCMID = FCMID;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLastTrancAmount() {
        return LastTrancAmount;
    }

    public void setLastTrancAmount(String lastTrancAmount) {
        LastTrancAmount = lastTrancAmount;
    }

    public String getLastTrancDate() {
        return LastTrancDate;
    }

    public void setLastTrancDate(String lastTrancDate) {
        LastTrancDate = lastTrancDate;
    }

}