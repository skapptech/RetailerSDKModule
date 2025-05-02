package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSubcategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class SubCategoryFilterAdapter(
    private val context: Context,
    private var subCategoriesModelList: List<SubCategoriesModel>?,
    private val subCategoryInterface: SubCategoryInterface
) : RecyclerView.Adapter<SubCategoryFilterAdapter.ViewHolder>() {

    var basCatId = 0
    private var subCategoryId = -1

    fun setSubcategoryOrderList(
        subCategoriesModelList: List<SubCategoriesModel>?,
        subCategoryId: Int
    ) {
        this.subCategoriesModelList = subCategoriesModelList
        notifyDataSetChanged()
        this.subCategoryId = subCategoryId
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_subcategory, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        try {
            val model = subCategoriesModelList!![i]
            vh.mBinding.filterTitle.text = model.subcategoryname
            if (!TextUtils.isNullOrEmpty(model.logourl)) {
                Picasso.get().load(model.logourl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(vh.mBinding.subCatImg)
            } else {
                vh.mBinding.subCatImg.setImageResource(R.drawable.ic_placeholder)
            }
            vh.mBinding.subCategoryLayout.setOnClickListener {
                val analyticPost = AnalyticPost()
                analyticPost.baseCatId = basCatId.toString()
                analyticPost.categoryId = model.categoryid
                analyticPost.subCatId = model.subcategoryid
                RetailerSDKApp.getInstance().updateAnalytics(
                    model.subcategoryname
                            + "_click", analyticPost
                )
                subCategoryId = model.subcategoryid
                subCategoryInterface.SubCategoryClicked(subCategoryId, model.categoryid)
                notifyDataSetChanged()
            }
            if (subCategoryId == model.subcategoryid) {
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
}