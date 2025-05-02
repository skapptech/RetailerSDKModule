package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.bumptech.glide.Glide

class ImageAdapter(
    private var context: Context,
    private val list: ArrayList<ImageObjEntity>?,
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_full_image, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.ivImage)

        val model = list!![position]
        if (!TextUtils.isNullOrEmpty(model.imgFileFullPath)) {
            Glide.with(context)
                .load(SharePrefs.getInstance(context).getString(SharePrefs.TRADE_WEB_URL) + model.imgFileFullPath)
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .into(imageView)
        }
        imageView.setOnClickListener {
            if (context !is FeedImageShowActivity) {
                context.startActivity(
                    Intent(context, FeedImageShowActivity::class.java)
                        .putExtra("ImagePath", list)
                        .putExtra("pos", position))
            }
        }
        container.addView(itemView)
        return itemView
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }

//    inner class ViewHolder(var binding: ItemFeedImageBinding) : RecyclerView.ViewHolder(
//        binding.root
//    )

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
