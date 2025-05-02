package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMyDreamBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.MyDreamInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyDreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DreamItemDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso

class MyDreamAdapter(
    private val context: Context,
    private val list: ArrayList<MyDreamModel>,
    private val myDreamInterafce: MyDreamInterface
) : RecyclerView.Adapter<MyDreamAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<ItemMyDreamBinding>(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_my_dream, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val model = list[position]
        viewHolder.mBinding.btnBuy.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.buy)
        if (model.isDeleted || !model.isActive) {
            viewHolder.mBinding.liItem.visibility = View.GONE
            viewHolder.mBinding.cardView.visibility = View.GONE
        } else {
            viewHolder.mBinding.liItem.visibility = View.VISIBLE
            viewHolder.mBinding.cardView.visibility = View.VISIBLE
        }
        viewHolder.mBinding.tvItemName.text = model.getrItem()
        viewHolder.mBinding.tvDes.text = model.description
        viewHolder.mBinding.tvPointValue.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_points) + model.getrPoint()
        viewHolder.mBinding.tvQty.text = "" + model.qty
        if (!TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    context
                ).getString(SharePrefs.WALLET_POINT)
            ) && SharePrefs.getInstance(context).getString(SharePrefs.WALLET_POINT)
                .toDouble() < model.getrPoint()
        ) {
            viewHolder.mBinding.btnBuy.isEnabled = false
            viewHolder.mBinding.btnBuy.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.grey
                )
            )
        } else {
            viewHolder.mBinding.btnBuy.isEnabled = true
        }
        val temp = model.imageUrl.replace(" ".toRegex(), "%20")
        if (!TextUtils.isNullOrEmpty(temp)) {
            Picasso.get().load(temp)
                .placeholder(R.drawable.top_img_bg)
                .error(R.drawable.top_img_bg)
                .into(viewHolder.mBinding.ivImage)
        } else {
            viewHolder.mBinding.ivImage.setImageResource(R.drawable.top_img_bg)
        }
        viewHolder.mBinding.btnBuy.setOnClickListener { v: View? ->
            val dreamPointlist = ArrayList<DreamItemDetails>()
            val dreamPost = DreamItemDetails(model.getrItemId(), model.qty)
            dreamPointlist.add(dreamPost)
            val wallet = model.getrPoint() * model.qty
            val dreamModel = DreamModel(
                SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE),
                SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID),
                wallet,
                dreamPointlist
            )
            if (model.qty > 0) {
                if (SharePrefs.getInstance(context).getString(SharePrefs.WALLET_POINT)
                        .toDouble() > model.getrPoint() * model.qty
                ) {
                    myDreamInterafce.onBuyBtnClick(dreamModel)
                    viewHolder.mBinding.btnBuy.isEnabled = false
                } else {
                    viewHolder.mBinding.btnBuy.isEnabled = false
                    viewHolder.mBinding.btnBuy.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grey
                        )
                    )
                    Utils.setToast(
                        context,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.toast_walletpoint)
                    )
                }
            } else {
                Utils.setToast(
                    context,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.toast_selectitem)
                )
            }
        }
        viewHolder.mBinding.ivPlus.setOnClickListener { v: View? ->
            if (SharePrefs.getInstance(
                    context
                ).getString(SharePrefs.WALLET_POINT).toDouble() > model.getrPoint() * model.qty
            ) {
                model.qty = model.qty + 1
                viewHolder.mBinding.tvQty.text = "" + model.qty
            } else {
                Utils.setToast(
                    context,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.qty
                )
            }
        }
        viewHolder.mBinding.ivMinus.setOnClickListener { v: View? ->
            if (model.qty > 0) {
                model.qty = model.qty - 1
                viewHolder.mBinding.tvQty.text = "" + model.qty
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val mBinding: ItemMyDreamBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}