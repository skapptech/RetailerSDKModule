package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityEditProfileBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.EditProfileHomeFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActvityTradeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityEditProfileBinding

    @JvmField
    var editProfileModel: EditProfileModel? = null

    @JvmField
    var customerModel: CustomerResponse? = null

    @JvmField
    var tv_title: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
            if (EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.showNewSocial))
                startActivity(Intent(applicationContext, FeedActivity::class.java))
            else
                startActivity(Intent(applicationContext, TradeActivity::class.java))
            finish()
            return
        }
        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_SIGN_UP)) {
            startActivity(
                Intent(applicationContext, CheckSignUpActivity::class.java)
                    .putExtra(Constant.ACTIVATION_TITLE, getString(R.string.sign_up))
            )
            finish()
        } else {
            initialization()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.back) {
            onBackPressed()
        }
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


    fun initialization() {
        tv_title = mBinding.toolbarProfile.title
        mBinding.toolbarProfile.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.profile)
        supportFragmentManager.beginTransaction().replace(
            R.id.content,
            EditProfileHomeFragment(), EditProfileHomeFragment::class.java.name
        ).commit()
        mBinding.toolbarProfile.back.setOnClickListener(this)
        editProfileModel = EditProfileModel()
    }

    fun switchContentWithStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.content, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()
    }
}