package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPaynowBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EPayWebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EpayLaterEncryptDecryptUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.HDFCActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ScaleUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CheckBookData
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentGatewayModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.UpdateOrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Aes256
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class PayNowActivity : AppCompatActivity(), View.OnClickListener, PaymentResultWithDataListener {
    private val TAG = this.javaClass.simpleName
    private val E_PAY_LATER_REQUEST = 1563
    private val HDFC_REQUEST = 999
    private val SK_CREDIT = 888
    private val SCALEUP = 777
    private val PAYMENT_RESPONSE = "extra_payment_response"
    //
    private lateinit var mBinding: ActivityPaynowBinding
    private lateinit var viewModel: PaymentViewModel
    private var utils: Utils? = null
    private var progressDialog: Dialog? = null
    private var grossAmount = 0.0
    private var totalAmount = 0.0
    private var ePayLimit = 0.0
    private var custName: String? = null
    private var custMobile: String? = null
    private var custEmail: String? = null
    private var custShippingAddress: String? = null
    private var skCode: String? = null
    private var paymentCheckedMsg: String? = null
    private var orderId = 0
    private var custId = 0
    private var otherPaymentMode = false
    private var canPostOrder = false
    private var ePayLaterLimit = true
    private var isChqbookLimit = true
    private var IsSuccess = true
    private var IsBackPaymentFalse = false
    private var holePaymentSucceedCheck = false
    private var hdfcTxtAmt = 0.0
    private var ePayamount = 0.0
    private var checkBookLimit = 0.0
    private var scaleUpLimit = 0.0
    private var gatewayOrderId = ""
    private var hdfcRequest = ""
    private var ePaylaterRequest = ""
    private var transactionId: String? = ""
    private var paymentThrough = ""
    private var cashAmount = 0.0
    private var gullakAmount = 0.0
    private var hdfcAmount = 0.0
    private var ePayAmount = 0.0
    private var checkBookAmount = 0.0
    private var skCreditAmt = 0.0
    private var scaleUpAmt = 0.0
    private var availableCODLimit = -1.0
    private var razorpayAmount = 0.0
    private var iCICIPayAmount = 0.0
    private var iCICIFinaPayAmount = ""

    private var ePaystatusCode = ""
    private var ePayMarketplaceOrderId = ""
    private var ePayReponseObj = ""
    private var PayNowOption: String? = ""
    private var epaylaterRetry = false
    private var orderAmountFlag = false
    private var incremental_no = 1
    private var checkBookminiAmount = 0
    private var calAmountCOD = 0.0
    private var calAmountOnline = 0.0
    private var orderList2: ConformOrderModel? = null
    private var prepaidOrderModel: PrepaidOrderModel? = null
    private var skCreditRes: CreditLimit? = null
    private var chqbookTransactionId = ""
    private var orderType = 0
    private var isUdharOrderOverDue = false
    private var isHdfcPayment = false
    private val FLUTTER_ENGINE_ID = "my_flutter_engine"
    private val CHANNEL = "com.ScaleUP"
    //private var flutterEngine: FlutterEngine? = null
   // private var methodChannel: MethodChannel? = null
    private var iCICIMerchantId = ""
    private var iciciPayRequest: String? = null
    private var updateCashStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPaynowBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            PaymentViewModelFactory(application, appRepository)
        )[PaymentViewModel::class.java]
        if (intent.extras != null) {
            orderList2 = intent.getSerializableExtra("PendingOrderList") as ConformOrderModel?
            PayNowOption = intent.getStringExtra("PayNowOption")
            orderType = intent.getIntExtra("orderType", 0)
            // set Pending Order data
            orderId = orderList2!!.orderId
            // init view
            initialization()
            // Shared Data
            sharedData()
            if (PayNowOption != null) {
                if (PayNowOption.equals("Online", ignoreCase = true)) {
                    mBinding.tvPayOption.visibility = View.GONE
                    mBinding.rlCash.visibility = View.GONE
                } else if (PayNowOption.equals("Both", ignoreCase = true)) {
                    mBinding.rlCash.visibility = View.VISIBLE
                    //Prepaid Order API
                    observe(viewModel.getPrepaidOrderData, ::handlePrepaidOrderResult)
                    viewModel.getPrepaidOrder(
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                        "Pay Now Screen"
                    )
                }
            }
            activePaymentMode()
        }
        grossAmount = orderList2!!.grossAmount
        totalAmount = orderList2!!.remainingAmount
        setPendingOrderValues()
        // back btn
        mBinding.toolbarPaymentPaynowOption.back.setOnClickListener { onBackPressed() }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // API calling
        callDirectAPI()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        if (IsBackPaymentFalse) {
            IsSuccess = false
            updatePaymentAPICall("", false)
        } else {
            finish()
            Utils.fadeTransaction(this)
            super.onBackPressed()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.callBtn -> {
                val phone =
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
                if (!TextUtils.isNullOrEmpty(phone)) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                    startActivity(intent)
                }
            }

            R.id.btnCallSu -> {
                val phone = RetailerSDKApp.getInstance().dbHelper.getString(R.string.scaleUpContact)
                if (!TextUtils.isNullOrEmpty(phone)) {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
                }
            }

            R.id.placeBtn -> {
                mBinding.placeBtn.isClickable = false
                if (PayNowOption.equals("Online", ignoreCase = true) && !SharePrefs.getInstance(
                        applicationContext
                    ).getBoolean(SharePrefs.CUST_ACTIVE)
                ) {
                    Utils.setLongToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.not_eligible_to_pay_online)
                    )
                    mBinding.placeBtn.isClickable = true
                } else {
                    var isChqbookBackLimit = false
                    val gullakBal = SharePrefs.getInstance(applicationContext)
                        .getDouble(SharePrefs.GULLAK_BALANCE)
                    ePayLaterLimit = if (ePayAmount > 0) {
                        ePayLimit == 0.0 || !(ePayAmount <= ePayLimit)
                    } else {
                        false
                    }
                    if (checkBookAmount > 0) {
                        isChqbookLimit =
                            checkBookLimit == 0.0 || !(checkBookAmount <= checkBookLimit)
                        isChqbookBackLimit =
                            checkBookminiAmount == 0 || !(checkBookAmount >= checkBookminiAmount)
                    } else {
                        isChqbookLimit = false
                    }
                    if (gullakAmount > 0 && gullakBal < gullakAmount) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.gullak_in_sufficient_balance)
                        )
                        mBinding.placeBtn.isClickable = true
                    } else if (ePayLaterLimit) {
                        Utils.setToast(
                            baseContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.epaylater_message) + " " + ePayLimit
                        )
                        mBinding.placeBtn.isClickable = true
                    } else if (isChqbookLimit || isChqbookBackLimit) {
                        if (isChqbookLimit) {
                            Utils.setToast(
                                applicationContext,
                                RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkbook_message) + " " + checkBookLimit
                            )
                        } else {
                            Utils.setToast(
                                applicationContext,
                                RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkBok_bill_value) + checkBookminiAmount
                            )
                        }
                        mBinding.placeBtn.isClickable = true
                    } else if (skCreditAmt > 0 && skCreditRes!!.dynamicData!!.amount < skCreditAmt
                    ) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                        )
                        mBinding.placeBtn.isClickable = true
                    } else if (scaleUpAmt > 0 && scaleUpLimit < scaleUpAmt) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                        )
                    } else if (availableCODLimit != -1.0 && cashAmount > availableCODLimit) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.cod_limit_not_available)
                        )
                        mBinding.placeBtn.isClickable = true
                    } else {
                        if (orderDividePercentCheck()) {
                            if (canPostOrder) {
                                otherPaymentMode = true
                                if (cashAmount > 0 && finalAmount == cashAmount) {
                                    IsSuccess = true
                                    insertCashPaymentStatusAPICall(
                                        "Success",
                                        "Cash",
                                        cashAmount,
                                        "Offline"
                                    )
                                    paymentThrough = "Cash"
                                } else if (gullakAmount > 0 && gullakBal >= gullakAmount) {
                                    holePaymentSucceedCheck = true
                                    paymentThrough = "Gullak"
                                    insertPaymentStatusAPICall(
                                        "Success",
                                        "200",
                                        "Gullak",
                                        "" + orderId,
                                        gullakAmount,
                                        "Gullak",
                                        "Gullak",
                                        "Gullak"
                                    )
                                } else if (hdfcAmount > 0) {
                                    getHDFCRSAKey(false)
                                    paymentThrough = "Hdfc"
                                } else if (checkBookAmount > 0) {
                                   // checkBookPayment(checkBookAmount)
                                    paymentThrough = "CheckBook"
                                } else if (ePayAmount > 0) {
                                    ePayLater(ePayAmount)
                                    paymentThrough = "ePayLater"
                                } else if (skCreditAmt > 0) {
                                    paymentThrough = "DirectUdhar"
                                    if (skCreditRes!!.dynamicData!!.amount > skCreditAmt) {
                                        creditPayment()
                                    } else {
                                        Utils.setToast(
                                            applicationContext,
                                            RetailerSDKApp.getInstance().dbHelper.getString(
                                                R.string.no_sufficient_limit
                                            )
                                        )
                                    }
                                } else if (scaleUpAmt > 0) {
                                    paymentThrough = "scaleUp"
                                    if (scaleUpLimit > scaleUpAmt) {
                                        scaleUpPayment()
                                    } else {
                                        Utils.setToast(
                                            applicationContext,
                                            RetailerSDKApp.getInstance().dbHelper.getString(
                                                R.string.no_sufficient_limit
                                            )
                                        )
                                    }
                                } else if (razorpayAmount > 0) {
                                    getRazorpayOrderId()
                                    paymentThrough = "razorpay"
                                } else if (iCICIPayAmount > 0) {
                                    paymentThrough = "icici"
                                    //callICICIPay()
                                }
                            } else {
                                mBinding.placeBtn.isClickable = true
                                Utils.setToast(
                                    baseContext,
                                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_amount_does_not_match)
                                )
                            }
                        }
                    }
                }
            }

            R.id.ivInfo -> showCodLimitDialog()
        }
    }

    private fun callDirectAPI() {
        observe(viewModel.getUdharCreditLimitData, ::handleUdharCreditLimitResult)
        observe(viewModel.ePayLaterCustomerLimitData, ::handleEPaylaterCreditLimitResult)
        observe(viewModel.checkbookLimitData, ::handleCheckBookLimitResult)
        observe(viewModel.getCODLimitData, ::handleCheckCODLimitResult)
        observe(viewModel.getScaleUpLimitData, ::handleScaleUpLimitResult)
        observe(viewModel.getEPaylaterConfirmPaymentData, ::handleEpalaterPaymentResult)
        observe(viewModel.getInsertOnlineTransactionData, ::handleInsertTransactionResult)
        observe(viewModel.getUpdateOnlineTransactionData, ::handleUpdateTransactionResult)
        observe(viewModel.getCreditPaymentData, ::handleGeneratePaymentResult)
        observe(viewModel.getScaleUpPaymentInitiateData, ::handleScaleUpPaymentInitiateResult)

        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            Utils.showProgressDialog(this)
            viewModel.isGullakOptionEnable(custId, orderId)
            viewModel.isGullakOptionEnableData.observe(this) {
                if (it == true) {
                    mBinding.rlGullak.visibility = View.VISIBLE
                } else {
                    mBinding.rlGullak.visibility = View.GONE
                }
            }
            if (mBinding.rlSkCredit.visibility == View.VISIBLE) {
                viewModel.getUdharCreditLimit(custId)
            }
            if (mBinding.rlEpay.visibility == View.VISIBLE) {
                viewModel.ePayLaterCustomerLimit(
                    skCode,
                    SharePrefs.getInstance(this).getString(SharePrefs.BEARER_TOKEN)
                )
            }

            if (mBinding.rlCheckBook.visibility == View.VISIBLE) {
                viewModel.checkBookCustomerLimit(
                    SharePrefs.getInstance(this).getString(SharePrefs.CHECKBOOK_BASE_URL),
                    SharePrefs.getInstance(this).getString(SharePrefs.CHECKBOOK_API_KEY),
                    CheckBookCreditLimitRes(
                        skCode!!
                    )
                )
            }

            viewModel.getCustomerCODLimit(custId)

            if (mBinding.rlScaleUp.visibility == View.VISIBLE) {
                viewModel.getScaleUpLimit(custId)
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("onResult-ActivityMain", requestCode.toString())
        if (requestCode == HDFC_REQUEST) {
            if (data != null && resultCode == RESULT_OK) {
                val `object`: JSONObject
                try {
                    `object` = JSONObject(data.getStringExtra(AvenuesParams.RESPONSE))
                    hdfcTxtAmt = `object`.getString("amount").toDouble()
                    Utils.setToast(
                        applicationContext,
                        "Transaction " + `object`.getString(AvenuesParams.STATUS)
                    )
                    println("HDFC RESPONSE::$hdfcTxtAmt")
                    if (`object`.getString(AvenuesParams.STATUS)
                            .equals("Success", ignoreCase = true)
                    ) {
                        holePaymentSucceedCheck = true
                        insertPaymentStatusAPICall(
                            "Success",
                            `object`.getString("response_code"),
                            "hdfc",
                            `object`.getString(AvenuesParams.TRACKING_ID),
                            hdfcTxtAmt,
                            hdfcRequest,
                            `object`.toString(),
                            `object`.getString("payment_mode")
                        )
                        // update analytics purchase
                        RetailerSDKApp.getInstance().updateAnalyticPurchase(
                            `object`.getString(AvenuesParams.TRACKING_ID),
                            hdfcTxtAmt,
                            0.0,
                            "payNow",
                            "" + orderId,
                            0,
                            "hdfc",
                            ArrayList()
                        )
                    } else {
                        mBinding.placeBtn.isClickable = true
                        holePaymentSucceedCheck = false
                        insertPaymentStatusAPICall(
                            "Failed",
                            `object`.getString("response_code"),
                            "hdfc",
                            `object`.getString(AvenuesParams.TRACKING_ID),
                            hdfcTxtAmt,
                            hdfcRequest,
                            `object`.toString(),
                            `object`.getString("payment_mode")
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                mBinding.placeBtn.isClickable = true
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "hdfc",
                    "",
                    hdfcAmount,
                    hdfcRequest,
                    "",
                    ""
                )
            }
        } else if (requestCode == 9 && resultCode == RESULT_OK) {
            mBinding.tvGullakBal.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.balance) + " " + SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.GULLAK_BALANCE)
            mBinding.placeBtn.callOnClick()
        } else if (requestCode == E_PAY_LATER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    val jsonObject = JSONObject(data.getStringExtra(PAYMENT_RESPONSE))
                    val Data = jsonObject.getString("Data")
                    val decrypt = Aes256.decrypt(
                        Data, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                            Date()
                        ) + "1201"
                    )
                    val `object` = JSONObject(convertStandardJSONString(decrypt))
                    println(TAG + `object`)
                    val encdata = `object`.getJSONObject("encdata")
                    // encdata.put("PaymentFrom", getString(R.string.epaylater));
                    // encdata.put("OrderId", orderId);
                    //  encdata.put("Skcode", skCode);
                    Utils.setToast(
                        applicationContext, """
     ${encdata.getString("status")}
     ${encdata.getString("statusDesc")}
     """.trimIndent()
                    )
                    var amount = encdata.getString("amount").toDouble()
                    amount = amount / 100
                    val jsonParser = JsonParser()
                    val reponseObj = jsonParser.parse(encdata.toString()) as JsonObject
                    if (encdata.getString("status").equals("Success", ignoreCase = true)) {
                        holePaymentSucceedCheck = true
                        val gson = Gson()
                        // paymentGatewayList.add(gson.fromJson(encdata.toString(), PaymentGatewayModel.class));
                        val eplOrderId = encdata.getString("eplOrderId")
                        paymentCheckedMsg = encdata.getString("statusDesc")
                        ePaystatusCode = encdata.getString("statusCode")
                        gatewayOrderId = encdata.getString("marketplaceOrderId")
                        ePayMarketplaceOrderId = eplOrderId
                        ePayReponseObj = reponseObj.toString()
                        ePayamount = amount

                        viewModel.ePayLaterConfirmOrder(
                            SharePrefs.getInstance(this).getString(SharePrefs.BEARER_TOKEN),
                            eplOrderId,
                            encdata.getString("marketplaceOrderId")
                        )

                        // update analytics purchase
                        RetailerSDKApp.getInstance().updateAnalyticPurchase(
                            gatewayOrderId,
                            amount,
                            0.0,
                            "payNow",
                            "" + orderId,
                            0,
                            "ePayLater",
                            ArrayList()
                        )
                    } else {
                        insertPaymentStatusAPICall(
                            "Failed",
                            encdata.getString("statusCode"),
                            "ePaylater",
                            encdata.getString("marketplaceOrderId"),
                            amount,
                            ePaylaterRequest,
                            reponseObj.toString(),
                            ""
                        )
                        holePaymentSucceedCheck = false
                        mBinding.placeBtn.isClickable = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == SK_CREDIT) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    val accountId = data.getStringExtra("accountId")
                    val orderId = data.getStringExtra("orderId")
                    val status = data.getStringExtra("status")
                    val amount = data.getStringExtra("amount")!!.toDouble()
                    val transactionRefNo = data.getStringExtra("transactionRefNo")
                    holePaymentSucceedCheck = true
                    insertPaymentStatusAPICall(
                        "Success",
                        status,
                        "DirectUdhar",
                        transactionRefNo,
                        amount,
                        "",
                        data.extras.toString(),
                        "DirectUdhar"
                    )
                    // update analytics purchase
                    RetailerSDKApp.getInstance().updateAnalyticPurchase(
                        transactionRefNo,
                        amount,
                        0.0,
                        "payNow",
                        "" + orderId,
                        0,
                        "hdfc",
                        ArrayList()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                mBinding.placeBtn.isClickable = true
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_cancel)
                )
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "DirectUdhar",
                    "",
                    skCreditAmt,
                    "DirectUdhar",
                    "",
                    "DirectUdhar"
                )
            }
        } else if (requestCode == SCALEUP) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    val accountId = data.getStringExtra("accountId")
                    val orderId = data.getStringExtra("orderId")
                    val status = data.getStringExtra("status")
                    val amount = data.getStringExtra("amount")!!.toDouble()
                    val transactionRefNo = data.getStringExtra("transactionRefNo")
                    transactionId = transactionRefNo
                    holePaymentSucceedCheck = true
                    insertPaymentStatusAPICall(
                        "Success",
                        status,
                        "ScaleUp",
                        transactionRefNo,
                        amount,
                        "",
                        data.extras.toString(),
                        "ScaleUp"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                mBinding.placeBtn.isClickable = true
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_cancel)
                )
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "ScaleUp",
                    "",
                    scaleUpAmt,
                    "ScaleUp",
                    "",
                    "ScaleUp"
                )
            }
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


    private fun initialization() {
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        isUdharOrderOverDue = SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_UDHAAR_OVERDUE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        utils = Utils(this)
        mBinding.toolbarPaymentPaynowOption.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(
                R.string.payment_option
            )
        mBinding.tvTotalAmntHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_amnt)
        mBinding.tvPayOption.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.payon)
        mBinding.tvOnlineH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.online_h)
        mBinding.tvCreditCardChargesH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.credit_card_charges_1_gst)
        mBinding.tvPaybleAmountHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Amount_Payble)
        mBinding.tvPayonDelivery.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.payon)
        mBinding.tvCardCheckCash.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.cash_card_cheque)
        mBinding.tvPaySuc.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.etAmountCash.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.tvTruPayHead.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.trupay)
        mBinding.tvInstantOnline.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.instant_online_payment)
        mBinding.tvPaymentStatusHdfc.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.etAmountHdfc.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.name.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.epaylater)
        mBinding.tvDescription.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.interest_free_credit_limit)
        mBinding.tvPaymentStatusEpay.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.callBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_call)
        mBinding.etAmountEpay.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.placeBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment)
        mBinding.tvSkCreditH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.direct_udhar)
        mBinding.tvGullakBal.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.balance) + " " + SharePrefs.getInstance(
                applicationContext
            ).getString(SharePrefs.GULLAK_BALANCE)
        mBinding.tvScaleUp.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.scale_up_pay)
        mBinding.tvRazorpay.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.pay_via_razorpay)
        mBinding.tvInstantRazorpay.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_instant_payment_razorpay)
        mBinding.radioGullak.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioGullak
            )
        )
        mBinding.cbCash.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.cbCash
            )
        )
        mBinding.radioHdfc.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioHdfc
            )
        )
        mBinding.radioEpay.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioEpay
            )
        )
        mBinding.radioCheckbook.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioCheckbook
            )
        )
        mBinding.radioSkC.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioSkC
            )
        )
        mBinding.radioScaleUp.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioScaleUp
            )
        )
        mBinding.radioRazorpay.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioRazorpay
            )
        )
        mBinding.radioICICIPayment.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioICICIPayment
            )
        )
        mBinding.placeBtn.setOnClickListener(this)
        mBinding.callBtn.setOnClickListener(this)
        mBinding.btnCallSu.setOnClickListener(this)
        mBinding.ivInfo.setOnClickListener(this)
        if (orderList2!!.isPayLater) {
            mBinding.cbCash.isEnabled = false
            mBinding.radioGullak.isEnabled = false
            mBinding.radioEpay.isEnabled = false
            mBinding.radioSkC.isEnabled = false
            mBinding.radioScaleUp.isEnabled = false
            mBinding.radioCheckbook.isEnabled = false
        }
