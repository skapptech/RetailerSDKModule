package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityRateAppBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver

class RateAppActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRateAppBinding.inflate(layoutInflater)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initialization()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }


    fun initialization() {
        val utils = Utils(this)
        val commonClassForAPI = CommonClassForAPI.getInstance(this)
        mBinding.toolbarRateApp.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_rate_the_app)
        mBinding.tvHelpUsImprove.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.help_us_improve_our_service)
        mBinding.tvHowWouldYouRate.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.how_would_you_rate_our_app)
        mBinding.tvEnterFeedback.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_your_valuable_feedback)
        mBinding.editFeedback.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.type_here)
        mBinding.submitRating.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit)
        mBinding.submitRating.setOnClickListener { v: View? ->
            if (mBinding.rateUs.rating.toDouble() == 0.0) {
                Toast.makeText(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_rating),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mBinding.editFeedback.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true) && mBinding.editFeedback.text.toString()
                    .trim { it <= ' ' } == "" && mBinding.editFeedback.text.toString()
                    .trim { it <= ' ' }
                    .isEmpty()) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.feedback_empty_field)
                )
            } else {
                if (utils.isNetworkAvailable) {
                    if (commonClassForAPI != null) {
                        val `object` = JsonObject()
                        try {
                            `object`.addProperty(
                                "CustomerId",
                                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
                            )
                            `object`.addProperty("CustomerRating", mBinding.rateUs.rating)
                            `object`.addProperty(
                                "CustomerRatingCommnets",
                                mBinding.editFeedback.text.toString()
                            )
                        } catch (e: JsonIOException) {
                            e.printStackTrace()
                        }
                        Utils.showProgressDialog(this)
                        commonClassForAPI.addRating(feedbackDes, `object`)
                    }
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            }
        }
        mBinding.toolbarRateApp.back.setOnClickListener { onBackPressed() }
    }


    private val feedbackDes: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(response: MyProfileResponse) {
                Utils.hideProgressDialog()
                try {
                    if (response != null && response.isStatus) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.toast_submitted)
                        )
                    }
                    if (this@RateAppActivity != null) onBackPressed()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {}
        }
}