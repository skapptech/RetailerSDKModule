package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSubSubcategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class SubSubCategoryAdapter(
    private val context: Context,
    private var categoryfilterList: List<SubSubCategoriesModel>?,
    private val subSubCategoryFilterInterface: SubSubCategoryFilterInterface
) : RecyclerView.Adapter<SubSubCategoryAdapter.ViewHolder>() {

    private var subSubCatId = -1

    fun setcategoryOrderFilterList(
        categoryfilterList: List<SubSubCategoriesModel>?,
        subSubCatId: Int
    ) {
        this.categoryfilterList = categoryfilterList
        this.subSubCatId = subSubCatId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_sub_subcategory, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        try {
            val model = categoryfilterList!![i]
            vh.mBinding.filterTitle.text = model.subsubcategoryname
            if (!TextUtils.isNullOrEmpty(model.logourl)) {
                Picasso.get().load(model.logourl)
                    .placeholder(R.drawable.logo_grey)
                    .error(R.drawable.ic_placeholder)
                    .into(vh.mBinding.subCatImg)
            } else {
                vh.mBinding.subCatImg.setImageResource(R.drawable.ic_placeholder)
            }
            vh.mBinding.subSubCategoryLayout.setOnClickListener {
                subSubCatId = model.subsubcategoryid
                notifyDataSetChanged()
            }
            if (subSubCatId == model.subsubcategoryid) {
                vh.mBinding.subSubCategoryLayout.background =
                    context.resources.getDrawable(R.drawable.drawable_select_orange_border)
                vh.mBinding.filterTitle.setTextColor(Color.parseColor("#ff6347"))
                subSubCategoryFilterInterface.SubSubCategoryFilterClicked(
                    i, model.subsubcategoryid,
                    model.subcategoryid, model.categoryid
                )
                // analytics data
                val analyticPost = AnalyticPost()
                analyticPost.baseCatId = model.basecategoryid.toString()
                analyticPost.categoryId = model.categoryid
                analyticPost.subCatId = model.subcategoryid
                analyticPost.subSubCatId = model.subcategoryid
                RetailerSDKApp.getInstance()
                    .updateAnalytics(model.subsubcategoryname + "_click", analyticPost)
            } else {
                vh.mBinding.subSubCategoryLayout.background =
                    context.resources.getDrawable(R.drawable.drawable_unselect_white_border)
                vh.mBinding.filterTitle.setTextColor(Color.parseColor("#000000"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (categoryfilterList == null) 0 else categoryfilterList!!.size
    }

    inner class ViewHolder(var mBinding: ItemSubSubcategoryBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}