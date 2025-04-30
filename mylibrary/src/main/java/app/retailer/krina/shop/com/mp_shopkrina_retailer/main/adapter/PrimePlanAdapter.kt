package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPrimePlanListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipPlanModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class PrimePlanAdapter(
    private val activity: Activity,
    private val list: ArrayList<MembershipPlanModel>,
    private val onButtonClick: OnButtonClick
) : RecyclerView.Adapter<PrimePlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    activity
                ),
                R.layout.item_prime_plan_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.mBinding.tvName.text = model.memberShipName
        if (model.memberShipInMonth == 12) {
            holder.mBinding.btnBuy.text =
                (MyApplication.getInstance().dbHelper.getString(R.string.join_prime_at)
                        + model.amount + MyApplication.getInstance().dbHelper.getString(R.string.per_year))
        } else if (model.memberShipInMonth == 1) {
            holder.mBinding.btnBuy.text =
                (MyApplication.getInstance().dbHelper.getString(R.string.join_prime_at)
                        + model.amount + MyApplication.getInstance().dbHelper.getString(R.string.per_month))
        } else {
            holder.mBinding.btnBuy.text =
                (MyApplication.getInstance().dbHelper.getString(R.string.join_prime_at)
                        + model.amount + MyApplication.getInstance().dbHelper.getString(R.string.for_) + model.memberShipInMonth + MyApplication.getInstance().dbHelper.getString(
                    R.string.month
                ))
        }
        if (list[position].isTaken && list[position].id == list[position].takenMemberId) {
            holder.mBinding.btnBuy.backgroundTintList =
                ColorStateList.valueOf(activity.resources.getColor(R.color.black))
            val days = Utils.getRemainingDays(
                list[position].endDate
            )
            if (days < 7) {
                holder.mBinding.btnRenew.visibility = View.VISIBLE
                holder.mBinding.btnBuy.visibility = View.GONE
                holder.mBinding.btnRenew.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.renew_prime_at) + model.amount + model.memberShipInMonth
            } else {
                holder.mBinding.btnRenew.visibility = View.GONE
                holder.mBinding.btnBuy.visibility = View.VISIBLE
            }
        } else {
            holder.mBinding.btnBuy.backgroundTintList =
                ColorStateList.valueOf(activity.resources.getColor(R.color.colorAccent))
        }
        if (list[position].isTaken) {
            holder.mBinding.btnBuy.backgroundTintList =
                ColorStateList.valueOf(activity.resources.getColor(R.color.black))
        } else {
            holder.mBinding.btnBuy.backgroundTintList =
                ColorStateList.valueOf(activity.resources.getColor(R.color.colorAccent))
        }
        holder.mBinding.btnBuy.isEnabled = true
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder internal constructor(var mBinding: ItemPrimePlanListBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        init {
            mBinding.btnBuy.setOnClickListener { v: View? ->
                mBinding.btnBuy.isEnabled = false
                onButtonClick.onButtonClick(adapterPosition, false)
            }
            mBinding.btnRenew.setOnClickListener { v: View? ->
                mBinding.btnRenew.isEnabled = false
                onButtonClick.onButtonClick(adapterPosition, true)
            }
        }
    }
}