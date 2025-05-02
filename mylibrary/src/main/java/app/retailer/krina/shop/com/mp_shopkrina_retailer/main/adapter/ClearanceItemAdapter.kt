package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemClearanceStockBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ClearanceItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class ClearanceItemAdapter(
    private val activity: AppCompatActivity,
    private var list: List<ClearanceItemModel>?,
    private var cartList: MutableList<ClearanceItemModel>,
    private var onButtonClick: OnButtonClick
) : RecyclerView.Adapter<ClearanceItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_clearance_stock, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = list!![i]
        // set String
        holder.mBinding.tvRemainingQtyText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty) + " "
        holder.mBinding.btnAddItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_btn)
        if (!TextUtils.isNullOrEmpty(model.imageUrl)) {
            Picasso.get().load(model.imageUrl)
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .into(holder.mBinding.productImage)
        } else {
            holder.mBinding.productImage.setImageResource(R.drawable.logo_grey)
        }
        holder.mBinding.tvItemName.text = model.ItemName
        holder.mBinding.tvMoq.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.moq)
        holder.mBinding.tvMultiMoq.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.moq)

        val sPRICE = "| ₹" + DecimalFormat("##.##").format(model.unitPrice)
        val sMRP = DecimalFormat("##.##").format(model.MRP)
        //set values
        holder.mBinding.tvMrp.text = sMRP
        holder.mBinding.tvMrp.paintFlags =
            holder.mBinding.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.mBinding.tvPrice.text = sPRICE
        holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
        val qty = cartList.find { it.id == model.id }?.qty
        if (qty != null && qty > 0) {
            list!![i].qty = qty
            holder.mBinding.tvQty.text = "" + (qty)
            holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
            holder.mBinding.tvItemTotal.text =
                "" + DecimalFormat("##.##").format(model.unitPrice * qty)
            holder.mBinding.visible.visibility = View.VISIBLE
            holder.mBinding.btnAddItem.visibility = View.GONE
        } else {
            list!![i].qty = 0
            holder.mBinding.tvQty.text = "0"
            holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
            holder.mBinding.tvItemTotal.text = "0"
            holder.mBinding.visible.visibility = View.GONE
            holder.mBinding.btnAddItem.visibility = View.VISIBLE
        }
        holder.mBinding.tvSelfLife.text =
            MyApplication.getInstance().noteRepository.getString(R.string.shelf_life) +
                    model.shelfLife + " " + MyApplication.getInstance().noteRepository.getString(R.string.days)

        // Minus Btn clicked
        holder.mBinding.minusBtn.setOnClickListener {
            if (model.qty > 0) {
                list!![holder.adapterPosition].qty = list!![holder.adapterPosition].qty - model.moq
                holder.mBinding.tvItemTotal.text =
                    "" + DecimalFormat("##.##").format(model.unitPrice * list!![holder.adapterPosition].qty)
                holder.mBinding.tvQty.text = "" + list!![holder.adapterPosition].qty
                holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
                cartList.find { it.id == model.id }?.qty = list!![holder.adapterPosition].qty
                if (list!![holder.adapterPosition].qty == 0) {
                    holder.mBinding.visible.visibility = View.GONE
                    holder.mBinding.btnAddItem.visibility = View.VISIBLE
                    cartList.removeAll { it.qty == 0 }
                }
                onButtonClick.onButtonClick(0, false)
            }
        }
        //plus Btn clicked
        holder.mBinding.plusBtn.setOnClickListener {
            if ((model.qty + model.moq) <= model.remainingStockQty) {
                list!![holder.adapterPosition].qty = list!![holder.adapterPosition].qty + model.moq
                holder.mBinding.tvItemTotal.text =
                    "" + DecimalFormat("##.##").format(model.unitPrice * model.qty)
                holder.mBinding.tvQty.text = "" + list!![holder.adapterPosition].qty
                holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
                cartList.find { it.id == model.id }?.qty = list!![holder.adapterPosition].qty
                onButtonClick.onButtonClick(i, true)
            } else {
                Toast.makeText(
                    activity,
                    MyApplication.getInstance().dbHelper.getString(R.string.only_add_maximum_item) + " " + model.remainingStockQty,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Add Btn clicked
        holder.mBinding.btnAddItem.setOnClickListener {
            holder.mBinding.visible.visibility = View.VISIBLE
            holder.mBinding.btnAddItem.visibility = View.GONE
            list!![holder.adapterPosition].qty = list!![holder.adapterPosition].qty + model.moq
            holder.mBinding.tvItemTotal.text =
                "" + DecimalFormat("##.##").format(model.unitPrice * list!![holder.adapterPosition].qty)
            holder.mBinding.tvQty.text = "" + list!![holder.adapterPosition].qty
            holder.mBinding.tvAvailQty.text = "" + (model.remainingStockQty - model.qty)
            cartList.add(list!![holder.adapterPosition])
            onButtonClick.onButtonClick(i, true)
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    inner class ViewHolder(var mBinding: ItemClearanceStockBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}