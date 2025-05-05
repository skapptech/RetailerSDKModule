package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityBusinessCardBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCheckSignUpBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerAddressActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class CheckSignUpActivity : AppCompatActivity(), View.OnClickListener {
    var mBinding: ActivityCheckSignUpBinding? = null

    var backLayout: ImageView? = null
    var tittleHeader: String? = null
    var titleTV: TextView? = null
    var discretionTv: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCheckSignUpBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val intent = intent
        if (intent != null) {
            tittleHeader = intent.getStringExtra(Constant.ACTIVATION_TITLE)
        }
        //init view
        initView()
    }

    private fun initView() {
        backLayout = mBinding!!.toolbar.back
        discretionTv = mBinding!!.tvDiscripction
        titleTV = mBinding!!.toolbar.title
        mBinding!!.tvDiscripction.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.sign_up_tittle)
        mBinding!!.tvSelectMultiplePay.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_select_nultiple_pay_no_order_to_pay_at_once)
        mBinding!!.tvPaynow.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.pay_now)
        mBinding!!.btnSignUp.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.hint_signup)
        titleTV!!.text = tittleHeader
        backLayout!!.setOnClickListener(this)
        mBinding!!.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_sign_up -> {
                startActivity(
                    Intent(applicationContext, CustomerAddressActivity::class.java)
                        .putExtra(
                            "cityName",
                            SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME)
                        )
                        .putExtra("REDIRECT_FLAG", 3)
                )
                Utils.leftTransaction(this)
                finish()
            }
            R.id.back -> onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
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
}