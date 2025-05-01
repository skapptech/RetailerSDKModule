package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentWebViewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ProductShareActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentOptionActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.reactivex.observers.DisposableObserver
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WebViewActivity : AppCompatActivity() {
    private lateinit var mBinding: FragmentWebViewBinding
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1

    //
    private var mFilePathCallback: ValueCallback<Array<Uri?>?>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val mCapturedImageURI: Uri? = null
    private var mCameraPhotoPath: String? = null
    var permissionResult = false
    private val contentType = "*/*"
    var openCameraOnly = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_web_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mBinding.webView.clearCache(true)
        val webSettings = mBinding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowContentAccess = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true

        mBinding.webView.addJavascriptInterface(JavaScriptInterface(this), "Android")
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                println(url)
                if (url.startsWith("http")) return false
                //if not activity found, try to parse intent://
                try {
                    if (url.startsWith("intent:")) {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        //try to find fallback url
                        val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                        if (fallbackUrl != null) {
                            view.loadUrl(fallbackUrl)
                            return true
                        }
                    } else {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                            return true
                        }
                        //invite to install
                        val marketIntent = Intent(Intent.ACTION_VIEW).setData(
                            Uri.parse("market://details?id=" + intent.getPackage())
                        )
                        if (marketIntent.resolveActivity(packageManager) != null) {
                            startActivity(marketIntent)
                            return true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //not an intent uri
                }
                return false //do nothing in other cases
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                println("" + url)
                mBinding.progressWeb.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                println("" + url)
                mBinding.progressWeb.visibility = View.GONE
            }
        }
        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView, url: String, message: String, result: JsResult
            ): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onShowFileChooser(
                webView: WebView,
                filePath: ValueCallback<Array<Uri?>?>?,
                fileChooserParams: FileChooserParams
            ): Boolean {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback?.onReceiveValue(null)
                }
                mFilePathCallback = filePath

                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent?.putExtra("PhotoPath", mCameraPhotoPath)
                } catch (ex: Exception) {
                    Log.e("Common.TAG", "Unable to create File", ex)
                    ex.printStackTrace()
                }

                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.absolutePath
                    val photoUri = FileProvider.getUriForFile(
                        applicationContext, applicationContext.packageName  + ".provider", photoFile
                    )
                    takePictureIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                } else {
                    takePictureIntent = null
                }

                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = contentType

                val intentArray: Array<Intent?> = if (takePictureIntent != null) {
                    arrayOf(takePictureIntent)
                } else {
                    arrayOfNulls(0)
                }

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

                if (openCameraOnly)
                    takePictureIntent?.let { startActivityForResult(it, INPUT_FILE_REQUEST_CODE) }
                else
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }
        }
        if (intent.extras != null) {
            val s = replaceParams(intent.getStringExtra("url"))
            mBinding.webView.loadUrl(s)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri?>? = null

            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
                if (data != null) {
                    // If there is not data, then we may have taken a photo
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
                openCameraOnly = false
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    // retrieve from the private variable if the intent is null
                    if (data == null) result = mCapturedImageURI
                    else data.data
                } catch (e: java.lang.Exception) {
                    Toast.makeText(
                        applicationContext, "activity :$e", Toast.LENGTH_LONG
                    ).show()
                }
                if (result != null) mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
    }

    override fun onBackPressed() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


    // Requesting permission
    private fun callRunTimePermissions(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        Permissions.check(this /*context*/,
            permissions,
            null /*rationale*/,
            null /*options*/,
            object : PermissionHandler() {
                override fun onGranted() {
                    permissionResult = true
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                    super.onDenied(context, deniedPermissions)
                    permissionResult = false
                    callRunTimePermissions()
                }
            })
        return permissionResult
    }

    private fun openFileExplorer() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    private fun shareWuduApp(body: String, url: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(
            Intent.EXTRA_TEXT, """
     $body
     $url
     """
        )
        startActivity(Intent.createChooser(share, "Share with"))
        MyApplication.getInstance()
            .updateAnalyticShare(javaClass.simpleName, "Wudu Share On WhatsApp")
    }

    private fun shareProduct(imagePath: String, body: String, returnPath: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, "$body\n http://dl.trade.er15.xyz/$returnPath")
        startActivity(Intent.createChooser(share, "Share Product"))
        MyApplication.getInstance()
            .updateAnalyticShare(javaClass.simpleName, "App Share On WhatsApp")
    }

    private fun replaceParams(url: String?): String {
        var url = url
        url = url!!.replace(
            "custid", "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
        url = url.replace(
            "customerid",
            "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
        url = url.replace(
            "wid", "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        )
        url = url.replace("lang", LocaleHelper.getLanguage(applicationContext))
        url = url.replace(
            "name", SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
        )
        url = url.replace(
            "skcode", SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        )
        url = url.replace(
            "[CUSTOMERID]",
            "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
        url = url.replace(
            "[SKCODE]",
            "" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        )
        url = url.replace(
            "[WAREHOUSEID]",
            "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        )
        url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(applicationContext))
        url = url.replace(
            "[MOBILE]",
            "" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
        )
        return url
    }

    private fun updateCartAllValue(mShoppingCart: ShopingCartItemDetailsResponse) {
        MyApplication.getInstance().noteRepository.addToCart(mShoppingCart.shoppingCartItemDcs)
    }

    inner class JavaScriptInterface internal constructor(private val context: Context) {
        @JavascriptInterface
        fun setHeader(s: String?) {
            runOnUiThread { title = s }
        }

        fun hideHeader(s: Boolean?) {
            runOnUiThread { supportActionBar?.hide() }
        }

        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(context, "" + toast, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun shareItem(imagePath: String, body: String, returnPath: String) {
            shareProduct(imagePath, body, returnPath)
        }

        @JavascriptInterface
        fun shareWudu(body: String, url: String) {
            shareWuduApp(body, url)
        }

        @JavascriptInterface
        fun clearCache() {
            mBinding.webView.clearCache(true)
            mBinding.webView.reload()
        }

        @JavascriptInterface
        fun closeSKRetailerWebApp() {
            finish()
        }

        @JavascriptInterface
        fun openProfile() {
            startActivity(Intent(applicationContext, EditProfileActivity::class.java))
        }

        @JavascriptInterface
        fun openCart() {
            startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
        }

        @JavascriptInterface
        fun openPaymentPage() {
            startActivity(Intent(applicationContext, PaymentOptionActivity::class.java))
        }

        @JavascriptInterface
        fun openTrade() {
            startActivity(Intent(applicationContext, TradeActivity::class.java))
        }

        @JavascriptInterface
        fun openFreebies() {
            startActivity(Intent(applicationContext, FreebiesOfferActivity::class.java))
        }

        @JavascriptInterface
        fun home() {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        @JavascriptInterface
        fun openHome() {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }

        @JavascriptInterface
        fun openProductDetail(number: String?, multiMRPId: String?, warehouseId: String?) {
            startActivity(
                Intent(applicationContext, ProductShareActivity::class.java).putExtra(
                    "number",
                    number
                ).putExtra("multiMRPId", multiMRPId).putExtra("warehouseId", warehouseId)
            )
        }

        @JavascriptInterface
        fun getCameraPermission(): Boolean {
            return callRunTimePermissions()
        }

        @JavascriptInterface
        fun openCameraOnly(enable: Boolean) {
            openCameraOnly = enable
        }

        // for reloading cart data
        @JavascriptInterface
        fun ReloadCart() {
            runOnUiThread {
                CommonClassForAPI.getInstance(this@WebViewActivity).getCustomerShoppingCart(
                    shoppingCartItemDes,
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                    LocaleHelper.getLanguage(applicationContext),
                    "webview"
                )
            }
        }

        @JavascriptInterface
        fun udaharPaymentSuccess(
            accountId: String,
            orderId: String,
            status: String,
            amount: String,
            transactionRefNo: String?
        ) {
            println("success $accountId$orderId$status$amount")
            val intent = Intent()
            intent.putExtra("accountId", accountId)
            intent.putExtra("orderId", orderId)
            intent.putExtra("status", status)
            intent.putExtra("amount", amount)
            intent.putExtra("transactionRefNo", transactionRefNo)
            setResult(RESULT_OK, intent)
            finish()
        }

        @JavascriptInterface
        fun udaharPaymentFailure(
            accountId: String,
            orderId: String,
            status: String,
            amount: String,
            transactionRefNo: String?
        ) {
            println("fail $accountId$orderId$status$amount")
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        @JavascriptInterface
        fun closeScreen() {
            finish()
        }

        @JavascriptInterface
        fun openBrowser(url: String) {
            Log.d("TAG", "url: $url")
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }


    // shopping cart observer
    private val shoppingCartItemDes: DisposableObserver<CheckoutCartResponse> =
        object : DisposableObserver<CheckoutCartResponse>() {
            override fun onNext(mCart: CheckoutCartResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (mCart.status) {
                        val response = mCart.shoppingCartItemDetailsResponse
                        if (response?.shoppingCartItemDcs != null && response.shoppingCartItemDcs.size > 0) {
                            updateCartAllValue(response)
                        }
                    }
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
}