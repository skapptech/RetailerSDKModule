package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

data class PostLikeModelRequest(var userId: String, val postId: String, var likeStatus: Int)