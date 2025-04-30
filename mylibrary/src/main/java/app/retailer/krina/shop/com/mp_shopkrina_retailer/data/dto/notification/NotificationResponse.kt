package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification

import com.google.gson.annotations.SerializedName

class NotificationResponse {
    @SerializedName("total_count")
    var totalCount: String? = null

    @SerializedName("notificationmaster")
    var notificationListBeans: List<NotificationModel>? = null
}
