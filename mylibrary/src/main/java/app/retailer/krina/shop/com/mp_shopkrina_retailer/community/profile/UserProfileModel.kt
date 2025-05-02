package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import java.io.Serializable

data class UserProfileModel(
    var CustomerId: String,
    var Name: String?,
    var ShopName: String,
    var Mobile: String,
    var totalFollowings: Int,
    var totalFollowers: Int,
    var isFollowed: Boolean,
    var followId: String,
    var img: String,
    var City: String,
    var State: String
) : Serializable


data class UserLikeListModel(
    var userId: String, var _id: String, var createdDate: String,
    var imgBasePath: String,
    var img: String,
    var userName: String,
    var isPosted: Int
)


data class UserFollowingModel(var followerId: String, var followeeId: String)
data class FollowingResponceModel(var status: Boolean, var res: FollowingModel)
data class FollowingModel(
    var _id: String,
    var followerId: String,
    var followeeId: String,
    var createdAt: String,
    var updatedAt: String,
    var __v: Int
)

data class UserUpdate(
    var userId: String,
    var Name: String,
    var ShopName: String,
    var City: String,
    var state: String,
    var imgBasePath: String,
    var imgFileFullpath: String,
)



