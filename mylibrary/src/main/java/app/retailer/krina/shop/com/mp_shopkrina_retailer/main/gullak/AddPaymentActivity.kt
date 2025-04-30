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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.payphi.customersdk.utils.HmacUtility
import com.payphi.customersdk.views.Application
import com.payphi.customersdk.views.PayPhiSdk
import com.payphi.customersdk.views.PaymentOptionsActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import io.reactivex.observers.DisposableObserver
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.Random
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_payment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = MyApplication.getInstance().dbHelper.getString(R.string.gullak_balance)

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
                                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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
                                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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
                    MyApplication.getInstance().dbHelper.getString(R.string.payment_cancel)
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
                        MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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
        if (binding.btnICICI.visibility == View.VISIBLE) {
            iCICIMerchantId =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_MERCHANT_ID)
            val application = Application()
            if (BuildConfig.DEBUG) {
                application.setEnv(application.QA)
            } else {
                application.setEnv(application.PROD)
            }
            application.setMerchantName("ShopKirana", this)
            application.setAppInfo(iCICIMerchantId,
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_APP_ID),
                this,
                object : Application.IAppInitializationListener {
                    override fun onFailure(errorCode: String?) {
                        runOnUiThread {
                            binding.btnICICI.visibility = View.GONE
                        }
                    }

                    override fun onSuccess(status: String?) {}
                })
        }
    }

    private fun addPaymentReq() {
        amount = binding.tiAmount.editText?.text.toString().trim()
        if (TextUtils.isNullOrEmpty(amount)) {
            binding.tiAmount.error =
                MyApplication.getInstance().dbHelper.getString(R.string.amount_required)
        } else if (amount.toDouble() < 1) {
            binding.tiAmount.error =
                MyApplication.getInstance().dbHelper.getString(R.string.amount_greater_than_zero)
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
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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

    private fun callICICIPay(orderId: String) {
        val rnd = Random()
       // orderId = (orderId1 + rnd.nextInt(900768000)).toString()
        val secureToken: String? = getSecureToken()
        val intent = Intent(applicationContext, PaymentOptionsActivity::class.java)
        intent.putExtra("SecureToken", secureToken)
        intent.putExtra("MerchantID", iCICIMerchantId)
        // intent.putExtra("aggregatorID", "J_34407")
        intent.putExtra("Amount", amount)
        intent.putExtra("MerchantTxnNo", orderId)
        intent.putExtra("invoiceNo", orderId)
        intent.putExtra("CurrencyCode", 356)
        intent.putExtra(
            "CustomerEmailID",
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_EMAIL)
        )
        intent.putExtra("allowDisablePaymentMode", "CARD")
        intent.putExtra(
            "CustomerID",
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
        intent.putExtra("addlParam1", "7304828261")
        intent.putExtra("addlParam2", "7304828262")
        iciciPayRequest = intent.extras.toString()

        println("Request> " + iciciPayRequest)

        PayPhiSdk.makePayment(applicationContext,
            intent,
            PayPhiSdk.DIALOG,
            object : PayPhiSdk.IAppPaymentResponseListenerEx {
                override fun onPaymentResponse(
                    resultCode: Int, data: Intent?, additionalInfo: Map<String?, String?>?
                ) {/*  println("data>>"+data.toString())
                      val bundle1: Bundle? = data!!.extras
                      if (bundle1 != null) {
                          for (key in bundle1.keySet()) {
                              val value = bundle1[key]
                              println("Data from main app key=" + key + "Data from main app value=" + value)
                          }
                      }*/

                    showProgressDialog()
                    if (resultCode == RESULT_OK) {
                        val bundle = data?.extras
                        if (bundle != null) {
                            val merchantTxnNo = bundle.getString("merchantTxnNo")
                            val responseCode = bundle.getString("responseCode")
                            val txnID = bundle.getString("txnID")
                            val respDescription = bundle.getString("respDescription")
                            println("DaTA>>>>>>>${bundle.toString()}")
                            println("txnID>>>>>>>$txnID")
                            println("respDescription>>>>>>>$respDescription")
                            Utils.setToast(
                                applicationContext,
                                respDescription+""
                            )
                            if (responseCode == "0000" || responseCode == "000") {
                                //Transaction success
                                val transactionModel = PostUPIPaymentResponse(
                                    merchantTxnNo!!,
                                    sharePrefs.getInt(SharePrefs.CUSTOMER_ID),
                                    merchantTxnNo,
                                    iciciPayRequest,
                                    bundle.toString(),
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
                        }
                    }/*else if (resultCode == RESULT_CANCELED) {
                        iCICIPaymentFailed("0")
                    }*/ else {
                        Utils.setToast(
                            applicationContext,
                            "Transaction failed"
                        )
                        callICICIPaymentResult()
                    }
                }

                override fun onPaymentResponse1(resultCode: Int, data: Intent?) {}
            })
    }

    private fun callICICIPaymentResult() {
        val value = iCICIMerchantId + orderId + "STATUS"
        val secureHash = generateHMAC(value)
        viewModel.getICICIPaymentCheck(
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_RESULT_URL),
            iCICIMerchantId,
            orderId.toString(),
            orderId.toString(),
            "STATUS",
            secureHash!!
        )
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

    private fun getSecureToken(): String? {
        val df = DecimalFormat()
        df.minimumFractionDigits = 2
        val f = amount.toFloat()
        df.format(f)
        amount = String.format("%.2f", f)
        val value = amount + 356 + iCICIMerchantId + orderId
        println("TokeString== $`value`")
        // 0d50a3f9aec3492cba25fef9b3c1a3c1
        return generateHMAC(value)
    }

    private fun generateHMAC(message: String): String? {
        val secretKey =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_SECRET_KEY)
        val sha256_HMAC: Mac
        val hashedBytes: ByteArray
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256")
            val secret_key = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
            sha256_HMAC.init(secret_key)
            hashedBytes = sha256_HMAC.doFinal(message.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return HmacUtility.bytesToHex(hashedBytes)
    }


    private fun initViews() {
        binding.tvAddPayment.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_payment_to_gullak)
        binding.btnHDFC.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_payment_to_gullak)
        binding.tvGullakTerms.text =
            MyApplication.getInstance().dbHelper.getString(R.string.gullak_money_cannot_be_transferred)
        binding.btnRazorpy.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_from_razorpay)

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
            Utils.setToast(applicationContext, getString(R.string.places_try_again))
        }
    }

    private fun callRazorpay(orderId: String) {
        val checkout = Checkout()
        checkout.setKeyID(
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.RAZORPAY_KEY_ID)
        )
        checkout.setImage(R.mipmap.ic_launcher)
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
                            callICICIPay(txnId.toString())
                        }
                    } else {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
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
                        MyApplication.getInstance().dbHelper.getString(R.string.text_some_error_occured)
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                hideProgressDialog()
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.no_response)
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