//        flutterEngine = FlutterEngine(this)
//        flutterEngine!!.dartExecutor.executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        )
//        methodChannel = MethodChannel(
//            flutterEngine!!.dartExecutor.binaryMessenger,
//            CHANNEL
//        )
//        FlutterEngineCache
//            .getInstance()
//            .put(FLUTTER_ENGINE_ID, flutterEngine)
//        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL)
//            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
//                if (call.method == "returnToPayment") {
//                    val transactionReqNo = call.argument<String>("transactionReqNo")
//                    val amount = call.argument<Any>("amount")
//                    val mobileNo = call.argument<String>("mobileNo")
//                    val loanAccountId = call.argument<Int>("loanAccountId")
//                    val creditDay = call.argument<Int>("creditDay")
//                    holePaymentSucceedCheck = true
//                    insertPaymentStatusAPICall(
//                        "Success",
//                        "200",
//                        "ScaleUp",
//                        transactionReqNo,
//                        amount.toString().toDouble(),
//                        "",
//                        "",
//                        "ScaleUp"
//                    )
//                } else {
//                    result.notImplemented()
//                }
//            }
    }

    private fun activePaymentMode() {
        val customerActive =
            SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.CUST_ACTIVE)
        val isCheckBookCustomer =
            SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_CHECKBOOK_SHOW)
        isHdfcPayment =
            SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_HDFC_PAYMENT)
        if (isCheckBookCustomer) {
            mBinding.rlCheckBook.visibility = View.VISIBLE
        } else {
            mBinding.rlCheckBook.visibility = View.GONE
        }
        if (isHdfcPayment) {
            mBinding.rlHdfc.visibility = View.VISIBLE
            if (SharePrefs.getInstance(applicationContext).getString(SharePrefs.IS_PAYMENT_GATWAY)
                    .equals("HDFC", ignoreCase = true)
            ) {
                mBinding.rlHdfc.visibility = View.VISIBLE
                mBinding.radioHdfc.isChecked = true
            } else {
                mBinding.rlHdfc.visibility = View.GONE
            }
        } else {
            mBinding.rlHdfc.visibility = View.GONE
        }
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_EPAY_LATER_SHOW)) {
            mBinding.rlEpay.visibility = View.VISIBLE
        } else {
            mBinding.rlEpay.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_DIRECT_UDHAR)) {
            mBinding.rlSkCredit.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_SCALEUP)) {
            mBinding.rlScaleUp.visibility = View.GONE
        }
        if (isUdharOrderOverDue) {
            mBinding.tvPayOption.visibility = View.GONE
            mBinding.rlCash.visibility = View.GONE
            mBinding.rlSkCredit.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_RAZORPAY_PAYMENT)) {
            mBinding.rlRazorpay.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_ICICI_PAYMENT)) {
            mBinding.rlICICIPay.visibility = View.GONE
        }
        if (!customerActive) {
            mBinding.rlHdfc.visibility = View.GONE
            mBinding.rlEpay.visibility = View.GONE
            mBinding.rlCheckBook.visibility = View.GONE
            mBinding.rlSkCredit.visibility = View.GONE
            mBinding.rlRazorpay.visibility = View.GONE
            mBinding.rlICICIPay.visibility = View.GONE
        }
