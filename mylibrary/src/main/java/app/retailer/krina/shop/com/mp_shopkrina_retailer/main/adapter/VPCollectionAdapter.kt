package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat

class VPCollectionAdapter(
    private val context: Context,
    private val customerRes: ArrayList<CustomerRes>?
) : PagerAdapter() {
    override fun getCount(): Int {
        return customerRes?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_hisab_collection, container, false)
        val customersContactModel = customerRes!![position]
        val initialsTextView = itemView.findViewById<TextView>(R.id.initialsTextView)
        val tv_user_name = itemView.findViewById<TextView>(R.id.tv_user_name)
        val tv_user_number = itemView.findViewById<TextView>(R.id.tv_user_number)
        val tv_last_trs_amount = itemView.findViewById<TextView>(R.id.tv_last_trs_amount)
        val tv_date = itemView.findViewById<TextView>(R.id.tv_date)
        if (customersContactModel.name != "" && customersContactModel.name != null) {
            initialsTextView.text =
                Utils.wordFirstCap(
                    customersContactModel.name
                ).substring(0, 1)
        }
        tv_user_name.text = customersContactModel.name
        tv_user_number.text = customersContactModel.mobile
        tv_last_trs_amount.text = "₹ " + customersContactModel.lastTrancAmount
        tv_date.text =
            Utils.getChangeDateFormatInProfile(
                customersContactModel.lastTrancDate
            )
        val Amount = customersContactModel.lastTrancAmount.toDouble()
        if (Amount == 0.0) {
            val StringAmt = customersContactModel.lastTrancAmount.replace("-", "")
            val i2 = StringAmt.toDouble()
            tv_last_trs_amount.text = "₹ " + DecimalFormat("##.##")
                .format(i2) + "\n" + context.resources.getString(R.string.due)
            tv_last_trs_amount.setTextColor(context.resources.getColor(R.color.red))
        } else if (Amount < 0) {
            val StringAmt = customersContactModel.lastTrancAmount.replace("-", "")
            val i2 = StringAmt.toDouble()
            tv_last_trs_amount.text = "₹ " + DecimalFormat("##.##")
                .format(i2) + "\n" + context.resources.getString(R.string.due)
            tv_last_trs_amount.setTextColor(context.resources.getColor(R.color.red))
        } else {
            val StringAmt = customersContactModel.lastTrancAmount.replace("-", "")
            val i2 = StringAmt.toDouble()
            tv_last_trs_amount.text = "₹ " + DecimalFormat("##.##")
                .format(i2) + "\n" + context.resources.getString(R.string.advance)
            tv_last_trs_amount.setTextColor(context.resources.getColor(R.color.green_50))
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}