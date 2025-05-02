package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentRateAppBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver

class RateAppFragment : Fragment() {
    var editProfileActivity = activity as? EditProfileActivity
    private lateinit var mBinding: FragmentRateAppBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        editProfileActivity = context as EditProfileActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentRateAppBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialization()
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            editProfileActivity!!,
            this.javaClass.simpleName,
            null
        )
    }


    fun initialization() {
        editProfileActivity!!.tv_title!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_rate_the_app)
        val utils = Utils(activity)
        val commonClassForAPI = CommonClassForAPI.getInstance(activity)
        mBinding.helpUsImproveOurService.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.help_us_improve_our_service)
        mBinding.howWouldYouRateOurApp.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.how_would_you_rate_our_app)
        mBinding.enterYourValuableFeedback.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_your_valuable_feedback)
        mBinding.submitRating.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit)
        mBinding.editFeedback.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.type_here)
        mBinding.submitRating.setOnClickListener { v: View? ->
            if (mBinding.rateUs.rating.toDouble() == 0.0) {
                Toast.makeText(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_rating),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mBinding.editFeedback.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true) && mBinding.editFeedback.text.toString()
                    .trim { it <= ' ' } == "" && mBinding.editFeedback.text.toString()
                    .trim { it <= ' ' }
                    .isEmpty()) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.feedback_empty_field)
                )
            } else {
                if (utils.isNetworkAvailable) {
                    if (commonClassForAPI != null) {
                        val `object` = JsonObject()
                        try {
                            `object`.addProperty(
                                "CustomerId",
                                SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                            )
                            `object`.addProperty("CustomerRating", mBinding.rateUs.rating)
                            `object`.addProperty(
                                "CustomerRatingCommnets",
                                mBinding.editFeedback.text.toString()
                            )
                        } catch (e: JsonIOException) {
                            e.printStackTrace()
                        }
                        Utils.showProgressDialog(activity)
                        commonClassForAPI.addRating(feedbackDes, `object`)
                    }
                } else {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            }
        }
    }

    // post rating
    private val feedbackDes: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(response: MyProfileResponse) {
                Utils.hideProgressDialog()
                if (response != null && response.isStatus) {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.toast_submitted)
                    )
                }
                try {
                    if (activity != null) activity!!.onBackPressed()
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

    companion object {
        fun newInstance(): RateAppFragment {
            return RateAppFragment()
        }
    }
}