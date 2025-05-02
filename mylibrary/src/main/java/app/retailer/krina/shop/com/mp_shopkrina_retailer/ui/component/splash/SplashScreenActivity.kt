package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.AppVersionModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse.CompanyDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityLoginBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivitySplashScreenBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.firebase.FirebaseLanguageFetch
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ContactUsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MaintenanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.services.ContactService
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SaveCustomerLocalInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewUtils.Companion.snackbar
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class SplashScreenActivity : AppCompatActivity() {
    private val APP_PACKAGE_COUNT = 6
    private var REQUEST_FROM = 101
    private lateinit var mBinding: ActivitySplashScreenBinding
    private lateinit var viewModel: SplashViewModel
    private var mAppVersion: String? = null
    private var isCompulsory = false
    private var isPresent = false
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var isCallLogin: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            SplashViewModelFactory(application, appRepository)
        )[SplashViewModel::class.java]
        observe(viewModel.versionData, ::handleAppVersionResult)
        observe(viewModel.getProfileData, ::handleProfileResult)
        observe(viewModel.companyDetails, ::handleCompanyDetailsResult)
        observe(viewModel.tokenData, ::handleResultToken)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (REQUEST_FROM == 101 && result.resultCode == RESULT_OK) {
                    baseUrl()
                } else if (result.resultCode == RESULT_CANCELED) {
                    Log.d("Result:", "Cancel")
                }
            }
        // check for clone
        checkCloner()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
       FirebaseApp.initializeApp(this)
        mBinding.tvVersion.text = "App version " + Constant.VERSION_NAME
        mBinding.btnTryAgain.setOnClickListener { loadingData() }
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(2)
            .build()
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        //set default values
        mFirebaseRemoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.IS_DIALOG_SHOWN, false)
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.IS_UDHAAR_OVERDUE, false)
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.IS_UDHAAR_ORDER, false)
        SharePrefs.getInstance(applicationContext)
            .putBoolean(SharePrefs.IS_UDHAAR_POPUP_OPEN_IN_FIRST_TIME, true)

        // internet receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"), Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        }
        // inApp update
        appUpdateManager = AppUpdateManagerFactory.create(this)
        inAppUpdate()
        // get base url's
        baseUrl()
        LocaleHelper.setLocale(applicationContext, LocaleHelper.getLanguage(applicationContext))
        Utils.setLocale(
            applicationContext, LocaleHelper.getLanguage(
                applicationContext
            )
        )
        languageForBundle()
        MyApplication.getInstance().noteRepository.deleteFeed()
        MyApplication.getInstance().checkLastLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(netConnectionReceiver)
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(applicationContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    // for testing comment above & unComment below - dev
    private fun baseUrl() {
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener(this) { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    val baseUrl = mFirebaseRemoteConfig!!.getString("baseUrl")
                    val tradeUrl = mFirebaseRemoteConfig!!.getString("tradeUrl")
                    val ePayUrl = mFirebaseRemoteConfig!!.getString("ePayUrl")
                    val checkBookUrl = mFirebaseRemoteConfig!!.getString("checkBookUrl")
                    val cartPosition = mFirebaseRemoteConfig!!.getLong("cartPosition")
                    val searchPosition = mFirebaseRemoteConfig!!.getLong("searchPosition")
                    val isMaintenance = mFirebaseRemoteConfig!!.getBoolean("isMaintenance")
                    val isAnalytic = mFirebaseRemoteConfig!!.getBoolean("isAnalytic")
                    val isMixpanel = mFirebaseRemoteConfig!!.getBoolean("isMixpanel")
                    val isVanRtgs = mFirebaseRemoteConfig!!.getBoolean("isVanRtgs")
                    val areaRadius = mFirebaseRemoteConfig!!.getLong("areaRadius")
                    val addressRadius = mFirebaseRemoteConfig!!.getLong("addressRadius")
                    val showChatBot = mFirebaseRemoteConfig!!.getBoolean("showChatBot")
                    val clearanceMinOrder = mFirebaseRemoteConfig!!.getLong("clearanceMinOrder")
                    val isShowSeller = mFirebaseRemoteConfig!!.getBoolean("isShowSeller")
                    val isClShowCOD = mFirebaseRemoteConfig!!.getBoolean("isClShowCOD")
                    val imageUploadQty = mFirebaseRemoteConfig!!.getLong("imageUploadQty")
                    val logOutDays = mFirebaseRemoteConfig!!.getLong("logOutDays")
                    val showNewSocial = mFirebaseRemoteConfig!!.getBoolean("showNewSocial")
                    val showOfferBtn = mFirebaseRemoteConfig!!.getBoolean("showOfferBtn")
                    val isScaleUp = mFirebaseRemoteConfig!!.getBoolean("isScaleUp")
                    val isRazorpayPayment = mFirebaseRemoteConfig!!.getBoolean("isRazorpayPayment")
                    val isICICIpayPayment = mFirebaseRemoteConfig!!.getBoolean("isICICIPayment")
                    val isScaleUpSdk = mFirebaseRemoteConfig!!.getBoolean("isScaleUpSdk")
                    val hdfcNewUI = mFirebaseRemoteConfig!!.getBoolean("hdfcNewUI")

                    EndPointPref.getInstance(applicationContext)
                        .putString(EndPointPref.API_ENDPOINT, baseUrl)
                    EndPointPref.getInstance(applicationContext)
                        .putString(EndPointPref.TRADE_ENDPOINT, tradeUrl)
                    EndPointPref.getInstance(applicationContext)
                        .putString(EndPointPref.EPAY_ENDPOINT, ePayUrl)
                    EndPointPref.getInstance(applicationContext)
                        .putString(EndPointPref.CHECKBOOK_ENDPOINT, checkBookUrl)
                    EndPointPref.getInstance(applicationContext)
                        .putInt(EndPointPref.CART_POSITION, cartPosition.toInt())
                    EndPointPref.getInstance(applicationContext)
                        .putInt(EndPointPref.SEARCH_POSITION, searchPosition.toInt())
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_ANALYTIC, isAnalytic)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_MIXPANEL, isMixpanel)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_VAN_RTGS, isVanRtgs)
                    EndPointPref.getInstance(applicationContext).putInt(
                        EndPointPref.AREA_RADIUS,
                        areaRadius.toInt()
                    )
                    EndPointPref.getInstance(applicationContext)
                        .putInt(EndPointPref.ADDRESS_RADIUS, addressRadius.toInt())
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.Show_Chat_Bot, showChatBot)
                    EndPointPref.getInstance(applicationContext)
                        .putLong(EndPointPref.CLEARANCE_MIN_ORDER, clearanceMinOrder)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_SHOW_SELLER, isShowSeller)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_CL_SHOW_COD, isClShowCOD)
                    EndPointPref.getInstance(applicationContext)
                        .putLong(EndPointPref.IMAGE_UPLOAD_QTY, imageUploadQty)
                    EndPointPref.getInstance(applicationContext)
                        .putLong(EndPointPref.logOutDays, logOutDays)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.showNewSocial, showNewSocial)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.showOfferBtn, showOfferBtn)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_SCALEUP, isScaleUp)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_RAZORPAY_PAYMENT, isRazorpayPayment)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_ICICI_PAYMENT, isICICIpayPayment)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.IS_SCALE_UP_SDK, isScaleUpSdk)
                    EndPointPref.getInstance(applicationContext)
                        .putBoolean(EndPointPref.HDFC_NEW_UI, hdfcNewUI)
                    // for testing comment above & unComment below - dev
                    if (BuildConfig.DEBUG) {
//                        EndPointPref.getInstance(applicationContext).putString(EndPointPref.API_ENDPOINT, "https://das.er15.xyz")
 //                       EndPointPref.getInstance(applicationContext).putString(EndPointPref.API_ENDPOINT, "https://internal.er15.xyz")
//                        EndPointPref.getInstance(applicationContext).putString(EndPointPref.TRADE_ENDPOINT, "https://tradeservice.er15.xyz:4436")
//                        EndPointPref.getInstance(applicationContext).putString(EndPointPref.EPAY_ENDPOINT, "https://api2.epaylater.in:443")
//                        EndPointPref.getInstance(applicationContext).putString(EndPointPref.CHECKBOOK_ENDPOINT, "https://www.chqbook.com/api/cl/pg/account")
                    }
                    if (isMaintenance) {
                        callMaintenance()
                    } else
                        callAPI()
                }
            }.addOnFailureListener { e: Exception ->
                e.printStackTrace()
                callAPI()
            }.addOnCanceledListener {
                callAPI()
            }
    }

    private fun callAPI() {
        MyApplication.getInstance().clearLocalData()
        viewModel.callAppVersion()
    }

    private fun inAppUpdate() {
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        101
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showAlert(isCompulsory: Boolean) {
        @SuppressLint("RestrictedApi") val alertDialogBuilder = AlertDialog.Builder(
            ContextThemeWrapper(this, R.style.Base_Theme_AppCompat_Dialog)
        )
        alertDialogBuilder.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.youAreNotUpdatedTitle))
        alertDialogBuilder.setMessage(
            MyApplication.getInstance().dbHelper.getString(R.string.youAreNotUpdatedMessage)
                    + " " + mAppVersion + " " + MyApplication.getInstance().dbHelper.getString(R.string.youAreNotUpdatedMessage1)
        )
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton(
            MyApplication.getInstance().dbHelper.getString(R.string.update)
        ) { dialog: DialogInterface, id: Int ->
            dialog.cancel()
            if (BuildConfig.DEBUG)
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://drive.google.com/drive/folders/14Br_7yqy4Ty0pWCF36qQ1SXHMcXXpI3M")
                    )
                )
            else
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=app.retailer.krina.shop.com.shopkrina_retailer&hl=en")
                    )
                )
        }
        alertDialogBuilder.setNegativeButton(
            MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        ) { dialog: DialogInterface, i: Int ->
            dialog.cancel()
            finish()
        }
        alertDialogBuilder.show()
    }

    /**
     * Home screen result
     */
    private fun ePayCredentials(model: CompanyDetails?) {
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.E_PAY_LATER_URL, model!!.ePAYLATERURL)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.COMPANY_CONTACT, model.contact)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.ENCODED_KEY, model.eNCODEDKEY)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.BEARER_TOKEN, model.bEARERTOKEN)
        SharePrefs.getInstance(applicationContext).putString(SharePrefs.IV, model.iV)
        SharePrefs.getInstance(applicationContext).putString(SharePrefs.M_CODE, model.mCODE)
        SharePrefs.getInstance(applicationContext).putString(SharePrefs.CATEGORY, model.category)
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.IS_COMPANY_API_CALL, false)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.IS_PAYMENT_GATWAY, model.gatewayName)
    }

    private fun saveHDFCCredential(model: CompanyDetails?) {
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.MERCHANT_ID, model!!.hDFCMerchantId)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.ACCESS_CODE, model.hDFCAccessCode)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.WORKING_KEY, model.hDFCWorkingKey)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.REDIRECT_URL, model.redirect_url)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.CANCEL_URL, model.cancel_url)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.GATWAY_URL, model.gatewayURL)
        // hdfc credit option
        SharePrefs.getInstance(applicationContext)
            .putBoolean(SharePrefs.IS_SHOW_CREDIT, model.isShowCreditOption)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_MERCHANT_ID, model.creditMerchantId)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_OPTION_NAME, model.creditOptionName)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_GATEWAY_URL, model.creditGatewayURL)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_WORKING_KEY, model.creditWorkingKey)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_ACCESS_CODE, model.creditAccessCode)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_REDIRECT_URL, model.creditRedirectUrl)
        SharePrefs.getInstance(applicationContext)
            .putString(SharePrefs.C_CANCEL_URL, model.creditCancelUrl)
        mBinding!!.pBar.visibility = View.GONE

        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        } else if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_SELLER_AVAIL)) {
            if (EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.showNewSocial))
                startActivity(Intent(applicationContext, FeedActivity::class.java))
            else
                startActivity(Intent(applicationContext, TradeActivity::class.java))
        } else if (!SharePrefs.getInstance(this)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL) && !SharePrefs.getInstance(this)
                .getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)
        ) {
            if (EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.showNewSocial))
                startActivity(Intent(applicationContext, FeedActivity::class.java))
            else
                startActivity(Intent(applicationContext, TradeActivity::class.java))
        }
        Utils.leftTransaction(this)
        finish()
    }

    private fun loadingData() {
        MyApplication.getInstance().clearLocalData()
        mBinding!!.btnTryAgain.visibility = View.GONE
        mBinding!!.pBar.visibility = View.VISIBLE
        viewModel.getMyProfile(
            SharePrefs.getInstance(
                applicationContext
            ).getInt(SharePrefs.CUSTOMER_ID) , Utils.getDeviceUniqueID(this)
        )
    }

    // Uses the addLanguage() method to include Hindi language resources in the request.
    // Note that country codes are ignored. That is, if your app
    // includes resources for “fr-FR” and “fr-CA”, resources for both
    // country codes are downloaded when requesting resources for "fr".
    // Submits the request to install the additional language resources.
    // Creates an instance of SplitInstallManager.
    @SuppressLint("NewApi")
    private fun languageForBundle() {
        // Creates an instance of SplitInstallManager.
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        // Creates a request to download and install additional language resources.
        val request =
            SplitInstallRequest.newBuilder()
                .addModule("bucketgame")
                // Uses the addLanguage() method to include French language resources in the request.
                // Note that country codes are ignored. That is, if your app
                // includes resources for “fr-FR” and “fr-CA”, resources for both
                // country codes are downloaded when requesting resources for "fr".
                .addLanguage(Locale.forLanguageTag("hi"))
                .build()

        // Submits the request to install the additional language resources.
        splitInstallManager.startInstall(request)
    }

    private val netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent1: Intent) {
            val status = Utils.getConnectivityStatusString(context)
            if (!status) {
                REQUEST_FROM = 101
                var intent = Intent(applicationContext, NoInternetActivity::class.java)
                intent = Intent("netStatus")
                intent.putExtra("status", status)
                resultLauncher?.launch(intent)
                Utils.leftTransaction(this@SplashScreenActivity)
            }
        }
    }

    private fun handleAppVersionResult(it: Response<ArrayList<AppVersionModel>>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        if (it != null && it.size != 0) {
                            for (i in it.indices) {
                                mAppVersion = it[i].app_version
                                isCompulsory = it[i].isCompulsory
                                if (Constant.VERSION_NAME.equals(
                                        mAppVersion,
                                        ignoreCase = true
                                    ) && isCompulsory
                                ) {
                                    SharePrefs.setStringSharedPreference(
                                        applicationContext,
                                        SharePrefs.APP_VERSION,
                                        mAppVersion
                                    )
                                    isPresent = true
                                    break
                                } else {
                                    isPresent = false
                                    isCallLogin = false
                                    val mobileNo = SharePrefs.getInstance(this@SplashScreenActivity)
                                        .getString(SharePrefs.MOBILE_NUMBER)
                                    if (mobileNo.isNullOrEmpty()) {
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                MobileSignUpActivity::class.java
                                            )
                                                .putExtra("isSplash", true)
                                        )
                                        finish()
                                        isCallLogin = true
                                    } else if (mobileNo == "9752640201") {
                                        isPresent = true
                                    }
                                }
                            }
                            if (!isCallLogin) {
                                if (isPresent) {
                                    if (MyApplication.getInstance().prefManager.isLoggedIn) {
                                        loadingData()
                                    } else {
                                        mBinding!!.pBar.visibility = View.GONE
                                        // NewSignUpActivity
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                MobileSignUpActivity::class.java
                                            )
                                                .putExtra("isSplash", true)
                                        )
                                        finish()
                                        Utils.fadeTransaction(this@SplashScreenActivity)
                                    }
                                } else {
                                    SharePrefs.setStringSharedPreference(
                                        applicationContext,
                                        SharePrefs.APP_VERSION,
                                        mAppVersion
                                    )
                                    showAlert(isCompulsory)
                                }
                            }
                        } else {
                            mBinding!!.root.snackbar(
                                MyApplication.getInstance().dbHelper.getString(
                                    R.string.no_response
                                )
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                callMaintenance()
            }
        }
    }

    private fun handleProfileResult(it: Response<MyProfileResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        if (it.isLogOutFromThisDevice) {
                            MyApplication.getInstance().logout(this@SplashScreenActivity)
                            val intent =
                                Intent(applicationContext, MobileSignUpActivity::class.java)
                            intent.putExtra("isSplash", true)
                            startActivity(intent)
                            finish()
                        } else if (it.isStatus) {
                            SharePrefs.getInstance(applicationContext).putString(
                                SharePrefs.CRITICAL_INFO_MISSING_MSG,
                                it.criticalInfoMissingMsg
                            )
                            val customer = it.customers
                            if (customer != null) {
                                SaveCustomerLocalInfo.saveCustomerInfo(
                                    applicationContext,
                                    customer,
                                    true
                                )
                                if (customer.registeredApk != null) {
                                    SaveCustomerLocalInfo.saveTokenInfo(
                                        applicationContext,
                                        customer.registeredApk
                                    )
                                }
                                SharePrefs.getInstance(applicationContext).putBoolean(
                                    SharePrefs.IS_UDHAAR_OVERDUE,
                                    it.isUdharOverDue
                                )
                                if (!customer.isContactsRead) {
                                    startService(
                                        Intent(
                                            applicationContext,
                                            ContactService::class.java
                                        )
                                    )
                                }
                                if (customer.isResetPasswordOnLogin) {
                                    MyApplication.getInstance().logout(this@SplashScreenActivity)
                                    val intent =
                                        Intent(applicationContext, ContactUsActivity::class.java)
                                    intent.putExtra("Type", "ResetPasswordActivity")
                                    startActivity(intent)
                                    finish()
                                } else {
                                    viewModel.callCompanyDetailsApi(
                                        customer.customerId,
                                        "App start screen"
                                    )
                                }
                            }
                        } else {
                            // mBinding!!.btnTryAgain.visibility = View.VISIBLE
                            mBinding!!.pBar.visibility = View.GONE
                            Utils.setToast(
                                applicationContext, it.message
                            )
                            if (it.message.equals(
                                    "Customer not exist.",
                                    ignoreCase = true
                                )
                            ) {
                                MyApplication.getInstance().logout(this@SplashScreenActivity)
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        mBinding!!.btnTryAgain.visibility = View.VISIBLE
                        mBinding!!.pBar.visibility = View.GONE
                    }
                }
            }
            is Response.Error -> {
                try {
                    if (it.errorMesssage!! == "401") {
                        if (TextUtils.isNullOrEmpty(
                                SharePrefs.getInstance(
                                    applicationContext
                                ).getString(SharePrefs.TOKEN_NAME)
                            )
                        ) {
                            MyApplication.getInstance().logout(this@SplashScreenActivity)
                        } else {
                            viewModel.callToken(
                                "password",
                                SharePrefs.getInstance(applicationContext)
                                    .getString(SharePrefs.TOKEN_NAME),
                                SharePrefs.getInstance(applicationContext)
                                    .getString(SharePrefs.TOKEN_PASSWORD)
                            )
                        }
                    } else {
                        mBinding!!.btnTryAgain.visibility = View.VISIBLE
                        mBinding!!.pBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    callMaintenance()
                }
            }
        }
    }

    private fun handleCompanyDetailsResult(it: Response<CompanyInfoResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FinboxClientApiKey,
                        it.companyDetails!!.finboxclientApikey
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_SHOW_LEDGER, it.companyDetails.isShowLedger)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_SHOW_TARGET, it.companyDetails.isShowTarget)
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.MAX_WALLET_POINT_USED,
                        it.companyDetails.maxWalletPointUsed
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_SHOW_RETURN_ORDER,
                        it.companyDetails.isShowReturn
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.WEB_VIEW_BASE_URL,
                        it.companyDetails.webViewBaseUrl
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_SHOW_HISAB,
                        it.companyDetails.isShowHisabKitab
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.TRADE_WEB_URL,
                        it.companyDetails.tradeWebViewURL
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_SHOW_TICKET_MENU,
                        it.companyDetails.isShowTicketMenu
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_PRIME_ACTIVE,
                        it.companyDetails.isPrimeActive
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_CHECKBOOK_SHOW,
                        it.companyDetails.isIscheckBookShow
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_HDFC_PAYMENT,
                        it.companyDetails.isOnlinePayment
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_EPAY_LATER_SHOW,
                        it.companyDetails.isIsePayLaterShow
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.PRIME_AMOUNT,
                        "" + it.companyDetails.primeAmount
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.CHECKBOOK_API_KEY,
                        "" + it.companyDetails.checkBookAPIKey
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.CHECKBOOK_BASE_URL,
                        "" + it.companyDetails.checkBookBaseURl
                    )
                    SharePrefs.getInstance(applicationContext).putInt(
                        SharePrefs.PRIME_DURATION,
                        it.companyDetails.primeMemberShipInMonth
                    )
                    SharePrefs.getInstance(applicationContext).putInt(
                        SharePrefs.IS_ChQBOOKMINI_AMT,
                        it.companyDetails.ischeckBookMinAmt
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.ISFINBOX, it.companyDetails.isFinBox)
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_SHOW_CREDIT_LINE,
                        it.companyDetails.isCreditLineShow
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FINBOX_CREDIT_KEY,
                        it.companyDetails.finBoxCreditKey
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_SHOW_VATM, it.companyDetails.isShowVATM)
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_DIRECT_UDHAR,
                        it.companyDetails.isCreditLendingEnable
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_D_UDHAR_GULLAK,
                        it.companyDetails.isDUdharGullakShow
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FIRESTORE_APPLICATION_ID,
                        it.companyDetails.firestoreApplicationId
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FIRESTORE_API_KEY,
                        it.companyDetails.firestoreApiKey
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FIRESTORE_DATABASE_URL,
                        it.companyDetails.firestoreDatabaseUrl
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.FIRESTORE_PROJECT_ID,
                        it.companyDetails.firestoreProjectId
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.AZURE_ACCOUNT_NAME,
                        it.companyDetails.azureAccountName
                    )
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.AZURE_ACCOUNT_KEY,
                        it.companyDetails.azureAccountKey
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putString(
                            SharePrefs.RAZORPAY_KEY_ID,
                            it.companyDetails.razorpayApiKeyId
                        )

                    SharePrefs.getInstance(applicationContext)
                        .putString(
                            SharePrefs.ICICI_MERCHANT_ID,
                            it.companyDetails.iCICIMerchantId
                        )

                    SharePrefs.getInstance(applicationContext)
                        .putString(
                            SharePrefs.ICICI_APP_ID,
                            it.companyDetails.iCICIAppId
                        )

                    SharePrefs.getInstance(applicationContext)
                        .putString(
                            SharePrefs.ICICI_SECRET_KEY,
                            it.companyDetails.iCICISecretKey
                        )

                    SharePrefs.getInstance(applicationContext)
                        .putString(
                            SharePrefs.ICICI_RESULT_URL,
                            it.companyDetails.iCICIPaymentResultUrl
                        )
                    /* // Prod
                      SharePrefs.getInstance(applicationContext).putString(SharePrefs.ICICI_MERCHANT_ID, "P_50518")

                      SharePrefs.getInstance(applicationContext).putString(SharePrefs.ICICI_APP_ID, "80bc18249511f868")

                      SharePrefs.getInstance(applicationContext).putString(SharePrefs.ICICI_SECRET_KEY, "b924b15c9d9e4feb90e4da341b19dda8")

                      SharePrefs.getInstance(applicationContext)
                          .putString(
                              SharePrefs.ICICI_RESULT_URL,
                              "https://secure-ptg.payphi.com/pg/api/command"
                          )*/
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.RAZORPAY_KEY_ID,
                        it.companyDetails.razorpayApiKeyId
                    )
                    val localLastLanguageUpdateDate = SharePrefs.getInstance(applicationContext)
                        .getString(SharePrefs.LAST_LANGUAGE_UPDATE_DATE)
                    val serverLastLanguageUpdateDate = it.companyDetails.languageLastUpdated
                    val originalFormat: DateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    try {
                        if (TextUtils.isNullOrEmpty(localLastLanguageUpdateDate)) {
                            SharePrefs.getInstance(applicationContext).putString(
                                SharePrefs.LAST_LANGUAGE_UPDATE_DATE,
                                serverLastLanguageUpdateDate
                            )
                            SharePrefs.getInstance(applicationContext)
                                .putBoolean(SharePrefs.IS_FETCH_LANGUAGE, true)
                        } else {
                            val serverdate = originalFormat.parse(serverLastLanguageUpdateDate)
                            val localdate = originalFormat.parse(localLastLanguageUpdateDate)
                            if (serverdate.after(localdate)) {
                                SharePrefs.getInstance(applicationContext).putString(
                                    SharePrefs.LAST_LANGUAGE_UPDATE_DATE,
                                    serverLastLanguageUpdateDate
                                )
                                SharePrefs.getInstance(applicationContext)
                                    .putBoolean(SharePrefs.IS_FETCH_LANGUAGE, true)
                            }
                        }
                        if (SharePrefs.getInstance(applicationContext)
                                .getBoolean(SharePrefs.IS_FETCH_LANGUAGE)
                        ) {
                            FirebaseLanguageFetch(applicationContext).fetchLanguage()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (!it.companyDetails.isPrimeActive) {
                        SharePrefs.getInstance(applicationContext)
                            .putBoolean(SharePrefs.IS_PRIME_MEMBER, false)
                    }
                    if (LocaleHelper.getLanguage(applicationContext)
                            .equals("hi", ignoreCase = true)
                    ) {
                        SharePrefs.getInstance(applicationContext).putString(
                            SharePrefs.PRIME_NAME,
                            it.companyDetails.memberShipHindiName
                        )
                    } else {
                        SharePrefs.getInstance(applicationContext).putString(
                            SharePrefs.PRIME_NAME,
                            it.companyDetails.memberShipName
                        )
                    }
                    ePayCredentials(it.companyDetails)
                    saveHDFCCredential(it.companyDetails)
                }
            }

            is Response.Error -> {
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    private fun handleResultToken(it: Response<TokenResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.TOKEN, it.access_token)
                    MyApplication.getInstance().prefManager.isLoggedIn = true
                    MyApplication.getInstance().prefManager.setShowcaseFirstTimeLaunch(true)
                    callAPI()
                }
            }

            is Response.Error -> {}
        }
    }

    private fun callMaintenance() {
        REQUEST_FROM = 101
        val intent = Intent(applicationContext, MaintenanceActivity::class.java)
        resultLauncher?.launch(intent)
        Utils.leftTransaction(this)
    }

    // check for App Clone - MultiSpace - Parallel Space
    private fun checkCloner() {
        val path = this.filesDir.absolutePath
        val count = getDotCount(path)
        println("count $count")
        if (count > APP_PACKAGE_COUNT) {
            Utils.setToast(applicationContext, "This app do not work on clone.")
            finish()
            finishAffinity()
        }
    }

    private fun getDotCount(path: String): Int {
        var count = 0
        for (i in 0 until path.length) {
            if (count > APP_PACKAGE_COUNT) {
                break
            }
            if (path[i] == '.') {
                count++
            }
        }
        return count
    }
}