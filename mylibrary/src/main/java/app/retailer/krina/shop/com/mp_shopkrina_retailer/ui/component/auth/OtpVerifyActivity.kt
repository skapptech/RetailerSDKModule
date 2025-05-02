package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GetLokedCusResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OTPResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OtpVerifyRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.RegistrationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityOtpVerifyBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OtpReceivedInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.receiver.SmsBroadcastReceiver
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AppSignatureHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AutomaticallyEnableGPSLocation
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SaveCustomerLocalInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewUtils.Companion.snackbar
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class OtpVerifyActivity : AppCompatActivity(), OtpReceivedInterface {
    private var cTimer: CountDownTimer? = null
    private var mBinding: ActivityOtpVerifyBinding? = null
    private lateinit var viewModel: AuthViewModel
    private var versionName: String? = null
    private var deviceOs: String? = null
    private var deviceName: String? = null
    private var sMobileNumber: String? = ""
    private var otpresponse: String? = ""
    private var trueCustomer = false
    private var isCustomerAvailable = false
    private var TIMER_DURATION: Long = 30000

    // otp auto fetch
    private var mSmsBroadcastReceiver: SmsBroadcastReceiver? = null
    private var customer: CustomerResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verify)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(application, appRepository)
        )[AuthViewModel::class.java]
        val intent = intent
        if (intent != null) {
            sMobileNumber = intent.getStringExtra("MobileNumber")
            trueCustomer = intent.getBooleanExtra("TRUE_CUSTOMER", false)
            otpresponse = intent.getStringExtra("otpresponse")
        }
        // init broadcast receiver
        mSmsBroadcastReceiver = SmsBroadcastReceiver()
        mSmsBroadcastReceiver!!.setOnOtpListeners(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(mSmsBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else
            registerReceiver(mSmsBroadcastReceiver, intentFilter)
        startSMSListener()
        initialization()
        observe(viewModel.verifyOtpData, ::handleVerifyOtpResult)
        observe(viewModel.customerVerifyData, ::handleCustVerifyResult)
        observe(viewModel.tokenData, ::handleResultToken)
        observe(viewModel.insertCustomerData, ::handleInsertCustResult)
        observe(viewModel.otpData, ::handleResendOtpResult)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //only debug build
        if (BuildConfig.DEBUG) {
            mBinding!!.etOtp.setText(otpresponse)
            mBinding!!.ivVerifyOtp.callOnClick()
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

    override fun onDestroy() {
        super.onDestroy()
        if (mSmsBroadcastReceiver != null) {
            unregisterReceiver(mSmsBroadcastReceiver)
        }
    }

    override fun onBackPressed() {
        cancelTimer()
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.LAST_IS_OTP_SENT)
            && SharePrefs.getInstance(applicationContext).getInt(SharePrefs.LAST_OTP_COUNT) == 3
        ) {
            finishAffinity()
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onOtpReceived(otp: String) {
        if (!BuildConfig.DEBUG) {
            mBinding!!.etOtp.setText(otp + "")
            mBinding!!.ivVerifyOtp.callOnClick()
        }
    }

    override fun onOtpTimeout() {}
    fun initialization() {
        val automaticallyEnableGPSLocation = AutomaticallyEnableGPSLocation()
        automaticallyEnableGPSLocation.EnableGPSAutoMatically(this)
        versionName = Constant.VERSION_NAME
        deviceOs = Build.VERSION.RELEASE
        deviceName = Build.MODEL
        mBinding!!.tvEnterMobileT.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.EnterMobileNumber)
        mBinding!!.tvOtpT.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_sent_otp)
        mBinding!!.btnChngnumber.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.ChangeNumber)
        mBinding!!.tvResendOtp.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.resend_otp)
        mBinding!!.tvMobileNumber.text = sMobileNumber
        mBinding!!.tvResendOtp.isEnabled = false
        mBinding!!.tvResendOtp.setTextColor(resources.getColor(R.color.grey))
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.LAST_IS_OTP_SENT)
            && SharePrefs.getInstance(this).getInt(SharePrefs.LAST_OTP_COUNT) == 3
        ) {
            val lastOtpTime =
                SharePrefs.getInstance(this).getString(SharePrefs.LAST_OTP_TIME).toLong()
            val timeDifference = lastOtpTime - System.currentTimeMillis()
            if (timeDifference > 0) {
                TIMER_DURATION = timeDifference
            } else {
                TIMER_DURATION = 30000
                clearOtpSessionValue()
            }
        } else {
            TIMER_DURATION = if (TextUtils.isNullOrEmpty(
                    SharePrefs.getInstance(this).getString(SharePrefs.LAST_OTP_TIME)
                )
            ) 30000 else SharePrefs.getInstance(this).getString(SharePrefs.LAST_OTP_TIME).toLong()
        }
        startTimer(mBinding!!.tvResendOtpTimer, mBinding!!.tvResendOtp)
        mBinding!!.etOtp.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                mBinding!!.ivVerifyOtp.callOnClick()
                return@setOnEditorActionListener true
            }
            false
        }
        mBinding!!.btnChngnumber.setOnClickListener { v: View? ->
            RetailerSDKApp.getInstance().updateAnalytics("change_no_click")
            mBinding!!.etOtp.setText("")
            cancelTimer()
            startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
            finish()
            Utils.rightTransaction(this)
        }
        mBinding!!.ivVerifyOtp.setOnClickListener { v: View? ->
            RetailerSDKApp.getInstance().updateAnalyticAuth("verify_otp_click", "otp", sMobileNumber)
            if (mBinding!!.etOtp.text.toString().equals("", ignoreCase = true)) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.enteotp)
                )
            } else {
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.MOBILE_NUMBER, sMobileNumber)
                val fcmToken =
                    EndPointPref.getInstance(applicationContext).getFcmToken(EndPointPref.FCM_TOKEN)
                viewModel.callVerifyOtp(
                    OtpVerifyRequest(
                        sMobileNumber!!,
                        Utils.getDeviceUniqueID(this), mBinding!!.etOtp.text.toString(),
                        trueCustomer, versionName!!, deviceOs!!, deviceName!!, fcmToken ?: ""
                    )
                )
            }
        }
        mBinding!!.tvResendOtp.setOnClickListener { v: View? ->
            mBinding!!.tvResendOtp.isEnabled = false
            RetailerSDKApp.getInstance().updateAnalytics("resend_otp_click")
            startSMSListener()
            mBinding!!.etOtp.setText("")
            if (sMobileNumber == "") {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.entermobilenumber)
                )
            } else if (!TextUtils.isValidMobileNo(sMobileNumber)) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.validMobilenumbe)
                )
            } else {
                viewModel.genLoginOtp(
                    sMobileNumber!!, Utils.getDeviceUniqueID(
                        this@OtpVerifyActivity
                    ), AppSignatureHelper(
                        applicationContext
                    ).appSignatures
                )
            }
            val handler = Handler()
            handler.postDelayed({ mBinding!!.tvResendOtp.isEnabled = true }, 1000)
        }
    }

    fun startSMSListener() {
        val mClient = SmsRetriever.getClient(this)
        val mTask = mClient.startSmsRetriever()
        mTask.addOnSuccessListener { aVoid: Void? -> }
        mTask.addOnFailureListener { e: Exception? -> }
    }

    fun clearOtpSessionValue() {
        SharePrefs.getInstance(applicationContext).putString(SharePrefs.LAST_MOBILE_NUMBER, "")
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.LAST_TRUE_CUSTOMER, false)
        SharePrefs.getInstance(applicationContext).putString(SharePrefs.LAST_OTP_TIME, "")
        SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.LAST_IS_OTP_SENT, false)
        SharePrefs.getInstance(applicationContext).putInt(SharePrefs.LAST_OTP_COUNT, 1)
    }

    // start timer function
    private fun startTimer(tvResendOtpTimer: TextView, textView: TextView) {
        cancelTimer()
        mBinding!!.RLTimer.visibility = View.VISIBLE
        mBinding!!.progressbar1Timerview.max = TIMER_DURATION.toInt()
        cTimer = object : CountDownTimer(TIMER_DURATION, 100) {
            override fun onTick(mills: Long) {
                mBinding!!.progressbar1Timerview.progress = mills.toInt()
                if (mills >= 60000) {
                    tvResendOtpTimer.text = "" +
                            (TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(mills)
                            )) + ":" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + ""
                } else {
                    tvResendOtpTimer.text = "" +
                            (TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mills)
                            )) + ""
                }
            }

            override fun onFinish() {
                textView.isEnabled = true
                textView.setBackgroundResource(R.drawable.rectangle)
                textView.setPadding(8, 8, 8, 8)
                textView.setTextColor(resources.getColor(R.color.colorAccent))
                mBinding!!.RLTimer.visibility = View.INVISIBLE
                if (TIMER_DURATION == 30000L) {
                    TIMER_DURATION = 45000
                    SharePrefs.getInstance(applicationContext).putInt(SharePrefs.LAST_OTP_COUNT, 2)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_MOBILE_NUMBER, sMobileNumber)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_TRUE_CUSTOMER, trueCustomer)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_OTP_TIME, 45000.toString())
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_IS_OTP_SENT, true)
                } else if (TIMER_DURATION == 45000L) {
                    SharePrefs.getInstance(applicationContext).putInt(SharePrefs.LAST_OTP_COUNT, 3)
                    TIMER_DURATION = 3600000
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_MOBILE_NUMBER, sMobileNumber)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_TRUE_CUSTOMER, trueCustomer)
                    SharePrefs.getInstance(applicationContext).putString(
                        SharePrefs.LAST_OTP_TIME,
                        (System.currentTimeMillis() + 3600000).toString()
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_IS_OTP_SENT, true)
                }
            }
        }
        cTimer!!.start()
    }

    // cancel timer
    private fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun launchHomeScreen() {
        SharePrefs.getInstance(applicationContext)
            .putString(
                SharePrefs.LAST_LOGIN_DATE,
                SimpleDateFormat("dd/MM/yyyy").format(Date())
            )
        RetailerSDKApp.getInstance().clearLocalData()
        RetailerSDKApp.getInstance().clearCartData()
        RetailerSDKApp.getInstance().prefManager.isLoggedIn = true
        // start analytic new session
        RetailerSDKApp.getInstance().startAnalyticSession()
        startActivity(
            Intent(applicationContext, SplashScreenActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
        Utils.leftTransaction(this)
    }

    // Second OTP verify and getting user data when cust already reg
    private fun handleVerifyOtpResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it) {
                        clearOtpSessionValue()
                        val fcmToken = EndPointPref.getInstance(applicationContext)
                            .getFcmToken(EndPointPref.FCM_TOKEN)
                        // Second OTP verify and getting user data when cust already reg
                        viewModel.getCustVerifyInfo(
                            sMobileNumber!!,
                            "true", fcmToken, versionName!!, deviceOs!!, deviceName!!,
                            Utils.getDeviceUniqueID(this@OtpVerifyActivity)
                        )
                        cancelTimer()
                    } else {
                        mBinding!!.etOtp.setText("")
                        mBinding!!.root.snackbar(RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_correct_otp))
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    // Second OTP verify and getting user data when cust already reg
    private fun handleCustVerifyResult(it: Response<GetLokedCusResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    try {
                        customer = it.customers
                        if (customer != null) {
                            lifecycleScope.launch {
                                SaveCustomerLocalInfo.saveCustomerInfo(
                                    applicationContext,
                                    customer,
                                    false
                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (it.status.equals("true")) {
                        val regApk = customer!!.registeredApk
                        RetailerSDKApp.getInstance()
                            .updateAnalyticAuth(FirebaseAnalytics.Event.LOGIN, "otp", sMobileNumber)
                        if (regApk != null) {
                            val password = regApk.password
                            val username = regApk.userName
                            SaveCustomerLocalInfo.saveTokenInfo(applicationContext, regApk)
                            // If customer create already the these call and go to home
                            isCustomerAvailable = true
                            viewModel.callToken("password", username!!, password)
                        } else {
                        }
                    } else {
                        if (customer != null) {
                            val regApk = customer!!.registeredApk
                            if (regApk != null) {
                                val password = regApk.password
                                val username = regApk.userName
                                SaveCustomerLocalInfo.saveTokenInfo(applicationContext, regApk)
                                // If customer create already the these call and go to home
                                isCustomerAvailable = true
                                viewModel.callToken("password", username!!, password)
                            } else {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        CustomerAddressActivity::class.java
                                    )
                                )
                                finish()
                            }
                        } else {
                            // Third if customer not exist then new Customer reg
                            viewModel.insertCustomer(
                                SharePrefs.getInstance(applicationContext)
                                    .getString(SharePrefs.MOBILE_NUMBER)
                            )
                        }
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    //gen token
    private fun handleResultToken(it: Response<TokenResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.TOKEN, it.access_token)
                    if (isCustomerAvailable) {
                        if (TextUtils.isNullOrEmpty(
                                SharePrefs.getInstance(
                                    applicationContext
                                ).getString(SharePrefs.CITY_NAME)
                            )
                        ) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    CustomerAddressActivity::class.java
                                )
                            )
                            Utils.leftTransaction(this)
                            finish()
                        } else launchHomeScreen()
                    } else {
                        if (customer != null) {
                            launchHomeScreen()
                        } else {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    CustomerAddressActivity::class.java
                                )
                            )
                            finish()
                            Utils.leftTransaction(this@OtpVerifyActivity)
                        }
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog();
            }
        }
    }

    // Third if customer not exist create then new Customer reg
    private fun handleInsertCustResult(it: Response<RegistrationResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    SharePrefs.getInstance(applicationContext)
                        .putInt(SharePrefs.CUSTOMER_ID, it.customerid)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.SK_CODE, it.skcode)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_SIGN_UP, it.isSignup)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.CUST_ACTIVE, it.isActive)
                    val regApk = it.registeredApk
                    RetailerSDKApp.getInstance()
                        .updateAnalyticAuth(
                            FirebaseAnalytics.Event.SIGN_UP,
                            "mobile",
                            sMobileNumber
                        )
                    if (regApk != null) {
                        val password = regApk.password
                        val username = regApk.userName
                        SaveCustomerLocalInfo.saveTokenInfo(applicationContext, regApk)
                        // If customer create already the these call and go to home
                        isCustomerAvailable = false
                        viewModel.callToken("password", username!!, password)
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog();
            }
        }
    }

    //Resend otp
    private fun handleResendOtpResult(it: Response<OTPResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    val otpresponse = it.otpNo
                    if (otpresponse != null) {
                        if (otpresponse.equals("You are not authorize", ignoreCase = true)) {
                            Utils.setToast(
                                applicationContext, otpresponse
                            )
                        } else {
                            mBinding!!.tvResendOtp.isEnabled = false
                            mBinding!!.tvResendOtp.setTextColor(resources.getColor(R.color.grey))
                            mBinding!!.tvResendOtp.setBackgroundResource(android.R.color.transparent)
                            mBinding!!.tvResendOtp.setPadding(8, 8, 8, 8)
                            startTimer(mBinding!!.tvResendOtpTimer, mBinding!!.tvResendOtp)
                        }
                    }

                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }
}