package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubSubCategoriesModel : Serializable {
    @JvmField
    var isChecked = false

    @JvmField
    @SerializedName("SubsubCategoryid")
    var subsubcategoryid = 0

    @SerializedName("SubsubcategoryName")
    var subsubcategoryname: String? = null

    @SerializedName("BaseCategoryId")
    var basecategoryid = 0

    @SerializedName("Categoryid")
    var categoryid = 0

    @SerializedName("SubCategoryId")
    var subcategoryid = 0

    @SerializedName("LogoUrl")
    var logourl: String? = null

    @SerializedName("itemcount")
    var itemcount = 0

    constructor()
    constructor(
        subsubcategoryname: String?,
        baseCatId: Int,
        categoryId: Int,
        subCatId: Int,
        subSubCatId: Int
    ) {
        this.subsubcategoryname = subsubcategoryname
        basecategoryid = baseCatId
        categoryid = categoryId
        subcategoryid = subCatId
        subsubcategoryid = subSubCatId
    }
}
