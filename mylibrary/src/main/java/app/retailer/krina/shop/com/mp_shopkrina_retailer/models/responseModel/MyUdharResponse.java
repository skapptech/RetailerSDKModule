package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyUdharResponse implements Serializable {

    @SerializedName("Status")
    private boolean Status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("MyudharId")
    private int MyudharId;

    @SerializedName("CustomerId")
    private int CustomerId;

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


    @SerializedName("CreatedDate")
    private String CreatedDate;

    @SerializedName("UpdatedDate")
    private String UpdatedDate;

    @SerializedName("Deleted")
    private boolean Deleted;

    public MyUdharResponse(int myudharId, int customerId, String panCardurl, String addressProofUrl, String annualTurnOver, String gender, String businessVintage, String createdDate, String updatedDate, boolean deleted) {
        MyudharId = myudharId;
        CustomerId = customerId;
        PanCardurl = panCardurl;
        AddressProofUrl = addressProofUrl;
        AnnualTurnOver = annualTurnOver;
        Gender = gender;
        BusinessVintage = businessVintage;
        CreatedDate = createdDate;
        UpdatedDate = updatedDate;
        Deleted = deleted;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getMyudharId() {
        return MyudharId;
    }

    public void setMyudharId(int myudharId) {
        MyudharId = myudharId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
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

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }
}
