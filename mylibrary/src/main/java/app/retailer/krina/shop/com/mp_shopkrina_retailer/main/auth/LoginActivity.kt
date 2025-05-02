package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth

import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OTPResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.UserAuth
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityLoginBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ContactUsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LoginModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LoginResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.services.OnClearFromRecentService
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.OtpVerifyActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AppSignatureHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Validation
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.lang.Boolean
import kotlin.CharSequence
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.Throwable
import kotlin.toString

class LoginActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var mBinding: ActivityLoginBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var customDialog: Dialog? = null
    private var versionName: String? = null
    private var deviceOs: String? = null
    private var deviceName: String? = null
    private var customer: CustomerResponse? = null
    private var autoNumberhendle = false
    private var mobileNumber = ""
    private var phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initialization()
    }

    override fun onClick(v: View) {
        attemptLogin()
    }

    public override fun onDestroy() {
        super.onDestroy()
        callLoginDes?.dispose()
        callTokenDes?.dispose()
        getOTPDes?.dispose()
        postMobileObserver?.dispose()
        forgotPassDes?.dispose()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(applicationContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    fun initialization() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        versionName = Constant.VERSION_NAME
        deviceOs = Build.VERSION.RELEASE
        deviceName = Build.MODEL
        val value: String =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.IsSignup)
        if (value != null) {
            if (value.equals("false", ignoreCase = true)) {
                mBinding!!.llForgotpassword.visibility = View.GONE
                mBinding!!.llResendotp.visibility = View.VISIBLE
            } else {
                mBinding!!.llForgotpassword.visibility = View.VISIBLE
                mBinding!!.llResendotp.visibility = View.GONE
            }
        }
        mBinding!!.etEmaillogin.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.mobile_number))
        mBinding!!.etPasswordlogin.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.hint_passwordr))
        mBinding!!.tvForgotpassword.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.forget_passwordr))
        mBinding!!.tvResendotp.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.resend_otp))
        mBinding!!.btnLogin.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.login))
        mBinding!!.tvDontHaveAccount.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.don_t_have_an_account))
        mBinding!!.tvSignUp.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.sign_up))
        mBinding!!.etPasswordlogin.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                attemptLogin()
                return@setOnEditorActionListener true
            }
            false
        }
        mBinding!!.etEmaillogin.removeTextChangedListener(textWatcher)
        mBinding!!.btnLogin.setOnClickListener(this)
        mBinding!!.tvSignUp.setOnClickListener { v: View? ->
            RetailerSDKApp.getInstance().updateAnalytics("signup_click")
            startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
            Utils.leftTransaction(this)
        }
        // forgot pass fun
        mBinding!!.tvForgotpassword.setOnClickListener { v: View? ->
            RetailerSDKApp.getInstance().updateAnalytics("forgot_password_click")
            forgotPasswordPopup()
        }
        mBinding!!.llResendotp.setOnClickListener { v: View? ->
            RetailerSDKApp.getInstance().updateAnalytics("resend_otp_click")
            val email = mBinding!!.etEmaillogin.text.toString().trim { it <= ' ' }
            if (TextUtils.isNullOrEmpty(email)) {
                mBinding!!.etEmaillogin.error =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_mobile_number)
                mBinding!!.etEmaillogin.requestFocus()
            } else {
                if (utils!!.isNetworkAvailable) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.GetOTP(getOTPDes, email)
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            }
        }
        getHintNumber()
        mBinding!!.etEmaillogin.addTextChangedListener(textWatcher)
    }
    private fun getHintNumber(){
        val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()
        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    phoneNumberHintIntentResultLauncher!!.launch(
                        IntentSenderRequest.Builder(result).build()
                    )
                } catch (e: Exception) {
                    Log.e("TAG", "Launching the PendingIntent failed")
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Phone Number Hint failed")
            }
        phoneNumberHintIntentResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                var phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                if (phoneNumber.contains("+")) {
                    phoneNumber = phoneNumber.substring(3)
                }
                autoNumberhendle = true
                mBinding!!.etEmaillogin.setText(phoneNumber)
            } catch(e: Exception) {
                Log.e("TAG", "Phone Number Hint failed")
            }
        }
    }

    private fun attemptLogin() {
        val fcmToken: String =
            EndPointPref.getInstance(applicationContext).getFcmToken(EndPointPref.FCM_TOKEN)
        mobileNumber = mBinding!!.etEmaillogin.text.toString().trim { it <= ' ' }
        val password = mBinding!!.etPasswordlogin.text.toString().trim { it <= ' ' }
        if (TextUtils.isNullOrEmpty(mobileNumber)) {
            mBinding!!.etEmaillogin.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_mobile_number)
            mBinding!!.etEmaillogin.requestFocus()
        } else if (Validation.chkmobileNo(mobileNumber)) {
            mBinding!!.etEmaillogin.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_proper_mobile_number)
            mBinding!!.etEmaillogin.requestFocus()
        } else if (TextUtils.isNullOrEmpty(password.trim { it <= ' ' })) {
            mBinding!!.etPasswordlogin.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_password)
            mBinding!!.etPasswordlogin.requestFocus()
        } else if (password.trim { it <= ' ' }.length < 3) {
            mBinding!!.etPasswordlogin.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_valid_password)
            mBinding!!.etPasswordlogin.requestFocus()
        } else if (utils!!.isNetworkAvailable) {
            Utils.showProgressDialog(this)
            if (commonClassForAPI != null) {
                RetailerSDKApp.getInstance().updateAnalytics("login_attempt")
                Utils.showProgressDialog(this)
                commonClassForAPI!!.fetchLoginData(
                    callLoginDes, LoginModel(
                        mobileNumber,
                        password,
                        fcmToken ?: "",
                        versionName,
                        deviceOs,
                        deviceName,
                        Utils.getDeviceUniqueID(this),
                        Utils.getDeviceUniqueID(this),
                        autoNumberhendle
                    )
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // method for forgot password
    private fun forgotPasswordPopup() {
        val inflater = layoutInflater
        val mView = inflater.inflate(R.layout.forgot_password_popup, null)
        customDialog = Dialog(this, R.style.CustomDialog)
        customDialog!!.setContentView(mView)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val cancelBtn = mView.findViewById<TextView>(R.id.cancel_btn)
        val tvFPHead = mView.findViewById<TextView>(R.id.tvFPHead)
        val etMobileNo = mView.findViewById<EditText>(R.id.et_Mobile_No)
        val tvEnterMobile = mView.findViewById<TextView>(R.id.tvEnterMobile)
        tvFPHead.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.forgot_password_title))
        tvEnterMobile.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.entermobilenumber))
        etMobileNo.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.mobile_number))
        cancelBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.cancel))
        okBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok))
        okBtn.setOnClickListener { v: View? ->
            if (etMobileNo.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)) {
                Toast.makeText(
                    this,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_mobile_number),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (TextUtils.isValidMobileNo(etMobileNo.text.toString().trim { it <= ' ' })) {
                if (utils!!.isNetworkAvailable) {
                    customDialog!!.dismiss()
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.forgetPassword(
                        forgotPassDes,
                        etMobileNo.text.toString().trim { it <= ' ' })
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.validMobilenumbe),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        cancelBtn.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    // method for forgot password
    private fun notAuthCustomer(authMsg: String) {
        val inflater = layoutInflater
        val mView = inflater.inflate(R.layout.cust_not_auth_popup, null)
        customDialog = Dialog(this, R.style.CustomDialog)
        customDialog!!.setContentView(mView)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val cancelBtn = mView.findViewById<TextView>(R.id.cancel_btn)
        val tvAuthMsg = mView.findViewById<TextView>(R.id.auth_msg)
        val tvTitleAuth = mView.findViewById<TextView>(R.id.tvTitleAuth)
        tvTitleAuth.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.alert))
        cancelBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_title))
        okBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.yes_title))
        tvAuthMsg.text = authMsg
        okBtn.setOnClickListener { v: View? ->
            customDialog!!.dismiss()
            if (commonClassForAPI != null) {
                commonClassForAPI!!.fetchOTP(
                    postMobileObserver, mobileNumber,
                    Utils.getDeviceUniqueID(this@LoginActivity),
                    AppSignatureHelper(
                        applicationContext
                    ).appSignatures
                )
            }
        }
        cancelBtn.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    private fun gotoActiveScreen() {
        // start analytic new session
        RetailerSDKApp.getInstance().startAnalyticSession()
        RetailerSDKApp.getInstance().prefManager.setLoggedIn(true)
        RetailerSDKApp.getInstance().prefManager.setShowcaseFirstTimeLaunch(true)
        RetailerSDKApp.getInstance().clearLocalData()
        startActivity(
            Intent(applicationContext, SplashScreenActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    // method result popup
    private fun forgotPasswordResult() {
        val mView = layoutInflater.inflate(R.layout.forgot_password_result, null)
        customDialog = Dialog(this, R.style.CustomDialog)
        customDialog!!.setContentView(mView)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val tvFPTitle = mView.findViewById<TextView>(R.id.tvFPTitle)
        val tvForgotResult = mView.findViewById<TextView>(R.id.tvForgotResult)
        okBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok))
        tvFPTitle.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.forgot_password_title))
        tvForgotResult.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.forgot_password_result_title))
        okBtn.setOnClickListener { v: View? -> customDialog!!.dismiss() }
        customDialog!!.show()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            //  Auto-generated method stub
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            //  Auto-generated method stub
        }

        override fun afterTextChanged(s: Editable) {
            autoNumberhendle = false
        }
    }


    // First otp getting
    private val postMobileObserver: DisposableObserver<OTPResponse> =
        object : DisposableObserver<OTPResponse>() {
            override fun onNext(response: OTPResponse) {
                Utils.hideProgressDialog()
                if (response.isStatus) {
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_OTP_TIME, 30000.toString())
                    SharePrefs.getInstance(applicationContext).putInt(SharePrefs.LAST_OTP_COUNT, 1)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_MOBILE_NUMBER, mobileNumber)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_TRUE_CUSTOMER, autoNumberhendle)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LAST_OTP_TIME, 30000.toString())
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.LAST_IS_OTP_SENT, true)
                    stopService(Intent(applicationContext, OnClearFromRecentService::class.java))
                    startActivity(
                        Intent(
                            applicationContext,
                            OtpVerifyActivity::class.java
                        ).putExtra("MobileNumber", mobileNumber)
                            .putExtra("TRUE_CUSTOMER", autoNumberhendle)
                    )
                    Utils.leftTransaction(this@LoginActivity)
                } else {
                    Utils.setToast(
                        applicationContext, response.message
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

    // getting forgot pass user response
    private val forgotPassDes: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(`object`: JsonObject) {
                Utils.hideProgressDialog()
                if (`object`["Status"].asBoolean) {
                    forgotPasswordResult()
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.forgot_password_not_changed)
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

    // getting OTP response
    private val getOTPDes: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(jsonObject: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    if (jsonObject["OtpNo"].asString != null) {
                        Utils.setToast(
                            applicationContext, "Otp sent at your Number"
                        )
                    } else {
                        Utils.setToast(
                            applicationContext, "Please Resend Otp"
                        )
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

    // getting user response
    private val callLoginDes: DisposableObserver<LoginResponse> =
        object : DisposableObserver<LoginResponse>() {
            override fun onNext(model: LoginResponse) {
                Utils.hideProgressDialog()
                try {
                    if (model.status == "false") {
                        if (!model.isNotAuthorize) {
                            Utils.setToast(
                                applicationContext, model.message
                            )
                        } else {
                            notAuthCustomer(model.message)
                        }
                    } else {
                        customer = model.customers
                        if (customer!!.isSignup) {
                            SharePrefs.getInstance(applicationContext)
                                .putInt(SharePrefs.CUSTOMER_ID, customer!!.customerId)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.SK_CODE, customer!!.skcode)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CUSTOMER_NAME, customer!!.name)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CUSTOMER_EMAIL, customer!!.emailid)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.MOBILE_NUMBER, customer!!.mobile)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.SHOP_NAME, customer!!.shopName)
                            SharePrefs.getInstance(applicationContext)
                                .putInt(SharePrefs.COMPANY_ID, customer!!.companyId)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CUSTOMER_TYPE, "")
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.SHIPPING_ADDRESS, customer!!.shippingAddress)
                            SharePrefs.getInstance(applicationContext)
                                .putInt(SharePrefs.WAREHOUSE_ID, customer!!.warehouseid)
                            SharePrefs.getInstance(applicationContext)
                                .putBoolean(SharePrefs.IS_SIGN_UP, customer!!.isSignup)
                            SharePrefs.getInstance(applicationContext).putBoolean(
                                SharePrefs.CUST_ACTIVE,
                                Boolean.valueOf(customer!!.active)
                            )
                            SharePrefs.getInstance(applicationContext)
                                .putInt(SharePrefs.CITY_ID, customer!!.cityid)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CITY_NAME, customer!!.city)
                            SharePrefs.getInstance(applicationContext).putString(
                                SharePrefs.USER_PROFILE_IMAGE,
                                customer!!.uploadProfilePichure
                            )
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.PASSWORD, customer!!.password)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.LATITUDE, customer!!.lat)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.LONGITUDE, customer!!.lg)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CLUSTER_ID, customer!!.clusterId)
                            val regApk: UserAuth = customer!!.registeredApk!!
                            RetailerSDKApp.getInstance().updateAnalyticAuth(
                                FirebaseAnalytics.Event.LOGIN,
                                "password",
                                mobileNumber
                            )
                            if (regApk != null) {
                                val password: String = regApk.password!!
                                val username: String = regApk.userName!!
                                SharePrefs.getInstance(applicationContext)
                                    .putString(SharePrefs.TOKEN_PASSWORD, password)
                                SharePrefs.getInstance(applicationContext)
                                    .putString(SharePrefs.TOKEN_NAME, username)
                                commonClassForAPI!!.getToken(
                                    callTokenDes,
                                    "password",
                                    username,
                                    password
                                )
                            }
                        } else {
                            val intent = Intent(applicationContext, ContactUsActivity::class.java)
                            intent.putExtra("Type", "ContactUsActivity")
                            startActivity(intent)
                            finish()
                        }
                        Utils.leftTransaction(this@LoginActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().log(e.message!!)
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.server_error)
                )
                if (this != null) {
                    dispose()
                }
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    // getting token response
    private val callTokenDes: DisposableObserver<TokenResponse> =
        object : DisposableObserver<TokenResponse>() {
            override fun onNext(response: TokenResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null) {
                        SharePrefs.getInstance(applicationContext)
                            .putString(SharePrefs.TOKEN, response.access_token)
                        if (customer!!.isResetPasswordOnLogin) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    ChangePasswordActivity::class.java
                                ).putExtra("FLAG", 0)
                            )
                            finish()
                        } else {
                            gotoActiveScreen()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.server_error)
                )
                if (this != null) {
                    dispose()
                }
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}