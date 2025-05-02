package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemReturnOrderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class ReturnOrderAdapter(
    private val activity: Activity,
    val list: ArrayList<ReturnOrderItemModel>
) : RecyclerView.Adapter<ReturnOrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_return_order, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        Glide.with(activity).load(model.getItemPic()).into(holder.mBinding.ivImage)
        holder.mBinding.tvName.text = model.getItemName()
        holder.mBinding.tvPrice.text =
            (RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp)
                    + "  " + model.getPrice())
        holder.mBinding.tvQty.text =
            (RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_order_qty)
                    + " " + model.getQty())
        if (model.returnQty != 0) {
            holder.mBinding.tvReturnQty.text =
                (RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_return_qty)
                        + " " + model.returnQty)
        } else {
            holder.mBinding.tvReturnQty.text = ""
        }
        if (model.isReplaceable) {
            holder.mBinding.cbSelect.visibility = View.VISIBLE
            if (model.isReturnReplaced) {
                holder.mBinding.cbSelect.visibility = View.GONE
                holder.mBinding.tvReturnQty.text = RetailerSDKApp.getInstance().dbHelper
                    .getString(R.string.text_return_replace_request)
            } else {
                holder.mBinding.cbSelect.visibility = View.VISIBLE
            }
        } else {
            holder.mBinding.cbSelect.visibility = View.INVISIBLE
        }
        holder.mBinding.cbSelect.isChecked = model.isSelected()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: ItemReturnOrderBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        init {
            mBinding.cbSelect.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                    list[adapterPosition].setSelected(true)
                    showReturnQtyDialog(adapterPosition)
                } else {
                    list[adapterPosition].setSelected(false)
                    list[adapterPosition].setReturnQty(0)
                    mBinding.tvReturnQty.text = ""
                }
            }
        }
    }

    private fun showReturnQtyDialog(position: Int) {
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(R.layout.dialog_return_qty)
        val liQty = dialog.findViewById<TextInputLayout>(R.id.li_qty)
        val spReason = dialog.findViewById<AppCompatSpinner>(R.id.sp_return_reason)
        val btnSubmit = dialog.findViewById<Button>(R.id.btn_submit)
        btnSubmit!!.setOnClickListener { v: View? ->
            val qty = liQty!!.editText!!.text.toString()
            if (qty.isEmpty()) {
                liQty.error = "Quantity is required!"
            } else if (qty == "0") {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.minimum_qty_should_not_be_zero)
                )
                //liQty.setError(  activity.getResources().getString(R.string.minimum_qty_should_not_be_zero));
            } else if (qty.toInt() > list[position].getQty()) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_qty_less_msg)
                )
                //liQty.setError("Quantity should be equal or less than Order quantity");
            } else if (spReason!!.selectedItemPosition == 0) {
                liQty.error = null
                liQty.isErrorEnabled = false
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_select_valid_reson)
                )
            } else {
                dialog.dismiss()
                list[position].setReturnQty(qty.toInt())
                list[position].setComment(spReason.selectedItem.toString())
                notifyDataSetChanged()
            }
        }
        dialog.setOnCancelListener { dialog1: DialogInterface? ->
            if (list[position].getReturnQty() == 0) {
                list[position].setSelected(false)
                notifyDataSetChanged()
            }
        }
        dialog.show()
    }
}