//        if (mBinding.rlICICIPay.visibility == View.VISIBLE) {
//            iCICIMerchantId =
//                SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_MERCHANT_ID)
//            val application = Application()
//            if (BuildConfig.DEBUG) {
//                application.setEnv(application.QA)
//            } else {
//                application.setEnv(application.PROD)
//            }
//            application.setMerchantName("ShopKirana", this)
//            application.setAppInfo(
//                iCICIMerchantId,
//                SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_APP_ID),
//                this,
//                object : Application.IAppInitializationListener {
//                    override fun onFailure(errorCode: String?) {}
//                    override fun onSuccess(status: String?) {}
//                })
//        }
    }

    private fun getRazorpayOrderId() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            showProgressDialog(this)
            viewModel.fetchRazorpayOrderId(orderId.toString(), razorpayAmount)
            viewModel.getRazorpayOrderIdData.observe(this) {
                hideProgressDialog()
                if (!TextUtils.isNullOrEmpty(it)) {
                    callRazorpay(it)
                }
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun setPendingOrderValues() {
        var itemqty = 0
        orderId = orderList2!!.orderId
        mBinding.tvTotalPrice.text = DecimalFormat("##.##").format(grossAmount)
        mBinding.tvPayableamnt.text = "₹" + DecimalFormat("##.##").format(totalAmount)
        val totalAMountPay = DecimalFormat("##.##").format(totalAmount)
        mBinding.tvTotalAmountPay.text = "₹$totalAMountPay"
        if (orderList2!!.itemDetails != null && orderList2!!.itemDetails!!.size > 0) {
            for (i in orderList2!!.itemDetails!!.indices) {
                itemqty += orderList2!!.itemDetails!![i].qty
            }
        } else {
            mBinding.tvItemCount.visibility = View.INVISIBLE
        }
        val itemQty = DecimalFormat("##.##").format(itemqty.toLong())
        mBinding.tvItemCount.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_item) + " " + itemQty
        if (orderList2!!.payNowOption != null && orderList2!!.payNowOption.equals(
                "Both",
                ignoreCase = true
            )
        ) {
            cashAmount = finalAmount
            if (availableCODLimit != -1.0 && cashAmount <= availableCODLimit) {
                cashAmount = finalAmount
                mBinding.cbCash.isChecked = true
                mBinding.etAmountCash.text = "" + cashAmount
            } else {
                if (isHdfcPayment) {
                    hdfcAmount = finalAmount
                    mBinding.radioHdfc.isChecked = true
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountHdfc.text = "" + hdfcAmount
                    cashAmount = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                } else {
                    razorpayAmount = finalAmount
                    mBinding.radioRazorpay.isChecked = true
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountRazorpay.text = "" + razorpayAmount
                    cashAmount = 0.0
                    hdfcAmount = 0.0
                    iCICIPayAmount = 0.0
                }
            }
        } else if (orderList2!!.payNowOption != null && orderList2!!.payNowOption.equals(
                "Online",
                ignoreCase = true
            )
        ) {
            if (isHdfcPayment) {
                hdfcAmount = finalAmount
                mBinding.radioHdfc.isChecked = true
                mBinding.etAmountCash.text = "0"
                mBinding.etAmountRazorpay.text = "0"
                mBinding.etAmountICICI.text = "0"
                mBinding.etAmountHdfc.text = "" + hdfcAmount
                cashAmount = 0.0
                razorpayAmount = 0.0
                iCICIPayAmount = 0.0
            } else {
                razorpayAmount = finalAmount
                mBinding.radioRazorpay.isChecked = true
                mBinding.etAmountCash.text = "0"
                mBinding.etAmountHdfc.text = "0"
                mBinding.etAmountICICI.text = "0"
                mBinding.etAmountRazorpay.text = "" + razorpayAmount
                cashAmount = 0.0
                hdfcAmount = 0.0
                iCICIPayAmount = 0.0
            }
        } else {
            onBackPressed()
        }
    }

    private fun sharedData() {
            custName =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
            custMobile =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
            custEmail =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_EMAIL)
            custShippingAddress =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.SHIPPING_ADDRESS)
            skCode = SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)

            checkBookminiAmount =
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.IS_ChQBOOKMINI_AMT)

        }

    private fun goToHome(orderStatus: String) {
        if (orderId != 0 && orderStatus.equals("success", ignoreCase = true)) {
            RetailerSDKApp.getInstance().clearCartData()
            SharePrefs.getInstance(applicationContext).putBoolean(SharePrefs.IS_GULLAK_BAL, false)
            SharePrefs.getInstance(applicationContext).putString(SharePrefs.AVAIL_DIAL, "0")
            orderConformationPopup()
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_error_msg)
            )
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            Utils.rightTransaction(this)
        }
    }

    fun onButtonClick() {
        var amount: Double
        var ePayLaterUsed = false
        var isCheckBook = false
        canPostOrder = false
        amount = if (PayNowOption.equals("Online", ignoreCase = true)) {
            gullakAmount + ePayAmount + hdfcAmount + checkBookAmount + skCreditAmt + scaleUpAmt + razorpayAmount + iCICIPayAmount
        } else {
            cashAmount + gullakAmount + hdfcAmount + ePayAmount + checkBookAmount + skCreditAmt + scaleUpAmt + razorpayAmount + iCICIPayAmount
        }
        if (ePayAmount > 0) {
            ePayLaterUsed = true
        }
        if (checkBookAmount > 0) {
            isCheckBook = true
        }
        amount = DecimalFormat("##.##").format(amount).toDouble()
        Logger.logD(
            TAG,
            finalAmount.toString() + " amount= " + amount + " cash= " + cashAmount + " gullak= " + gullakAmount + " ePay= " + ePayAmount + " hdfc= " + hdfcAmount + " checkBook= " + checkBookAmount + " skC= " + skCreditAmt + " su= " + scaleUpAmt + " razorpayAmount= " + razorpayAmount + " ICICIAmount= " + iCICIPayAmount
        )
        if (amount == finalAmount) {
            if (ePayLaterUsed) {
                if (ePayLimit != 0.0 && ePayAmount <= ePayLimit) {
                    mBinding.tvRemainingAmount.text = ""
                    canPostOrder = true
                    ePayLaterLimit = false
                    isChqbookLimit = false
                } else {
                    ePayLaterLimit = true
                    isChqbookLimit = true
                    canPostOrder = true
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.epaylater_message) + ePayLimit
                    )
                }
            } else if (isCheckBook) {
                if (checkBookLimit != 0.0 && checkBookAmount <= checkBookLimit) {
                    mBinding.tvRemainingAmount.text = ""
                    canPostOrder = true
                    isChqbookLimit = false
                } else {
                    isChqbookLimit = true
                    canPostOrder = true
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkbook_message) + " " + checkBookLimit
                    )
                }
            } else {
                mBinding.tvRemainingAmount.text = ""
                canPostOrder = true
            }
        } else {
            val d = finalAmount - amount
            mBinding.tvRemainingAmount.text =
                """
                Remaining Amt.
                ${DecimalFormat("##.##").format(d)}
                """.trimIndent()
        }
    }

    private val finalAmount: Double
        get() {
            val totalAmount = orderList2!!.remainingAmount
            return DecimalFormat("##.##").format(totalAmount).toDouble()
        }

    // Check the internet connection.
    private fun detectNetwork(): String {
        var WIFI = false
        val CM = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = CM.allNetworkInfo
        for (netInfo in networkInfo) {
            if (netInfo.typeName.equals("WIFI", ignoreCase = true)) if (netInfo.isConnected) WIFI =
                true
        }
        return if (WIFI) {
            deviceIpWiFiData
        } else {
            deviceIpMobileData
        }
    }

    private val deviceIpMobileData: String
        get() {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            val sAddr = addr.hostAddress
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (isIPv4) return sAddr
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    private val deviceIpWiFiData: String
        get() {
            val wm =
                applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        }

    fun convertStandardJSONString(data_json: String): String {
        var data_json = data_json
        data_json = data_json.replace("\\", "")
        data_json = data_json.replace("u003Cbody>", "")
        data_json = data_json.replace("u003C/body>", "")
        data_json = data_json.replace("\"{", "{")
        data_json = data_json.replace("}\",", "},")
        data_json = data_json.replace("}\"", "}")
        Log.i("data_json", data_json)
        return data_json
    }

    private fun PrepaidOrder(OrderAmountGross: Double) {
        val cashMsg = ""
        var onlineMsg = ""
        var calPrepaidAmount = 0.0
        if (OrderAmountGross >= prepaidOrderModel!!.orderAmount && prepaidOrderModel!!.orderAmount != 0.0) {
            if (prepaidOrderModel!!.cashPercentage != 0.0) {
                calAmountCOD = OrderAmountGross * prepaidOrderModel!!.cashPercentage / 100
                orderAmountFlag = true
                // mBinding.prepaidOrderText.setVisibility(View.VISIBLE);
            }
            if (prepaidOrderModel!!.onlinePercentage != 0.0) {
                calAmountOnline = OrderAmountGross * prepaidOrderModel!!.onlinePercentage / 100
                val codFraction =
                    calAmountCOD.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1].toInt()
                val onlineFraction =
                    calAmountOnline.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1].toInt()
                if (codFraction == 5 && onlineFraction == 5) {
                    onlineMsg =
                        "* Minimum Prepaid amount = <font color=#FFFF4500>&#8377; " + DecimalFormat(
                            "##.##"
                        ).format(
                            Math.round(calAmountOnline - 1)
                        )
                    calPrepaidAmount = calAmountOnline - 1
                } else {
                    onlineMsg =
                        "* Minimum Prepaid amount = <font color=#FFFF4500>&#8377; " + DecimalFormat(
                            "##.##"
                        ).format(
                            Math.round(calAmountOnline)
                        )
                    calPrepaidAmount = calAmountOnline
                }
                orderAmountFlag = true
                mBinding.prepaidOrderText.visibility = View.VISIBLE
                showPrepaidOrderPopup(Math.round(calPrepaidAmount))
            }
            val prepaidOrderMsg = onlineMsg
            mBinding.prepaidOrderText.text = Html.fromHtml(prepaidOrderMsg)
        } else {
            orderAmountFlag = false
            mBinding.prepaidOrderText.visibility = View.GONE
        }
    }

    private fun orderDividePercentCheck(): Boolean {
        var precentageFlag = true
        var cashMsg = ""
        var onlineMsg = ""
        val msg = "Only Online Payment"
        if (orderAmountFlag) {
            calAmountCOD = finalAmount * prepaidOrderModel!!.cashPercentage / 100
            if (Math.round(calAmountCOD) >= cashAmount) {
                precentageFlag = true
            } else {
                calAmountOnline = finalAmount * prepaidOrderModel!!.onlinePercentage / 100
                val codFraction = calAmountCOD.toString().split("\\.".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].toInt()
                val onlineFraction = calAmountOnline.toString().split("\\.".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].toInt()
                onlineMsg = if (codFraction == 5 && onlineFraction == 5) {
                    "Minimum Prepaid Amount" + " = ₹" + Math.round(calAmountOnline - 1)
                } else {
                    "Minimum Prepaid Amount" + " = ₹" + Math.round(calAmountOnline)
                }
                cashMsg = "Please enter $onlineMsg"
                precentageFlag = false
            }
        } else {
            precentageFlag = true
        }
        if (!precentageFlag) {
            mBinding.placeBtn.isClickable = true
            Utils.setToast(
                applicationContext, cashMsg
            )
        }
        return precentageFlag
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
            options.put("amount", razorpayAmount) //pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put(
                "email",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_EMAIL)
            )
            prefill.put("contact", custMobile)
            options.put("prefill", prefill)
            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e)
        }
    }

    // HDFC payment request intent
    private fun callHDFC(orderId: String, rsaKey: String, amount: String) {
        gatewayOrderId = orderId
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

    // For payment using ePayLater
    private fun ePayLater(amount: Double) {
        val payList = orderList2!!.orderPayments
        if (payList != null && payList.size > 0) for (details in payList) {
            if (details.paymentFrom != null && details.paymentFrom.equals(
                    "epaylater",
                    ignoreCase = true
                )
            ) {
                epaylaterRetry = true
                break
            }
        }
        var marketPlaceOrderId = ""
        if (!epaylaterRetry) {
            epaylaterRetry = true
            marketPlaceOrderId = "$orderId($skCode)"
        } else {
            incremental_no = incremental_no + 1
            marketPlaceOrderId =
                orderId.toString() + "_" + incremental_no + "_" + "(" + skCode + ")"
        }
        var checksum: String? = ""
        var encdata: String? = ""
        val city = SharePrefs.getInstance(applicationContext).getString(SharePrefs.CITY_NAME)
        val date: String
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        date = try {
            dateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
            "2019-05-27T11:06:46Z"
        }
        val `object` = JSONObject()
        try {
            `object`.put("redirectType", "WEBPAGE")
            `object`.put("marketplaceOrderId", marketPlaceOrderId)
            `object`.put(
                "mCode",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.M_CODE)
            )
            `object`.put("callbackUrl", Constant.CALLBACK_URL)
            `object`.put("amount", amount * 100)
            `object`.put("currencyCode", "INR")
            `object`.put("date", date) // 2018-03-27T11:06:46Z
            `object`.put(
                "category",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CATEGORY)
            )
            val customerObject = JSONObject()
            customerObject.put("firstName", custName)
            customerObject.put("emailAddress", if (custEmail == "") "user@gmail.com" else custEmail)
            customerObject.put("telephoneNumber", custMobile)
            val deviceObject = JSONObject()
            deviceObject.put("deviceType", "MOBILE")
            deviceObject.put(
                "deviceClient",
                "Mozilla/5.0 (Linux; Android 6.0; XT1706 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36"
            )
            deviceObject.put("deviceNumber", detectNetwork())
            val merchantObject = JSONObject()
            merchantObject.put("marketplaceMerchantId", "1")
            merchantObject.put("name", "ShopKirana")
            merchantObject.put(
                "telephoneNumber",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
            )
            val addressObject = JSONObject()
            addressObject.put("line1", custShippingAddress)
            addressObject.put("city", city)
            addressObject.put("postcode", "452001")
            merchantObject.put("address", addressObject)
            val array = JSONArray()
            for (info in orderList2!!.itemDetails!!) {
                if (info.qty > 0) {
                    val jsonObject = JSONObject()
                    jsonObject.put("code", info.itemId)
                    jsonObject.put("name", info.itemName)
                    jsonObject.put("quantity", info.qty)
                    jsonObject.put("unitPrice", info.unitPrice)
                    jsonObject.put("tax", "0")
                    array.put(jsonObject)
                }
            }
            val marketplaceObject = JSONObject()
            marketplaceObject.put("marketplaceCustomerId", skCode)
            marketplaceObject.put("itemList", array)
            //          marketplaceObject.put("memberSince", "2014-04-27");
            `object`.put("customer", customerObject)
            `object`.put("device", deviceObject)
            `object`.put("merchant", merchantObject)
            `object`.put("marketplaceSpecificSection", marketplaceObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(`object`.toString()) as JsonObject
        ePaylaterRequest = gsonObject.toString()
        Logger.logD(TAG, "ePaylaterRequestSend::--$ePaylaterRequest")
        try {
            checksum = EpayLaterEncryptDecryptUtil.createChecksum(`object`.toString())
            encdata = EpayLaterEncryptDecryptUtil.encryptAES256AndBase64(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.ENCODED_KEY),
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.IV),
                `object`.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intent = Intent(applicationContext, EPayWebViewActivity::class.java)
        intent.putExtra(
            "extra_mcode",
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.M_CODE)
        )
        intent.putExtra("extra_encdata", encdata)
        intent.putExtra("extra_checksum", checksum)
        intent.putExtra(
            "extra_payment_url",
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.E_PAY_LATER_URL)
        )
        startActivityForResult(intent, E_PAY_LATER_REQUEST)
    }

    private fun creditPayment() {
        if (skCreditRes != null) {
            viewModel.creditPayment(
                CreditPayment(
                    SharePrefs.getInstance(
                        applicationContext
                    ).getInt(SharePrefs.COMPANY_ID),
                    custId,
                    orderId.toLong(),
                    skCreditRes!!.dynamicData!!.accountId,
                    skCreditAmt,
                    ""
                )
            )
        } else {
            Utils.setToast(
                applicationContext, getString(R.string.please_try_again)
            )
        }
    }

    private fun scaleUpPayment() {
        viewModel.scaleUpPaymentInitiate(
            custId,
            orderId,
            scaleUpAmt
        )
    }

    fun showProgressDialog(activity: Activity) {
        if (progressDialog != null) {
            progressDialog = null
        }
        progressDialog = Dialog(activity, R.style.CustomDialog)
        val inflater = LayoutInflater.from(activity)
        val mView = inflater.inflate(R.layout.progress_dialog, null)
        progressDialog!!.setContentView(mView)
        if (!progressDialog!!.isShowing && !activity.isFinishing) {
            progressDialog!!.show()
        }
    }

    private fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    private fun showPrepaidOrderPopup(calPrepaidAmount: Long) {
        val handler = Handler()
        val mView = layoutInflater.inflate(R.layout.prepaid_order_popup, null)
        val customDialog = Dialog(this, R.style.CustomDialog)
        customDialog.setContentView(mView)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val description = mView.findViewById<TextView>(R.id.pd_description)
        val title = mView.findViewById<TextView>(R.id.pd_title)
        okBtn.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.ok)
        title.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.prepaid_order)
        //description.setText(getString(R.string.prepaid_order_mag) + " " + Html.fromHtml("<font color=#424242>&#8377; 700") + " " + getString(R.string.msg_orderless));
        description.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.prepaid_order_mag1) + DecimalFormat(
                "##.##"
            ).format(calPrepaidAmount) + RetailerSDKApp.getInstance().dbHelper.getString(
                R.string.prepaid_order_mag2
            )
        okBtn.setOnClickListener { v: View? -> customDialog.dismiss() }
        handler.postDelayed(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 2000)
                handler.removeCallbacks(this)
                customDialog.dismiss()
            }
        }, 2000)
        customDialog.show()
    }

    // Order placed confirmation popup
    private fun orderConformationPopup() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.popup_order_confirmation)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(false)
        val continueShopping = dialog.findViewById<Button>(R.id.continueShopping)
        val tvOrderId = dialog.findViewById<TextView>(R.id.tv_order_id)
        val tvTotalAmount = dialog.findViewById<TextView>(R.id.tv_total_amt)
        val orderMsg = dialog.findViewById<TextView>(R.id.orderMsg)
        val tv_congratulation = dialog.findViewById<TextView>(R.id.tv_congratulation)
        val tvDeliverByH = dialog.findViewById<TextView>(R.id.tvDeliverByH)
        val iv_kisan_dan = dialog.findViewById<ImageView>(R.id.iv_kisan_dan)
        val tvSelectDateH = dialog.findViewById<TextView>(R.id.tvSelectDateH)
        val tvDelayH = dialog.findViewById<TextView>(R.id.tvDelayH)
        val tvEtaH = dialog.findViewById<TextView>(R.id.tvEtaH)
        val tvETA = dialog.findViewById<TextView>(R.id.tvETA)
        val cgDate = dialog.findViewById<FlexboxLayout>(R.id.cgDate)
        val btnChangeDate = dialog.findViewById<Button>(R.id.btnChangeDate)
        iv_kisan_dan!!.visibility = View.GONE
        tvSelectDateH!!.visibility = View.GONE
        tvEtaH!!.visibility = View.GONE
        tvETA!!.visibility = View.GONE
        tvDelayH!!.visibility = View.GONE
        cgDate!!.visibility = View.GONE
        btnChangeDate!!.visibility = View.GONE
        if (orderType == 8) {
            btnChangeDate.visibility = View.GONE
        }
        tv_congratulation!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Congratulation)
        continueShopping!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_continue_shopping)
        orderMsg!!.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment_done_msg)
        tvDeliverByH!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_delivered_by_shopkirana)
        tvOrderId!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_order_id) + orderId
        tvTotalAmount!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_order_amount) + mBinding.tvPayableamnt.text.toString()
        btnChangeDate.setOnClickListener { view: View? ->
            btnChangeDate.visibility = View.GONE
            tvDelayH.visibility = View.VISIBLE
            cgDate.visibility = View.VISIBLE
        }
        continueShopping.setOnClickListener { v: View? ->
            dialog.dismiss()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
            Utils.rightTransaction(this)
        }
        dialog.show()
    }

    private fun showCodLimitDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.popup_cod_limit_terms)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = dialog.findViewById<TextView>(R.id.tvDesc)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        tvTitle!!.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.maximum_pod_limit)
        tvDesc!!.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.cod_limit_terms)
        ivClose!!.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        dialog.show()
        RetailerSDKApp.getInstance().updateAnalytics("COD_limit_terms_dialog")
    }

    private fun insertPaymentStatusAPICall(
        status: String,
        statusCode: String?,
        paymentFrom: String,
        gatewayTransId: String?,
        truePayTxtnAmt: Double,
        GatewayRequest: String,
        GatewayResponse: String,
        PaymentThrough: String
    ) {
        val paymentGatewayModel = PaymentGatewayModel(
            orderId,
            gatewayOrderId,
            gatewayTransId,
            truePayTxtnAmt,
            "INR",
            status,
            paymentCheckedMsg,
            statusCode,
            paymentFrom,
            GatewayRequest,
            GatewayResponse,
            PaymentThrough,
            orderList2!!.isPayLater
        )
        val jsonString = Gson().toJson(paymentGatewayModel)
        println("jsonString::$jsonString")
        try {
            val destr = Aes256.encrypt(
                jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                    Date()
                ) + "1201"
            )
            viewModel.getInsertOnlineTransaction(
                PaymentReq(destr),
                "Payment Screen $paymentFrom(Status:$status)"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (status.equals("Failed", ignoreCase = true)) {
            IsBackPaymentFalse = true
        }
    }

    private fun insertCashPaymentStatusAPICall(
        status: String,
        paymentFrom: String,
        truePayTxtnAmt: Double,
        paymentThrough: String
    ) {
        val paymentGatewayModel = PaymentGatewayModel(
            orderId,
            "",
            "",
            truePayTxtnAmt,
            "INR",
            status,
            "",
            "",
            paymentFrom,
            "",
            "",
            paymentThrough,
            orderList2!!.isPayLater
        )
        val jsonString = Gson().toJson(paymentGatewayModel)
        println("jsonString::$jsonString")
        try {
            val destr = Aes256.encrypt(
                jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                    Date()
                ) + "1201"
            )

            updateCashStatus = true
            viewModel.getInsertOnlineTransaction(
                PaymentReq(destr),
                "Payment Screen $paymentFrom(Status:$status)"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePaymentAPICall(paymentMode: String, isSuccess: Boolean) {
        val updateOrderPlacedModel = UpdateOrderPlacedModel(
            orderId,
            isSuccess, paymentMode, transactionId, paymentThrough, orderList2!!.isPayLater
        )
        val jsonString = Gson().toJson(updateOrderPlacedModel)
        println("jsonStringUpdate::$jsonString")
        try {
            val destr = Aes256.encrypt(
                jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                    Date()
                ) + "1201"
            )
            viewModel.getUpdateOnlineTransaction(
                PaymentReq(destr),
                "Pay Now Screen $paymentMode(Status:$isSuccess)"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inner class GenericCheckChangeListener constructor(private val view: View) :
        CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            when (view.id) {
                R.id.cbCash -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = ""
                    mBinding.etAmountHdfc.text = ""
                    mBinding.etAmountEpay.text = ""
                    mBinding.etAmountCb.text = ""
                    mBinding.etAmountSkC.text = ""
                    mBinding.etAmountSu.text = ""
                    mBinding.etAmountRazorpay.text = ""
                    mBinding.etAmountICICI.text = ""
                    mBinding.etAmountCash.text = "" + finalAmount
                    cashAmount = finalAmount
                    gullakAmount = 0.0
                    hdfcAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    if (availableCODLimit != -1.0 && cashAmount > availableCODLimit) {
                        mBinding.etAmountCash.text = "0"
                        mBinding.etAmountHdfc.text = "" + finalAmount
                        cashAmount = 0.0
                    }
                    onButtonClick()
                }

                R.id.radioGullak -> if (isChecked) {
                    mBinding.cbCash.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountGullak.text = "" + finalAmount
                    gullakAmount = finalAmount
                    cashAmount = 0.0
                    hdfcAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radio_hdfc -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountHdfc.text = "" + finalAmount
                    hdfcAmount = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radio_razorpay -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountRazorpay.text = "" + finalAmount
                    razorpayAmount = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    hdfcAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radioICICIPayment -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "" + finalAmount
                    iCICIPayAmount = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    hdfcAmount = 0.0
                    razorpayAmount = 0.0
                    onButtonClick()
                }

                R.id.radio_epay -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = ""
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = ""
                    mBinding.etAmountCb.text = ""
                    mBinding.etAmountSkC.text = ""
                    mBinding.etAmountSu.text = ""
                    mBinding.etAmountRazorpay.text = ""
                    mBinding.etAmountICICI.text = ""
                    mBinding.etAmountEpay.text = "" + finalAmount
                    ePayAmount = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    hdfcAmount = 0.0
                    checkBookAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radio_checkbook -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = ""
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = ""
                    mBinding.etAmountEpay.text = ""
                    mBinding.etAmountSkC.text = ""
                    mBinding.etAmountSu.text = ""
                    mBinding.etAmountRazorpay.text = ""
                    mBinding.etAmountICICI.text = ""
                    mBinding.etAmountCb.text = "" + finalAmount
                    checkBookAmount = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    hdfcAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radioSkC -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.cbCash.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountSkC.text = "" + finalAmount
                    skCreditAmt = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    hdfcAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    scaleUpAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }

                R.id.radioScaleUp -> if (isChecked) {
                    mBinding.cbCash.isChecked = false
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPayment.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountCash.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountRazorpay.text = "0"
                    mBinding.etAmountICICI.text = "0"
                    mBinding.etAmountSu.text = "" + finalAmount
                    scaleUpAmt = finalAmount
                    cashAmount = 0.0
                    gullakAmount = 0.0
                    hdfcAmount = 0.0
                    checkBookAmount = 0.0
                    ePayAmount = 0.0
                    skCreditAmt = 0.0
                    razorpayAmount = 0.0
                    iCICIPayAmount = 0.0
                    onButtonClick()
                }
            }
        }
    }

    override fun onPaymentSuccess(s: String, paymentData: PaymentData) {
        println(
            """
    Payment Successful :
    Payment ID: $s
    Payment Data: ${paymentData.data}
    """.trimIndent()
        )
        val `object` = paymentData.data
        try {
            transactionId = `object`.getString("razorpay_payment_id")
            gatewayOrderId = orderId.toString()
            holePaymentSucceedCheck = true
            insertPaymentStatusAPICall(
                "Success",
                "200",
                "razorpay",
                transactionId,
                razorpayAmount,
                "Razorpay",
                `object`.toString(),
                "Razorpay"
            )
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    override fun onPaymentError(i: Int, s: String, paymentData: PaymentData) {
        val `object` = paymentData.data
        holePaymentSucceedCheck = false
        println(
            """
    Payment Failed:
    Payment Data: ${paymentData.data}
    """.trimIndent()
        )
        insertPaymentStatusAPICall(
            "Failed",
            "0",
            "razorpay",
            "",
            razorpayAmount,
            "Razorpay",
            `object`.toString(),
            "Razorpay"
        )
    }

    private fun handleUdharCreditLimitResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.isSuccess && it.dynamicData != null && it.dynamicData!!.amount > 0
                    ) {
                        skCreditRes = it
                        mBinding.tvLimitSk.visibility = View.VISIBLE
                        mBinding.tvLimitSk.text = "₹ " + it.dynamicData!!.amount + "/-"
                        mBinding.radioSkC.visibility = View.VISIBLE
                        mBinding.etAmountSkC.visibility = View.VISIBLE
                        mBinding.btnCallSkC.visibility = View.GONE
                        mBinding.tvContactUs.visibility = View.GONE
                    } else {
                        mBinding.radioSkC.visibility = View.GONE
                        mBinding.etAmountSkC.visibility = View.GONE
                        mBinding.btnCallSkC.visibility = View.VISIBLE
                        mBinding.tvContactUs.visibility = View.VISIBLE
                        mBinding.tvContactUs.textSize = 14f
                        mBinding.tvContactUs.text =
                            "( " + getString(R.string.contact_on) + RetailerSDKApp.getInstance().dbHelper.getString(
                                R.string.checkbook_contact
                            ) + ")"
                        if (it.dynamicData != null) {
                            if (!TextUtils.isNullOrEmpty(it.message)) {
                                mBinding.tvMessage.visibility = View.VISIBLE
                                mBinding.tvMessage.text = it.message
                            }
                            if (!it.dynamicData!!.showHideLimit) {
                                mBinding.tvLimitSk.visibility = View.VISIBLE
                                mBinding.tvLimitSk.text =
                                    "₹ " + it.dynamicData!!.amount + "/-"
                            }
                        }
                    }
                }
            }

            is Response.Error -> {
                mBinding.tvContactUs.visibility = View.VISIBLE
                mBinding.tvContactUs.textSize = 15f
                mBinding.tvContactUs.text =
                    "(" + getString(R.string.contact_on) + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountSkC.visibility = View.GONE
                mBinding.btnCallSkC.visibility = View.VISIBLE
                mBinding.radioSkC.visibility = View.GONE
            }
        }
    }

    private fun handleEPaylaterCreditLimitResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        if (it.size() > 0) {
                            // debug mode add by devendra
                            ePayLimit = it["availableCredit"].asDouble
                            mBinding.tvLimit.text =
                                "₹ " + DecimalFormat("##.##").format(ePayLimit) + "/-"
                        } else {
                            Toast.makeText(
                                applicationContext, RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.net_connection_off
                                ), Toast.LENGTH_SHORT
                            ).show()
                        }
                        if (ePayLimit == 0.0) {
                            mBinding.tvLimit.textSize = 15f
                            mBinding.tvLimit.text =
                                "( " + RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.contact_on
                                ) + RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkbook_contact) + ")"
                            mBinding.etAmountEpay.visibility = View.GONE
                            mBinding.callBtn.visibility = View.VISIBLE
                            mBinding.radioEpay.visibility = View.GONE
                        } else {
                            mBinding.etAmountEpay.visibility = View.VISIBLE
                            mBinding.callBtn.visibility = View.GONE
                            mBinding.radioEpay.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                mBinding.tvLimit.textSize = 15f
                mBinding.tvLimit.text =
                    "( " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.contact_on) + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountEpay.visibility = View.GONE
                mBinding.callBtn.visibility = View.VISIBLE
                mBinding.radioEpay.visibility = View.GONE
                mBinding.rlEpay.visibility = View.GONE
            }
        }
    }

    private fun handleCheckBookLimitResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    val checkBookCustomerLimit =
                        Gson().fromJson(it.toString(), CheckBookData::class.java)
                    checkBookLimit = if (BuildConfig.DEBUG) {
                        checkBookCustomerLimit.checkBookCustomerLimit.getCheckBookCustomerResult()
                            .getAvailable_amount().toDouble()
                    } else {
                        checkBookCustomerLimit.checkBookCustomerLimit.getCheckBookCustomerResult()
                            .getAvailable_amount().toDouble()
                    }
                    mBinding.tvLimitCb.text = "₹ $checkBookLimit/-"
                    if (checkBookLimit <= 0) {
                        mBinding.tvLimitCb.textSize = 15f
                        mBinding.tvLimitCb.text =
                            "( " + getString(R.string.contact_on) + SharePrefs.getInstance(
                                applicationContext
                            ).getString(SharePrefs.COMPANY_CONTACT) + ")"
                        mBinding.etAmountCb.visibility = View.GONE
                        mBinding.callBtnCb.visibility = View.VISIBLE
                        mBinding.radioCheckbook.visibility = View.GONE
                    } else {
                        mBinding.etAmountCb.visibility = View.VISIBLE
                        mBinding.callBtnCb.visibility = View.GONE
                        mBinding.radioCheckbook.visibility = View.VISIBLE
                    }
                }
            }

            is Response.Error -> {
                mBinding.tvLimitCb.textSize = 15f
                mBinding.tvLimitCb.text =
                    "(" + getString(R.string.contact_on) + SharePrefs.getInstance(
                        applicationContext
                    ).getString(SharePrefs.COMPANY_CONTACT) + ")"
                mBinding.etAmountCb.visibility = View.GONE
                mBinding.callBtnCb.visibility = View.VISIBLE
                mBinding.radioCheckbook.visibility = View.GONE
                mBinding.rlCheckBook.visibility = View.GONE
            }
        }
    }

    private fun handleCheckCODLimitResult(it: Response<JsonElement>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        if (!it.isJsonNull) {
                            val jsonObject = it.asJsonObject
                            if (jsonObject["IsCustomCODLimit"].asBoolean) {
                                val cODLimit = jsonObject["CODLimit"].asDouble
                                availableCODLimit =
                                    jsonObject["AvailableCODLimit"].asDouble.toLong().toDouble()
                                mBinding.tvCodLimit.text =
                                    RetailerSDKApp.getInstance().noteRepository.getString(
                                        R.string.maximum_cod_limit
                                    ) + " ₹" + cODLimit
                                mBinding.tvCodLimitAvail.text =
                                    RetailerSDKApp.getInstance().noteRepository.getString(
                                        R.string.available_cod_limit
                                    ) + " ₹" + availableCODLimit
                                mBinding.rlCodLimit.visibility = View.VISIBLE
                                if (availableCODLimit <= 0) {
                                    mBinding.cbCash.isEnabled = false
                                    mBinding.cbCash.isClickable = false
                                    mBinding.cbCash.isFocusable = false
                                }
                            }
                        } else {
                            availableCODLimit = -1.0
                            mBinding.rlCodLimit.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                mBinding.rlCodLimit.visibility = View.GONE
            }
        }
    }

    private fun handleScaleUpLimitResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    scaleUpLimit = it.creditLimit
                    mBinding.tvLimitSu.text =
                        "₹ " + DecimalFormat("##.##").format(scaleUpLimit) + "/-"
                    if (it.isBlock || scaleUpLimit <= 0) {
                        mBinding.etAmountSu.visibility = View.GONE
                        mBinding.btnCallSu.visibility = View.VISIBLE
                        mBinding.radioScaleUp.visibility = View.GONE
                        if (it.isBlock) mBinding.tvLimitSu.text = it.message
                    } else {
                        mBinding.etAmountSu.visibility = View.VISIBLE
                        mBinding.btnCallSu.visibility = View.GONE
                        mBinding.radioScaleUp.visibility = View.VISIBLE
                        if (it.isBlock) {
                            mBinding.tvLimitSu.text = it.message
                            mBinding.radioScaleUp.visibility = View.INVISIBLE
                            mBinding.etAmountSu.visibility = View.INVISIBLE
                            mBinding.btnCallSu.visibility = View.VISIBLE
                        }
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.tvContactUs.visibility = View.VISIBLE
                mBinding.tvContactUs.textSize = 15f
                mBinding.tvContactUs.text =
                    "(" + getString(R.string.contact_on) + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountSu.visibility = View.GONE
                mBinding.btnCallSu.visibility = View.VISIBLE
                mBinding.radioScaleUp.visibility = View.GONE
            }
        }
    }

    private fun handlePrepaidOrderResult(it: Response<PrepaidOrder>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.isStatus) {
                        prepaidOrderModel = it.prepaidOrderModel
                        PrepaidOrder(finalAmount)
                    }
                    Utils.hideProgressDialog()
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleEpalaterPaymentResult(it: Response<EpayLaterResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    var flag = false
                    if (utils!!.isNetworkAvailable) {
                        try {
                            if (it.status.equals(
                                    "confirmed",
                                    ignoreCase = true
                                ) || it.status.equals("delivered", ignoreCase = true)
                            ) {
                                flag = false
                                insertPaymentStatusAPICall(
                                    "Success",
                                    ePaystatusCode,
                                    "ePaylater",
                                    ePayMarketplaceOrderId,
                                    ePayamount,
                                    ePaylaterRequest,
                                    ePayReponseObj,
                                    ""
                                )
                            } else {
                                flag = true
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        flag = true
                    }
                    if (flag) {
                        insertPaymentStatusAPICall(
                            "Failed",
                            "epaylater txtn amount not conform",
                            "ePaylater",
                            ePayMarketplaceOrderId,
                            ePayamount,
                            ePaylaterRequest,
                            ePayReponseObj,
                            ""
                        )
                        holePaymentSucceedCheck = false
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                insertPaymentStatusAPICall(
                    "Failed",
                    "epaylater txtn amount not conform",
                    "ePaylater",
                    ePayMarketplaceOrderId,
                    ePayamount,
                    ePaylaterRequest,
                    ePayReponseObj,
                    ""
                )
                holePaymentSucceedCheck = false
            }
        }
    }

    private fun handleInsertTransactionResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    hideProgressDialog()
                    if (updateCashStatus) {
                        var paymentMode = ""
                        paymentMode = if (holePaymentSucceedCheck) {
                            "Online"
                        } else {
                            "Offline"
                        }
                        updatePaymentAPICall(paymentMode, true)
                    } else {
                        if (holePaymentSucceedCheck) {
                            if (cashAmount > 0) {
                                insertCashPaymentStatusAPICall(
                                    "Success",
                                    "Cash",
                                    cashAmount,
                                    "Offline"
                                )
                            } else {
                                updatePaymentAPICall("Online", it)
                            }
                        }
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                hideProgressDialog()
                mBinding.placeBtn.isClickable = true
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleUpdateTransactionResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    try {
                        hideProgressDialog()
                        if (it) {
                            if (!IsSuccess) {
                                Utils.setToast(
                                    applicationContext, getString(R.string.payment_cancel)
                                )
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                Utils.rightTransaction(this@PayNowActivity)
                            } else {
                                if (otherPaymentMode) {
                                    goToHome("success")
                                } else {
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            ShoppingCartActivity::class.java
                                        )
                                    )
                                    Utils.rightTransaction(this@PayNowActivity)
                                }
                            }
                        } else {
                            mBinding.placeBtn.isClickable = true
                            Utils.setToast(
                                applicationContext, "Something went to wrong"
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                goToHome("exception")
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleGeneratePaymentResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    if (it.isSuccess) {
                        val url = it.message
                        startActivityForResult(
                            Intent(
                                applicationContext,
                                DirectUdharActivity::class.java
                            ).putExtra("url", url), SK_CREDIT
                        )
                    } else {
                        mBinding.placeBtn.isClickable = true
                        Utils.setToast(
                            applicationContext, it.message
                        )
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleScaleUpPaymentInitiateResult(it: Response<ScaleUpResponse>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    if (it.status) {
                        transactionId = it.transactionId
                        val baseUrl = it.BaiseUrl
                        if(!TextUtils.isNullOrEmpty(it.url)) {
                            startActivityForResult(
                                Intent(
                                    applicationContext,
                                    ScaleUpActivity::class.java
                                )
                                    .putExtra("url", it.url), SCALEUP
                            )
                        }
//                        if (EndPointPref.getInstance(applicationContext)
//                                .getBoolean(EndPointPref.IS_SCALE_UP_SDK)
//                        ) {
//                            val json = JSONObject()
//                            try {
//                                json.put("isPayNow", true)
//                                json.put("transactionId", transactionId)
//                                json.put("baseUrl", baseUrl)
//                                Log.e(TAG, "onNext pay now transactionId: $transactionId")
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
////                            methodChannel!!.invokeMethod("ScaleUP", json.toString())
////                            startActivity(
////                                FlutterActivity
////                                    .withCachedEngine(FLUTTER_ENGINE_ID)
////                                    .build(applicationContext)
////                            )
//                        } else {
//                            if(!TextUtils.isNullOrEmpty(it.url)) {
//                                startActivityForResult(
//                                    Intent(
//                                        applicationContext,
//                                        ScaleUpActivity::class.java
//                                    )
//                                        .putExtra("url", it.url), SCALEUP
//                                )
//                            }
//                        }
                    } else {
                        mBinding.placeBtn.isClickable = true
                        Utils.setToast(
                            applicationContext, it.message
                        )
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun getHDFCRSAKey(isCredit: Boolean) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            showProgressDialog(this)
            viewModel.getHDFCRSAKey(
                orderId.toString(),
                "" + hdfcAmount.toString(),
                isCredit,
                "Payment Screen"
            )
            viewModel.getHDFCRSAKeyData.observe(this) {
                try {
                    hideProgressDialog()
                    if (!TextUtils.isNullOrEmpty(it)) {
                        callHDFC(orderId.toString(), it, hdfcAmount.toString())
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
}