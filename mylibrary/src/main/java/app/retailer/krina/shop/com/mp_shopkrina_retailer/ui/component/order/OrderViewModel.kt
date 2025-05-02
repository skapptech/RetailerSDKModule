package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.PaylaterLimitsResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val orderLiveData = SingleLiveEvent<Response<ArrayList<ConformOrderModel>>>()
    val orderData: SingleLiveEvent<Response<ArrayList<ConformOrderModel>>>
        get() = orderLiveData

    private val payLaterLimitLiveData = SingleLiveEvent<Response<PaylaterLimitsResponseModel>>()
    val payLaterLimitData: SingleLiveEvent<Response<PaylaterLimitsResponseModel>>
        get() = payLaterLimitLiveData


    fun getOrdersWithPages(customerId: Int, page: Int, total: Int, type: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication())) {
                orderLiveData.postValue(Response.Loading())
                val result = repository.getOrdersWithPages(customerId, page, total, type)
                if (result != null) {
                    orderLiveData.postValue(Response.Success(result.body()))
                } else {
                    orderLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                orderLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }

    fun getPayLaterLimits(custId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication())) {
                payLaterLimitLiveData.postValue(Response.Loading())
                val result = repository.getPayLaterLimits(custId)
                if (result != null) {
                    payLaterLimitLiveData.postValue(Response.Success(result.body()))
                } else {
                    payLaterLimitLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                payLaterLimitLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }
    }
}