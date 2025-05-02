package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemYourLevelBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnTargetLevelClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.YourLevelTargetModel

class YourLevelAdapter(
    private val context: Context,
    private val list: ArrayList<YourLevelTargetModel>?,
    private val onTargetLevelClick: OnTargetLevelClick
) : RecyclerView.Adapter<YourLevelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemYourLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        try {
            viewHolder.mBinding.tvLevelName.text = model.levelName
            if (i == 0) {
                viewHolder.mBinding.viewLine.visibility = View.INVISIBLE
            } else {
                viewHolder.mBinding.viewLine.visibility = View.VISIBLE
            }
            if (model.isSelected) {
                viewHolder.mBinding.viewThumb.setBackgroundResource(R.drawable.circle)
                viewHolder.mBinding.viewLine.setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))
            } else {
                viewHolder.mBinding.viewThumb.setBackgroundResource(R.drawable.circle_grey)
                viewHolder.mBinding.viewLine.setBackgroundColor(context.resources.getColor(R.color.grey))
            }
            viewHolder.mBinding.viewThumb.setOnClickListener {
                onTargetLevelClick.onSelectClick(
                    i,
                    model
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemYourLevelBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}