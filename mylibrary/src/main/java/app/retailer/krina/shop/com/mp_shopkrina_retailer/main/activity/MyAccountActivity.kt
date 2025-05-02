package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyAccountBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyAccountAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyAccountActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMyAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account)
        initView()
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

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }

    private fun initView() {
        mBinding.toolbarMyAccount.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.myAccount)
        mBinding.toolbarMyAccount.back.setOnClickListener { onBackPressed() }
        val myaccount = ArrayList<String>()
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.myOrder))
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.myWallet))
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.myFavourite))
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.mydream))
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.my_ledger))
        //        myaccount.add(getString(R.string.kissan_dan));
        myaccount.add(MyApplication.getInstance().dbHelper.getString(R.string.my_target))
        //   myaccount.add(getString(R.string.my_issue));
        mBinding.accoutRecycle.adapter = MyAccountAdapter(this, myaccount)
    }
}