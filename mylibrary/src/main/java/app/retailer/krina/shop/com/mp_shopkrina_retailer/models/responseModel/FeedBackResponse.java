package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

public class FeedBackResponse {

    @SerializedName("createdDate")
    private String createddate;

    @SerializedName("satisfactionLevel")
    private int satisfactionlevel;

    @SerializedName("suggestions")
    private String suggestions;

    @SerializedName("shopName")
    private String shopname;

    @SerializedName("Mobile")
    private String mobile;

    @SerializedName("customerId")
    private int customerid;

    @SerializedName("CompanyId")
    private int companyid;

    @SerializedName("feedBackId")
    private int feedbackid;

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public int getSatisfactionlevel() {
        return satisfactionlevel;
    }

    public void setSatisfactionlevel(int satisfactionlevel) {
        this.satisfactionlevel = satisfactionlevel;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(int feedbackid) {
        this.feedbackid = feedbackid;
    }
}