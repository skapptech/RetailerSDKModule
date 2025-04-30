package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification.NotificationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val getNotificationLiveData = SingleLiveEvent<Response<NotificationResponse>>()
    val getNotificationData: SingleLiveEvent<Response<NotificationResponse>>
        get() = getNotificationLiveData

    private val notificationClickLiveData = SingleLiveEvent<JsonObject>()
    val notificationClickData: SingleLiveEvent<JsonObject>
        get() = notificationClickLiveData

    private val addItemInCartLiveData = SingleLiveEvent<Response<JsonObject>>()
    val addItemInCartData: SingleLiveEvent<Response<JsonObject>>
        get() = addItemInCartLiveData

    private val getNotificationItemLiveData = SingleLiveEvent<Response<ArrayList<ItemListModel>>>()
    val getNotificationItemData: SingleLiveEvent<Response<ArrayList<ItemListModel>>>
        get() = getNotificationItemLiveData
    fun notificationClick(customerId: Int,notificationId:Int) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.notificationClick(customerId,notificationId)
            notificationClickLiveData.postValue(result?.body())
        }
    }

    fun getNotification(customerId: Int,list: Int, page: Int) = viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getNotificationLiveData.postValue(Response.Loading())
                val result = repository.getNotification(customerId,list,page)
                if (result!!.body() != null) {
                    getNotificationLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    getNotificationLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
                }
            } else {
                getNotificationLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
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
                        Response.Error(MyApplication.getInstance().noteRepository.getString(R.string.server_error))
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

    fun getNotificationItem(custId: Int, wId: Int, notificationType: String?, typeId: Int, lang: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            getNotificationItemLiveData.postValue(Response.Loading())
            val result = repository.getNotificationItem(custId,wId,notificationType,typeId,lang)
            if (result.body() != null) {
                getNotificationItemLiveData.postValue(Response.Success(result.body()))
            } else {
                getNotificationItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            getNotificationItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
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