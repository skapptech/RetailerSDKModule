package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReferralHistoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ReferredListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferredModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class ReferralHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityReferralHistoryBinding

    private var list: ArrayList<ReferredModel>? = null
    private var referredAdapter: ReferredListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferralHistoryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.referral_history)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.tvShopNameH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.shop_name)
        binding.tvWalletPoint.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.wallet_Point)
        binding.tvStatusH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.status)

        list = intent.getParcelableArrayListExtra("list")
        referredAdapter = ReferredListAdapter(this, list!!)
        binding.rvReferList.adapter = referredAdapter
        binding.rvReferList.addItemDecoration(DividerItemDecoration(applicationContext, 1))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}