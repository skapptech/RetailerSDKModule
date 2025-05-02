package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyUdharBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.util.*

class MyUdharActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityMyUdharBinding? = null

    private var incomeRateSP: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_udhar)
        // init view
        initView()
    }

    private fun initView() {
        val toolBarBackLL = mBinding!!.toolbarMyUdhar.back
        val toolbarTittleTV = mBinding!!.toolbarMyUdhar.title
        incomeRateSP = mBinding!!.spIncomeRate
        val nextBT = mBinding!!.btNext
        toolbarTittleTV.text = RetailerSDKApp.getInstance().dbHelper.getData("txt_My_Udhaar")
        mBinding!!.tvSelectIncome.text =
            RetailerSDKApp.getInstance().dbHelper.getData("txt_Select_income")
        mBinding!!.btNext.text = RetailerSDKApp.getInstance().dbHelper.getData("next")
        toolBarBackLL.setOnClickListener(this)
        nextBT.setOnClickListener(this)
        incomeSetData()
    }

    //set data
    private fun incomeSetData() {
        val incomeArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, udharList)
        incomeRateSP!!.adapter = incomeArrayAdapter
    }

    //get price list
    val udharList: ArrayList<String>
        get() {
            val udharIncomeArray = resources.getStringArray(R.array.udhar_price)
            return ArrayList(Arrays.asList(*udharIncomeArray))
        }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.bt_next -> setAnnualData()
        }
    }

    private fun setAnnualData() {
        val incomeRateSt = incomeRateSP!!.selectedItem.toString()
        if (incomeRateSt.equals("Please Select", ignoreCase = true)) {
            Utils.setToast(
                this,
                RetailerSDKApp.getInstance().dbHelper.getData("Please_Select_Annual")
            )
        } else {
            startActivity(
                Intent(this, MyUdharIncomeActivity::class.java).putExtra(
                    "incomeRateSt",
                    incomeRateSt
                )
            )
            Utils.leftTransaction(this)
        }
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
}