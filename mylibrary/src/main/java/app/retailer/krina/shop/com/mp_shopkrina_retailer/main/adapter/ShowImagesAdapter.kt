package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabKitabImageModel
import com.bumptech.glide.Glide

class ShowImagesAdapter(
    private val context: Context,
    private val trainingDevelopmentAL: ArrayList<HisabKitabImageModel>
) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout =
            LayoutInflater.from(context).inflate(R.layout.slidingimages_layout, view, false)!!
        val imageView = imageLayout.findViewById<ImageView>(R.id.myZoomageView)
        Glide.with(context).load(trainingDevelopmentAL[position].imagePath.replace("\"", ""))
            .into(imageView)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun getCount(): Int {
        return trainingDevelopmentAL.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}