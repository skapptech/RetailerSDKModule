package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.RelatedItemsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemRelatedProductBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment.Companion.newInstance
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class CategoryRelatedItemViewAdapter(
    private val activity: HomeActivity,
    private val list: ArrayList<RelatedItemsModel>
) : RecyclerView.Adapter<CategoryRelatedItemViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_related_product, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set String
        holder.mBinding.tvMargin.text = MyApplication.getInstance().dbHelper.getData("margins")
        if (!TextUtils.isNullOrEmpty(list[position].itemname)) {
            holder.mBinding.tvTitle.text = "" + list[position].itemname
        }
        if (!TextUtils.isNullOrEmpty(list[position].price.toString())) {
            holder.mBinding.tvMrp.text = (MyApplication.getInstance().dbHelper.getData("item_mrp")
                    + " " + list[position].price!!.toDouble().toInt())
        }
        if (!TextUtils.isNullOrEmpty(list[position].logoUrl)) {
            Picasso.get().load(list[position].logoUrl!!.trim { it <= ' ' })
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .into(holder.mBinding.ivTile)
        } else {
            holder.mBinding.ivTile.setImageResource(R.drawable.logo_grey)
        }
        holder.mBinding.tvMarginPercent.text =
            DecimalFormat("##.##").format(list[position].marginPoint!!.toDouble()) + "%"
        holder.mBinding.backLayout.setOnClickListener { view: View? ->
            val args = Bundle()
            args.putString("BaseCategoryId", list[position].baseCategoryId)
            args.putInt("CATEGORY_ID", list[position].categoryid!!.toInt())
            args.putInt("SUB_CAT_ID", list[position].subCategoryId!!.toInt())
            args.putInt("SUB_SUB_CAT_ID", list[position].subsubCategoryid!!.toInt())
            args.putBoolean("HOME_FLAG", true)
            args.putString("SectionType", "RelatedItemSection")
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment: Fragment = newInstance()
            fragment.arguments = args
            fragmentTransaction.replace(R.id.content, fragment)
                .addToBackStack(SubSubCategoryFragment::class.java.name)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: ItemRelatedProductBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}