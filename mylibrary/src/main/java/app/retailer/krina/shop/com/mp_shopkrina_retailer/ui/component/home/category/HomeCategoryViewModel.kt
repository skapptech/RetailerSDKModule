package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.HomeCategoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeCategoryViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getCategoryLiveData = SingleLiveEvent<Response<HomeCategoryResponse>>()
    val getCategoryData: SingleLiveEvent<Response<HomeCategoryResponse>>
        get() = getCategoryLiveData


    fun getCategory(
        customerId: Int,
        wId: Int,
        lang: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getCategoryLiveData.postValue(Response.Loading())
                val result = repository.getCategory(customerId, wId, lang)
                if (result.body() != null) {
                    getCategoryLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getCategoryLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getCategoryLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }
}