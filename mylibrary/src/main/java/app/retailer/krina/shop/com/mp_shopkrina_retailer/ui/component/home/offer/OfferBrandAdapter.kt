package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemOfferBrandAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp

class OfferBrandAdapter(
    private val context: Context,
    private var categoryFilterList: List<SubSubCategoriesModel>?,
    private val subSubCategoryFilterInterface: SubSubCategoryFilterInterface
) : RecyclerView.Adapter<OfferBrandAdapter.ViewHolder>() {

    private var subSubCatId = -1

    fun setCategoryOrderFilterList(
        categoryFilterList: List<SubSubCategoriesModel>?, subSubCatId: Int
    ) {
        this.categoryFilterList = categoryFilterList
        this.subSubCatId = subSubCatId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_offer_brand_adapter,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        try {
            val model = categoryFilterList!![i]
            vh.mBinding.tvTitle.text = model.subsubcategoryname

            vh.mBinding.liOfferBrand.setOnClickListener {
                subSubCatId = model.subsubcategoryid
                notifyDataSetChanged()
            }
            if (subSubCatId == model.subsubcategoryid) {
                vh.mBinding.liOfferBrand.background =
                    context.resources.getDrawable(R.drawable.drawable_select_orange_border)
                vh.mBinding.tvTitle.setTextColor(Color.parseColor("#ff6347"))
                subSubCategoryFilterInterface.SubSubCategoryFilterClicked(
                    i, model.subsubcategoryid, model.subcategoryid, model.categoryid
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
                vh.mBinding.liOfferBrand.background =
                    context.resources.getDrawable(R.drawable.drawable_unselect_white_border)
                vh.mBinding.tvTitle.setTextColor(Color.parseColor("#000000"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (categoryFilterList == null) 0 else categoryFilterList!!.size
    }

    inner class ViewHolder(var mBinding: ItemOfferBrandAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}