package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserNotificationViewModel(private var repository: UserNotificationRepository) :ViewModel() {

    private var _userNotificationResponse = MutableLiveData<NetworkResult<ArrayList<UserNotificationModel>>>()
    val userNotificationResponse: LiveData<NetworkResult<ArrayList<UserNotificationModel>>> = _userNotificationResponse


    private var _readNotificationResponse = MutableLiveData<NetworkResult<Boolean>>()
    val readNotificationResponse: LiveData<NetworkResult<Boolean>> = _readNotificationResponse

    fun getNotification(skip:Int,take:Int,customerID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.getNotification(skip,take,customerID).collect() {
                    _userNotificationResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(),"No internet connectivity")
        }
    }

    fun getNotificationReadNotification(notificationID: String) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getNotificationReadNotification(notificationID).collect() {
                    _readNotificationResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(),"No internet connectivity")
        }
    }
}