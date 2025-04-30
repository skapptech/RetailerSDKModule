package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import ReturnOrderViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository

class ReturnOrderViewModelFactory(val app: Application, private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReturnOrderViewModel(app,repository) as T
    }
}