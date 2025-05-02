package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityGamesWebBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.AdapterGamesBanner
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddGamePointModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GamesBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class GamesWebActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityGamesWebBinding

    private var commonClassForAPI: CommonClassForAPI? = null
    private var utils: Utils? = null
    private var intentUrl: String? = null
    private var list: ArrayList<GamesBannerModel>? = null
    private var adapterGamesBanner: AdapterGamesBanner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_games_web)
        setSupportActionBar(mBinding.toolbar)

        try {
            intentUrl = intent.getStringExtra("url")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val webView = mBinding.webView
        webView.clearCache(true)
        webView.webViewClient = MyBrowser()
        webView.settings.loadsImagesAutomatically = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val settingWebView = webView.settings
        settingWebView.javaScriptEnabled = true
        settingWebView.javaScriptCanOpenWindowsAutomatically = true
        settingWebView.allowFileAccess = true
        settingWebView.domStorageEnabled = true
//        settingWebView.setAppCachePath("/data/data/$packageName/cache")
//        settingWebView.setAppCacheEnabled(true)
        settingWebView.cacheMode = WebSettings.LOAD_DEFAULT
        webView.loadUrl(intentUrl!!)
        //        webView.loadUrl("file:///android_asset/Fruit-Ninja/index.html");
        webView.addJavascriptInterface(JavaScriptInterface(this), "Android")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        list = ArrayList()
        adapterGamesBanner = AdapterGamesBanner(this, list!!)
        mBinding.vpBanner.adapter = adapterGamesBanner
        mBinding.vpBanner.startAutoScroll(3000)
        mBinding.vpBanner.interval = 900
        mBinding.vpBanner.isCycle = true
        mBinding.vpBanner.isBorderAnimation = false
        mBinding.vpBanner.isStopScrollWhenTouch = true
        mBinding.vpBanner.setAutoScrollDurationFactor(20.0)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        callBannerApi()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mBinding.webView.destroy()
        finish()
    }

    fun checkScore(message: String) {
        val score = message.toInt()
        if (score <= 0) {
            showGameOverDialog("Nice try! Score more to get wallet points")
        } else {
            showClaimPointDialog(message)
        }
        // update analytic
        MyApplication.getInstance().updateAnalyticGame("game_over", score)
    }

    fun ShowToast(message: String?) {
        Toast.makeText(applicationContext, "Clicked on KK", Toast.LENGTH_SHORT).show()
    }

    fun showGameOverDialog(Message: String?) {
        val mView = layoutInflater.inflate(R.layout.dialog_game_over, null)
        val customDialog = Dialog(this, R.style.CustomDialog)
        customDialog.setContentView(mView)
        customDialog.setCancelable(true)
        val pd_title = mView.findViewById<TextView>(R.id.pd_title)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val tvGameOverHead = mView.findViewById<TextView>(R.id.tvGameOverHead)
        tvGameOverHead.text = MyApplication.getInstance().dbHelper.getString(R.string.game_over)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.play_again)
        pd_title.text = Message
        okBtn.setOnClickListener { v: View? -> customDialog.dismiss() }
        customDialog.show()
    }

    fun showClaimPointDialog(message: String) {
        val mView = layoutInflater.inflate(R.layout.dialog_game_claim, null)
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(mView)
        dialog.setCancelable(false)
        val tvDetail = mView.findViewById<TextView>(R.id.tvDetail)
        val tvClaim = mView.findViewById<TextView>(R.id.tvClaim)
        val tvCancel = mView.findViewById<TextView>(R.id.tvCancel)
        val tvGameOverHead = mView.findViewById<TextView>(R.id.tvGameOverHead)
        tvGameOverHead.text = MyApplication.getInstance().dbHelper.getString(R.string.game_over)
        tvClaim.text = MyApplication.getInstance().dbHelper.getString(R.string.claim)
        tvCancel.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        tvDetail.text = MyApplication.getInstance().dbHelper.getString(R.string.won_wallet_points)
        tvClaim.setOnClickListener {
            // update analytic
            MyApplication.getInstance().updateAnalyticGame("game_claim_click", message.toInt())
            dialog.dismiss()
            callApi(message.toInt())
        }
        tvCancel.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    private fun callApi(score: Int) {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this@GamesWebActivity)
                commonClassForAPI!!.addGamePoint(
                    addWalletPoint, AddGamePointModel(
                        SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                        score, intent.getStringExtra("title"), "GameOver"
                    )
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    private inner class JavaScriptInterface(private val context: Context) {
        @JavascriptInterface
        fun showToast(message: String?) {
            ShowToast(message)
        }

        @JavascriptInterface
        fun callApi(message: String) {
            checkScore(message)
        }

        @JavascriptInterface
        fun gameOver(message: String) {
            checkScore(message)
        }
    }

    private fun callBannerApi() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.getRetailAppGameBanner(
                    bannerObserver,
                    SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                    SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private val addWalletPoint: DisposableObserver<String> = object : DisposableObserver<String>() {
        override fun onNext(message: String) {
            try {
                Utils.hideProgressDialog()
                Utils.setToast(
                    applicationContext, message
                )
            } catch (e: Exception) {
                e.printStackTrace()
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
    private val bannerObserver: DisposableObserver<ArrayList<GamesBannerModel>> =
        object : DisposableObserver<ArrayList<GamesBannerModel>>() {
            override fun onNext(response: ArrayList<GamesBannerModel>) {
                try {
                    list!!.addAll(response)
                    adapterGamesBanner!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {}
        }
}