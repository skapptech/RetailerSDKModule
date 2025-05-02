package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyOrderItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DeliveryConcernActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail.OrderDetailsNewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PayNowActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.DBoyRatingFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.SalesRateFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse.CompanyDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.MyOrderItemInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class MyOrderAdapter(
    private val activity: AppCompatActivity,
    private val list: ArrayList<ConformOrderModel>
) : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {

    private val utils: Utils = Utils(activity)
    private var orderDate: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.activity_my_order_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val obj = list[position]
        //set Strings
        vh.mBinding.tvOrderIdH.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id)
        vh.mBinding.tvOrderIdH2.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id)
        vh.mBinding.tvOrderIdH3.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id)
        vh.mBinding.tvAmountH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_amount)
        vh.mBinding.tvAmountPayableH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_amount1)
        vh.mBinding.btnPayNow.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.pay_now)
        vh.mBinding.tvDetailsSelect.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_View_Detail)
        vh.mBinding.tvOtp.visibility = View.GONE
        vh.mBinding.tvOrderId.text = "" + obj.orderId
        vh.mBinding.tvOrderId2.text = "" + obj.orderId
        vh.mBinding.tvOrderId3.text = "" + obj.orderId
        vh.mBinding.tvAmount.text = "₹" + obj.grossAmount
        vh.mBinding.tvPayableAmt.text = "₹" + obj.remainingAmount
        vh.mBinding.tvDBoy.text = obj.deliveryPerson
        vh.mBinding.tvDBoyR.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.delivery_person)
        vh.mBinding.tvSalesP.text = obj.salesPerson
        vh.mBinding.tvSalesPR.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.sales_person)
        if (obj.remainingAmount > 0) {
            vh.mBinding.liAmtP.visibility = View.VISIBLE
        } else {
            vh.mBinding.liAmtP.visibility = View.GONE
        }
        Picasso.get().load(EndPointPref.getInstance(activity).baseUrl + obj.dBoyProfilePic)
            .placeholder(R.drawable.logo_grey)
            .error(R.drawable.logo_grey)
            .into(vh.mBinding.ivImage)
        Picasso.get().load(EndPointPref.getInstance(activity).baseUrl + obj.salesPersonProfilePic)
            .placeholder(R.drawable.logo_grey)
            .error(R.drawable.logo_grey)
            .into(vh.mBinding.ivImageS)
        vh.mBinding.ratingbar.rating = obj.rating
        if (obj.enablePayNowButton) {
            vh.mBinding.btnPayNow.visibility = View.VISIBLE
        } else {
            vh.mBinding.btnPayNow.visibility = View.GONE
        }
        val myOrderItemInfo = MyOrderItemInfo()
        if (obj.orderType == 8) {
            vh.mBinding.tvSelfOrder.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.clearance_order)
        } else if (obj.isRebookOrder) {
            vh.mBinding.tvSelfOrder.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.rebook)
        } else {
            vh.mBinding.tvSelfOrder.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.self_order)
        }
        myOrderItemInfo.orderNumber = obj.orderId.toString()
        val text =
            "<font color=#FF4500>&#8377; " + DecimalFormat("#,###.##").format(obj.grossAmount.toLong())
        myOrderItemInfo.amount = Html.fromHtml(text).toString()
        myOrderItemInfo.orderDate = Utils.getDateFormat(obj.createdDate)
        myOrderItemInfo.deliveryDate = Utils.getDateFormat(obj.deliverydate)
        myOrderItemInfo.updateddate = Utils.getDateFormat(obj.updatedDate)
        myOrderItemInfo.readytoDispatchedDate = Utils.getOrderDateFormat(obj.readytoDispatchedDate)
        val statusList = ArrayList<ReturnOrderStatusModel>()
        statusList.add(
            ReturnOrderStatusModel(
                position,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.ordered),
                "",
                obj.createdDate
            )
        )
        statusList.add(ReturnOrderStatusModel(position, "" + obj.status, "", obj.updatedDate))
        val adapter = OrderStatusAdapter(activity, statusList)
        vh.mBinding.rvStatus.adapter = adapter
        if (!TextUtils.isNullOrEmpty(obj.deliveryPersonMobile)) {
            vh.mBinding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + obj.deliveryPersonMobile)
                activity.startActivity(intent)
            }
        } else {
            vh.mBinding.btnCall.visibility = View.GONE
        }
        vh.mBinding.ivMore.setOnClickListener {
            orderDate = obj.createdDate
            Utils.showProgressDialog(activity)
            CommonClassForAPI.getInstance(activity).getOrderStatus(orderStatusObserver, obj.orderId)
        }
        vh.mBinding.btnRate.setOnClickListener {
            Utils.showProgressDialog(activity)
            CommonClassForAPI.getInstance(activity).getAppHomeBottomData(
                object : DisposableObserver<ArrayList<RatingModel>>() {
                    override fun onNext(ratingList1: ArrayList<RatingModel>) {
                        Utils.hideProgressDialog()
                        if (ratingList1.size > 0) {
                            DBoyRatingFragment.newInstance(position, ratingList1)
                                .show(activity.supportFragmentManager, "a")
                        }
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                },
                EndPointPref.getInstance(RetailerSDKApp.application).baseUrl + "/api/RetailerApp/GetDboyRatingOrder/" + obj.orderId
            )
            if (obj.salesPersonMobile != null && obj.salesPersonMobile!!.isNotEmpty()) {
                CommonClassForAPI.getInstance(activity).getAppHomeBottomData(
                    object : DisposableObserver<ArrayList<RatingModel>>() {
                        override fun onNext(ratingList1: ArrayList<RatingModel>) {
                            Utils.hideProgressDialog()
                            if (ratingList1.size > 0) {
                                SalesRateFragment.newInstance(position, ratingList1)
                                    .show(activity.supportFragmentManager, "a")
                            }
                        }

                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    },
                    EndPointPref.getInstance(RetailerSDKApp.application).baseUrl + "/api/RetailerApp/GetSalesManRatingOrder/" + obj.orderId
                )
            }
        }
        vh.mBinding.btnRaiseConcern.setOnClickListener {
            activity.startActivity(
                Intent(activity, DeliveryConcernActivity::class.java)
                    .putExtra("orderId", obj.orderId)
            )
        }
        if (obj.status.equals("Pending", ignoreCase = true) || obj.status.equals(
                "Failed",
                ignoreCase = true
            )
        ) {
            vh.mBinding.liDBoy.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
            if (obj.salesPerson != null) {
                vh.mBinding.liSalesP.visibility = View.VISIBLE
                vh.mBinding.liSelf.visibility = View.GONE
            } else {
                vh.mBinding.liSelf.visibility = View.VISIBLE
                vh.mBinding.liSalesP.visibility = View.GONE
            }
        } else if (obj.status.equals("Ready to Dispatch", ignoreCase = true)) {
            vh.mBinding.liDBoy.visibility = View.VISIBLE
            vh.mBinding.liSelf.visibility = View.GONE
            vh.mBinding.liSalesP.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
        } else if (obj.status.equals("Issued", ignoreCase = true)) {
            vh.mBinding.liDBoy.visibility = View.VISIBLE
            vh.mBinding.liSelf.visibility = View.GONE
            vh.mBinding.liSalesP.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
        } else if (obj.status.equals("Shipped", ignoreCase = true)) {
            vh.mBinding.liDBoy.visibility = View.VISIBLE
            vh.mBinding.liSelf.visibility = View.GONE
            vh.mBinding.liSalesP.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
            if (obj.deliveryOtp != 0) {
                vh.mBinding.tvOtp.visibility = View.VISIBLE
                vh.mBinding.tvOtp.text = "Delivery OTP " + obj.deliveryOtp
            } else {
                vh.mBinding.tvOtp.visibility = View.GONE
            }
        } else if (obj.status.equals("Delivered", ignoreCase = true) || obj.status.equals(
                "sattled",
                ignoreCase = true
            ) || obj.status.equals(
                "partial settled",
                ignoreCase = true
            ) || obj.status.equals("Account settled", ignoreCase = true)
        ) {
            vh.mBinding.ivImageS.visibility = View.VISIBLE
            vh.mBinding.liDBoy.visibility = View.VISIBLE
            vh.mBinding.btnRate.visibility = View.VISIBLE
            vh.mBinding.liSelf.visibility = View.GONE
            vh.mBinding.liSalesP.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnCall.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
            if (obj.rating > 0) {
                vh.mBinding.liRating.visibility = View.VISIBLE
                vh.mBinding.btnRate.visibility = View.GONE
            } else {
                vh.mBinding.btnRate.visibility = View.VISIBLE
                vh.mBinding.liRating.visibility = View.GONE
            }
            if (obj.isCustomerRaiseConcern) {
                vh.mBinding.btnRaiseConcern.visibility = View.VISIBLE
            } else {
                vh.mBinding.btnRaiseConcern.visibility = View.GONE
            }
        } else if (obj.status.equals(
                "Delivery Canceled",
                ignoreCase = true
            ) || obj.status.equals(
                "Order Canceled",
                ignoreCase = true
            ) || obj.status.equals("Post Order Canceled", ignoreCase = true)
        ) {
            vh.mBinding.liSelf.visibility = View.VISIBLE
            vh.mBinding.ivImageS.visibility = View.GONE
            vh.mBinding.liDBoy.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
        } else if (obj.status.equals("Dummy Order", ignoreCase = true)) {
            vh.mBinding.liSelf.visibility = View.VISIBLE
            vh.mBinding.ivImageS.visibility = View.GONE
            vh.mBinding.liDBoy.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
        } else {
            vh.mBinding.liSelf.visibility = View.VISIBLE
            vh.mBinding.ivImageS.visibility = View.GONE
            vh.mBinding.liDBoy.visibility = View.GONE
            vh.mBinding.btnRate.visibility = View.GONE
            vh.mBinding.liRating.visibility = View.GONE
            vh.mBinding.btnRaiseConcern.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun ePayCredentials(model: CompanyDetails?) {
        //   SharePrefs.getInstance(this).putString(SharePrefs.ePayLaterEndpoint, model.getEPayLaterEndpoint());
        SharePrefs.getInstance(activity).putString(SharePrefs.E_PAY_LATER_URL, model!!.ePAYLATERURL)
        SharePrefs.getInstance(activity).putString(SharePrefs.COMPANY_CONTACT, model.contact)
        SharePrefs.getInstance(activity).putString(SharePrefs.ENCODED_KEY, model.eNCODEDKEY)
        SharePrefs.getInstance(activity).putString(SharePrefs.BEARER_TOKEN, model.bEARERTOKEN)
        SharePrefs.getInstance(activity).putString(SharePrefs.IV, model.iV)
        SharePrefs.getInstance(activity).putString(SharePrefs.M_CODE, model.mCODE)
        SharePrefs.getInstance(activity).putString(SharePrefs.CATEGORY, model.category)
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_COMPANY_API_CALL, false)
        SharePrefs.getInstance(activity).putString(SharePrefs.IS_PAYMENT_GATWAY, model.gatewayName)
    }

    private fun saveHDFCCredential(model: CompanyDetails?) {
        SharePrefs.getInstance(activity).putString(SharePrefs.MERCHANT_ID, model!!.hDFCMerchantId)
        SharePrefs.getInstance(activity).putString(SharePrefs.ACCESS_CODE, model.hDFCAccessCode)
        SharePrefs.getInstance(activity).putString(SharePrefs.REDIRECT_URL, model.redirect_url)
        SharePrefs.getInstance(activity).putString(SharePrefs.CANCEL_URL, model.cancel_url)
        SharePrefs.getInstance(activity).putString(SharePrefs.GATWAY_URL, model.gatewayURL)
    }

    // order status dialog
    private fun showOrderStatusDialog(list: ArrayList<ReturnOrderStatusModel>) {
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(R.layout.dialog_order_status)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        val rvStatus = dialog.findViewById<RecyclerView>(R.id.rvStatus)
        val adapter = OrderStatusAdapter(activity, list)
        rvStatus!!.adapter = adapter
        dialog.show()
        RetailerSDKApp.getInstance().updateAnalytics("order_status_dialog")
    }

    inner class ViewHolder(var mBinding: ActivityMyOrderItemBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        init {
            mBinding.tvDetailsSelect.setOnClickListener {
                RetailerSDKApp.getInstance().updateAnalytics("order_detail_click")
                val intent = Intent(activity, OrderDetailsNewActivity::class.java)
                intent.putExtra("selectedPosition", adapterPosition)
                intent.putExtra("orderId", list[adapterPosition].orderId)
                intent.putExtra("status", list[adapterPosition].status)
                intent.putExtra("deliveryOtp", list[adapterPosition].deliveryOtp)
                intent.putExtra("orderType", list[adapterPosition].orderType)
                activity.startActivity(intent)
                Utils.fadeTransaction(activity)
            }
            mBinding.btnPayNow.setOnClickListener {
                RetailerSDKApp.getInstance().updateAnalytics("pay_now_click")
                if (utils.isNetworkAvailable) {
                    if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_COMPANY_API_CALL)
                    ) {
                        CommonClassForAPI.getInstance(activity).getCompanyInfo(
                            getInfoDes,
                            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
                            "Pay now button"
                        )
                    }
                    val intent = Intent(activity, PayNowActivity::class.java)
                    if (list[adapterPosition].payNowOption.isNullOrEmpty())
                        list[adapterPosition].payNowOption = "Online"
                    intent.putExtra("PendingOrderList", list[adapterPosition])
                    intent.putExtra("pendingorderFlag", true)
                    if (list[adapterPosition].orderType == 8)
                        intent.putExtra("PayNowOption", "Online")
                    else
                        intent.putExtra("PayNowOption", list[adapterPosition].payNowOption)
                    intent.putExtra("RemainingAmount", list[adapterPosition].remainingAmount)
                    intent.putExtra("orderType", list[adapterPosition].orderType)
                    activity.startActivity(intent)
                    Utils.fadeTransaction(
                        activity
                    )
                } else {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            }
        }
    }

    var getInfoDes: DisposableObserver<CompanyInfoResponse> =
        object : DisposableObserver<CompanyInfoResponse>() {
            override fun onNext(response: CompanyInfoResponse) {
                Utils.hideProgressDialog()
                if (response.isStatus) {
                    SharePrefs.getInstance(activity).putBoolean(
                        SharePrefs.IS_SHOW_LEDGER,
                        response.companyDetails!!.isShowLedger
                    )
                    SharePrefs.getInstance(activity)
                        .putBoolean(SharePrefs.IS_SHOW_TARGET, response.companyDetails.isShowTarget)
                    SharePrefs.getInstance(activity).putString(
                        SharePrefs.MAX_WALLET_POINT_USED,
                        response.companyDetails.maxWalletPointUsed
                    )
                    SharePrefs.getInstance(activity).putString(
                        SharePrefs.WEB_VIEW_BASE_URL,
                        response.companyDetails.webViewBaseUrl
                    )
                    ePayCredentials(response.companyDetails)
                    saveHDFCCredential(response.companyDetails)
                } else {
                    Utils.setToast(activity, response.message)
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }

    var orderStatusObserver: DisposableObserver<ArrayList<ReturnOrderStatusModel>> =
        object : DisposableObserver<ArrayList<ReturnOrderStatusModel>>() {
            override fun onNext(statusList: ArrayList<ReturnOrderStatusModel>) {
                Utils.hideProgressDialog()
                statusList.add(
                    0,
                    ReturnOrderStatusModel(
                        0,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.ordered),
                        "",
                        orderDate
                    )
                )
                showOrderStatusDialog(statusList)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}