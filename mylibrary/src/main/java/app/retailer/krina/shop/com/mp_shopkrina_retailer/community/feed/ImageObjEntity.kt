package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageObjEntity() : Serializable {

    @SerializedName("imgFileRelativePath")
    var imgFileRelativePath: String? = null

    @SerializedName("imgFileFullpath")
    var imgFileFullPath: String? = null

    @SerializedName("imgFileName")
    var imgFileName: String? = null

    constructor(imgFileFullPath: String) : this() {
        this.imgFileFullPath = imgFileFullPath
    }
}