package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GetLokedCusResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OTPResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OtpVerifyRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.RegistrationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val otpLiveData = SingleLiveEvent<Response<OTPResponse>>()
    val otpData: SingleLiveEvent<Response<OTPResponse>>
        get() = otpLiveData

    private val showOtherLoginOptionLiveData = SingleLiveEvent<Boolean>()
    val showOtherLoginOptionData: SingleLiveEvent<Boolean>
        get() = showOtherLoginOptionLiveData

    private val verifyOtpLiveData = SingleLiveEvent<Response<Boolean>>()
    val verifyOtpData: SingleLiveEvent<Response<Boolean>>
        get() = verifyOtpLiveData

    private val customerVerifyLiveData = SingleLiveEvent<Response<GetLokedCusResponse>>()
    val customerVerifyData: SingleLiveEvent<Response<GetLokedCusResponse>>
        get() = customerVerifyLiveData

    private val tokenLiveData = SingleLiveEvent<Response<TokenResponse>>()
    val tokenData: SingleLiveEvent<Response<TokenResponse>>
        get() = tokenLiveData

    private val insertCustomerLiveData = SingleLiveEvent<Response<RegistrationResponse>>()
    val insertCustomerData: SingleLiveEvent<Response<RegistrationResponse>>
        get() = insertCustomerLiveData


    fun genLoginOtp(mobileNumber: String, deviceId: String, keyHas: String) = viewModelScope.launch(Dispatchers.IO) {
        if (TextUtils.isNullOrEmpty(mobileNumber)) {
            otpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.entermobilenumber)))
        } else if (!TextUtils.isValidMobileNo(mobileNumber)) {
            otpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.validMobilenumbe)))
        } else {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                otpLiveData.postValue(Response.Loading())
                val result = repository.getOtp(mobileNumber,deviceId,keyHas)
                if (result!!.body() != null) {
                    otpLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    otpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
                }
            } else {
                otpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }
    }

    fun showOtherLoginOption() = viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                val result = repository.showOtherLoginOption()
                showOtherLoginOptionLiveData.postValue(result.body()!!)
            }
    }

    fun callVerifyOtp(verifyOtpModel: OtpVerifyRequest?) = viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                verifyOtpLiveData.postValue(Response.Loading())
                val result = repository.callVerifyOtp(verifyOtpModel)
                if (result.body() != null) {
                    verifyOtpLiveData.postValue(Response.Success(result.body()!!))
                } else {
                    verifyOtpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
                }
            } else {
                verifyOtpLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
    }

    fun getCustVerifyInfo(mobileNumber:String, verify:String, fcmtoken:String, currentAPKversion:String, phoneOSversion:String, userDeviceName:String, IMEI:String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            customerVerifyLiveData.postValue(Response.Loading())
            val result = repository.getCustVerifyInfo(mobileNumber, verify, fcmtoken, currentAPKversion, phoneOSversion, userDeviceName, IMEI)
            if (result?.body() != null) {
                customerVerifyLiveData.postValue(Response.Success(result.body()!!))
            } else {
                customerVerifyLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
            }
        } else {
            customerVerifyLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }
    fun callToken(grantType: String?, username: String, password: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                tokenLiveData.postValue(Response.Loading())
                val result = repository.getToken(grantType, username, password)
                if (result!!.body() != null) {
                    tokenLiveData.postValue(Response.Success(result.body()))
                } else {
                    tokenLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
                }
            } else {
                tokenLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }

    fun insertCustomer(mobileNumber: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                insertCustomerLiveData.postValue(Response.Loading())
                val result = repository.insertCustomer(mobileNumber)
                if (result!!.body() != null) {
                    insertCustomerLiveData.postValue(Response.Success(result.body()))
                } else {
                    insertCustomerLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.somthing_went_wrong)))
                }
            } else {
                insertCustomerLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }
}