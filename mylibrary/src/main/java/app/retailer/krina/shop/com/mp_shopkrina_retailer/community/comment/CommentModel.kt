package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommentModel : Serializable {
    @SerializedName("likeStatus")
    var userLikeStatus = false

    @SerializedName("likeCount")
    var likeCount = 0

    @SerializedName("commentcount")
    val commentcount = 0

    @SerializedName("img")
    val img: String? = null

    @SerializedName("imgBasePath")
    val imgBasePath: String? = null

    @SerializedName("comments")
    var comments: String? = null

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("userId")
    var userId: String? = ""

    @SerializedName("_id")
    val _id: String = ""

    @SerializedName("repliesInComment")
    val repliesInComment: ArrayList<CommentModel>? = null

    @SerializedName("postOn")
    var postOn: String? = null

    @SerializedName("isPosted")
    val isPosted = 0

    @SerializedName("isDeleted")
    val isDeleted = false
}