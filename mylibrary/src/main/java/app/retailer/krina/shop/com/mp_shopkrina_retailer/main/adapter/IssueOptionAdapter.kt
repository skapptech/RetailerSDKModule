package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemTicketUsersBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues.AddIssueActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueThreadModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueTicketsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class IssueOptionAdapter(
    private val context: Context,
    private var chatList: ArrayList<IssueThreadModel>,
    private var ticketUsersList: ArrayList<IssueTicketsModel>,
    private val listener: AddIssueActivity
) : RecyclerView.Adapter<IssueOptionAdapter.ViewHolder>() {
    private var adapter: IssueOptionListAdapter? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        return ViewHolder(
            ItemTicketUsersBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        try {
            viewHolder.mBinding.ltvMessageTime.text = Utils.getTimeForChat(chatList[i].time)
            viewHolder.mBinding.ltvResTime.text = Utils.getTimeForChat(chatList[i].time)
            if (chatList[i].isSelected) {
                viewHolder.mBinding.rtvMessage.text = chatList[i].message

                viewHolder.mBinding.lcvTicketItem.visibility = View.GONE
                viewHolder.mBinding.rcvTicketItem.visibility = View.VISIBLE
            } else {
                if (i == 0) {
                    val arrSplit: List<String> = chatList[i].message.split(", ")
                    viewHolder.mBinding.ltvMessageName.visibility = View.GONE

                    viewHolder.mBinding.rcvTicketItem.visibility = View.GONE
                    viewHolder.mBinding.cvRvIssue.visibility = View.GONE
                    viewHolder.mBinding.ltvMessage.text = arrSplit[0] + ","
                    if (!TextUtils.isNullOrEmpty(arrSplit[0])) {
                        viewHolder.mBinding.ltvMessageName.text = arrSplit[0]
                    }
                } else {
                    if (i == chatList.size - 1) {
                        if (ticketUsersList.size != 0) {
                            val layoutManager = LinearLayoutManager(context)
                            viewHolder.mBinding.rvIssueCategories.layoutManager = layoutManager
                            adapter = IssueOptionListAdapter(context, ticketUsersList, listener)
                            viewHolder.mBinding.rvIssueCategories.adapter = adapter
                            viewHolder.mBinding.cvRvIssue.visibility = View.VISIBLE
                        }
                    } else {
                        viewHolder.mBinding.cvRvIssue.visibility = View.GONE
                    }
                    viewHolder.mBinding.ltvMessageName.visibility = View.GONE
                    viewHolder.mBinding.ltvMessage.text = chatList[i].message
                    viewHolder.mBinding.rcvTicketItem.visibility = View.GONE
                    if (TextUtils.isNullOrEmpty(chatList[i].message)) {
                        viewHolder.mBinding.lcvTicketItem.visibility = View.GONE
                    } else {
                        viewHolder.mBinding.lcvTicketItem.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ViewHolder(var mBinding: ItemTicketUsersBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun getItemViewType(position: Int): Int {
        return position
    }
}