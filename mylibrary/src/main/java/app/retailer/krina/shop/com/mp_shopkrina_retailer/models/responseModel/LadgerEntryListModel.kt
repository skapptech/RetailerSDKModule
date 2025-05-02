package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class LadgerEntryListModel :Serializable   {
    @SerializedName("ID")
    var id = 0
    @SerializedName("Date")
    var date: String? = ""
    @SerializedName("Particulars")
    var particulars: String? = ""
    @SerializedName("LagerID")
    var lagerID = 0
    @SerializedName("VouchersTypeID")
    var vouchersTypeID = 0
    @SerializedName("VouchersNo")
    var vouchersNo = 0
    @SerializedName("Debit")
    var debit = 0.0
    @SerializedName("Credit")
    var credit = 0.0
    @SerializedName("ObjectID")
    var objectID: String? = ""
    @SerializedName("ObjectType")
    var objectType: String? = ""
    @SerializedName("AffectedLadgerID")
    var affectedLadgerID = 0
    @SerializedName("IsActive")
    var isActive = false
    @SerializedName("CreatedBy")
    var createdBy = 0
    @SerializedName("CreatedDate")
    var createdDate: String? = ""
    @SerializedName("UpdatedBy")
    var updatedBy = 0
    @SerializedName("UpdatedDate")
    var updatedDate: String? = ""
    @SerializedName("LadgerName")
    var ladgerName: String? = ""
    @SerializedName("VoucherName")
    var voucherName: String? = ""
    @SerializedName("DayBalance")
    var dayBalance = 0.0
    @SerializedName("AffactedLadgerName")
    var affactedLadgerName: String? = ""


}