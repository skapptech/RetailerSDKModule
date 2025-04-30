package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemHisabCollectionListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.webView.HisabDetailActivity
import java.text.DecimalFormat
import java.util.*

class HisabCollectionAdapter(
    private val context: Context,
    customerResArrayList: ArrayList<CustomerRes>?
) : RecyclerView.Adapter<HisabCollectionAdapter.ViewHolder>() {
    private val customerResArrayList: ArrayList<CustomerRes>?
    private val itemStoreList: MutableList<CustomerRes>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_hisab_collection_list, viewGroup, false
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
                viewHolder.mbinding.tvLastTrsAmount.text = """
                    ₹ ${DecimalFormat("##.##").format(i2)}
                    ${context.resources.getString(R.string.due)}
                    """.trimIndent()
                viewHolder.mbinding.tvLastTrsAmount.setTextColor(context.resources.getColor(R.color.red))
            } else if (Amount < 0) {
                val StringAmt = customersContactModel.lastTrancAmount.replace("-", "")
                val i2 = StringAmt.toDouble()
                viewHolder.mbinding.tvLastTrsAmount.text = """
                    ₹ ${DecimalFormat("##.##").format(i2)}
                    ${context.resources.getString(R.string.due)}
                    """.trimIndent()
                viewHolder.mbinding.tvLastTrsAmount.setTextColor(context.resources.getColor(R.color.red))
                //viewHolder.mbinding.tvLastTrsAmount.setText("₹ "+StringAmt+"\nDue");
            } else {
                viewHolder.mbinding.tvLastTrsAmount.text = """
                    ₹ ${customersContactModel.lastTrancAmount}
                    ${context.resources.getString(R.string.advance)}
                    """.trimIndent()
                viewHolder.mbinding.tvLastTrsAmount.setTextColor(context.resources.getColor(R.color.green_50))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewHolder.mbinding.tvLastTrsAmount.text = "₹ " + customersContactModel.lastTrancAmount
        }
        viewHolder.mbinding.tvDate.text =
            Utils.getChangeDateFormatInProfile(customersContactModel.lastTrancDate)
        if (customersContactModel.name != null && customersContactModel.name != "") {
            viewHolder.mbinding.initialsTextView.text =
                Utils.wordFirstCap(customersContactModel.name).substring(0, 1)
        }
        viewHolder.mbinding.tvUserName.text = customersContactModel.name
        viewHolder.mbinding.tvUserNumber.text = customersContactModel.mobile
        viewHolder.mbinding.llMainChatView.setOnClickListener { v: View? ->
            context.startActivity(
                Intent(
                    context,
                    HisabDetailActivity::class.java
                ).putExtra("list", customersContactModel).putExtra("id", customersContactModel.id)
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

    inner class ViewHolder(var mbinding: ItemHisabCollectionListBinding) : RecyclerView.ViewHolder(
        mbinding.root
    )

    init {
        itemStoreList = ArrayList()
        itemStoreList.addAll(customerResArrayList!!)
        this.customerResArrayList = customerResArrayList
    }
}