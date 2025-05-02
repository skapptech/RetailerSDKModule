package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CartDealResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OfferViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getBillDiscountListLiveData = SingleLiveEvent<Response<BillDiscountListResponse>>()
    val getBillDiscountListData: SingleLiveEvent<Response<BillDiscountListResponse>>
        get() = getBillDiscountListLiveData


    private val categoryLiveData = SingleLiveEvent<Response<JsonObject>>()
    val categoryData: SingleLiveEvent<Response<JsonObject>>
        get() = categoryLiveData


    private val itemListLiveData = SingleLiveEvent<Response<CartDealResponse>>()
    val itemData: SingleLiveEvent<Response<CartDealResponse>>
        get() = itemListLiveData


    private val itemListLiveData1 = SingleLiveEvent<Response<CartDealResponse>>()
    val itemData1: SingleLiveEvent<Response<CartDealResponse>>
        get() = itemListLiveData1


    private val removeOfferLiveData = SingleLiveEvent<Response<Boolean>>()
    val removeOfferData: SingleLiveEvent<Response<Boolean>>
        get() = removeOfferLiveData

    fun getAllBillDiscountOffer(
        customerId: Int,
        sectionType: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getBillDiscountListLiveData.postValue(Response.Loading())
                val result = repository.getAllBillDiscountOffer(customerId, sectionType)
                if (result?.body() != null && result.body()!!.isStatus && result.body()!!.billDiscountList!!.size != 0) {
                    getBillDiscountListLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getBillDiscountListLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getBillDiscountListLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getOfferCategory(
        custId: Int,
        offerId: Int,
        ssCatId: Int,
        brandId: Int,
        step: Int,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            categoryLiveData.postValue(Response.Loading())
            val result = repository.getOfferCategory(
                custId, offerId, ssCatId, brandId, step, lang
            )
            if (result?.body() != null) {
                categoryLiveData.postValue(Response.Success(result.body()))
            } else {
                categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun fetchItemList(
        custId: Int,
        offerId: Int,
        ssCatId: Int,
        brandId: Int,
        step: Int,
        skip: Int,
        take: Int,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            itemListLiveData.postValue(Response.Loading())
            val result = repository.getOfferItemList(
                custId, offerId, ssCatId, brandId, step, skip, take, lang
            )
            if (result?.body() != null) {
                itemListLiveData.postValue(
                    Response.Success(
                        result.body()
                    )
                )
            } else {
                itemListLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(R.string.somthing_went_wrong)
                    )
                )
            }
        } else {
            itemListLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun fetchItemList1(
        custId: Int,
        offerId: Int,
        ssCatId: Int,
        brandId: Int,
        step: Int,
        skip: Int,
        take: Int,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            itemListLiveData1.postValue(Response.Loading())
            val result = repository.getOfferItemList(
                custId, offerId, ssCatId, brandId, step, skip, take, lang
            )
            if (result?.body() != null) {
                itemListLiveData1.postValue(
                    Response.Success(
                        result.body()
                    )
                )
            } else {
                itemListLiveData1.postValue(
                    Response.Error(
                        getApplication<Application>().getString(R.string.somthing_went_wrong)
                    )
                )
            }
        } else {
            itemListLiveData1.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun removeAllOffer(
        custId: Int,
        wId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            removeOfferLiveData.postValue(Response.Loading())
            val result = repository.removeAllOffer(
                custId, wId, 0
            )
            if (result?.body() != null) {
                removeOfferLiveData.postValue(
                    Response.Success(
                        result.body()
                    )
                )
            } else {
                removeOfferLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(R.string.somthing_went_wrong)
                    )
                )
            }
        } else {
            removeOfferLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }
}