package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class MyAgentModel {
    @SerializedName("SalesPersonId")
    var salesPersonId: String? = ""

    @SerializedName("SalesPersonName")
    var salesPersonName: String? = ""

    @SerializedName("GroupName")
    var groupName: String? = ""

    @SerializedName("MobileNumber")
    var mobileNumber: String? = ""

    @SerializedName("profileImg")
    var profileImg: String? = ""
}