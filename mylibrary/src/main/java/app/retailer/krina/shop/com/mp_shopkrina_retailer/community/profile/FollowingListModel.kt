package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

data class FollowingListModel(var status: Boolean, var res: ArrayList<ListModel>)

data class ListModel(
    var _id: String,
    var followerId: String,
    var followeeId: String,
    var createdAt: String,
    var updatedAt: String,
    var img: String,
    var imgBasePath: String,
    var userName: String,
    var city: String,
    var ShopName: String,
    var __v: Int
)