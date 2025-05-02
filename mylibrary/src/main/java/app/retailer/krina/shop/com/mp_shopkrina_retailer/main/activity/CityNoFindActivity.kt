package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class CityNoFindActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_not_found)
        val intent = intent
        if (intent != null) {
            val cityNameString = intent.getStringExtra("cityNameKey")
        }
        initView()
    }

    private fun initView() {
        val cityNameTV = findViewById<TextView>(R.id.tv_not_found_city)
        val tv_oops = findViewById<TextView>(R.id.tv_oops)
        cityNameTV.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.not_serving)
        tv_oops.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ooops)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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