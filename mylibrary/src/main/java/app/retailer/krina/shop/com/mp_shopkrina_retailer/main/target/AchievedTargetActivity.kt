package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAchievedTargetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.AchievedGiftItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.TargetAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SubCategoryTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CustomerTargetResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AchievedTargetActivity : AppCompatActivity() {
    var mBinding: ActivityAchievedTargetBinding? = null
    var giftItemAdapter: AchievedGiftItemAdapter? = null
    var targetAdapter: TargetAdapter? = null
    var subCategoryTargetModel: SubCategoryTargetModel? = null
    var msgList: ArrayList<TargetModel>? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    var length = 0
    lateinit var splitted: Array<String>
    var finalString = ""
    var shortString = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_achieved_target)
        val bundle = intent.extras
        if (bundle != null) {
            subCategoryTargetModel =
                bundle.getSerializable("SUB_CAT_CUSTOMER_TARGET") as SubCategoryTargetModel?
        }
        initViews()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }


    private fun initViews() {
        mBinding!!.title.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_target)
        mBinding!!.tvHurryUpHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.hurry_up_2)
        mBinding!!.tvTargetPeriodHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.target_period)
        mBinding!!.tvTargetPeriodHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.target_period)
        mBinding!!.tvWalletPointsHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.wallet_points)
        mBinding!!.tvChanceToWin.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.chance_win)
        mBinding!!.tvClaimButton.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.claim)
        mBinding!!.tvTargetHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.target)
        mBinding!!.tvAchievedHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.achieved)
        mBinding!!.tvBrandTargetDescription.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.click_here_to_check_rest_brand_list)
        mBinding!!.tvLineItemTargetDescription.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.click_here_to_check_rest_brand_list)
        mBinding!!.tvItemTargetDescription.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.click_here_to_check_rest_products)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        mBinding!!.back.setOnClickListener { onBackPressed() }
        mBinding!!.title.text =
            subCategoryTargetModel!!.companyName + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                R.string.target
            )
        val percentage = calculatePercentage(
            subCategoryTargetModel!!.currentMonthSales,
            subCategoryTargetModel!!.target
        )
        mBinding!!.progressTarget.max = 100
        mBinding!!.progressTarget.progress = percentage.toInt()
        mBinding!!.tvProgressPercent.text = "$percentage%"
        mBinding!!.tvStrEndDate.text = Utils.getDateTimeForTarget(
            subCategoryTargetModel!!.startDate
        ) + " to " + Utils.getDateTimeForTarget(
            subCategoryTargetModel!!.endDate
        )
        //mBinding.tvDaysLeft.setText(subCategoryTargetModel.getGetTargetData().getLeftDays() + " " + getString(R.string.days_left));
        mBinding!!.tvTargetWorth.text = "₹ " + DecimalFormat("##.##").format(
            subCategoryTargetModel!!.target
        )
        mBinding!!.tvTotalTarget.text = "₹ " + DecimalFormat("##.##").format(
            subCategoryTargetModel!!.currentMonthSales
        )
        if (subCategoryTargetModel!!.currentMonthSales >= subCategoryTargetModel!!.target) {
            mBinding!!.tvTotalTarget.setTextColor(resources.getColor(R.color.green_50))
        }
        mBinding!!.tvYourTargetHead.text =
            "Your " + SimpleDateFormat("MMMM", Locale.ENGLISH).format(
                Date()
            ) + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.month_target)
        // mBinding.tvMonthTarget.setText(getString(R.string.achieve_your) + " " + new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date()) + " " + getString(R.string.month_target));
        //  mBinding.tvCompanyName.setText(subCategoryTargetModel.getCompanyName());
        mBinding!!.tvDate.text = Utils.getCurrentDate(Calendar.getInstance().time)
        if (subCategoryTargetModel!!.brandNames != null && !subCategoryTargetModel!!.brandNames.isEmpty()) {
            val name = subCategoryTargetModel!!.brandNames
            splitted = name.split("\\;").toTypedArray()
            length = splitted.size
            for (i in splitted.indices) {
                if (splitted.size > 3 && i == 3) {
                    shortString = finalString
                }
                finalString = if (length > 1) {
                    length--
                    finalString + splitted[i] + ","
                } else {
                    finalString + splitted[i]
                }
            }
            mBinding!!.tvCompanyName.text = finalString
        }
        val vectorDrawable = AppCompatResources.getDrawable(this, R.drawable.logo_grey)
        if (!TextUtils.isNullOrEmpty(
                subCategoryTargetModel!!.companyLogoUrl
            )
        ) {
            Picasso.get().load(subCategoryTargetModel!!.companyLogoUrl)
                .placeholder(vectorDrawable!!).into(mBinding!!.imCompany)
        } else {
            mBinding!!.imCompany.setImageDrawable(vectorDrawable)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        try {
            val startDate = sdf.parse(subCategoryTargetModel!!.startDate)
            val endDate = sdf.parse(subCategoryTargetModel!!.endDate)
            val diff = endDate.time - startDate.time
            mBinding!!.tvDaysLeft.text = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                .toString() + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.days_left)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (subCategoryTargetModel!!.isCompleted && !subCategoryTargetModel!!.isClaimed && !subCategoryTargetModel!!.isTargetExpire) {
            mBinding!!.tvClaimButton.visibility = View.VISIBLE
        } else {
            mBinding!!.tvClaimButton.visibility = View.INVISIBLE
        }
        mBinding!!.tvClaimButton.setOnClickListener {
            if (utils!!.isNetworkAvailable) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.claimCustomerCompanyTarget(
                        claimObserver,
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                        subCategoryTargetModel!!.targetDetailId
                    )
                }
            } else {
                startActivity(Intent(applicationContext, NoInternetActivity::class.java))
            }
        }
        msgList = ArrayList()
        if (subCategoryTargetModel!!.brandNames != null && subCategoryTargetModel!!.brandNames.isNotEmpty()) {
            if (shortString.isNotEmpty()) {
                msgList!!.add(TargetModel("Buy $shortString", 0))
            } else {
                msgList!!.add(
                    TargetModel(
                        "Buy $finalString worth RS. " + DecimalFormat("##.##").format(
                            subCategoryTargetModel!!.target
                        ) + "/-", 0
                    )
                )
            }
        }
        if (subCategoryTargetModel!!.getmTargetCustomerBrandDcs() != null && subCategoryTargetModel!!.getmTargetCustomerBrandDcs().size > 0) {
            msgList!!.add(
                TargetModel(
                    "Buy " + subCategoryTargetModel!!.getmTargetCustomerBrandDcs().size + " Brands of " + subCategoryTargetModel!!.companyName,
                    1
                )
            )
        }
        if (subCategoryTargetModel!!.requiredNoOfLineItem > 0) {
            msgList!!.add(
                TargetModel(
                    "Buy " + subCategoryTargetModel!!.requiredNoOfLineItem + " line Item of " + subCategoryTargetModel!!.companyName,
                    2
                )
            )
        }
        if (subCategoryTargetModel!!.getmTargetCustomerItemDcs() != null && subCategoryTargetModel!!.getmTargetCustomerItemDcs().size > 0) {
            msgList!!.add(
                TargetModel(
                    "Buy minimum " + subCategoryTargetModel!!.getmTargetCustomerItemDcs().size + " products of " + subCategoryTargetModel!!.companyName,
                    3
                )
            )
        }
        mBinding!!.rvTarget.layoutManager = LinearLayoutManager(applicationContext)
        targetAdapter =
            TargetAdapter(this, msgList, subCategoryTargetModel, splitted.size, finalString)
        mBinding!!.rvTarget.adapter = targetAdapter
        if (subCategoryTargetModel!!.valueType == "DreamItem") {
            mBinding!!.tvPointsType.text = "Product Details"
            mBinding!!.RLWalletPoints.visibility = View.GONE
            if (subCategoryTargetModel!!.getmGiftItemDcs() != null && subCategoryTargetModel!!.getmGiftItemDcs().size > 0) {
                mBinding!!.recyclerGiftItemDC.visibility = View.VISIBLE
                giftItemAdapter =
                    AchievedGiftItemAdapter(this, subCategoryTargetModel!!.getmGiftItemDcs())
                mBinding!!.recyclerGiftItemDC.layoutManager =
                    LinearLayoutManager(applicationContext)
                mBinding!!.recyclerGiftItemDC.adapter = giftItemAdapter
                mBinding!!.rlDreamGift.visibility = View.GONE
            } else {
                mBinding!!.rlDreamGift.visibility = View.VISIBLE
                mBinding!!.rlDreamGift.setBackgroundResource(R.drawable.symbol_165_1)
            }
        } else {
            mBinding!!.tvPointsType.text = "WALLET POINTS"
            mBinding!!.recyclerGiftItemDC.visibility = View.GONE
            mBinding!!.RLWalletPoints.visibility = View.VISIBLE
            mBinding!!.rlDreamGift.visibility = View.GONE
            mBinding!!.tvWalletPoints.text = subCategoryTargetModel!!.walletValue.toString() + ""
        }
    }

    private fun calculatePercentage(currentMonthSales: Double, totalTarget: Double): Double {
        return currentMonthSales * 100 / totalTarget
    }

    fun showDialog(message: String?) {
        val customDialog = Dialog(this, R.style.CustomDialog)
        val mView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_info, null)
        customDialog.setContentView(mView)
        customDialog.setCancelable(false)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val pdTitle = mView.findViewById<TextView>(R.id.pd_title)
        val alert = mView.findViewById<TextView>(R.id.tv_alert)
        val cancel_btn = mView.findViewById<TextView>(R.id.cancel_btn)
        alert.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.alert_for_gps)
        pdTitle.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.licence_update_msg)
        cancel_btn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.cancel)
        okBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok_d)
        alert.visibility = View.GONE
        pdTitle.text = message
        val cancelBtn = mView.findViewById<TextView>(R.id.cancel_btn)
        okBtn.setOnClickListener { v: View? ->
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            Utils.fadeTransaction(this@AchievedTargetActivity)
            customDialog.dismiss()
        }
        cancelBtn.setOnClickListener { v: View? -> customDialog.dismiss() }
        customDialog.show()
    }

    private val claimObserver: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response.status) {
                        mBinding!!.tvClaimButton.isEnabled = false
                    }
                    showDialog(response.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding!!.tvClaimButton.isEnabled = false
            }

            override fun onComplete() {}
        }
}