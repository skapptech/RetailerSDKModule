package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyUdharPojo implements Serializable {

    @SerializedName("CustomerId")
    private String CustomerId;
    @SerializedName("PanCardurl")
    private String PanCardurl;
    @SerializedName("AddressProofUrl")
    private String AddressProofUrl;
    @SerializedName("AnnualTurnOver")
    private String AnnualTurnOver;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("BusinessVintage")
    private String BusinessVintage;
    @SerializedName("Deleted")
    private boolean Deleted;
    @SerializedName("BackImageUrl")
    private String BackImageUrl;
    @SerializedName("PanCardNo")
    private String PanCardNo;
    @SerializedName("othercustdoc")
    private String othercustdoc;

    public MyUdharPojo(String customerId, String panCardurl, String addressProofUrl, String annualTurnOver, String gender, String businessVintage, boolean deleted, String backImageUrl, String panCardNo, String othercustdoc) {
        CustomerId = customerId;
        PanCardurl = panCardurl;
        AddressProofUrl = addressProofUrl;
        AnnualTurnOver = annualTurnOver;
        Gender = gender;
        BusinessVintage = businessVintage;
        Deleted = deleted;
        BackImageUrl = backImageUrl;
        PanCardNo = panCardNo;
        this.othercustdoc = othercustdoc;
    }

    public String getOthercustdoc() {
        return othercustdoc;
    }

    public void setOthercustdoc(String othercustdoc) {
        this.othercustdoc = othercustdoc;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getPanCardurl() {
        return PanCardurl;
    }

    public void setPanCardurl(String panCardurl) {
        PanCardurl = panCardurl;
    }

    public String getAddressProofUrl() {
        return AddressProofUrl;
    }

    public void setAddressProofUrl(String addressProofUrl) {
        AddressProofUrl = addressProofUrl;
    }

    public String getAnnualTurnOver() {
        return AnnualTurnOver;
    }

    public void setAnnualTurnOver(String annualTurnOver) {
        AnnualTurnOver = annualTurnOver;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBusinessVintage() {
        return BusinessVintage;
    }

    public void setBusinessVintage(String businessVintage) {
        BusinessVintage = businessVintage;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getBackImageUrl() {
        return BackImageUrl;
    }

    public void setBackImageUrl(String backImageUrl) {
        BackImageUrl = backImageUrl;
    }

    public String getPanCardNo() {
        return PanCardNo;
    }

    public void setPanCardNo(String panCardNo) {
        PanCardNo = panCardNo;
    }
}
