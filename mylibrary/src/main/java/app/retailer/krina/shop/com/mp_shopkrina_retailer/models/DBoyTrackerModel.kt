package app.retailer.krina.shop.com.mp_shopkrina_retailer.models

import com.google.gson.annotations.SerializedName
import java.util.*

class DBoyTrackerModel {
    @SerializedName("CurrentServingOrderId")
    var currentServingOrderId = 0

    @SerializedName("Lat")
    var lat = 0.0

    @SerializedName("Lng")
    var lng = 0.0

    @SerializedName("RecordStatus")
    var recordStatus = 0

    @SerializedName("RecordTime")
    var recordTime: Date? = null

    @SerializedName("TripPlannerConfirmedDetailId")
    var tripPlannerConfirmedDetailId = 0

    @SerializedName("TripPlannerVehicleId")
    var tripPlannerVehicleId = 0
}