package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CartDealResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CompanyWheelConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingCartViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getUdhaarOverDueLiveData = SingleLiveEvent<JsonObject>()
    val getUdhaarOverDueData: SingleLiveEvent<JsonObject>
        get() = getUdhaarOverDueLiveData

    private val minOrderAmountLiveData = SingleLiveEvent<Response<Double>>()
    val getMinOrderAmountData: SingleLiveEvent<Response<Double>>
        get() = minOrderAmountLiveData

    private val generateLeadLiveData = SingleLiveEvent<JsonObject>()
    val generateLeadData: SingleLiveEvent<JsonObject>
        get() = generateLeadLiveData

    private val shoppingCartResponseLiveData = SingleLiveEvent<Response<CheckoutCartResponse>>()
    val shoppingCartResponseData: SingleLiveEvent<Response<CheckoutCartResponse>>
        get() = shoppingCartResponseLiveData

    private val companyWheelConfigLiveData = SingleLiveEvent<Response<CompanyWheelConfig>>()
    val getCompanyWheelConfigData: SingleLiveEvent<Response<CompanyWheelConfig>>
        get() = companyWheelConfigLiveData

    private val cartDelResponseLiveData = SingleLiveEvent<JsonObject>()
    val getCartDelResponseData: SingleLiveEvent<JsonObject>
        get() = cartDelResponseLiveData

    private val clearCartItemLiveData = SingleLiveEvent<JsonObject>()
    val getClearCartItemData: SingleLiveEvent<JsonObject>
        get() = clearCartItemLiveData

    private val offerItemLiveData = SingleLiveEvent<Response<ArrayList<ItemListModel>>>()
    val getOfferItemData: SingleLiveEvent<Response<ArrayList<ItemListModel>>>
        get() = offerItemLiveData

    private val cartDealItemLiveData = SingleLiveEvent<Response<CartDealResponse>>()
    val getCartDealItemData: SingleLiveEvent<Response<CartDealResponse>>
        get() = cartDealItemLiveData

    private val addItemInCartLiveData = SingleLiveEvent<Response<JsonObject>>()
    val addItemInCartData: SingleLiveEvent<Response<JsonObject>>
        get() = addItemInCartLiveData

    fun getUdhaarOverDue(customerId: Int, lang: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getUdhaarOverDue(customerId, lang)
            getUdhaarOverDueLiveData.postValue(result.body()!!)
        }
    }


    fun getMinOrderAmount(customerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            minOrderAmountLiveData.postValue(Response.Loading())
            val result = repository.getMinOrderAmount(customerId)
            if (result.body() != null) {
                minOrderAmountLiveData.postValue(Response.Success(result.body()))
            } else {
                minOrderAmountLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.no_response)))
            }
        } else {
            minOrderAmountLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun generateLead(url: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.generateLead(url)
                if (result?.body()!=null){
                    generateLeadLiveData.postValue(result.body())
                }
            }
        }


    fun getCustomerCart(customerId: Int, warehouseId: Int, lang: String, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                shoppingCartResponseLiveData.postValue(Response.Loading())
                val result = repository.getCustomerCart(customerId, warehouseId, lang, mSectionType)
                if (result.body() != null) {
                    shoppingCartResponseLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    shoppingCartResponseLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                shoppingCartResponseLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getCompanyWheelConfig(warehouseId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                companyWheelConfigLiveData.postValue(Response.Loading())
                val result = repository.getCompanyWheelConfig(warehouseId)
                if (result.body() != null) {
                    companyWheelConfigLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    companyWheelConfigLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                companyWheelConfigLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getCartDelResponse(cartAddItemModel: CartAddItemModel) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getCartDelResponse(cartAddItemModel)
                if (result.body()!=null){
                    cartDelResponseLiveData.postValue(result.body())
                }
            }
        }

    fun clearCartItem(customerId: Int, warehouseId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.clearCartItem(customerId,warehouseId)
                if (result.body()!=null){
                    clearCartItemLiveData.postValue(result.body())
                }
            }
        }

    fun getOfferItem(customerId: Int, warehouseId: Int,lang: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                offerItemLiveData.postValue(Response.Loading())
                val result = repository.getOfferItem(customerId,warehouseId,lang)
                if (result.body() != null) {
                    offerItemLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    offerItemLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                offerItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getCartDealItem(customerId: Int, warehouseId: Int, skip: Int, take: Int, lang: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                cartDealItemLiveData.postValue(Response.Loading())
                val result = repository.getCartDealItem(customerId,warehouseId,skip,take,lang)
                if (result.body() != null) {
                    cartDealItemLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    cartDealItemLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                cartDealItemLiveData.postValue(
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