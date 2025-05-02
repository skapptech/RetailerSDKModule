package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityTermsServicesBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class TermOfServicesActivity : AppCompatActivity() {
    private var mBinding: ActivityTermsServicesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms_services)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initialization()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun initialization() {
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.terms_and_condition)
        // load HTML file
        val termsAndCondition = intent.getStringExtra("isTermsAndCondition")
        if (termsAndCondition != null) {
            if (termsAndCondition == "isTermsAndCondition") {
                mBinding!!.webView.loadUrl(EndPointPref.getInstance(applicationContext).baseUrl + "/images/policy/privacy.html")
            } else if (termsAndCondition == "isPrivacyPolicy") {
                title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_privacy_policy)
                mBinding!!.webView.loadUrl(EndPointPref.getInstance(applicationContext).baseUrl + "/images/policy/termsUse.html")
            } else {
                mBinding!!.webView.loadUrl(EndPointPref.getInstance(applicationContext).baseUrl + "/images/policy/termsUseAndPrivacy.html")
            }
        } else {
            mBinding!!.webView.loadUrl(intent.getStringExtra("url")!!)
        }
    }
}