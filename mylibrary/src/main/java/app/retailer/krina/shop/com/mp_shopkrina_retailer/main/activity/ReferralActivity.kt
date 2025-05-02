package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReferralBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.RefConfigAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralConfigModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferredModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.observers.DisposableObserver

class ReferralActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityReferralBinding

    lateinit var sharePrefs: SharePrefs
    lateinit var utils: Utils
    lateinit var commonAPICall: CommonClassForAPI

    private var list: ArrayList<ReferralConfigModel>? = null
    private var referredList: ArrayList<ReferredModel>? = null
    private var refConfigAdapter: RefConfigAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_referral)
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.refer_and_earn)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnHow -> {
                showInfoDialog()
            }
            R.id.btnHistory -> {
                if (referredList != null && referredList?.size != 0)
                    startActivity(
                        Intent(applicationContext, ReferralHistoryActivity::class.java)
                            .putExtra("list", referredList)
                    )
                else
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.no_history_available)
                    )
            }
            R.id.btnShare -> {
                Utils.shareApp(this, false, sharePrefs.getString(SharePrefs.SK_CODE))
            }
            R.id.btnCopy -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    null,
                    "play.google.com/store/apps/details?id="
                            + packageName + "&referrer=" + sharePrefs.getString(SharePrefs.SK_CODE)
                )
                clipboard.setPrimaryClip(clip)
                Utils.setToast(applicationContext, "copied successfully")
            }
            R.id.btnMore -> {
                Utils.shareApp(applicationContext, false, sharePrefs.getString(SharePrefs.SK_CODE))
            }
        }
    }


    fun init() {
        binding.tvReferralDoubleH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.referral_double_dhamaka)
        binding.tvReferEarnH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.refer_your_friends_earn_rewards)
        binding.tvCodeH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.referral_code)
        binding.tvTotalRefH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.total_referred)
        binding.tvReferComH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.refer_completed)
        binding.tvRewardEarnH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.rewards_earned)
        binding.btnHow.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.how_it_work)
        binding.btnHistory.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.referral_history)
        binding.tvInviteFfH.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.invite_your_friends)
        binding.btnShare.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.invite)

        sharePrefs = SharePrefs(applicationContext)
        utils = Utils(this)
        commonAPICall = CommonClassForAPI.getInstance(this)

        list = ArrayList()

        binding.tvCode.text = sharePrefs.getString(SharePrefs.SK_CODE)
        binding.btnHow.setOnClickListener(this)
        binding.btnHistory.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnCopy.setOnClickListener(this)
        binding.btnMore.setOnClickListener(this)

        Utils.showProgressDialog(this)
        commonAPICall.getReferralConfig(refConfigObserver, sharePrefs.getInt(SharePrefs.CITY_ID))
        commonAPICall.getReferredList(
            referralListObserver,
            sharePrefs.getInt(SharePrefs.CUSTOMER_ID)
        )
    }

    private fun showInfoDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_referral)

        val tvHow = dialog.findViewById<TextView>(R.id.tvHow)
        val tvPointsMsg = dialog.findViewById<TextView>(R.id.tvPointsMsg)
        val tvOrderCountH = dialog.findViewById<TextView>(R.id.tvOrderCountH)
        val tvYouGetH = dialog.findViewById<TextView>(R.id.tvYouGetH)
        val tvFriendGetH = dialog.findViewById<TextView>(R.id.tvFriendGetH)
        val tcCreditOnH = dialog.findViewById<TextView>(R.id.tcCreditOnH)
        val rvReferral = dialog.findViewById<RecyclerView>(R.id.rvReferral)

        tvHow?.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.how_it_work)
        tvPointsMsg?.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.you_will_get_wallet_points_on_referral_msg)
        tvOrderCountH?.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.order_count)
        tvYouGetH?.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.you_get)
        tvFriendGetH?.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.friend_get)
        tcCreditOnH?.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.credit_on)

        refConfigAdapter = RefConfigAdapter(this, list!!)
        rvReferral?.adapter = refConfigAdapter

        dialog.show()
    }


    private var refConfigObserver: DisposableObserver<ArrayList<ReferralConfigModel>> =
        object : DisposableObserver<ArrayList<ReferralConfigModel>>() {
            override fun onNext(res: ArrayList<ReferralConfigModel>) {
                Utils.hideProgressDialog()
                list?.addAll(res)
                refConfigAdapter?.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    private var referralListObserver: DisposableObserver<ArrayList<ReferredModel>> =
        object : DisposableObserver<ArrayList<ReferredModel>>() {
            override fun onNext(res: ArrayList<ReferredModel>) {
                referredList = res
                val referred =
                    referredList?.filter { referredModel -> referredModel.IsUsed == 1 }

                val total = referred?.sumOf { it.referralWalletPoint }
                binding.tvTotalRefer.text = "" + referredList?.distinctBy { it.SkCode }?.size
                binding.tvReferred.text = "${referred?.distinctBy { it.SkCode }?.size}"
                binding.tvTotalEarn.text = "$total"
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {}
        }
}