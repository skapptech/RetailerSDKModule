package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackOrderModel {
    @SerializedName("CollectionName")
    public String collectionName;
    @SerializedName("TripId")
    public int TripId;
    @SerializedName("TripPlannerVehicleId")
    public int TripPlannerVehicleId;
    @SerializedName("CustomerId")
    public int CustomerId;
    @SerializedName("DBoyId")
    public int DBoyId;
    @SerializedName("DBoyName")
    public String DBoyName;
    @SerializedName("ShippingAddress")
    public String ShippingAddress;
    @SerializedName("Lat")
    public double Lat;
    @SerializedName("Lng")
    public double Lng;
    @SerializedName("Orders")
    public List<TrackOrdersDetails> trackOrdersDetails;
    @SerializedName("DboyProfilePic")
    public String DboyProfilePic;
    @SerializedName("DboyMobile")
    public String DboyMobile;
    @SerializedName("DeliveryBoyRating")
    public String DeliveryBoyRating;


    public int getTripId() {
        return TripId;
    }

    public void setTripId(int tripId) {
        TripId = tripId;
    }

    public int getTripPlannerVehicleId() {
        return TripPlannerVehicleId;
    }

    public void setTripPlannerVehicleId(int tripPlannerVehicleId) {
        TripPlannerVehicleId = tripPlannerVehicleId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getDBoyId() {
        return DBoyId;
    }

    public void setDBoyId(int DBoyId) {
        this.DBoyId = DBoyId;
    }

    public String getDBoyName() {
        return DBoyName;
    }

    public void setDBoyName(String DBoyName) {
        this.DBoyName = DBoyName;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public List<TrackOrdersDetails> getTrackOrdersDetails() {
        return trackOrdersDetails;
    }

    public void setTrackOrdersDetails(List<TrackOrdersDetails> trackOrdersDetails) {
        this.trackOrdersDetails = trackOrdersDetails;
    }

    public String getDboyProfilePic() {
        return DboyProfilePic;
    }

    public void setDboyProfilePic(String dboyProfilePic) {
        DboyProfilePic = dboyProfilePic;
    }

    public String getDboyMobile() {
        return DboyMobile;
    }

    public void setDboyMobile(String dboyMobile) {
        DboyMobile = dboyMobile;
    }

    public String getDeliveryBoyRating() {
        return DeliveryBoyRating;
    }

    public void setDeliveryBoyRating(String deliveryBoyRating) {
        DeliveryBoyRating = deliveryBoyRating;
    }
}
