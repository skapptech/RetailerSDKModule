package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.format.Formatter
import android.text.util.Linkify
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderMaster
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderPlacedNewResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.WalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPaymentOptionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EPayWebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater.EpayLaterEncryptDecryptUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnApplyOfferClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnSelectClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.CheckSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DialWheelActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.HDFCActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ScaleUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.BillDiscountOfferAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.AddPaymentActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.RtgsInfoActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CheckBookData
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ItemDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.OrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentGatewayModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.UpdateOrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.scratchCard.ScratchCard
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Aes256
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AvenuesParams
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.OfferCheck
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.chqbook.vypaar.ChqbookVypaarCallback
import com.chqbook.vypaar.ChqbookVypaarClient
import com.chqbook.vypaar.ChqbookVypaarKeys
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.payphi.customersdk.utils.HmacUtility
import com.payphi.customersdk.views.Application
import com.payphi.customersdk.views.PayPhiSdk
import com.payphi.customersdk.views.PaymentOptionsActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
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
import java.util.Random
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PaymentOptionActivity : AppCompatActivity(), View.OnClickListener, OnSelectClick,
    OnApplyOfferClick, PaymentResultWithDataListener {
    private val TAG = this.javaClass.simpleName
    private val E_PAY_LATER_REQUEST = 1563
    private val HDFC_REQUEST = 999
    private val SK_CREDIT = 888
    private val SCALEUP = 777

    private lateinit var mBinding: ActivityPaymentOptionBinding
    private lateinit var viewModel: PaymentViewModel
    private var utils: Utils? = null
    private var customProgressDialog: Dialog? = null
    private var customDialogOrderPlacedTime: Dialog? = null
    private var rewardPoints: String? = "0"
    private var px = 0.0
    private var rx = 0.0
    private var enterRewardPoint = 0.0
    private var amountToReduct = 0.0
    private var grossTotalAmount = 0.0
    private var cartTotalAmount = 0.0
    private var totalDiscount = 0.0
    private var ePayLimit = 0.0
    private var deliveryCharges = 0.0
    private var ePayResAmt = 0.0
    private var checkBookLimit = 0.0
    private var scaleUpLimit = 0.0
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
    private var iciciPayRequest: String? = null
    private var otherPaymentMode = false
    private var canPostOrder = false
    private var ePayLaterLimit = true
    private var isChqbookLimit = true
    private var IsSuccess = true
    private var holePaymentSucceedCheck = false
    private var IsBackPaymentFalse = false
    private var epaylaterRetry = false
    private var orderAmountFlag = false
    private var PrepaidOrderStatus = false
    private var isOfferApiCalled = false
    private var cashAmount: Long = 0
    private var gullakAmount: Long = 0
    private var ePayAmount: Long = 0
    private var creditAmount: Long = 0
    private var hdfcAmount: Long = 0
    private var razorpayAmount: Long = 0
    private var iCICIPayAmount: Long = 0
    private var iCICIFinaPayAmount = ""
    private var availableCODLimit: Long = -1
    private var checkBookAmount: Long = 0
    private var skCreditAmt: Long = 0
    private var rtgsAmt: Long = 0
    private var scaleUpAmt: Long = 0
    private var lang = ""
    private var maxWalletUseAmount = ""
    private var marketPlaceOrderId = ""
    private var transactionId: String? = null
    private var paymentThrough: String? = null
    private var incremental_no = 0
    private var calAmountCOD = 0.0
    private var calAmountOnline = 0.0
    private var tcsAmount = 0.0
    private var lat = 0.0
    private var lng = 0.0
    private var codLimit = 0.0
    private var orderPlacedNewResponse: OrderPlacedNewResponse? = null

    // Bill Discount params
    private var discountList: ArrayList<BillDiscountModel>? = null
    private var dialog: BottomSheetDialog? = null
    private var discountAdapter: BillDiscountOfferAdapter? = null
    private var mShoppingCart: ShopingCartItemDetailsResponse? = null
    private var prepaidOrderModel: PrepaidOrderModel? = null
    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null
    private var chqbookInitialize: ChqbookVypaarClient? = null
    private var skCreditRes: CreditLimit? = null
    private var chqbookTransactionId = ""
    private var isUdharOrderOverDue = false
    private val FLUTTER_ENGINE_ID = "my_flutter_engine"
    private val CHANNEL = "com.ScaleUP"
    private var flutterEngine: FlutterEngine? = null
    private var methodChannel: MethodChannel? = null
    private var iCICIMerchantId = ""
    private var updateCashStatus = false
    private var otherCharges = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
        )
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment_option)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this, PaymentViewModelFactory(application, appRepository)
        )[PaymentViewModel::class.java]
        if (intent.extras != null) {
            mShoppingCart =
                intent.extras!!.getSerializable("SHOPING_CART") as ShopingCartItemDetailsResponse?
        } else {
            Log.e("null", "null")
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        calculateBillAmount(mShoppingCart)
        chqbookInitialize = ChqbookVypaarClient.getInstance()
        chqbookInitialize!!.init(this)
        // initialize views
        initialization()
        // Shared Data
        sharedData()
        activePaymentMode()
        // set Pending Order data
        deliveryCharges = mShoppingCart!!.deliveryCharges
        grossTotalAmount = mShoppingCart!!.grossTotalAmt
        cartTotalAmount = mShoppingCart!!.cartTotalAmt
        otherCharges =
            (if (mShoppingCart!!.isConvenienceFee) mShoppingCart!!.convenienceFees else 0.0) +
                    (if (mShoppingCart!!.isPlatformFee) mShoppingCart!!.platformFeeFinalAmt else 0.0) +
//                    (if (mShoppingCart!!.isVisitCharges) mShoppingCart!!.salesPersonVisitAmt else 0.0) +
                    (if (mShoppingCart!!.isHikeCharge) mShoppingCart!!.hikeCharges else 0.0) +
                    (if (mShoppingCart!!.isOrderProcessCharge) mShoppingCart!!.orderProcessAmt else 0.0) +
                    (if (mShoppingCart!!.isSmallCartCharge) mShoppingCart!!.smallCartAmt else 0.0)
        //set value


        setValues()
        totalPayableAmountShow(cartTotalAmount, totalDiscount)
        mBinding.pointEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    var maxWalletUseAmount1 = 0.0
                    if (!TextUtils.isNullOrEmpty(maxWalletUseAmount)) {
                        maxWalletUseAmount1 = maxWalletUseAmount.toDouble()
                    }
                    if (s.length > 0) {
                        if (rewardPoints!!.isEmpty()) {
                            rewardPoints = "0"
                        }
                        if (rewardPoints == "null") {
                            rewardPoints = "0"
                        }
                        val currentRewardPoints = rewardPoints!!.toDouble()
                        enterRewardPoint = s.toString().toDouble()
                        val totalAmountPopUp: Double = calculatedAmount
                        val netTotal: Double
                        if (enterRewardPoint > maxWalletUseAmount1 && maxWalletUseAmount1 > 0) {
                            if (enterRewardPoint > currentRewardPoints) {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(
                                        R.string.you_do_not_have_enough_points
                                    )
                                )
                                enterRewardPoint = 0.0
                                amountToReduct = 0.0
                                tcsAmount = getTcsAmount(calculatedAmount)
                                mBinding.tvPayableAmt.text =
                                    getString(R.string.space) + DecimalFormat("##.##").format(
                                        Math.round(totalAmountPopUp + tcsAmount)
                                    )
                                mBinding.tvTotalAmountPay.text =
                                    "₹" + DecimalFormat("##.##").format(
                                        Math.round(totalAmountPopUp + tcsAmount)
                                    )
                                mBinding.tvTcsAmt.text =
                                    "₹ " + DecimalFormat("##.##").format(tcsAmount)
                                PrepaidOrder(Math.round(totalAmountPopUp).toDouble(), false)
                                setWalletPoint(
                                    "" + DecimalFormat("##.##").format(
                                        Math.round(
                                            totalAmountPopUp + tcsAmount
                                        )
                                    )
                                )
                                mBinding.pointEt.setText("")
                            } else {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(
                                        R.string.maximum_redeem_wallet_points
                                    ) + " " + DecimalFormat("##.##").format(maxWalletUseAmount1)
                                )
                                enterRewardPoint = 0.0
                                amountToReduct = 0.0
                                tcsAmount = getTcsAmount(calculatedAmount)
                                mBinding.tvPayableAmt.text =
                                    getString(R.string.space) + DecimalFormat("##.##").format(
                                        Math.round(totalAmountPopUp + tcsAmount)
                                    )
                                PrepaidOrder(Math.round(totalAmountPopUp).toDouble(), false)
                                val AMountPay =
                                    DecimalFormat("##.##").format(Math.round(totalAmountPopUp + tcsAmount))
                                mBinding.tvTotalAmountPay.text = "₹$AMountPay"
                                mBinding.tvTcsAmt.text =
                                    "₹ " + DecimalFormat("##.##").format(tcsAmount)
                                setWalletPoint(
                                    "" + DecimalFormat("##.##").format(
                                        Math.round(
                                            totalAmountPopUp + tcsAmount
                                        )
                                    )
                                )
                                mBinding.pointEt.setText(
                                    "" + DecimalFormat("##.##").format(
                                        maxWalletUseAmount1
                                    )
                                )
                                mBinding.pointEt.setSelection(mBinding.pointEt.text!!.length)
                            }
                        } else if (enterRewardPoint > currentRewardPoints) {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().noteRepository.getString(
                                    R.string.do_not_have_enough_points
                                )
                            )
                            enterRewardPoint = 0.0
                            amountToReduct = 0.0
                            tcsAmount = getTcsAmount(calculatedAmount)
                            PrepaidOrder(Math.round(totalAmountPopUp).toDouble(), false)
                            mBinding.tvPayableAmt.text =
                                getString(R.string.space) + DecimalFormat("##.##").format(
                                    Math.round(totalAmountPopUp + tcsAmount)
                                )
                            mBinding.tvTotalAmountPay.text = "₹" + DecimalFormat("##.##").format(
                                Math.round(totalAmountPopUp + +tcsAmount)
                            )
                            mBinding.tvTcsAmt.text =
                                "₹ " + DecimalFormat("##.##").format(tcsAmount)
                            setWalletPoint(
                                "" + DecimalFormat("##.##").format(
                                    Math.round(
                                        totalAmountPopUp + tcsAmount
                                    )
                                )
                            )
                            mBinding.pointEt.setText("")
                        } else {
                            amountToReduct = enterRewardPoint / px * rx
                            if (amountToReduct < Math.round(totalAmountPopUp)) {
                                netTotal = totalAmountPopUp - amountToReduct
                                tcsAmount = getTcsAmount(netTotal)
                                PrepaidOrder(Math.round(netTotal).toDouble(), false)
                                mBinding.tvPayableAmt.text =
                                    getString(R.string.space) + DecimalFormat("##.##").format(
                                        Math.round(netTotal + tcsAmount)
                                    )
                                mBinding.tvTotalAmountPay.text =
                                    "₹" + DecimalFormat("##.##").format(
                                        Math.round(netTotal + tcsAmount)
                                    )
                                mBinding.tvTcsAmt.text =
                                    "₹ " + DecimalFormat("##.##").format(tcsAmount)
                                setWalletPoint(
                                    "" + DecimalFormat("##.##").format(
                                        Math.round(
                                            netTotal + tcsAmount
                                        )
                                    )
                                )
                                mBinding.rlWallet.visibility = View.VISIBLE
                            } else {
                                enterRewardPoint = 0.0
                                amountToReduct = 0.0
                                tcsAmount = getTcsAmount(calculatedAmount)
                                PrepaidOrder(Math.round(totalAmountPopUp).toDouble(), false)
                                mBinding.tvPayableAmt.text =
                                    getString(R.string.space) + DecimalFormat("##.##").format(
                                        Math.round(totalAmountPopUp + tcsAmount)
                                    )
                                mBinding.tvTotalAmountPay.text =
                                    "₹" + DecimalFormat("##.##").format(
                                        Math.round(totalAmountPopUp + tcsAmount)
                                    )
                                mBinding.tvTcsAmt.text =
                                    "₹ " + DecimalFormat("##.##").format(tcsAmount)
                                setWalletPoint(
                                    "" + DecimalFormat("##.##").format(
                                        Math.round(
                                            totalAmountPopUp + tcsAmount
                                        )
                                    )
                                )
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(
                                        R.string.you_can_not_use_points
                                    )
                                )
                                mBinding.pointEt.setText("")
                            }
                        }
                        val earnPoint =
                            "- <font color=#FF6FB847>&#8377; " + DecimalFormat("##.##").format(
                                amountToReduct
                            )
                        mBinding.tvEarnWalletAmt.text = Html.fromHtml(earnPoint)
                    } else {
                        amountToReduct = 0.0
                        mBinding.tvEarnWalletAmt.text = ""
                        mBinding.rlWallet.visibility = View.GONE
                        tcsAmount = getTcsAmount(calculatedAmount)
                        val totalAmount = "" + Math.round(calculatedAmount + tcsAmount).toInt()
                        mBinding.tvPayableAmt.text = "₹$totalAmount"
                        mBinding.tvTotalAmountPay.text = "₹$totalAmount"
                        mBinding.tvTcsAmt.text = "₹ " + DecimalFormat("##.##").format(tcsAmount)
                        setWalletPoint(totalAmount)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        callDirectAPI()

        val gpsTracker = GPSTracker(this)
        if (gpsTracker != null) {
            lat = gpsTracker.latitude
            lng = gpsTracker.longitude
        }
    }

    private fun callDirectAPI() {
        observe(viewModel.walletData, ::handleWalletResult)
        observe(viewModel.getUdharCreditLimitData, ::handleUdharCreditLimitResult)
        observe(viewModel.ePayLaterCustomerLimitData, ::handleEPaylaterCreditLimitResult)
        observe(viewModel.checkbookLimitData, ::handleCheckBookLimitResult)
        observe(viewModel.getCODLimitData, ::handleCheckCODLimitResult)
        observe(viewModel.getScaleUpLimitData, ::handleScaleUpLimitResult)
        observe(viewModel.getOfferData, ::handleOfferResult)
        observe(viewModel.getPrepaidOrderData, ::handlePrepaidOrderResult)
        observe(viewModel.getCheckOfferData, ::handleCheckOfferResult)
        observe(viewModel.getEPaylaterConfirmPaymentData, ::handleEpalaterPaymentResult)
        observe(viewModel.getCreditPaymentData, ::handleGeneratePaymentResult)
        observe(viewModel.getScaleUpPaymentInitiateData, ::handleScaleUpPaymentInitiateResult)
        observe(viewModel.getOrderPlaceData, ::handleOrderPlaceResult)
        observe(viewModel.getInsertOnlineTransactionData, ::handleInsertTransactionResult)
        observe(viewModel.getUpdateOnlineTransactionData, ::handleUpdateTransactionResult)
        observe(viewModel.getICICIPaymentCheckData, ::handleICICITransactionResult)
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            showProgressDialog()
            viewModel.getWalletPoint(custId, "Payment Screen")
            if (mBinding.rlSkCredit.visibility == View.VISIBLE) {
                viewModel.getUdharCreditLimit(custId)
            }
            if (mBinding.rlEpay.visibility == View.VISIBLE) {
                viewModel.ePayLaterCustomerLimit(
                    skCode, SharePrefs.getInstance(this).getString(SharePrefs.BEARER_TOKEN)
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

            viewModel.getDiscountOffer(custId, "Payment Screen")

            viewModel.getPrepaidOrder(
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                "Payment Screen"
            )

        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
            onBackPressed()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_apply_coupon, R.id.rl_bill_discount -> {
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

            R.id.rlOtherChargeH -> {
                if (mBinding.rlOtherCharge.visibility == View.VISIBLE) {
                    mBinding.ivOtherCharges.setImageResource(R.drawable.arrow_down_24)
                    mBinding.rlOtherCharge.visibility = View.GONE
                } else {
                    mBinding.ivOtherCharges.setImageResource(R.drawable.arrow_up_24)
                    mBinding.rlOtherCharge.visibility = View.VISIBLE
                }
            }

            R.id.tvOtherChargesT -> {
                showPopupWindow(v)
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

            R.id.btnCallSu -> {
                val phone = MyApplication.getInstance().dbHelper.getString(R.string.scaleUpContact)
                if (!TextUtils.isNullOrEmpty(phone)) {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
                }
            }

            R.id.tvDesRtgs -> startActivity(
                Intent(
                    applicationContext, RtgsInfoActivity::class.java
                )
            )

            R.id.ivInfo -> showCodLimitDialog()
            R.id.placeBtn -> {
                val isChqbookBackLimit: Boolean
                val gullakBal =
                    SharePrefs.getInstance(applicationContext).getDouble(SharePrefs.GULLAK_BALANCE)
                val saveAppVer = SharePrefs.getStringSharedPreferences(
                    applicationContext, SharePrefs.APP_VERSION
                )
                if (BuildConfig.VERSION_NAME.equals(saveAppVer, ignoreCase = true)) {
                    ePayLaterLimit = if (ePayAmount > 0) {
                        ePayLimit == 0.0 || !(ePayAmount <= ePayLimit)
                    } else {
                        false
                    }
                    if (checkBookAmount > 0) {
                        isChqbookLimit =
                            checkBookLimit == 0.0 || !(checkBookAmount <= checkBookLimit)
                        isChqbookBackLimit =
                            checkBookminiAmount == 0 || checkBookAmount < checkBookminiAmount
                    } else {
                        isChqbookBackLimit = false
                        isChqbookLimit = false
                    }
                    if (availableCODLimit != -1L && cashAmount > 0 && cashAmount > availableCODLimit) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.cod_limit_not_available)
                        )
                    } else if (ePayLaterLimit) {
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
                                applicationContext, AddPaymentActivity::class.java
                            ).putExtra("amount", gullakAmount - gullakBal).putExtra("screen", 2), 9
                        )
                    } else if (skCreditAmt > 0 && skCreditRes!!.dynamicData!!.amount < skCreditAmt) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                        )
                        return
                    } else if (scaleUpAmt > 0 && scaleUpAmt >= scaleUpLimit) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.no_sufficient_limit)
                        )
                        return
                    } else {
                        mBinding.placeBtn.isClickable = false
                        if (orderDividePercentCheck) {
                            if (orderId != 0) {
                                if (canPostOrder) {
                                    val remainingAmt = finalAmount.toDouble()
                                    if (gullakAmount > 0) {
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
                                        } else {
                                            startActivityForResult(
                                                Intent(
                                                    applicationContext,
                                                    AddPaymentActivity::class.java
                                                ).putExtra("amount", gullakAmount - gullakBal)
                                                    .putExtra("screen", 2), 9
                                            )
                                        }
                                    } else if (hdfcAmount > 0) {
                                        getHDFCRSAKey(false)
                                    } else if (creditAmount > 0) {
                                        getHDFCRSAKey(true)
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
                                                MyApplication.getInstance().dbHelper.getString(
                                                    R.string.no_sufficient_limit
                                                )
                                            )
                                        }
                                    } else if (scaleUpAmt > 0) {
                                        if (scaleUpLimit > scaleUpAmt) {
                                            scaleUpPayment()
                                        } else {
                                            Utils.setToast(
                                                applicationContext,
                                                MyApplication.getInstance().dbHelper.getString(
                                                    R.string.no_sufficient_limit
                                                )
                                            )
                                        }
                                    } else if (razorpayAmount > 0) {
                                        getRazorpayOrderId()
                                    } else if (iCICIPayAmount > 0) {
                                        callICICIPay()
                                    } else {
                                        if (cashAmount > 0 && remainingAmt == cashAmount.toDouble()) {
                                            IsSuccess = true
                                            insertCashPaymentStatusAPICall(
                                                "Success", "Cash", cashAmount.toDouble(), "Offline"
                                            )
                                        } else {
                                            Utils.setToast(
                                                applicationContext,
                                                MyApplication.getInstance().dbHelper.getString(
                                                    R.string.update_your_app
                                                )
                                            )
                                        }
                                    }
                                } else {
                                    mBinding.placeBtn.isClickable = true
                                    Utils.setToast(
                                        applicationContext,
                                        MyApplication.getInstance().dbHelper.getString(
                                            R.string.amount_does_not_match_with_order_amount
                                        )
                                    )
                                }
                            } else {
                                IsSuccess = true
                                canPostOrder = false
                                if (billDiscountId.length == 0) {
                                    onButtonClick()
                                    if (canPostOrder) {
                                        postOrderBtnData()
                                    } else {
                                        mBinding.placeBtn.isClickable = true
                                        Utils.setToast(
                                            applicationContext,
                                            MyApplication.getInstance().dbHelper.getString(
                                                R.string.amount_does_not_match_with_order_amount
                                            )
                                        )
                                    }
                                } else {
                                    MyApplication.getInstance().updateAnalytics("discount_api_call")
                                    viewModel.checkBillDiscountOffer(custId, billDiscountId)
                                }
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
                RECEIVER_NOT_EXPORTED
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

    override fun onBackPressed() {
        if (IsBackPaymentFalse) {
            IsSuccess = false
            updatePaymentAPICall("", false, "", "")
        } else {
            finish()
            Utils.fadeTransaction(this)
            val intent = Intent(applicationContext, ShoppingCartActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("REVIEW_SCREEN", true)
            startActivity(intent)
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
                    transactionId = `object`.getString(AvenuesParams.TRACKING_ID)
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
            } else {/*mBinding.radioCredit.isChecked() ? "" + "credit hdfc" : "hdfc"*/
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
                        transactionId = eplOrderId
                        paymentCheckedMsg = encdata.getString("statusDesc")
                        ePaystatusCode = encdata.getString("statusCode")
                        gatewayOrderId = encdata.getString("marketplaceOrderId")
                        ePayMarketplaceOrderId = eplOrderId
                        ePayResponseObj = responseObj.toString()
                        ePayResAmt = amount

                        viewModel.ePayLaterConfirmOrder(
                            SharePrefs.getInstance(this).getString(SharePrefs.BEARER_TOKEN),
                            eplOrderId,
                            encdata.getString("marketplaceOrderId")
                        )

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
                    data.getStringExtra("accountId")
                    data.getStringExtra("orderId")
                    val status = data.getStringExtra("status")
                    val amount = data.getStringExtra("amount")!!.toDouble()
                    val transactionRefNo = data.getStringExtra("transactionRefNo")
                    transactionId = transactionRefNo
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
                mBinding.placeBtn.isClickable = true
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
        } else if (requestCode == SCALEUP) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    data.getStringExtra("accountId")
                    data.getStringExtra("orderId")
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
                    MyApplication.getInstance().dbHelper.getString(R.string.payment_cancel)
                )
                insertPaymentStatusAPICall(
                    "Failed",
                    "cancel by user",
                    "ScaleUp",
                    "",
                    scaleUpAmt.toDouble(),
                    "ScaleUp",
                    "",
                    "ScaleUp"
                )
            }
        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            mBinding.placeBtn.callOnClick()
        }
    }

    override fun onSelectClick(position: Int) {
        openScratchDialog(position, discountList)
    }

    override fun onApplyOfferClick(response: CheckoutCartResponse?) {
        mBinding.pointEt.setText("")
        mBinding.rlWallet.visibility = View.GONE
        amountToReduct = 0.0
        enterRewardPoint = 0.0
        mShoppingCart = response!!.shoppingCartItemDetailsResponse
        calculateBillAmount(mShoppingCart)
        totalPayableAmountShow(
            mShoppingCart!!.cartTotalAmt,
            mShoppingCart!!.totalDiscountAmt
        )
        PrepaidOrder(mShoppingCart!!.grossTotalAmt + deliveryCharges, false)
        try {
            var isFreeItem = false
            if (!TextUtils.isNullOrEmpty(
                    mShoppingCart!!.applyOfferId
                )
            ) {
                val str = mShoppingCart!!.applyOfferId
                val arrOfStr =
                    str!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in discountList!!.indices) {
                    for (a in arrOfStr) {
                        if (discountList!![i].offerId == a.toInt()) {
                            if (discountList!![i].billDiscountOfferOn.equals(
                                    "FreeItem", ignoreCase = true
                                )
                            ) {
                                isFreeItem = true
                            }
                            break
                        }
                    }
                }
                if (isFreeItem) {
                    mBinding.rlDiscountFreeItem.visibility = View.VISIBLE
                } else {
                    mBinding.rlDiscountFreeItem.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initialization() {
        lang = LocaleHelper.getLanguage(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        maxWalletUseAmount =
            SharePrefs.getInstance(this).getString(SharePrefs.MAX_WALLET_POINT_USED)
        isUdharOrderOverDue = SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_UDHAAR_ORDER)
        mBinding.toolbarPaymentOption.title.text = MyApplication.getInstance().dbHelper.getString(
            R.string.payment_option
        )
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
        mBinding.tvPayonT.text = MyApplication.getInstance().dbHelper.getString(R.string.payon)
        mBinding.tvCashT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.cash_card_cheque)
        mBinding.etAmountCash.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_amount)
        mBinding.tvOnlinePayT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.trupay)
        mBinding.tvInstantT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.instant_online_payment)
        mBinding.tvPaymentStatusHdfc.text =
            MyApplication.getInstance().dbHelper.getString(R.string.payment_success)
        mBinding.etAmountHdfc.hint =
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
        mBinding.tvCashH.text = MyApplication.getInstance().dbHelper.getString(R.string.cash_h)
        mBinding.tvOnlineH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.online_h)
        mBinding.tvCreditCardChargesH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.credit_card_charges_1_gst)
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
        mBinding.tvRtgs.text = MyApplication.getInstance().dbHelper.getString(R.string.van_rtgs)
        mBinding.tvDesRtgs.text =
            Html.fromHtml(MyApplication.getInstance().dbHelper.getString(R.string.van_rtgs_terms_description))
        mBinding.tvLimitRtgs.text =
            MyApplication.getInstance().dbHelper.getString(R.string.balance) + "₹ " + SharePrefs.getInstance(
                applicationContext
            ).getString(SharePrefs.RTGS_BAL)
        mBinding.tvDesRtgs.paintFlags =
            mBinding.tvDesRtgs.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        mBinding.tvScaleUp.text =
            MyApplication.getInstance().dbHelper.getString(R.string.scale_up_pay)
        mBinding.tvRazorpay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.pay_via_razorpay)
        mBinding.tvInstantRazorpay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.msg_instant_payment_razorpay)
        mBinding.tvICICIPay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.pay_via_icici_pay)
        mBinding.tvInstantICICIPay.text =
            MyApplication.getInstance().dbHelper.getString(R.string.msg_instant_payment_razorpay)
        if (!TextUtils.isNullOrEmpty(maxWalletUseAmount) && maxWalletUseAmount != "0" && maxWalletUseAmount != "0.0") {
            mBinding.txtMaxWalletPnt.text =
                MyApplication.getInstance().dbHelper.getString(R.string.text_max_wallet) + DecimalFormat(
                    "##.##"
                ).format(maxWalletUseAmount.toDouble()) + MyApplication.getInstance().dbHelper.getString(
                    R.string.wallet_points
                )
        } else {
            mBinding.txtMaxWalletPnt.visibility = View.GONE
        }
        utils = Utils(this)
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
        mBinding.radioRtgs.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioRtgs
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
        mBinding.radioICICIPay.setOnCheckedChangeListener(
            GenericCheckChangeListener(
                mBinding.radioICICIPay
            )
        )
        mBinding.etAmountCash.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountCash
            )
        )
        mBinding.etAmountGullak.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountGullak
            )
        )
        mBinding.etAmountHdfc.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountHdfc
            )
        )
        mBinding.etAmountEpay.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountEpay
            )
        )
        mBinding.etAmountCb.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountCb
            )
        )
        mBinding.etAmountSkC.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountSkC
            )
        )
        mBinding.etAmountRtgs.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountRtgs
            )
        )
        mBinding.etAmountSu.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountSu
            )
        )
        mBinding.etAmountRazorpay.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountRazorpay
            )
        )
        mBinding.etAmountICICIPay.addTextChangedListener(
            GenericTextWatcher(
                mBinding.etAmountICICIPay
            )
        )
        mBinding.etAmountSu.isFocusable = false

        // back btn
        mBinding.toolbarPaymentOption.back.setOnClickListener { onBackPressed() }
        mBinding.rlApplyCoupon.setOnClickListener(this)
        mBinding.rlBillDiscount.setOnClickListener(this)
        mBinding.liWallet.setOnClickListener(this)
        mBinding.callBtn.setOnClickListener(this)
        mBinding.callBtnCb.setOnClickListener(this)
        mBinding.btnCallSkC.setOnClickListener(this)
        mBinding.btnCallSu.setOnClickListener(this)
        // btn order place
        mBinding.placeBtn.setOnClickListener(this)
        mBinding.tvDesRtgs.setOnClickListener(this)
        mBinding.ivInfo.setOnClickListener(this)
        mBinding.rlOtherChargeH.setOnClickListener(this)
        mBinding.tvOtherChargesT.setOnClickListener(this)
        if (!StoryBordSharePrefs.getInstance(applicationContext)
                .getBoolean(StoryBordSharePrefs.PAYMENTOPTION)
        ) {
            appStoryView()
        }
        flutterEngine = FlutterEngine(this)
        flutterEngine!!.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        methodChannel = MethodChannel(
            flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL
        )
        FlutterEngineCache.getInstance().put(FLUTTER_ENGINE_ID, flutterEngine)
        MethodChannel(
            flutterEngine!!.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
            if (call.method == "returnToPayment") {
                val transactionReqNo = call.argument<String>("transactionReqNo")
                val amount = call.argument<Any>("amount")
                call.argument<String>("mobileNo")
                call.argument<Int>("loanAccountId")
                call.argument<Int>("creditDay")
                holePaymentSucceedCheck = true
                insertPaymentStatusAPICall(
                    "Success",
                    "200",
                    "ScaleUp",
                    transactionReqNo,
                    amount.toString().toDouble(),
                    "",
                    "",
                    "ScaleUp"
                )
            } else {
                result.notImplemented()
            }
        }
    }

    private fun setWalletPoint(amount: String) {
        if (mBinding.radioGullak.isChecked) {
            mBinding.etAmountGullak.setText(amount)
        } else if (mBinding.radioRtgs.isChecked) {
            mBinding.etAmountRtgs.setText(amount)
        } else if (mBinding.radioHdfc.isChecked) {
            mBinding.etAmountHdfc.setText(amount)
        } else if (mBinding.radioEpay.isChecked) {
            mBinding.etAmountHdfc.setText(amount)
        } else if (mBinding.radioCheckbook.isChecked) {
            mBinding.etAmountCb.setText(amount)
        } else if (mBinding.radioSkC.isChecked) {
            mBinding.etAmountSkC.setText(amount)
        } else if (mBinding.radioScaleUp.isChecked) {
            mBinding.etAmountSu.setText(amount)
        } else mBinding.etAmountCash.setText(amount)
    }

    private fun cashHandle(amount: String, isChecked: Boolean) {
        if (isUdharOrderOverDue) {
            cashAmount = 0
        } else {
            mBinding.etAmountCash.setText(amount)
            if (isChecked) {
                mBinding.cbCash.isChecked = true
            }
        }
    }

    private fun setValues() {
        mBinding.pointEt.hint = "0"
        mBinding.tvItemCount.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_item) + " " + mShoppingCart!!.totalQty
        mBinding.txtWlletPnt.text =
            "10" + " " + MyApplication.getInstance().dbHelper.getString(R.string.total_dp) + " = 1 RS."
        mBinding.tvEarningPntDp.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_dreamPoint) + " " + mShoppingCart!!.deamPoint
        cashAmount = finalAmount.toLong()
        if (deliveryCharges == 0.0) {
            mBinding.tvDeliveryCharges.text = "Free"
        } else {
            mBinding.tvDeliveryCharges.text =
                "+ ₹" + DecimalFormat("##.##").format(deliveryCharges)
        }
        if (mShoppingCart!!.wheelCount > 0) {
            mBinding.dialAvailable.text = "" + mShoppingCart!!.wheelCount
        } else {
            mBinding.dialAvailable.text = "0"
        }
        if (mShoppingCart!!.tcsPercent > 0) {
            mBinding.rlTcs.visibility = View.VISIBLE
        } else {
            mBinding.rlTcs.visibility = View.GONE
        }
        mBinding.rlConvenience.visibility = if(mShoppingCart!!.isConvenienceFee) View.VISIBLE else View.GONE
        mBinding.rlPlatform.visibility = if(mShoppingCart!!.isPlatformFee) View.VISIBLE else View.GONE
        mBinding.rlHike.visibility = if(mShoppingCart!!.isHikeCharge) View.VISIBLE else View.GONE
        mBinding.rlOrderProcess.visibility = if(mShoppingCart!!.isOrderProcessCharge) View.VISIBLE else View.GONE
        mBinding.rlSmallCart.visibility = if(mShoppingCart!!.isSmallCartCharge) View.VISIBLE else View.GONE

        mBinding.tvOtherCharges.text =
            "+ ₹" + DecimalFormat("##.##").format(otherCharges + deliveryCharges)

        mBinding.tvConvenienceFee.text = if (mShoppingCart!!.isConvenienceFee)
            "+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.convenienceFees)
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.convenienceFees) + "</s>")
        mBinding.tvPlatformFee.text = if (mShoppingCart!!.platformFeeFinalAmt > 0 && mShoppingCart!!.platformFeeFinalAmt < mShoppingCart!!.platformFees)
            Html.fromHtml("+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.platformFeeFinalAmt) + "<s> ₹" + DecimalFormat("##.##").format(mShoppingCart!!.platformFees))
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.platformFees) + "</s>")
        mBinding.tvSPVCharge.text = if (mShoppingCart!!.isVisitCharges)
            "+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.salesPersonVisitCharges)
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.salesPersonVisitAmt) + "</s>")
        mBinding.tvHikeCharge.text = if (mShoppingCart!!.isHikeCharge)
            "+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.hikeCharges)
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.hikeCharges) + "</s>")
        mBinding.tvOrderProcessCharge.text = if (mShoppingCart!!.isOrderProcessCharge)
            "+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.orderProcessAmt)
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.orderProcessCharge) + "</s>")
        mBinding.tvSmallCartCharge.text = if (mShoppingCart!!.isSmallCartCharge)
            "+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.smallCartAmt)
        else Html.fromHtml("Free " + "<s>+ ₹" + DecimalFormat("##.##").format(mShoppingCart!!.smallCartCharge) + "</s>")
    }

    private fun setPoints(getPx: Double, getRx: Double, walletAmount: String?) {
        px = getPx
        rx = getRx
        rewardPoints = walletAmount
        if (rewardPoints!!.isEmpty()) {
            rewardPoints = "0"
        }
        if (rewardPoints == "null") {
            rewardPoints = "0"
        }
        mBinding.tvWalletpoint.text = DecimalFormat("##.##").format(rewardPoints!!.toDouble())
    }

    private fun sharedData() {
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        custMobile =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
        skCode = SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
        checkBookminiAmount =
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.IS_ChQBOOKMINI_AMT)
    }

    private fun getTimeStamp(dateStr: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val all = dateStr!!.replace("\\+0([0-9]){1}\\:00".toRegex(), "+0$100")
        val s = all.replace("T".toRegex(), " ")
        var timestamp: Long = 0
        try {
            val date = format.parse(s)
            timestamp = date.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return timestamp
    }

    private fun activePaymentMode() {
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_HDFC_PAYMENT)) {
            mBinding.rlHdfc.visibility = View.VISIBLE
            if (SharePrefs.getInstance(this).getString(SharePrefs.IS_PAYMENT_GATWAY)
                    .equals("HDFC", ignoreCase = true)
            ) {
                mBinding.rlHdfc.visibility = View.VISIBLE
                // mBinding.radioGullak.setChecked(true);
            } else {
                mBinding.rlHdfc.visibility = View.GONE
            }
        } else {
            mBinding.rlHdfc.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_VAN_RTGS)) {
            mBinding.rlRtgs.visibility = View.GONE
        }
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_EPAY_LATER_SHOW)) {
            mBinding.rlEpay.visibility = View.VISIBLE
        } else {
            mBinding.rlEpay.visibility = View.GONE
        }
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_CHECKBOOK_SHOW)) {
            mBinding.rlCheckBook.visibility = View.VISIBLE
        } else {
            mBinding.rlCheckBook.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_DIRECT_UDHAR)) {
            mBinding.rlSkCredit.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_SCALEUP)) {
            mBinding.rlScaleUp.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_RAZORPAY_PAYMENT)) {
            mBinding.rlRazorpay.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_ICICI_PAYMENT)) {
            mBinding.rlICICIPay.visibility = View.GONE
        }
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.CUST_ACTIVE)) {
            mBinding.rlGullak.visibility = View.GONE
            mBinding.tvOnlineH.visibility = View.GONE
            mBinding.rlHdfc.visibility = View.GONE
            mBinding.rlEpay.visibility = View.GONE
            mBinding.rlCheckBook.visibility = View.GONE
            mBinding.rlSkCredit.visibility = View.GONE
            mBinding.tvPayLaterH.visibility = View.GONE
            mBinding.rlRtgs.visibility = View.GONE
            mBinding.rlScaleUp.visibility = View.GONE
            mBinding.rlRazorpay.visibility = View.GONE
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
            application.setAppInfo(
                iCICIMerchantId,
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
            ColorDrawable(Color.TRANSPARENT)
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
        ivClose!!.setOnClickListener { v: View? -> dialog!!.dismiss() }
        dialog!!.show()
        MyApplication.getInstance().updateAnalytics("discount_dialog_open")
    }

    private fun openScratchDialog(position: Int, discountList: ArrayList<BillDiscountModel>?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_scratch_card)
        dialog.setCancelable(true)
        val card = dialog.findViewById<ScratchCard>(R.id.scratchCard)
        val tvTime = dialog.findViewById<TextView>(R.id.tv_time)
        val tvName = dialog.findViewById<TextView>(R.id.tv_name)
        val tvCoupon = dialog.findViewById<TextView>(R.id.tv_coupon)
        dialog.findViewById<TextView>(R.id.tvDynamicAmt)
        val tvDetail = dialog.findViewById<TextView>(R.id.tv_detail)
        val btnApply = dialog.findViewById<Button>(R.id.btn_apply)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener { v: View? -> dialog.dismiss() }
        btnApply.setOnClickListener { v: View? ->
            viewModel.applyOffer(
                custId,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                discountList!![position].offerId,
                true,
                lang,
                "Payment"
            )
            viewModel.getApplyOfferData.observe(this@PaymentOptionActivity) {
                when (it) {
                    is Response.Loading -> {
                        Utils.showProgressDialog(this)
                    }

                    is Response.Success -> {
                        it.data?.let {
                            Utils.hideProgressDialog()
                            if (it.status) {
                                try {
                                    mShoppingCart = it.shoppingCartItemDetailsResponse
                                    calculateBillAmount(mShoppingCart)
                                    totalPayableAmountShow(
                                        mShoppingCart!!.cartTotalAmt,
                                        mShoppingCart!!.totalDiscountAmt
                                    )
                                    PrepaidOrder(
                                        mShoppingCart!!.grossTotalAmt + deliveryCharges, false
                                    )
                                    discountList[position].isSelected = true
                                    discountList[position].isScratchBDCode = true
                                    discountAdapter!!.setBillDiscount(discountList)
                                    // update analytic apply promotion
                                    MyApplication.getInstance()
                                        .updateAnalyticPromotion(discountList[position])
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else {
                                Utils.setToast(
                                    applicationContext, it.message
                                )
                            }

                        }
                    }

                    is Response.Error -> {
                        Utils.setToast(
                            applicationContext, "some technical issue. please try again later"
                        )
                        Utils.hideProgressDialog()
                    }
                }

            }
            dialog.dismiss()
        }
        card.setOnScratchListener { scratchCard: ScratchCard?, visiblePercent: Float ->
            if (visiblePercent > 0.3) {
                card.visibility = View.GONE
                tvTime.visibility = View.GONE
                btnApply.visibility = View.VISIBLE
                tvName.setText(R.string.congratulations)
                tvDetail.text = discountList!![position].offerName
                discountList[position].isScratchBDCode = true
                discountAdapter!!.setBillDiscount(discountList)
                viewModel.updateScratchCardStatus(custId, discountList[position].offerId, true)
            }
        }
        if (discountList != null) {
            if (discountList[position].billDiscountOfferOn.equals(
                    "DynamicAmount", ignoreCase = true
                )
            ) {
                tvCoupon.text = "₹" + discountList[position].billDiscountWallet
            } else tvCoupon.text = discountList[position].offerCode
            val timestamp = getTimeStamp(discountList[position].end)
            val expiryTime = timestamp - Date().time
            object : CountDownTimer(expiryTime, 1000) {
                override fun onTick(millis: Long) {
                    val sec = TimeUnit.MILLISECONDS.toSeconds(millis)
                    val min = TimeUnit.MILLISECONDS.toMinutes(millis)
                    val hour = TimeUnit.MILLISECONDS.toHours(millis)
                    tvTime.text = """Offer Date
Offer Expires in ${hour % 24}:${min % 60}:${sec % 60} hrs"""
                }

                override fun onFinish() {
                    tvTime.text = "Time Expired!"
                }
            }.start()
        }
        MyApplication.getInstance().updateAnalytics("scratch_dialog_open")
        dialog.show()
    }

    private fun calculateBillAmount(mShoppingCart: ShopingCartItemDetailsResponse?) {
        billDiscountId = ""
        totalDiscount = mShoppingCart!!.totalDiscountAmt
        grossTotalAmount = mShoppingCart.grossTotalAmt
        cartTotalAmount = mShoppingCart.cartTotalAmt
        billDiscountId = mShoppingCart.applyOfferId!!
        if (!TextUtils.isNullOrEmpty(
                mShoppingCart.applyOfferId
            )
        ) {
            mBinding.tvBillDiscount.visibility = View.VISIBLE
            mBinding.tvBillDiscount.text = "₹ " + DecimalFormat("##.##").format(totalDiscount)
            mBinding.tvCouponAmount.text = "-₹ " + DecimalFormat("##.##").format(totalDiscount)
            mBinding.rlBillDiscount.visibility = View.VISIBLE
            mBinding.rlCoupon.visibility = View.VISIBLE
            mBinding.rlApplyCoupon.visibility = View.GONE
            mBinding.tvNextBillDiscount.visibility = View.GONE
            mBinding.tvNextBillDiscountText.visibility = View.GONE
            if (mShoppingCart.newBillingWalletPoint != 0) {
                mBinding.tvNextBillDiscount.visibility = View.VISIBLE
                mBinding.tvNextBillDiscountText.visibility = View.VISIBLE
                mBinding.tvNextBillDiscount.text =
                    "₹ " + convertToAmount(mShoppingCart.newBillingWalletPoint.toDouble())
            }
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

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    private fun postOrderBtnData() {
        SharePrefs.getInstance(this).putString(SharePrefs.ENTER_REWARD_POINT, "" + enterRewardPoint)
        SharePrefs.getInstance(this).putString(SharePrefs.AMOUNT_TO_REDUCE, "" + amountToReduct)
        val isSignUp = SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_SIGN_UP)
        if (isSignUp) {
            orderPlacedPopup()
        } else {
            mBinding.placeBtn.isClickable = true
            SharePrefs.getInstance(this).putString(SharePrefs.SIGNUPLOC, "PAYMENT")
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
        okBtn.setOnClickListener { v: View? ->
            dialog.dismiss()
            okBtn.isClickable = false
            val total = finalAmount.toDouble()
            if (cashAmount.toDouble() == total || rtgsAmt.toDouble() == total) {
                otherPaymentMode = false
                callOrderPlaceApi(false, "offline")
            } else {
                otherPaymentMode = true
                callOrderPlaceApi(true, "Online")
            }
        }
        cancelBtn.setOnClickListener { v: View? ->
            dialog.dismiss()
            mBinding.placeBtn.isClickable = true
        }
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun callOrderPlaceApi(paymentOption: Boolean, paymentMode: String) {
        showOrderPlaceDialog()
        var paymentThrough = ""
        if (cashAmount > 0) {
            paymentThrough = paymentThrough + "cash" + ","
            this.paymentThrough = "cash"
        }
        if (gullakAmount > 0) {
            paymentThrough = paymentThrough + "Gullak" + ","
            this.paymentThrough = "Gullak"
        }
        if (creditAmount > 0) {
            paymentThrough = paymentThrough + "credit hdfc" + ","
            this.paymentThrough = "hdfc"
        }
        if (hdfcAmount > 0) {
            paymentThrough = paymentThrough + "hdfc" + ","
            this.paymentThrough = "hdfc"
        }
        if (ePayAmount > 0) {
            paymentThrough = paymentThrough + "epaylater" + ","
            this.paymentThrough = "epaylater"
        }
        if (checkBookAmount > 0) {
            paymentThrough = paymentThrough + "chqbook" + ","
            this.paymentThrough = "chqbook"
        }
        if (skCreditAmt > 0) {
            paymentThrough = paymentThrough + "DirectUdhar" + ","
            this.paymentThrough = "cash"
        }
        if (rtgsAmt > 0) {
            paymentThrough = paymentThrough + "rtgs/neft" + ","
            this.paymentThrough = "cash"
        }
        if (scaleUpAmt > 0) {
            paymentThrough = paymentThrough + "scaleUp" + ","
            this.paymentThrough = "scaleUp"
        }
        if (razorpayAmount > 0) {
            paymentThrough = paymentThrough + "razorpay" + ","
            this.paymentThrough = "razorpay"
        }
        if (iCICIPayAmount > 0) {
            paymentThrough = paymentThrough + "icici" + ","
            this.paymentThrough = "icici"
        }
        paymentThrough = try {
            paymentThrough.substring(0, paymentThrough.length - 1)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
        var paymentMode1 = ""
        paymentMode1 = if (paymentOption) {
            "online"
        } else {
            "offline"
        }
        val mItemDetailsList = ArrayList<ItemDetailsModel>()
        // order place api call
        val jsonFlashString = SharePrefs.getStringSharedPreferences(
            applicationContext, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
        )
        var freeItemId = 0
        var freeItemQty = 0
        var offerCategory = 0
        var freeItemWalletPoint = 0.0
        var isflashDealUsed = false
        var isOffer: Boolean
        val mShopingItemCartList = mShoppingCart!!.shoppingCartItemDcs
        for (i in mShopingItemCartList!!.indices) {
            if (mShopingItemCartList[i].qty != 0) {
                isOffer = mShopingItemCartList[i].isOffer
                if (isOffer) {
                    offerCategory = mShopingItemCartList[i].offerCategory
                    try {
                        if (mShopingItemCartList[i].offerType.equals(
                                "FlashDeal", ignoreCase = true
                            )
                        ) {
                            if (!jsonFlashString.isEmpty()) {
                                var jsonObject: JSONObject
                                jsonObject = JSONObject(jsonFlashString)
                                if (jsonObject.has(mShopingItemCartList[i].itemId.toString())) {
                                    if (jsonObject[mShopingItemCartList[i].itemId.toString()] == "1") {
                                        isflashDealUsed = true
                                    }
                                }
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (mShopingItemCartList[i].offerType.equals(
                            "WalletPoint", ignoreCase = true
                        )
                    ) {
                        freeItemId = 0
                        freeItemQty = 0
                        freeItemWalletPoint = mShopingItemCartList[i].totalFreeWalletPoint
                    } else if (mShopingItemCartList[i].offerType.equals(
                            "ItemMaster", ignoreCase = true
                        )
                    ) {
                        freeItemId = mShopingItemCartList[i].freeItemId
                        freeItemQty = mShopingItemCartList[i].totalFreeItemQty
                        freeItemWalletPoint = mShopingItemCartList[i].offerWalletPoint!!
                    } else if (mShopingItemCartList[i].offerType.equals(
                            "FlashDeal", ignoreCase = true
                        )
                    ) {
                        freeItemId = mShopingItemCartList[i].freeItemId
                        freeItemQty = mShopingItemCartList[i].totalFreeItemQty
                        freeItemWalletPoint = 0.0
                        if (isflashDealUsed) {
                            offerCategory = 0
                            isOffer = false
                        }
                    }
                } else if (mShopingItemCartList[i].isDealItem) {
                    offerCategory = mShopingItemCartList[i].offerCategory
                } else {
                    freeItemId = 0
                    freeItemQty = 0
                    freeItemWalletPoint = 0.0
                    offerCategory = 0
                }
                if (SharePrefs.getInstance(applicationContext)
                        .getBoolean(SharePrefs.IS_PRIME_MEMBER) && mShopingItemCartList[i].isPrimeItem
                ) {
                    mItemDetailsList.add(
                        ItemDetailsModel(
                            mShopingItemCartList[i].itemId,
                            mShopingItemCartList[i].qty,
                            SharePrefs.getInstance(
                                applicationContext
                            ).getInt(SharePrefs.WAREHOUSE_ID),
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.COMPANY_ID),
                            mShopingItemCartList[i].offerId,
                            freeItemWalletPoint,
                            offerCategory,
                            freeItemQty,
                            isOffer,
                            freeItemId,
                            "" + mShopingItemCartList[i].primePrice,
                            mShopingItemCartList[i].isPrimeItem
                        )
                    )
                } else {
                    mItemDetailsList.add(
                        ItemDetailsModel(
                            mShopingItemCartList[i].itemId,
                            mShopingItemCartList[i].qty,
                            SharePrefs.getInstance(
                                applicationContext
                            ).getInt(SharePrefs.WAREHOUSE_ID),
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.COMPANY_ID),
                            mShopingItemCartList[i].offerId,
                            freeItemWalletPoint,
                            offerCategory,
                            freeItemQty,
                            isOffer,
                            freeItemId,
                            "" + mShopingItemCartList[i].unitPrice,
                            mShopingItemCartList[i].isPrimeItem
                        )
                    )
                }
            }
        }
        val orderCreatedDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
        val orderPlaceModel = OrderPlacedModel(
            custId,
            orderCreatedDate,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME),
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_TYPE),
            custMobile,
            0,
            0,
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.SHIPPING_ADDRESS),
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.SHOP_NAME),
            skCode,
            mShoppingCart!!.deamPoint.toDouble(),
            mShoppingCart!!.grossTotalAmt,
            mShoppingCart!!.totalSavingAmt,
            "" + deliveryCharges,
            "0",
            amountToReduct,
            enterRewardPoint,
            paymentOption,
            mItemDetailsList,
            "",
            paymentMode1,
            paymentThrough,
            billDiscountId,
            totalDiscount,
            gullakAmount.toDouble(),
            cashAmount.toDouble(),
            "Retailer",
            BuildConfig.VERSION_NAME,
            lat,
            lng,
            if (mShoppingCart!!.isConvenienceFee) mShoppingCart!!.convenienceFees else 0.0,
            if (mShoppingCart!!.isPlatformFee) mShoppingCart!!.platformFees else 0.0,
            if (mShoppingCart!!.isVisitCharges) mShoppingCart!!.salesPersonVisitAmt else 0.0,
            if (mShoppingCart!!.isHikeCharge) mShoppingCart!!.hikeCharges else 0.0
        )
        println("asd " + Gson().toJson(orderPlaceModel))
        MyApplication.getInstance()
            .updateAnalyticBC(cartTotalAmount, mShoppingCart!!.applyOfferId, mShopingItemCartList)
        viewModel.getOrderPlaced(orderPlaceModel)
    }

    private fun onButtonClick() {
        var ePayLaterUsed = false
        var isCheckBook = false
        canPostOrder = false
        val total = finalAmount.toLong()
        val amount =
            Math.round((cashAmount + gullakAmount + creditAmount + ePayAmount + hdfcAmount + checkBookAmount + skCreditAmt + rtgsAmt + scaleUpAmt + razorpayAmount + iCICIPayAmount).toFloat())
                .toLong()
        if (ePayAmount > 0) {
            ePayLaterUsed = true
        }
        if (checkBookAmount > 0) {
            isCheckBook = true
            Logger.logD(
                TAG,
                "DiffAmt::-total= $total amount $amount cash $cashAmount gullak $gullakAmount credit $creditAmount ePayAmt $ePayAmount hdfcAmt $hdfcAmount skCredit $skCreditAmt scaleUpAmt $scaleUpAmt razorpayAmt $razorpayAmount phipayAmt $iCICIPayAmount discount $totalDiscount"
            )
        }
        if (amount == total) {
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
            val d = (total - amount).toDouble()
            mBinding.tvRemainingAmount.text =
                MyApplication.getInstance().dbHelper.getString(R.string.remaining_amt) + DecimalFormat(
                    "##.##"
                ).format(d)
        }
    }

    private val finalAmount: Int
        get() = Math.round(cartTotalAmount + deliveryCharges - amountToReduct + tcsAmount + otherCharges)
            .toInt()
    private val calculatedAmount: Double
        get() = cartTotalAmount + deliveryCharges + otherCharges

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

    private fun getHDFCRSAKey(isCredit: Boolean) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            showProgressDialog()
            viewModel.getHDFCRSAKey(
                orderId.toString(),
                if (isCredit) "" + creditAmount else hdfcAmount.toString(),
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
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun getRazorpayOrderId() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            showProgressDialog()
            viewModel.fetchRazorpayOrderId(orderId.toString(), razorpayAmount.toDouble())
            viewModel.getRazorpayOrderIdData.observe(this) {
                hideProgressDialog()
                if (!TextUtils.isNullOrEmpty(it)) {
                    callRazorpay(it)
                }
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
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
            for (info in mShoppingCart!!.shoppingCartItemDcs!!) {
                if (info.qty > 0) {
                    val jsonObject = JSONObject()
                    jsonObject.put("code", info.itemNumber)
                    jsonObject.put("name", info.itemname)
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
        chqbookInitialize!!.initiatePayment(orderRequest, object : ChqbookVypaarCallback {
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
            viewModel.creditPayment(
                CreditPayment(
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
        viewModel.scaleUpPaymentInitiate(
            custId, orderId, scaleUpAmt.toDouble()
        )
    }

    private fun totalPayableAmountShow(cartTotalAmt: Double, totalDiscount: Double) {
        tcsAmount = getTcsAmount(calculatedAmount)
        mBinding.tvOrderValue.text =
            "₹" + DecimalFormat("##.##").format(cartTotalAmt + totalDiscount)
        mBinding.tvTotalPrice.text =
            "₹" + DecimalFormat("##.##").format(cartTotalAmt + totalDiscount + deliveryCharges + otherCharges)
        mBinding.tvPayableAmt.text = "" + Math.round(calculatedAmount + tcsAmount).toInt()
        mBinding.tvTotalAmountPay.text = "₹" + Math.round(calculatedAmount + tcsAmount).toInt()
        mBinding.tvTcsAmt.text = "₹" + DecimalFormat("##.##").format(tcsAmount)
        // clear views tvTotalPrice
        mBinding.etAmountHdfc.setText("")
        mBinding.etAmountEpay.setText("")
        mBinding.etAmountSu.setText("")
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.CUST_ACTIVE)) {
            if (SharePrefs.getInstance(applicationContext)
                    .getDouble(SharePrefs.GULLAK_BALANCE) > 0
            ) {
                mBinding.radioGullak.isChecked = true
                mBinding.etAmountGullak.setText(
                    "" + Math.round(calculatedAmount + tcsAmount).toInt()
                )
            } else {
                if (isUdharOrderOverDue) {
                    if (mBinding.rlHdfc.visibility == View.VISIBLE) {
                        cashAmount = 0
                        mBinding.radioHdfc.isChecked = true
                        mBinding.etAmountHdfc.setText(
                            "" + Math.round(calculatedAmount + tcsAmount).toInt()
                        )
                    } else {
                        cashAmount = 0
                    }
                } else {
                    mBinding.cbCash.isChecked = true
                    mBinding.etAmountCash.setText(
                        "" + Math.round(calculatedAmount + tcsAmount).toInt()
                    )
                }
            }
        } else {
            //mBinding.cbCash.setChecked(true);
            //mBinding.etAmountCash.setText("" + (int) Math.round(getCalculatedAmount() + tcsAmount));
            cashHandle("" + Math.round(calculatedAmount + tcsAmount).toInt(), true)
        }
    }

    private fun PrepaidOrder(OrderAmountGross: Double, popupShow: Boolean) {
        var onlineMsg = ""
        val calPrepaidAmount: Double
        if (PrepaidOrderStatus) {
            if (OrderAmountGross >= prepaidOrderModel!!.orderAmount && prepaidOrderModel!!.orderAmount != 0.0) {
                if (prepaidOrderModel!!.cashPercentage != 0.0) {
                    calAmountCOD = OrderAmountGross * prepaidOrderModel!!.cashPercentage / 100
                    orderAmountFlag = true
                }
                if (prepaidOrderModel!!.onlinePercentage != 0.0) {
                    calAmountOnline = OrderAmountGross * prepaidOrderModel!!.onlinePercentage / 100
                    val codFraction = calAmountCOD.toString().split("\\.".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                    val onlineFraction = calAmountOnline.toString().split("\\.".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
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
            MyApplication.getInstance().dbHelper.getString(R.string.prepaid_order_mag1) + calPrepaidAmount + MyApplication.getInstance().dbHelper.getString(
                R.string.prepaid_order_mag2
            )
        okBtn.setOnClickListener { v: View? -> customDialog.dismiss() }
        customDialog.show()
    }

    private fun showPopupWindow(anchor: View) {
        val popupWindow = PopupWindow(anchor.context)
        val background = ContextCompat.getDrawable(
            this@PaymentOptionActivity, R.drawable
                .rectangle_curve_corner_grey_white_background_8
        )
        popupWindow.setBackgroundDrawable(background)
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true
        val inflater = LayoutInflater.from(anchor.context)
        val contentView: View = inflater.inflate(R.layout.dialog_other_charges, null)
        popupWindow.contentView = contentView
        popupWindow.showAsDropDown(anchor)
        val tvDeliveryCharges = contentView.findViewById<TextView>(R.id.tvDeliveryCharges)
        tvDeliveryCharges.text =
            MyApplication.getInstance().noteRepository.getString(R.string.other_charges_for_payments_exceeding_each_slab)
    }

    private val orderDividePercentCheck: Boolean
        get() {
            val precentageFlag: Boolean
            var cashMsg = ""
            var onlineMsg = ""
            if (orderAmountFlag) {
                calAmountCOD = finalAmount * prepaidOrderModel!!.cashPercentage / 100
                if (Math.round(calAmountCOD) >= cashAmount) {
                    precentageFlag = true
                } else {
                    calAmountOnline = finalAmount * prepaidOrderModel!!.onlinePercentage / 100
                    val codFraction = calAmountCOD.toString().split("\\.".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                    val onlineFraction = calAmountOnline.toString().split("\\.".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
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

    private fun getTcsAmount(total: Double): Double {
        val tcsLimit = mShoppingCart!!.tCSLimit
        val totalRTDAndTotalAmount = total + mShoppingCart!!.preTotalDispatched
        var calApplyAmountTcs = 0.0
        var tcsAmount = 0.0
        calApplyAmountTcs = if (mShoppingCart!!.preTotalDispatched >= tcsLimit) {
            total
        } else if (totalRTDAndTotalAmount >= tcsLimit) {
            // totalRTDAndTotalAmount - tcsLimit
            total
        } else {
            0.0
        }
        tcsAmount = total * mShoppingCart!!.tcsPercent / 100
        return tcsAmount/*return total * mShoppingCart.tcsPercent / 100;*/
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
            viewModel.getInsertOnlineTransaction(
                PaymentReq(destr), "Payment Screen $paymentFrom(Status:$Status)"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun insertCashPaymentStatusAPICall(
        status: String, paymentFrom: String, amount: Double, paymentThrough: String
    ) {
        val paymentGatewayModel = PaymentGatewayModel(
            orderId,
            "",
            "",
            amount,
            "INR",
            status,
            "",
            "",
            paymentFrom,
            "",
            "",
            paymentThrough,
            false
        )
        val jsonString = Gson().toJson(paymentGatewayModel)
        Logger.logD(TAG, "InsertCashStatus::-$jsonString")
        try {
            val destr = Aes256.encrypt(
                jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                    Date()
                ) + "1201"
            )
            updateCashStatus = true
            viewModel.getInsertOnlineTransaction(
                PaymentReq(destr), "Payment Screen $paymentFrom(Status:$status)"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePaymentAPICall(
        paymentMode: String, isSuccess: Boolean, transactionId: String?, paymentThrough: String?
    ) {
        val updateOrderPlacedModel = UpdateOrderPlacedModel(
            orderId, isSuccess, paymentMode, transactionId, paymentThrough, false
        )
        val jsonString = Gson().toJson(updateOrderPlacedModel)
        Logger.logD(TAG, "jsonStringUpdate::-$jsonString")
        try {
            val destr = Aes256.encrypt(
                jsonString, SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(
                    Date()
                ) + "1201"
            )
            viewModel.getUpdateOnlineTransaction(
                PaymentReq(destr), "Payment Screen $paymentMode(Status:$isSuccess)"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Order placed confirmation popup
    private fun orderConformationPopup(
        flag: Int,
        orderMaster: OrderMaster?,
        wheelCount: Int,
        earnWalletPoint: Int,
        kisanDanAmount: Double
    ) {
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
        val btnChangeDate = dialog.findViewById<Button>(R.id.btnChangeDate)
        tv_congratulation!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Congratulation)
        orderMsg!!.text = MyApplication.getInstance().dbHelper.getString(R.string.order_done_msg)
        continueShopping!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_continue_shopping)
        tvETAH!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.expected_normal_delivery)
        tvETA!!.text = Utils.getDateMonthFormat(
            orderPlacedNewResponse!!.orderMaster!!.expectedETADate
        )
        tvDeliverByH!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_delivered_by_shopkirana)
        tvHeavyDeliveryLoadMsg!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.msg_heavy_load)
        if (orderPlacedNewResponse!!.orderMaster!!.isDefaultDeliveryChange) {
            tvHeavyDeliveryLoadMsg.visibility = View.VISIBLE
        }
        val eTADate = arrayOf("")
        if (earnWalletPoint > 0) {
            tvNextOrderWallet!!.visibility = View.VISIBLE
            if (LocaleHelper.getLanguage(applicationContext) == "en") {
                tvNextOrderWallet.text =
                    earnWalletPoint.toString() + MyApplication.getInstance().dbHelper.getString(
                        R.string.wallet_added_after_order_text
                    )
            } else {
                tvNextOrderWallet.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.wallet_added_after_order_text) + earnWalletPoint + MyApplication.getInstance().dbHelper.getString(
                        R.string.wallet_added_after_order_text2
                    )
            }
        }
        if (kisanDanAmount > 0) {
            tvKisanDanAmount!!.visibility = View.VISIBLE
            val kisanDaanAmount =
                MyApplication.getInstance().dbHelper.getString(R.string.kisan_daan_text) + "<font color=#000000>&#8377; " + DecimalFormat(
                    "##.##"
                ).format(kisanDanAmount)
            tvKisanDanAmount.text = Html.fromHtml(kisanDaanAmount)
            ivKisanDan!!.setImageResource(R.drawable.kisan_daan_icon)
        } else {
            tvKisanDanAmount!!.visibility = View.VISIBLE
            val kisanDaanAmount =
                MyApplication.getInstance().dbHelper.getString(R.string.no_kisan_daan_text) + "<font color=#000000>&#8377; " + DecimalFormat(
                    "##.##"
                ).format(kisanDanAmount)
            tvKisanDanAmount.text = Html.fromHtml(kisanDaanAmount)
            ivKisanDan!!.setImageResource(R.drawable.no_kisan_daan_icon)
        }
        val orderIdText =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_order_id) + "<font color=#000000>" + orderId
        tvOrderId!!.text = Html.fromHtml(orderIdText)
        val totalAmountText =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_order_amount) + "<font color=#000000>" + mBinding.tvPayableAmt.text.toString()
        tvTotalAmount!!.text = Html.fromHtml(totalAmountText)
        if (flag == 0) {
            continueShopping.text =
                MyApplication.getInstance().dbHelper.getString(R.string.continue_shopping)
            tvDialValue!!.visibility = View.GONE
            continueShopping.setOnClickListener { v: View? ->
                callUpdateETA(eTADate[0])
                dialog.dismiss()
                startActivity(
                    Intent(
                        applicationContext, HomeActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                Utils.rightTransaction(this)
            }
        } else if (flag == 1) {
            continueShopping.text =
                MyApplication.getInstance().dbHelper.getString(R.string.play_dial)
            if (wheelCount >= 0) {
                tvDialValue!!.visibility = View.VISIBLE
                tvDialValue.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.you_won_dial) + wheelCount + MyApplication.getInstance().dbHelper.getString(
                        R.string.diaql_for_this_order
                    )
            }
            continueShopping.setOnClickListener { v: View? ->
                callUpdateETA(eTADate[0])
                MyApplication.getInstance().updateAnalytics("play_dial_click")
                dialog.dismiss()
                val bundle = Bundle()
                bundle.putSerializable(Constant.ORDER_MODEL, orderMaster)
                val newIntent = Intent(applicationContext, DialWheelActivity::class.java)
                newIntent.putExtras(bundle)
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(newIntent)
                finish()
                Utils.leftTransaction(this)
            }
        }
        if (orderPlacedNewResponse!!.isNotServicing) {
            tvNotServiceing!!.visibility = View.VISIBLE
            tvNotServiceing.text = orderPlacedNewResponse!!.message
        }
        val viewList: MutableList<TextView> = ArrayList()
        // delivery date
        if (orderPlacedNewResponse!!.orderMaster!!.etaDateList != null && orderPlacedNewResponse!!.orderMaster!!.etaDateList!!.size > 0) {
            for (i in orderPlacedNewResponse!!.orderMaster!!.etaDateList!!.indices) {
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(15, 10, 15, 10)
                val textView = TextView(this)
                textView.layoutParams = params
                textView.background = resources.getDrawable(R.drawable.rectangle_grey)
                textView.setPadding(40, 20, 40, 20)
                textView.text = Utils.formatToDateMonth(
                    orderPlacedNewResponse!!.orderMaster!!.etaDateList!![i].etaDate
                )
                textView.tag = orderPlacedNewResponse!!.orderMaster!!.etaDateList!![i].etaDate
                viewList.add(textView)
                cgDate!!.addView(textView)
                textView.setOnClickListener { view: View ->
                    tvETA.background = resources.getDrawable(R.drawable.rectangle_grey)
                    eTADate[0] = textView.tag.toString()
                    for (view1 in viewList) {
                        view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                    }
                    view.background = resources.getDrawable(R.drawable.rectangle_orange)
                }
            }
        } else {
            btnChangeDate!!.visibility = View.GONE
            cgDate!!.visibility = View.GONE
            tvSelectDateH!!.visibility = View.GONE
        }
        tvETA.setOnClickListener { view: View? ->
            eTADate[0] = ""
            tvETA.background = resources.getDrawable(R.drawable.rectangle_orange)
            for (i in viewList.indices) viewList[i].background =
                resources.getDrawable(R.drawable.rectangle_grey)
        }
        btnChangeDate!!.setOnClickListener { view: View? ->
            btnChangeDate.visibility = View.GONE
            tvDelayH!!.visibility = View.VISIBLE
            cgDate!!.visibility = View.VISIBLE
        }
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        dialog.show()
        MyApplication.getInstance().updateAnalytics("order_confirm_dialog")
    }

    private fun callUpdateETA(date: String?) {
        if (!TextUtils.isNullOrEmpty(date)) {
            Utils.showProgressDialog(this)
            val jsonObject = JsonObject()
            jsonObject.addProperty("OrderId", orderId)
            jsonObject.addProperty("ETADate", date)
            jsonObject.addProperty("lang", lang)
            viewModel.updateDeliveryETA(jsonObject, "Payment Screen")
            viewModel.getUpdateDeliveryETALiveData.observe(this) {
                Utils.hideProgressDialog()
                if (!TextUtils.isNullOrEmpty(it["Message"].asString)) {
                    Utils.setToast(
                        applicationContext, it["Message"].asString
                    )
                }
            }
        }
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

    private fun showCodLimitDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.popup_cod_limit_terms)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = dialog.findViewById<TextView>(R.id.tvDesc)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        var des = MyApplication.getInstance().dbHelper.getString(R.string.cod_limit_terms)
        des = des.replace("₹50,000", "" + codLimit)
        tvTitle!!.text = MyApplication.getInstance().dbHelper.getString(R.string.maximum_pod_limit)
        tvDesc!!.text = "" + des
        ivClose!!.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        dialog.show()
        MyApplication.getInstance().updateAnalytics("COD_limit_terms_dialog")
    }

    private fun goToHome(orderStatus: String) {
        if (orderId != 0 && orderStatus.equals("success", ignoreCase = true)) {
            SharePrefs.getInstance(this).putString(SharePrefs.AVAIL_DIAL, "0")
            orderConformationPopup(
                0,
                null,
                0,
                orderPlacedNewResponse!!.earnWalletPoint,
                orderPlacedNewResponse!!.kisanDaanAmount
            )
            clearCartData()
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.order_error_msg)
            )
            startActivity(
                Intent(
                    applicationContext, HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            Utils.rightTransaction(this)
        }
    }

    private fun clearCartData() {
        MyApplication.getInstance().noteRepository.truncateCart()
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

    private fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.apply_coupon))
            .setContentText(
                MyApplication.getInstance().dbHelper.getString(
                    R.string.apply_coupon_detail
                )
            ).setGravity(Gravity.center).setDismissType(DismissType.anywhere).setTargetView(
                mBinding.rlApplyCoupon
            ).setGuideListener { view: View ->
                when (view.id) {
                    R.id.rl_apply_coupon -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.wallet_Point
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.wallet_Point_deatil
                        )
                    ).setTargetView(
                        mBinding.pointEt
                    ).build()

                    R.id.pointEt -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.cod
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.cod_detail
                        )
                    )
                        .setTargetView(if (isUdharOrderOverDue) mBinding.etAmountHdfc else mBinding.etAmountCash)
                        .build()

                    R.id.et_amount_cash -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_online
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_online_detail
                        )
                    ).setTargetView(
                        mBinding.etAmountHdfc
                    ).build()

                    R.id.et_amount_hdfc -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_epay_Later
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_epay_Later_call
                        )
                    ).setTargetView(
                        mBinding.callBtn
                    ).build()

                    R.id.callBtn -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_epay_Later
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Pay_epay_Later_amount
                        )
                    ).setTargetView(
                        mBinding.etAmountEpay
                    ).build()

                    R.id.et_amount_epay -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Place_Order
                        )
                    ).setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.Place_Order_detail
                        )
                    ).setTargetView(
                        mBinding.placeBtn
                    ).build()

                    R.id.placeBtn -> {
                        StoryBordSharePrefs.getInstance(applicationContext)
                            .putBoolean(StoryBordSharePrefs.PAYMENTOPTION, true)
                        return@setGuideListener
                    }
                }
                mGuideView = builder!!.build()
                mGuideView!!.show()
            }
        mGuideView = builder!!.build()
        mGuideView!!.show()
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.pointEt.windowToken, 0)
    }

    private fun showAlert(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.alert_for_exit_screen))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setNegativeButton("ok") { dialog: DialogInterface, id: Int -> dialog.cancel() }
        val dialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    private fun showAlertGoToShoppingCart(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.alert_for_exit_screen))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setNegativeButton("ok") { dialog: DialogInterface, id: Int ->
            dialog.cancel()
            val intent = Intent(applicationContext, ShoppingCartActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("REVIEW_SCREEN", true)
            startActivity(intent)
        }
        val dialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    private inner class GenericCheckChangeListener(private val view: View) :
        CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            when (view.id) {
                R.id.cbCash -> if (isChecked) {
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    if (availableCODLimit == -1L || cashAmount > 0 && cashAmount <= availableCODLimit) {
                        mBinding.etAmountCash.setText("" + finalAmount)
                    }
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    hideKeyBoard()
                }

                R.id.radioGullak -> if (isChecked) {
                    mBinding.cbCash.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountCash.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountHdfc.setText("0")
                    mBinding.etAmountEpay.setText("0")
                    mBinding.etAmountCb.setText("0")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    hdfcAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    ePayAmount = 0
                    skCreditAmt = 0
                    rtgsAmt = 0
                    scaleUpAmt = 0
                    razorpayAmount = 0
                    iCICIPayAmount = 0
                    mBinding.etAmountGullak.setText("" + finalAmount)
                    hideKeyBoard()
                }

                R.id.radio_hdfc -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountHdfc.isFocusable = true
                    mBinding.etAmountHdfc.isFocusableInTouchMode = true
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    if (isUdharOrderOverDue) {
                        mBinding.etAmountHdfc.setText("" + finalAmount)
                    }
                    mBinding.etAmountHdfc.setText("" + finalAmount)
                    hideKeyBoard()
                }

                R.id.radio_epay -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountEpay.isFocusable = true
                    mBinding.etAmountEpay.isFocusableInTouchMode = true
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    if (availableCODLimit != -1L && cashAmount > 0 && cashAmount > availableCODLimit) {
                        mBinding.etAmountEpay.setText("" + finalAmount)
                    }
                    hideKeyBoard()
                }

                R.id.radio_checkbook -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountCb.isFocusable = true
                    mBinding.etAmountCb.isFocusableInTouchMode = true
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    if (availableCODLimit != -1L && cashAmount > 0 && cashAmount > availableCODLimit) {
                        mBinding.etAmountCb.setText("" + finalAmount)
                    }
                    hideKeyBoard()
                }

                R.id.radioSkC -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountSkC.isFocusable = true
                    mBinding.etAmountSkC.isFocusableInTouchMode = true
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    if (availableCODLimit != -1L && cashAmount > 0 && cashAmount >= availableCODLimit) {
                        mBinding.etAmountCash.setText("" + finalAmount)
                    } else {
                        mBinding.etAmountSkC.setText("" + finalAmount)
                    }
                    hideKeyBoard()
                }

                R.id.radioRtgs -> if (isChecked) {
                    mBinding.cbCash.isChecked = false
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountCash.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusableInTouchMode = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountSkC.isFocusableInTouchMode = false
                    mBinding.etAmountRtgs.isFocusable = true
                    mBinding.etAmountRtgs.isFocusableInTouchMode = true
                    mBinding.etAmountCash.setText("")
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    cashAmount = 0
                    hdfcAmount = 0
                    razorpayAmount = 0
                    iCICIPayAmount = 0
                    creditAmount = 0
                    checkBookAmount = 0
                    ePayAmount = 0
                    mBinding.etAmountRtgs.setText("" + finalAmount)
                    hideKeyBoard()
                    startActivityForResult(
                        Intent(
                            applicationContext, RtgsInfoActivity::class.java
                        ).putExtra("screen", 2), 10
                    )
                }

                R.id.radioScaleUp -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountSu.isFocusable = true
                    mBinding.etAmountSu.isFocusableInTouchMode = true
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    mBinding.etAmountSu.setText("" + finalAmount)
                    hideKeyBoard()
                }

                R.id.radio_razorpay -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioICICIPay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountRazorpay.isFocusable = true
                    mBinding.etAmountICICIPay.isFocusable = false
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountRazorpay.isFocusableInTouchMode = true
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountHdfc.isFocusableInTouchMode = false
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountICICIPay.setText("")
                    mBinding.etAmountRazorpay.setText("" + finalAmount)
                    hideKeyBoard()
                }

                R.id.radioICICIPay -> if (isChecked) {
                    if (!isUdharOrderOverDue) {
                        mBinding.cbCash.isChecked = true
                    }
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioEpay.isChecked = false
                    mBinding.radioCheckbook.isChecked = false
                    mBinding.radioGullak.isChecked = false
                    mBinding.radioSkC.isChecked = false
                    mBinding.radioRtgs.isChecked = false
                    mBinding.radioScaleUp.isChecked = false
                    mBinding.radioHdfc.isChecked = false
                    mBinding.radioRazorpay.isChecked = false
                    mBinding.etAmountGullak.isFocusable = false
                    mBinding.etAmountCb.isFocusable = false
                    mBinding.etAmountEpay.isFocusable = false
                    mBinding.etAmountSkC.isFocusable = false
                    mBinding.etAmountRtgs.isFocusable = false
                    mBinding.etAmountSu.isFocusable = false
                    mBinding.etAmountRazorpay.isFocusable = false
                    mBinding.etAmountCash.isFocusable = true
                    mBinding.etAmountICICIPay.isFocusable = true
                    mBinding.etAmountCash.isFocusableInTouchMode = true
                    mBinding.etAmountICICIPay.isFocusableInTouchMode = true
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountHdfc.isFocusable = false
                    mBinding.etAmountHdfc.isFocusableInTouchMode = false
                    mBinding.etAmountGullak.setText("")
                    mBinding.etAmountEpay.setText("")
                    mBinding.etAmountCb.setText("")
                    mBinding.etAmountSkC.setText("")
                    mBinding.etAmountRtgs.setText("")
                    mBinding.etAmountSu.setText("")
                    mBinding.etAmountHdfc.setText("")
                    mBinding.etAmountRazorpay.setText("")
                    mBinding.etAmountICICIPay.setText("" + finalAmount)
                    hideKeyBoard()
                }
            }
        }
    }

    private inner class GenericTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(s: Editable) {
            when (view.id) {
                R.id.et_amount_cash -> {
                    cashAmount = if (s.length > 0) {
                        s.toString().toLong()
                    } else {
                        0
                    }
                    onButtonClick()
                }

                R.id.etAmountGullak -> {
                    if (s.length > 0) {
                        gullakAmount = s.toString().toLong()
                        cashAmount = 0
                    } else {
                        gullakAmount = 0
                        cashAmount = finalAmount.toLong()
                    }
                    //mBinding.etAmountCash.setText("" + cashAmount);
                    cashHandle("" + cashAmount, false)
                    onButtonClick()
                }

                R.id.et_amount_hdfc -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            hdfcAmount = s.toString().toLong()
                            cashAmount = finalAmount - hdfcAmount
                        } else {
                            hdfcAmount = 0
                            cashAmount = finalAmount.toLong()
                        }
                        cashAmount = if (cashAmount < 0) 0 else cashAmount
                        // mBinding.etAmountCash.setText("" + cashAmount);
                        cashHandle("" + cashAmount, false)
                    }
                    onButtonClick()
                }

                R.id.et_amount_epay -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            ePayAmount = s.toString().toLong()
                            cashAmount = finalAmount - ePayAmount
                            cashAmount = if (cashAmount < 0) 0 else cashAmount
                            // mBinding.etAmountCash.setText("" + cashAmount);
                            cashHandle("" + cashAmount, false)
                        } else {
                            ePayAmount = 0
                            cashAmount = finalAmount.toLong()
                            //  mBinding.etAmountCash.setText("" + cashAmount);
                            cashHandle("" + cashAmount, false)
                        }
                    }
                    onButtonClick()
                }

                R.id.et_amount_cb -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            checkBookAmount = s.toString().toLong()
                            cashAmount = finalAmount - checkBookAmount
                            cashAmount = if (cashAmount < 0) 0 else cashAmount
                        } else {
                            checkBookAmount = 0
                            cashAmount = finalAmount.toLong()
                        }
                        //  mBinding.etAmountCash.setText("" + cashAmount);
                        cashHandle("" + cashAmount, false)
                    }
                    onButtonClick()
                }

                R.id.etAmountSkC -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            skCreditAmt = s.toString().toLong()
                            cashAmount = finalAmount - skCreditAmt
                            cashAmount = if (cashAmount < 0) 0 else cashAmount
                        } else {
                            skCreditAmt = 0
                            cashAmount = finalAmount.toLong()
                        }
                        cashHandle("" + cashAmount, false)
                    }
                    onButtonClick()
                }

                R.id.etAmountRtgs -> {
                    if (s.length > 0) {
                        rtgsAmt = s.toString().toLong()
                        cashAmount = 0
                    } else {
                        rtgsAmt = 0
                    }
                    cashHandle("" + cashAmount, false)
                    onButtonClick()
                }

                R.id.etAmountSu -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            scaleUpAmt = s.toString().toLong()
                            cashAmount = finalAmount - scaleUpAmt
                            cashAmount = if (cashAmount < 0) 0 else cashAmount
                            cashHandle("" + cashAmount, false)
                        } else {
                            scaleUpAmt = 0
                            cashAmount = finalAmount.toLong()
                            cashHandle("" + cashAmount, false)
                        }
                    }
                    onButtonClick()
                }

                R.id.et_amount_razorpay -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            razorpayAmount = s.toString().toLong()
                            cashAmount = finalAmount - razorpayAmount
                        } else {
                            razorpayAmount = 0
                            cashAmount = finalAmount.toLong()
                        }
                        cashAmount = if (cashAmount < 0) 0 else cashAmount
                        // mBinding.etAmountCash.setText("" + cashAmount);
                        cashHandle("" + cashAmount, false)
                    }
                    onButtonClick()
                }

                R.id.etAmountICICIPay -> {
                    if (!mBinding.radioGullak.isChecked) {
                        if (s.length > 0) {
                            iCICIPayAmount = s.toString().toLong()
                            cashAmount = finalAmount - iCICIPayAmount
                        } else {
                            iCICIPayAmount = 0
                            cashAmount = finalAmount.toLong()
                        }
                        cashAmount = if (cashAmount < 0) 0 else cashAmount
                        // mBinding.etAmountCash.setText("" + cashAmount);
                        cashHandle("" + cashAmount, false)
                    }
                    onButtonClick()
                }
            }
        }
    }

    class AscComparator : Comparator<BillDiscountModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: BillDiscountModel, rhs: BillDiscountModel): Int {
            return java.lang.Boolean.compare(rhs.isApplicable, lhs.isApplicable)
        }
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

        PayPhiSdk.makePayment(
            applicationContext,
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
                            println("DaTA>>> $bundle")
                            println("txnID>>> $txnID")
                            println("respDescription>>> $respDescription")
                            if (responseCode == "0000" || responseCode == "000") {
                                // Transaction success
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

    private fun callICICIPaymentResult() {
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
        mBinding.placeBtn.isClickable = true
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
                razorpayAmount.toDouble(),
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
            razorpayAmount.toDouble(),
            "Razorpay",
            `object`.toString(),
            "Razorpay"
        )
    }


    private fun handleWalletResult(it: Response<WalletResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        val px = it.conversion!!.point.toDouble()
                        val rx = it.conversion!!.rupee.toDouble()
                        val walletAmount = it.wallet!!.totalAmount
                        setPoints(px, rx, walletAmount)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {}
        }
    }

    private fun handleUdharCreditLimitResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.isSuccess && it.dynamicData != null && it.dynamicData!!.amount > 0) {
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
                            "( " + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                                R.string.checkbook_contact
                            ) + ")"
                        mBinding.tvContactUs.autoLinkMask = Linkify.PHONE_NUMBERS
                        if (it.dynamicData != null) {
                            if (!TextUtils.isNullOrEmpty(it.message)) {
                                mBinding.tvMessage.visibility = View.VISIBLE
                                mBinding.tvMessage.text = it.message
                            }
                            if (!it.dynamicData!!.showHideLimit) {
                                mBinding.tvLimitSk.visibility = View.VISIBLE
                                mBinding.tvLimitSk.text = "₹ " + it.dynamicData!!.amount + "/-"
                            }
                        }
                    }
                    if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE) {
                        mBinding.tvPayLaterH.visibility = View.GONE
                    }
                }
            }

            is Response.Error -> {
                mBinding.tvContactUs.visibility = View.VISIBLE
                mBinding.tvContactUs.textSize = 15f
                mBinding.tvContactUs.text =
                    "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountSkC.visibility = View.GONE
                mBinding.btnCallSkC.visibility = View.VISIBLE
                mBinding.radioSkC.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
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
                            Utils.setToast(
                                applicationContext, MyApplication.getInstance().dbHelper.getString(
                                    R.string.no_data_available
                                )
                            )
                        }
                        if (ePayLimit == 0.0) {
                            mBinding.tvLimit.textSize = 15f
                            mBinding.tvLimit.text =
                                "( " + MyApplication.getInstance().dbHelper.getString(
                                    R.string.contact_on
                                ) + SharePrefs.getInstance(
                                    applicationContext
                                ).getString(SharePrefs.COMPANY_CONTACT) + ")"
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
                mBinding.tvLimit.textSize = 14f
                mBinding.tvLimit.text =
                    "(" + MyApplication.getInstance().dbHelper.getString(R.string.contact_on) + " +91" + SharePrefs.getInstance(
                        applicationContext
                    ).getString(SharePrefs.COMPANY_CONTACT) + ")"
                mBinding.etAmountEpay.visibility = View.GONE
                mBinding.callBtn.visibility = View.VISIBLE
                mBinding.radioEpay.visibility = View.GONE
                mBinding.rlEpay.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
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

            is Response.Error -> {
                mBinding.tvLimitCb.textSize = 14f
                mBinding.tvLimitCb.text =
                    "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountCb.visibility = View.GONE
                mBinding.callBtnCb.visibility = View.VISIBLE
                mBinding.radioCheckbook.visibility = View.GONE
                mBinding.rlCheckBook.visibility = View.GONE
                if (mBinding.rlSkCredit.visibility == View.GONE && mBinding.rlEpay.visibility == View.GONE && mBinding.rlCheckBook.visibility == View.GONE) {
                    mBinding.tvPayLaterH.visibility = View.GONE
                }
            }
        }
    }

    private fun handleCheckCODLimitResult(it: Response<JsonElement>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        val jsonObject = it.asJsonObject
                        if (jsonObject["IsCustomCODLimit"].asBoolean) {
                            codLimit = jsonObject["CODLimit"].asDouble
                            availableCODLimit = jsonObject["AvailableCODLimit"].asDouble.toLong()
                            mBinding.tvCodLimit.text =
                                MyApplication.getInstance().noteRepository.getString(
                                    R.string.maximum_cod_limit
                                ) + " ₹" + codLimit
                            mBinding.tvCodLimitAvail.text =
                                MyApplication.getInstance().noteRepository.getString(
                                    R.string.available_cod_limit
                                ) + " ₹" + availableCODLimit
                            mBinding.rlCodLimit.visibility = View.VISIBLE
                            if (availableCODLimit <= 0) {
                                mBinding.cbCash.isEnabled = false
                                mBinding.cbCash.isClickable = false
                                mBinding.cbCash.isFocusable = false
                            }
                        } else {
                            availableCODLimit = -1
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {}
        }
    }

    private fun handleScaleUpLimitResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    scaleUpLimit = it.creditLimit
                    mBinding.tvLimitSu.text =
                        "₹ " + DecimalFormat("##.##").format(scaleUpLimit) + "/-"
                    if (it.isBlock || scaleUpLimit <= 0) {
                        mBinding.tvLimitSu.text = ""
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
                hideProgressDialog()
                mBinding.tvContactUs.visibility = View.VISIBLE
                mBinding.tvContactUs.textSize = 15f
                mBinding.tvContactUs.text =
                    "(" + getString(R.string.contact_on) + MyApplication.getInstance().dbHelper.getString(
                        R.string.checkbook_contact
                    ) + ")"
                mBinding.etAmountSu.visibility = View.GONE
                mBinding.btnCallSu.visibility = View.VISIBLE
                mBinding.radioScaleUp.visibility = View.GONE
            }
        }
    }

    private fun handleOfferResult(it: Response<BillDiscountListResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    mBinding.placeBtn.isClickable = true
                    isOfferApiCalled = true
                    if (it.isStatus) {
                        if (it.billDiscountList != null && it.billDiscountList.size > 0) {
                            discountList = it.billDiscountList
                            if (!TextUtils.isNullOrEmpty(
                                    mShoppingCart!!.applyOfferId
                                )
                            ) {
                                val str = mShoppingCart!!.applyOfferId
                                val arrOfStr =
                                    str!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()
                                for (i in discountList!!.indices) {
                                    for (a in arrOfStr) {
                                        if (discountList!![i].offerId == a.toInt()) {
                                            discountList!![i].isSelected = true
                                            if (discountList!![i].billDiscountOfferOn.equals(
                                                    "FreeItem", ignoreCase = true
                                                )
                                            ) {
                                                mBinding.rlDiscountFreeItem.visibility =
                                                    View.VISIBLE
                                            }
                                            break
                                        }
                                    }
                                }
                            }
                            for (i in discountList!!.indices) {
                                val offerCheck = OfferCheck(
                                    discountList!![i], mShoppingCart!!
                                )
                                discountList!![i].isApplicable = offerCheck.checkCoupon()
                                discountList!![i].message = offerCheck.message
                            }
                            discountAdapter = BillDiscountOfferAdapter(
                                this@PaymentOptionActivity,
                                discountList,
                                this@PaymentOptionActivity,
                                this@PaymentOptionActivity
                            )
                            discountAdapter!!.notifyDataSetChanged()
                            Collections.sort(discountList, AscComparator())
                        }
                    }

                }
            }

            is Response.Error -> {
                mBinding.placeBtn.isClickable = true
            }
        }
    }

    private fun handlePrepaidOrderResult(it: Response<PrepaidOrder>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.isStatus) {
                        PrepaidOrderStatus = true
                        prepaidOrderModel = it.prepaidOrderModel
                        // prepaidOrderModel.setCashPercentage(50);
                        // prepaidOrderModel.setOnlinePercentage(50);
                        // prepaidOrderModel.setOrderAmount(0);
                        PrepaidOrder(grossTotalAmount + deliveryCharges, true)
                    } else {
                        PrepaidOrderStatus = false
                    }
                    hideProgressDialog()

                }
            }

            is Response.Error -> {
                hideProgressDialog()
            }
        }
    }

    private fun handleCheckOfferResult(it: Response<CheckBillDiscountResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it.isStatus) {
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
                        Utils.setToast(
                            applicationContext, it.billDiscount!![0].message
                        )
                        startActivity(
                            Intent(applicationContext, ShoppingCartActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                        )
                        Utils.rightTransaction(this@PaymentOptionActivity)
                        finish()
                    }
                }
            }

            is Response.Error -> {
                mBinding.placeBtn.isClickable = true
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
                    hideProgressDialog()
                    var flag = false
                    if (utils!!.isNetworkAvailable) {
                        try {
                            if (it.status.equals(
                                    "confirmed", ignoreCase = true
                                ) || it.status.equals("delivered", ignoreCase = true)
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
            }

            is Response.Error -> {
                hideProgressDialog()
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
    }

    private fun handleGeneratePaymentResult(it: Response<CreditLimit>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog()
            }

            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    if (it.isSuccess) {
                        val url = it.message
                        startActivityForResult(
                            Intent(
                                applicationContext, DirectUdharActivity::class.java
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
                showProgressDialog()
            }

            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    if (it.status) {
                        transactionId = it.transactionId
                        val baseUrl = it.BaiseUrl
                        if (EndPointPref.getInstance(applicationContext)
                                .getBoolean(EndPointPref.IS_SCALE_UP_SDK)
                        ) {
                            val json = JSONObject()
                            try {
                                json.put("isPayNow", true)
                                json.put("transactionId", transactionId)
                                json.put("baseUrl", baseUrl)
                                Log.e(TAG, "onNext Payment Optin transactionId: $transactionId")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            methodChannel!!.invokeMethod("ScaleUP", json.toString())
                            startActivity(
                                FlutterActivity.withCachedEngine(FLUTTER_ENGINE_ID)
                                    .build(applicationContext)
                            )
                        } else {
                            if (!TextUtils.isNullOrEmpty(it.url)) {
                                startActivityForResult(
                                    Intent(
                                        applicationContext, ScaleUpActivity::class.java
                                    ).putExtra("url", it.url), SCALEUP
                                )
                            }
                        }
                    } else {
                        mBinding.placeBtn.isClickable = true
                        showAlert(it.message)
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleOrderPlaceResult(it: Response<OrderPlacedNewResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        hideOrderPlaceDialog()
                        orderPlacedNewResponse = it
                        if (orderPlacedNewResponse!!.isSuccess) {
                            // disable walletPoint after order - devendra
                            mBinding.pointEt.isFocusable = false
                            SharePrefs.getInstance(applicationContext)
                                .putBoolean(SharePrefs.IS_GULLAK_BAL, false)
                            // clear cart
                            MyApplication.getInstance().noteRepository.truncateCart()
                            if (!TextUtils.isNullOrEmpty(
                                    orderPlacedNewResponse!!.cart!!.createdDate
                                )
                            ) {
                                createdDate = orderPlacedNewResponse!!.cart!!.createdDate!!
                            } else {
                                val date: String
                                val calendar = Calendar.getInstance()
                                val dateFormat =
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                                date = try {
                                    dateFormat.format(calendar.time)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    ""
                                }
                                createdDate = date
                            }
                            orderId = orderPlacedNewResponse!!.orderMaster!!.orderid
                            if (orderPlacedNewResponse!!.cart!!.isTrupay) {
                                if (hdfcAmount > 0) {
                                    getHDFCRSAKey(false)
                                } else if (creditAmount > 0) {
                                    getHDFCRSAKey(true)
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
                                            MyApplication.getInstance().dbHelper.getString(
                                                R.string.no_sufficient_limit
                                            )
                                        )
                                    }
                                } else if (scaleUpAmt > 0) {
                                    scaleUpPayment()
                                } else if (razorpayAmount > 0) {
                                    getRazorpayOrderId()
                                } else if (iCICIPayAmount > 0) {
                                    callICICIPay()
                                } else {
                                    if (orderPlacedNewResponse!!.orderMaster!!.wheelcount != 0) {
                                        orderConformationPopup(
                                            1,
                                            orderPlacedNewResponse!!.orderMaster,
                                            orderPlacedNewResponse!!.orderMaster!!.wheelcount,
                                            orderPlacedNewResponse!!.earnWalletPoint,
                                            orderPlacedNewResponse!!.kisanDaanAmount
                                        )
                                        clearCartData()
                                    } else {
                                        goToHome("success")
                                    }
                                }
                            } else {
                                if (orderPlacedNewResponse!!.orderMaster!!.wheelcount != 0) {
                                    orderConformationPopup(
                                        1,
                                        orderPlacedNewResponse!!.orderMaster,
                                        orderPlacedNewResponse!!.orderMaster!!.wheelcount,
                                        orderPlacedNewResponse!!.earnWalletPoint,
                                        orderPlacedNewResponse!!.kisanDaanAmount
                                    )
                                    clearCartData()
                                } else {
                                    goToHome("success")
                                }
                                // update purchase analytics
                                MyApplication.getInstance().updateAnalyticPurchase(
                                    "" + orderId,
                                    orderPlacedNewResponse!!.orderMaster!!.totalamount,
                                    mShoppingCart!!.deliveryCharges,
                                    mShoppingCart!!.applyOfferId,
                                    "" + orderId,
                                    orderPlacedNewResponse!!.orderMaster!!.wheelcount,
                                    "pod",
                                    mShoppingCart!!.shoppingCartItemDcs
                                )
                            }
                        } else {
                            mBinding.placeBtn.isClickable = true
                            if (orderPlacedNewResponse!!.cart != null) {
                                showAlertGoToShoppingCart("" + orderPlacedNewResponse!!.message)/* Utils.setToast(getApplicationContext(), "" + orderPlacedNewResponse.getMessage());
                            Intent intent = new Intent(getApplicationContext(), ShopingCartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("REVIEW_SCREEN", true);
                            startActivity(intent);*/
                            } else {
                                showAlert("" + orderPlacedNewResponse!!.message)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            is Response.Error -> {
                hideOrderPlaceDialog()
                goToHome("exception")
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleInsertTransactionResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog()
            }

            is Response.Success -> {
                it.data?.let {
                    hideProgressDialog()
                    if (updateCashStatus) {
                        var paymentMode = ""
                        paymentMode = if (holePaymentSucceedCheck) {
                            "Online"
                        } else {
                            "Offline"
                        }
                        updatePaymentAPICall(paymentMode, true, transactionId, paymentThrough)
                    } else {
                        if (holePaymentSucceedCheck) {
                            if (cashAmount > 0) {
                                insertCashPaymentStatusAPICall(
                                    "Success", "Cash", cashAmount.toDouble(), "Offline"
                                )
                            } else {
                                updatePaymentAPICall("Online", true, transactionId, paymentThrough)
                            }
                        }
                    }
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                mBinding.placeBtn.isClickable = true
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleUpdateTransactionResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog()
            }

            is Response.Success -> {
                it.data?.let {
                    try {
                        hideProgressDialog()
                        if (it) {
                            if (!IsSuccess) {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(R.string.payment_cancel)
                                )
                                startActivity(
                                    Intent(
                                        applicationContext, HomeActivity::class.java
                                    ).setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                    )
                                )
                                Utils.rightTransaction(this@PaymentOptionActivity)
                            } else {
                                if (otherPaymentMode) {
                                    if (orderPlacedNewResponse!!.orderMaster!!.wheelcount != 0) {
                                        orderConformationPopup(
                                            1,
                                            orderPlacedNewResponse!!.orderMaster,
                                            orderPlacedNewResponse!!.orderMaster!!.wheelcount,
                                            orderPlacedNewResponse!!.earnWalletPoint,
                                            orderPlacedNewResponse!!.kisanDaanAmount
                                        )
                                        clearCartData()
                                    } else {
                                        goToHome("success")
                                        //  Utils.setToast(getApplicationContext(), "Order confirmed");
                                    }
                                } else {
                                    startActivity(
                                        Intent(
                                            applicationContext, ShoppingCartActivity::class.java
                                        ).setFlags(
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        )
                                    )
                                    Utils.rightTransaction(this@PaymentOptionActivity)
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
                }
            }

            is Response.Error -> {
                hideProgressDialog()
                goToHome("exception")
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleICICITransactionResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                showProgressDialog()
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