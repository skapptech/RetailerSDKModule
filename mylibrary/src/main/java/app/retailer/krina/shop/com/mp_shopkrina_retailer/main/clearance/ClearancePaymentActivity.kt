package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.format.Formatter
import android.text.util.Linkify
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityClearancePaymentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EPayWebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EpayLaterEncryptDecryptUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnClearanceOfferClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnSelectClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.CheckSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.HDFCActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ScaleUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ClearanceDiscountAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.AddPaymentActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.RtgsInfoActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CheckBookData
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ClearanceItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceOfferResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceShoppingCart
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ItemDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentGatewayModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.UpdateOrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ClearanceOrderResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.clearance.ClearanceViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.clearance.ClearanceViewModelFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Aes256
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.chqbook.vypaar.ChqbookVypaarCallback
import com.chqbook.vypaar.ChqbookVypaarClient
import com.chqbook.vypaar.ChqbookVypaarKeys
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.payphi.customersdk.utils.HmacUtility
import com.payphi.customersdk.views.Application
import com.payphi.customersdk.views.PayPhiSdk
import com.payphi.customersdk.views.PaymentOptionsActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.roundToInt

class ClearancePaymentActivity : AppCompatActivity(), View.OnClickListener, OnSelectClick,
    OnClearanceOfferClick {
    private val TAG = this.javaClass.simpleName
    private val E_PAY_LATER_REQUEST = 1563
    private val HDFC_REQUEST = 999
    private val SK_CREDIT = 888
    private val SCALEUP = 777
    private lateinit var viewModel: ClearanceViewModel

    private lateinit var mBinding: ActivityClearancePaymentBinding
    private var utils: Utils? = null
    private lateinit var commonClassForAPI: CommonClassForAPI
    private var customProgressDialog: Dialog? = null
    private var customDialogOrderPlacedTime: Dialog? = null

    private var cartTotalAmount = 0.0
    private var totalDiscount = 0.0
    private var ePayLimit = 0.0
    private val deliveryCharges = 0.0
    private var ePayResAmt = 0.0
    private var checkBookLimit = 0.0

    private var custId = 0
    private var orderId = 0
    private var checkBookminiAmount = 0
    private var custMobile: String? = null
    private var skCode: String? = null
    private var gatewayOrderId = ""
    private var hdfcRequest = ""
    private var ePaylaterRequest = ""
    private var ePaystatusCode = ""
    private var ePayMarketplaceOrderId = ""
    private var ePayResponseObj = ""
    private var paymentCheckedMsg: String? = null
    private var createdDate = ""
    private var billDiscountId = ""
    private var otherPaymentMode = false
    private var canPostOrder = false
    private var ePayLaterLimit = true
    private var isChqbookLimit = true
    private var IsSuccess = true
    private var holePaymentSucceedCheck = false
    private var IsBackPaymentFalse = false
    private var epaylaterRetry = false
    private var orderAmountFlag = false
    private var prepaidOrderStatus = false
    private var isOfferApiCalled = false

    // amt
    private var gullakAmount: Long = 0
    private var eCODAmount: Long = 0
    private var ePayAmount: Long = 0
    private var creditAmount: Long = 0
    private var hdfcAmount: Long = 0
    private var checkBookAmount: Long = 0
    private var skCreditAmt: Long = 0
    private var scaleUpAmt: Long = 0
    private var iCICIPayAmount: Long = 0

    //
    private var maxWalletUseAmount = ""
    private var marketPlaceOrderId = ""
    private var incremental_no = 0
    private var calAmountOnline = 0.0
    private var tcsPercent = 0.0
    private var tcsAmount = 0.0
    private var lat = 0.0
    private var lng = 0.0
    private var scaleUpLimit = 0.0
    private var transactionId = ""
    private var paymentThrough = ""
    private lateinit var list: ArrayList<ClearanceItemModel>
    private var orderPlacedNewResponse: ClearanceOrderResponse? = null

    // Bill Discount params
    private var discountList: ArrayList<BillDiscountModel>? = null
    private var dialog: BottomSheetDialog? = null
    private var discountAdapter: ClearanceDiscountAdapter? = null
    private var prepaidOrderModel: PrepaidOrderModel? = null
//    private lateinit var chqbookInitialize: ChqbookVypaarClient
    private var skCreditRes: CreditLimit? = null
    private var chqbookTransactionId = ""
    private lateinit var cartModel: ClearanceShoppingCart
    private var mop = ""

//    private val FLUTTER_ENGINE_ID = "my_flutter_engine"
//    private val CHANNEL = "com.ScaleUP"
//    private var flutterEngine: FlutterEngine? = null
//    private var methodChannel: MethodChannel? = null
    private var iCICIMerchantId = ""
    private var iCICIFinaPayAmount = ""
    private var iciciPayRequest: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!BuildConfig.DEBUG) window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
