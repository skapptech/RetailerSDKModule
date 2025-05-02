package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

data class FeedRequestModel(
    var skip: Int,
    var take: Int,
    var userId: String,
    var userId_str: String,
    var isGetUserPost: Boolean,
    var otherUserId: String,
    var otherUserId_str: String,
    var postId: String
)
