package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyAccountAdapterBinding

class AppGuideAdapter(private val _context: Context, private val mylist: ArrayList<String>) :
    RecyclerView.Adapter<AppGuideAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.my_account_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.tvTitle.text = mylist[position]
    }

    override fun getItemCount(): Int {
        return mylist.size
    }

    inner class ViewHolder(var mBinding: MyAccountAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}