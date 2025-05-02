package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomerSubCategoryTargetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.PopupMoreInfoBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.TargetClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.YourLevelActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.CustomerSubCategoryItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NewTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CustomerTargetResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import io.reactivex.observers.DisposableObserver
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerSubCategoryTargetActivity : AppCompatActivity(), TargetClick {
    private lateinit var mBinding: ActivityCustomerSubCategoryTargetBinding

    private var utils: Utils? = null

    private var list: MutableList<NewTargetModel>? = null
    private var adapter: CustomerSubCategoryItemAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_customer_sub_category_target)

        mBinding.toolbar.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_target)
        mBinding.btnCheckLevel.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.check_level)
        mBinding.btnCheckTarget.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.check_target)
        mBinding.tvCurrentlyNoBrand.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.currently_no_brand_target_assigned_you_to_get_connected_with_us)
        mBinding.tvDate.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.target_objects)
        mBinding.tvTargetObject.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.target_object_not_created)
        mBinding.tvComingSoon.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.coming_soon)
        mBinding.tvPleaseCheck.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_check_back_later)

        list = ArrayList()
        adapter = CustomerSubCategoryItemAdapter(this, list, this)
        mBinding.recyclerCusSubTarget.adapter = adapter

        utils = Utils(this)

        mBinding.toolbar.back.setOnClickListener { onBackPressed() }
        mBinding.btnCheckLevel.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, YourLevelActivity::class.java
                )
            )
        }
        mBinding.btnCheckTarget.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, CustomerTargetActivity::class.java
                )
            )
        }
        // Target API call
        if (utils!!.isNetworkAvailable) {
            Utils.showProgressDialog(this)
            CommonClassForAPI.getInstance(this).fetchCustomerTarget(
                customerTargetDes,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE),
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            )
            CommonClassForAPI.getInstance(this).fetchCustomerSubCategoryTarget(
                customerSubCatTargetDes,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            )
        } else {
            startActivity(Intent(applicationContext, NoInternetActivity::class.java))
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            RetailerSDKApp.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
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


    override fun onClaimButtonClicked(id: Int, target: String?) {
        if (utils!!.isNetworkAvailable) {
            if (target.equals("customerTarget", ignoreCase = true)) {
                Utils.showProgressDialog(this)
                CommonClassForAPI.getInstance(this).claimCustomerTarget(
                    claimCustomerObserver,
                    SharePrefs.getInstance(
                        applicationContext
                    ).getInt(SharePrefs.WAREHOUSE_ID),
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE),
                    SharePrefs.getInstance(
                        applicationContext
                    ).getInt(SharePrefs.CUSTOMER_ID)
                )
            } else {
                Utils.showProgressDialog(this)
                CommonClassForAPI.getInstance(this).claimCustomerCompanyTarget(
                    claimObserver,
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                    id
                )
            }
        } else {
            startActivity(Intent(applicationContext, NoInternetActivity::class.java))
        }
    }

    override fun moreInfoDialog(model: NewTargetModel) {
        val dialog = BottomSheetDialog(this)
        val popupKnowMoreDialogBinding: PopupMoreInfoBinding = DataBindingUtil.inflate(
            this.layoutInflater, R.layout.popup_more_info, null, false
        )
        dialog.setContentView(popupKnowMoreDialogBinding.root)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(false)

        val isClaim = model.isClaimed
        val type = model.type
        val value = model.value
        var itemName = ""
        itemName = if (!model.giftItemName.isNullOrEmpty()) {
            model.giftItemName
        } else if (model.giftItemName.isNullOrEmpty() && type == "WalletPoint") {
            "Wallet Point " + model.value.toString()
        } else {
            type
        }

        if (model.achivePercent >= 100 && isClaim) {
            popupKnowMoreDialogBinding.onCompleti.text = getString(R.string.Congratulations)
            popupKnowMoreDialogBinding.tvGiftItemName.text = "You have won $itemName"
            popupKnowMoreDialogBinding.tvGiftItemName.setTextColor(
                ContextCompat.getColor(
                    this, R.color.greenish_cyan_color
                )
            )
        } else if (model.achivePercent >= 100) {
            popupKnowMoreDialogBinding.onCompleti.text = getString(R.string.Congratulations)
            popupKnowMoreDialogBinding.tvGiftItemName.text = "Claim Now to Redeem $itemName"
            popupKnowMoreDialogBinding.tvGiftItemName.setTextColor(
                ContextCompat.getColor(
                    this, R.color.greenish_cyan_color
                )
            )
        } else {
            popupKnowMoreDialogBinding.tvGiftItemName.text = "Get $itemName"
        }

        if (!TextUtils.isNullOrEmpty(model.giftImage)) {
            popupKnowMoreDialogBinding.giftImage.visibility = View.VISIBLE
            popupKnowMoreDialogBinding.RLWalletPoints.visibility = View.GONE
            popupKnowMoreDialogBinding.rlOffer.visibility = View.GONE
            popupKnowMoreDialogBinding.overlayImage.visibility = View.VISIBLE
            popupKnowMoreDialogBinding.overlayImageOffer.visibility = View.GONE
            popupKnowMoreDialogBinding.overlayImagePoint.visibility = View.GONE

            Picasso.get().load(model.giftImage).placeholder(R.drawable.logo_sk)
                .into(popupKnowMoreDialogBinding.shopimage)
            if (isClaim) {
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.GONE
                popupKnowMoreDialogBinding.overlayImage.visibility = View.GONE
            } else {
                popupKnowMoreDialogBinding.overlayImage.visibility = View.VISIBLE
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.VISIBLE
            }
        } else if (type == "WalletPoint") {
            popupKnowMoreDialogBinding.giftImage.visibility = View.GONE
            popupKnowMoreDialogBinding.rlOffer.visibility = View.GONE
            popupKnowMoreDialogBinding.RLWalletPoints.visibility = View.VISIBLE
            popupKnowMoreDialogBinding.tvWalletPoints.text = value.toString() + ""
            popupKnowMoreDialogBinding.overlayImage.visibility = View.GONE
            popupKnowMoreDialogBinding.overlayImageOffer.visibility = View.GONE
            popupKnowMoreDialogBinding.overlayImagePoint.visibility = View.VISIBLE
            if (isClaim) {
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.GONE
                popupKnowMoreDialogBinding.overlayImagePoint.visibility = View.GONE
                popupKnowMoreDialogBinding.overlayImage.visibility = View.VISIBLE
            } else {
                popupKnowMoreDialogBinding.overlayImagePoint.visibility = View.VISIBLE
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.VISIBLE
            }
        } else {
            popupKnowMoreDialogBinding.giftImage.visibility = View.GONE
            popupKnowMoreDialogBinding.RLWalletPoints.visibility = View.GONE
            popupKnowMoreDialogBinding.rlOffer.visibility = View.VISIBLE
            popupKnowMoreDialogBinding.tvOfferValue.text = "₹ $value"
            popupKnowMoreDialogBinding.overlayImage.visibility = View.GONE
            popupKnowMoreDialogBinding.overlayImageOffer.visibility = View.VISIBLE
            popupKnowMoreDialogBinding.overlayImagePoint.visibility = View.GONE
            if (isClaim) {
                popupKnowMoreDialogBinding.overlayImageOffer.visibility = View.GONE
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.GONE
            } else {
                popupKnowMoreDialogBinding.overlayImageOffer.visibility = View.VISIBLE
                popupKnowMoreDialogBinding.ivUnlock.visibility = View.VISIBLE
            }
        }

        popupKnowMoreDialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun showDialog(message: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_edit_info)
        dialog.setCancelable(false)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val pdTitle = dialog.findViewById<TextView>(R.id.pd_title)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfo)
        val alert = dialog.findViewById<TextView>(R.id.tv_alert)
        val btnCancel = dialog.findViewById<TextView>(R.id.cancel_btn)

        alert.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.congratulations)
        pdTitle.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.licence_update_msg)
        tvInfo.text = ""
        btnCancel.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.cancel)
        okBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok_d)
        pdTitle.text = message

        okBtn.setOnClickListener {
            dialog.dismiss()
            recreate()
        }
