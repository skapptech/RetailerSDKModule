package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddPaymentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.flip.AphidLog.TAG
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.HDFCActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostUPIPaymentResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import io.reactivex.observers.DisposableObserver
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class AddPaymentActivity : AppCompatActivity(), View.OnClickListener,
    PaymentResultWithDataListener {
    private val SK_CREDIT = 888
    private val HDFC_REQUEST = 999

    lateinit var binding: ActivityAddPaymentBinding
    private lateinit var viewModel: AddPaymentViewModel

    private lateinit var commonAPICall: CommonClassForAPI
    private lateinit var sharePrefs: SharePrefs
    private var skCreditRes: CreditLimit? = null

    private var amount = ""
    private var screen = 0
    private var hdfcRequest = ""
    private var orderId = 0L
    private var orderAmount = 0.0
    private var clickedPos = 0
    private var iCICIMerchantId = ""
    private var iciciPayRequest = ""
    private var customProgressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPaymentBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.gullak_balance)

        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this, AddPaymentViewModelFactory(application, appRepository)
        )[AddPaymentViewModel::class.java]

        orderId = System.currentTimeMillis()
        try {
            if (intent.extras != null) {
                if (intent.hasExtra("amount")) {
                    orderAmount = intent.getDoubleExtra("amount", 0.0)
                    orderAmount++
                    val amount1: String =
                        DecimalFormat("##.##").format(orderAmount)
                    screen = intent.getIntExtra("screen", 0)
                    binding.tiAmount.editText?.setText("" + amount1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        observe(viewModel.getICICIPaymentCheckData, ::handleICICITransactionResult)

        activePaymentMode()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (intent.extras != null) {
            if (intent.getIntExtra("type", 0) == 2) {
                showSnackBar("Please recharge your Gullak")
            }
        }

        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        binding.btnPay.isEnabled = true
        binding.btnHDFC.isEnabled = true
        binding.btnDUdhaar.isEnabled = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPay -> validateFields()
            R.id.btnHDFC -> {
                clickedPos = 0
                addPaymentReq()
            }

            R.id.btnICICI -> {
                clickedPos = 3
                addPaymentReq()
            }

            R.id.btnDUdhaar -> {
                clickedPos = 1
                addPaymentReq()
            }

            R.id.btnRazorpy -> {
                clickedPos = 2
                addPaymentReq()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HDFC_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                val jsonObject: JSONObject
                try {
                    Utils.showProgressDialog(this@AddPaymentActivity)
                    jsonObject = JSONObject(data.getStringExtra(AvenuesParams.RESPONSE))
                    Utils.setToast(
                        applicationContext,
                        "Transaction " + jsonObject.getString(AvenuesParams.STATUS)
                    )
                    if (jsonObject.getString(AvenuesParams.STATUS)
                            .equals("Success", ignoreCase = true)
                    ) {
                        if (Utils.isNetworkAvailable(applicationContext)) {
                            val transactionModel = PostUPIPaymentResponse(
                                jsonObject.getString(AvenuesParams.ORDER_ID),
                                sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                                jsonObject.getString(AvenuesParams.ORDER_ID),
                                "",
                                jsonObject.toString(),
                                "HDFC",
                                jsonObject.getString(AvenuesParams.STATUS)
                            )
                            commonAPICall.postPaymentResponse(
                                postPaymentResponseObserver,
                                transactionModel
                            )
                        } else {
                            Utils.setToast(
                                applicationContext,
                                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                            )
                        }
                    } else {
                        if (Utils.isNetworkAvailable(applicationContext)) {
                            try {
                                val transactionModel = PostUPIPaymentResponse(
                                    jsonObject.getString(AvenuesParams.ORDER_ID),
                                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                                    jsonObject.getString(AvenuesParams.ORDER_ID),
                                    "",
                                    jsonObject.toString(),
                                    "HDFC",
                                    jsonObject.getString(AvenuesParams.STATUS)
                                )
                                commonAPICall.postPaymentResponse(
                                    postPaymentResponseObserver,
                                    transactionModel
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Utils.setToast(
                                applicationContext,
                                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Utils.hideProgressDialog()
                }
            }
        } else if (requestCode == SK_CREDIT) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Utils.showProgressDialog(this@AddPaymentActivity)
//                    val accountId = data.getStringExtra("accountId")
//                    val orderId = data.getStringExtra("orderId")
                    val status = data.getStringExtra("status")
//                    val amount = data.getStringExtra("amount")
                    val transactionRefNo = data.getStringExtra("transactionRefNo")

                    val transactionModel = PostUPIPaymentResponse(
                        orderId.toString(),
                        sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                        transactionRefNo,
                        "",
                        data.extras.toString(),
                        "DirectUdhar",
                        status
                    )
                    commonAPICall.postPaymentResponse(
                        postPaymentResponseObserver,
                        transactionModel
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_cancel)
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    override fun onPaymentSuccess(s: String, paymentData: PaymentData) {
        println(
            """
            Payment Successful :
            Payment ID: $s
            Payment Data: ${paymentData.data}
            """.trimIndent()
        )
        val jsonObject = paymentData.data
        try {
            val razorpayPaymentId = jsonObject.getString("razorpay_payment_id")
            try {
                Utils.showProgressDialog(this@AddPaymentActivity)
                if (Utils.isNetworkAvailable(applicationContext)) {
                    val transactionModel = PostUPIPaymentResponse(
                        orderId.toString(),
                        sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                        razorpayPaymentId,
                        "",
                        jsonObject.toString(),
                        "razorpay",
                        "Success"
                    )
                    commonAPICall.postPaymentResponse(
                        postPaymentResponseObserver,
                        transactionModel
                    )
                    Utils.setToast(
                        applicationContext,
                        "Payment successfully added"
                    )
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    override fun onPaymentError(i: Int, s: String?, paymentData: PaymentData) {
        val jsonObject = paymentData.data
        println(
            """
            Payment Failed:
            Payment Data: ${paymentData.data}
            """.trimIndent()
        )
        if (Utils.isNetworkAvailable(applicationContext)) {
            try {
                val transactionModel = PostUPIPaymentResponse(
                    orderId.toString(),
                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                    "",
                    "",
                    jsonObject.toString(),
                    "razorpay",
                    "Failed"
                )
                commonAPICall.postPaymentResponse(
                    postPaymentResponseObserver,
                    transactionModel
                )
                Utils.setToast(
                    applicationContext,
                    "Payment  added failed"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }


    private fun activePaymentMode() {
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_HDFC_PAYMENT)) {
            binding.btnHDFC.visibility = View.VISIBLE
        } else {
            binding.btnHDFC.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_RAZORPAY_PAYMENT)) {
            binding.btnRazorpy.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_ICICI_PAYMENT)) {
            binding.btnICICI.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.CUST_ACTIVE)) {
            binding.btnHDFC.visibility = View.GONE
            binding.btnICICI.visibility = View.GONE
            binding.btnDUdhaar.visibility = View.GONE
            binding.btnRazorpy.visibility = View.GONE
        }
    }

    private fun addPaymentReq() {
        amount = binding.tiAmount.editText?.text.toString().trim()
        if (TextUtils.isNullOrEmpty(amount)) {
            binding.tiAmount.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.amount_required)
        } else if (amount.toDouble() < 1) {
            binding.tiAmount.error =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.amount_greater_than_zero)
        } else {
            binding.tiAmount.isErrorEnabled = false
            Utils.showProgressDialog(this)
            if (Utils.isNetworkAvailable(applicationContext)) {
                val paymentRequestModel = PaymentRequestModel(
                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                    amount.toDouble(), "", "", "HDFC",
                    sharePrefs.getString(SharePrefs.CUSTOMER_TYPE),
                    "RetailerApp"
                )
                binding.btnHDFC.isEnabled = false
                binding.btnDUdhaar.isEnabled = false
                if (clickedPos == 0) {
                    paymentRequestModel.PaymentFrom = "HDFC"
                } else if (clickedPos == 1) {
                    paymentRequestModel.PaymentFrom = "DirectUdhar"
                } else if (clickedPos == 2) {
                    paymentRequestModel.PaymentFrom = "Razorpay"
                } else if (clickedPos == 3) {
                    paymentRequestModel.PaymentFrom = "icici"
                }
                commonAPICall.paymentRequest(paymentRequestObserver, paymentRequestModel)
            }
        }
    }


    private fun getHDFCRSAKey(isCredit: Boolean) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            Utils.showProgressDialog(this)
            viewModel.getHDFCRSAKey(
                orderId.toString(),
                amount,
                isCredit,
                "Payment Screen"
            )
            viewModel.getHDFCRSAKeyData.observe(this) {
                try {
                    Utils.hideProgressDialog()
                    if (!TextUtils.isNullOrEmpty(it)) {
                        callHDFC(orderId.toString(), it, amount)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // HDFC payment request intent
    private fun callHDFC(orderId: String, rsaKey: String, amount: String) {
        val intent = Intent(applicationContext, HDFCActivity::class.java)
        intent.putExtra(AvenuesParams.ORDER_ID, orderId)
        intent.putExtra(AvenuesParams.AMOUNT, amount)
        intent.putExtra(AvenuesParams.CURRENCY, "INR")
        intent.putExtra(AvenuesParams.RSA_KEY, rsaKey)
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

    private fun iCICIPaymentFailed(paymentFrom: String) {
        val transactionModel = PostUPIPaymentResponse(
            orderId.toString(),
            sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
            "",
            iciciPayRequest,
            "",
            paymentFrom,
            "Failed"
        )
        commonAPICall.postPaymentResponse(
            postPaymentResponseObserver,
            transactionModel
        )
    }


    private fun initViews() {
        binding.tvAddPayment.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_payment_to_gullak)
        binding.btnHDFC.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_payment_to_gullak)
        binding.tvGullakTerms.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.gullak_money_cannot_be_transferred)
        binding.btnRazorpy.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_from_razorpay)

        sharePrefs = SharePrefs(applicationContext)
        commonAPICall = CommonClassForAPI.getInstance(this)
        binding.btnPay.setOnClickListener(this)
        binding.btnHDFC.setOnClickListener(this)
        binding.btnICICI.setOnClickListener(this)
        binding.btnDUdhaar.setOnClickListener(this)
        binding.btnRazorpy.setOnClickListener(this)

        if (sharePrefs.getBoolean(SharePrefs.IS_D_UDHAR_GULLAK)) {
            commonAPICall.getCreditLimit(
                creditLimitObserver,
                sharePrefs.getInt(SharePrefs.CUSTOMER_ID)
            )
        } else {
            binding.btnDUdhaar.visibility = View.GONE
        }
    }

    private fun showSnackBar(message: String) {
        val snack: Snackbar = Snackbar.make(binding.tiAmount, message, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }

    private fun validateFields() {
        val amount = binding.tiAmount.editText?.text.toString().trim()

        if (TextUtils.isNullOrEmpty(amount)) {
            binding.tiAmount.error = "Payment amount is required!"
        } else if (amount.toDouble() < 1) {
            binding.tiAmount.error = "Payment amount should be greater than 0"
        } else {
            //  tiUpi.isErrorEnabled = false
            binding.tiAmount.isErrorEnabled = false

            if (orderAmount != 0.0 && binding.tiAmount.editText?.text.toString()
                    .toDouble() < orderAmount
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Your payment amount is less than Order amount")
                builder.setPositiveButton("ok") { dialog, _ -> dialog.dismiss() }
                builder.show()
            } else {
                binding.btnPay.isEnabled = false
                val paymentRequestModel = PaymentRequestModel(
                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                    amount.toDouble(), "", "", "HDFC",
                    sharePrefs.getString(SharePrefs.CUSTOMER_TYPE),
                    "RetailerApp"
                )
                commonAPICall.paymentRequest(paymentRequestObserver, paymentRequestModel)
            }
        }
    }

    private fun creditPayment(txnId: String, amount: String) {
        if (skCreditRes != null) {
            commonAPICall.creditPayment(
                skCreditPayObserver, CreditPayment(
                    1,
                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                    orderId,
                    skCreditRes!!.dynamicData!!.accountId,
                    amount.toDouble(),
                    "Gullak"
                )
            )
        } else {
            Utils.setToast(applicationContext, getString(R.string.please_try_again))
        }
    }

    private fun callRazorpay(orderId: String) {
        val checkout = Checkout()
        checkout.setKeyID(
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.RAZORPAY_KEY_ID)
        )
        checkout.setImage(R.drawable.direct_sign)
        try {
            val options = JSONObject()
            options.put(
                "name",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
            )
            options.put("description", "Reference No. #$orderId")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("order_id", orderId) //from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", amount) //pass amount in currency subunits
            options.put(
                "prefill.email",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_EMAIL)
            )
            options.put(
                "prefill.contact",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
            )
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            checkout.open(this, options)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e)
        }
    }


    private val paymentRequestObserver: DisposableObserver<Long?> =
        object : DisposableObserver<Long?>() {
            override fun onNext(txnId: Long) {
                Utils.hideProgressDialog()
                try {
                    orderId = txnId
//                    random = txnId.toInt()
                    if (Utils.isNetworkAvailable(applicationContext)) {
                        Utils.showProgressDialog(this@AddPaymentActivity)
                        if (clickedPos == 0) {
                            getHDFCRSAKey(false)
                        } else if (clickedPos == 1) {
                            creditPayment(txnId.toString(), amount)
                        } else if (clickedPos == 2) {
                            commonAPICall.fetchRazorpayOrderId(
                                getRazorpayOrderObserver,
                                orderId.toString(),
                                amount.toDouble(),
                                "Payment Screen"
                            )
                        } else if (clickedPos == 3) {
                            //callICICIPay(txnId.toString())
                        }
                    } else {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // sk credit payment response
    private val skCreditPayObserver: DisposableObserver<CreditLimit?> =
        object : DisposableObserver<CreditLimit?>() {
            override fun onNext(response: CreditLimit) {
                Utils.hideProgressDialog()
                if (response.isSuccess) {
                    val url = response.message
                    startActivityForResult(
                        Intent(
                            applicationContext,
                            DirectUdharActivity::class.java
                        ).putExtra("url", url), SK_CREDIT
                    )
                } else {
                    binding.btnDUdhaar.isEnabled = true
                    Utils.setToast(
                        applicationContext, response.message
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    private val postPaymentResponseObserver: DisposableObserver<Boolean?> =
        object : DisposableObserver<Boolean?>() {
            override fun onNext(boolean: Boolean) {
                Utils.hideProgressDialog()
                hideProgressDialog()
                if (boolean) {
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_GULLAK_BAL, false)
                    if (screen == 1) {
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else if (screen == 2) {
                        commonAPICall.getGullakBalance(
                            gullakBalanceObserver,
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.CUSTOMER_ID)
                        )
                    } else {
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        Utils.leftTransaction(this@AddPaymentActivity)
                    }
                } else {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_some_error_occured)
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                hideProgressDialog()
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_response)
                )
            }

            override fun onComplete() {}
        }

    private val gullakBalanceObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(jsonObject: JsonObject) {
                try {
                    if (jsonObject != null) {
                        val num = jsonObject["Amount"].asDouble
                        val df = DecimalFormat("#.##")
                        SharePrefs.getInstance(applicationContext)
                            .putString(SharePrefs.GULLAK_BALANCE, df.format(num))
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.setToast(applicationContext, "No Response")
            }

            override fun onComplete() {}
        }

    // sk check limit response
    private val creditLimitObserver: DisposableObserver<CreditLimit?> =
        object : DisposableObserver<CreditLimit?>() {
            override fun onNext(response: CreditLimit) {
                Utils.hideProgressDialog()
                if (response.isSuccess && response.dynamicData != null && response.dynamicData!!.amount > 0
                ) {
                    skCreditRes = response
                } else {
                    binding.btnDUdhaar.visibility = View.GONE
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    var getRazorpayOrderObserver: DisposableObserver<String> =
        object : DisposableObserver<String>() {
            override fun onNext(response: String) {
                try {
                    Utils.hideProgressDialog()
                    if (!TextUtils.isNullOrEmpty(response)) {
                        callRazorpay(response)
                    }
                } catch (e: java.lang.Exception) {
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

    private fun handleICICITransactionResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
              //  Utils.showProgressDialog(this@AddPaymentActivity)
            }
            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    val responseCode = it["responseCode"].asString
                    if (responseCode == "0000" || responseCode == "000") {
                        if (it.has("txnResponseCode")) {
                            val txnStatus = it["txnStatus"].asString   //REQ ,SUC,REJ
                            val txnResponseCode = it["txnResponseCode"].asString
                            if (txnResponseCode == "0000" || txnResponseCode == "000" && txnStatus == "SUC") {
                                val txnRespDescription = it["txnRespDescription"].asString
                                val merchantTxnNo = it["merchantTxnNo"].asString
                                val txnID = it["txnID"].asString
                                //Transaction success
                                val transactionModel = PostUPIPaymentResponse(
                                    merchantTxnNo,
                                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                                    txnID,
                                    iciciPayRequest,
                                    it.toString(),
                                    "icici",
                                    "Success"
                                )
                                commonAPICall.postPaymentResponse(
                                    postPaymentResponseObserver,
                                    transactionModel
                                )
                            } else {
                                iCICIPaymentFailed("icici")
                            }
                        } else {
                            iCICIPaymentFailed("icici")
                        }
                    } else {
                        iCICIPaymentFailed("icici")
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun showProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog!!.dismiss()
            customProgressDialog = null
        }
        customProgressDialog = Dialog(this, R.style.CustomDialog)
        val mView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null)
        customProgressDialog!!.setContentView(mView)
        customProgressDialog!!.setCancelable(false)
        customProgressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (customProgressDialog != null && customProgressDialog!!.isShowing && !isFinishing) {
            customProgressDialog!!.dismiss()
        }
    }
}