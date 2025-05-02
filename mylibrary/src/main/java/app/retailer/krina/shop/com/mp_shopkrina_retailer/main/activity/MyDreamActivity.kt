package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyDreamBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.MyDreamInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyDreamAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyDreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.WalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyAccountBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonElement
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class MyDreamActivity : AppCompatActivity(), MyDreamInterface {
    private var mBinding: ActivityMyDreamBinding? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var list: ArrayList<MyDreamModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMyDreamBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        // init views
        initialization()
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

    // Buy BTN clicked
    override fun onBuyBtnClick(dreamModel: DreamModel?) {
        if (utils!!.isNetworkAvailable) {
            Utils.showProgressDialog(this)
            commonClassForAPI!!.buyProduct(desPostDream, dreamModel)
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    fun initialization() {
        mBinding!!.toolbarDream.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.mydream)
        mBinding!!.customerName.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.customer_name)
        mBinding!!.totalPoint.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.totalpoint)
        list = ArrayList()
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        val back = mBinding!!.toolbarDream.back
        mBinding!!.recyeler.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )
        back.setOnClickListener { onBackPressed() }

        // CAll Wallet API
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.fetchMyDream(desMyDream)
                commonClassForAPI!!.fetchWalletPoint(
                    wallet,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                    "My Dream"
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // Wallet response
    var wallet: DisposableObserver<WalletResponse> =
        object : DisposableObserver<WalletResponse>() {
            override fun onNext(response: WalletResponse) {
                Utils.hideProgressDialog()
                mBinding!!.totalPoint.text =
                    (RetailerSDKApp.getInstance().dbHelper.getString(R.string.totalpoint)
                            + " : " + DecimalFormat("##.##")
                        .format(response.wallet?.totalAmount!!.toDouble()))
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.WALLET_POINT, response.wallet?.totalAmount!!.toString())
                val name =
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
                mBinding!!.customerName.text = name
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // fetch Dream value
    var desMyDream: DisposableObserver<ArrayList<MyDreamModel>> =
        object : DisposableObserver<ArrayList<MyDreamModel>>() {
            override fun onNext(arrayList: ArrayList<MyDreamModel>) {
                list = arrayList
                val adapter = MyDreamAdapter(this@MyDreamActivity, list!!, this@MyDreamActivity)
                mBinding!!.recyeler.adapter = adapter
                Utils.hideProgressDialog()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // But item response
    var desPostDream: DisposableObserver<JsonElement> =
        object : DisposableObserver<JsonElement>() {
            override fun onNext(jsonElement: JsonElement) {
                Utils.setToast(
                    applicationContext, "Buy successfully"
                )
                Utils.hideProgressDialog()
                recreate()
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}