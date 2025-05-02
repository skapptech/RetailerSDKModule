package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityOrdersummaryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderSummaryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class OrderSummaryActivity : AppCompatActivity() {
    private lateinit var mbinding: ActivityOrdersummaryBinding

    private var deliveredtotalamount = 0.0
    private var canceltotalamount = 0.0
    private var pendingtotalamount = 0.0
    private var totalorderOfcancel = 0
    private var totalorderofdelivered = 0
    private var totalorderofpending = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_ordersummary)
        //init view
        initialization()
        // back btn
        mbinding.toolbarOrderSummary.back.setOnClickListener { onBackPressed() }
        mbinding.toolbarOrderSummary.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_summary)
        mbinding.status1.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.status)
        mbinding.totalorder.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.totalorder)
        mbinding.amount.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.amount)
        mbinding.del.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.delivered)
        mbinding.cancel.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.canceled)
        mbinding.pending.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.pending)
        mbinding.order.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.totalorder)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
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
        val skcode = SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE)
        val commonClassForAPI = CommonClassForAPI.getInstance(this)
        val utils = Utils(this)

        // API call
        if (utils.isNetworkAvailable) {
            Utils.showProgressDialog(this)
            commonClassForAPI?.Fetchordersummary(getOrderSummaryDes, 30, skcode)
        } else {
            Utils.setToast(
                this,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // Order Summary data
    private val getOrderSummaryDes: DisposableObserver<List<OrderSummaryModel>> =
        object : DisposableObserver<List<OrderSummaryModel>>() {
            override fun onNext(orderList: List<OrderSummaryModel>) {
                Utils.hideProgressDialog()
                for (i in orderList.indices) {
                    if (orderList[i].status == "Order Canceled" || orderList[i].status == "DialModel Canceled" || orderList[i].status.equals(
                            "Delivery Canceled",
                            ignoreCase = true
                        )
                    ) {
                        canceltotalamount += orderList[i].totalAmount
                        totalorderOfcancel += orderList[i].totalOrder
                    } else if (orderList[i].status.equals(
                            "Pending",
                            ignoreCase = true
                        ) || orderList[i].status.equals("Ready to Dispatch", ignoreCase = true)
                    ) {
                        pendingtotalamount += orderList[i].totalAmount
                        totalorderofpending += orderList[i].totalOrder
                    } else  /*if(obj.getString("status").equalsIgnoreCase("Delivered")||obj.getString("status").equalsIgnoreCase("sattled"))*/ {
                        deliveredtotalamount += orderList[i].totalAmount
                        totalorderofdelivered += orderList[i].totalOrder
                    }
                }
                mbinding.cancelamount.text = DecimalFormat("##.##").format(canceltotalamount)
                mbinding.count3.text = totalorderOfcancel.toString()
                mbinding.pendingamount.text = DecimalFormat("##.##").format(pendingtotalamount)
                mbinding.pendingcount.text = totalorderofpending.toString()
                mbinding.deliveredamount.text =
                    DecimalFormat("##.##").format(deliveredtotalamount)
                mbinding.deliveredcount.text = totalorderofdelivered.toString()
                val alltotalamount = canceltotalamount + deliveredtotalamount + pendingtotalamount
                mbinding.totalamount.text = DecimalFormat("##.##").format(alltotalamount)
                val alltotalcount =
                    (totalorderOfcancel + totalorderofdelivered + totalorderofpending).toDouble()
                mbinding.totalcount.text = alltotalcount.toString()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
            }
        }
}