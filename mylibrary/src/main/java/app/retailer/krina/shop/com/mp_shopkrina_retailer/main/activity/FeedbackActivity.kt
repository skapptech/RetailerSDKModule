package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFeedbackBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.FeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.FeedBackResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class FeedbackActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFeedbackBinding

    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null

    private var edtFeedbackComment: EditText? = null
    private var SubmitFeedback: Button? = null
    private var shopName: String? = null
    private var mobileNumber: String? = null
    private var customerId = 0
    private var rateUs: RatingBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)
        // init view
        initialization()
        // back BTn
        mBinding.toolbarFeedback.back.setOnClickListener { v: View? -> onBackPressed() }
        //submit feedback
        SubmitFeedback!!.setOnClickListener { v: View? ->
            if (rateUs!!.rating.toDouble() == 0.0) {
                Toast.makeText(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_rating),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edtFeedbackComment!!.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true) && edtFeedbackComment!!.text.toString()
                    .trim { it <= ' ' } == "" && edtFeedbackComment!!.text.toString()
                    .trim { it <= ' ' }
                    .isEmpty()) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.feedback_empty_field)
                )
            } else {
                if (utils!!.isNetworkAvailable) {
                    if (commonClassForAPI != null) {
                        SubmitFeedback!!.isEnabled = false
                        Utils.showProgressDialog(this)
                        commonClassForAPI!!.addfeedback(
                            feedbackDes,
                            FeedbackModel(
                                shopName,
                                customerId,
                                mobileNumber,
                                edtFeedbackComment!!.text.toString(),
                                rateUs!!.rating
                            )
                        )
                    }
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
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

    fun initialization() {
        shopName = SharePrefs.getInstance(this).getString(SharePrefs.SHOP_NAME)
        mobileNumber = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER)
        customerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        edtFeedbackComment = mBinding.edtFeedbackComment
        SubmitFeedback = mBinding.submitFeedback
        rateUs = mBinding.rateUs
        mBinding.toolbarFeedback.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Tiltle_feedback)
        mBinding.tvfeedback.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_feedback)
        mBinding.edtFeedbackComment.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.hint)
        mBinding.submitFeedback.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit_requestbrand)
    }

    // post feedback
    private val feedbackDes: DisposableObserver<FeedBackResponse> =
        object : DisposableObserver<FeedBackResponse>() {
            override fun onNext(feedBackResponse: FeedBackResponse) {
                Utils.hideProgressDialog()
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.toast_submitted)
                )
                onBackPressed()
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {}
        }
}