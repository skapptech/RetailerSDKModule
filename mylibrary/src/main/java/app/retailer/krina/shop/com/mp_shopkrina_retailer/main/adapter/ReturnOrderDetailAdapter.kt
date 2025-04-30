package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemReturnOrderDetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import com.bumptech.glide.Glide

class ReturnOrderDetailAdapter(
    private val activity: Activity,
    val list: ArrayList<ReturnOrderItemModel>
) : RecyclerView.Adapter<ReturnOrderDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_return_order_detail, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        Glide.with(activity).load(model.getItemPic()).into(holder.mBinding.ivImage)
        holder.mBinding.tvName.text = model.getItemName()
        holder.mBinding.tvPrice.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_mrp) + "  " + model.getPrice()
        if (model.getRequestType() == 0) {
            holder.mBinding.tvQty.text =
                MyApplication.getInstance().dbHelper.getString(R.string.text_return_qty) + " " + model.getQty()
        } else {
            holder.mBinding.tvQty.text =
                MyApplication.getInstance().dbHelper.getString(R.string.text_return_qty) + " " + model.getQty()
        }
        holder.mBinding.tvStatus.text = model.getComment()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(var mBinding: ItemReturnOrderDetailBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}