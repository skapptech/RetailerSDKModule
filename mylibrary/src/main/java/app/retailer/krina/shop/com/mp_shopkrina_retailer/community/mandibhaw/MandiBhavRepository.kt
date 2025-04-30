package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServices
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.PostLikeModelRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MandiBhavRepository constructor(private val apiServices: APIServiceCom) {

    suspend fun addUserForMandi(model: MandiBhavModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.addUserMandiBhavDistrict(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
    suspend fun getState() = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.GetStates()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getDistrict(stateName:String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.GetDistrict(stateName)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun getMandiBhavData(stateName:String,cityName:String) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.getMandiBhav(stateName,cityName)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}