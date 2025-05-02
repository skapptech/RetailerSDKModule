package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class CustomerHoliday {
    @SerializedName("Id")
    var id = 0
    @SerializedName("warehouseid")
    var warehouseId = 0
    @SerializedName("clusterId")
    var clusterId = 0
    @SerializedName("skCode")
    var skCode = ""
    @SerializedName("holiday")
    var holiday = emptyArray<String>()
    @SerializedName("year")
    var year = 0
}