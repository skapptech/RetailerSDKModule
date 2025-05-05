package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.text.util.Linkify
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityContactUsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class ContactUsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityContactUsBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var type: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        setValue()
        mBinding.toolbarCu.back.setOnClickListener { v: View? -> onBackPressed() }
        val intent = intent
        type = intent.getStringExtra("Type")
        assert(type != null)
        if (type.equals("ContactUsActivity", ignoreCase = true)) {
            mBinding.llMeassage.visibility = View.GONE
        } else if (type.equals("ResetPasswordActivity", ignoreCase = true)) {
            mBinding.llMeassage.visibility = View.VISIBLE
            mBinding.txtMessage.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.reset_pass_text)
        } else {
            mBinding.llMeassage.visibility = View.VISIBLE
            mBinding.txtMessage.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.inactive_accnt)
        }
        mBinding.btnOk.setOnClickListener { v: View? ->
            if (type.equals("ResetPasswordActivity", ignoreCase = true)) {
                SharePrefs.getInstance(this@ContactUsActivity)
                    .putString(SharePrefs.IsSignup, "false")
                val intent1 = Intent(this@ContactUsActivity, MobileSignUpActivity::class.java)
                startActivity(intent1)
            } else {
                finish()
            }
        }
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                val token = Utils.getToken(this)
                Log.e("MAN_TOKN>>> ", ">>$token")
                commonClassForAPI!!.getCompanyInfo(
                    tokenObserver,
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                    "Contact-us"
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    override fun onBackPressed() {
        if (type.equals("ResetPasswordActivity", ignoreCase = true)) {
            SharePrefs.getInstance(applicationContext).putString(SharePrefs.IsSignup, "false")
            val intent = Intent(applicationContext, MobileSignUpActivity::class.java)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
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

    private fun setValue() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        mBinding.toolbarCu.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.customer_support)
        mBinding.tvAnyIssue.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.if_any_issue_please_contact_us)
        mBinding.btnOk.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok)
    }


    private val tokenObserver: DisposableObserver<CompanyInfoResponse> =
        object : DisposableObserver<CompanyInfoResponse>() {
            override fun onNext(response: CompanyInfoResponse) {
                Utils.hideProgressDialog()
                if (response.isStatus) {
                    val text1 = RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact) +
                            ":" + "  " + "<font color=#FF4500> +91 " + response.companyDetails?.contact + "</font>"
                    mBinding.callus.text = Html.fromHtml(text1)
                    mBinding.callus.autoLinkMask = Linkify.PHONE_NUMBERS
                    mBinding.email.text =
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.email) +
                                ":" + "  " + response.companyDetails?.email
                    mBinding.website.text =
                        (RetailerSDKApp.getInstance().dbHelper.getString(R.string.Name)
                                + "  " + response.companyDetails?.name)
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

            override fun onComplete() {}
        }
}