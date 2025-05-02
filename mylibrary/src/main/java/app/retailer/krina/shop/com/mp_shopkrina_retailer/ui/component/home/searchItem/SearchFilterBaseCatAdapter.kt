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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SerachfilterheaderadapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BaseCatClicked

class SearchFilterBaseCatAdapter(
    private var list: ArrayList<BasecategoryModel>,
    private val context: Context,
    private val searchCheckBoxClick: BaseCatClicked,
    private var selectedPos: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setData(list: ArrayList<BasecategoryModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            SerachfilterheaderadapterBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        try {
            val model = list[position]
            (vh as ViewHolder).mBinding.tvTitle.text = model.baseCategoryName
            if (list[position].isChecked) {
                vh.mBinding.tvTitle.isChecked = true
                vh.mBinding.lblListHeader.visibility = View.VISIBLE
            } else {
                vh.mBinding.tvTitle.isChecked = false
                vh.mBinding.lblListHeader.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(var mBinding: SerachfilterheaderadapterBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {

        init {
            mBinding.tvTitle.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                try {
                    if (compoundButton.isChecked) {
                        selectedPos = adapterPosition
                        list[selectedPos].isChecked = true
                        searchCheckBoxClick.getPosition(selectedPos)
                    } else {
                        selectedPos = adapterPosition
                        list[selectedPos].isChecked = false
                        searchCheckBoxClick.getPosition(selectedPos)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}