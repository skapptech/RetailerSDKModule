package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemCustomerListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CustomerListClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomersContactModel
import java.util.*

class CustomerListAdapter(
    private val context: Context,
    contactsList: ArrayList<CustomersContactModel>?,
    customerlistClicked: CustomerListClicked
) : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {
    private val contactsList: ArrayList<CustomersContactModel>?
    private val customerListClicked: CustomerListClicked
    private val itemStoreList: MutableList<CustomersContactModel>


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    viewGroup.context
                ), R.layout.item_customer_list, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val customersContactModel = contactsList!![i]
        viewHolder.mbinding.tvUserName.text = customersContactModel.customerName
        viewHolder.mbinding.tvUserNumber.text = customersContactModel.customerNumber
        viewHolder.mbinding.rlMainView.setOnClickListener { v: View? ->
            customerListClicked.getPosition(
                i,
                customersContactModel
            )
        }
    }

    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        contactsList!!.clear()
        if (charText.length == 0) {
            contactsList.addAll(itemStoreList)
        } else {
            for (wp in itemStoreList) {
                if (wp.customerName.lowercase(Locale.getDefault()).contains(charText)) {
                    contactsList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contactsList?.size ?: 0
    }

    inner class ViewHolder(var mbinding: ItemCustomerListBinding) :
        RecyclerView.ViewHolder(mbinding.root)

    init {
        itemStoreList = ArrayList()
        itemStoreList.addAll(contactsList!!)
        this.contactsList = contactsList
        customerListClicked = customerlistClicked
    }
}