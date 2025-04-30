package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderMaster
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityOrderDetailsNewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DialWheelActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.OrderDetailsAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.OrderPaymentDetailAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.OrderStatusAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.EtaDates
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.DBoyRatingFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.SalesRateFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import io.reactivex.observers.DisposableObserver

class OrderDetailsNewActivity : AppCompatActivity(), OnButtonClick {
    private var mBinding: ActivityOrderDetailsNewBinding? = null

    private var etaDatesList: ArrayList<EtaDates.ETADate>? = null
    private var orderList: List<OrderDetailsModel>? = null
    private var orderId = 0
    private var status: String? = null
    private var etaDate: String? = null
    private var dboyNumber: String? = ""
    private var orderByNumber: String? = ""
    private var eTADate: String? = null
    private var deliveryOtp = 0
    private var orderType = 0
    private var grossAmount = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details_new)

        if (intent.extras != null) {
            deliveryOtp = intent.getIntExtra("deliveryOtp", 0)
            orderId = intent.getIntExtra("orderId", 0)
            status = intent.getStringExtra("status")
            orderType = intent.getIntExtra("orderType", 0)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        initialization()
        Utils.showProgressDialog(this)
        CommonClassForAPI.getInstance(this).getOrderDetail(orderDetailObserver, orderId)
    }


    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        mBinding!!.btnRate.visibility = View.GONE
        onPostCreate(null)
    }


    fun initialization() {
        mBinding!!.toolbarOd.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_details)
        mBinding!!.tvDetail.text =
            MyApplication.getInstance().dbHelper.getString(R.string.payment_detail)
        mBinding!!.tvDeliveryDateH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.your_selected_delivery_date)
        mBinding!!.tvOrderDetailH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_details)
        mBinding!!.tvOrderAmtH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.order_amount)
        mBinding!!.tvAmtPayableH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.amount_payble)
        mBinding!!.tvItemNameH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_name)
        mBinding!!.tvQtyHead.text = MyApplication.getInstance().dbHelper.getString(R.string.qty)
        mBinding!!.tvAmountH.text = MyApplication.getInstance().dbHelper.getString(R.string.amount)

        mBinding!!.toolbarOd.back.setOnClickListener { onBackPressed() }

        mBinding!!.scrollView.visibility = View.GONE

        mBinding!!.btnChangeDate.setOnClickListener {
            if (etaDatesList != null && etaDatesList!!.size > 0) {
                showDeliveryDateDialog()
            } else {
                Utils.setToast(
                    applicationContext, "No dates available"
                )
            }
        }
        mBinding!!.btnRate.setOnClickListener {
            Utils.showProgressDialog(
                this
            )
            CommonClassForAPI.getInstance(this).getAppHomeBottomData(
                object :
                    DisposableObserver<ArrayList<RatingModel>?>() {
                    override fun onNext(ratingList1: ArrayList<RatingModel>) {
                        Utils.hideProgressDialog()
                        if (ratingList1.size > 0) {
                            DBoyRatingFragment.newInstance(0, ratingList1)
                                .show(supportFragmentManager, "a")
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                    }
                },
                EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/api/RetailerApp/GetDboyRatingOrder/" + orderId
            )
            CommonClassForAPI.getInstance(this).getAppHomeBottomData(
                object :
                    DisposableObserver<ArrayList<RatingModel>?>() {
                    override fun onNext(ratingList1: ArrayList<RatingModel>) {
                        Utils.hideProgressDialog()
                        if (ratingList1.size > 0) {
                            SalesRateFragment.newInstance(0, ratingList1)
                                .show(supportFragmentManager, "a")
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                    }
                },
                EndPointPref.getInstance(MyApplication.getInstance()).baseUrl + "/api/RetailerApp/GetSalesManRatingOrder/" + orderId
            )
        }

        if (deliveryOtp != 0) {
            mBinding!!.tvOtp.visibility = View.VISIBLE
            mBinding!!.tvOtp.text = "Delivery OTP $deliveryOtp"
        } else {
            mBinding!!.tvOtp.visibility = View.GONE
        }
        if (status.equals("Delivered", ignoreCase = true) ||
            status.equals("sattled", ignoreCase = true) ||
            status.equals("partial settled", ignoreCase = true) ||
            status.equals("Account settled", ignoreCase = true)
        ) {
            mBinding!!.LLCall.visibility = View.GONE
            mBinding!!.LLDeliveredCall.visibility = View.GONE
        }

        mBinding!!.playWheelBtn.setOnClickListener {
            val orderMaster = OrderMaster()
            orderMaster.orderid = orderList!![0].orderid
            orderMaster.customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            orderMaster.dialearnigpoint = 0
            orderMaster.wheelcount = orderList!![0].wheelCount
            orderMaster.wheelList = orderList!![0].wheelList
            orderMaster.orderid = orderList!![0].orderid
            orderMaster.playedWheelCount = 0

            MyApplication.getInstance().updateAnalytics("play_dial_click_by_order_screen")
            val bundle = Bundle()
            bundle.putSerializable(
                Constant.ORDER_MODEL,
                orderMaster
            )
            val newIntent = Intent(applicationContext, DialWheelActivity::class.java)
            newIntent.putExtras(bundle)
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(newIntent)
            finish()
            Utils.leftTransaction(
                this
            )
        }
        mBinding!!.LLCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$orderByNumber"))
            startActivity(intent)
        }
        mBinding!!.LLDeliveredCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$dboyNumber"))
            startActivity(intent)
        }
        mBinding!!.liDetail.setOnClickListener {
            paymentDetailPopup()
        }
    }

    fun paymentDetailPopup() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.payment_detail_popup)
        val totalamnt = dialog.findViewById<TextView>(R.id.tv_total_amnt)
        val tvDTotalAmount = dialog.findViewById<TextView>(R.id.tvDTotalAmount)
        val rvDetail = dialog.findViewById<RecyclerView>(R.id.rv_detail)

        tvDTotalAmount!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_amount)
        totalamnt!!.text = "" + grossAmount
        rvDetail!!.adapter =
            OrderPaymentDetailAdapter(applicationContext, orderList!![0].orderPayments)

        dialog.show()
    }

    private fun showDeliveryDateDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.popup_delivery_date)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCanceledOnTouchOutside(false)

        val tvOrderId = dialog.findViewById<TextView>(R.id.tv_order_id)
        val tvETAH = dialog.findViewById<TextView>(R.id.tvEtaH)
        val tvETA = dialog.findViewById<TextView>(R.id.tvETA)
        val tvDelayH = dialog.findViewById<TextView>(R.id.tvDelayH)
        val cgDate = dialog.findViewById<FlexboxLayout>(R.id.cgDate)
        // Button btnChangeDate = dialog.findViewById(R.id.btnChangeDate);
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        val orderIdText =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_order_id) + "<font color=#000000>" + orderId
        tvOrderId!!.text = Html.fromHtml(orderIdText)
        tvETAH!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.expected_normal_delivery)
        tvETA!!.text = Utils.getDateMonthFormat(etaDate)
        tvDelayH!!.text = MyApplication.getInstance().dbHelper.getString(R.string.delay_delivery)
        btnSave!!.text = MyApplication.getInstance().dbHelper.getString(R.string.update)

        val viewList: MutableList<TextView> = ArrayList()
        for (i in etaDatesList!!.indices) {
            val params = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(15, 5, 15, 10)
            val textView = TextView(this)
            textView.layoutParams = params
            textView.background = resources.getDrawable(R.drawable.rectangle_grey)
            textView.setPadding(40, 20, 40, 20)
            textView.text = Utils.formatToDateMonth(
                etaDatesList!![i].etaDate
            )
            textView.tag = etaDatesList!![i].etaDate
            viewList.add(textView)

            cgDate!!.addView(textView)
            textView.setOnClickListener { view: View ->
                tvETA.background = resources.getDrawable(R.drawable.rectangle_grey)
                eTADate = textView.tag.toString()
                for (view1 in viewList) {
                    view1.background = resources.getDrawable(R.drawable.rectangle_grey)
                }
                view.background = resources.getDrawable(R.drawable.rectangle_orange)
            }
        }

        /*btnChangeDate.setOnClickListener(view -> {
            btnChangeDate.setVisibility(View.GONE);
            tvDelayH.setVisibility(View.VISIBLE);
            cgDate.setVisibility(View.VISIBLE);
        });*/
        tvETA.setOnClickListener { view: View? ->
            eTADate = tvETA.text.toString()
            tvETA.background = resources.getDrawable(R.drawable.rectangle_orange)
            for (i in viewList.indices) viewList[i].background =
                resources.getDrawable(R.drawable.rectangle_grey)
        }
        btnSave.setOnClickListener { v: View? ->
            if (TextUtils.isNullOrEmpty(
                    eTADate
                )
            ) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.select_delivery_date)
                )
            } else {
                Utils.showProgressDialog(
                    this
                )
                val jsonObject = JsonObject()
                jsonObject.addProperty("OrderId", orderId)
                jsonObject.addProperty("ETADate", eTADate)
                jsonObject.addProperty("lang", LocaleHelper.getLanguage(this))
                CommonClassForAPI.getInstance(this).updateDeliveryETA(
                    delivETAObserver,
                    jsonObject, "Payment Screen"
                )
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun updateEtaButton(isEta: Boolean) {
        if (isEta) {
            mBinding!!.btnChangeDate.isEnabled = true
            mBinding!!.btnChangeDate.visibility = View.VISIBLE
            CommonClassForAPI.getInstance(this).getOrderETADate(etaDatesObserver, orderId)
        } else {
            mBinding!!.btnChangeDate.isEnabled = false
            mBinding!!.btnChangeDate.visibility = View.GONE
            mBinding!!.btnChangeDate.background = ColorDrawable(R.color.divider)
        }
    }

    private fun createChargeView(list : ArrayList<OrderDetailsModel.OrderCharges>) {
        for (model in list) {
            val rlConvenience = RelativeLayout(this)
            val rlParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            rlParams.setMargins(3, 5, 3, 0)
            rlConvenience.setLayoutParams(rlParams)
            rlConvenience.setId(View.generateViewId())

            // Creating the first TextView (tvConvenienceFeeT)
            val tvConvenienceFeeT = TextView(this)
            val tvParamsT = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            tvParamsT.addRule(RelativeLayout.ALIGN_PARENT_START)
            tvConvenienceFeeT.setLayoutParams(tvParamsT)
            tvConvenienceFeeT.setId(View.generateViewId())
            tvConvenienceFeeT.setText(model.ChargesType)
            tvConvenienceFeeT.setTextColor(ContextCompat.getColor(this, R.color.secondary_text))
            tvConvenienceFeeT.setTextAppearance(this, R.style.CommonStyleSR)

            // Creating the second TextView (tvConvenienceFee)
            val tvConvenienceFee = TextView(this)
            val tvParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            tvParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            tvConvenienceFee.setLayoutParams(tvParams)
            tvConvenienceFee.setId(View.generateViewId())
            tvConvenienceFee.setText("" + model.Amount)
            tvConvenienceFee.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            tvConvenienceFee.setTextAppearance(this, R.style.CommonStyleSB)

            // Adding views to RelativeLayout
            rlConvenience.addView(tvConvenienceFeeT)
            rlConvenience.addView(tvConvenienceFee)

            // Adding RelativeLayout to parent layout
            mBinding!!.liCharges.addView(rlConvenience)
        }
    }


    // order status dialog
    private fun showOrderStatusDialog(list: ArrayList<ReturnOrderStatusModel>) {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_order_status)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)

        val rvStatus = dialog.findViewById<RecyclerView>(R.id.rvStatus)
        val adapter = OrderStatusAdapter(this, list)
        rvStatus!!.adapter = adapter

        dialog.show()
        MyApplication.getInstance().updateAnalytics("order_status_dialog")
    }


    private val orderDetailObserver: DisposableObserver<List<OrderDetailsModel>> =
        object : DisposableObserver<List<OrderDetailsModel>>() {
            override fun onNext(list: List<OrderDetailsModel>) {
                Utils.hideProgressDialog()
                if (list != null && list.size > 0) {
                    orderList = list
                    mBinding!!.scrollView.visibility = View.VISIBLE
                    mBinding!!.tvNoDataFound.visibility = View.GONE
                    mBinding!!.rvOrderDetails.adapter =
                        OrderDetailsAdapter(this@OrderDetailsNewActivity, list)

                    grossAmount = list[0].grossAmount
                    mBinding!!.tvOrderId.text = "Order Id: " + list[0].getOrderid()
                    mBinding!!.tvAmountPayble.text = "₹" + list[0].payableAmount
                    mBinding!!.tvAmountOrder.text = "₹" + list[0].grossAmount
                    mBinding!!.tvTotalItemCount.text =
                        resources.getString(R.string.total_item_order) + " " + list.size
                    mBinding!!.tvTotalQtyCount.text =
                        resources.getString(R.string.total_qty_order) + " " + list[0].totalQty
                    mBinding!!.tvOrderBookedByRating.text =
                        if (list[0].orderTakenRating == null) "" else "" + list[0].orderTakenRating
                    mBinding!!.tvDeliveredByRating.text =
                        if (list[0].deliveryBoyRating == null) "" else "" + list[0].deliveryBoyRating
                    mBinding!!.dialAvailable.text = list[0].wheelCount.toString() + ""

                    if (list[0].isPlayWheel) {
                        mBinding!!.liWheel.visibility = View.VISIBLE
                    } else {
                        mBinding!!.liWheel.visibility = View.GONE
                    }
                    if (list[0].deliveredBy == null || list[0].deliveredBy == "") {
                        mBinding!!.llOrderDeliveredBy.visibility = View.GONE
                    } else {
                        mBinding!!.llOrderDeliveredBy.visibility = View.VISIBLE
                        mBinding!!.tvDeliveredByName.text = list[0].deliveredBy
                        Picasso.get()
                            .load(
                                EndPointPref.getInstance(applicationContext)
                                    .getString(EndPointPref.API_ENDPOINT)
                                        + list[0].dboyProfilePic
                            )
                            .placeholder(R.drawable.logo_grey)
                            .error(R.drawable.logo_grey)
                            .into(mBinding!!.imOrderDelivered)
                        dboyNumber = list[0].deliveryPersonMobile
                        mBinding!!.ratingbar.rating = list[0].rating
                        if (status != null && status.equals(
                                "Delivered",
                                ignoreCase = true
                            ) || status.equals(
                                "sattled",
                                ignoreCase = true
                            ) || status.equals(
                                "partial settled",
                                ignoreCase = true
                            ) || status.equals(
                                "Account settled",
                                ignoreCase = true
                            ) && list[0].rating == 0f
                        ) {
                            mBinding!!.btnRate.visibility = View.VISIBLE
                        } else {
                            mBinding!!.btnRate.visibility = View.GONE
                        }
                    }

                    if (list[0].orderTakenBy == null || list[0].orderTakenBy == "") {
                        mBinding!!.llOrderBookedBy.visibility = View.GONE
                    } else {
                        mBinding!!.llOrderBookedBy.visibility = View.VISIBLE
                        mBinding!!.tvOrderBookedByName.text = list[0].orderTakenBy
                        Picasso.get()
                            .load(
                                EndPointPref.getInstance(applicationContext)
                                    .getString(EndPointPref.API_ENDPOINT) +
                                        list[0].orderTakenSalesPersonProfilePic
                            )
                            .placeholder(R.drawable.logo_grey)
                            .error(R.drawable.logo_grey)
                            .into(mBinding!!.imOrderBooked)
                        orderByNumber = list[0].orderTakenSalesPersonMobile
                    }

                    if (list[0].deliverydate != null) {
                        etaDate = list[0].deliverydate
                        mBinding!!.tvDeliveryDate.text =
                            "" + Utils.getDateMonthFormat(
                                list[0].deliverydate
                            )
                        if (orderType != 8) updateEtaButton(list[0].isEta)
                    }

                    if (list[0].orderCharges != null && list[0].orderCharges.isNotEmpty()) {
                        createChargeView(list[0].orderCharges)
                    } else {
                        mBinding!!.liCharges.visibility = View.GONE
                    }
                } else {
                    mBinding!!.scrollView.visibility = View.GONE
                    mBinding!!.tvNoDataFound.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                mBinding!!.scrollView.visibility = View.GONE
                mBinding!!.liDetail.visibility = View.GONE
                mBinding!!.tvNoDataFound.visibility = View.VISIBLE
            }

            override fun onComplete() {
            }
        }

    private val etaDatesObserver: DisposableObserver<EtaDates> =
        object : DisposableObserver<EtaDates>() {
            override fun onNext(dates: EtaDates) {
                Utils.hideProgressDialog()
                if (dates.etaDates != null && dates.etaDates.size > 0) {
                    etaDatesList = dates.etaDates
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
            }
        }

    // update delivery date
    private val delivETAObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(`object`: JsonObject) {
                try {
                    Utils.hideProgressDialog()
                    if (`object`["status"].asBoolean) {
                        CommonClassForAPI.getInstance(this@OrderDetailsNewActivity)
                            .getOrderDetail(orderDetailObserver, orderId)
                        Utils.setToast(
                            applicationContext, `object`["Message"].asString
                        )
                    } else {
                        Utils.setToast(
                            applicationContext, `object`["Message"].asString
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

            override fun onComplete() {
            }
        }
}