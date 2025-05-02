package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository

class AllBrandViewModelFactory(val app: Application, private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllBrandViewModel(app,repository) as T
    }
}