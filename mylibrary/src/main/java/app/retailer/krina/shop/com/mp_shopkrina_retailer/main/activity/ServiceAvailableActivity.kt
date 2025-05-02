package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityServiceAvailableBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class ServiceAvailableActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceAvailableBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceAvailableBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val phone = SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)

        binding.tvSkCode.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.sk_code) +
                    " " + SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE)
        binding.tvTitle.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.thank_you_for_your_registration)
        binding.tvDescription.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.we_will_inform_you_once_we_available)
        binding.btnCall.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.call_now)

        binding.tvPhone.text = "+91 $phone"

        binding.btnCall.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }
    }
}