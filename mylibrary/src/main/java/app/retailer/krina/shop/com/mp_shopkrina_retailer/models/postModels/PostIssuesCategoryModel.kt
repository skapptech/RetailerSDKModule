package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostIssuesCategoryModel  {
    @Expose
    @SerializedName("CategoryId")
    var categoryId = 0

    @Expose
    @SerializedName("Type")
    var type = 0

    @Expose
    @SerializedName("CreatedBy")
    var CreatedBy = 0

    @Expose
    @SerializedName("CategoryAnsware")
    var categoryAnsware: String? = ""

    @Expose
    @SerializedName("TicketDescription")
    var ticketDescription: String? = ""

    @Expose
    @SerializedName("Language")
    var Language: String? = ""

    constructor(categoryId: Int, type: Int, CreatedBy: Int, categoryAnsware: String?, ticketDescription: String?,language: String?) {
        this.categoryId = categoryId
        this.type = type
        this.CreatedBy = CreatedBy
        this.categoryAnsware = categoryAnsware
        this.ticketDescription = ticketDescription
        this.Language = language
    }
}
