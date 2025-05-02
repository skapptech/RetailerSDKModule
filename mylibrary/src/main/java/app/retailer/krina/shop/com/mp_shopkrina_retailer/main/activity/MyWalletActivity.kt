package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMywalletBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyWalletAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class MyWalletActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMywalletBinding

    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var PageCount = 0
    private var loading = true
    private var walletUsed = ""
    private var ExpiringPoints = 0.0

    private var walletList: ArrayList<MyWalletModel>? = null
    private var myWalletAdapter: MyWalletAdapter? = null
    private var utils: Utils? = null
    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMywalletBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        setSupportActionBar(mBinding.toolbarMyWallet.arrowToolbar)

        //init view
        init()
        mBinding.toolbarMyWallet.ImgInfo.setOnClickListener { showInfoPop() }
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            RetailerSDKApp.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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

    override fun onBackPressed() {
        super.onBackPressed()
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


    private fun init() {
        // set data
        mBinding.toolbarMyWallet.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.myWallet)
        mBinding.tvTotalBalPt.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_balance_points)
        mBinding.tvOnTheWayPoint.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_on_the_way_points)
        mBinding.tvSpendPoint.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_spent_coins)
        mBinding.tvExPt.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.expiring_points)

        utils = Utils(this)
        walletList = ArrayList()
        val layoutManager = LinearLayoutManager(applicationContext)
        mBinding.rvWalletHistory.layoutManager = layoutManager

        myWalletAdapter = MyWalletAdapter(this, walletList!!)
        mBinding.rvWalletHistory.adapter = myWalletAdapter
        mBinding.rvWalletHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            PageCount++
                            mBinding.progressLoad.visibility = View.VISIBLE
                            callApi()
                        }
                    }
                }
            }
        })
        mBinding.shimmerViewContainer.startShimmer()
        mBinding.shimmerViewContainer.visibility = View.VISIBLE
        callApi()
        mBinding.toolbarMyWallet.back.setOnClickListener { onBackPressed() }
        mBinding.LLExpiringPoints.setOnClickListener { view: View? ->
            if (ExpiringPoints > 0) {
                startActivity(Intent(applicationContext, ExpiringPointsActivity::class.java))
                Utils.fadeTransaction(this)
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_expiring_point)
                )
            }
        }
        if (!StoryBordSharePrefs.getInstance(applicationContext)
                .getBoolean(StoryBordSharePrefs.MYWALLET)
        ) {
            appStoryView()
        }
    }

    private fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(RetailerSDKApp.getInstance().dbHelper.getString(R.string.info))
            .setContentText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.info_details))
            .setGravity(Gravity.center)
            .setDismissType(DismissType.anywhere)
            .setTargetView(mBinding.toolbarMyWallet.ImgInfo)
            .setGuideListener { view: View ->
                if (view.id == R.id.Img_info) {
                    StoryBordSharePrefs.getInstance(applicationContext)
                        .putBoolean(StoryBordSharePrefs.MYWALLET, true)
                    return@setGuideListener
                }
                mGuideView = builder!!.build()
                mGuideView?.show()
            }
        mGuideView = builder?.build()
        mGuideView?.show()
    }

    // Wallet API call
    fun callApi() {
        if (utils!!.isNetworkAvailable) {
            CommonClassForAPI.getInstance(this).fetchWalletPointNew(
                walletDes,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                PageCount, "Wallet History"
            )
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun showInfoPop() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_wallet_terms)
        dialog.setCancelable(false)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val pdTitle = dialog.findViewById<TextView>(R.id.pd_title)
        val pdDesc = dialog.findViewById<TextView>(R.id.pd_description)
        pdTitle.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.terms_and_conditions)
        okBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok)
        pdDesc.text = Html.fromHtml("" + walletUsed)
        okBtn.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }


    private val walletDes: DisposableObserver<MyWalletResponse> =
        object : DisposableObserver<MyWalletResponse>() {
            override fun onNext(response: MyWalletResponse) {
                mBinding.progressLoad.visibility = View.GONE
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.LLMain.visibility = View.VISIBLE
                try {
                    if (PageCount == 0) {
                        mBinding.toolbarMyWallet.ImgInfo.visibility = View.VISIBLE
                        mBinding.txtTerms.text = "" + response.totalEarnPoint
                        mBinding.txtEarnPoint.text = "" + response.upcomingPoints
                        mBinding.txtUpcomingPt.text = "" + response.totalUsedPoints
                        mBinding.txtExpiringPoints.text = "" + response.expiringPoints
                        ExpiringPoints = response.expiringPoints
                        walletUsed = response.howToUseWalletPoints!!
                        SharePrefs.getInstance(applicationContext).putString(
                            SharePrefs.CURRENT_WALLET_POINT,
                            "" + response.totalEarnPoint
                        )
                    }
                    if (response.list != null && response.list!!.size > 0) {
                        walletList!!.addAll(response.list!!)
                        myWalletAdapter!!.notifyDataSetChanged()
                        loading = true
                    } else {
                        loading = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mBinding.progressLoad.visibility = View.GONE
                    mBinding.shimmerViewContainer.stopShimmer()
                    mBinding.shimmerViewContainer.visibility = View.GONE
                    mBinding.LLMain.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {
                mBinding.progressLoad.visibility = View.GONE
                e.printStackTrace()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.LLMain.visibility = View.VISIBLE
            }

            override fun onComplete() {
                mBinding.progressLoad.visibility = View.GONE
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.LLMain.visibility = View.VISIBLE
            }
        }

    private val netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = Utils.getConnectivityStatusString(context)
            if (!status) {
                startActivityForResult(
                    Intent(applicationContext, NoInternetActivity::class.java),
                    222
                )
            }
        }
    }
}