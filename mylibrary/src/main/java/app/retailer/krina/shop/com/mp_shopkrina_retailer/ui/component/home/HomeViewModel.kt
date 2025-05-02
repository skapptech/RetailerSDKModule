package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.BottomCall
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.MurliStoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class HomeViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getUdhaarOverDueLiveData = SingleLiveEvent<JsonObject>()
    val getUdhaarOverDueData: SingleLiveEvent<JsonObject>
        get() = getUdhaarOverDueLiveData

    private val shoppingCartResponseLiveData = SingleLiveEvent<Response<CheckoutCartResponse>>()
    val shoppingCartResponseData: SingleLiveEvent<Response<CheckoutCartResponse>>
        get() = shoppingCartResponseLiveData

    private val getWalletPointLiveData = SingleLiveEvent<MyWalletResponse>()
    val getWalletPointData: SingleLiveEvent<MyWalletResponse>
        get() = getWalletPointLiveData

    private val appHomeBottomLiveData = SingleLiveEvent<Response<ArrayList<BottomCall>>>()
    val appHomeBottomData: SingleLiveEvent<Response<ArrayList<BottomCall>>>
        get() = appHomeBottomLiveData

    private val getGullakBalanceLiveData = SingleLiveEvent<JsonObject>()
    val getGullakBalanceData: SingleLiveEvent<JsonObject>
        get() = getGullakBalanceLiveData

    private val getRTGSBalanceLiveData = SingleLiveEvent<Double>()
    val getRTGSBalanceData: SingleLiveEvent<Double>
        get() = getRTGSBalanceLiveData

    private val getVATMUrlLiveData = SingleLiveEvent<String>()
    val getVATMUrlData: SingleLiveEvent<String>
        get() = getVATMUrlLiveData

    private val getNotifyItemsLiveData = SingleLiveEvent<Boolean>()
    val getNotifyItemsData: SingleLiveEvent<Boolean>
        get() = getNotifyItemsLiveData

    private val generateLeadLiveData = SingleLiveEvent<JsonObject>()
    val generateLeadData: SingleLiveEvent<JsonObject>
        get() = generateLeadLiveData

    private val scaleUpLeadInitiateLiveData = SingleLiveEvent<Response<JsonObject>>()
    val scaleUpLeadInitiateData: SingleLiveEvent<Response<JsonObject>>
        get() = scaleUpLeadInitiateLiveData

    private val addItemInCartLiveData = SingleLiveEvent<Response<JsonObject>>()
    val addItemInCartData: SingleLiveEvent<Response<JsonObject>>
        get() = addItemInCartLiveData

    private val getMurliAudioForMobileLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getMurliAudioForMobileData: SingleLiveEvent<Response<JsonObject>>
        get() = getMurliAudioForMobileLiveData

    private val downloadFileWithUrlLiveData = SingleLiveEvent<Response<ResponseBody>>()
    val downloadFileWithUrlData: SingleLiveEvent<Response<ResponseBody>>
        get() = downloadFileWithUrlLiveData

    private val getMurliPublishedStoryLiveData = SingleLiveEvent<Response<MurliStoryResponse>>()
    val getMurliPublishedStoryData: SingleLiveEvent<Response<MurliStoryResponse>>
        get() = getMurliPublishedStoryLiveData

    private val getDboyRatingOrderLiveData = SingleLiveEvent<ArrayList<RatingModel>>()
    val getDboyRatingOrderData: SingleLiveEvent<ArrayList<RatingModel>>
        get() = getDboyRatingOrderLiveData

    private val getDboyRatingOrderOtherLiveData = SingleLiveEvent<RatingModel>()
    val getDboyRatingOrderOtherData: SingleLiveEvent<RatingModel>
        get() = getDboyRatingOrderOtherLiveData

    private val addRatingLiveData = SingleLiveEvent<Boolean>()
    val getAddRatingData: SingleLiveEvent<Boolean>
        get() = addRatingLiveData

    fun getUdhaarOverDue(customerId: Int, lang: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getUdhaarOverDue(customerId, lang)
            getUdhaarOverDueLiveData.postValue(result.body()!!)
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

    fun getWalletPointNew(customerId: Int, pageNo: Int, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getWalletPointNew(customerId, pageNo, mSectionType)
                if (result.body() != null) {
                    getWalletPointLiveData.postValue(result.body()!!)
                }
            }
        }

    fun getAppHomeBottomCall(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                appHomeBottomLiveData.postValue(Response.Loading())
                val result = repository.getAppHomeBottomCall(customerId)
                if (result.body() != null) {
                    appHomeBottomLiveData.postValue(Response.Success(result.body()))
                } else {
                    appHomeBottomLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                appHomeBottomLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getGullakBalance(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getGullakBalance(customerId)
                if (result.body()!=null){
                    getGullakBalanceLiveData.postValue(result.body())
                }
            }
        }

    fun getRTGSBalance(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getRTGSBalance(customerId)
                if (result.body()!=null){
                    getRTGSBalanceLiveData.postValue(result.body())
                }
            }
        }

    fun getVATMUrl(customerId: Int, warehouseId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getVATMUrl(customerId, warehouseId)
                getVATMUrlLiveData.postValue(result.body())
            }
        }

    fun getNotifyItems(customerId: Int, warehouseId: Int, itemNumber: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getNotifyItems(customerId, warehouseId, itemNumber)
                getNotifyItemsLiveData.postValue(result?.body())
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

    fun scaleUpLeadInitiate(url: String?, customerId: Int, isUrlUsingCustId: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                scaleUpLeadInitiateLiveData.postValue(Response.Loading())
                val result: retrofit2.Response<JsonObject>
                if (isUrlUsingCustId) {
                    result = repository.scaleUpLeadInitiate(customerId)
                } else {
                    result = repository.scaleUpLeadInitiateUsingUrl(url)
                }
                if (result?.body() != null) {
                    scaleUpLeadInitiateLiveData.postValue(Response.Success(result.body()))
                } else {
                    scaleUpLeadInitiateLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                scaleUpLeadInitiateLiveData.postValue(
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

    fun getMurliAudioForMobile(customerId: Int,warehouseId:Int,mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getMurliAudioForMobileLiveData.postValue(Response.Loading())
                val result = repository.getMurliAudioForMobile(customerId,warehouseId, mSectionType)
                if (result.body() != null) {
                    getMurliAudioForMobileLiveData.postValue(Response.Success(result.body()))
                } else {
                    getMurliAudioForMobileLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getMurliAudioForMobileLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun downloadFileWithUrl(fileUrl: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                downloadFileWithUrlLiveData.postValue(Response.Loading())
                val result = repository.downloadFileWithUrl(fileUrl)
                if (result.body() != null) {
                    downloadFileWithUrlLiveData.postValue(Response.Success(result.body()))
                } else {
                    downloadFileWithUrlLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                downloadFileWithUrlLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getMurliPublishedStory(customerId: Int,warehouseId:Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getMurliPublishedStoryLiveData.postValue(Response.Loading())
                val result = repository.getMurliPublishedStory(customerId,warehouseId)
                if (result?.body() != null) {
                    getMurliPublishedStoryLiveData.postValue(Response.Success(result.body()))
                } else {
                    getMurliPublishedStoryLiveData.postValue(
                        Response.Error(RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error))
                    )
                }
            } else {
                getMurliPublishedStoryLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getGetDboyRatingOrder(url: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getGetDboyRatingOrder(url!!)
                getDboyRatingOrderLiveData.postValue(result?.body())
            }
        }
    fun getGetDboyRatingOrderOther(url: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.getGetDboyRatingOrderOther(url!!)
                if (result.code()==200){
                    getDboyRatingOrderOtherLiveData.postValue(result.body())
                }
            }
        }

    fun addRating(model: RatingModel?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.addRating(model!!)
                addRatingLiveData.postValue(result.body())
            }
        }

}