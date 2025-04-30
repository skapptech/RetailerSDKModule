package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServices
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class EditProfileRepository  constructor(private val apiServices: APIServiceCom) {

    suspend fun submitUser(request: UserUpdate) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.UpdateUser(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun uploadPostImg(body: MultipartBody.Part?) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.uploadPostImg(body)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}