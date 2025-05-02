package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BR
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemPaymentAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.PendingPaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class PendingPaymentAdapter(
    private val context: Context,
    private val ladgerEntryListModel: ArrayList<PendingPaymentModel>?
) : RecyclerView.Adapter<PendingPaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_payment_adapter, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mBinding.tvDateIpa.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.date)
        viewHolder.mBinding.tvId.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id)
        val model = ladgerEntryListModel!![i]
        viewHolder.mBinding.tvDate.text =
            Utils.getDateFormat(model.date)
        viewHolder.bind(model)
    }

    override fun getItemCount(): Int {
        return ladgerEntryListModel?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemPaymentAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        fun bind(obj: PendingPaymentModel?) {
            mBinding.setVariable(BR.pendingPaymentModel, obj)
            mBinding.executePendingBindings()
        }
    }
}