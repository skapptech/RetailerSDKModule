package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings

import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityTermsConditionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class TermsAndConditionActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityTermsConditionBinding
    private var toolBarBackLL: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms_condition)
        //init view
        initView()
        // back btn
        toolBarBackLL!!.setOnClickListener { onBackPressed() }
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
        toolBarBackLL = mBinding.toolbarTermsAndCondition.back
        mBinding.toolbarTermsAndCondition.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.terms_and_condition)
        mBinding.tvpolicy.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_terms_cond_text)
    }
}