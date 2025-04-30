package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCutomerTargetAssineBrandBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCutomerTargetAssineViewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.TargetClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.TargetOrderListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NewTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso

class CustomerSubCategoryItemAdapter(
    private val context: Context,
    private var list: List<NewTargetModel>?,
    private val listener: TargetClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setData(list: List<NewTargetModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.activity_cutomer_target_assine_view, parent, false
                )
            )
        } else {
            return ViewHolderBrand(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.activity_cutomer_target_assine_brand,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder.itemViewType == 1) {
            val model = list!![position]
            (viewHolder as ViewHolder?)?.mBinding!!.moreInfo.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            (viewHolder as ViewHolder?)?.mBinding!!.tvgiftmpr.text =
                " You get " + model.type + " worth ₹ " + " " + model.value

            if (model.achivePercent >= 100) {
                viewHolder.mBinding.btOrderNow.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.claim)
                viewHolder.mBinding.tvleftDay.text = context.getString(R.string.congratulations)
                viewHolder.mBinding.tvCleamedAmount.text = "" + model.value
            } else {
                viewHolder.mBinding.btOrderNow.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.order_now)
                viewHolder.mBinding.tvleftDay.text = "" + model.leftDays + " Days Left"
                viewHolder.mBinding.tvTargetMonth.text = model.targetMonth + " Target"
            }

            if (!TextUtils.isNullOrEmpty(model.storeUrl)) {
                Picasso.get().load(model.storeUrl).placeholder(R.drawable.direct_logo)
                    .into(viewHolder.mBinding.directLogo)
            } else {
                viewHolder.mBinding.directLogo.setImageResource(R.drawable.direct_logo)
            }

            if (model.isClaimed) {
                viewHolder.mBinding.llTargetDone.visibility = View.VISIBLE
                viewHolder.mBinding.recyclerTargetAssine.visibility = View.VISIBLE
                viewHolder.mBinding.btOrderNow.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.claimed)
                val targetProcessAdapter =
                    TargetProcessAdapter(context, list!![position].model, true)
                viewHolder.mBinding.recyclerTargetAssine.adapter = targetProcessAdapter
            } else {
                viewHolder.mBinding.llTargetDone.visibility = View.GONE
                viewHolder.mBinding.recyclerTargetAssine.visibility = View.VISIBLE
                if (model.achivePercent >= 100)
                    viewHolder.mBinding.btOrderNow.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.claim)
                else
                    viewHolder.mBinding.btOrderNow.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.order_now)
                val targetProcessAdapter =
                    TargetProcessAdapter(context, list!![position].model, model.isClaimed)
                viewHolder.mBinding.recyclerTargetAssine.adapter = targetProcessAdapter
            }
            viewHolder.mBinding.moreInfo.setOnClickListener {
                listener.moreInfoDialog(model)
            }
            viewHolder.mBinding.btOrderNow.setOnClickListener {
                if (!model.isClaimed) {
                    if (model.achivePercent >= 100) {
                        listener.onClaimButtonClicked(
                            model.targetDetailId,
                            "customerTarget"
                        )
                    } else {
                        context.startActivity(
                            Intent(
                                context,
                                TargetOrderListActivity::class.java
                            ).putExtra("companyId", model.companyId)
                        )
                    }
                }
            }
        } else {
            val modelBrand = list!![position]
            (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.moreInfo.paintFlags =
                Paint.UNDERLINE_TEXT_FLAG
            if (modelBrand.type != null && modelBrand.type == "WalletPoint") {
                (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.tvgiftmpr.text =
                    " You will get " + modelBrand.type + " worth ₹" + " " + modelBrand.value
            } else if (modelBrand.type != null && modelBrand.type == "DreamItem") {
                (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.tvgiftmpr.text =
                    " You will get " + modelBrand.type + " " + modelBrand.giftItemName
            } else {
                (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.tvgiftmpr.text =
                    " You get " + modelBrand.type + " worth ₹" + " " + modelBrand.value
            }
            viewHolder.mBindingBrand.tvStoreName.text = modelBrand.storeName ?: ""
            viewHolder.mBindingBrand.tvleftDay.text = "" + modelBrand.leftDays + " Days Left"
            viewHolder.mBindingBrand.tvTargetMonth.text = modelBrand.targetMonth + " Target"
            viewHolder.mBindingBrand.tvBrandName.text = modelBrand.brandNames ?: ""

            if (modelBrand.isClaimed) {
                viewHolder.mBindingBrand.btOrderNow.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.claimed)
            } else {
                if (modelBrand.achivePercent >= 100) {
                    viewHolder.mBindingBrand.btOrderNow.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.claim)
                } else {
                    viewHolder.mBindingBrand.btOrderNow.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.order_now)
                }
            }

            val targetProcessAdapter = TargetProcessAdapter(
                context, list!![position].model, modelBrand.isClaimed
            )
            (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.recyclerTargetAssine.adapter =
                targetProcessAdapter

            if (!TextUtils.isNullOrEmpty(modelBrand.storeUrl)) {
                Picasso.get().load(modelBrand.storeUrl).placeholder(R.drawable.direct_logo)
                    .into((viewHolder as ViewHolderBrand?)?.mBindingBrand!!.directLogo)
            } else {
                (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.directLogo.setImageResource(R.drawable.direct_logo)
            }
            (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.moreInfo.setOnClickListener {
                listener.moreInfoDialog(modelBrand)
            }
            (viewHolder as ViewHolderBrand?)?.mBindingBrand!!.btOrderNow.setOnClickListener {
                if (!modelBrand.isClaimed) {
                    if (modelBrand.achivePercent >= 100) {
                        listener.onClaimButtonClicked(
                            modelBrand.targetDetailId,
                            "companyTarget"
                        )
                    } else {
                        context.startActivity(
                            Intent(context, TargetOrderListActivity::class.java).putExtra(
                                "companyId",
                                modelBrand.companyId
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        if (list!![position].valueType == "Target") {
            return 1
        } else {
            return 2
        }
    }

    inner class ViewHolder(var mBinding: ActivityCutomerTargetAssineViewBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    inner class ViewHolderBrand(var mBindingBrand: ActivityCutomerTargetAssineBrandBinding) :
        RecyclerView.ViewHolder(mBindingBrand.root)
}