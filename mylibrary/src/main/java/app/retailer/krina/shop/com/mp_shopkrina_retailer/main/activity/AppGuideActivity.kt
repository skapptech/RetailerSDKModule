package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyAccountBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.AppGuideAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class AppGuideActivity : AppCompatActivity() {

    var mBinding: ActivityMyAccountBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account)
        initView()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }

    private fun initView() {
        mBinding!!.toolbarMyAccount.title.text = getString(R.string.appguide)
        mBinding!!.toolbarMyAccount.back.setOnClickListener { v: View? -> onBackPressed() }
        val myaccount = ArrayList<String>()
        myaccount.add(getString(R.string.myOrder))
        val adapter = AppGuideAdapter(this, myaccount)
        mBinding!!.accoutRecycle.adapter = adapter
    }
}