package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.PaylaterLimitsResponseListModel
import java.text.DecimalFormat

class PaylaterLimitsAdapter(
    private val activity: AppCompatActivity,
    private val mList: ArrayList<PaylaterLimitsResponseListModel>
) : RecyclerView.Adapter<PaylaterLimitsAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val tvCreditLimit: TextView = itemView.findViewById(R.id.creditLimit)
        val availableRemainingAmount: TextView =
            itemView.findViewById(R.id.AvailableRemainingAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paylater_limits, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]

        // sets the text to the textsview from our itemHolder class
        holder.storeName.text = data.StoreName
        holder.tvCreditLimit.text = DecimalFormat("##.##").format(data.CreditLimit).toString()
        holder.availableRemainingAmount.text = DecimalFormat("##.##").format(data.AvailableRemainingAmount).toString()
    }
}