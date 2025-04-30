package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment.CommentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DataConverterImage
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DataConverterPoll
import java.io.Serializable

data class FeedModel(val list: ArrayList<FeedPostModel>, val count: Int)

@Entity(tableName = "Feed")
data class FeedPostModel(
    @PrimaryKey(autoGenerate = false)
    var postId: String,
    var userId: String,
    var userName: String?,
    var title: String,
    var desc: String?,
    var imgFileFullpath: String?,
    var postType: String?,
    var commentCount: Int,
    var likeStatus: Boolean,
    var likeCount: Int,
    var isPollAnswerGiven: Boolean,
    var createdDate: String,
    var ShopName: String?,
    var img: String?,
    var imgBasePath: String,
    var city: String,
    var isFollowed: Boolean,
    @TypeConverters(DataConverterPoll::class)
    var pollValue: ArrayList<PollModel>?,
    @TypeConverters(DataConverterImage::class)
    var imageObj: ArrayList<ImageObjEntity>? = null,
    @Ignore
    var comment: ArrayList<CommentModel>? = null
) : Serializable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        false,
        0,
        false,
        "",
        "",
        "",
        "",
        "",
        false,
        null,
        null,
        null
    )
}