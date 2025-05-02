package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification.NotificationModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification.NotificationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityNotificationBinding
    private lateinit var viewModel: NotificationViewModel
    private var mNotificationFragAdapter: NotificationAdapter? = null
    private var notificationListArrayList: List<NotificationModel> = ArrayList()
    private var mNotificationClick = false
    private var notificationId = 0


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
                startActivity(Intent(applicationContext, FeedActivity::class.java))
            else
                startActivity(Intent(applicationContext, TradeActivity::class.java))
            finish()
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(application, appRepository)
        )[NotificationViewModel::class.java]
        // init view
        initialization()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.back) {
            onBackPressed()
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
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(applicationContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun initialization() {
        mBinding.toolbarNotification.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_notification)
        mBinding.tvNoNot.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_notification)
        mBinding.tvNoNot.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_notification)
        mBinding.toolbarNotification.back.setOnClickListener(this)
        val custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        val commonClassForAPI = CommonClassForAPI.getInstance(this)
        val utils = Utils(this)
        mNotificationFragAdapter = NotificationAdapter(this, notificationListArrayList)
        mBinding.rcNotification.adapter = mNotificationFragAdapter
        SharePrefs.getInstance(applicationContext).putInt(SharePrefs.NOTIFICATION_COUNT, 0)
        mBinding.shimmerViewContainer.startShimmer()
        // get Notification API call

        if (intent.extras != null) {
            mNotificationClick = intent.extras!!.getBoolean("Notification")
            notificationId = intent.extras!!.getInt("NotificationId")
        }

        observe(viewModel.getNotificationData, ::getNotificationResult)

        viewModel.getNotification(custId, 0, 1)

        if (mNotificationClick) {
            viewModel.notificationClick(custId, notificationId)
            viewModel.notificationClickData.observe(this) {
                intent.extras!!.clear()
            }
        }
    }

    private fun getNotificationResult(it: Response<NotificationResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    mBinding.rcNotification.visibility = View.VISIBLE
                    mBinding.shimmerViewContainer.stopShimmer()
                    mBinding.shimmerViewContainer.visibility = View.GONE
                    notificationListArrayList = it.notificationListBeans!!
                    if (notificationListArrayList.size > 0) {
                        mBinding.liEmpty.visibility = View.GONE
                        mNotificationFragAdapter!!.setNotificationItem(notificationListArrayList)
                    } else {
                        mBinding.liEmpty.visibility = View.VISIBLE
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.rcNotification.visibility = View.VISIBLE
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }


}