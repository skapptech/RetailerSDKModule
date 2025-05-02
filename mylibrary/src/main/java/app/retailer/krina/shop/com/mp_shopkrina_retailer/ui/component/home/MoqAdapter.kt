package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import java.text.DecimalFormat

class MoqAdapter(
    private val context: Context,
    private var mItemList: ArrayList<ItemListModel>,
    private val listener: AdapterInterface?
) : BaseAdapter() {
    private val moqArray = ArrayList<Int>()
    fun setItemListCategory(mItemList: ArrayList<ItemListModel>) {
        this.mItemList = mItemList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return  mItemList.size
    }

    override fun getItem(position: Int): Any {
        return mItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = LayoutInflater.from(context).inflate(R.layout.multi_moq_list, parent, false)
        val label = row.findViewById<TextView>(R.id.moq)
        val lerclick = row.findViewById<LinearLayout>(R.id.lerclick)
        val ll_check_box_check = row.findViewById<LinearLayout>(R.id.ll_check_box_check)
        val price = row.findViewById<TextView>(R.id.rs)
        val tvMrp = row.findViewById<TextView>(R.id.tv_mrp)
        val margin = row.findViewById<TextView>(R.id.margin)
        val selectedCB = row.findViewById<CheckBox>(R.id.cb_selected_items)
        val tvPPrice = row.findViewById<TextView>(R.id.tvPPrice)
        val tvSchemeText = row.findViewById<TextView>(R.id.tvSchemeText)
        val liPrime = row.findViewById<LinearLayout>(R.id.liPrime)
        val model = mItemList!![position]
        selectedCB.isChecked = model.isChecked
        selectedCB.setOnClickListener { v: View? -> listener?.onClick(position) }
        lerclick.setOnClickListener { v: View? -> listener?.onClick(position) }
        label.text = "" + model.minOrderQty
        price.text = "" + model.unitPrice
        tvMrp.text = "" + model.price
        tvPPrice.text = (SharePrefs.getInstance(context).getString(SharePrefs.PRIME_NAME)
                + " " + RetailerSDKApp.getInstance().dbHelper.getData("price")
                + ": ₹" + DecimalFormat("##.##").format(model.primePrice))
        if (model.isPrimeItem) {
            liPrime.visibility = View.VISIBLE
        } else {
            liPrime.visibility = View.GONE
        }
        if (!TextUtils.isNullOrEmpty(model.scheme)) {
            tvSchemeText.visibility = View.VISIBLE
            tvSchemeText.text = "" + model.scheme
        } else {
            tvSchemeText.visibility = View.GONE
        }

        // Logic to highlight highest moq
        moqArray.add(model.minOrderQty)
        if (moqArray.size > 0) {
            var highest = moqArray[0]
            var highestIndex = 0
            for (s in 1 until moqArray.size) {
                val curValue = moqArray[s]
                if (curValue > highest) {
                    highest = curValue
                    highestIndex = s
                }
            }
            if (position == highestIndex) {
                ll_check_box_check.setBackgroundColor(
                    ContextCompat.getColor(
                        context, R.color.light_grey
                    )
                )
            }
        }
        margin.text = DecimalFormat("##.##").format(model.marginPoint!!.toDouble())
        return row
    }
}