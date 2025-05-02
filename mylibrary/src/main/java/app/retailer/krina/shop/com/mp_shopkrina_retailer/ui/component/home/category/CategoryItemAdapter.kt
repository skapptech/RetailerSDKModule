package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.CategoriesResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemListGridviewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment.Companion.newInstance
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class CategoryItemAdapter internal constructor(
    context: Context,
    list: ArrayList<CategoriesResponse>?
) : RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>() {
    private val list: ArrayList<CategoriesResponse>?
    private val handler = Handler(Looper.getMainLooper())
    private val activity: HomeActivity = context as HomeActivity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_list_gridview,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val categories = list!![i]
        viewHolder.binding.textViewGirdItemHome.text = categories.categoryName
        viewHolder.binding.catLinearLayout.isEnabled = true
        if (!TextUtils.isNullOrEmpty(categories.logoUrl)) {
            Picasso.get().load(categories.logoUrl)
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .into(viewHolder.binding.imageViewGirdItemHome)
        } else {
            viewHolder.binding.imageViewGirdItemHome.setImageResource(R.drawable.logo_grey)
        }
        viewHolder.binding.catLinearLayout.setOnClickListener {
            // update analytics
            viewHolder.binding.catLinearLayout.isEnabled = false
            val args = Bundle()
            args.putInt("BaseCategoryId", categories.baseCategoryId)
            args.putInt("CATEGORY_ID", categories.categoryid)
            args.putString("SectionType", "BottomCategory")
            handler.postDelayed({
                val fragmentManager = activity.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment = newInstance()
                fragment.arguments = args
                fragmentTransaction.replace(R.id.content, fragment)
                    .addToBackStack(SubSubCategoryFragment::class.java.name)
                fragmentTransaction.commit()
            }, 350)
            // analytics data
            val analyticPost = AnalyticPost()
            analyticPost.baseCatId = "" + categories.baseCategoryId
            analyticPost.categoryId = categories.categoryid
            analyticPost.categoryName = categories.categoryName
            MyApplication.getInstance().updateAnalytics("category_click", analyticPost)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(val binding: ItemListGridviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        this.list = list
    }
}