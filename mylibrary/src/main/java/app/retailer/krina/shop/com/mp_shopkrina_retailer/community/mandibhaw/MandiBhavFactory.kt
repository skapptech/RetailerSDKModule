package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class MandiBhavFactory constructor(private val repository:MandiBhavRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return MandiBhavViewModel(repository) as T
    }
}
