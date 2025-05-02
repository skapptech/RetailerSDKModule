package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentFactory constructor(private val repository: CommentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentViewModel(repository) as T
    }
}
