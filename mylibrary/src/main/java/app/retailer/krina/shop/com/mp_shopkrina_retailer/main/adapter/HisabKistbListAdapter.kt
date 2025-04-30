package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemHisabKitabBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.webView.HisabDetailActivity
import java.text.DecimalFormat
import java.util.*

class HisabKistbListAdapter(
    private val context: Context,
    customerResArrayList: ArrayList<CustomerRes>?
) : RecyclerView.Adapter<HisabKistbListAdapter.ViewHolder>() {
    private val customerResArrayList: ArrayList<CustomerRes>?
    private val itemStoreList: MutableList<CustomerRes>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    viewGroup.context
                ), R.layout.item_hisab_kitab, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val customersContactModel = customerResArrayList!![i]
        try {
            val Amount = customersContactModel.lastTrancAmount.toDouble()
            if (Amount == 0.0) {
                val i2 = customersContactModel.lastTrancAmount.toDouble()
                viewHolder.mbinding.tvLastTrsAmount.text = "₹ " + DecimalFormat("##.##").format(i2)
                viewHolder.mbinding.tvPaymentStatus.text = context.getString(R.string.due)
                viewHolder.mbinding.tvPaymentStatus.setTextColor(context.resources.getColor(R.color.colorAccent))
            } else if (Amount < 0) {
                val StringAmt = customersContactModel.lastTrancAmount.replace("-", "")
                val i2 = StringAmt.toDouble()
                viewHolder.mbinding.tvLastTrsAmount.text = "₹ " + DecimalFormat("##.##").format(i2)
                viewHolder.mbinding.tvPaymentStatus.text = context.getString(R.string.due)
                viewHolder.mbinding.tvPaymentStatus.setTextColor(context.resources.getColor(R.color.colorAccent))
            } else {
                val i2 = customersContactModel.lastTrancAmount.toDouble()
                viewHolder.mbinding.tvLastTrsAmount.text = "₹ " + DecimalFormat("##.##").format(i2)
                viewHolder.mbinding.tvPaymentStatus.text = context.getString(R.string.advance)
                viewHolder.mbinding.tvPaymentStatus.setTextColor(context.resources.getColor(R.color.green_50))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val i2 = customersContactModel.lastTrancAmount.toDouble()
            viewHolder.mbinding.tvLastTrsAmount.text = "₹ " + DecimalFormat("##.##").format(i2)
            //viewHolder.mbinding.tvLastTrsAmount.setText("₹ " +customersContactModel.getLastTrancAmount());
            viewHolder.mbinding.tvPaymentStatus.text = ""
        }
        try {
            viewHolder.mbinding.tvDate.text =
                Utils.getChangeDateFormatInProfile(customersContactModel.lastTrancDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (customersContactModel.type == null || customersContactModel.type == "") {
            viewHolder.mbinding.tvType.visibility = View.GONE
        } else {
            viewHolder.mbinding.tvType.visibility = View.VISIBLE
            viewHolder.mbinding.tvType.text = customersContactModel.type
        }
        if (customersContactModel.name != null && customersContactModel.name != "") {
            viewHolder.mbinding.initialsTextView.text =
                Utils.wordFirstCap(customersContactModel.name).substring(0, 1)
            viewHolder.mbinding.tvUserName.text = customersContactModel.name
        }
        viewHolder.mbinding.llMainChatView.setOnClickListener { v: View? ->
            context.startActivity(
                Intent(
                    context, HisabDetailActivity::class.java
                )
                    .putExtra("list", customersContactModel)
                    .putExtra("id", customersContactModel.id)
            )
        }
    }

    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        customerResArrayList!!.clear()
        if (charText.length == 0) {
            customerResArrayList.addAll(itemStoreList)
        } else {
            for (wp in itemStoreList) {
                if (wp.name.lowercase(Locale.getDefault()).contains(charText)) {
                    customerResArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return customerResArrayList?.size ?: 0
    }

    inner class ViewHolder(var mbinding: ItemHisabKitabBinding) : RecyclerView.ViewHolder(
        mbinding.root
    )

    init {
        itemStoreList = ArrayList()
        itemStoreList.addAll(customerResArrayList!!)
        this.customerResArrayList = customerResArrayList
    }
}