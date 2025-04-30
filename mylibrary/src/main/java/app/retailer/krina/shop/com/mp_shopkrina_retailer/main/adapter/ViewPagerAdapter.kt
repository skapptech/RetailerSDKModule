package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SliderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.bumptech.glide.Glide

/**
 * Created by abc on 5/25/2016.
 */
class ViewPagerAdapter(private val context: Context, private val picList: ArrayList<SliderModel>?) :
    PagerAdapter() {

    override fun getCount(): Int {
        return picList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.viewpager_subsubcat, container, false)
        val imgflag = itemView.findViewById<ImageView>(R.id.ivSlider)
        val vectorDrawable = AppCompatResources.getDrawable(context, R.drawable.logo_grey)
        if (!TextUtils.isNullOrEmpty(picList!![position].image)) {
            if (picList[position].id != 4154) {
                Glide.with(context).load(picList[position].image).placeholder(vectorDrawable)
                    .into(imgflag)
            } else {
                Glide.with(context).load(picList[position].image).placeholder(vectorDrawable)
                    .error(vectorDrawable).into(imgflag)
            }
        } else {
            imgflag.setImageDrawable(vectorDrawable)
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView(`object` as CardView)
    }
}