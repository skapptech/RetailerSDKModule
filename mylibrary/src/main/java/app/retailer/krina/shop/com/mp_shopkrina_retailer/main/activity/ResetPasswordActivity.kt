package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityResetPassBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityResetPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        setValue()
        mBinding.btnOk.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    applicationContext,
                    MobileSignUpActivity::class.java
                )
            )
        }
        mBinding.toolbarResetPassword.back.setOnClickListener { v: View? -> onBackPressed() }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
        finish()
        Utils.rightTransaction(this)
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
        mBinding.toolbarResetPassword.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.customer_support)
        mBinding.tvAnyIssueContact.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact_us_issues)
        mBinding.btnOk.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok)
        mBinding.callus.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact) + ":" + "    " + "+(91)7828112112"
        mBinding.email.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.email) + ":" + "          " + "info@shopKirana.com"
        mBinding.website.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.website) + ":" + "      " + "http://www.ShopKirana.com"
    }
}