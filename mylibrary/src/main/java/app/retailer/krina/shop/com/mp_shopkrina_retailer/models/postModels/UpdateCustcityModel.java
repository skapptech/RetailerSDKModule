package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class UpdateCustcityModel {

    @SerializedName("Cityid")
    private String custerID;

    @SerializedName("Mobile")
    private String mobileNumber;

    @SerializedName("CompanyId")
    private int CompanyId;

    @SerializedName("CustomerId")
    private int CustomerId;

    public UpdateCustcityModel(String custerID, String mobileNumber, int companyId, int customerId) {
        this.custerID = custerID;
        this.mobileNumber = mobileNumber;
        CompanyId = companyId;
        CustomerId = customerId;
    }

    public String getCusterID() {
        return custerID;
    }

    public void setCusterID(String custerID) {
        this.custerID = custerID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }


}
