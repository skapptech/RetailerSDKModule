package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostFactory constructor(private val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostViewModel(repository) as T
    }
}
