package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PostViewModel constructor(private val repository: PostRepository) : ViewModel() {

    private var newPostLiveData = MutableLiveData<NetworkResult<JsonObject>>()
    val newPostResponse: LiveData<NetworkResult<JsonObject>> = newPostLiveData


    private var uploadPostImgData = MutableLiveData<NetworkResult<JsonObject>>()
    val uploadPostImg: LiveData<NetworkResult<JsonObject>> = uploadPostImgData

    fun uploadPostImg(body: MultipartBody.Part?) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.uploadPostImg(body).collect() {
                    uploadPostImgData.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }


    fun newPost(model: PostModel) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.newPost(model).collect() {
                    newPostLiveData.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }

    fun editPost(model: PostModel) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO)  {
                repository.editPost(model).collect() {
                    newPostLiveData.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }

}