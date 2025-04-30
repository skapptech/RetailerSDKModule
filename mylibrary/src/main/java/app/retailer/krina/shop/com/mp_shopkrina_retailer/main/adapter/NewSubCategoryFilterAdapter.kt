package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSubcategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.bumptech.glide.Glide

class NewSubCategoryFilterAdapter(
    private val context: Context,
    private var subCategoriesModelList: List<SubCategoriesModel>?,
    private val subCategoryInterface: SubCategoryInterface
) : RecyclerView.Adapter<NewSubCategoryFilterAdapter.ViewHolder>() {
    private var clickIndexing = -1
    fun setSubcategoryOrderList(subCategoriesModelList: List<SubCategoriesModel>?) {
        this.subCategoriesModelList = subCategoriesModelList
        notifyDataSetChanged()
        clickIndexing = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<ItemSubcategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_subcategory, parent, false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        try {
            val vectorDrawable = AppCompatResources.getDrawable(context, R.drawable.logo_grey)
            val model = subCategoriesModelList!![i]
            vh.mBinding.filterTitle.text = model.subcategoryname
            if (!TextUtils.isNullOrEmpty(model.logourl)) {
                Glide.with(context).load(model.logourl).placeholder(vectorDrawable)
                    .into(vh.mBinding.subCatImg)
            } else {
                vh.mBinding.subCatImg.setImageDrawable(vectorDrawable)
            }
            vh.mBinding.subCategoryLayout.setOnClickListener { view: View? ->
                clickIndexing = i
                subCategoryInterface.SubCategoryClicked(model.subcategoryid, model.categoryid)
                notifyDataSetChanged()
            }
            if (clickIndexing == i) {
                vh.mBinding.subCategoryLayout.background =
                    context.resources.getDrawable(R.drawable.select_orange_border)
                vh.mBinding.filterTitle.setTextColor(Color.parseColor("#ff6347"))
                subCategoryInterface.SubCategoryClicked(model.subcategoryid, model.categoryid)
            } else {
                vh.mBinding.subCategoryLayout.background =
                    context.resources.getDrawable(R.drawable.unselect_white_border)
                vh.mBinding.filterTitle.setTextColor(Color.parseColor("#000000"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (subCategoriesModelList == null) 0 else subCategoriesModelList!!.size
    }

    inner class ViewHolder(var mBinding: ItemSubcategoryBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    interface SubCategoryInterface {
        fun SubCategoryClicked(subCategoryId: Int, categoryId: Int)
    }
}