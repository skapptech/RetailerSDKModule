package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchFilterModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemHistoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchItemViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getAllCategoriesLiveData = SingleLiveEvent<Response<SearchFilterModel>>()
    val getAllCategoriesData: SingleLiveEvent<Response<SearchFilterModel>>
        get() = getAllCategoriesLiveData


    private val searchItemHistoryLiveData = SingleLiveEvent<Response<SearchItemHistoryModel>>()
    val getSearchItemHistoryData: SingleLiveEvent<Response<SearchItemHistoryModel>>
        get() = searchItemHistoryLiveData


    private val searchItemHintLiveData = SingleLiveEvent<Response<JsonArray>>()
    val searchItemHintData: SingleLiveEvent<Response<JsonArray>>
        get() = searchItemHintLiveData

    private val searchItemsLiveData = SingleLiveEvent<Response<ItemListResponse>>()
    val searchItemsData: SingleLiveEvent<Response<ItemListResponse>>
        get() = searchItemsLiveData


    private val searchHintItemsDeleteLiveData = SingleLiveEvent<String>()
    val searchHintItemsDeleteData: SingleLiveEvent<String>
        get() = searchHintItemsDeleteLiveData



    fun getAllCategories(customerId: Int,wId:Int,lang:String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getAllCategoriesLiveData.postValue(Response.Loading())
                val result = repository.getAllCategories(customerId,wId,lang)
                if (result?.body() != null) {
                    getAllCategoriesLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getAllCategoriesLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.server_error
                            )
                        )
                    )
                }
            } else {
                getAllCategoriesLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getSearchItemHistory(custId: Int,wId: Int,skip: Int,take: Int,lang: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            searchItemHistoryLiveData.postValue(Response.Loading())
            val result = repository.getSearchItemHistory(custId,wId,skip,take,lang)
            if (result?.body() != null) {
                searchItemHistoryLiveData.postValue(Response.Success(result.body()))
            } else {
                searchItemHistoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            searchItemHistoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getSearchItemHint(custId: Int,skip: Int,take: Int,lang: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            searchItemHintLiveData.postValue(Response.Loading())
            val result = repository.getSearchItemHint(custId,skip,take,lang)
            if (result?.body() != null) {
                searchItemHintLiveData.postValue(Response.Success(result.body()))
            } else {
                searchItemHintLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            searchItemHintLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getSearchData(model: SearchItemRequestModel?) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            searchItemsLiveData.postValue(Response.Loading())
            val result = repository.getSearchData(model)
            if (result?.body() != null) {
                searchItemsLiveData.postValue(Response.Success(result.body()))
            } else {
                searchItemsLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            searchItemsLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun deleteSearchHintItem(customerId: Int?,keyWord:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.deleteSearchHintItem(customerId,keyWord)
            searchHintItemsDeleteLiveData.postValue(result?.body())
        }
    }
    fun getMoqList(itemList: ArrayList<ItemListModel>): ArrayList<ItemListModel> {
        val list = ArrayList<ItemListModel>()
        if (itemList.size != 0) {
            for (i in itemList.indices) {
                var ispresent = false
                for (j in list.indices) {
                    if (list[j].itemNumber.equals(
                            itemList[i].itemNumber,
                            ignoreCase = true
                        )
                    ) {
                        ispresent = true
                        if (list[j].moqList.size == 0) {
                            list[j].moqList.add(list[j])
                            list[j].moqList[0].isChecked = true
                        }
                        list[j].moqList.add(itemList[i])
                        break
                    }
                }
                if (!ispresent) {
                    list.add(itemList[i])
                }
            }
        }
        return list
    }

}