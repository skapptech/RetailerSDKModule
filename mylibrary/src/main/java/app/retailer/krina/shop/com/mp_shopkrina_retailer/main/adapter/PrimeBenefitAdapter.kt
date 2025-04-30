package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPrimeBenefitListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipModel.Benefit

class PrimeBenefitAdapter(private val activity: Activity, private val list: ArrayList<Benefit>) :
    RecyclerView.Adapter<PrimeBenefitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    activity
                ),
                R.layout.item_prime_benefit_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val model = list[pos]
        holder.mBinding.tvName.text = model.getText()
        if (model.getAmount() >= 0) {
            holder.mBinding.tvPrice.text = "₹" + model.getAmount()
        } else {
            holder.mBinding.tvPrice.text = ""
        }
        when (model.Id) {
            1 -> holder.mBinding.ivImage.setImageResource(R.drawable.ic_offer_orange)
            2 -> holder.mBinding.ivImage.setImageResource(R.drawable.ic_best_price)
            3 -> holder.mBinding.ivImage.setImageResource(R.drawable.ic_delivery_truck)
            4 -> holder.mBinding.ivImage.setImageResource(R.drawable.ic_rupee)
            5 -> holder.mBinding.ivImage.setImageResource(R.drawable.ic_deal)
            else -> holder.mBinding.ivImage.setImageResource(R.drawable.trade_offer_icon)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder internal constructor(var mBinding: ItemPrimeBenefitListBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}