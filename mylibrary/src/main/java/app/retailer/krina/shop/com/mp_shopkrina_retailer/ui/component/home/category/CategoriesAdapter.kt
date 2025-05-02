package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.AllCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemCategoriesBinding

class CategoriesAdapter(
    private val context: Context,
    private val list: ArrayList<AllCategoryModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHolder(
            ItemCategoriesBinding.inflate(
                LayoutInflater.from(parent.context),
               parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerViewHolder).mBinding.tvHeader.text = list!![position].baseCategoryName
        holder.mBinding.recyclerCategories.adapter =
            CategoryItemAdapter(context, list[position].categoriesList)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class RecyclerViewHolder internal constructor(var mBinding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}