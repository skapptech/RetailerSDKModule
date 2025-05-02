package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

public class FeedbackModel {
    private String Mobile, shopName, suggestions;
    private int satisfactionLevel, customerId;
    private float rating;

    public FeedbackModel(String shopName, int customerId, String mobileNumber, String suggestions, float rating) {
        this.shopName = shopName;
        this.customerId = customerId;
        this.Mobile = mobileNumber;
        this.suggestions = suggestions;
        this.rating = rating;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(int satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }

    public String getShopName() {

        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }


}
