package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentViewModelFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import com.payphi.customersdk.utils.HmacUtility
import com.payphi.customersdk.views.Application
import com.payphi.customersdk.views.PayPhiSdk
import com.payphi.customersdk.views.PaymentOptionsActivity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PhiPayActivity : AppCompatActivity() {

    private lateinit var amt: String

    // private var merchantId: String = "T_24761"
    // private var appId: String = "80bc18249511f868"
    private var merchantId: String = "T_24761"
    private var appId: String = "80bc18249511f868"
    private lateinit var currencyCode: String
    private lateinit var orderId: String
    private lateinit var application: Application
    private var rnd = Random()
    private lateinit var viewModel: PaymentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phi_pay)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            PaymentViewModelFactory(getApplication(), appRepository)
        )[PaymentViewModel::class.java]

        application = Application() //"80bc18249511f868"
        application.setEnv(application.QA)
        application.setMerchantName("ShopKirana", applicationContext)
        application.setAppInfo(merchantId, appId, applicationContext,
            object : Application.IAppInitializationListener {
                override fun onFailure(errorCode: String?) {
                    if (errorCode == "201") {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Login failed!!$errorCode",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else
                        if (errorCode == "504") {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Connection Error!==$errorCode",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else
                            if (errorCode == "205") {
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Payments not enabled!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else
                                if (errorCode == "101") {
                                    runOnUiThread {
                                        Toast.makeText(
                                            applicationContext,
                                            "internal Error!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                }

                override fun onSuccess(status: String?) {
                    if (status != null) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, status, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })

        findViewById<Button>(R.id.btnPay).setOnClickListener {
            makePayment()
        }
        observe(viewModel.getICICIPaymentCheckData, ::handleICICITransactionResult)

        val rnd = Random()
        var gatewayOrderId = "381218672"
        val value = "T_24761" + gatewayOrderId + gatewayOrderId + "STATUS"
        val secureHash = generateHMAC(value, "abc")
        //   viewModel.getICICIPaymentCheck("https://qa.phicommerce.com/pg/api/command",model)
    }

    private fun handleICICITransactionResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {

            }

            is Response.Success -> {
                it.data?.let {
                    val txnRespDescription = it["txnRespDescription"].asString
                    val txnResponseCode = it["txnResponseCode"].asString
                    val txnID = it["txnID"].asString
                    println("txnRespDescription>>$txnRespDescription<<txnResponseCode>>>$txnResponseCode<<txnID>>>$txnID")
                }
            }

            is Response.Error -> {
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }


    private fun makePayment() {
        currencyCode = "356"
        orderId = (10000009080 + rnd.nextInt(900768000)).toString()
        amt = "5.0"
        val scecureToken: String? = getSecureToken()
        val intent = Intent(applicationContext, PaymentOptionsActivity::class.java)

        intent.putExtra("SecureToken", scecureToken)
        intent.putExtra("MerchantID", merchantId)
        // intent.putExtra("aggregatorID", "J_34407")
        intent.putExtra("Amount", amt)
        intent.putExtra("MerchantTxnNo", orderId)
        intent.putExtra("invoiceNo", orderId)
        intent.putExtra("CurrencyCode", 356)
        intent.putExtra("CustomerEmailID", "jai@phicommerce.com")
        intent.putExtra("allowDisablePaymentMode", "CARD")
        intent.putExtra("CustomerID", "50000426")
        intent.putExtra("addlParam1", "7304828261")
        intent.putExtra("addlParam2", "7304828262")
        // intent.putExtra("PaymentType", "[card")
        //intent.putExtra("PaymentType", "nb")
        //intent.putExtra("vpa","ramtest@axis")


        PayPhiSdk.makePayment(
            applicationContext,
            intent,
            PayPhiSdk.DIALOG,
            object : PayPhiSdk.IAppPaymentResponseListenerEx {
                override fun onPaymentResponse(
                    resultCode: Int,
                    data: Intent?,
                    additionalInfo: Map<String?, String?>?
                ) {
                    Toast.makeText(
                        applicationContext,
                        "In /start app==$resultCode",
                        Toast.LENGTH_LONG
                    ).show()
                    var statusCode: Int
                    val bundle1: Bundle? = intent.extras
                    if (bundle1 != null) {
                        for (key in bundle1.keySet()) {
                            val value = bundle1[key]
                            println("Data from main app key=" + key + "Data from main app value=" + value)
                        }
                    }
                    if (resultCode == RESULT_CANCELED) {
                        statusCode = 1
                        showNotificationForActivityResult(statusCode)
                    } else if (resultCode == 2 || resultCode == 4) {
                        statusCode = 2
                        showNotificationForActivityResult(statusCode)
                    } else if (resultCode == 3) {
                        statusCode = 3
                        showNotificationForActivityResult(statusCode)
                    }
                    var paymentDateTime: String? = null
                    var merchantTxnNo: String? = null
                    var paymentID: String? = null
                    val bundle = data?.extras
                    try {
                        paymentDateTime = bundle?.getString("paymentDateTime")
                        merchantTxnNo = bundle?.getString("merchantTxnNo")
                        paymentID = bundle?.getString("paymentID")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (bundle != null) {
                        for (key in bundle.keySet()) {
                            val value = bundle[key]
                            //transaction id
                            Log.d("backtocart", "In paraent : Key= $key value= $value")
                            if (key == "txnResponseCode" || key == "responseCode") {
                                if (value == "0000" || value == "000") {
                                    val dialogM = Dialog(this@PhiPayActivity)
                                    dialogM.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                                        dialogM.setContentView(R.layout.success_popup)
//                                        dialogM.window!!.setBackgroundDrawable(
//                                            ColorDrawable(
//                                                resources.getColor(R.color.black)
//                                            )
//                                        )
//                                        dialogM.window!!.attributes.windowAnimations =
//                                            R.style.AppTheme
//                                        val lp = WindowManager.LayoutParams()
//                                        dialogM.setCanceledOnTouchOutside(false)
//                                        lp.copyFrom(dialogM.window!!.attributes)
//                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//                                        dialogM.window!!.attributes = lp
//                                        val clicker =
//                                            dialogM.findViewById<View>(R.id.sucessButon) as Button
//                                        val date =
//                                            dialogM.findViewById<View>(R.id.dateIdValue) as TextView
//                                        val merRef =
//                                            dialogM.findViewById<View>(R.id.MerchantRefNoValue) as TextView
//                                        val payId =
//                                            dialogM.findViewById<View>(R.id.paymentIdValue) as TextView
//                                        val monthDate: String? = paymentDateTime?.let {
//                                            getFormattedDate(
//                                                it
//                                            )
//                                        }
//                                        date.text = monthDate
//                                        merRef.text = merchantTxnNo
//                                        payId.text = paymentID
//                                        clicker.setOnClickListener {
//                                            dialogM.dismiss()
//                                            finish()
//                                        }
                                    dialogM.show()
                                } else if (value == "R1000") {
                                    statusCode = 6
                                    showNotificationForActivityResult(statusCode)
                                } else {
                                    statusCode = 4
                                    showNotificationForActivityResult(statusCode)
                                }
                            }
                        }
                    }
                }

                override fun onPaymentResponse1(resultCode: Int, data: Intent?) {
                    println("data>>>>>>" + data.toString())
                }

            })


    }

    private fun getSecureToken(): String? {
        val df = DecimalFormat()
        df.minimumFractionDigits = 2
        val f = amt.toFloat()
        df.format(f)
        amt = String.format("%.2f", f)
        val value = amt + currencyCode + merchantId + orderId
        println("TokeString==$`value`")
        // 0d50a3f9aec3492cba25fef9b3c1a3c1
        return generateHMAC(value, "abc")
    }


    private fun generateHMAC(message: String, secretKey: String): String? {
        val sha256_HMAC: Mac
        var hashedBytes: ByteArray? = null
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256")
            val secret_key = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
            sha256_HMAC.init(secret_key)
            hashedBytes = sha256_HMAC.doFinal(message.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return HmacUtility.bytesToHex(hashedBytes!!)
    }


    fun showNotificationForActivityResult(statusCode: Int) {
        val dialogM = Dialog(this)
//        dialogM.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialogM.setContentView(R.layout.cancel)
//        dialogM.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
//        dialogM.window!!.attributes.windowAnimations = R.style.AppTheme //style id
//        val lp = WindowManager.LayoutParams()
//        dialogM.setCanceledOnTouchOutside(false)
//        lp.copyFrom(dialogM.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialogM.window!!.attributes = lp
//        val clicker = dialogM.findViewById<View>(R.id.cancelButton) as Button
//        val infoText = dialogM.findViewById<View>(R.id.infoForError) as TextView
//        val errorImage = dialogM.findViewById<View>(R.id.imageError) as ImageView
//        if (statusCode == 1) {
//            infoText.text = "Your Transaction is canceled or Failed !"
//        } else if (statusCode == 2) {
//            infoText.text = "Application Communication Error Configuration Error !"
//        } else if (statusCode == 3) {
//            infoText.text = "!"
//        } else if (statusCode == 4) {
//            infoText.text = "Your Transaction is rejected , Please Retry !"
//        } else if (statusCode == 5) {
//            infoText.text = "You don't have internet connection !"
//        } else if (statusCode == 6) {
//            infoText.text =
//                "Your UPI payment request has been initiated successfully. You will get notification on your app."
//        }
//        clicker.setOnClickListener {
//            dialogM.dismiss()
//            finish()
//        }
//        dialogM.setCanceledOnTouchOutside(false)
//        dialogM.show()
    }

    fun getFormattedDate(iso8601string: String): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:MM:SS", Locale.US)
        var date: Date? = Date()
        val arr = iso8601string.split("T").toTypedArray()
        val dateInString = arr[0]
        try {
            date = formatter.parse(dateInString)
        } catch (e: java.lang.Exception) {
        }
        return date?.let { dateFormat.format(it) }
    }
}