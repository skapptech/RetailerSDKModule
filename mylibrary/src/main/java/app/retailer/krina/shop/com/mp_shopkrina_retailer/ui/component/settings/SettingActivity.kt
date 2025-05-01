package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivitySettingBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.CheckSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ShopDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth.ChangePasswordActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivitySettingBinding? = null
    private val CHANNEL = "com.ScaleUP"
 //   private var flutterEngine: FlutterEngine? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        setSupportActionBar(mBinding?.toolbarMyUdhar?.arrowToolbar)
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.ll_edtProfile -> {
                if (TextUtils.isNullOrEmpty(
                        SharePrefs.getInstance(
                            applicationContext
                        ).getString(SharePrefs.CLUSTER_ID)
                    )
                ) {
                    val phone = SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
                    val msg = MyApplication.getInstance().dbHelper.getString(R.string.msg_cluster_null)+phone
                    android.app.AlertDialog.Builder(this@SettingActivity).setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.alert
                        )
                    ).setMessage(msg).setNegativeButton(getString(R.string.ok), null).show()
                } else {
                    MyApplication.getInstance().updateAnalytics("edit_profile_click")
                    startActivity(Intent(applicationContext, EditProfileActivity::class.java))
                    Utils.fadeTransaction(this)
                }

            }

            R.id.ll_changePass -> if (SharePrefs.getInstance(applicationContext)
                    .getBoolean(SharePrefs.IS_SIGN_UP)
            ) {
                MyApplication.getInstance().updateAnalytics("change_password_click")
                startActivity(
                    Intent(
                        applicationContext,
                        ChangePasswordActivity::class.java
                    ).putExtra("FLAG", 1)
                )
                Utils.fadeTransaction(this)
            } else {
                MyApplication.getInstance().updateAnalytics("check_signup_click")
                SharePrefs.getInstance(applicationContext).putString(SharePrefs.SIGNUPLOC, "HOME")
                val intentActivation = Intent(applicationContext, CheckSignUpActivity::class.java)
                intentActivation.putExtra(Constant.ACTIVATION_TITLE, "Change Password")
                startActivity(intentActivation)
            }

            R.id.ll_changelanguage -> {
                MyApplication.getInstance().updateAnalytics("change_language_click")
                startActivity(Intent(applicationContext, ChangeLanguageActivity::class.java))
                Utils.fadeTransaction(this)
            }

            R.id.ll_rateApp -> {
                MyApplication.getInstance().updateAnalytics("rate_app_click")
                startActivity(Intent(applicationContext, RateAppActivity::class.java))
                Utils.fadeTransaction(this)
            }

            R.id.ll_shopDetail -> {
                MyApplication.getInstance().updateAnalytics("shop_detail_click")
                startActivity(Intent(applicationContext, ShopDetailsActivity::class.java))
                Utils.fadeTransaction(this)
            }

            R.id.liTerms -> {
                MyApplication.getInstance().updateAnalytics("terms_condition_click")
                startActivity(
                    Intent(applicationContext, TermOfServicesActivity::class.java)
                        .putExtra("isTermsAndCondition", "terms")
                )
            }

            R.id.ll_logout -> {
                MyApplication.getInstance().updateAnalytics("logout_click")
                AlertDialog.Builder(this)
                    .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.logout))
                    .setMessage(MyApplication.getInstance().dbHelper.getString(R.string.are_you_want_to_log_out))
                    .setPositiveButton(
                        MyApplication.getInstance().dbHelper.getString(R.string.yes_title)
                    ) { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                        MyApplication.getInstance().logout(this)
                        callFlutterFunction()
                    }
                    .setNegativeButton(
                        MyApplication.getInstance().dbHelper.getString(R.string.no_title)
                    ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                    .show()
            }
        }
    }

    private fun callFlutterFunction() {
//        flutterEngine?.getDartExecutor()?.let {
//            MethodChannel(it.binaryMessenger, CHANNEL)
//                .invokeMethod("logout", null, object : MethodChannel.Result {
//                    override fun success(result: Any?) {
//                        println("Result from Flutter: $result")
//
//                    }
//
//                    override fun error(
//                        errorCode: String,
//                        errorMessage: String?,
//                        errorDetails: Any?
//                    ) {
//                        System.err.println("Error: $errorMessage")
//                    }
//
//                    override fun notImplemented() {
//                        System.err.println("Method not implemented")
//                    }
//                })
//        }
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


    private fun initView() {
        mBinding!!.tvProfileHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.profile)
        mBinding!!.tvEditProfile.text =
            MyApplication.getInstance().dbHelper.getString(R.string.edit_profile)
        mBinding!!.tvChangePassword.text =
            MyApplication.getInstance().dbHelper.getString(R.string.changepassword)
        mBinding!!.tvRegionalHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.regional)
        mBinding!!.tvShopDetails.text =
            MyApplication.getInstance().dbHelper.getString(R.string.shop_details)
        mBinding!!.tvChangeLanguage.text =
            MyApplication.getInstance().dbHelper.getString(R.string.changelang)
        mBinding!!.tvRateApp.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_rate_the_app)
        mBinding!!.tvTerms.text =
            MyApplication.getInstance().dbHelper.getString(R.string.terms_and_condition)
        mBinding!!.tvLogout.text = MyApplication.getInstance().dbHelper.getString(R.string.logout)
        mBinding!!.toolbarMyUdhar.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.setting)
        mBinding!!.toolbarMyUdhar.back.setOnClickListener(this)
        mBinding!!.llEdtProfile.setOnClickListener(this)
        mBinding!!.llChangePass.setOnClickListener(this)
        mBinding!!.llChangelanguage.setOnClickListener(this)
        mBinding!!.llShopDetail.setOnClickListener(this)
        mBinding!!.llRateApp.setOnClickListener(this)
        mBinding!!.liTerms.setOnClickListener(this)
        mBinding!!.llLogout.setOnClickListener(this)
        mBinding!!.userNameSet.text =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
        mBinding!!.userSkcode.text =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        mBinding!!.txtAppVersion.text = "App ver " + BuildConfig.VERSION_NAME

        if (SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.USER_PROFILE_IMAGE) != null && SharePrefs.getInstance(
                applicationContext
            ).getString(SharePrefs.USER_PROFILE_IMAGE) != ""
        ) {
            Picasso.get()
                .load(
                    Constant.BASE_URL_PROFILE + SharePrefs.getInstance(
                        applicationContext
                    ).getString(SharePrefs.USER_PROFILE_IMAGE)
                )
                .placeholder(R.drawable.profile_round)
                .error(R.drawable.profile_round)
                .into(mBinding!!.profileImageSetting)
        } else {
            mBinding!!.profileImageSetting.setImageResource(R.drawable.profile_round)
        }

//        flutterEngine = FlutterEngine(this);
//        flutterEngine!!.getDartExecutor().executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        );
    }
}