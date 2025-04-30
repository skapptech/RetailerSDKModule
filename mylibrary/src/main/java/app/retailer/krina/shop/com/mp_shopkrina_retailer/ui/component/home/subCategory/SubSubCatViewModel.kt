package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.RelatedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.CatRelatedItemPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubSubCatViewModel(application: Application,private val repository: AppRepository) : AndroidViewModel(application) {

    private val itemListLiveData = SingleLiveEvent<Response<ItemListResponse>>()
    val itemData: SingleLiveEvent<Response<ItemListResponse>> get() = itemListLiveData


    private val itemListLiveData1 = SingleLiveEvent<Response<ItemListResponse>>()
    val itemData1: SingleLiveEvent<Response<ItemListResponse>> get() = itemListLiveData1


    private val getCategoriesLiveData = SingleLiveEvent<Response<JsonObject>>()
    val getCategoriesData: SingleLiveEvent<Response<JsonObject>>
        get() = getCategoriesLiveData

    private val getRelatedItemLiveData = SingleLiveEvent<Response<RelatedModel>>()
    val getRelatedItemData: SingleLiveEvent<Response<RelatedModel>>
        get() = getRelatedItemLiveData

    fun getCategories(
        custId: Int,
        wId: Int,
        baseCatId: Int,
        subCatId: Int,
        lang: String?,
        isStore:Boolean
    ) = viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getCategoriesLiveData.postValue(Response.Loading())
               val result = if(isStore){
                    repository.getStoreCategories(custId,wId,baseCatId,subCatId,lang)
                }else{
                    repository.getCategories(custId,wId,baseCatId,lang)
                }
                if (result.body() != null) {
                    getCategoriesLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getCategoriesLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getCategoriesLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getRelatedItem(
        model: CatRelatedItemPostModel
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            getRelatedItemLiveData.postValue(Response.Loading())
            val result = repository.getRelatedItem(model)
            if (result.body() != null && result.body()?.isStatus!!) {
                getRelatedItemLiveData.postValue(Response.Success(result.body()!!))
            } else {
                getRelatedItemLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.server_error
                        )
                    )
                )
            }
        } else {
            getRelatedItemLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun fetchItemList(
        custId: Int,
        ssCatId: Int,
        sCateId: Int,
        catId: Int,
        lang: String,
        skip: Int,
        take: Int,
        sortType: String,
        direction: String
    ) = viewModelScope.launch(Dispatchers.IO) {
//        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
        itemListLiveData.postValue(Response.Loading())
        val result = repository.getItemList(
            custId,
            ssCatId,
            sCateId,
            catId,
            lang,
            skip,
            take,
            sortType,
            direction
        )
        if (result?.body() != null) {
            itemListLiveData.postValue(Response.Success(result.body()))
        } else {
            itemListLiveData.postValue(Response.Error("Something went wrong"))
        }
//        } else {
//            categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
//        }
    }

    fun fetchItemList1(
        custId: Int,
        ssCatId: Int,
        sCateId: Int,
        catId: Int,
        lang: String,
        skip: Int,
        take: Int,
        sortType: String,
        direction: String
    ) = viewModelScope.launch(Dispatchers.IO) {
//        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
        itemListLiveData1.postValue(Response.Loading())
        val result = repository.getItemList(
            custId,
            ssCatId,
            sCateId,
            catId,
            lang,
            skip,
            take,
            sortType,
            direction
        )
        if (result?.body() != null) {
            itemListLiveData1.postValue(Response.Success(result.body()))
        } else {
            itemListLiveData1.postValue(Response.Error("Something went wrong"))
        }
//        } else {
//            categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
//        }
    }


}