package app.retailer.krina.shop.com.mp_shopkrina_retailer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMainTestBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class MainActivityTest : AppCompatActivity() {
    private lateinit var binding: ActivityMainTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener {
            RetailerSDKApp.initialize(application)
            val intent = Intent(applicationContext, SplashScreenActivity::class.java)
            startActivity(intent)
        }
    }
}