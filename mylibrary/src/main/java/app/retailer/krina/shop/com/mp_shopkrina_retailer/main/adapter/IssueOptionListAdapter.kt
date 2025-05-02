package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemIssueCategoriesBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues.AddIssueActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueTicketsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.util.*

class IssueOptionListAdapter(
        private val context: Context,
        private val ticketUsersList: ArrayList<IssueTicketsModel>,
        private val listener: AddIssueActivity
) : RecyclerView.Adapter<IssueOptionListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
    ): ViewHolder {
        return ViewHolder(ItemIssueCategoriesBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        ))
    }

    override fun onBindViewHolder(
            viewHolder: ViewHolder,
            i: Int
    ) {
        try {
            if (ticketUsersList[i].IsAskQuestion && ticketUsersList[i].isSelected) {
                viewHolder.mBinding.llCateSelect.visibility = View.GONE
                viewHolder.mBinding.llIsQuestionAnswer.visibility = View.VISIBLE
                viewHolder.mBinding.RBIssueCat.visibility = View.GONE
                viewHolder.mBinding.tvQuestion.text = ticketUsersList[i].Question
                viewHolder.mBinding.doneBtn.setOnClickListener {
                    if (!TextUtils.isNullOrEmpty(viewHolder.mBinding.etAnswer.text.trim().toString())) {
                        listener.doneBtnClick(ticketUsersList[i], viewHolder.mBinding.etAnswer.text.toString())
                    } else {
                        Utils.setToast(context, context.getString(R.string.can_not_empty_field))
                    }
                }
            } else {
                if (!ticketUsersList[i].isRadioBtn) {
                    viewHolder.mBinding.llCateSelect.visibility = View.VISIBLE
                    viewHolder.mBinding.llIsQuestionAnswer.visibility = View.GONE
                    viewHolder.mBinding.RBIssueCat.visibility = View.GONE
                    viewHolder.mBinding.tvIssueCat.text = ticketUsersList[i].CategoryName
                    viewHolder.mBinding.llCateSelect.setOnClickListener {
                        listener.loadOptions(ticketUsersList[i], "")
                    }
                } else {
                    viewHolder.mBinding.RBIssueCat.visibility = View.VISIBLE
                    viewHolder.mBinding.llCateSelect.visibility = View.GONE
                    viewHolder.mBinding.llIsQuestionAnswer.visibility = View.GONE
                    viewHolder.mBinding.RBIssueCat.text = ticketUsersList[i].CategoryName
                    viewHolder.mBinding.RBIssueCat.setOnClickListener {
                        listener.RadioBtnClick(ticketUsersList[i].CategoryName!!)
                    }
                }
                if (ticketUsersList.size - 1 == i) {
                    viewHolder.mBinding.viewLine.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return ticketUsersList.size
    }

    inner class ViewHolder(var mBinding: ItemIssueCategoriesBinding) :
            RecyclerView.ViewHolder(mBinding.root)

    override fun getItemViewType(position: Int): Int {
        return position
    }
}