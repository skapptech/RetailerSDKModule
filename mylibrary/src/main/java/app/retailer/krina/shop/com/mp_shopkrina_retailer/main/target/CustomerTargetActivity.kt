package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomerTargetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CustomerTargetResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import io.reactivex.observers.DisposableObserver
import java.text.SimpleDateFormat
import java.util.*

class CustomerTargetActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCustomerTargetBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_target)
        mBinding.back.setOnClickListener { onBackPressed() }
        initView()

        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        mBinding.btnClaim.setOnClickListener {
            if (utils!!.isNetworkAvailable) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.claimCustomerTarget(
                        claimObserver,
                        SharePrefs.getInstance(
                            applicationContext
                        ).getInt(SharePrefs.WAREHOUSE_ID),
                        SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE),
                        SharePrefs.getInstance(
                            applicationContext
                        ).getInt(SharePrefs.CUSTOMER_ID)
                    )
                }
            } else {
                startActivity(Intent(applicationContext, NoInternetActivity::class.java))
            }
        }

        // Target API call
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_SHOW_TARGET)) {
            if (utils!!.isNetworkAvailable) {
                if (commonClassForAPI != null) {
                    commonClassForAPI!!.fetchCustomerTarget(
                        customerTargetDes,
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                        SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE),
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
                    )
                }
            } else {
                startActivity(Intent(applicationContext, NoInternetActivity::class.java))
            }
        } else {
            mBinding.comingSoon.visibility = View.VISIBLE
        }
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

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }


    private fun initView() {
        mBinding.title.text = MyApplication.getInstance().dbHelper.getString(R.string.my_target)
        mBinding.tvHurryup.text =
            MyApplication.getInstance().dbHelper.getString(R.string.hurry_up)
        mBinding.tvOnComplitingTarget.text =
            MyApplication.getInstance().dbHelper.getString(R.string.on_completing_target)
        mBinding.tvPoints.text =
            MyApplication.getInstance().dbHelper.getString(R.string.get_giftcard)
        mBinding.tvStatic.text =
            MyApplication.getInstance().dbHelper.getString(R.string.wallet_points_sec)
        mBinding.tvYourTotal.text =
            MyApplication.getInstance().dbHelper.getString(R.string.your_total)
        mBinding.tvNotDelivered.text =
            MyApplication.getInstance().dbHelper.getString(R.string.not_delivered)
        mBinding.tvAchievedTotal.text =
            MyApplication.getInstance().dbHelper.getString(R.string.achieved_total_calculated_on_delivered_value_only)
        mBinding.tvCongratulations.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Congratulation)
        mBinding.tvCompleteMonthTarget.text =
            MyApplication.getInstance().dbHelper.getString(R.string.completed_month_target)
        mBinding.tvStatic1.text =
            MyApplication.getInstance().dbHelper.getString(R.string.dream_points)
        mBinding.btnClaim.text = MyApplication.getInstance().dbHelper.getString(R.string.claim)
        mBinding.tvComingSoon1.text =
            MyApplication.getInstance().dbHelper.getString(R.string.coming_soon)
        mBinding.tvCheckback.text =
            MyApplication.getInstance().dbHelper.getString(R.string.please_check_back_later)
    }


    // Customer Target Response
    private val customerTargetDes: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null && response.getTargetData.targetAmount > 0 && response.status) {
                        if (response.getTargetData.totalPurchaseAmount.toInt() >= response.getTargetData.targetAmount.toInt()) {
                            mBinding.targetCompleted.visibility = View.VISIBLE
                            mBinding.targetNotCompleted.visibility = View.GONE
                            mBinding.btnClaim.visibility = View.VISIBLE
                            if (response.getTargetData.type != null && response.getTargetData.type.contains(
                                    "Gift"
                                )
                            ) {
                                Glide.with(baseContext).load(response.getTargetData.giftImage).into(
                                    mBinding.giftImage1
                                )
                                mBinding.tvCoins1.visibility = View.GONE
                                mBinding.tvStatic1.visibility = View.GONE
                                mBinding.tvNotice.text =
                                    MyApplication.getInstance().dbHelper.getString(R.string.reward_order_created)
                            } else if (response.getTargetData.type.contains("Point")) {
                                mBinding.giftImage1.setImageResource(R.drawable.component_178_1)
                                mBinding.tvCoins1.text =
                                    response.getTargetData.value.toInt().toString()
                                mBinding.tvCoins1.visibility = View.VISIBLE
                                mBinding.tvStatic1.visibility = View.VISIBLE
                                mBinding.tvNotice.text =
                                    MyApplication.getInstance().dbHelper.getString(R.string.reward_points_added_to_wallet)
                            } else if (response.getTargetData.type.contains("Offer")) {
                                if (response.getTargetData.offerType == 0)
                                    mBinding.tvCoins1.text = response.getTargetData.offerValue + "%"
                                else
                                    mBinding.tvCoins1.text = "₹" + response.getTargetData.offerValue
                                mBinding.tvCoins1.textSize = 22f
                                mBinding.tvStatic1.text = ""
                                mBinding.giftImage1.setImageResource(R.drawable.component_178_1)
                                mBinding.tvOfferDes1.text = "" + response.getTargetData.offerDesc
                            } else {
                                mBinding.giftImage.setImageResource(R.drawable.symbol_165_1)
                            }
                        } else {
                            mBinding.targetCompleted.visibility = View.GONE
                            mBinding.targetNotCompleted.visibility = View.VISIBLE
                            mBinding.btnClaim.visibility = View.GONE
                            if (response.getTargetData.type.contains("Gift")) {
                                Glide.with(baseContext).load(response.getTargetData.giftImage).into(
                                    mBinding.giftImage
                                )
                                mBinding.tvPoints.text =
                                    MyApplication.getInstance().dbHelper.getString(R.string.get_giftcard)
                                mBinding.tvCoins.visibility = View.GONE
                                mBinding.tvStatic.visibility = View.GONE
                            } else if (response.getTargetData.type.contains("Point")) {
                                mBinding.giftImage.setImageResource(R.drawable.component_178_1)
                                mBinding.tvPoints.text =
                                    MyApplication.getInstance().dbHelper.getString(R.string.get_giftcard)
                                mBinding.tvCoins.text =
                                    response.getTargetData.value.toInt().toString()
                                mBinding.tvCoins.visibility = View.VISIBLE
                                mBinding.tvStatic.visibility = View.VISIBLE
                            } else if (response.getTargetData.type.contains("Offer")) {

                                mBinding.giftImage.setImageResource(R.drawable.symbol_165_1)
                                mBinding.tvOfferDes.text = "" + response.getTargetData.offerDesc
                            } else {
                                mBinding.giftImage.setImageResource(R.drawable.symbol_165_1)
                            }
                        }
                        mBinding.tvMonth.text =
                            (MyApplication.getInstance().dbHelper.getString(R.string.achieve_your) + " " +
                                    SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date())
                                    + " " + MyApplication.getInstance().dbHelper.getString(R.string.month_target))
                        mBinding.tvTarget.text = "₹" + response.getTargetData.targetAmount.toInt()
                        mBinding.tvPurchaseAmt.text =
                            "₹" + response.getTargetData.totalPurchaseAmount.toInt()
                        if (response.getTargetData.totalPendingPurchaseAmount.toInt() == 0) {
                            mBinding.tvNotDelivered.visibility = View.GONE
                        } else {
                            mBinding.tvNotDelivered.text =
                                "Not delivered: ₹" + response.getTargetData.totalPendingPurchaseAmount.toInt()
                        }
                        mBinding.targetProgress.incrementProgressBy(response.getTargetData.achivePercent.toInt())
                        mBinding.progressPercent.text =
                            response.getTargetData.achivePercent.toInt().toString() + "%"
                        mBinding.tvDaysLeft.text =
                            response.getTargetData.leftDays.toString() + " " +
                                    MyApplication.getInstance().dbHelper.getString(R.string.days_left)
                        if (response.getTargetData.isClaimed) {
                            mBinding.btnClaim.visibility = View.GONE
                        }
                    } else {
                        mBinding.comingSoon.visibility = View.VISIBLE
                        mBinding.targetCompleted.visibility = View.GONE
                        mBinding.targetNotCompleted.visibility = View.GONE
                        mBinding.btnClaim.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding.comingSoon.visibility = View.VISIBLE
            }

            override fun onComplete() {}
        }

    private val claimObserver: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null && response.status) {
                        mBinding.btnClaim.isEnabled = false
                        AlertDialog.Builder(applicationContext)
                            .setTitle("Target Claim")
                            .setMessage("" + response.message)
                            .setPositiveButton(
                                "ok"
                            ) { dialogInterface, i ->
                                dialogInterface.dismiss()
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                                Utils.fadeTransaction(this@CustomerTargetActivity)
                            }.show()
                    }
                    Utils.setToast(applicationContext, response.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding.btnClaim.isEnabled = false
            }

            override fun onComplete() {}
        }
}