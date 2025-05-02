package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DialModel;

class WheelDetails {

    @SerializedName("WheelPlayedCount")
    public String WheelPlayedCount;

    @SerializedName("WheelPlayedEarnigPoint")
    public String WheelPlayedEarnigPoint;

    @SerializedName("OrderId")
    public String order;

    public String getWheelPlayedCount() {
        return WheelPlayedCount;
    }

    public void setWheelPlayedCount(String wheelPlayedCount) {
        WheelPlayedCount = wheelPlayedCount;
    }

    public String getWheelPlayedEarnigPoint() {
        return WheelPlayedEarnigPoint;
    }

    public void setWheelPlayedEarnigPoint(String wheelPlayedEarnigPoint) {
        WheelPlayedEarnigPoint = wheelPlayedEarnigPoint;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
