package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SubcategoryitemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment.Companion.newInstance
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CategoryDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class SubCategoryAdapter(
    private val context: Context,
    private var subCategoryList: List<CategoryDetailsModel>?,
    baseCatId: Int,
    mSectionType: String
) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private val activity: HomeActivity
    private val handler = Handler()
    private var baseCatId: Int
    private val mSectionType: String

    fun setSubCategory(list: List<CategoryDetailsModel>?, baseCatId: Int) {
        subCategoryList = list
        this.baseCatId = baseCatId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubcategoryitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        try {
            val obj = subCategoryList!![vh.adapterPosition]
            vh.binding.catLinearLayout.setOnClickListener {
                val args = Bundle()
                args.putInt("BaseCategoryId", baseCatId)
                args.putInt("CATEGORY_ID", obj.categoryid)
                args.putString("SectionType", mSectionType)
                vh.binding.catLinearLayout.isEnabled = false
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        //Do something after 100ms
                        vh.binding.catLinearLayout.isEnabled = true
                        val fragmentManager = activity.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        val fragment: Fragment = newInstance()
                        fragment.arguments = args
                        fragmentTransaction.replace(R.id.content, fragment).addToBackStack(
                            SubSubCategoryFragment::class.java.name
                        )
                        fragmentTransaction.commit()
                        handler.postDelayed(this, 500)
                        handler.removeCallbacks(this)
                    }
                }, 500)
            }
            vh.binding.textViewGirdItemHome.text = obj.categoryName
            val vectorDrawable = AppCompatResources.getDrawable(context, R.drawable.logo_grey)
            if (!TextUtils.isNullOrEmpty(obj.logoUrl)) {
                Picasso.get().load(obj.logoUrl).placeholder((vectorDrawable)!!)
                    .into(vh.binding.imageViewGirdItemHome)
            } else {
                vh.binding.imageViewGirdItemHome.setImageDrawable(vectorDrawable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (subCategoryList == null) 0 else subCategoryList!!.size
    }

    inner class ViewHolder(var binding: SubcategoryitemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    init {
        activity = context as HomeActivity
        this.baseCatId = baseCatId
        this.mSectionType = mSectionType
    }
}