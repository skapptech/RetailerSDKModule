package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GstInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject

class CustomerRegistrationViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val searchAddressLiveData = SingleLiveEvent<Response<JsonObject>>()
    val searchAddressData: SingleLiveEvent<Response<JsonObject>>
        get() = searchAddressLiveData

    private val editCustomerInfoLiveData = SingleLiveEvent<Response<MyProfileResponse>>()
    val editCustomerInfoData: SingleLiveEvent<Response<MyProfileResponse>>
        get() = editCustomerInfoLiveData

    private val signupLiveData = SingleLiveEvent<Response<SignupResponse>>()
    val signupData: SingleLiveEvent<Response<SignupResponse>>
        get() = signupLiveData

    private val getCustomerAddressLiveData = SingleLiveEvent<Response<CustomerAddressModel>>()
    val getCustomerAddressData: SingleLiveEvent<Response<CustomerAddressModel>>
        get() = getCustomerAddressLiveData

    private val getCityLiveData = SingleLiveEvent<ArrayList<CityModel>>()
    val getCityData: SingleLiveEvent<ArrayList<CityModel>>
        get() = getCityLiveData

    private val uploadImageLiveData = SingleLiveEvent<Response<String>>()
    val uploadImageData: SingleLiveEvent<Response<String>>
        get() = uploadImageLiveData

    private val gstInfoResponseLiveData = SingleLiveEvent<Response<GstInfoResponse>>()
    val gstInfoResponseData: SingleLiveEvent<Response<GstInfoResponse>>
        get() = gstInfoResponseLiveData

    private val getDocumentTypeLiveData = SingleLiveEvent<ArrayList<DocTypeModel>>()
    val getDocumentTypeData: SingleLiveEvent<ArrayList<DocTypeModel>>
        get() = getDocumentTypeLiveData

    fun searchAddress(url: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            searchAddressLiveData.postValue(Response.Loading())
            val result = repository.searchAddress(url)
            if (result.body() != null) {
                searchAddressLiveData.postValue(Response.Success(result.body()!!))
            } else {
                searchAddressLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.somthing_went_wrong
                        )
                    )
                )
            }
        } else {
            searchAddressLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun editCustomerInfo(model: EditProfileModel, mSectionType: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                editCustomerInfoLiveData.postValue(Response.Loading())
                val result = repository.editCustomerInfo(model, mSectionType)
                if (result?.body() != null && result?.body()!!.isStatus) {
                    editCustomerInfoLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    editCustomerInfoLiveData.postValue(Response.Error(result?.body()!!.message!!))
                }
            } else {
                editCustomerInfoLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun doNewSignup(signupModel: NewSignupRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                signupLiveData.postValue(Response.Loading())
                val result = repository.doNewSignup(signupModel)
                if (result!!.body() != null && result.body()!!.isStatus) {
                    signupLiveData.postValue(Response.Success(result.body()))
                } else {
                    signupLiveData.postValue(Response.Error(result.body()!!.message + ""))
                }
            } else {
                signupLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }

    fun getCustAddress(customerId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                getCustomerAddressLiveData.postValue(Response.Loading())
                val result = repository.getCustAddress(customerId)
                if (result!!.body() != null) {
                    getCustomerAddressLiveData.postValue(Response.Success(result.body()))
                } else {
                    getCustomerAddressLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.somthing_went_wrong
                            )
                        )
                    )
                }
            } else {
                getCustomerAddressLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }

    fun getCity() = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getCity()
            getCityLiveData.postValue(result.body()!!)
        }
    }

    fun uploadImage(body: MultipartBody.Part) = viewModelScope.launch {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            uploadImageLiveData.postValue(Response.Loading())
            val result = repository.uploadImage(body)
            val `object` = JSONObject(result.body().toString())
            if (result.body() != null && `object`.getBoolean("status")) {
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

    fun getGstStatus(gst: String, mSectionType: String) = viewModelScope.launch {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            gstInfoResponseLiveData.postValue(Response.Loading())
            val result = repository.getGstStatus(gst, mSectionType)
            if (result.body() != null) {
                gstInfoResponseLiveData.postValue(Response.Success(result.body()))
            } else {
                gstInfoResponseLiveData.postValue(
                    Response.Error(
                        getApplication<Application>().getString(
                            R.string.somthing_went_wrong
                        )
                    )
                )
            }
        } else {
            gstInfoResponseLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }

    fun getCustomerDocType(wId:Int,custId:Int) = viewModelScope.launch {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            val result = repository.getCustomerDocType(wId, custId)
            if (result.body() != null) {
                getDocumentTypeLiveData.postValue(result.body())
            }
        }
    }
}