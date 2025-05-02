package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.freebies.OfferDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FreebiesViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {


    private val getFreebiesItemLiveData = SingleLiveEvent<Response<OfferDataModel>>()
    val getFreebiesItemData: SingleLiveEvent<Response<OfferDataModel>>
        get() = getFreebiesItemLiveData


    private val addItemInCartLiveData = SingleLiveEvent<Response<JsonObject>>()
    val addItemInCartData: SingleLiveEvent<Response<JsonObject>>
        get() = addItemInCartLiveData

    fun getFreebiesItem(
        customerId: Int,
        warehouseId: Int,
        lang: String,
        subCatId: Int,
        isStore: Boolean
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getFreebiesItemLiveData.postValue(Response.Loading())
                val result = if (isStore) {
                    repository.getStoreFreebies(customerId, warehouseId, subCatId, lang)
                } else {
                    repository.getFreebiesItem(customerId, warehouseId, lang)
                }
                if (result?.body() != null) {
                    getFreebiesItemLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getFreebiesItemLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                getFreebiesItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun addItemIncCart(cartAddItemModel: CartAddItemModel?, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                addItemInCartLiveData.postValue(Response.Loading())
                val result = repository.getCartResponse(cartAddItemModel, mSectionType)
                if (result?.body() != null) {
                    addItemInCartLiveData.postValue(Response.Success(result.body()))
                } else {
                    addItemInCartLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                addItemInCartLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }


}