package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentImageShoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ShowImagesAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabKitabImageModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper

class ShowImageActivity : AppCompatActivity() {
    private var mBinding: FragmentImageShoBinding? = null
    private var irImagesModels: ArrayList<HisabKitabImageModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = FragmentImageShoBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val extras = intent.extras
        if (extras != null) {
            irImagesModels =
                intent.getSerializableExtra("ImageData") as ArrayList<HisabKitabImageModel>?
        }
        initialization()
    }

    private fun initialization() {
        mBinding!!.toolbarDream.tvUserName.text =
            this@ShowImageActivity.resources.getString(R.string.show_images)
        mBinding!!.toolbarDream.back.setOnClickListener { onBackPressed() }
        val mPager = mBinding!!.pager
        mPager.adapter = ShowImagesAdapter(this, irImagesModels!!)
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
}