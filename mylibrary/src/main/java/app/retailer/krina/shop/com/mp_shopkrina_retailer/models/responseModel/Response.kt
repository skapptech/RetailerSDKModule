package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel

sealed class Response<T>(val data: T? = null, val errorMesssage: String? = null) {
    class Loading<T> : Response<T>()
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(errorMesssage: String) : Response<T>(errorMesssage = errorMesssage)
}