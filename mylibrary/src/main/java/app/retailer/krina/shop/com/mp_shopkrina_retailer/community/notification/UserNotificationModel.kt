package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

data class UserNotificationModel(
    var NotificationId: String,
    var IsRead: Boolean,
    var NotificationType: Int,
    var createdAt: String,
    var UserId: String,
    var img: String?,
    var imgBasePath: String,
    var userName: String,
    var city: String,
    var ShopName: String,
    var Summary: String,
    var Heading: String,
    var Subheading: String,
    var NewPost: NewPostNotificationModel,
    var Comment: CommentNotificationModel,
    var Like: LikeNotificationModel,
)

data class NewPostNotificationModel(var PostedByUserId: String,
                                    var PostId: String)
data class LikeNotificationModel(var LikeByUserId: String,
                                    var PostId: String)
data class CommentNotificationModel(var CommentId: String,
                                    var PostId: String,var CommentedByUserId:String)
