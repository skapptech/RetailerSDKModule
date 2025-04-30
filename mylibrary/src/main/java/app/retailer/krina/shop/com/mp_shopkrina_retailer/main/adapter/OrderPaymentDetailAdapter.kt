package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyOrderPaymendetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyorderPaymentDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class OrderPaymentDetailAdapter(
    private val context: Context,
    private val orderPayments: ArrayList<MyorderPaymentDetails>?
) : RecyclerView.Adapter<OrderPaymentDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.my_order_paymendetail, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTransactionNumber.text =
            MyApplication.getInstance().dbHelper.getString(R.string.text_transaction_no)
        holder.binding.tvTransitionDate.text =
            MyApplication.getInstance().dbHelper.getString(R.string.text_transaction_date)
        val model = orderPayments!![position]
        holder.binding.tvPaymentFrom.text = model.paymentFrom
        holder.binding.tvAmount.text = "" + model.amount
        holder.binding.tvTransDate.text =
            "" + Utils.getDateTimeFormate(model.transactionDate)
        holder.binding.tvTransNo.text =
            if (model.transactionNumber == null) "-" else model.transactionNumber

        if (model.amount > 0) {
            holder.binding.tvAmount.setTextColor(context.resources.getColor(R.color.green_dark))
            holder.binding.tvAmount.text = "" + model.amount
        } else {
            holder.binding.tvAmount.text = "" + model.amount + " (" + model.statusDesc + ")"
            holder.binding.tvTransDate.visibility = View.VISIBLE
            when (model.statusDesc) {
                "Refund Initiated" -> {
                    holder.binding.tvTransDate.visibility = View.INVISIBLE
                    holder.binding.tvAmount.setTextColor(context.resources.getColor(R.color.bright_blue))
                }
                "Refund Failed" -> {
                    holder.binding.tvTransDate.visibility = View.INVISIBLE
                    holder.binding.tvAmount.setTextColor(context.resources.getColor(android.R.color.holo_red_dark))
                }
                "Refund Success" -> {
                    holder.binding.tvTransDate.visibility = View.VISIBLE
                    holder.binding.tvAmount.setTextColor(context.resources.getColor(android.R.color.holo_green_dark))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return orderPayments?.size ?: 0
    }

    inner class ViewHolder(var binding: MyOrderPaymendetailBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}