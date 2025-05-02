package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository

class PaymentViewModelFactory(val app: Application, private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(app,repository) as T
    }
}