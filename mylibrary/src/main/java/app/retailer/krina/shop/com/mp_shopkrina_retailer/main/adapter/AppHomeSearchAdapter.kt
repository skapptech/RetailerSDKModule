package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemHomeRecentSearchBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem.SearchItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class AppHomeSearchAdapter(
    private val activity: HomeActivity,
    private val list: ArrayList<AppItemsList>,
    listSize: Int
) : RecyclerView.Adapter<AppHomeSearchAdapter.ViewHolder>() {
    @JvmField
    var listSize = 3

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemHomeRecentSearchBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = list[i]
        // set values
        holder.mBinding.tvName.text = model.tileName
        holder.mBinding.tvPrice.text = "₹" + DecimalFormat("##.##").format(model.unitPrice)
        holder.mBinding.tvMrp.text = "₹" + DecimalFormat("##.##").format(model.price)
        holder.mBinding.tvMrp.paintFlags =
            holder.mBinding.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        Picasso.get().load(model.tileImage).placeholder(R.drawable.logo_grey)
            .error(R.drawable.logo_grey).into(holder.mBinding.ivImage)
    }

    override fun getItemCount(): Int {
        return if (list.size > listSize) listSize else list.size
    }

    inner class ViewHolder(val mBinding: ItemHomeRecentSearchBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        init {
            mBinding.liItem.setOnClickListener { v: View? ->
                val bundle = Bundle()
                bundle.putInt("pos", 67)
                bundle.putString("query", list[adapterPosition].bannerImage)
                activity.pushFragments(SearchItemFragment.newInstance(), true, true, bundle)
            }
        }
    }

    init {
        this.listSize = listSize
    }
}