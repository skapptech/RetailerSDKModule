package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipPlanModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.bumptech.glide.Glide

/**
 * Created by abc on 5/25/2016.
 */
class PrimeBannerAdapter(
    private val context: Context,
    private val list: ArrayList<MembershipPlanModel>?
) : PagerAdapter() {

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.viewpager_membership, container, false)
        val imgflag = itemView.findViewById<ImageView>(R.id.ivImage)
        if (!TextUtils.isNullOrEmpty(list!![position].memberShipLogo)) {
            Glide.with(context).load(list[position].memberShipLogo).into(imgflag)
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}