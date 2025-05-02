package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ReturnOrderDetailsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListDetailsModel
import java.text.DecimalFormat

class ReturnOrderDetailsAdapter(
    mList: ArrayList<SalesReturnRequestListDetailsModel>
) :
    RecyclerView.Adapter<ReturnOrderDetailsAdapter.ViewHolder>() {
    private var mList = mList
    fun submitList(list:ArrayList<SalesReturnRequestListDetailsModel>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ReturnOrderDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mList[position])

    inner class ViewHolder(val binding: ReturnOrderDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SalesReturnRequestListDetailsModel) {
            with(itemView) {
                if(position %2 == 1)
                {
                    binding.llBody.setBackgroundColor(Color.parseColor("#EBFBF3"));
                }
                else
                {
                    binding.llBody.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                binding.tvItemName.text = model.itemName
                binding.tvQty.text = model.qty.toString()
                binding.tvRate.text = DecimalFormat("##.##").format(model.rate)
                binding.tvTotalValue.text = "₹"+DecimalFormat("##.##").format(model.totalValue)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}