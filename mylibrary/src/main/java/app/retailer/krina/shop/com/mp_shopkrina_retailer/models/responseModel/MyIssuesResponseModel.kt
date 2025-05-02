package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyIssuesResponseModel : java.io.Serializable {
    @Expose
    @SerializedName("TicketId")
    var TicketId = 0

    @Expose
    @SerializedName("TicketDescription")
    var TicketDescription: String? = ""

    @Expose
    @SerializedName("CreatedDate")
    var CreatedDate: String? = ""

    @Expose
    @SerializedName("Status")
    var Status: String? = ""

    @Expose
    @SerializedName("TATInHrs")
    var TATInHrs = 0

    @Expose
    @SerializedName("Closeresolution")
    var Closeresolution: String? = ""
}