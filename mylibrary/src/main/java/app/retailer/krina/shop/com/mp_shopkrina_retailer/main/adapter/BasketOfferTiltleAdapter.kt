package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.BasketCategoryTitleBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BasketFilterClicked

class BasketOfferTiltleAdapter(
    private val context: Context,
    private val title: ArrayList<String>,
    private val basketFilterClicked: BasketFilterClicked
) : RecyclerView.Adapter<BasketOfferTiltleAdapter.ViewHolder>() {
    private var subsubCatId = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.basket_category_title, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        try {
            vh.mBinding.filterTitle.text = title[i]
            vh.layout.setOnClickListener { view: View? ->
                subsubCatId = i
                notifyDataSetChanged()
            }
            if (subsubCatId == i) {
                vh.layout.background =
                    context.resources.getDrawable(R.drawable.select_orange_basket)
                vh.filterTitle.setTextColor(context.resources.getColor(R.color.colorAccent))
                vh.ivImg.setImageDrawable(context.resources.getDrawable(R.drawable.ic_next_orange))
                basketFilterClicked.onClicked(title[i])
            } else {
                vh.layout.background = context.resources.getDrawable(R.drawable.select_white_basket)
                vh.filterTitle.setTextColor(context.resources.getColor(R.color.black))
                vh.ivImg.setImageDrawable(context.resources.getDrawable(R.drawable.ic_next_black))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return title.size
    }

    inner class ViewHolder(var mBinding: BasketCategoryTitleBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        var ivImg: ImageView
        var filterTitle: TextView
        var layout: LinearLayout

        init {
            ivImg = mBinding.ivImg
            filterTitle = mBinding.filterTitle
            layout = mBinding.llTitle
        }
    }
}