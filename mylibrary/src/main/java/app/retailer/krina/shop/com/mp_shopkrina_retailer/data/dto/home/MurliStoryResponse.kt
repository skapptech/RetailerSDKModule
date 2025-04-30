package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MurliStoryResponse  : Serializable {
    @SerializedName("MurliStoryPageList")
    var murliStoryPageList: ArrayList<MurliStoryPageListBean>? = null

    @Expose
    @SerializedName("Title")
    var title: String? = null

    @Expose
    @SerializedName("Id")
    var id = 0

     class MurliStoryPageListBean : Serializable{
        @SerializedName("MurliStoryId")
        var murliStoryId= 0

        @SerializedName("ImagePath")
        var imagePath: String?=null

        @SerializedName("PageNumber")
        var pageNumber = 0

        @SerializedName("Id")
        var id = 0

        constructor(murliStoryId: Int, imagePath: String?, id: Int, pageNumber: Int) {
            this.murliStoryId = murliStoryId
            this.imagePath = imagePath
            this.pageNumber = pageNumber
            this.id = id
        }
    }
}