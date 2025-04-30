package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentModel
import com.google.gson.annotations.SerializedName

class CommentPostModel {
    @SerializedName("userLikeStatus")
    var userLikeStatus = false

    @SerializedName("likeCount")
    var likeCount = 0

    @SerializedName("replyCount")
    val replyCount = 0

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
    val _id: String? = null

    @SerializedName("repliesInComment")
    val repliesInComment: ArrayList<CommentModel>? = null

    @SerializedName("likesInComment")
    val likesInComment: ArrayList<CommentModel>? = null

    @SerializedName("postOn")
    var postOn: String? = null

    @SerializedName("PostId")
    var PostId: String? = null

    @SerializedName("isPosted")
    val isPosted = 0

    @SerializedName("isDeleted")
    val isDeleted = false
}