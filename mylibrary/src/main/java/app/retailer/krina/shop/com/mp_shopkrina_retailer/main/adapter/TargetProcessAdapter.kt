package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ViewTargetProcessBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TargetConditionsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import java.text.DecimalFormat

class TargetProcessAdapter(
    private val context: Context,
    private var targetProcessList: ArrayList<TargetConditionsModel?>? = null,
    private val isCleamed: Boolean
) :
    RecyclerView.Adapter<TargetProcessAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(

            ViewTargetProcessBinding.inflate(
                LayoutInflater.from(parent.context),
                 parent, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = targetProcessList!![i]

        if (isCleamed) {
            viewHolder.mBinding.llisClemedView.visibility = View.VISIBLE
            viewHolder.mBinding.llMainView.visibility = View.GONE
            viewHolder.mBinding.tvShopTargetIsClamed.text = model!!.conditionText

            if (model.achivePercent >= 100) {
                viewHolder.mBinding.tvShopTarget.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.targetcomplet,
                    0,
                    0,
                    0
                )
            } else {
                viewHolder.mBinding.tvShopTarget.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.target_incomplet,
                    0,
                    0,
                    0
                )
            }
        } else {
            viewHolder.mBinding.llisClemedView.visibility = View.GONE
            viewHolder.mBinding.llMainView.visibility = View.VISIBLE

            viewHolder.mBinding.tvShopTarget.text = model!!.conditionText
            viewHolder.mBinding.tvCompletePercent.text = model.conditionCompleteText

            if (!TextUtils.isNullOrEmpty(model.message)) {
                viewHolder.mBinding.tvTargetIncomplte.text = model.message
            } else {
                viewHolder.mBinding.tvTargetIncomplte.visibility = View.GONE
            }
            viewHolder.mBinding.progressPercent.text = "" + DecimalFormat("##.##").format(model.achivePercent) + " %"
            viewHolder.mBinding.targetProgress.progress = model.achivePercent.toInt()

            if (model.achivePercent >= 100) {
                viewHolder.mBinding.tvShopTarget.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.targetcomplet,
                    0,
                    0,
                    0
                );
            } else {
                viewHolder.mBinding.tvShopTarget.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.target_incomplet,
                    0,
                    0,
                    0
                );
            }
        }
    }

    override fun getItemCount(): Int {
        return if (targetProcessList == null) 0 else targetProcessList!!.size
    }

    inner class ViewHolder(var mBinding: ViewTargetProcessBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }
}