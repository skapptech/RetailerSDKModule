package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActvityTradeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentOptionActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class DirectUdharActivity : AppCompatActivity() {
    private val MY_PERMISSION_REQUEST_CODE = 123
    private val INPUT_FILE_REQUEST_CODE = 1
    private val UPI_REQUEST_CODE = 89
    private var mBinding: ActvityTradeBinding? = null

    private var activity: DirectUdharActivity? = null

    private var mUploadMessage: ValueCallback<Uri?>? = null
    private val mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    private var dialogRazor: Dialog? = null
    private var permissionResult: Boolean = false
    private var contentType = "*/*"
    private var isCustomUrl = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.actvity_trade)
        activity = this
        initialization()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        /* if (getIntent().getExtras() != null && getIntent().hasExtra("notificationId")) {
            int notificationId = getIntent().getExtras().getInt("notificationId");
            MyApplication.getInstance().notificationView(notificationId);
            getIntent().getExtras().clear();
        }*/
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver, IntentFilter("UdharUPINotification")
        )
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onBackPressed() {
        if (isCustomUrl) {
            mBinding!!.webview.evaluateJavascript(
                "document.getElementById('successclose').click()", null
            )
            isCustomUrl = true
        } else if (!isCustomUrl && mBinding!!.webview.canGoBack()) {
            mBinding!!.webview.goBack()
        } else {
            showExitDialog()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UPI_REQUEST_CODE) {
            if (data != null) {
                // Utils.setToast(getApplicationContext(), " " + data.getData());
            } else {
                mBinding!!.webview.evaluateJavascript(
                    "document.getElementById('Invisible').click()", null
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null
            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    results = if (dataString != null) {
                        arrayOf(Uri.parse(dataString))
                    } else arrayOf(Uri.parse(mCameraPhotoPath))
                }
            }
            try {
                val filePath = getPath(results!![0])
                if (filePath != null && filePath.contains(".pdf")) {
                    mFilePathCallback!!.onReceiveValue(results)
                } else compressImage(getPath(results[0]))
            } catch (e: Exception) {
                mFilePathCallback!!.onReceiveValue(results)
                mFilePathCallback = null
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != 1 || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var result: Uri? = null
            try {
                if (resultCode == RESULT_OK) {
                    // retrieve from the private variable if the intent is null
                    result = if (data == null) mCapturedImageURI else data.data
                }
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, "activity :$e", Toast.LENGTH_LONG
                ).show()
            }
            try {
                compressImage1(getPath(result))
            } catch (e: Exception) {
                e.printStackTrace()
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
    }


    private fun initialization() {
        mBinding!!.swipeRefreshLayout.isEnabled = false
        val webSettings = mBinding!!.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        //        webSettings.setAppCacheEnabled(true);
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowContentAccess = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.userAgentString =
            "Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        mBinding!!.webview.addJavascriptInterface(
            JavaScriptInterface(
                this
            ), "Android"
        )

        mBinding!!.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url!!.contains(".png") || url!!.contains(".jpg")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(url), "application/file")
                    try {
                        view!!.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    mBinding!!.webview.loadUrl(url)
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                mBinding!!.pBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding!!.pBar.visibility = View.GONE
                isCustomUrl = false
                println("DirectUrl:::$url")
                if (url!!.contains("lead/UPIPaymentStatus?TxnNo")) {
                    isCustomUrl = true
                }
            }

            override fun onUnhandledKeyEvent(view: WebView, event: KeyEvent) {
                if (event.action == KeyEvent.ACTION_UP) if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    mBinding!!.webview.loadUrl("javascript:onEnter()")
                }
            }
        }
        mBinding!!.webview.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView, url: String, message: String, result: JsResult
            ): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onCreateWindow(
                view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                dialogRazor = Dialog(activity!!, R.style.BottomTheme)
                dialogRazor!!.setContentView(R.layout.dialog_webview)
                dialogRazor!!.setCanceledOnTouchOutside(false)
                val dialogWebview = dialogRazor!!.findViewById<WebView>(R.id.dialogWebview)
                val webSettings = dialogWebview.settings
                webSettings.javaScriptEnabled = true
                webSettings.allowFileAccess = true
                //                webSettings.setAppCacheEnabled(true);
                webSettings.databaseEnabled = true
                webSettings.domStorageEnabled = true
                webSettings.allowContentAccess = true
                webSettings.setSupportMultipleWindows(true)
                webSettings.allowFileAccessFromFileURLs = true
                webSettings.allowUniversalAccessFromFileURLs = true
                webSettings.setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                webSettings.javaScriptCanOpenWindowsAutomatically = true
                webSettings.cacheMode = WebSettings.LOAD_DEFAULT
                webSettings.setSupportZoom(true)
                webSettings.builtInZoomControls = true
                webSettings.pluginState = WebSettings.PluginState.ON
                dialogWebview.addJavascriptInterface(PaymentInterface(), "PaymentInterface")
                val transport = resultMsg.obj as WebViewTransport
                transport.webView = dialogWebview
                resultMsg.sendToTarget()

                dialogWebview.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        if (url.contains("status=failed")) {
                            if (dialogRazor != null && dialogRazor!!.isShowing) {
                                dialogRazor!!.dismiss()
                            }
                        }
                    }

                    override fun onReceivedError(
                        view: WebView, errorCode: Int, description: String, failingUrl: String
                    ) {
                        //System.out.println("ErrorUrl - "+ failingUrl+" - "+view.getUrl());
                    }
                }
                dialogRazor!!.show()
                return true
            }

            override fun onShowFileChooser(
                view: WebView,
                filePath: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback!!.onReceiveValue(null)
                }
                mFilePathCallback = filePath

                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent!!.putExtra("PhotoPath", mCameraPhotoPath)
                } catch (ex: IOException) {
                    Log.e("Common.TAG", "Unable to create File", ex)
                }

                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.absolutePath
                    val photoUri = FileProvider.getUriForFile(
                        applicationContext, BuildConfig.APPLICATION_ID + ".provider", photoFile
                    )
                    takePictureIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                } else {
                    takePictureIntent = null
                }

                //                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.setType(contentType)
                val intentArray = if (takePictureIntent != null) {
                    arrayOf<Intent?>(takePictureIntent)
                } else {
                    arrayOfNulls(0)
                }

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)

                return true
            }
        }

        mBinding!!.webview.clearCache(true)
        if (intent.extras != null) {
            val s = replaceParams(intent.getStringExtra("url")!!)
            println("URL>>>>>>>>>>>>>>>"+s)
            mBinding!!.webview.loadUrl(s)
        }
    }

    private fun replaceParams(url: String): String {
        var url = url
        url = url.replace(
            "custid", "" + SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        )
        url = url.replace(
            "customerid", "" + SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        )
        url = url.replace("wid", "" + SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID))
        url = url.replace("lang", LocaleHelper.getLanguage(this))
        url = url.replace("name", SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_NAME))
        url = url.replace("skcode", SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE))
        return url
    }

    private fun closeDialogRazor() {
        if (dialogRazor != null && dialogRazor!!.isShowing) {
            dialogRazor!!.dismiss()
        }
    }

    private fun setNotification(messageBody: String, title: String) {
        try {
            val CHANNEL_ID = "chat"
            val CHANNEL_NAME = "chat"

            val intent = Intent(this, TradeActivity::class.java)
            //intent.putExtra("list", poModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Main PendingIntent that restarts
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            // create notification
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder =
                NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.direct_sign)
                    .setContentTitle(title).setContentText(messageBody).setContentInfo(title)
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                )
                mChannel.enableLights(true)
                mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(mChannel)
            }
            notificationManager.notify(1, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareProduct(imagePath: String, body: String, returnPath: String) {
        Utils.hideProgressDialog()

        runOnUiThread {
            try {
                Picasso.get().load(imagePath).into(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val share = Intent(Intent.ACTION_SEND)
        share.setType("image/*")
        share.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                Objects.requireNonNull(
                    applicationContext
                ), BuildConfig.APPLICATION_ID + ".provider", File(
                    Environment.getExternalStorageDirectory()
                        .toString() + Constant.PRODUCT_IMAGE_FOLDER + "image.png"
                )
            )
        )
        share.putExtra(Intent.EXTRA_TEXT, "$body\n http://dl.trade.er15.xyz/$returnPath")
        startActivity(Intent.createChooser(share, "Share Product"))
    }

    fun getPath(uri: Uri?): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(applicationContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id.toLong()
                )

                return getDataColumn(applicationContext, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )

                return getDataColumn(applicationContext, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            return getDataColumn(applicationContext, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )

        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun compressImage(result: String?) {
        val fileToUpload = File(result)
        lifecycleScope.launch {
            try {
                val compressedFile = Compressor.compress(applicationContext, fileToUpload) {
                    quality(90)
                    format(Bitmap.CompressFormat.JPEG)
                }
                mFilePathCallback!!.onReceiveValue(arrayOf(Uri.fromFile(compressedFile)))
                mFilePathCallback = null
            } catch (e: Exception) {
                e.printStackTrace()
                mFilePathCallback!!.onReceiveValue(null)
                mFilePathCallback = null
            }
        }
    }

    private fun compressImage1(result: String?) {
        val fileToUpload = File(result)
        lifecycleScope.launch {
            try {
                val compressedFile = Compressor.compress(applicationContext, fileToUpload) {
                    quality(90)
                    format(Bitmap.CompressFormat.JPEG)
                }
                mUploadMessage!!.onReceiveValue(Uri.fromFile(compressedFile))
                mUploadMessage = null
            } catch (e: Exception) {
                e.printStackTrace()
                mUploadMessage!!.onReceiveValue(null)
                mUploadMessage = null
            }
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.alert_for_exit_screen))
            .setMessage(getString(R.string.do_you_want_to_exit))
            .setPositiveButton(getString(R.string.yes_title)) { dialog: DialogInterface, i: Int ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, i: Int -> dialog.dismiss() }
            .show()
    }


    private fun ringtone() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareText1(text: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(share, "Share"))
        MyApplication.getInstance().updateAnalyticShare("TradeActivity", text)
    }

    private fun Call(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(text)
        } else {
            callContact(text)
        }
    }

    private fun checkPermission(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(
                        this@DirectUdharActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSION_REQUEST_CODE
                    )
                } else { // Request permission
                    ActivityCompat.requestPermissions(
                        this@DirectUdharActivity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                callContact(text)
            }
        }
    }

    private fun accessCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), MY_PERMISSION_REQUEST_CODE
                    )
                }
                return false
            } else {
                return true
            }
        }
        return true
    }

    private fun callRunTimePermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        Permissions.check(this,  /*context*/
            permissions, null,  /*rationale*/
            null,  /*options*/
            object : PermissionHandler() {
                override fun onGranted() {
                    permissionResult = true
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                    super.onDenied(context, deniedPermissions)
                    permissionResult = false
                }
            })
        return permissionResult
    }

    private fun callContact(text: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.setData(Uri.parse("tel: $text"))
        if (ActivityCompat.checkSelfPermission(
                this@DirectUdharActivity, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@DirectUdharActivity, Manifest.permission.CALL_PHONE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this@DirectUdharActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSION_REQUEST_CODE
                )
            }
        }
        startActivity(callIntent)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        return imageFile
    }

    private fun currentLatLong(): String {
        val jsonObject = JSONObject()
        val gpsTracker = GPSTracker(this@DirectUdharActivity)
        if (gpsTracker.canGetLocation()) {
            val latitude = gpsTracker.latitude
            val longitude = gpsTracker.longitude
            try {
                jsonObject.put("lat", latitude)
                jsonObject.put("long", longitude)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            gpsTracker.showSettingsAlert()
        }
        return jsonObject.toString()
    }

    private fun Open(packageName: String) {
        var intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
    }

    private fun appInstalledOrNot(packageManager: String): Boolean {
        val pm = getPackageManager()
        try {
            pm.getPackageInfo(packageManager, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    private fun showShareWhatsappDialog(textMsg: String, number: String) {
        val dialog = BottomSheetDialog(activity!!, R.style.BottomTheme)
        dialog.setContentView(R.layout.dialog_whatsapp_share)
        dialog.setCanceledOnTouchOutside(true)
        val llWhatsapp = dialog.findViewById<LinearLayout>(R.id.llWhatsapp)
        val llWhatsappBusiness = dialog.findViewById<LinearLayout>(R.id.llWhatsappBusiness)
        if (appInstalledOrNot("com.whatsapp") && appInstalledOrNot("com.whatsapp.w4b")) {
            dialog.show()
        } else shareOnWhatsapp(textMsg, number, !appInstalledOrNot("com.whatsapp"))
        llWhatsapp!!.setOnClickListener { view: View? ->
            shareOnWhatsapp(textMsg, number, false)
            dialog.dismiss()
        }
        llWhatsappBusiness!!.setOnClickListener { view: View? ->
            shareOnWhatsapp(textMsg, number, true)
            dialog.dismiss()
        }
    }

    private fun shareOnWhatsapp(textMsg: String, number: String?, isWB: Boolean) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.setType("text/plain")
        if (isWB) {
            whatsappIntent.setPackage("com.whatsapp.w4b")
        } else {
            whatsappIntent.setPackage("com.whatsapp")
        }
        if (number != null && number != "") {
            whatsappIntent.putExtra(
                "jid", PhoneNumberUtils.stripSeparators("91$number") + "@s.whatsapp.net"
            )
        }
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, textMsg)
        try {
            activity!!.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            Utils.setToast(activity, "Whatsapp not installed.")
        }
    }

    private fun closeApp() {
        finish()
        finishAffinity()
    }

    private fun reloadPageview() {
        runOnUiThread { mBinding!!.webview.reload() }
    }

    private fun redirectPageview(url: String) {
        mBinding!!.webview.loadUrl(url)
    }

    private fun urlOpenInBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun clearWebViewCache() {
        try {
            runOnUiThread { mBinding!!.webview.clearCache(true) }
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.setToast(activity, e.message)
        }
    }

    private fun showToastMessage(msg: String) {
        Utils.setToast(activity, msg)
    }

    private fun downloadFileFromUrl(url: String) {
        val downloadManager = this.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val PdfUri = Uri.parse(url) // Replace with the actual URL of your PDF

        val fileName = "PhsicalNachfile.pdf"
        val destinationFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName
        )
        val destinationUri = Uri.fromFile(destinationFile)

        val request = DownloadManager.Request(PdfUri)
        request.setDestinationUri(destinationUri) // Set the destination URI to the Downloads folder
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(request)
        Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show()
    }


    inner class JavaScriptInterface internal constructor(private val context: Context) {
        @JavascriptInterface
        fun setHeader(s: String?) {
            runOnUiThread { title = s }
        }


        @JavascriptInterface
        fun openActivity(data: String?) {
            startActivity(Intent(context, TradeActivity::class.java))
        }

        @JavascriptInterface
        fun shareItem(imagePath: String, body: String, returnPath: String) {
            shareProduct(imagePath, body, returnPath)
        }

        @JavascriptInterface
        fun closeTrade() {
            if (!TextUtils.isNullOrEmpty(
                    SharePrefs.getInstance(
                        this@DirectUdharActivity
                    ).getString(SharePrefs.CLUSTER_ID)
                )
            ) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            } else {
//                finish();
            }
        }

        @JavascriptInterface
        fun editcustomer(b: Boolean) {
            if (b) {
                Utils.setToast(
                    context,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_update_your_profile_to_access_trade)
                )
                startActivity(Intent(context, EditProfileActivity::class.java))
            } else {
                startActivity(Intent(context, EditProfileActivity::class.java))
            }
            finish()
        }

        @JavascriptInterface
        fun showToast(toast: String) {
            showToastMessage(toast)
        }

        @JavascriptInterface
        fun toneSoundPath() {
            ringtone()
        }

        @JavascriptInterface
        fun openSellerBuyerSelectActivity(data: String?) {
            startActivity(Intent(context, HomeActivity::class.java))
        }

        @JavascriptInterface
        fun sendNotification(data: String) {
            setNotification(data, "Direct")
        }

        @JavascriptInterface
        fun shareWhatsapp(message: String, number: String) {
            showShareWhatsappDialog(message, number)
        }

        @JavascriptInterface
        fun exitApp() {
            closeApp()
        }

        @JavascriptInterface
        fun clearCache() {
            clearWebViewCache()
        }

        @JavascriptInterface
        fun openApp(AppName: String?, PackageName: String) {
            Open(PackageName)
        }

        @JavascriptInterface
        fun shareText(text: String) {
            shareText1(text)
        }

        @JavascriptInterface
        fun callNumber(text: String) {
            Call(text)
        }


        @JavascriptInterface
        fun reloadPage() {
            reloadPageview()
        }

        @JavascriptInterface
        fun redirectPage(url: String) {
            redirectPageview(url)
        }

        @JavascriptInterface
        fun openUrlInBrowser(url: String) {
            urlOpenInBrowser(url)
        }

        @JavascriptInterface
        fun askPermission() {
            accessCameraPermission()
        }

        @JavascriptInterface
        fun currentLocation(): String {
            return currentLatLong()
        }

        @JavascriptInterface
        fun closeRazorDialog() {
            closeDialogRazor()
        }

        @get:JavascriptInterface
        val cameraPermission: Boolean
            get() = callRunTimePermissions()

        @JavascriptInterface
        fun openPaymentPage() {
            startActivity(Intent(applicationContext, PaymentOptionActivity::class.java))
        }

        @JavascriptInterface
        fun openHome() {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
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
        fun scaleUpPaymentSuccess(
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
        fun scaleUpPaymentFailure(
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
        fun setContentType(s: String) {
            contentType = s
        }

        @JavascriptInterface
        fun initiateUPI(url: String?) {
            if (BuildConfig.DEBUG) println(url)
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            val chooser = Intent.createChooser(intent, "Pay with...")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(chooser, UPI_REQUEST_CODE, null)
            }
        }

        @JavascriptInterface
        fun downloadpdf(invoiceURl: String) {
            Log.d("TAG", "downloadpdf: $invoiceURl")
            downloadFileFromUrl(invoiceURl)
        }
    }

    private inner class PaymentInterface {
        @JavascriptInterface
        fun success(data: String?) {
        }

        @JavascriptInterface
        fun error(data: String?) {
        }
    }

    private val target: Target = object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
            Utils.hideProgressDialog()
            try {
                val filename = Constant.PRODUCT_IMAGE_FOLDER
                val sd = Environment.getExternalStorageDirectory()
                val dest = File(sd, filename)
                if (!dest.exists()) {
                    dest.mkdirs()
                }
                val out = FileOutputStream("$dest/image.png")
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        }
    }


    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "UdharUPINotification") {
                mBinding!!.webview.loadUrl(intent.getStringExtra("url")!!)
            }
        }
    }

    companion object {
        private fun isExternalStorageDocument(uri: Uri?): Boolean {
            return "com.android.externalstorage.documents" == uri!!.authority
        }

        private fun isDownloadsDocument(uri: Uri?): Boolean {
            return "com.android.providers.downloads.documents" == uri!!.authority
        }

        private fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }
    }
}