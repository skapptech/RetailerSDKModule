package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ReturnOrderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class ReturnOrderAdapter(var mContext: Context, var mList: ArrayList<SalesReturnRequestListModel>) :
    RecyclerView.Adapter<ReturnOrderAdapter.ViewHolder>() {

    fun submitList(list: ArrayList<SalesReturnRequestListModel>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReturnOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mList[position])

    inner class ViewHolder(val binding: ReturnOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SalesReturnRequestListModel) {
            with(itemView) {
                if (position % 2 == 1) {
                    binding.llBody.setBackgroundColor(Color.parseColor("#EBFBF3"));
                } else {
                    binding.llBody.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                binding.tvOrderId.text = model.requestId.toString()
                binding.tvOrderValue.text = "₹" + DecimalFormat("##.##").format(model.orderValue)
                binding.tvOrderDate.text = Utils.getDateFormat(model.orderDate, "d MMM yyyy")
                binding.tvStatus.text = model.status
                binding.llBody.setOnClickListener {
                    mContext.startActivity(
                        Intent(
                            mContext,
                            ReturnOrderDetailsActivity::class.java
                        ).putExtra("REQUEST_ID", model.requestId)
                            .putExtra("ORDER_ID", model.orderId)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}