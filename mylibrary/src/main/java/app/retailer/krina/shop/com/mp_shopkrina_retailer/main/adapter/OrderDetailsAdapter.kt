package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemOrderDetailsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderDetailsModel

class OrderDetailsAdapter(
    private val activity: Activity,
    private val list: List<OrderDetailsModel>
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_order_details, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.mBinding.tvItemName.text = "" + model.itemName
        holder.mBinding.tvQty.text = "" + model.getQty()
        holder.mBinding.tvAmount.text = "₹" + model.itemAmount
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: ItemOrderDetailsBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}