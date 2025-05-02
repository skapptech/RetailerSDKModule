package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityKissanDaanBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class KissanDaanActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityKissanDaanBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kissan_daan)
        val webSettings = mBinding.daanWebview.settings
        val cust_id = "" + SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        val hub_id = "" + SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        var str_eng = "" + SharePrefs.getInstance(this).getString(SharePrefs.CURRENT_LANGUAGE)
        str_eng = if (str_eng == "hi") {
            "hindi"
        } else {
            "english"
        }
        mBinding.toolbarKisaan.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.kisan_dan)
        mBinding.daanWebview.webViewClient = WebViewClient()
        webSettings.javaScriptEnabled = true
//        webSettings.setAppCacheEnabled(true)
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.domStorageEnabled = true
        mBinding.daanWebview.visibility = View.INVISIBLE
        mBinding.daanWebview.isVerticalScrollBarEnabled = true
        val webview_url =
            (SharePrefs.getInstance(applicationContext).getString(SharePrefs.WEB_VIEW_BASE_URL)
                    + "/#/webview/KisanDan/" + cust_id + "/" + str_eng + "/" + hub_id)
        mBinding.daanWebview.loadUrl(webview_url)
        mBinding.daanWebview.webViewClient =
            WebViewClass(mBinding.daanWebview, mBinding.webviewProgrss)
        mBinding.toolbarKisaan.back.setOnClickListener { v: View? ->
            if (mBinding.daanWebview.canGoBack()) {
                mBinding.daanWebview.goBack()
            } else {
                onBackPressed()
            }
        }
        mBinding.daanWebview.setOnKeyListener { v: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val webView = v as WebView
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack()
                        return@setOnKeyListener true
                    }
                }
            }
            false
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

    internal inner class WebViewClass(val wv: WebView, val loadProgress: ProgressBar) :
        WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            wv.visibility = View.VISIBLE
            loadProgress.visibility = View.INVISIBLE
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            wv.loadData(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<center>" + "Try again later" + ".</center>",
                "text/html",
                "UTF-8"
            )
        }
    }
}