package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemSubcategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class ClearanceCatAdapter(
    private val context: Context,
    private var list: List<CategoriesModel>?,
    private val subCategoryInterface: SubCategoryInterface
) : RecyclerView.Adapter<ClearanceCatAdapter.ViewHolder>() {

    var basCatId = 0
    private var categoryId = 0


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
            val model = list!![i]
            vh.mBinding.filterTitle.text = model.categoryname
            if (!TextUtils.isNullOrEmpty(model.logourl)) {
                Picasso.get().load(model.logourl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(vh.mBinding.subCatImg)
            } else {
                vh.mBinding.subCatImg.setImageResource(R.drawable.ic_placeholder)
            }
            vh.mBinding.subCategoryLayout.setOnClickListener {
                if (categoryId != model.categoryid) {
                    val analyticPost = AnalyticPost()
                    analyticPost.baseCatId = basCatId.toString()
                    analyticPost.categoryId = model.categoryid
                    RetailerSDKApp.getInstance().updateAnalytics(
                        model.categoryname
                                + "_click", analyticPost
                    )
                    categoryId = model.categoryid
                    subCategoryInterface.SubCategoryClicked(categoryId, model.categoryid)
                    notifyDataSetChanged()
                }
            }
            if (categoryId == model.categoryid) {
                vh.mBinding.subCategoryLayout.background =
                    context.resources.getDrawable(R.drawable.select_orange_border)
                vh.mBinding.filterTitle.setTextColor(Color.parseColor("#ff6347"))
                subCategoryInterface.SubCategoryClicked(model.categoryid, model.categoryid)
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
        return if (list == null) 0 else list!!.size
    }

    inner class ViewHolder(var mBinding: ItemSubcategoryBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}