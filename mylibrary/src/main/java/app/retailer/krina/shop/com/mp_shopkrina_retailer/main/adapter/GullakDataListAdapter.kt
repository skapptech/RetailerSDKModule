package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemGullakBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class GullakDataListAdapter(
    private val activity: AppCompatActivity,
    pickerList: ArrayList<GullakModel>
) :
    RecyclerView.Adapter<GullakDataListAdapter.ViewHolder>() {
    private var list = pickerList
    private var utils: Utils? = null

    init {
        utils = Utils(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGullakBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(val binding: ItemGullakBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GullakModel) {
            with(itemView) {
                binding.tvId.text = model.objectId
                binding.tvPaymentFrom.text = "" + model.objectType
                binding.tvComment.text = model.comment
                binding.tvAmt.text = model.amount.toString()
                binding.tvDate.text = Utils.getDateFormat(model.createdDate)

                if (model.amount!! < 0) {
                    binding.iconGroup.setImageResource(R.drawable.ic_group_1105_rupe_new)
                    binding.tvAmt.text = DecimalFormat("##.##").format(model.amount!!)
                    binding.tvAmt.setTextColor(activity.resources.getColor(R.color.red))
                } else {
                    binding.iconGroup.setImageResource(R.drawable.ic_rupe_greeen)
                    binding.tvAmt.text = "+" + DecimalFormat("#.##").format(model.amount!!)
                    binding.tvAmt.setTextColor(activity.resources.getColor(R.color.green_50))
                }
                if (model.objectType == "GullakInPayment") {
                    binding.tvId.text = ""
                } else if (model.objectType == "Order") {
                    binding.tvId.text = "" + model.objectId
                    binding.tvPaymentFrom.text = "" + model.objectType
                } else {
                    binding.tvId.text = "" + model.objectId
                    binding.tvPaymentFrom.text = "" + model.objectType
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}