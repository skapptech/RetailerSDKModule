package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSupplierPaymentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LadgerEntryListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class SupplierPaymentAdapter(
    private val context: Context,
    private val ladgerEntryListModel: ArrayList<LadgerEntryListModel>?
) : RecyclerView.Adapter<SupplierPaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    viewGroup.context
                ), R.layout.item_supplier_payment, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = ladgerEntryListModel!![i]
        viewHolder.mbinding.tvDate.text =
            Utils.getDateFormat(model.createdDate)
        viewHolder.mbinding.tvOrderId.text =
            RetailerSDKApp.getInstance().dbHelper.getData("order_id_colon") + " " + +model.id
        if (model.credit > 0.0) {
            viewHolder.mbinding.view.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
            viewHolder.mbinding.tvAmt.text = Html.fromHtml(
                "<font color=#FF4500>&#8377;" + DecimalFormat("#,###.##").format(model.credit)
            ).toString()
            viewHolder.mbinding.txtStatus.text = model.affactedLadgerName + " (Credit)"
        } else {
            viewHolder.mbinding.view.setBackgroundColor(context.resources.getColor(R.color.holo_green))
            viewHolder.mbinding.tvAmt.text = Html.fromHtml(
                "<font color=#FF4500>&#8377;" + DecimalFormat("#,###.##").format(model.debit)
            ).toString()
            viewHolder.mbinding.txtStatus.text = model.affactedLadgerName + " (Debit)"
        }
    }

    override fun getItemCount(): Int {
        return ladgerEntryListModel?.size ?: 0
    }

    inner class ViewHolder(var mbinding: ItemSupplierPaymentBinding) : RecyclerView.ViewHolder(
        mbinding.root
    )
}