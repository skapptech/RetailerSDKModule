package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CommentTextAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyIssueDetailAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssueDetailModel.CommentList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class IssueDetailAdapter(private val context: Context, private var list: ArrayList<CommentList>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1


    fun updateData(list: ArrayList<CommentList>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                MyIssueDetailAdapterBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
            TYPE_ITEM -> ViewHolder(
                CommentTextAdapterBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
            else -> ViewHolder(
                CommentTextAdapterBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list!![position]
        if (holder.itemViewType == TYPE_HEADER) {
            (holder as HeaderViewHolder).tvTicketDescription.text = model.ticketDescription
            holder.time1.text = Utils.getTimeForChat(
                model.createdDate
            )
        } else {
            (holder as ViewHolder).Msg.text = model.comment.toString()
            holder.time.text = Utils.getTimeForChat(
                model.createdDate
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list!![position].createdBy.equals("me")) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class HeaderViewHolder internal constructor(var mBinding: MyIssueDetailAdapterBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var tvTicketDescription: TextView = mBinding.tvTicketDescription
        var time1: TextView = mBinding.tvTime1
    }

    inner class ViewHolder(var mBinding: CommentTextAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        var Msg: TextView = mBinding.tvMsg
        var time: TextView = mBinding.tvTime
    }
}