package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyUdharIncomeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class MyUdharIncomeActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityMyUdharIncomeBinding? = null
    private var incomeRateSP: Spinner? = null
    private var incomeRateSt: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_udhar_income)
        if (intent.extras != null) {
            incomeRateSt = intent.getStringExtra("incomeRateSt")
        }
        //init view
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.bt_next -> setReturnYearData()
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


    private fun initView() {
        incomeRateSP = mBinding!!.spIncomeRate
        val nextBT = mBinding!!.btNext
        mBinding!!.toolbarUdharIncome.title.text =
            RetailerSDKApp.getInstance().dbHelper.getData("txt_My_Udhaar")
        mBinding!!.tvSelectIncome.text = RetailerSDKApp.getInstance().dbHelper.getData("txt_how_old")
        mBinding!!.btNext.text = RetailerSDKApp.getInstance().dbHelper.getData("next")
        mBinding!!.toolbarUdharIncome.back.setOnClickListener(this)
        nextBT.setOnClickListener(this)
        incomeTimeData()
    }

    //set data
    private fun incomeTimeData() {
        val incomeArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, udharList)
        incomeRateSP!!.adapter = incomeArrayAdapter
    }

    //get data
    val udharList: ArrayList<String>
        get() {
            val udharIncomeArray = resources.getStringArray(R.array.udhar_tencher_time)
            return ArrayList(listOf(*udharIncomeArray))
        }

    private fun setReturnYearData() {
        val returnYearSt = incomeRateSP!!.selectedItem.toString()
        if (returnYearSt.equals("Please Select", ignoreCase = true)) {
            Toast.makeText(
                this,
                RetailerSDKApp.getInstance().dbHelper.getData("Please_Select_year"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            startActivity(
                Intent(this, PanCardUploadActivity::class.java)
                    .putExtra("incomeRateSt", incomeRateSt)
                    .putExtra("returnYearSt", returnYearSt)
            )
        }
    }
}