package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServices
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class PostRepository constructor(private val apiServices: APIServiceCom) {

    suspend fun uploadPostImg(body: MultipartBody.Part?) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.uploadPostImg(body)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun newPost(model: PostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.newPost(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

    suspend fun editPost(model: PostModel) = flow {
        emit(NetworkResult.Loading(true))
        val response = apiServices.editPost(model)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}