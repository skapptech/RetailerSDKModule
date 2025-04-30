package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import android.app.Application
import androidx.lifecycle.*
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.PostKKReturnReplaceRequestDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnOrderBatchItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SaleReturnRequestResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject


class ReturnOrderViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val salesReturnRequestLiveData = SingleLiveEvent<Response<ArrayList<SalesReturnRequestListModel>>>()
    val salesReturnRequestData: SingleLiveEvent<Response<ArrayList<SalesReturnRequestListModel>>>
        get() = salesReturnRequestLiveData

    private val salesReturnRequestDetailsLiveData = SingleLiveEvent<Response<ArrayList<SalesReturnRequestListDetailsModel>>>()
    val salesReturnRequestDetailsData: SingleLiveEvent<Response<ArrayList<SalesReturnRequestListDetailsModel>>>
        get() = salesReturnRequestDetailsLiveData

    private val returnItemLiveData = SingleLiveEvent<Response<ArrayList<ReturnItemModel>>>()
    val returnItemData: SingleLiveEvent<Response<ArrayList<ReturnItemModel>>>
        get() = returnItemLiveData

    private val returnOrderBatchItemLiveData = SingleLiveEvent<Response<ArrayList<ReturnOrderBatchItemModel>>>()
    val returnOrderBatchItemData: SingleLiveEvent<Response<ArrayList<ReturnOrderBatchItemModel>>>
        get() = returnOrderBatchItemLiveData

    private val uploadImageLiveData = SingleLiveEvent<Response<String>>()
    val uploadImageData: SingleLiveEvent<Response<String>>
        get() = uploadImageLiveData

    private val postSalesReturnRequestLiveData = SingleLiveEvent<Response<SaleReturnRequestResponseModel>>()
    val postSalesReturnRequestData: SingleLiveEvent<Response<SaleReturnRequestResponseModel>>
        get() = postSalesReturnRequestLiveData


    fun getSalesReturnList(customerId: Int) = viewModelScope.launch{
        if (Utils(getApplication()).isNetworkAvailable) {
            salesReturnRequestLiveData.postValue(Response.Loading())
            val result = repository.getSalesReturnList(customerId)
            if (result.body() != null && result.body()?.size != 0) {
                salesReturnRequestLiveData.postValue(Response.Success(result.body()))
            } else {
                salesReturnRequestLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            salesReturnRequestLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getSalesReturnDetailsList(requestId: Int) = viewModelScope.launch{
        if (Utils(getApplication()).isNetworkAvailable) {
            salesReturnRequestDetailsLiveData.postValue(Response.Loading())
            val result = repository.getSalesReturnDetailsList(requestId)
            if (result.body() != null && result.body()?.size != 0) {
                salesReturnRequestDetailsLiveData.postValue(Response.Success(result.body()))
            } else {
                salesReturnRequestDetailsLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            salesReturnRequestDetailsLiveData.postValue(
                Response.Error(getApplication<Application>().getString(
                R.string.internet_connection)))
        }
    }

      fun getReturnItem(customerId: Int, wId: Int, keyValue: String) = viewModelScope.launch{
          if (Utils(getApplication()).isNetworkAvailable) {
            returnItemLiveData.postValue(Response.Loading())
            val result = repository.getReturnItem(customerId, wId, keyValue)
            if (result.body() != null && result.body()?.size != 0) {
                returnItemLiveData.postValue(Response.Success(result.body()))
            } else {
                returnItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.no_item_available)))
            }
        } else {
            returnItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getSaleReturnOrderBatchItem(customerId: Int, itemMultiMrpId: Int) = viewModelScope.launch{
        if (Utils(getApplication()).isNetworkAvailable) {
            returnOrderBatchItemLiveData.postValue(Response.Loading())
            val result = repository.getSaleReturnOrderBatchItem(customerId, itemMultiMrpId)
            if (result.body() != null && result.body()?.size != 0) {
                returnOrderBatchItemLiveData.postValue(Response.Success(result.body()))
            } else {
                returnOrderBatchItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.msg_no_eligible_order)))
            }
        } else {
            returnOrderBatchItemLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }



    fun uploadImage(body: MultipartBody.Part) = viewModelScope.launch {
        if (Utils(getApplication()).isNetworkAvailable) {
            uploadImageLiveData.postValue(Response.Loading())
            val result = repository.uploadKKReturnReplaceImages(body)
            val `object` = JSONObject(result.body().toString())
            if (`object`.getBoolean("status")) {
                uploadImageLiveData.postValue(Response.Success(`object`.getString("Name")))
            } else {
                uploadImageLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.image_not_uploaded
                        )
                    )
                )
            }
        } else {
            uploadImageLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun postSalesReturnRequest(model: PostKKReturnReplaceRequestDc) = viewModelScope.launch{
        if (Utils(getApplication()).isNetworkAvailable) {
            postSalesReturnRequestLiveData.postValue(Response.Loading())
            val result = repository.postSalesReturnRequest(model)
            postSalesReturnRequestLiveData.postValue(Response.Success(result.body()))
        } else {
            postSalesReturnRequestLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

}