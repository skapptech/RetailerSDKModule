package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemIssueTicketsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues.MyIssueDetailActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssuesResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyIssueListAdapter(
    private val context: Context,
    private val issueList: ArrayList<MyIssuesResponseModel>
) : RecyclerView.Adapter<MyIssueListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_issue_tickets, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        try {
            viewHolder.mBinding.tvIssueId.text = "#" + issueList[i].TicketId.toString()
            viewHolder.mBinding.tvStatus.text = issueList[i].Status.toString()
            if (issueList[i].Status == "Pending") {
                viewHolder.mBinding.tvStatus.setTextColor(
                    context.resources.getColor(R.color.red_light)
                )
            } else if (issueList[i].Status == "Closed") {
                viewHolder.mBinding.tvStatus.setTextColor(
                    context.resources.getColor(R.color.green_dark)
                )
            } else if (issueList[i].Status == "Open") {
                viewHolder.mBinding.tvStatus.setTextColor(
                    context.resources.getColor(R.color.slime_green)
                )
            } else if (issueList[i].Status == "On Hold") {
                viewHolder.mBinding.tvStatus.setTextColor(
                    context.resources.getColor(R.color.chinese_orange)
                )
            }

            if (!TextUtils.isNullOrEmpty(issueList[i].CreatedDate)) {
                viewHolder.mBinding.tvReportedDate.text =
                    Utils.getDateTimeFormate(issueList[i].CreatedDate)
            }
            //    viewHolder.mBinding.tvResolvedTime.text = issueList[i].TATInHrs.toString() + " Hour"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return issueList.size
    }

    inner class ViewHolder(var mBinding: ItemIssueTicketsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.llViewDetail.setOnClickListener {
                val intent = Intent(context, MyIssueDetailActivity::class.java)
                intent.putExtra("model", issueList[adapterPosition])
                context.startActivity(intent)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

}