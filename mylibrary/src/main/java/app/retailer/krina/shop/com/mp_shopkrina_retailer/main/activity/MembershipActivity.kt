package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMembershipBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PrimeBenefitAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipModel.Benefit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso
import io.reactivex.observers.DisposableObserver

class MembershipActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMembershipBinding
    private var model: MembershipModel? = null
    private var list: ArrayList<Benefit>? = null
    private var benefitAdapter: PrimeBenefitAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_membership)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Sk " + SharePrefs.getInstance(applicationContext)
            .getString(SharePrefs.PRIME_NAME) + MyApplication.getInstance().dbHelper.getString(R.string.membership)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initialize()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvTerms -> if (LocaleHelper.getLanguage(applicationContext)
                    .equals("hi", ignoreCase = true)
            ) {
                startActivity(
                    Intent(applicationContext, WebViewActivity::class.java)
                        .putExtra(
                            "url",
                            EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/images/game/prime_terms_hindi.html"
                        )
                )
            } else {
                startActivity(
                    Intent(applicationContext, WebViewActivity::class.java)
                        .putExtra(
                            "url",
                            EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/images/game/prime_terms.html"
                        )
                )
            }
            R.id.btnRenew -> startActivity(
                Intent(
                    applicationContext,
                    MembershipPlanActivity::class.java
                )
            )
        }
    }

    override fun onBackPressed() {
        if (intent.extras != null && intent.getIntExtra("page", 0) == 1) {
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
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
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    private fun initialize() {
        mBinding.tvTerms.text = getString(R.string.app_name) + " " +
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.PRIME_NAME) +
                MyApplication.getInstance().dbHelper.getString(R.string.shopkirana_prime_terms_and_conditions)
        mBinding.tvRenewal.text =
            MyApplication.getInstance().dbHelper.getString(R.string.renewal_and_next_payment)
        mBinding.btnRenew.text =
            MyApplication.getInstance().dbHelper.getString(R.string.renew_membership)
        mBinding.tvMembershipBenefits.text =
            MyApplication.getInstance().dbHelper.getString(R.string.your_benefits)
        mBinding.btnRenew.setOnClickListener(this)
        mBinding.tvTerms.setOnClickListener(this)
        val commonClassForAPI = CommonClassForAPI.getInstance(this)
        list = ArrayList()
        benefitAdapter = PrimeBenefitAdapter(this, list!!)
        mBinding.rvBenefit.adapter = benefitAdapter
        Utils.showProgressDialog(this)
        commonClassForAPI.membershipDetail(
            observer,
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
            LocaleHelper.getLanguage(applicationContext)
        )
    }

    private fun setData(model: MembershipModel) {
        Picasso.get().load(model.logo).into(mBinding.ivImage)
        mBinding.tvTitle.text = SharePrefs.getInstance(applicationContext)
            .getString(SharePrefs.PRIME_NAME) + " " + MyApplication.getInstance().dbHelper.getString(
            R.string.savings
        )
        mBinding.tvAmount.text = "₹" + model.totalBenefit
        mBinding.tvDuration.text =
            MyApplication.getInstance().dbHelper.getString(R.string.since_you_joined) + Utils.getDateMonthFormat(
                model.startDate
            )
        mBinding.tvName.text = model.memberShipName
        mBinding.tvStartDate.text =
            MyApplication.getInstance().dbHelper.getString(R.string.valid_from) + Utils.getDateTimeFormate(
                model.startDate
            )
        mBinding.tvEndDate.text =
            MyApplication.getInstance().dbHelper.getString(R.string.valid_till) + Utils.getDateTimeFormate(
                model.endDate
            )
        mBinding.webView.loadData(
            "<style>img{display: inline;height: auto;max-width: 100%;}</style>"
                    + model.primeHtmL, "text/html", "utf-8"
        )
        val days = Utils.getRemainingDays(model.endDate)
        if (days < 7) {
            mBinding.btnRenew.visibility = View.VISIBLE
        } else {
            mBinding.btnRenew.visibility = View.GONE
        }
    }

    // prime plans
    private val observer: DisposableObserver<MembershipModel> =
        object : DisposableObserver<MembershipModel>() {
            override fun onNext(model1: MembershipModel) {
                Utils.hideProgressDialog()
                try {
                    if (model1 != null) {
                        model = model1
                        setData(model!!)
                        for (dto in model1.benefit) {
                            if (dto.amount != 0) {
                                list!!.add(dto)
                            }
                        }
                        benefitAdapter!!.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}