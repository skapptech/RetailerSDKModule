package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IssueTicketsModel {
    @Expose
    @SerializedName("CategoryId")
    var CategoryId = 0

    @Expose
    @SerializedName("CategoryName")
    var CategoryName: String? = ""

    @Expose
    @SerializedName("AfterSelectMessage")
    var AfterSelectMessage: String? = ""

    @Expose
    @SerializedName("IsAskQuestion")
    var IsAskQuestion = false

    @Expose
    @SerializedName("Question")
    var Question: String? = ""

    var isSelected: Boolean = false
    var isRadioBtn: Boolean = false

    constructor(CategoryId: Int, CategoryName: String?, IsAskQuestion: Boolean, Question: String?, isSelected: Boolean, isRadioBtn: Boolean) {
        this.CategoryId = CategoryId
        this.CategoryName = CategoryName
        this.IsAskQuestion = IsAskQuestion
        this.Question = Question
        this.isSelected = isSelected
        this.isRadioBtn = isRadioBtn
    }
}
