package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemAchievedGiftBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GiftItemDcs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils

class AchievedGiftItemAdapter(
    private val context: Context,
    private val list: ArrayList<GiftItemDcs>?
) : RecyclerView.Adapter<AchievedGiftItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAchievedGiftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            val model = list!![i]
            if (!TextUtils.isNullOrEmpty(model.itemName)) {
                viewHolder.mBinding.tvGiftItemName.text =
                    "You will get " + model.qty + " QTY  of  " + model.itemName
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(var mBinding: ItemAchievedGiftBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}