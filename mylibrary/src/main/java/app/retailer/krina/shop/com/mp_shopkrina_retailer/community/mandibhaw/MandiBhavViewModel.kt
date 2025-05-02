package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Network
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MandiBhavViewModel constructor(private val repository: MandiBhavRepository):ViewModel() {

    private var _addUserMandiBhavResponse = MutableLiveData<NetworkResult<MandiCheckModel>>()
    val addUserResponse: LiveData<NetworkResult<MandiCheckModel>> = _addUserMandiBhavResponse

    private var _getstateResponse = MutableLiveData<NetworkResult<ArrayList<String>>>()
    val getState: LiveData<NetworkResult<ArrayList<String>>> = _getstateResponse

    private var _GetDistrictResponse = MutableLiveData<NetworkResult<ArrayList<String>>>()
    val getDistrictResponse: LiveData<NetworkResult<ArrayList<String>>> = _GetDistrictResponse


    private var _getMandiDataResponse = MutableLiveData<NetworkResult<ArrayList<MandiDataModel>>>()
    val getMandiData: LiveData<NetworkResult<ArrayList<MandiDataModel>>> = _getMandiDataResponse

    fun addUser(model: MandiBhavModel) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addUserForMandi(model).collect {
                    _addUserMandiBhavResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }

    fun getState() {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getState().collect {
                    _getstateResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }

    fun getDistrict(stateName:String) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getDistrict(stateName).collect {
                    _GetDistrictResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }

    fun getMandiBhav(stateName:String,cityName:String) {
        if (Network.checkConnectivity(RetailerSDKApp.application!!)) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getMandiBhavData(stateName,cityName).collect {
                    _getMandiDataResponse.postValue(it)
                }
            }
        } else {
            Utils.setToast(RetailerSDKApp.application, "No internet connectivity")
        }
    }
}
