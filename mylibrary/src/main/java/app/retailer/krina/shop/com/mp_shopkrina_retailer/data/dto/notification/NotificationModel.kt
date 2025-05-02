package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification

import com.google.gson.annotations.SerializedName

class NotificationModel {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("Message")
    var message: String? = null

    @SerializedName("ImageUrl")
    var imageUrl: String? = null

    @SerializedName("NotificationTime")
    var notificationTime: String? = null

    @SerializedName("NotificationCategory")
    var notificationCategory: String? = null

    @SerializedName("NotificationType")
    var notificationType: String? = null

    @SerializedName("ObjectId")
    var objectId = 0

    @SerializedName("notify_type")
    var notifyType: String? = null
}
