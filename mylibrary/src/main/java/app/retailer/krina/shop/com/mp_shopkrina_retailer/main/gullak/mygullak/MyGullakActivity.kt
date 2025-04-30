package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyGullakBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SectionsPagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.AddPaymentActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.RtgsInfoActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyGullakActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyGullakBinding
    lateinit var tvInfo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_gullak)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarG.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.my_gullak)
        tvInfo = binding.toolbarG.llCartClear
        binding.toolbarG.llCartClear.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_money)

        binding.toolbarG.back.setOnClickListener {
            onBackPressed()
        }
        binding.toolbarG.llCartClear.setOnClickListener {
            if (binding.toolbarG.llCartClear.text == MyApplication.getInstance().dbHelper.getString(
                    R.string.add_money
                ))
                startActivityForResult(
                    Intent(applicationContext, AddPaymentActivity::class.java)
                        .putExtra("screen", 1), 9
                )
            else
                startActivityForResult(
                    Intent(applicationContext, RtgsInfoActivity::class.java)
                        .putExtra("screen", 1), 9
                )
            Utils.leftTransaction(this)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            MyApplication.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }

        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.gullak, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            startActivityForResult(
                Intent(applicationContext, AddPaymentActivity::class.java)
                    .putExtra("screen", 1), 9
            )
            Utils.leftTransaction(this)
        } else {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(
            GullakFragment.newInstance(),
            MyApplication.getInstance().dbHelper.getString(R.string.my_gullak)
        )
        adapter.addFragment(
            RtgsFragment.newInstance(),
            MyApplication.getInstance().dbHelper.getString(R.string.van_rtgs)
        )
        viewPager.adapter = adapter
        if (intent.extras != null && intent.getIntExtra("screen", 0) == 2) {
            viewPager.currentItem = 1
        }
    }
}