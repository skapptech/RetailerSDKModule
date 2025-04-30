package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.ImageObjEntity
import com.google.gson.annotations.SerializedName

class PostModel {
    @SerializedName("imageObj")
    var imageObj: List<ImageObjEntity>? = null

    @SerializedName("PollValue")
    var pollValue: List<PollValueEntity>? = null

    @SerializedName("postType")
    var postType: String? = null

    @SerializedName("imgFileRelativePath")
    var imgFileRelativePath: String? = null

    @SerializedName("imgFileFullpath")
    var imgFileFullPath: String? = null

    @SerializedName("imgFileName")
    var imgFileName: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("desc")
    var desc: String? = null

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("userId")
    var userId = 0

    @SerializedName("postId")
    var postId:String?=null

    class PollValueEntity {
        @SerializedName("ansewrs")
        var answers: List<String>? = null

        @SerializedName("optionsValue")
        var optionsValue: String? = ""
    }
}