package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

import com.google.gson.annotations.SerializedName;

public class LatLongModel {
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lg")
    private double lg;
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @SerializedName("ShippingAddress1")
    private String ShippingAddress1;
    public LatLongModel(int customerId, double lat, double lg, String shippingAddress,String shippingAddress1) {
        CustomerId = customerId;
        this.lat = lat;
        this.lg = lg;
        ShippingAddress = shippingAddress;
        ShippingAddress1 = shippingAddress1;
    }

    @SerializedName("BillingAddress")
    private String BillingAddress;
    @SerializedName("BillingCity")
    private String BillingCity;
    @SerializedName("BillingState")
    private String BillingState;
    @SerializedName("BillingZipCode")
    private String BillingZipCode;

    public LatLongModel(int customerId, String billingAddress, String billingCity, String billingState, String billingZipCode) {
        CustomerId = customerId;
        BillingAddress = billingAddress;
        BillingCity = billingCity;
        BillingState = billingState;
        BillingZipCode = billingZipCode;
    }
}