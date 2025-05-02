package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemOrderStatusBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class OrderStatusAdapter(
    private val context: Context,
    private var list: ArrayList<ReturnOrderStatusModel>
) : RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<ItemOrderStatusBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_order_status, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.mBinding.tvStatus.text = model.status
        holder.mBinding.tvDate.text = Utils.getDateTimeFormate(model.date)
        if (position == list.size - 1){
            holder.mBinding.divider.visibility = View.GONE
            holder.mBinding.ivStatus.setImageResource(R.drawable.order_status_complete_red)
        } else {
            holder.mBinding.divider.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: ArrayList<ReturnOrderStatusModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(var mBinding: ItemOrderStatusBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}