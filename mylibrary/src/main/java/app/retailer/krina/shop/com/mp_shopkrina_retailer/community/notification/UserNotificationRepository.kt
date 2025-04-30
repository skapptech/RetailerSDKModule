package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.notification

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.APIServiceCom
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserNotificationRepository(val apiServices: APIServiceCom) {

    suspend fun getNotification(skip: Int, take: Int, customerID: String) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.getNotification(skip, take, customerID)
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }


    suspend fun getNotificationReadNotification(notificationID: String) =
        flow {
            emit(NetworkResult.Loading(true))
            val response = apiServices.getNotificationReadNotification(notificationID)
            emit(NetworkResult.Success(response))
        }.catch { e ->
            emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        }

}