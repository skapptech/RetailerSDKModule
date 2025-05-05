package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityExpiringPointsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyExpiringWalletAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyExpiringWalletModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class ExpiringPointsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityExpiringPointsBinding
    private var myWalletAdapter: MyExpiringWalletAdapter? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExpiringPointsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.toolbarMyWallet.title.text =
            RetailerSDKApp.getInstance().dbHelper.getData("expiring_points")
        //init view
        initialization()
        // back btn
        mBinding.toolbarMyWallet.back.setOnClickListener { onBackPressed() }
        mBinding.toolbarMyWallet.ImgInfo.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            netConnectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"), Context.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(netConnectionReceiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
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


    fun initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        val layoutManager = LinearLayoutManager(this)
        mBinding.rvExpiringPoints.layoutManager = layoutManager
        callApi()
    }

    fun callApi() {
        // Wallet API call
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this@ExpiringPointsActivity)
                commonClassForAPI!!.fetchExpiringPointsList(
                    walletDes,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getData("internet_connection")
            )
        }
    }


    private val walletDes: DisposableObserver<ArrayList<MyExpiringWalletModel>> =
        object : DisposableObserver<ArrayList<MyExpiringWalletModel>>() {
            override fun onNext(o: ArrayList<MyExpiringWalletModel>) {
                Utils.hideProgressDialog()
                try {
                    myWalletAdapter = MyExpiringWalletAdapter(this@ExpiringPointsActivity, o)
                    mBinding.rvExpiringPoints.adapter = myWalletAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    private val netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = Utils.getConnectivityStatusString(context)
            if (!status) {
                startActivityForResult(
                    Intent(applicationContext, NoInternetActivity::class.java),
                    222
                )
            }
        }
    }
}