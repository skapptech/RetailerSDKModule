package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.LayoutNoInternetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class NoInternetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = DataBindingUtil.setContentView<LayoutNoInternetBinding>(
            this,
            R.layout.layout_no_internet
        )
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_internet)
        mBinding.tvNotConnected.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.not_connected_to_internet)
        mBinding.retry.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.retry)
        mBinding.retry.setOnClickListener {
            Toast.makeText(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.retrying),
                Toast.LENGTH_SHORT
            ).show()
            val status = Utils.getConnectivityStatusString(
                applicationContext
            )
            if (status) {
                setResult(RESULT_OK, Intent())
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.still_not_connected),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
                RECEIVER_NOT_EXPORTED
            )
        } else registerReceiver(
            netConnectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(netConnectionReceiver)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    var netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (callingActivity!!.packageName == packageName) {
                val status = Utils.getConnectivityStatusString(context)
                if (status) {
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}