package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMembershipPlanBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PrimeBannerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PrimePlanAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipPlanModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PrimePaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.PrimePaymentResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import org.json.JSONObject

class MembershipPlanActivity : AppCompatActivity(), OnButtonClick, View.OnClickListener {
    private val HDFC_REQUEST = 999

    private lateinit var mBinding: ActivityMembershipPlanBinding

    private var commonClassForAPI: CommonClassForAPI? = null
    private var list: ArrayList<MembershipPlanModel>? = null
    private var bannerAdapter: PrimeBannerAdapter? = null
    private var planAdapter: PrimePlanAdapter? = null

    private var hdfcRequest: String? = null
    private var pos = 0
    private var orderId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_membership_plan)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "SK " + SharePrefs.getInstance(applicationContext)
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

    override fun onResume() {
        super.onResume()
        if (planAdapter != null) planAdapter!!.notifyDataSetChanged()
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == HDFC_REQUEST) {
                if (data != null && resultCode == RESULT_OK) {
                    val `object`: JSONObject
                    try {
                        `object` = JSONObject(data.getStringExtra(AvenuesParams.RESPONSE))
                        //                        hdfcTxtAmt = Double.parseDouble(object.getString("amount"));
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.transaction_p) + " " + `object`.getString(
                                AvenuesParams.STATUS
                            )
                        )
                        if (`object`.getString(AvenuesParams.STATUS)
                                .equals("Success", ignoreCase = true)
                        ) {
                            updatePaymentAPI(
                                `object`.getString(AvenuesParams.TRACKING_ID),
                                `object`.getString(AvenuesParams.STATUS), `object`.toString()
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        this.pos = pos
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.CUST_ACTIVE)) {
            if (itemAdded) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.primePaymentRequest(
                    paymentReqObserver,
                    PrimePaymentModel(
                        list!![pos].id, "", "", "",
                        "", "HDFC",
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID), 0
                    )
                )
            } else {
                if (!list!![pos].isTaken) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.primePaymentRequest(
                        paymentReqObserver,
                        PrimePaymentModel(
                            list!![pos].id, "", "", "",
                            "", "HDFC",
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.CUSTOMER_ID), 0
                        )
                    )
                }
            }
        } else {
            Utils.showSnackBar(
                mBinding.rvPlan,
                MyApplication.getInstance().dbHelper.getString(R.string.inactive_account_cannot_join_prime),
                true,
                MyApplication.getInstance().dbHelper.getString(R.string.ok)
            )
        }
    }

    private fun initialize() {
        mBinding.tvTerms.text = getString(R.string.app_name) + " " +
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.PRIME_NAME) +
                MyApplication.getInstance().dbHelper.getString(R.string.shopkirana_prime_terms_and_conditions)
        mBinding.tvTerms.setOnClickListener(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        list = ArrayList()
        bannerAdapter = PrimeBannerAdapter(this, list)
        mBinding.viewPager.adapter = bannerAdapter
        mBinding.indicator.setViewPager(mBinding.viewPager)
      //  mBinding.indicator.strokeColor = applicationContext.resources.getColor(R.color.hint_color)
       // mBinding.indicator.fillColor = applicationContext.resources.getColor(R.color.black)
        mBinding.rvPlan.addItemDecoration(DividerItemDecoration(applicationContext, 1))
        planAdapter = PrimePlanAdapter(this, list!!, this)
        mBinding.rvPlan.adapter = planAdapter
        mBinding.webView.settings.loadWithOverviewMode = true
        mBinding.webView.settings.useWideViewPort = false
        mBinding.webView.settings.javaScriptEnabled = true
        Utils.showProgressDialog(this)
        commonClassForAPI?.getAllMemberShip(
            planObserver,
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
            LocaleHelper.getLanguage(applicationContext)
        )
    }

    // HDFC payment request intent
    private fun callHDFC(orderId: String, amount: String) {
        val intent = Intent(applicationContext, HDFCActivity::class.java)
        intent.putExtra(AvenuesParams.ORDER_ID, orderId)
        intent.putExtra(AvenuesParams.AMOUNT, amount)
        intent.putExtra(AvenuesParams.CURRENCY, "INR")
        intent.putExtra(
            AvenuesParams.MERCHANT_ID,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.MERCHANT_ID)
        )
        intent.putExtra(
            AvenuesParams.ACCESS_CODE,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.ACCESS_CODE)
        )
        intent.putExtra(
            AvenuesParams.WORKING_KEY,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.WORKING_KEY)
        )
        intent.putExtra(
            AvenuesParams.REDIRECT_URL,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.REDIRECT_URL)
        )
        intent.putExtra(
            AvenuesParams.CANCEL_URL,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CANCEL_URL)
        )
        intent.putExtra(
            AvenuesParams.URL,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.GATWAY_URL)
        )
        hdfcRequest = intent.extras.toString()
        startActivityForResult(intent, HDFC_REQUEST)
    }

    private fun updatePaymentAPI(transactionId: String, status: String, response: String) {
        Utils.showProgressDialog(this)
        commonClassForAPI!!.primePaymentResponse(
            paymentResObserver,
            PrimePaymentModel(
                list!![pos].id,
                transactionId,
                status,
                hdfcRequest,
                response,
                "HDFC",
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                orderId.toLong()
            )
        )
    }

    private fun profiledData() {
        commonClassForAPI!!.fetchProfileData(
            getMyProfile,
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
            Utils.getDeviceUniqueID(this)
        )
    }

    private fun orderConformationPopup(startDate: String, endDate: String) {
        val dialog = Dialog(this, R.style.CustomDialog)
        val mView = layoutInflater.inflate(R.layout.dialog_prime_joined, null)
        dialog.setContentView(mView)
        dialog.setCancelable(false)
        val orderMsg = mView.findViewById<TextView>(R.id.orderMsg)
        val tvDate = mView.findViewById<TextView>(R.id.tvDate)
        val tv_congratulation = mView.findViewById<TextView>(R.id.tv_congratulation)
        val btnContinue = mView.findViewById<Button>(R.id.btnContinue)
        tv_congratulation.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Congratulation)
        btnContinue.text = MyApplication.getInstance().dbHelper.getString(R.string.txt_Continue)
        orderMsg.text =
            MyApplication.getInstance().dbHelper.getString(R.string.on_your_sk_prime_membership) +
                    " " + SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.PRIME_NAME) +
                    MyApplication.getInstance().dbHelper.getString(R.string.membership)
        tvDate.text = "" +
                MyApplication.getInstance().dbHelper.getString(R.string.subscribed_on)
        Utils.getDateTimeFormate(startDate) + MyApplication.getInstance().dbHelper.getString(R.string.valid_till) + Utils.getDateTimeFormate(
            endDate
        )

        btnContinue.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(
                    applicationContext,
                    MembershipActivity::class.java
                ).putExtra("page", 1)
            )
            finish()
        }
        dialog.show()
        MyApplication.getInstance().updateAnalytics("prime_joined_dialog")
    }

    // prime plans
    private val planObserver: DisposableObserver<ArrayList<MembershipPlanModel>> =
        object : DisposableObserver<ArrayList<MembershipPlanModel>>() {
            override fun onNext(arrayList: ArrayList<MembershipPlanModel>) {
                Utils.hideProgressDialog()
                try {
                    if (arrayList != null && arrayList.size > 0) {
                        list!!.clear()
                        list!!.addAll(arrayList)
                        mBinding.webView.loadData(
                            "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + list!![0].memberShipDescription,
                            "text/html",
                            "utf-8"
                        )
                        bannerAdapter!!.notifyDataSetChanged()
                        planAdapter!!.notifyDataSetChanged()
                        //                    mBinding.tvName.setText(arrayList.get(0).getMemberShipName());
//                    mBinding.tvPrice.setText("Rs. " + arrayList.get(0).getAmount() + "/-");
//                    mBinding.tvDetail.setText(Html.fromHtml(arrayList.get(0).getMemberShipDescription()));
//                    if (list.get(0).isTaken()) {
//                        mBinding.tvDate.setText("Subscribed on " + Utils.getDateTimeFormate(list.get(0).getStartDate()));
//                        mBinding.btnBuy.setText("Subscribed");
//                    } else {
//                        mBinding.tvDate.setText("");
//                        mBinding.btnBuy.setText(getResources().getString(R.string.buy_now));
//                    }
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

    // prime payment request
    private val paymentReqObserver: DisposableObserver<PrimePaymentResponse> =
        object : DisposableObserver<PrimePaymentResponse>() {
            override fun onNext(model: PrimePaymentResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (model.status) {
                        Utils.showProgressDialog(this@MembershipPlanActivity)
                        orderId = model.transId
                        callHDFC(orderId.toString(), "" + list!![pos].amount)
                    } else {
                        Utils.setToast(
                            applicationContext, model.message
                        )
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

    // prime payment response
    private val paymentResObserver: DisposableObserver<Boolean> =
        object : DisposableObserver<Boolean>() {
            override fun onNext(b: Boolean) {
                try {
                    Utils.hideProgressDialog()
                    if (b) {
                        profiledData()
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

    private val getMyProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(myProfileResponse: MyProfileResponse) {
                Utils.hideProgressDialog()
                if (myProfileResponse.isStatus) {
                    val customer = myProfileResponse.customers
                    if (customer != null) {
                        SharePrefs.getInstance(applicationContext)
                            .putBoolean(SharePrefs.IS_PRIME_MEMBER, customer.isPrimeCustomer)
                        orderConformationPopup(customer.primeStartDate!!, customer.primeEndDate!!)
                        Utils.showProgressDialog(this@MembershipPlanActivity)
                        commonClassForAPI!!.clearAllCartItem(
                            clearCartObserver,
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.CUSTOMER_ID),
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.WAREHOUSE_ID)
                        )
                    }
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

    // clear cart
    private val clearCartObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(`object`: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    if (`object`["Status"].asBoolean) {
                        MyApplication.getInstance().clearCartData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}