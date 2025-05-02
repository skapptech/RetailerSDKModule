package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MyGullakViewModel : ViewModel() {
    private var gullakLiveData: MutableLiveData<ArrayList<GullakModel>>? = null
    private val disposables = CompositeDisposable()
    private var commonAPICall: CommonClassForAPI? = null

    fun init() {
        commonAPICall = CommonClassForAPI.getInstance(RetailerSDKApp.getInstance().activity)
    }

    fun getMyGullakList(): LiveData<ArrayList<GullakModel>?>? {
        if (gullakLiveData == null) {
            gullakLiveData = MutableLiveData<ArrayList<GullakModel>>()
        }
        return gullakLiveData
    }

    fun hitGullakHistoryAPI(customerId: Int, pageCount: Int, totalOrderCount: Int) {
        commonAPICall?.fetchGullakDataList(
                gullakDataObserver,
                customerId,
                pageCount,
                totalOrderCount
        )
    }

    private var gullakDataObserver: DisposableObserver<ArrayList<GullakModel>> =
            object : DisposableObserver<ArrayList<GullakModel>>() {
                override fun onNext(list: ArrayList<GullakModel>) {
                    gullakLiveData?.value = list
                }

                override fun onError(e: Throwable) {
                    gullakLiveData?.value = null
                }

                override fun onComplete() {}
            }
}