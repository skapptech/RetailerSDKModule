package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemReturnOrderListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.returnrefund.ReturnOrderDetailActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class ReturnOrderListAdapter(
    private val activity: Activity,
    private val list: ArrayList<ReturnOrderListModel>
) : RecyclerView.Adapter<ReturnOrderListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_return_order_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.tvStatus1.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.status)
        val model = list[position]
        holder.mBinding.tvOrderId.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id_colon) + " " + model.orderId
        holder.mBinding.tvDate.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_date_colon) + " " + Utils.getChangeDateFormatInProfile(
                model.modifiedDate
            )
        holder.mBinding.tvStatus.text = "" + model.status
        if (model.requestType == 0) {
            holder.mBinding.tvStatus.setTextColor(Color.RED)
            holder.mBinding.tvStatus1.setTextColor(Color.RED)
        } else {
            holder.mBinding.tvStatus.setTextColor(activity.resources.getColor(R.color.green_dark))
            holder.mBinding.tvStatus1.setTextColor(activity.resources.getColor(R.color.green_dark))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: ItemReturnOrderListBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        init {
            mBinding.root.setOnClickListener { v: View? ->
                activity.startActivityForResult(
                    Intent(
                        activity, ReturnOrderDetailActivity::class.java
                    )
                        .putExtra("list", list[adapterPosition]), 9
                )
            }
        }
    }
}