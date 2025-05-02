package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemTrackOrderDetailsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.orderdetail.OrderDetailsNewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PayNowActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TrackOrdersDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class TrackOrderAdapter(
    private val activity: Activity,
    private val list: List<TrackOrdersDetails>?
) : RecyclerView.Adapter<TrackOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_track_order_details, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list!![position]
        holder.mBinding.tvOrderId.text = "OrderId: " + model.orderId
        holder.mBinding.tvAmountPayble.text = "₹" + model.getPayableAmount()
        holder.mBinding.tvAmountOrder.text = "₹" + model.orderAmount
        if (model.isPaid) {
            holder.mBinding.btnPayNow.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.paid)
            holder.mBinding.btnPayNow.isEnabled = false
            holder.mBinding.btnPayNow.setBackgroundResource(R.drawable.background_for_buttons_disble)
        } else {
            holder.mBinding.btnPayNow.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.pay_online_track)
            holder.mBinding.btnPayNow.isEnabled = true
            holder.mBinding.btnPayNow.setBackgroundResource(R.drawable.background_for_buttons)
        }
        if (model.deliveryOtp != 0) {
            holder.mBinding.tvOtp.visibility = View.VISIBLE
            holder.mBinding.tvOtp.text = "Delivery OTP " + model.deliveryOtp
        } else {
            holder.mBinding.tvOtp.visibility = View.GONE
        }
        holder.mBinding.btnOrderDetails.setOnClickListener {
            val intent = Intent(activity, OrderDetailsNewActivity::class.java)
            intent.putExtra("orderId", model.orderId)
            intent.putExtra("status", "")
            intent.putExtra("deliveryOtp", model.deliveryOtp)
            activity.startActivity(intent)
        }
        holder.mBinding.btnPayNow.setOnClickListener {
            val orderModel = ConformOrderModel(
                model.orderId, model.orderAmount, model.getPayableAmount(),
                model.getPayableAmount() > 0, "Online"
            )
            val intent = Intent(activity, PayNowActivity::class.java)
            intent.putExtra("PendingOrderList", orderModel)
            intent.putExtra("PayNowOption", "Online")
            intent.putExtra("itemCount", model.orderId)
            activity.startActivityForResult(intent, 9)
            Utils.fadeTransaction(activity)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemTrackOrderDetailsBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}