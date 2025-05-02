package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model

import com.google.gson.annotations.SerializedName

class DocTypeModel(
    @field:SerializedName("Id")
    var id: Int,
    @field:SerializedName("DocType")
    var docType: String
)
