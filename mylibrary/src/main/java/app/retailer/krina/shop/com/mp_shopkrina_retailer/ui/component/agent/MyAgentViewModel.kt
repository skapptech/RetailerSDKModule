package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.agent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyAgentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAgentViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val myAgentLiveData = SingleLiveEvent<Response<ArrayList<MyAgentModel>>>()
    val myAgentData: SingleLiveEvent<Response<ArrayList<MyAgentModel>>>
        get() = myAgentLiveData


    fun getCustomerSalesPersons(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication())) {
                myAgentLiveData.postValue(Response.Loading())
                val result = repository.getCustomerSalesPersons(customerId)
                if (result != null) {
                    myAgentLiveData.postValue(Response.Success(result.body()))
                } else {
                    myAgentLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                myAgentLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }
}