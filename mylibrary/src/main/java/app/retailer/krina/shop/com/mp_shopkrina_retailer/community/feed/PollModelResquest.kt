package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

data class PollModelResquest(
    var userId: String,
    var submissionTime: String,
    var pollId: String,
    var postId: String
)