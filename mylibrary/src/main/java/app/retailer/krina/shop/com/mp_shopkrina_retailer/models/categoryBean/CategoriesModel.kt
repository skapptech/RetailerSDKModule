package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoriesModel(
    @field:SerializedName("CategoryName")
    var categoryname: String, @field:SerializedName(
        "Categoryid"
    ) var categoryid: Int
) : Serializable {
    @SerializedName("LogoUrl")
    var logourl: String? = null

    @SerializedName("CategoryImg")
    var categoryImg: String? = null

}