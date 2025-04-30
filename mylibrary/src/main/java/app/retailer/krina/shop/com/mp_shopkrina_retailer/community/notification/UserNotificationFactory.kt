package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserNotificationFactory constructor(private val repository:UserNotificationRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return UserNotificationViewModel(repository) as T
    }
}
