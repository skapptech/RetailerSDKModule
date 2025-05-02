package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityRequestBrandBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.RequestBrandModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver

class RequestBrandActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRequestBrandBinding

    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var mobileNumber: String? = null
    private var SbrandName: String? = null
    private var customerId = 0
    private var brandName: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRequestBrandBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        //init view
        initialization()
        //back Btn
        mBinding.toolbarRequestBrand.back.setOnClickListener { v: View? -> onBackPressed() }
        //submit btn
        mBinding.submit.setOnClickListener {
            SbrandName = brandName!!.text.toString().trim { it <= ' ' }
            if (!SbrandName.equals("", ignoreCase = true)) {
                if (utils!!.isNetworkAvailable) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.addRequestBrand(
                        addRequest,
                        RequestBrandModel(customerId, mobileNumber, SbrandName)
                    )
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.not_empty_brand_name)
                )
            }
        }
    }

    fun initialization() {
        mobileNumber = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER)
        customerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        brandName = mBinding.brandName
        mBinding.toolbarRequestBrand.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.request_brand)
        mBinding.text.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_requestbrand)
        mBinding.brandName.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enetrbrandname)
        mBinding.submit.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit_requestbrand)
    }

    /**
     * add request
     */
    private val addRequest: DisposableObserver<JsonElement> =
        object : DisposableObserver<JsonElement>() {
            override fun onNext(`object`: JsonElement) {
                Utils.hideProgressDialog()
                if (`object` != null) {
                    mBinding.brandName.setText("")
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.request_placed_success)
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }
}