package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels;

public class CityIdModel {


    private String CityId;
    private int CustomerId;
    private String FcmId;

    public CityIdModel(int customerId, String cityId, String fcmId) {
        CityId = cityId;
        CustomerId = customerId;
        FcmId = fcmId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }
}
