package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedFactory constructor(private val repository: FeedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedViewModel(repository) as T
    }
}
