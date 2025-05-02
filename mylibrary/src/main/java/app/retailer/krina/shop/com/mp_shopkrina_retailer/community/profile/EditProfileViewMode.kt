package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PollModelResquest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditProfileViewMode constructor(private val repository: EditProfileRepository) : ViewModel() {

    private var _updateUserResponse = MutableLiveData<NetworkResult<UserUpdate>>()
    val updateResponse: LiveData<NetworkResult<UserUpdate>> = _updateUserResponse

    private var uploadPostImgData = MutableLiveData<NetworkResult<JsonObject>>()
    val uploadPostImg: LiveData<NetworkResult<JsonObject>> = uploadPostImgData

    fun getSubmitUser(model: UserUpdate) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.submitUser(model).collect() {
                    _updateUserResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

    fun uploadPostImg(body: MultipartBody.Part?) {
        if (Network.checkConnectivity(MyApplication.getInstance()!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.uploadPostImg(body).collect() {
                    uploadPostImgData.postValue(it)
                }
            }
        } else {
            Utils.setToast(MyApplication.getInstance(), "No internet connectivity")
        }
    }

}