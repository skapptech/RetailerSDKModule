package app.retailer.krina.shop.com.mp_shopkrina_retailer.community

import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel

interface FeedListener {
    fun openProfile(postId: String)
    fun following(userID: String)
    fun likePost(postId: String, like: Int, likeCount: Int, postType: String?)
    fun openComments(model: FeedPostModel)
    fun sharePost(model: FeedPostModel)
    fun likeList(model: FeedPostModel)
    fun editPost(postId: FeedPostModel)
}