//        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }


    // Customer Target Response
    private val customerSubCatTargetDes: DisposableObserver<List<NewTargetModel>> =
        object : DisposableObserver<List<NewTargetModel>>() {
            override fun onNext(response: List<NewTargetModel>) {
                try {
                    Utils.hideProgressDialog()
                    if (response.size != 0) {
                        mBinding.llTargetObject.visibility = View.VISIBLE
                        mBinding.llTargetObjectEmpty.visibility = View.GONE

                        list!!.addAll(response)
//                        adapter!!.setData(list)
                        adapter?.notifyDataSetChanged()

                        mBinding.tvDateTarget.text =
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.your) + " " + SimpleDateFormat(
                                "MMMM", Locale.ENGLISH
                            ).format(
                                Date()
                            ) + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.month_target)
                    } else {
                        if (list != null && list!!.size > 0) {
                            mBinding.llTargetObject.visibility = View.VISIBLE
                            mBinding.llTargetObjectEmpty.visibility = View.GONE
                        } else {
                            mBinding.llTargetObject.visibility = View.GONE
                            mBinding.llTargetObjectEmpty.visibility = View.VISIBLE
                        }
                    }
                    if (mBinding.comingSoon.visibility == View.VISIBLE && mBinding.llTargetObjectEmpty.visibility == View.VISIBLE) {
                        mBinding.llComingSoon.visibility = View.VISIBLE
                        mBinding.scrollTarget.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                if (list != null && list!!.size > 0) {
                    mBinding.llTargetObject.visibility = View.VISIBLE
                    mBinding.llTargetObjectEmpty.visibility = View.GONE
                } else {
                    mBinding.llTargetObject.visibility = View.GONE
                    mBinding.llTargetObjectEmpty.visibility = View.VISIBLE
                }
            }

            override fun onComplete() {}
        }

    // Customer Target Response
    private val customerTargetDes: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null && response.getTargetData.targetAmount > 0 && response.status) {
                        mBinding.targetProgress.incrementProgressBy(response.getTargetData.achivePercent.toInt())
                        mBinding.txtProgress.text =
                            response.getTargetData.achivePercent.toInt().toString() + "%"
                        mBinding.tvLevel.text = response.getTargetData.level
                        mBinding.comingSoon.visibility = View.GONE
                        mBinding.llTarget.visibility = View.VISIBLE
                        mBinding.llTargetObject.visibility = View.VISIBLE
                        mBinding.llTargetObjectEmpty.visibility = View.GONE
                        mBinding.tvDateTarget.text =
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.your) + " " + SimpleDateFormat(
                                "MMMM", Locale.ENGLISH
                            ).format(
                                Date()
                            ) + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.month_target)
                        list!!.add(
                            NewTargetModel(
                                response.getTargetData.targetMonth,
                                response.getTargetData.companyId,
                                response.getTargetData.brandNames,
                                response.getTargetData.achivePercent,
                                response.getTargetData.targetAmount,
                                "ShopKirana",
                                "Target",
                                response.getTargetData.isClaimed,
                                response.getTargetData.value,
                                response.getTargetData.leftDays,
                                response.getTargetData.model,
                                response.getTargetData.storeUrl,
                                response.getTargetData.type,
                                response.getTargetData.giftImage,
                                response.getTargetData.giftItemName,
                                response.getTargetData.offerDesc
                            )
                        )
//                        adapter!!.setData(list)
                        adapter?.notifyDataSetChanged()
                    } else {
                        mBinding.llTarget.visibility = View.VISIBLE
                        mBinding.tvLevel.text = "Level 0"
                        mBinding.comingSoon.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                mBinding.llTarget.visibility = View.VISIBLE
                mBinding.tvLevel.text = "Level 0"
                mBinding.comingSoon.visibility = View.GONE
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    private val claimObserver: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null && response.status) {
                        showDialog(response.message)
                    } else {
                        showDialog(response.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    private val claimCustomerObserver: DisposableObserver<CustomerTargetResponse> =
        object : DisposableObserver<CustomerTargetResponse>() {
            override fun onNext(response: CustomerTargetResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null && response.status) {
                        showDialog(response.message)
                    } else {
                        showDialog(response.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}