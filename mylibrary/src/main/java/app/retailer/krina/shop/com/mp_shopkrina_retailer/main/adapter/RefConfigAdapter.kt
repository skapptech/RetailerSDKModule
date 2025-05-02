package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemReferConfigBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralConfigModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class RefConfigAdapter(
    private val activity: AppCompatActivity,
    pickerList: ArrayList<ReferralConfigModel>
) :
    RecyclerView.Adapter<RefConfigAdapter.ViewHolder>() {
    private var list = pickerList
    private var utils: Utils? = null

    init {
        utils = Utils(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReferConfigBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(val binding: ItemReferConfigBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ReferralConfigModel) {
            with(itemView) {
                binding.tvAmt.text = "" + model.OrderCount
                binding.tvWalletPoint.text = "" + model.referralWalletPoint
                binding.tvReferredPoint.text = "" + model.customerWalletPoint
                binding.tvCredit.text = "Order " + model.orderStatus
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}