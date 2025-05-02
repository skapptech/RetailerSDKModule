package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemAchievedTargetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TargetCustomerDC
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class AchievedTargetItemAdapter(
    private val context: Context,
    private val list: ArrayList<TargetCustomerDC>?
) : RecyclerView.Adapter<AchievedTargetItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.item_achieved_target, parent, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            val model = list!![i]
            viewHolder.mBinding.tvTargetIat.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_target)
            viewHolder.mBinding.tvCurrentIat.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_current_value)
            if (model.isBrand) {
                viewHolder.mBinding.tvItemBrandName.text = model.brandName
            } else {
                viewHolder.mBinding.tvItemBrandName.text = model.itemName
            }
            viewHolder.mBinding.tvTarget.text = model.target.toString() + ""
            viewHolder.mBinding.tvValue.text = model.currentTarget.toString() + ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemAchievedTargetBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}