package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.BasecategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SearchfilterChildadapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BrandClicked

class SearchFilterBrandAdapter(
    private val context: Context,
    private val brandClicked: BrandClicked
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<BasecategoryModel>? = null
    private var selectedPos = 0

    fun setData(list: ArrayList<BasecategoryModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            SearchfilterChildadapterBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        try {
            val model = list!![position]
            (vh as ViewHolder).mBinding.checkbox.text = model.subSubcategoryName
            // ((ViewHolder) vh).mBinding.checkbox.setChecked(selectedPos == position);
            if (model.isChecked) {
                vh.mBinding.checkbox.isChecked = true
                vh.mBinding.lblListHeader.visibility = View.VISIBLE
            } else {
                vh.mBinding.checkbox.isChecked = false
                vh.mBinding.lblListHeader.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    inner class ViewHolder(val mBinding: SearchfilterChildadapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {

        init {
            mBinding.checkbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                try {
                    if (compoundButton.isChecked) {
                        selectedPos = adapterPosition
                        list!![selectedPos].isChecked = true
                        brandClicked.onBrandClicked(selectedPos)
                    } else {
                        selectedPos = adapterPosition
                        list!![selectedPos].isChecked = false
                        brandClicked.onBrandClicked(selectedPos)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}