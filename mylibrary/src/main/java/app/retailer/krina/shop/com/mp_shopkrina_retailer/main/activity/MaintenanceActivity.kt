package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMaintainanceBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class MaintenanceActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMaintainanceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_maintainance)
        mBinding.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.under_maintenance)
        mBinding.tvPreparing.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.we_are_preparing_to_serve_you_better)
        mBinding.retry.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.retry)
        mBinding.retry.setOnClickListener { closeScreen() }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("netStatus")
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun closeScreen() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (callingActivity!!.packageName == packageName) {
                val status = intent.getBooleanExtra("status", false)
                if (status) {
                    closeScreen()
                }
            }
        }
    }
}