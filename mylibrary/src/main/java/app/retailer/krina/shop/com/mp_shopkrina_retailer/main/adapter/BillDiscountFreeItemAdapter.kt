package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.BillFreeItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.RetailerBillDiscountFreeItemDcs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class BillDiscountFreeItemAdapter(
    private val context: Context,
    private val mylist: ArrayList<RetailerBillDiscountFreeItemDcs>
) : RecyclerView.Adapter<BillDiscountFreeItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BillFreeItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItemName.text = (mylist[position].itemName
                + " (" + mylist[position].qty + " " +
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_qty) + ")")
    }

    override fun getItemCount(): Int {
        return mylist.size
    }

    inner class ViewHolder(mBinding: BillFreeItemsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        var tvItemName: TextView

        init {
            tvItemName = mBinding.tvItemName
        }
    }
}