package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemHisabDeatailsServerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemHisabDeatailsUserBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnChatClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class HisabDetailAdapter(
    private val context: Context,
    private val hisabDetailList: ArrayList<HisabDetailModel>?,
    private val onChatClick: OnChatClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return if (i == HisabDetailModel.USER_SIDE) {
            ViewHolder(
                ItemHisabDeatailsUserBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        } else {
            ViewHolderTwo(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_hisab_deatails_server,
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val hisabDetailModel = hisabDetailList!![position]
        if (viewHolder.itemViewType == HisabDetailModel.USER_SIDE) {
            (viewHolder as ViewHolder).mBinding.messageUser.text = "₹ " + hisabDetailModel.amount
            if (hisabDetailModel.createdDate != null) {
                try {
                    viewHolder.mBinding.tvDateTime.text =
                        Utils.getChangeDateFormatInProfile(hisabDetailModel.createdDate)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (hisabDetailModel.getHisabNotesList()[0].notes == "") {
                viewHolder.mBinding.tvNote.visibility = View.GONE
            } else {
                viewHolder.mBinding.tvNote.visibility = View.VISIBLE
                viewHolder.mBinding.tvNote.text = hisabDetailModel.getHisabNotesList()[0].notes
            }
            if (hisabDetailModel.hisabKitabImagesList.size > 0) {
                viewHolder.mBinding.imUserCamera.visibility = View.VISIBLE
            } else {
                viewHolder.mBinding.imUserCamera.visibility = View.GONE
            }
            viewHolder.mBinding.RlMessage.setOnClickListener { v: View? ->
                onChatClick.onSelectClick(
                    position,
                    "Give"
                )
            }
        } else {
            (viewHolder as ViewHolderTwo).theirMessageBinding.messageServer.text =
                "₹ " + hisabDetailModel.amount
            try {
                viewHolder.theirMessageBinding.tvDateTime.text =
                    Utils.getChangeDateFormatInProfile(hisabDetailModel.createdDate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (hisabDetailModel.getHisabNotesList()[0].notes == "") {
                viewHolder.theirMessageBinding.tvNote.visibility = View.GONE
            } else {
                viewHolder.theirMessageBinding.tvNote.visibility = View.VISIBLE
                viewHolder.theirMessageBinding.tvNote.text =
                    hisabDetailModel.getHisabNotesList()[0].notes
            }
            if (hisabDetailModel.hisabKitabImagesList.size > 0) {
                viewHolder.theirMessageBinding.imServerCamera.visibility = View.VISIBLE
            } else {
                viewHolder.theirMessageBinding.imServerCamera.visibility = View.GONE
            }
            viewHolder.theirMessageBinding.RLSServer.setOnClickListener { v: View? ->
                onChatClick.onSelectClick(
                    position,
                    "Accept"
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (SharePrefs.getInstance(context)
                .getString(SharePrefs.HISAB_KITAB_ID) == hisabDetailList!![position].CrCustomerId
        ) {
            HisabDetailModel.USER_SIDE
        } else {
            HisabDetailModel.SERVER_SIDE
        }
    }

    override fun getItemCount(): Int {
        return hisabDetailList?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemHisabDeatailsUserBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    inner class ViewHolderTwo internal constructor(var theirMessageBinding: ItemHisabDeatailsServerBinding) :
        RecyclerView.ViewHolder(
            theirMessageBinding.root
        )
}