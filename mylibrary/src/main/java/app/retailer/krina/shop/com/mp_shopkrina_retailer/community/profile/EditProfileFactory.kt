package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class EditProfileFactory constructor(private val repository:EditProfileRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return EditProfileViewMode(repository) as T
    }
}
