package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyordersBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SectionsPagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail.OrderSummaryActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyOrderActivity : AppCompatActivity(), OnButtonClick {
    lateinit var mBinding: ActivityMyordersBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!RetailerSDKApp.getInstance().prefManager.isLoggedIn) {
            startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
            finish()
        }
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
            if (EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.showNewSocial))
                startActivity(
                    Intent(
                        applicationContext,
                        FeedActivity::class.java
                    )
                )
            else
                startActivity(Intent(applicationContext, TradeActivity::class.java))
            finish()
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_myorders)
        setSupportActionBar(mBinding.toolbarOrderDetails.toolbar)
        supportActionBar?.elevation = 0F
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // init view
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_order, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_summary -> {
                startActivity(Intent(applicationContext, OrderSummaryActivity::class.java))
                Utils.leftTransaction(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
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
    }

    override fun onPause() {
        super.onPause()
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
            unregisterReceiver(netConnectionReceiver)
        }
    }

    override fun onBackPressed() {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
        Utils.fadeTransaction(this)
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

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        try {
            val fragment =
                supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + mBinding.viewPager.currentItem) as AllOrderFragment?
            fragment!!.onButtonClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun init() {
        mBinding.toolbarOrderDetails.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.myOrder)
        mBinding.toolbarOrderDetails.back.setOnClickListener { onBackPressed() }

        setupViewPager(mBinding.viewPager)
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
        for (i in 0 until mBinding.tabLayout.tabCount) {
            val tab = (mBinding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 25, 0)
            tab.requestLayout()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)

        adapter.addFragment(
            AllOrderFragment.newInstance("live"),
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.live_order)
        )
        adapter.addFragment(
            AllOrderFragment.newInstance("completed"),
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.over)
        )
        adapter.addFragment(
            AllOrderFragment.newInstance("PayLater"),
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.paylater_order)
        )
        viewPager.adapter = adapter
    }

    private val netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var intent = intent
            val status = Utils.getConnectivityStatusString(context)
            intent = Intent("netStatus")
            intent.putExtra("status", status)
            if (!status) {
                startActivityForResult(
                    Intent(applicationContext, NoInternetActivity::class.java),
                    222
                )
            }
        }
    }
}