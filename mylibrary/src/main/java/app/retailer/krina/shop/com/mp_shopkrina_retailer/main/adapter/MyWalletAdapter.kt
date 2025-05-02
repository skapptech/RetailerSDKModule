package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyWalletAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyWalletAdapter(private val context: Context, private val mylist: ArrayList<MyWalletModel>) :
    RecyclerView.Adapter<MyWalletAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<MyWalletAdapterBinding>(
                LayoutInflater.from(parent.context),
                R.layout.my_wallet_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mylist[position]
        val reedemAmt = model.newOutWAmount
        val deliverAmt = model.newAddedWAmount
        holder.mBinding.tvEarnFrom.text = mylist[position].through
        if (model.orderId != 0) {
            holder.mBinding.txtOrderId.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id_colon) + model.orderId
        } else {
            holder.mBinding.txtOrderId.text = ""
        }
        if (mylist[position].transactionDate != null && model.transactionDate != "null") {
            holder.mBinding.tvDate.text =
                Utils.getDateFormat(
                    mylist[position].transactionDate
                )
        } else {
            holder.mBinding.tvDate.text = ""
        }
        if (reedemAmt > 0) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_group_1105_rupe_new)
            holder.mBinding.txtRedAmt.text = "-" + model.newOutWAmount
            holder.mBinding.txtRedAmt.setTextColor(context.resources.getColor(R.color.red))
        } else if (deliverAmt > 0) {
            holder.mBinding.txtRedAmt.text = "+" + model.newAddedWAmount
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_rupe_greeen)
            holder.mBinding.txtRedAmt.setTextColor(context.resources.getColor(R.color.green_50))
        } else {
            holder.mBinding.txtRedAmt.text = "+" + model.newAddedWAmount
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_rupe_greeen)
            holder.mBinding.txtRedAmt.setTextColor(context.resources.getColor(R.color.green_50))
        }
    }

    override fun getItemCount(): Int {
        return mylist.size
    }

    inner class ViewHolder(var mBinding: MyWalletAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}