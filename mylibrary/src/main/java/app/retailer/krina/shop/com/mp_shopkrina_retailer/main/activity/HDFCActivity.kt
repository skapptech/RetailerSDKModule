package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AesCryptUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RSAUtility
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ServiceUtility
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.net.URLEncoder

class HDFCActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName

    private lateinit var webview: WebView
    //
    private var amount: String? = null
    private var orderId: String? = null
    private var merchantId: String? = null
    private var accessCode: String? = null
    private var url: String? = null
    private var key: String? = null
    private var redirectUrl: String? = null
    private var cancelUrl: String? = null
    private var rsaKey: String? = null

    //
    private var vResponse: String? = null
    private var encVal: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_hdfc)

        if (intent.extras != null) {
            amount = intent.extras!!.getString(AvenuesParams.AMOUNT)
            orderId = intent.extras!!.getString(AvenuesParams.ORDER_ID)
            merchantId = intent.extras!!.getString(AvenuesParams.MERCHANT_ID)
            accessCode = intent.extras!!.getString(AvenuesParams.ACCESS_CODE)
            key = intent.extras!!.getString(AvenuesParams.WORKING_KEY)
            url = intent.extras!!.getString(AvenuesParams.URL)
            redirectUrl = intent.extras!!.getString(AvenuesParams.REDIRECT_URL)
            cancelUrl = intent.extras!!.getString(AvenuesParams.CANCEL_URL)
            rsaKey = intent.extras!!.getString(AvenuesParams.RSA_KEY)
        }

        webview = findViewById(R.id.webView)
        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.setSupportMultipleWindows(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webview, true)
        }
        webview.addJavascriptInterface(MyJavaScriptInterface(), "HTMLOUT")
        webview.webViewClient = MyWebViewClient()
        webview.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                val newWebView = WebView(this@HDFCActivity)
                val webSettings = newWebView.settings
                webSettings.javaScriptEnabled = true
                webSettings.javaScriptCanOpenWindowsAutomatically = true
                webSettings.setSupportMultipleWindows(true)
                webSettings.loadWithOverviewMode = true
                webSettings.allowFileAccess = true
                webSettings.domStorageEnabled = true

                (resultMsg.obj as WebView.WebViewTransport).webView = newWebView
                resultMsg.sendToTarget()
                return true
            }

            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                showAlert(message, result)
                return true
            }

            override fun onJsConfirm(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                showAlert(message, result)
                return true
            }

            override fun onCloseWindow(window: WebView) {
                Log.d(TAG, "onCloseWindow " + window.url)
                super.onCloseWindow(window)
                window.destroy()
            }
        }

        val aesCryptUtil = AesCryptUtil(key) //23FB82B8A2B40CC634F65FEBB7843A4D
        val sa =
            aesCryptUtil.encrypt(
                "merchant_id=" + merchantId + "&order_id=" + orderId +
                        "&redirect_url=" + redirectUrl + "&cancel_url=" + cancelUrl +
                        "&amount=" + amount + "&language=EN&currency=INR"
            )

        var postUrl =
            "https://test.ccavenue.com/transaction.do?command=initiateTransaction&encRequest=$sa&access_code=AVWH85GF97BD04HWDB"

        postUrl = "$url&encRequest=$sa&access_code=$accessCode"

        if (!EndPointPref.getInstance(applicationContext)
                .getBoolean(EndPointPref.HDFC_NEW_UI) && !rsaKey.isNullOrEmpty()) {
            vResponse = intent.getStringExtra(AvenuesParams.RSA_KEY)
            // get rsa key method
            RenderView().execute()
        } else
            webview.loadUrl(postUrl)
        Log.d(TAG, postUrl)
    }

    override fun onBackPressed() {
        show_alert("transaction will cancel")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val intent = Intent()
            intent.putExtra(AvenuesParams.ORDER_ID, data.getStringExtra(AvenuesParams.ORDER_ID))
            intent.putExtra(
                AvenuesParams.TRANSACTION_ID,
                data.getStringExtra(AvenuesParams.TRANSACTION_ID)
            )
            intent.putExtra(AvenuesParams.AMOUNT, data.getStringExtra(AvenuesParams.AMOUNT))
            intent.putExtra(AvenuesParams.STATUS, data.getStringExtra(AvenuesParams.STATUS))
            intent.putExtra(AvenuesParams.RESPONSE, data.getStringExtra(AvenuesParams.RESPONSE))
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    private fun showAlert(message: String, result: JsResult) {
        AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, which: Int -> result.confirm() }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface?, which: Int -> result.cancel() }
            .create().show()
    }

    private fun show_alert(msg: String) {
        var msg = msg
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Alert!!")
        if (msg.contains("\n")) msg = msg.replace("\\\n".toRegex(), "")

        alertDialog.setMessage(msg)
        alertDialog.setButton(
            Dialog.BUTTON_POSITIVE,
            "OK"
        ) { dialog: DialogInterface?, which: Int ->
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        alertDialog.setButton(
            Dialog.BUTTON_NEGATIVE,
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        alertDialog.show()
    }


    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: $url")
            if (url.contains("somePartOfYourUniqueUrl")) {
                view.loadUrl(url) // Stay within this webview and load url
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            if (request.url.schemeSpecificPart.contains("ccavenue")) {
                request.requestHeaders["Access-Control-Allow-Origin"] = "*"
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.d(TAG, "onPageFinished: $url")
//            if (url.equalsIgnoreCase("https://payment.shopkirana.in/HDFCPayment/CcAvenueResponse") || url.equalsIgnoreCase("https://payment.shopkirana.in/HDFCPayment/CcAvenueResponse")) {
//                webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            }
            if (url.indexOf("/CcAvenueResponse") != -1 || url.indexOf("/CcAvenueCreditResponse") != -1) {
                webview.evaluateJavascript(
                    "document.getElementById('decResp').value",
                    ValueCallback<String> { value: String ->
                        var value = value
                        println("value $value")
                        value = value.replace("\\\\".toRegex(), "")
                        value = value.substring(1, value.length - 1)

                        val intent = Intent()
                        intent.putExtra(AvenuesParams.RESPONSE, value)
                        setResult(RESULT_OK, intent)
                        finish()
                    })
            }
        }
    }

    @Suppress("unused")
    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processHTML(html: String) {
            // process the html source code to get final status of transaction
            var status: String? = null
            status = if (html.indexOf("Failure") != -1) {
                "Transaction Declined!"
            } else if (html.indexOf("Success") != -1) {
                "Transaction Successful!"
            } else if (html.indexOf("Aborted") != -1) {
                "Transaction Cancelled!"
            } else {
                "Status Not Known!"
            }
            println("PayResponse $status")
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class RenderView : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Utils.showProgressDialog(this@HDFCActivity)
        }

        override fun doInBackground(vararg arg0: Void?): Void? {
            if (ServiceUtility.chkNull(vResponse) != "" && !ServiceUtility.chkNull(vResponse)
                    .toString().contains("ERROR")
            ) {
                val vEncVal = StringBuilder()
                vEncVal.append(
                    ServiceUtility.addToPostParams(
                        AvenuesParams.AMOUNT,
                        amount
                    )
                )
                vEncVal.append(
                    ServiceUtility.addToPostParams(
                        AvenuesParams.CURRENCY,
                        "INR"
                    )
                )
                encVal = RSAUtility.encrypt(
                    vEncVal.substring(0, vEncVal.length - 1),
                    vResponse
                ) // encrypt amount and currency
                println(encVal)
            }

            return null
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Utils.hideProgressDialog()
            try {
                val postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(
                    accessCode,
                    "UTF-8"
                ) + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(
                    merchantId,
                    "UTF-8"
                ) + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(
                    orderId,
                    "UTF-8"
                ) + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(
                    redirectUrl,
                    "UTF-8"
                ) + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(
                    cancelUrl,
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_NAME + "=" + URLEncoder.encode(
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME),
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_ADDRESS + "=" + URLEncoder.encode(
                    SharePrefs.getInstance(applicationContext)
                        .getString(SharePrefs.SHIPPING_ADDRESS),
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_CITY + "=" + URLEncoder.encode(
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.CITY_NAME),
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_STATE + "=" + URLEncoder.encode(
                    "Madhya Pradesh",
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_ZIP + "=" + URLEncoder.encode(
                    "452001",
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_COUNTRY + "=" + URLEncoder.encode(
                    "India",
                    "UTF-8"
                ) + "&" + AvenuesParams.DELIVERY_TEL + "=" + URLEncoder.encode(
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER),
                    "UTF-8"
                ) + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8")

                if (!url.isNullOrEmpty()) {
                    webview.postUrl(
                        url!!,
                        postData.toByteArray()
                    )
                } else {
                    webview.postUrl(
                        SharePrefs.getInstance(applicationContext)
                            .getString(SharePrefs.GATWAY_URL), postData.toByteArray()
                    )
                }
                println(webview.url + " " + postData)
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.setToast(applicationContext, "Unable to complete payment")
                startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
                finish()
            }
        }
    }
}