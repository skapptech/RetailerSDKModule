package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.AppVersionModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(
    app: Application,
    private val repository: AppRepository
) : AndroidViewModel(app) {

    private val versionLiveData = SingleLiveEvent<Response<ArrayList<AppVersionModel>>>()
    val versionData: SingleLiveEvent<Response<ArrayList<AppVersionModel>>>
        get() = versionLiveData

    private val getProfileLiveData = SingleLiveEvent<Response<MyProfileResponse>>()
    val getProfileData: SingleLiveEvent<Response<MyProfileResponse>>
        get() = getProfileLiveData

    private val companyDetailsLiveData = SingleLiveEvent<Response<CompanyInfoResponse>>()
    val companyDetails: SingleLiveEvent<Response<CompanyInfoResponse>>
        get() = companyDetailsLiveData

    private val tokenLiveData = SingleLiveEvent<Response<TokenResponse>>()
    val tokenData: SingleLiveEvent<Response<TokenResponse>>
        get() = tokenLiveData

    fun callAppVersion() = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            versionLiveData.postValue(Response.Loading())
            val result = repository.getAppVersion()
            if (result.body() != null) {
                versionLiveData.postValue(Response.Success(result.body()))
            } else {
                versionLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.no_response)))
            }
        } else {
            versionLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun getMyProfile(customerId: Int,  deviceId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            getProfileLiveData.postValue(Response.Loading())
            val result = repository.getMyProfile(customerId,deviceId)
            if (result.body() != null) {
                getProfileLiveData.postValue(Response.Success(result.body()))
            } else {
                getProfileLiveData.postValue(
                    Response.Error(result.code().toString())
                )
            }
        } else {
            getProfileLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
        }
    }
    fun callCompanyDetailsApi(customerId: Int,  mSectionType: String) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            companyDetailsLiveData.postValue(Response.Loading())
            val result = repository.getCompanyDetailWithToken(customerId,mSectionType)
            if (result.body() != null && result.body()?.isStatus!!) {
                companyDetailsLiveData.postValue(Response.Success(result.body()))
            } else {
                companyDetailsLiveData.postValue(
                    Response.Error(
                        result.body()?.message!!
                    )
                )
            }
        } else {
            companyDetailsLiveData.postValue(
                Response.Error(
                    getApplication<Application>().getString(
                        R.string.internet_connection
                    )
                )
            )
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
                    tokenLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.msg_improper_response_server)))
                }
            } else {
                tokenLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }



}