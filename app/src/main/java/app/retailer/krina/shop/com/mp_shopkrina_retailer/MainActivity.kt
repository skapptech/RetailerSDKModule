package app.retailer.krina.shop.com.mp_shopkrina_retailer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMainBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, SplashScreenActivity::class.java)
            startActivity(intent)
        }
    }
}