//        )
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_clearance_payment)
        if (intent.extras != null) {
            list = intent.getSerializableExtra("list") as ArrayList<ClearanceItemModel>
        }
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this, ClearanceViewModelFactory(application, appRepository)
        )[ClearanceViewModel::class.java]

        observe(viewModel.getICICIPaymentCheckData, ::handleICICITransactionResult)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // initialize views
        initialization()
        // Shared Data
        sharedData()
        activePaymentMode()
        //set value
        setValues()
        cartTotalAmount = list.sumOf { it.unitPrice * it.qty }
        val itemList = ArrayList<ClearanceShoppingCart.ClearanceIDetailDc>()
        for (i in list) {
            val model = ClearanceShoppingCart.ClearanceIDetailDc()
            model.id = i.id
            model.unitPrice = i.unitPrice
            model.qty = i.qty
            itemList.add(model)
        }
        cartModel = ClearanceShoppingCart()
        cartModel.customerId =
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        cartModel.totalAmount = cartTotalAmount
        cartModel.createdDate = ""
        cartModel.orderId = 0
        cartModel.lat = lat
        cartModel.lng = lng
        cartModel.itemDetails = itemList
        cartModel.mop = mop

        totalPayableAmountShow(totalDiscount)

        // Wallet API calling
        if (utils!!.isNetworkAvailable) {
            showProgressDialog()
            commonClassForAPI.getTcsPercent(tcsObserver, custId)
            commonClassForAPI.getCreditLimit(creditLimitObserver, custId)
            commonClassForAPI.getScaleUpLimit(scaleUpLimitObserver, custId)
            commonClassForAPI.ePayLaterCustomerLimit(
                ePayLaterCheckLimit,
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.BEARER_TOKEN),
                skCode
            )
            commonClassForAPI.checkBookLimitDy(
                checkBookCheckLimit,
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CHECKBOOK_BASE_URL),
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CHECKBOOK_API_KEY),
                CheckBookCreditLimitRes(
                    skCode!!
                )
            )
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
        //offer bill discount
        if (utils!!.isNetworkAvailable) {
            showProgressDialog()
            mBinding.placeBtn.isClickable = false
            commonClassForAPI!!.getBillDiscountOfferList(
                billDiscountObserver,
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                "CL Payment Screen"
            )
        }
        //Prepaid Order API
        if (utils!!.isNetworkAvailable) {
            commonClassForAPI!!.prepaidOrder(
                prepaidOrder,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                "CL Payment Screen"
            )
        } else {
            onBackPressed()
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_apply_coupon, R.id.rlBillDiscount -> {
                MyApplication.getInstance().updateAnalytics("coupon_click")
                if (isOfferApiCalled) if (discountList != null && discountList!!.size > 0 && orderPlacedNewResponse == null) {
                    showBillDiscountDialog()
                } else {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.no_discount_available)
                    )
                } else Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_wait)
                )
            }

            R.id.callBtn -> {
                val phone =
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
                if (!TextUtils.isNullOrEmpty(phone)) {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
                }
            }

            R.id.callBtn_cb, R.id.btnCallSkC -> {
                val phone =
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
                if (!TextUtils.isNullOrEmpty(phone)) {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
                }
            }

            R.id.tvDesRtgs -> startActivity(
                Intent(
                    applicationContext, RtgsInfoActivity::class.java
                )
            )

            R.id.placeBtn -> {
                val isChqbookBackLimit: Boolean
                val gullakBal =
                    SharePrefs.getInstance(applicationContext).getDouble(SharePrefs.GULLAK_BALANCE)
                val saveAppVer = SharePrefs.getStringSharedPreferences(this, SharePrefs.APP_VERSION)
                if ("1.24".equals(saveAppVer, ignoreCase = true)) {
                    ePayLaterLimit = if (ePayAmount > 0) {
                        ePayLimit == 0.0 || ePayAmount > ePayLimit
                    } else {
                        false
                    }
                    if (checkBookAmount > 0) {
                        isChqbookLimit = checkBookLimit == 0.0 || checkBookAmount > checkBookLimit
                        isChqbookBackLimit =
                            checkBookminiAmount == 0 || checkBookAmount < checkBookminiAmount
                    } else {
                        isChqbookBackLimit = false
                        isChqbookLimit = false
                    }

                    if (ePayLaterLimit) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.epaylater_message) + " " + ePayLimit
                        )
                    } else if (isChqbookLimit || isChqbookBackLimit) {
                        if (isChqbookLimit) {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.checkbook_message) + " " + checkBookLimit
                            )
                        } else {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.checkBok_bill_value) + checkBookminiAmount
                            )
                        }
                    } else if (gullakAmount > 0 && gullakBal < gullakAmount) {
                        startActivityForResult(
                            Intent(
                                applicationContext,
                                AddPaymentActivity::class.java
                            ).putExtra("amount", gullakAmount - gullakBal).putExtra("screen", 2), 9
                        )
                    } else if (skCreditAmt > 0 && skCreditRes!!.dynamicData!!.amount < skCreditAmt
                    ) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                        )
                        return
                    } else {
                        mBinding.placeBtn.isClickable = false
                        if (orderId != 0) {
                            if (mBinding.radioPayNowCod.isChecked) {
                                orderPlacedPopup()
                            } else {
                                commonClassForAPI?.clearanceOrderValidPayment(
                                    clearanceOrderValidPayObserver, orderId
                                )
                            }
                        } else {
                            IsSuccess = true
                            canPostOrder = false
                            if (utils!!.isNetworkAvailable) {
                                onButtonClick()
                                if (canPostOrder) {
                                    postOrderBtnData()
                                } else {
                                    mBinding.placeBtn.isClickable = true
                                    Utils.setToast(
                                        applicationContext,
                                        MyApplication.getInstance().dbHelper.getString(R.string.amount_does_not_match_with_order_amount)
                                    )
                                }
                            } else {
                                mBinding.placeBtn.isClickable = true
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                                )
                            }
                        }
                    }
                } else {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.update_your_app)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            )
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(netConnectionReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        orderPlaceObserver.dispose()
    }

    override fun onBackPressed() {
        if (IsBackPaymentFalse) {
            IsSuccess = false
            updatePaymentAPICall("", false, transactionId)
        } else {
            super.onBackPressed()
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(applicationContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HDFC_REQUEST) {
            if (data != null && resultCode == RESULT_OK) {
                val `object`: JSONObject
                try {
                    `object` = JSONObject(data.getStringExtra(AvenuesParams.RESPONSE))
                    val hdfcTxtAmt = `object`.getString("amount").toDouble()
                    Utils.setToast(
                        applicationContext,
                        "Transaction " + `object`.getString(AvenuesParams.STATUS)
                    )
                    if (`object`.getString(AvenuesParams.STATUS)
                            .equals("Success", ignoreCase = true)
                    ) {
                        if (ePayAmount > 0) {
                            ePayLater(ePayAmount.toDouble())
                        } else {
                            holePaymentSucceedCheck = true
                        }
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
                        //                        // update analytics purchase
//                        MyApplication.getInstance().updateAnalyticPurchase(object.getString(AvenuesParams.TRACKING_ID),
//                                hdfcTxtAmt, mShoppingCart.getDeliveryCharges(), mShoppingCart.getApplyOfferId(), "" + orderId,
//                                orderPlacedNewResponse.getOrderMaster().getWheelcount(), "hdfc", mShoppingCart.getShoppingCartItemDcs());
                    } else {
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
                /*mBinding.radioCredit.isChecked() ? "" + "credit hdfc" : "hdfc"*/
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "hdfc",
                    "",
                    hdfcAmount.toDouble(),
                    hdfcRequest,
                    "",
                    ""
                )
            }
        } else if (requestCode == E_PAY_LATER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    showProgressDialog()
                    val jsonObject = JSONObject(data.getStringExtra("extra_payment_response"))
                    val Data = jsonObject.getString("Data")
                    val decrypt = Aes256.decrypt(
                        Data, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                            Date()
                        ) + "1201"
                    )
                    val `object` = JSONObject(convertStandardJSONString(decrypt))
                    Logger.logD(TAG, "EpayLaterRequest::-$`object`")
                    val encdata = `object`.getJSONObject("encdata")
                    Utils.setToast(
                        this, """
     ${encdata.getString("status")}
     ${encdata.getString("statusDesc")}
     """.trimIndent()
                    )
                    var amount = encdata.getString("amount").toDouble()
                    amount = amount / 100
                    val jsonParser = JsonParser()
                    val responseObj = jsonParser.parse(encdata.toString()) as JsonObject
                    if (encdata.getString("status").equals("Success", ignoreCase = true)) {
                        holePaymentSucceedCheck = true
                        val eplOrderId = encdata.getString("eplOrderId")
                        if (commonClassForAPI != null) {
                            paymentCheckedMsg = encdata.getString("statusDesc")
                            ePaystatusCode = encdata.getString("statusCode")
                            gatewayOrderId = encdata.getString("marketplaceOrderId")
                            ePayMarketplaceOrderId = eplOrderId
                            ePayResponseObj = responseObj.toString()
                            ePayResAmt = amount
                            commonClassForAPI.ePayLaterOrderConfirmed(
                                ePayLaterOrderConfirm,
                                eplOrderId,
                                SharePrefs.getInstance(this).getString(SharePrefs.BEARER_TOKEN),
                                encdata.getString("marketplaceOrderId")
                            )
                        }
                        //                        // update analytics
//                        MyApplication.getInstance().updateAnalyticPurchase(eplOrderId,
//                                amount, mShoppingCart.getDeliveryCharges(), mShoppingCart.getApplyOfferId(), "" + orderId,
//                                orderPlacedNewResponse.getOrderMaster().getWheelcount(), "epayLater", mShoppingCart.getShoppingCartItemDcs());
                    } else {
                        hideProgressDialog()
                        insertPaymentStatusAPICall(
                            "Failed",
                            encdata.getString("statusCode"),
                            "ePaylater",
                            encdata.getString("marketplaceOrderId"),
                            amount,
                            ePaylaterRequest,
                            responseObj.toString(),
                            ""
                        )
                        holePaymentSucceedCheck = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                hideProgressDialog()
                insertPaymentStatusAPICall(
                    "Failed",
                    "0",
                    "ePaylater",
                    marketPlaceOrderId,
                    ePayAmount.toDouble(),
                    ePaylaterRequest,
                    null,
                    ""
                )
                holePaymentSucceedCheck = false
            }
        } else if (requestCode == 9 && resultCode == RESULT_OK) {
            mBinding.tvGullakBal.text =
                MyApplication.getInstance().dbHelper.getString(R.string.balance) + " " + SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.GULLAK_BALANCE)
            mBinding.placeBtn.callOnClick()
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.payment_cancel)
                )
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "DirectUdhar",
                    "",
                    skCreditAmt.toDouble(),
                    "DirectUdhar",
                    "",
                    "DirectUdhar"
                )
            }
        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            mBinding.placeBtn.callOnClick()
        }
    }


    override fun onSelectClick(position: Int) {
//        openScratchDialog(position, discountList)
    }

    override fun onApplyOfferClick(offerId: Int, isApply: Boolean) {
        if (isApply) cartModel.offer.add(offerId)
        else cartModel.offer.remove(offerId)
        CommonClassForAPI.getInstance(this).applyClearanceOffer(
            object : DisposableObserver<ClearanceOfferResponse>() {
                override fun onNext(response: ClearanceOfferResponse) {
                    try {
                        Utils.hideProgressDialog()
                        Utils.setToast(applicationContext, response.message)
                        if (response.offer != null && response.offer!!.size > 0) {
                            val offerIds = response.offer!!.map { it.OfferId }
                            cartModel.offer = offerIds.toMutableList()
                        } else {
                            cartModel.offer = ArrayList()
                        }
                        totalDiscount = response.billDiscount
                        discountList?.forEach {
                            it.isSelected = cartModel.offer.contains(it.offerId)
                        }
                        discountAdapter?.notifyDataSetChanged()
                        calculateBillAmount(cartModel)
                        totalPayableAmountShow(totalDiscount)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Utils.hideProgressDialog()
                }

                override fun onComplete() {}
            }, isApply, cartModel
        )
    }


    private fun initialization() {
//        flutterEngine = FlutterEngine(this)
//        flutterEngine!!.getDartExecutor().executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        )
//
//        methodChannel = MethodChannel(
//            flutterEngine!!.getDartExecutor().getBinaryMessenger(),
//            CHANNEL
//        )
//
//        FlutterEngineCache
//            .getInstance()
//            .put(FLUTTER_ENGINE_ID, flutterEngine)
//
//        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL)
//            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
//                if (call.method == "returnToPayment") {
//                    val transactionReqNo = call.argument<String>("transactionReqNo")
//                    val amount = call.argument<Any>("amount")
//                    val mobileNo = call.argument<String>("mobileNo")
//                    val loanAccountId = call.argument<Int>("loanAccountId")
//                    val creditDay = call.argument<Int>("creditDay")
//
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

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        if (EndPointPref.getInstance(applicationContext)
                .getBoolean(EndPointPref.IS_CL_SHOW_COD)
        ) {
            mBinding.llCOD.visibility = View.VISIBLE
        } else {
            mBinding.llCOD.visibility = View.GONE
        }

        maxWalletUseAmount =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.MAX_WALLET_POINT_USED)
        mBinding.toolbarPaymentOption.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.payment_option)
        mBinding.tvApplyCodeT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Apply_coupon_code)
        mBinding.tvOffer.text =
            MyApplication.getInstance().dbHelper.getString(R.string.discount_applied)
        mBinding.tvPromoDetails.text =
            MyApplication.getInstance().dbHelper.getString(R.string.promo_code_details)
        mBinding.tvNextBillDiscountText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.post_bill_text_o)
        mBinding.tvWalletText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Wallet_Points)
        mBinding.txtWlletPnt.text =
            MyApplication.getInstance().dbHelper.getString(R.string._10_dp_1_rs)
        mBinding.tvPriceDetailsT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Price_Details)
        mBinding.tvOrderValueT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_value)
        mBinding.tvDChargesT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Delivery_Charges)
        mBinding.tvDeliveryCharges.text =
            MyApplication.getInstance().dbHelper.getString(R.string.free)
        mBinding.tvTotalAmountT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_amnt)
        mBinding.tvCouponDT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.coupon_discount)
        mBinding.tvFreeItemT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.bill_free_item_added)
        mBinding.tvWalletAmountT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.wallet_amount)
        mBinding.tvAmountPayT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Amount_Payble)
        mBinding.tvPreferOption.text =
            MyApplication.getInstance().dbHelper.getString(R.string.prefer_payment_option)
        mBinding.tvOnlinePayT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.trupay)
        mBinding.tvInstantT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.instant_online_payment)
        mBinding.tvPaymentStatusHdfc.text =
            MyApplication.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.etAmountHdfc.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.etAmountICICIPay.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.name.text = MyApplication.getInstance().dbHelper.getString(R.string.epaylater)
        mBinding.tvDescription.text =
            MyApplication.getInstance().dbHelper.getString(R.string.interest_free_credit_limit)
        mBinding.tvPaymentStatusEpay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.callBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.txt_call)
        mBinding.etAmountEpay.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.tvEarningPntDp.text =
            MyApplication.getInstance().dbHelper.getString(R.string.earn_points)
        mBinding.placeBtn.text =
            MyApplication.getInstance().dbHelper.getString(R.string.place_order)
        mBinding.tvGullakHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.pay_from_gullak)
        mBinding.tvChqB.text = MyApplication.getInstance().dbHelper.getString(R.string.checkBok)
        mBinding.tvOnlineH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.online_h)
        mBinding.tvPayLaterH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.paylater_h)
        mBinding.tvGullakBal.text =
            MyApplication.getInstance().dbHelper.getString(R.string.balance) + " " + SharePrefs.getInstance(
                applicationContext
            ).getString(SharePrefs.GULLAK_BALANCE)
        // direct udhar
        mBinding.tvSkCreditH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.direct_udhar)
        mBinding.tvTcsH.text = MyApplication.getInstance().dbHelper.getString(R.string.tcs_charge)
        mBinding.tvICICIPay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.pay_via_icici_pay)
        mBinding.tvInstantICICIPay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.msg_instant_payment_razorpay)
        if (!TextUtils.isNullOrEmpty(maxWalletUseAmount) && maxWalletUseAmount != "0" && maxWalletUseAmount != "0.0") {
            mBinding.txtMaxWalletPnt.text =
                (MyApplication.getInstance().dbHelper.getString(R.string.text_max_wallet) + DecimalFormat(
                    "##.##"
                ).format(maxWalletUseAmount.toDouble()) + MyApplication.getInstance().dbHelper.getString(
                    R.string.wallet_points
                ))
        } else {
            mBinding.txtMaxWalletPnt.visibility = View.GONE
        }
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)

        mBinding.radioGullak.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioGullak))
        mBinding.radioHdfc.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioHdfc))
        mBinding.radioEpay.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioEpay))
        mBinding.radioCheckbook.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioCheckbook))
        mBinding.radioSkC.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioSkC))
        mBinding.radioPayNowCod.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioPayNowCod))
        mBinding.radioScaleUp.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioScaleUp))
        mBinding.radioICICIPay.setOnCheckedChangeListener(GenericCheckChangeListener(mBinding.radioICICIPay))
        // back btn
        mBinding.toolbarPaymentOption.back.setOnClickListener { onBackPressed() }
        mBinding.rlApplyCoupon.setOnClickListener(this)
        mBinding.rlBillDiscount.setOnClickListener(this)
        mBinding.liWallet.setOnClickListener(this)
        mBinding.callBtn.setOnClickListener(this)
        mBinding.callBtnCb.setOnClickListener(this)
        mBinding.btnCallSkC.setOnClickListener(this)
        // btn order place
        mBinding.placeBtn.setOnClickListener(this)
    }

    private fun setValues() {
        mBinding.pointEt.hint = "0"
        mBinding.tvItemCount.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_item) + " " + list.size
        mBinding.txtWlletPnt.text =
            "10" + " " + MyApplication.getInstance().dbHelper.getString(R.string.total_dp) + " = 1 RS."
        if (deliveryCharges == 0.0) {
            mBinding.tvDeliveryCharges.text = "Free"
        } else {
            mBinding.tvDeliveryCharges.text =
                "+ ₹" + DecimalFormat("##.##").format(deliveryCharges)
        }
    }

    private fun sharedData() {
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        custMobile = SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
        skCode = SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        checkBookminiAmount =
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.IS_ChQBOOKMINI_AMT)
    }

    private fun activePaymentMode() {
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_HDFC_PAYMENT)) {
            mBinding.rlHdfc.visibility = View.VISIBLE
            if (SharePrefs.getInstance(applicationContext).getString(SharePrefs.IS_PAYMENT_GATWAY)
                    .equals("HDFC", ignoreCase = true)
            ) {
                mBinding.rlHdfc.visibility = View.VISIBLE
                mBinding.radioGullak.isChecked = true
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
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_CHECKBOOK_SHOW)) {
            mBinding.rlCheckBook.visibility = View.VISIBLE
        } else {
            mBinding.rlCheckBook.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_DIRECT_UDHAR)) {
            mBinding.rlSkCredit.visibility = View.GONE
        }
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_UDHAAR_ORDER)) {
            mBinding.rlSkCredit.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_ICICI_PAYMENT)) {
            mBinding.rlICICIPay.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.CUST_ACTIVE)) {
            mBinding.rlGullak.visibility = View.GONE
            mBinding.tvOnlineH.visibility = View.GONE
            mBinding.rlHdfc.visibility = View.GONE
            mBinding.rlEpay.visibility = View.GONE
            mBinding.rlCheckBook.visibility = View.GONE
            mBinding.rlSkCredit.visibility = View.GONE
            mBinding.tvPayLaterH.visibility = View.GONE
            mBinding.rlICICIPay.visibility = View.GONE
        }

        if (mBinding.rlICICIPay.visibility == View.VISIBLE) {
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
                    override fun onFailure(errorCode: String?) {}
                    override fun onSuccess(status: String?) {}
                })
        }
    }

    private fun showBillDiscountDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_bill_discount_list, null)
        dialog = BottomSheetDialog(this)
        dialog!!.setContentView(view)
        dialog!!.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        val ivClose = dialog!!.findViewById<AppCompatImageView>(R.id.iv_close)
        val recyclerView = dialog!!.findViewById<RecyclerView>(R.id.recycler_bill_discount)
        val tvPromoT = dialog!!.findViewById<TextView>(R.id.tvPromoT)
        val tvNoOfferT = dialog!!.findViewById<TextView>(R.id.tvNoOfferT)
        tvPromoT!!.text = MyApplication.getInstance().dbHelper.getString(R.string.promotions)
        tvNoOfferT!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = discountAdapter
        discountAdapter!!.notifyDataSetChanged()
        ivClose!!.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
        MyApplication.getInstance().updateAnalytics("discount_dialog_open")
    }

    private fun calculateBillAmount(mShoppingCart: ClearanceShoppingCart) {
        billDiscountId = ""
        totalDiscount = totalDiscount
        cartTotalAmount = mShoppingCart.totalAmount
        billDiscountId = mShoppingCart.offer.toString()
        if (mShoppingCart.offer.isNotEmpty()) {
            mBinding.tvBillDiscount.visibility = View.VISIBLE
            mBinding.tvBillDiscount.text = "₹ " + DecimalFormat("##.##").format(totalDiscount)
            mBinding.tvCouponAmount.text = "-₹ " + DecimalFormat("##.##").format(totalDiscount)
            mBinding.rlBillDiscount.visibility = View.VISIBLE
            mBinding.rlCoupon.visibility = View.VISIBLE
            mBinding.rlApplyCoupon.visibility = View.GONE
            mBinding.tvNextBillDiscount.visibility = View.GONE
            mBinding.tvNextBillDiscountText.visibility = View.GONE
            if (totalDiscount == 0.0) {
                mBinding.rlCoupon.visibility = View.GONE
                mBinding.tvBillDiscount.visibility = View.GONE
            }
        } else {
            mBinding.tvBillDiscount.text = "₹ " + 0
            mBinding.tvCouponAmount.text = "-₹ " + 0
            mBinding.rlBillDiscount.visibility = View.GONE
            mBinding.rlCoupon.visibility = View.GONE
            mBinding.rlApplyCoupon.visibility = View.VISIBLE
            mBinding.tvNextBillDiscount.visibility = View.GONE
            mBinding.tvNextBillDiscountText.visibility = View.GONE
        }
    }

    private fun postOrderBtnData() {
        val isSignUp = SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_SIGN_UP)
        if (isSignUp) {
            orderPlacedPopup()
        } else {
            mBinding.placeBtn.isClickable = true
            SharePrefs.getInstance(applicationContext).putString(SharePrefs.SIGNUPLOC, "PAYMENT")
            val intent = Intent(applicationContext, CheckSignUpActivity::class.java)
            intent.putExtra(Constant.ACTIVATION_TITLE, getString(R.string.payment_option))
            startActivity(intent)
        }
    }

    /**
     * Order place popup
     */
    private fun orderPlacedPopup() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.placeorder_popup)
        dialog.setCancelable(false)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val cancelBtn = dialog.findViewById<TextView>(R.id.cancel_btn)
        val title = dialog.findViewById<TextView>(R.id.pd_title)
        title.text = MyApplication.getInstance().dbHelper.getString(R.string.msg_ask_paymenttype)
        cancelBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)

        okBtn.setOnClickListener {
            dialog.dismiss()
            okBtn.isClickable = false
            otherPaymentMode = true
            callOrderPlaceApi()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
            mBinding.placeBtn.isClickable = true
        }
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun callOrderPlaceApi() {
        showOrderPlaceDialog()
        if (gullakAmount > 0) {
            cartModel.gullakAmount = gullakAmount.toDouble()
        }
        val mItemDetailsList = ArrayList<ItemDetailsModel>()
        // order place api call
        val freeItemId = 0
        val freeItemQty = 0
        val offerCategory = 0
        val freeItemWalletPoint = 0.0
        for (i in list.indices) {
            if (list[i].qty > 0) {
                mItemDetailsList.add(
                    ItemDetailsModel(
                        list[i].id,
                        list[i].qty,
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.COMPANY_ID),
                        0,
                        freeItemWalletPoint,
                        offerCategory,
                        freeItemQty,
                        false,
                        freeItemId,
                        "" + list[i].unitPrice,
                        false
                    )
                )
            }
        }
        val orderCreatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
            Date()
        )
        cartModel.createdDate = orderCreatedDate
        if (eCODAmount > 0) {
            mop = "COD"
            cartModel.mop = mop
        }

        if (utils!!.isNetworkAvailable) {
            commonClassForAPI!!.placeClearanceOrder(orderPlaceObserver, cartModel)
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun onButtonClick() {
        var ePayLaterUsed = false
        var isCheckBook = false
        canPostOrder = false
        val amount =
            (gullakAmount + creditAmount + ePayAmount + hdfcAmount + checkBookAmount + skCreditAmt + eCODAmount + iCICIPayAmount)
        if (ePayAmount > 0) {
            ePayLaterUsed = true
        }
        if (checkBookAmount > 0) {
            isCheckBook = true
        }
        Logger.logD(
            TAG,
            "DiffAmt::-" + "total= " + finalAmount() + " amount " + amount + " gullak " + gullakAmount + " credit " + creditAmount + " ePayAmt " + ePayAmount + " hdfcAmt " + hdfcAmount + " skCredit " + skCreditAmt + " discount " + totalDiscount + " eCODAmount " + eCODAmount + " iCICIPayAmount " + iCICIPayAmount
        )
        if (amount == finalAmount()) {
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
                        MyApplication.getInstance().dbHelper.getString(R.string.epaylater_message) + " " + ePayLimit
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
                        MyApplication.getInstance().dbHelper.getString(R.string.checkbook_message) + " " + checkBookLimit
                    )
                }
            } else {
                mBinding.tvRemainingAmount.text = ""
                canPostOrder = true
            }
        } else {
            val d = (finalAmount() - amount)
            mBinding.tvRemainingAmount.text =
                MyApplication.getInstance().dbHelper.getString(R.string.remaining_amt) + DecimalFormat(
                    "##.##"
                ).format(d)
        }
    }

    private fun finalAmount(): Long {
        return DecimalFormat("##.##").format(Math.round(cartTotalAmount + deliveryCharges + tcsAmount - totalDiscount))
            .toLong()
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
        marketPlaceOrderId = ""
        if (!epaylaterRetry) {
            epaylaterRetry = true
            marketPlaceOrderId = "$orderId($skCode)"
        } else {
            incremental_no++
            marketPlaceOrderId =
                orderId.toString() + "_" + incremental_no + "_" + "(" + skCode + ")"
        }
        var checksum: String? = ""
        var encdata: String? = ""
        val city = SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME)
        val `object` = JSONObject()
        try {
            `object`.put("redirectType", "WEBPAGE")
            `object`.put("marketplaceOrderId", marketPlaceOrderId)
            `object`.put("mCode", SharePrefs.getInstance(this).getString(SharePrefs.M_CODE))
            `object`.put("callbackUrl", Constant.CALLBACK_URL)
            `object`.put("amount", amount * 100)
            `object`.put("currencyCode", "INR")
            `object`.put("date", createdDate) // 2018-03-27T11:06:46Z
            `object`.put("category", SharePrefs.getInstance(this).getString(SharePrefs.CATEGORY))
            val customerObject = JSONObject()
            customerObject.put(
                "firstName",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
            )
            customerObject.put(
                "emailAddress",
                if (SharePrefs.getInstance(applicationContext)
                        .getString(SharePrefs.CUSTOMER_EMAIL) == ""
                ) "user@gmail.com" else SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CUSTOMER_EMAIL)
            )
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
                SharePrefs.getInstance(this).getString(SharePrefs.COMPANY_CONTACT)
            )
            val addressObject = JSONObject()
            addressObject.put(
                "line1", SharePrefs.getInstance(this).getString(SharePrefs.SHIPPING_ADDRESS)
            )
            addressObject.put("city", city)
            addressObject.put("postcode", "452001")
            merchantObject.put("address", addressObject)
            val array = JSONArray()
            for (info in list) {
                if (info.qty > 0) {
                    val jsonObject = JSONObject()
                    jsonObject.put("code", info.id)
                    jsonObject.put("name", info.ItemName)
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
                SharePrefs.getInstance(this).getString(SharePrefs.ENCODED_KEY),
                SharePrefs.getInstance(this).getString(SharePrefs.IV),
                `object`.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intent = Intent(applicationContext, EPayWebViewActivity::class.java)
        intent.putExtra("extra_mcode", SharePrefs.getInstance(this).getString(SharePrefs.M_CODE))
        intent.putExtra("extra_encdata", encdata)
        intent.putExtra("extra_checksum", checksum)
        intent.putExtra(
            "extra_payment_url", SharePrefs.getInstance(this).getString(SharePrefs.E_PAY_LATER_URL)
        )
        startActivityForResult(intent, E_PAY_LATER_REQUEST)
    }

    private fun checkBookPayment(checkBookAmount: Long) {
        gatewayOrderId = orderId.toString()
        val orderRequest = JSONObject()
        try {
            orderRequest.put(ChqbookVypaarKeys.AMOUNT, checkBookAmount)
            orderRequest.put(ChqbookVypaarKeys.ACCOUNT_NO, skCode) //sk code in futher
            orderRequest.put(
                ChqbookVypaarKeys.API_KEY,
                SharePrefs.getInstance(this).getString(SharePrefs.CHECKBOOK_API_KEY)
            )
            orderRequest.put(ChqbookVypaarKeys.MOBILE_NO, custMobile)
            orderRequest.put(ChqbookVypaarKeys.STORE_CODE, 1) //wareHouseId
            orderRequest.put(ChqbookVypaarKeys.PARTNER_TX_NO, orderId)
            orderRequest.put(ChqbookVypaarKeys.ACCOUNT_PROVIDER, "SHOP_KIRANA")
            orderRequest.put(ChqbookVypaarKeys.DEBUG, if (BuildConfig.DEBUG) "true" else "false")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        chqbookInitialize.initiatePayment(orderRequest, object : ChqbookVypaarCallback {
            override fun onSuccess(code: String, transactionId: String) {
                Log.e("code", "code$code")
                Log.e("transactionId", "transactionId$transactionId")
                val jsonResponce = JSONObject()
                try {
                    jsonResponce.put("code", code)
                    jsonResponce.put("transactionId", transactionId)
                    if (!transactionId.equals(chqbookTransactionId, ignoreCase = true)) {
                        chqbookTransactionId = transactionId
                        if (code.equals("200", ignoreCase = true)) {
                            holePaymentSucceedCheck = true
                            insertPaymentStatusAPICall(
                                "Success",
                                code,
                                "chqbook",
                                transactionId,
                                checkBookAmount.toDouble(),
                                orderRequest.toString(),
                                jsonResponce.toString(),
                                ""
                            )
                        } else {
                            holePaymentSucceedCheck = false
                            insertPaymentStatusAPICall(
                                "Failed",
                                code,
                                "chqbook",
                                transactionId,
                                checkBookAmount.toDouble(),
                                orderRequest.toString(),
                                jsonResponce.toString(),
                                ""
                            )
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailed(code: String, error: String) {
                Log.e("onFailed", "onFailed")
                insertPaymentStatusAPICall(
                    "Failed",
                    code,
                    "chqbook",
                    "",
                    checkBookAmount.toDouble(),
                    orderRequest.toString(),
                    "",
                    ""
                )
            }
        })
    }

    private fun creditPayment() {
        if (skCreditRes != null) {
            commonClassForAPI!!.creditPayment(
                skCreditPayObserver, CreditPayment(
                    1,
                    custId,
                    orderId.toLong(),
                    skCreditRes!!.dynamicData!!.accountId,
                    skCreditAmt.toDouble(),
                    ""
                )
            )
        } else {
            Utils.setToast(
                applicationContext, getString(R.string.places_try_again)
            )
        }
    }

    private fun scaleUpPayment() {
        showProgressDialog()
        commonClassForAPI.scaleUpPaymentInitiate(
            scaleUpPayObserver,
            custId,
            orderId,
            scaleUpAmt.toDouble()
        )
    }

    private fun totalPayableAmountShow(totalDiscount: Double) {
        tcsAmount = getTcsAmount(cartTotalAmount - totalDiscount)
        mBinding.tvOrderValue.text = "₹" + DecimalFormat("##.##").format(cartTotalAmount)
        mBinding.tvTotalPrice.text =
            "₹" + DecimalFormat("##.##").format(cartTotalAmount + deliveryCharges)
        mBinding.tvPayableAmt.text =
            "" + DecimalFormat("##.##").format(Math.round(cartTotalAmount + tcsAmount - totalDiscount))
        mBinding.tvTotalAmountPay.text =
            "₹" + DecimalFormat("##.##").format(Math.round(cartTotalAmount + tcsAmount - totalDiscount))
        mBinding.tvTcsAmt.text = "₹" + DecimalFormat("##.##").format(tcsAmount)
        // clear views tvTotalPrice
        mBinding.etAmountHdfc.text = "0"
        mBinding.etAmountEpay.text = "0"
        if (SharePrefs.getInstance(applicationContext).getDouble(SharePrefs.GULLAK_BALANCE) > 0) {
            mBinding.radioGullak.isChecked = true
            gullakAmount = finalAmount()
            mBinding.etAmountGullak.text =
                "" + DecimalFormat("##.##").format(gullakAmount)
        } else {
            mBinding.radioHdfc.isChecked = true
            hdfcAmount = finalAmount()
            mBinding.etAmountHdfc.text =
                "" + DecimalFormat("##.##").format(hdfcAmount)
        }

        if (mBinding.radioPayNowCod.isChecked) {
            eCODAmount = finalAmount()
        }
        mBinding.etAmountCOD.text =
            "" + DecimalFormat("##.##").format(eCODAmount)
    }

    private fun prePaidOrder(OrderAmountGross: Double, popupShow: Boolean) {
        var onlineMsg = ""
        val calPrepaidAmount: Double
        if (prepaidOrderStatus) {
            if (OrderAmountGross >= prepaidOrderModel!!.orderAmount && prepaidOrderModel!!.orderAmount != 0.0) {
                if (prepaidOrderModel!!.cashPercentage != 0.0) {
                    orderAmountFlag = true
                }
                if (prepaidOrderModel!!.onlinePercentage != 0.0) {
                    calAmountOnline = OrderAmountGross * prepaidOrderModel!!.onlinePercentage / 100
                    onlineMsg =
                        "* Minimum Prepaid amount = <font color=#FFFF4500>&#8377; " + DecimalFormat(
                            "##.##"
                        ).format(
                            calAmountOnline.roundToInt()
                        )
                    calPrepaidAmount = calAmountOnline
                    orderAmountFlag = true
                    mBinding.prepaidOrderText.visibility = View.VISIBLE
                    if (popupShow) {
                        showPrepaidOrderPopup(Math.round(calPrepaidAmount))
                    }
                }
                val prepaidOrderMsg = onlineMsg
                mBinding.prepaidOrderText.text = Html.fromHtml(prepaidOrderMsg)
            } else {
                orderAmountFlag = false
                mBinding.prepaidOrderText.visibility = View.GONE
            }
        }
    }

    private fun showPrepaidOrderPopup(calPrepaidAmount: Long) {
        val mView = layoutInflater.inflate(R.layout.prepaid_order_popup, null)
        val customDialog = Dialog(this, R.style.CustomDialog)
        customDialog.setContentView(mView)
        val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
        val description = mView.findViewById<TextView>(R.id.pd_description)
        val title = mView.findViewById<TextView>(R.id.pd_title)
        title.text = MyApplication.getInstance().dbHelper.getString(R.string.prepaid_order)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        description.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.prepaid_order_mag1) + calPrepaidAmount + MyApplication.getInstance().dbHelper.getString(
                R.string.prepaid_order_mag2
            ))
        okBtn.setOnClickListener { v: View? -> customDialog.dismiss() }
        customDialog.show()
    }

    private fun getTcsAmount(total: Double): Double {
        return total * tcsPercent / 100
    }

    private fun insertPaymentStatusAPICall(
        Status: String,
        statusCode: String?,
        paymentFrom: String,
        gatewayTransId: String?,
        truePayTxtnAmt: Double,
        gatewayRequest: String,
        gatewayResponse: String?,
        paymentThrough: String
    ) {
        if (Status.equals("Failed", ignoreCase = true)) {
            mBinding.placeBtn.isClickable = true
        }
        IsBackPaymentFalse = true
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                val paymentGatewayModel = PaymentGatewayModel(
                    orderId,
                    gatewayOrderId,
                    gatewayTransId,
                    truePayTxtnAmt,
                    "INR",
                    Status,
                    paymentCheckedMsg,
                    statusCode,
                    paymentFrom,
                    gatewayRequest,
                    gatewayResponse,
                    paymentThrough,
                    false
                )
                val jsonString = Gson().toJson(paymentGatewayModel)
                Logger.logD(TAG, "InsertPaymentStatus::-$jsonString")
                try {
                    val destr = Aes256.encrypt(
                        jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                            Date()
                        ) + "1201"
                    )
                    println(destr)
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.insertOnlineTransaction(
                        orderStatusInsertDes,
                        PaymentReq(destr),
                        "CL Payment Screen $paymentFrom(Status:$Status)"
                    )
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

    private fun updatePaymentAPICall(
        paymentMode: String,
        isSuccess: Boolean,
        transactionId: String
    ) {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                showProgressDialog()
                val updateOrderPlacedModel =
                    UpdateOrderPlacedModel(
                        orderId,
                        isSuccess,
                        paymentMode,
                        transactionId,
                        paymentThrough,
                        false
                    )
                val jsonString = Gson().toJson(updateOrderPlacedModel)
                Logger.logD(TAG, "jsonStringUpdate::-$jsonString")
                try {
                    val destr = Aes256.encrypt(
                        jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                            Date()
                        ) + "1201"
                    )
                    commonClassForAPI!!.getUpdateOrderPlaced(
                        orderUpdateDes,
                        PaymentReq(destr),
                        "CL Payment Screen $paymentMode(Status:$isSuccess)"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            mBinding.placeBtn.isClickable = true
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // Order placed confirmation popup
    private fun orderConformationPopup() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.popup_order_confirmation)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(false)
        val tvOrderId = dialog.findViewById<TextView>(R.id.tv_order_id)
        val tvTotalAmount = dialog.findViewById<TextView>(R.id.tv_total_amt)
        val tvDialValue = dialog.findViewById<TextView>(R.id.tv_dial_value)
        val tvNotServiceing = dialog.findViewById<TextView>(R.id.tv_not_serviceing)
        val tvNextOrderWallet = dialog.findViewById<TextView>(R.id.tv_next_order_wallet)
        val tvKisanDanAmount = dialog.findViewById<TextView>(R.id.tv_kisan_dan_amt)
        val ivKisanDan = dialog.findViewById<ImageView>(R.id.iv_kisan_dan)
        val tv_congratulation = dialog.findViewById<TextView>(R.id.tv_congratulation)
        val orderMsg = dialog.findViewById<TextView>(R.id.orderMsg)
        val tvSelectDateH = dialog.findViewById<TextView>(R.id.tvSelectDateH)
        val tvETAH = dialog.findViewById<TextView>(R.id.tvEtaH)
        val tvETA = dialog.findViewById<TextView>(R.id.tvETA)
        val tvDelayH = dialog.findViewById<TextView>(R.id.tvDelayH)
        val cgDate = dialog.findViewById<FlexboxLayout>(R.id.cgDate)
        val continueShopping = dialog.findViewById<Button>(R.id.continueShopping)
        val tvDeliverByH = dialog.findViewById<TextView>(R.id.tvDeliverByH)
        val tvHeavyDeliveryLoadMsg = dialog.findViewById<TextView>(R.id.tvHeavyDeliveryLoadMsg)
        val btnChangeDate = dialog.findViewById<TextView>(R.id.btnChangeDate)

        tv_congratulation!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Congratulation)
        orderMsg!!.text = MyApplication.getInstance().dbHelper.getString(R.string.order_done_msg)
        continueShopping!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_continue_shopping)
//        tvETA!!.text = Utils.getDateMonthFormat(
//            orderPlacedNewResponse!!.orderMaster.getExpectedETADate()
//        )
        tvDeliverByH!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_delivered_by_shopkirana)
        tvHeavyDeliveryLoadMsg!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.msg_heavy_load)
//        if (orderPlacedNewResponse!!.orderMaster.isDefaultDeliveryChange) {
//            tvHeavyDeliveryLoadMsg.visibility = View.VISIBLE
//        }
//        if (earnWalletPoint > 0) {
//            tvNextOrderWallet!!.visibility = View.VISIBLE
//            if (LocaleHelper.getLanguage(applicationContext) == "en") {
//                tvNextOrderWallet.text =
//                    earnWalletPoint.toString() + MyApplication.getInstance().dbHelper.getString(R.string.wallet_added_after_order_text)
//            } else {
//                tvNextOrderWallet.text =
//                    (MyApplication.getInstance().dbHelper.getString(R.string.wallet_added_after_order_text) + earnWalletPoint + MyApplication.getInstance().dbHelper.getString(
//                        R.string.wallet_added_after_order_text2
//                    ))
//            }
//        }
//        if (kisanDanAmount > 0) {
//            tvKisanDanAmount!!.visibility = View.VISIBLE
//            val kisanDaanAmount =
//                (MyApplication.getInstance().dbHelper.getString(R.string.kisan_daan_text) + "<font color=#000000>&#8377; " + DecimalFormat(
//                    "##.##"
//                ).format(
//                    kisanDanAmount
//                ))
//            tvKisanDanAmount.text = Html.fromHtml(kisanDaanAmount)
//            ivKisanDan!!.setImageResource(R.drawable.kisan_daan_icon)
//        } else {
//            tvKisanDanAmount!!.visibility = View.VISIBLE
//            val kisanDaanAmount =
//                (MyApplication.getInstance().dbHelper.getString(R.string.no_kisan_daan_text) + "<font color=#000000>&#8377; " + DecimalFormat(
//                    "##.##"
//                ).format(
//                    kisanDanAmount
//                ))
//            tvKisanDanAmount.text = Html.fromHtml(kisanDaanAmount)
//            ivKisanDan!!.setImageResource(R.drawable.no_kisan_daan_icon)
//        }
        val orderIdText =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_order_id) + "<font color=#000000>" + orderId
        tvOrderId!!.text = Html.fromHtml(orderIdText)
        val totalAmountText =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_order_amount) + "<font color=#000000>" + mBinding.tvPayableAmt.text.toString()
        tvTotalAmount!!.text = Html.fromHtml(totalAmountText)

        continueShopping.text =
            MyApplication.getInstance().dbHelper.getString(R.string.continue_shopping)
        tvDialValue!!.visibility = View.GONE
        btnChangeDate!!.visibility = View.GONE
        continueShopping.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(
                    applicationContext,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            Utils.rightTransaction(this)
        }

        // delivery date
        tvETAH?.visibility = View.GONE
        tvETA?.visibility = View.GONE
        cgDate!!.visibility = View.GONE
        tvSelectDateH!!.visibility = View.GONE
        tvDelayH?.visibility = View.GONE

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        dialog.show()
        MyApplication.getInstance().updateAnalytics("order_confirm_dialog")
    }


    private fun convertStandardJSONString(data_json: String): String {
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

    private fun showOrderPlaceDialog() {
        if (customDialogOrderPlacedTime != null) {
            customDialogOrderPlacedTime = null
        }
        customDialogOrderPlacedTime = Dialog(this, R.style.CustomDialog)
        customDialogOrderPlacedTime!!.setContentView(R.layout.progress_dialog)
        if (!customDialogOrderPlacedTime!!.isShowing && !isFinishing) {
            customDialogOrderPlacedTime!!.show()
            customDialogOrderPlacedTime!!.setCancelable(false)
        }
        customDialogOrderPlacedTime!!.setOnKeyListener { arg0: DialogInterface?, keyCode: Int, event: KeyEvent? ->
            // Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                exitAlertDialog();
            }
            true
        }
    }

    private fun hideOrderPlaceDialog() {
        if (customDialogOrderPlacedTime != null && customDialogOrderPlacedTime!!.isShowing) {
            customDialogOrderPlacedTime!!.dismiss()
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

    private fun goToHome(orderStatus: String) {
        if (orderId != 0 && orderStatus.equals("success", ignoreCase = true)) {
            orderConformationPopup()
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.order_error_msg)
            )
            startActivity(
                Intent(
                    applicationContext,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            Utils.rightTransaction(this)
        }
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
            deviceIpWiFiData()
        } else {
            deviceIpMobileData()
        }
    }

    private fun deviceIpMobileData(): String {
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

    private fun deviceIpWiFiData(): String {
        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }

    private fun exitAlertDialog() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.alert_for_exit_screen))
        builder1.setMessage(MyApplication.getInstance().dbHelper.getString(R.string.exit_payment_screen_msg))
        builder1.setCancelable(true)
        builder1.setPositiveButton("Yes") { dialog: DialogInterface, id: Int -> dialog.cancel() }
        builder1.setNegativeButton("No") { dialog: DialogInterface, id: Int -> dialog.cancel() }
        val alert11 = builder1.create()
        if (!alert11.isShowing) {
            alert11.show()
        }
    }


    inner class GenericCheckChangeListener(val view: View) :
        CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            when (view.id) {
                R.id.radioGullak -> if (isChecked) {
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false

                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountICICIPay.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountGullak.text = "" + finalAmount()
                    hdfcAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    ePayAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    gullakAmount = finalAmount()
                }

                R.id.radio_hdfc -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioICICIPay.isChecked = false

                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountICICIPay.text = "0"
                    mBinding.etAmountHdfc.text = "" + finalAmount()
                    gullakAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    ePayAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    hdfcAmount = finalAmount()
                }

                R.id.radio_epay -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioICICIPay.isChecked = false

                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountICICIPay.text = "0"

                    mBinding.etAmountSkC.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    ePayAmount = finalAmount()
                }

                R.id.radio_checkbook -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioICICIPay.isChecked = false

                    mBinding.etAmountGullak.text = ""
                    mBinding.etAmountHdfc.text = ""
                    mBinding.etAmountEpay.text = ""
                    mBinding.etAmountSkC.text = ""
                    mBinding.etAmountCOD.text = ""
                    mBinding.etAmountICICIPay.text = "0"
                    mBinding.etAmountCb.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    ePayAmount = 0
                    creditAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    checkBookAmount = finalAmount()
                }

                R.id.radioSkC -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioICICIPay.isChecked = false

                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountICICIPay.text = "0"

                    mBinding.etAmountSkC.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    ePayAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    skCreditAmt = finalAmount()
                }

                R.id.radioPayNowCod -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountICICIPay.text = "0"
                    mBinding.etAmountCOD.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    ePayAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    skCreditAmt = 0
                    iCICIPayAmount = 0
                    eCODAmount = finalAmount()
                }

                R.id.radioScaleUp -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountICICIPay.text = "0"
                    mBinding.etAmountSu.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    ePayAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    iCICIPayAmount = 0
                    scaleUpAmt = finalAmount()
                }

                R.id.radioICICIPay -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioPayNowCod.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.etAmountGullak.text = "0"
                    mBinding.etAmountHdfc.text = "0"
                    mBinding.etAmountEpay.text = "0"
                    mBinding.etAmountCb.text = "0"
                    mBinding.etAmountSkC.text = "0"
                    mBinding.etAmountCOD.text = "0"
                    mBinding.etAmountSu.text = "0"
                    mBinding.etAmountICICIPay.text = "" + finalAmount()
                    gullakAmount = 0
                    hdfcAmount = 0
                    ePayAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    skCreditAmt = 0
                    eCODAmount = 0
                    scaleUpAmt = 0
                    iCICIPayAmount = finalAmount()
                }
            }
        }
    }


    // TCS response
    private var tcsObserver: DisposableObserver<Double> = object : DisposableObserver<Double>() {
        override fun onNext(double: Double) {
            try {
                tcsPercent = double
                totalPayableAmountShow(totalDiscount)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onComplete() {}
    }

    // Getting all available bill discount list
    private var billDiscountObserver: DisposableObserver<BillDiscountListResponse> =
        object : DisposableObserver<BillDiscountListResponse>() {
            override fun onNext(response: BillDiscountListResponse) {
                mBinding.placeBtn.isClickable = true
                isOfferApiCalled = true
                hideProgressDialog()
                if (response.isStatus) {
                    if (response.billDiscountList != null && response.billDiscountList.size > 0) {
                        discountList = response.billDiscountList

                        discountAdapter = ClearanceDiscountAdapter(
                            this@ClearancePaymentActivity,
                            discountList,
                            this@ClearancePaymentActivity
                        )
                        discountAdapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
                dispose()
                mBinding.placeBtn.isClickable = true
            }

            override fun onComplete() {}
        }

    private var prepaidOrder: DisposableObserver<PrepaidOrder> =
        object : DisposableObserver<PrepaidOrder>() {
            override fun onNext(response: PrepaidOrder) {
                if (response.isStatus) {
                    prepaidOrderStatus = true
                    prepaidOrderModel = response.prepaidOrderModel
                    prePaidOrder(cartTotalAmount + deliveryCharges, true)
                } else {
                    prepaidOrderStatus = false
                }
                Utils.hideProgressDialog()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // Order placed response
    private var orderPlaceObserver: DisposableObserver<ClearanceOrderResponse> =
        object : DisposableObserver<ClearanceOrderResponse>() {
            override fun onNext(response: ClearanceOrderResponse) {
                try {
                    hideOrderPlaceDialog()
                    orderPlacedNewResponse = response
                    if (response.isSuccess) {
                        // disable walletPoint after order - devendra
                        mBinding.pointEt.isFocusable = false
                        SharePrefs.getInstance(applicationContext)
                            .putBoolean(SharePrefs.IS_GULLAK_BAL, false)
                        // clear cart
                        createdDate = response.createdDate!!
                        orderId = response.orderId
                        if (hdfcAmount > 0) {
                            paymentThrough = "hdfc"
                            generateRsaKey()
                        } else if (checkBookAmount > 0) {
                            paymentThrough = "checkBook"
                            checkBookPayment(checkBookAmount)
                        } else if (ePayAmount > 0) {
                            paymentThrough = "ePayLater"
                            ePayLater(ePayAmount.toDouble())
                        } else if (skCreditAmt > 0) {
                            paymentThrough = "DirectUdhar"
                            if (skCreditRes!!.dynamicData!!.amount > skCreditAmt) {
                                creditPayment()
                            } else {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                                )
                            }
                        } else if (scaleUpAmt > 0) {
                            paymentThrough = "scaleUp"
                            if (scaleUpLimit > scaleUpAmt) {
                                creditPayment()
                            } else {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                                )
                            }
                        } else if (iCICIPayAmount > 0) {
                            paymentThrough = "icici"
                            callICICIPay()
                        } else {
                            orderConformationPopup()
                        }
                    } else {
                        Utils.setToast(applicationContext, "" + response.message)
                        mBinding.placeBtn.isClickable = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                this.dispose()
                hideOrderPlaceDialog()
                goToHome("exception")
            }

            override fun onComplete() {
                hideOrderPlaceDialog()
            }
        }

    private fun generateRsaKey() {
        commonClassForAPI.fetchRSAKey(
            rsaObserver,
            orderId.toString(),
            hdfcAmount.toString(),
            false,
            "Clearance"
        )
    }

    // Order status insert response
    private var orderStatusInsertDes: DisposableObserver<Boolean> =
        object : DisposableObserver<Boolean>() {
            override fun onNext(orderValue: Boolean) {
                try {
                    Utils.hideProgressDialog()
                    if (holePaymentSucceedCheck) {
                        updatePaymentAPICall("Online", true, transactionId)
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

    // update Order status
    var orderUpdateDes: DisposableObserver<Boolean> = object : DisposableObserver<Boolean>() {
        override fun onNext(orderValue: Boolean) {
            if (utils!!.isNetworkAvailable) {
                try {
                    hideProgressDialog()
                    if (orderValue) {
                        if (!IsSuccess) {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.payment_cancel)
                            )
                            startActivity(
                                Intent(
                                    applicationContext,
                                    HomeActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            Utils.rightTransaction(this@ClearancePaymentActivity)
                        } else {
                            if (otherPaymentMode) {
                                goToHome("success")
                            } else {
                                onBackPressed()
                            }
                        }
                    } else {
                        mBinding.placeBtn.isClickable = true
                        Utils.setToast(
                            applicationContext, "something went to wrong"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                goToHome("internet issue")
            }
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            hideProgressDialog()
            goToHome("exception")
        }

        override fun onComplete() {
            hideProgressDialog()
        }
    }

    // Order placed response
    var ePayLaterOrderConfirm: DisposableObserver<EpayLaterResponse> =
        object : DisposableObserver<EpayLaterResponse>() {
            override fun onNext(response: EpayLaterResponse) {
                hideProgressDialog()
                var flag = false
                if (utils!!.isNetworkAvailable) {
                    try {
                        if (response != null) {
                            if (response.status.equals(
                                    "confirmed", ignoreCase = true
                                ) || response.status.equals("delivered", ignoreCase = true)
                            ) {
                                flag = false
                                insertPaymentStatusAPICall(
                                    "Success",
                                    ePaystatusCode,
                                    "ePaylater",
                                    ePayMarketplaceOrderId,
                                    ePayResAmt,
                                    ePaylaterRequest,
                                    ePayResponseObj,
                                    ""
                                )
                            } else {
                                flag = true
                            }
                        } else {
                            flag = true
                            Toast.makeText(applicationContext, "Json null", Toast.LENGTH_SHORT)
                                .show()
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
                        "epaylater txtn amount not confirm",
                        "ePaylater",
                        ePayMarketplaceOrderId,
                        ePayResAmt,
                        ePaylaterRequest,
                        ePayResponseObj,
                        ""
                    )
                    holePaymentSucceedCheck = false
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                dispose()
                Utils.hideProgressDialog()
                insertPaymentStatusAPICall(
                    "Failed",
                    "epaylater txtn amount not confirm",
                    "ePaylater",
                    ePayMarketplaceOrderId,
                    ePayResAmt,
                    ePaylaterRequest,
                    ePayResponseObj,
                    ""
                )
                holePaymentSucceedCheck = false
            }

            override fun onComplete() {}
        }

    // ePayLater check limit response
    private var ePayLaterCheckLimit: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(response: JsonObject) {
                hideProgressDialog()
                try {
                    if (response.size() > 0) {
                        // debug mode add by devendra
                        ePayLimit = if (BuildConfig.DEBUG) {
                            response["availableCredit"].asDouble
                        } else {
                            response["availableCredit"].asDouble
                        }
                        mBinding.tvLimit.text =
                            "₹ " + DecimalFormat("##.##").format(ePayLimit) + "/-"
                    } else {
                        Toast.makeText(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.net_connection_off),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (ePayLimit == 0.0) {
                        mBinding.tvLimit.textSize = 15f
                        mBinding.tvLimit.text =
                            ("( " + MyApplication.getInstance().dbHelper.getString(R.string.contact_on) + SharePrefs.getInstance(
                                applicationContext
                            ).getString(SharePrefs.COMPANY_CONTACT) + ")")
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

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
                dispose()
                mBinding.tvLimit.textSize = 14f
                mBinding.tvLimit.text =
                    "(" + MyApplication.getInstance().dbHelper.getString(R.string.contact_on) + " +91" + SharePrefs.getInstance(
                        applicationContext
                    ).getString(SharePrefs.COMPANY_CONTACT) + ")"
                mBinding.etAmountEpay.visibility = View.GONE
                mBinding.callBtn.visibility = View.VISIBLE
                mBinding.radioEpay.visibility = View.GONE
                mBinding.rlEpay.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE
                ) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
            }

            override fun onComplete() {}
        }

    // CheckBook check limit response
    private var checkBookCheckLimit: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(response: JsonObject) {
                hideProgressDialog()
                if (response != null) {
                    val checkBookCustomerLimit =
                        Gson().fromJson(response.toString(), CheckBookData::class.java)
                    checkBookLimit = if (BuildConfig.DEBUG) {
                        checkBookCustomerLimit.checkBookCustomerLimit.getCheckBookCustomerResult()
                            .getAvailable_amount().toDouble()
                    } else {
                        checkBookCustomerLimit.checkBookCustomerLimit.getCheckBookCustomerResult()
                            .getAvailable_amount().toDouble()
                    }
                    mBinding.tvLimitCb.text = "₹ $checkBookLimit/-"
                    if (checkBookLimit == 0.0) {
                        mBinding.tvLimitCb.textSize = 15f
                        mBinding.tvLimitCb.text =
                            "( " + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                                R.string.checkbook_contact
                            ) + ")"
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

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
                mBinding.tvLimitCb.textSize = 14f
                mBinding.tvLimitCb.text =
                    "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountCb.visibility = View.GONE
                mBinding.callBtnCb.visibility = View.VISIBLE
                mBinding.radioCheckbook.visibility = View.GONE
                mBinding.rlCheckBook.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE
                ) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
            }

            override fun onComplete() {}
        }

    // sk check limit response
    private var creditLimitObserver: DisposableObserver<CreditLimit> =
        object : DisposableObserver<CreditLimit>() {
            override fun onNext(response: CreditLimit) {
                hideProgressDialog()
                if (response.isSuccess && response.dynamicData != null && response.dynamicData!!.amount > 0
                ) {
                    skCreditRes = response
                    mBinding.tvLimitSk.text = "₹ " + response.dynamicData!!.amount + "/-"
                    mBinding.radioSkC.visibility = View.VISIBLE
                    mBinding.etAmountSkC.visibility = View.VISIBLE
                    mBinding.btnCallSkC.visibility = View.GONE
                } else {
                    mBinding.radioSkC.visibility = View.GONE
                    mBinding.etAmountSkC.visibility = View.GONE
                    mBinding.btnCallSkC.visibility = View.VISIBLE
                    mBinding.tvLimitSk.textSize = 14f
                    mBinding.tvLimitSk.text =
                        "( " + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                            R.string.checkbook_contact
                        ) + ")"
                    mBinding.tvLimitSk.autoLinkMask = Linkify.PHONE_NUMBERS
                }
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE
                ) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
                mBinding.tvLimitSk.textSize = 15f
                mBinding.tvLimitSk.text =
                    "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountSkC.visibility = View.GONE
                mBinding.btnCallSkC.visibility = View.VISIBLE
                mBinding.radioSkC.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE
                ) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
            }

            override fun onComplete() {}
        }

    // sk credit payment response
    private var skCreditPayObserver: DisposableObserver<CreditLimit> =
        object : DisposableObserver<CreditLimit>() {
            override fun onNext(response: CreditLimit) {
                hideProgressDialog()
                if (response.isSuccess) {
                    val url = response.message
                    startActivityForResult(
                        Intent(applicationContext, DirectUdharActivity::class.java).putExtra(
                            "url",
                            url
                        ), SK_CREDIT
                    )
                } else {
                    mBinding.placeBtn.isClickable = true
                    Utils.setToast(
                        applicationContext, response.message
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // scaleUp check limit response
    private var scaleUpLimitObserver: DisposableObserver<CreditLimit> =
        object : DisposableObserver<CreditLimit>() {
            override fun onNext(response: CreditLimit) {
                hideProgressDialog()
                scaleUpLimit = response.creditLimit
                mBinding.tvLimitSu.setText("₹ " + DecimalFormat("##.##").format(scaleUpLimit) + "/-")
                if (response.isBlock || scaleUpLimit <= 0) {
                    mBinding.tvLimitSu.textSize = 15f
                    mBinding.tvLimitSu.text =
                        "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                            R.string.checkbook_contact
                        ) + ")"
                    mBinding.etAmountSu.visibility = View.GONE
                    mBinding.btnCallSu.visibility = View.VISIBLE
                    mBinding.radioScaleUp.visibility = View.GONE
                    if (response.isBlock) mBinding.tvLimitSu.text = response.message
                } else {
                    mBinding.etAmountSu.visibility = View.VISIBLE
                    mBinding.btnCallSu.visibility = View.GONE
                    mBinding.radioScaleUp.visibility = View.VISIBLE
                    if (response.isBlock) {
                        mBinding.tvLimitSu.text = response.message
                        mBinding.radioScaleUp.visibility = View.INVISIBLE
                        mBinding.etAmountSu.visibility = View.INVISIBLE
                        mBinding.btnCallSu.visibility = View.VISIBLE
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
                mBinding.etAmountSu.visibility = View.GONE
                mBinding.btnCallSu.visibility = View.VISIBLE
                mBinding.radioScaleUp.visibility = View.GONE
            }

            override fun onComplete() {}
        }

    // sk credit payment response
    private var scaleUpPayObserver: DisposableObserver<ScaleUpResponse> =
        object : DisposableObserver<ScaleUpResponse>() {
            override fun onNext(response: ScaleUpResponse) {
                hideProgressDialog()
                if (response.status) {
                    transactionId = response.transactionId
                    val baseUrl: String = response.BaiseUrl
                    val json = JSONObject()
                    try {
                        json.put("isPayNow", true);
                        json.put("transactionId", transactionId);
                        json.put("baseUrl", baseUrl);
                        Log.e(TAG, "onNext transactionId: $transactionId")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    methodChannel?.invokeMethod("ScaleUP", json.toString())

                    if (EndPointPref.getInstance(applicationContext)
                            .getBoolean(EndPointPref.IS_SCALE_UP_SDK)
                    ) {
                        startActivity(
                            FlutterActivity
                                .withCachedEngine(FLUTTER_ENGINE_ID)
                                .build(applicationContext)
                        )
                    } else {
                        TextUtils.isNullOrEmpty(response.url)
                        startActivityForResult(
                            Intent(
                                applicationContext,
                                ScaleUpActivity::class.java
                            )
                                .putExtra("url", response.url), SCALEUP
                        )
                    }

                } else {
                    mBinding.placeBtn.isClickable = true
                    Utils.setToast(
                        applicationContext, response.message
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                hideProgressDialog()
            }

            override fun onComplete() {}
        }

    // update delivery date
    private var clearanceOrderValidPayObserver: DisposableObserver<Boolean> =
        object : DisposableObserver<Boolean>() {
            override fun onNext(o: Boolean) {
                try {
                    Utils.hideProgressDialog()
                    if (canPostOrder) {
                        if (hdfcAmount > 0) {
                            generateRsaKey()
                        } else if (checkBookAmount > 0) {
                            checkBookPayment(checkBookAmount)
                        } else if (ePayAmount > 0) {
                            ePayLater(ePayAmount.toDouble())
                        } else if (skCreditAmt > 0) {
                            if (skCreditRes!!.dynamicData!!.amount > skCreditAmt) {
                                creditPayment()
                            } else {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                                )
                            }
                        } else if (iCICIPayAmount > 0) {
                            callICICIPay()
                        } else {
                            val gullakBal = SharePrefs.getInstance(applicationContext)
                                .getDouble(SharePrefs.GULLAK_BALANCE)
                            if (gullakBal >= gullakAmount) {
                                holePaymentSucceedCheck = true
                                insertPaymentStatusAPICall(
                                    "Success",
                                    "200",
                                    "Gullak",
                                    "" + orderId,
                                    gullakAmount.toDouble(),
                                    "Gullak",
                                    "Gullak",
                                    "Gullak"
                                )
                            } else startActivityForResult(
                                Intent(applicationContext, AddPaymentActivity::class.java).putExtra(
                                    "amount",
                                    gullakAmount - gullakBal
                                ).putExtra("screen", 2), 9
                            )
                        }
                    } else {
                        mBinding.placeBtn.isClickable = true
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.amount_does_not_match_with_order_amount)
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

            override fun onComplete() {}
        }

    private var rsaObserver: DisposableObserver<String> =
        object : DisposableObserver<String>() {
            override fun onNext(response: String) {
                try {
                    Utils.hideProgressDialog()
                    callHDFC(orderId.toString(), response, hdfcAmount.toString())
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

    private val netConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var intent = intent
            val status = Utils.getConnectivityStatusString(context)
            intent = Intent("netStatus")
            intent.putExtra("status", status)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            if (!status) {
                startActivityForResult(
                    Intent(applicationContext, NoInternetActivity::class.java), 222
                )
            }
        }
    }

    private fun callICICIPay() {
        val rnd = Random()
        gatewayOrderId = (orderId + rnd.nextInt(900768000)).toString()
        val secureToken: String? = getSecureToken()
        val intent = Intent(applicationContext, PaymentOptionsActivity::class.java)
        intent.putExtra("SecureToken", secureToken)
        intent.putExtra("MerchantID", iCICIMerchantId)
        // intent.putExtra("aggregatorID", "J_34407")
        intent.putExtra("Amount", iCICIFinaPayAmount)
        intent.putExtra("MerchantTxnNo", gatewayOrderId)
        intent.putExtra("invoiceNo", gatewayOrderId)
        intent.putExtra("CurrencyCode", 356)
        intent.putExtra(
            "CustomerEmailID",
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_EMAIL)
        )
        intent.putExtra("allowDisablePaymentMode", "CARD")
        intent.putExtra("CustomerID", custId)
        intent.putExtra("addlParam1", "7304828261")
        intent.putExtra("addlParam2", "7304828262")
        iciciPayRequest = intent.extras.toString()

        println("Request>>>>>>>>>" + iciciPayRequest)

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
                            if (responseCode == "0000" || responseCode == "000") {
                                //Transaction success
                                gatewayOrderId = merchantTxnNo!!
                                holePaymentSucceedCheck = true
                                insertPaymentStatusAPICall(
                                    "Success",
                                    "200",
                                    "icici",
                                    txnID,
                                    iCICIPayAmount.toDouble(),
                                    iciciPayRequest!!,
                                    data.extras.toString(),
                                    "icici"
                                )
                            } else {
                                iCICIPaymentFailed(responseCode.toString())
                            }
                        }
                    }/*else if (resultCode == RESULT_CANCELED) {
                        iCICIPaymentFailed("0")
                    }*/ else {
                        callICICIPaymentResult()
                    }

                }

                override fun onPaymentResponse1(resultCode: Int, data: Intent?) {}
            })
    }

    fun callICICIPaymentResult() {
        val value = iCICIMerchantId + gatewayOrderId + gatewayOrderId + "STATUS"
        val secureHash = generateHMAC(value)
        viewModel.getICICIPaymentCheck(
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.ICICI_RESULT_URL),
            iCICIMerchantId,
            gatewayOrderId,
            gatewayOrderId,
            "STATUS",
            secureHash!!
        )
    }

    private fun iCICIPaymentFailed(status: String) {
        mBinding!!.placeBtn.isClickable = true
        insertPaymentStatusAPICall(
            "Failed", status, "icici", "", iCICIPayAmount.toDouble(), iciciPayRequest!!, "", "icici"
        )
    }

    private fun getSecureToken(): String? {
        val df = DecimalFormat()
        df.minimumFractionDigits = 2
        val f = iCICIPayAmount.toFloat()
        df.format(f)
        iCICIFinaPayAmount = String.format("%.2f", f)
        val value = iCICIFinaPayAmount + 356 + iCICIMerchantId + gatewayOrderId
        println("TokeString==$`value`")
        // 0d50a3f9aec3492cba25fef9b3c1a3c1
        return generateHMAC(value)
    }

    fun generateHMAC(message: String): String? {
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

    private fun handleICICITransactionResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog()
            }

            is Response.Success -> {
                it.data?.let {
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
                                gatewayOrderId = merchantTxnNo!!
                                holePaymentSucceedCheck = true
                                println("txnRespDescription>>$txnRespDescription<<txnResponseCode>>>$txnResponseCode<<txnID>>>$txnID")
                                insertPaymentStatusAPICall(
                                    "Success",
                                    "200",
                                    "icici",
                                    txnID,
                                    iCICIPayAmount.toDouble(),
                                    iciciPayRequest!!,
                                    it.toString(),
                                    "icici"
                                )
                            } else {
                                iCICIPaymentFailed(txnResponseCode)
                            }
                        } else {
                            iCICIPaymentFailed(responseCode.toString())
                        }
                    } else {
                        iCICIPaymentFailed(responseCode)
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }
}