package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SearchViewadapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class SearchHistoryItemAdapter(
    private val activity: HomeActivity,
    private val list: MutableList<ItemListModel>?
) : RecyclerView.Adapter<SearchHistoryItemAdapter.ViewHolder>() {
    private var adapter: MoqAdapter? = null
    private val handler = Handler()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<SearchViewadapterBinding>(
                LayoutInflater.from(parent.context),
                R.layout.search_viewadapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = list!![i]
        holder.mBinding.singleMoq.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        holder.mBinding.multiMoq.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        holder.mBinding.tvMarginPercent.text =
            DecimalFormat("##.##").format(model.marginPoint!!.toDouble()) + "%"
        if (!TextUtils.isNullOrEmpty(model.logoUrl)) {
            Picasso.get().load(model.logoUrl!!.trim { it <= ' ' })
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .into(holder.mBinding.ivTile)
        } else {
            holder.mBinding.ivTile.setImageResource(R.drawable.logo_grey)
        }
        holder.mBinding.tvTitle.text = model.itemname
        holder.mBinding.tvMrp.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp) + " " + model.price
                .toInt()
        if (model.isPrimeItem) {
            holder.mBinding.liPrime.visibility = View.VISIBLE
            holder.mBinding.tvPPrice.text = "₹" + DecimalFormat("##.##").format(model.primePrice)
        } else {
            holder.mBinding.tvPPrice.text = ""
            holder.mBinding.liPrime.visibility = View.INVISIBLE
        }
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
        ) {
            holder.mBinding.tvUnlock.text = ""
            holder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock_open,
                0, 0, 0
            )
        } else {
            holder.mBinding.tvUnlock.text = ""
            holder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock,
                0, R.drawable.ic_right_arrow, 0
            )
        }
        model.isChecked = true
        // checkout clicked
        holder.mBinding.backLayout.setOnClickListener { v: View? ->
            holder.mBinding.backLayout.isClickable = false
            detailsScree(model, holder)
        }

        // set MOQ
        if (model.moqList != null && !model.moqList.isEmpty()) {
            holder.mBinding.singleMoq.visibility = View.GONE
            holder.mBinding.multiMoq.visibility = View.VISIBLE
        } else {
            holder.mBinding.singleMoq.visibility = View.VISIBLE
            holder.mBinding.multiMoq.visibility = View.GONE
        }
        holder.mBinding.multiMoq.setOnClickListener { v: View? ->
            val dialog = BottomSheetDialog(activity)
            val dialogLayout = LayoutInflater.from(activity).inflate(R.layout.moq_price_popup, null)
            dialog.setContentView(dialogLayout)
            val item_name = dialogLayout.findViewById<TextView>(R.id.itemName)
            val tvDSelectQty = dialogLayout.findViewById<TextView>(R.id.tvDSelectQty)
            val tvDMoq = dialogLayout.findViewById<TextView>(R.id.tvDMoq)
            val tvDMrp = dialogLayout.findViewById<TextView>(R.id.tvDMrp)
            val tvDRs = dialogLayout.findViewById<TextView>(R.id.tvDRs)
            val tvDMargin = dialogLayout.findViewById<TextView>(R.id.tvDMargin)
            val ivClose = dialogLayout.findViewById<ImageView>(R.id.ivClose)
            ivClose.setOnClickListener { v1: View? -> dialog.dismiss() }
            tvDSelectQty.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_quantities_for)
            tvDMoq.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq)
            tvDMrp.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.mrp)
            tvDRs.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.rs)
            tvDMargin.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.margins_d)
            val mMoqPriceList = dialogLayout.findViewById<ListView>(R.id.listview_moq_price)
            item_name.text = list[i].itemname
            val listener = AdapterInterface { pos ->
                val moq = list[i].moqList
                list[i] = list[i].moqList[pos]
                list[i].moqList = moq
                for (j in list[i].moqList.indices) {
                    list[i].moqList[j].isChecked = pos == j
                }
                list[i].moqList[pos].isChecked = true
                notifyDataSetChanged()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        dialog.dismiss()
                        handler.postDelayed(this, 500)
                        handler.removeCallbacks(this)
                    }
                }, 500)
            }
            adapter =
                MoqAdapter(
                    activity,
                    model.moqList,
                    listener
                )
            mMoqPriceList.adapter = adapter
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun detailsScree(model: ItemListModel, viewHolder: ViewHolder) {
        val args = Bundle()
        val intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra("PRODUCT_IMAGE", model.logoUrl)
        intent.putExtra("PRODUCT_NAME", model.itemname)
        intent.putExtra("PRODUCT_PRICE", model.unitPrice)
        intent.putExtra("PRODUCT_MOQ", model.minOrderQty)
        intent.putExtra("PRODUCT_ITEM_ID", model.itemId)
        intent.putExtra("PRODUCT_DP", model.dreamPoint)
        intent.putExtra("WAREHOUSE_ID", model.warehouseId)
        intent.putExtra("COMPANY_ID", model.companyId)
        intent.putExtra("PRICE", model.price)
        intent.putExtra("MARGIN_POINT", model.marginPoint)
        intent.putExtra("NUMBER", model.itemNumber)
        intent.putExtra("ItemMultiMRPId", model.itemMultiMRPId)
        args.putSerializable("ITEM_LIST", model)
        intent.putExtra("remainingqty", model.itemLimitQty.toString())
        intent.putExtras(args)
        activity.startActivity(intent)
        viewHolder.mBinding.backLayout.isClickable = true
        Utils.leftTransaction(activity)
    }

    inner class ViewHolder(var mBinding: SearchViewadapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}