package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityDeliveryFeedbackBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DeliveryFeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.FeedbackQuestionsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GetOrderAtFeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.reactivex.observers.DisposableObserver

class DeliveryFeedbackActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDeliveryFeedbackBinding

    private lateinit var commonClassForAPI: CommonClassForAPI
    private var questionList: ArrayList<FeedbackQuestionsModel>? = null
    private var radioList: ArrayList<RadioButton>? = null
    private var orderid = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_feedback)
        initialization()
        val extras = intent.extras
        if (extras != null) {
            val OrderId = extras.getString("ORDER_ID")
            if (!TextUtils.isNullOrEmpty(OrderId)) {
                commonClassForAPI.getOrderDetails(orderDetaildis, extras.getString("ORDER_ID"))
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun initialization() {
        mBinding.tvShopName.text = SharePrefs.getInstance(this).getString(SharePrefs.SHOP_NAME)
        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback5)
        mBinding.title.text = MyApplication.getInstance().dbHelper.getString(R.string.msg_yourorder)
        mBinding.tvAmountPaid.text =
            MyApplication.getInstance().dbHelper.getString(R.string.amount_paid)
        mBinding.etComment.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.write_your_comment_here)
        mBinding.btnSubmitFeedback.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.submit)
        mBinding.tvReactionText.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.rating_accroding)
        radioList = ArrayList()
        questionList = ArrayList()
        commonClassForAPI = CommonClassForAPI.getInstance(this)

        Utils.showProgressDialog(this)
        commonClassForAPI.fetchCustomerFeedbackQuestions(
            feedbackQuestionObserver,
            SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        )
        mBinding.skipFeedback.setOnClickListener { view: View? -> finish() }
        mBinding.btnSubmitFeedback.setOnClickListener { view: View? -> checkFormData() }
        mBinding.ratingbar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                when (mBinding.ratingbar.rating.toInt()) {
                    0 -> {
                        mBinding.ratingbar.rating = 1f
                        onClick(view)
                    }
                    1 -> {
                        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback1)
                        mBinding.tvReactionText.text = "Very Poor"
                        mBinding.queText.text = "नीचे दिए गए विकल्पों में से चुनें"
                    }
                    2 -> {
                        mBinding.tvReactionText.text = "Poor"
                        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback2)
                        mBinding.queText.text = "नीचे दिए गए विकल्पों में से चुनें"
                    }
                    3 -> {
                        mBinding.tvReactionText.text = "Good"
                        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback3)
                        mBinding.queText.text = "रेटिंग देने के लिए धन्यवाद"
                    }
                    4 -> {
                        mBinding.tvReactionText.text = "Very Good"
                        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback4)
                        mBinding.queText.text = "रेटिंग देने के लिए धन्यवाद"
                    }
                    5 -> {
                        mBinding.tvReactionText.text = "Excellent"
                        mBinding.ivReactionImage.setBackgroundResource(R.drawable.feedback5)
                        mBinding.queText.text = "रेटिंग देने के लिए धन्यवाद"
                    }
                }
                radioList!!.clear()
                mBinding.liRadio.removeAllViews()
                setQuestions(mBinding.ratingbar.rating.toInt())
            }
        })
    }

    private fun setQuestions(rating: Int) {
        mBinding.liRadio.clearCheck()
        radioList!!.clear()
        mBinding.liRadio.removeAllViews()
        for (i in questionList!!.indices) {
            if (rating == questionList!![i].ratingfrom) {
                createRadio(questionList!![i])
            }
        }
        if (radioList!!.isEmpty()) {
            mBinding.liRadio.visibility = View.GONE
        } else {
            mBinding.liRadio.visibility = View.VISIBLE
        }
    }

    private fun createRadio(model: FeedbackQuestionsModel) {
        val radioBtn = RadioButton(this)
        radioBtn.id = model.id
        radioBtn.layoutParams = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )
        radioBtn.text = model.question
        radioBtn.setPadding(20, 20, 20, 20) // in pixels (left, top, right, bottom)
        radioList!!.add(radioBtn)
        mBinding.liRadio.addView(radioBtn)
    }

    private fun checkFormData() {
        var ids = ""
        val rating = mBinding.ratingbar.rating.toInt()
        val comment = mBinding.etComment.text.toString()
        for (button in radioList!!) {
            if (button.isChecked) {
                ids = button.id.toString()
            }
        }
        if (rating > 0) {
            if (radioList!!.size != 0) {
                if (ids == "") {
                    Toast.makeText(
                        this,
                        MyApplication.getInstance().dbHelper.getString(R.string.select_any_one_option),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Utils.showProgressDialog(this)
                    commonClassForAPI.postCustomerFeedbackData(
                        postFeedbackObserver,
                        DeliveryFeedbackModel(
                            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                            orderid,
                            rating,
                            ids,
                            comment
                        )
                    )
                }
            } else if (radioList!!.isEmpty()) {
                Utils.showProgressDialog(this)
                commonClassForAPI.postCustomerFeedbackData(
                    postFeedbackObserver,
                    DeliveryFeedbackModel(
                        SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                        orderid,
                        rating,
                        ids,
                        comment
                    )
                )
            }
        } else {
            Toast.makeText(
                this,
                MyApplication.getInstance().dbHelper.getString(R.string.please_select_any_one_rating),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private val feedbackQuestionObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(jsonObject: JsonObject) {
                try {
                    questionList = Gson().fromJson(
                        jsonObject["Questiondata"].asJsonArray,
                        object : TypeToken<ArrayList<FeedbackQuestionsModel?>?>() {}.type
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Utils.hideProgressDialog()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    private val postFeedbackObserver: DisposableObserver<DeliveryFeedbackModel> =
        object : DisposableObserver<DeliveryFeedbackModel>() {
            override fun onNext(model: DeliveryFeedbackModel) {
                try {
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Utils.hideProgressDialog()
                Toast.makeText(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.thank_you_for_your_feedback),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    private val orderDetaildis: DisposableObserver<*> =
        object : DisposableObserver<GetOrderAtFeedbackModel>() {
            override fun onNext(`object`: GetOrderAtFeedbackModel) {
                Utils.hideProgressDialog()
                try {
                    if (`object` != null) {
                        orderid = `object`.orderid
                        val date = `object`.ordereddate.substring(0, 10).split("-").toTypedArray()
                        mBinding.tvDeliveryDate.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.delivered_on) + " " + date[2] + "-" + date[1] + "-" + date[0]
                        mBinding.tvAmount.text = "₹" + `object`.grossamount
                        mBinding.tvOrderId.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.order_id) + " " + orderid
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

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}