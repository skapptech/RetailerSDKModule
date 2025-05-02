package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

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