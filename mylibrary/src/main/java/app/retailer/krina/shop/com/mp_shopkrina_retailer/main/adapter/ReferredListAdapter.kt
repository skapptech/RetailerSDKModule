package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemReferredListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferredModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class ReferredListAdapter(
    val activity: AppCompatActivity,
    pickerList: ArrayList<ReferredModel>
) :
    RecyclerView.Adapter<ReferredListAdapter.ViewHolder>() {
    private var list = pickerList
    private var utils: Utils? = null

    init {
        utils = Utils(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_referred_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(val binding: ItemReferredListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ReferredModel) {
            with(itemView) {
                binding.tvName.text = if (model.ShopName == null) "-" else "" + model.ShopName
                binding.tvPoints.text =
                    "" + DecimalFormat("##.##").format(model.ReferralWalletPoint)
                binding.tvStatus.text = "" + model.Status
                if (model.Status != null && model.Status == "Delivered") {
                    binding.tvStatus.setTextColor(activity.resources.getColor(R.color.green_50))
                } else {
                    binding.tvStatus.setTextColor(activity.resources.getColor(R.color.secondary_text))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}