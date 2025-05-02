package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IssuesCategoryModel  {
    @Expose
    @SerializedName("Ticketmessage")
    var ticketmessage: String? = ""
    @Expose
    @SerializedName("TicketCategoryDc")
    var category: ArrayList<IssueTicketsModel>? = null

}