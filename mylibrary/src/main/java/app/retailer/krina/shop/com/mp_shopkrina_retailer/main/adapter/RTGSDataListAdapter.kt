package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemRtgsHistoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class RTGSDataListAdapter(
    private val activity: AppCompatActivity,
    pickerList: ArrayList<GullakModel>
) :
    RecyclerView.Adapter<RTGSDataListAdapter.ViewHolder>() {
    private var list = pickerList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rtgs_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(val binding: ItemRtgsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GullakModel) {
            with(itemView) {
                binding.tvId.text = model.objectId
                binding.tvComment.text = model.comment
                binding.tvAmt.text = model.amount.toString()
                binding.tvDate.text = Utils.getDateFormat(model.createdDate)

                if (model.amount!! < 0) {
                    binding.tvId.text = model.comment
                    binding.tvComment.text =
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.order_placed)
                    binding.iconGroup.setImageResource(R.drawable.ic_group_1105_rupe_new)
                    binding.tvAmt.text = DecimalFormat("##.##").format(model.amount!!)
                    binding.tvAmt.setTextColor(activity.resources.getColor(R.color.red))
                } else {
                    binding.tvId.text =
                        RetailerSDKApp.getInstance().noteRepository.getString(R.string.transaction_id) + ":" + model.refNo
                    binding.tvComment.text = model.comment
                    binding.iconGroup.setImageResource(R.drawable.ic_rupe_greeen)
                    binding.tvAmt.text = "+" + DecimalFormat("#.##").format(model.amount!!)
                    binding.tvAmt.setTextColor(activity.resources.getColor(R.color.green_50))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}