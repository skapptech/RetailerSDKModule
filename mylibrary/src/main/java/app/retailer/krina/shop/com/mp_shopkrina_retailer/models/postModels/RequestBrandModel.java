package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

public class RequestBrandModel {


    private String customerMobile,requestedBrand;
    private int customerId;

    public RequestBrandModel( int customerId, String customerMobile, String requestedBrand) {
        this.customerId = customerId;
        this.customerMobile = customerMobile;
        this.requestedBrand = requestedBrand;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getRequestedBrand() {
        return requestedBrand;
    }

    public void setRequestedBrand(String requestedBrand) {
        this.requestedBrand = requestedBrand;
    }
}
