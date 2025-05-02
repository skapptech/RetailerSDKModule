package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.MyAccountAdapterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.*
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class MyAccountAdapter(private val _context: Context, private val mylist: ArrayList<String>) :
    RecyclerView.Adapter<MyAccountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.my_account_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.tvTitle.text = mylist[position]
        val childText = mylist[position]
        if (childText == _context.resources.getString(R.string.txt_My_Order)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_myorder)
        } else if (childText == _context.resources.getString(R.string.txt_My_Wallet)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_wallet_new)
        } else if (childText == _context.resources.getString(R.string.txt_My_Dream)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_my_dream)
        } else if (childText == _context.resources.getString(R.string.txt_My_Favourite)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_shopping_online)
        } else if (childText == _context.resources.getString(R.string.my_ledger)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_accounting)
        } else if (childText == _context.resources.getString(R.string.kissan_dan)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_daan)
        } else if (childText == _context.resources.getString(R.string.my_target)) {
            holder.mBinding.iconGroup.setImageResource(R.drawable.ic_target)
        } /*else if (childText.equals(_context.getResources().getString(R.string.my_issue))) {
            holder.icon_group.setImageResource(R.drawable.ic_discuss_issue);
        }*/
        holder.mBinding.myAccoutCardview.setOnClickListener { v: View? ->
            var intent: Intent? = null
            when (position) {
                0 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("my_order_click")
                    intent = Intent(_context, MyOrderActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
                1 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("wallet_click")
                    intent = Intent(_context, MyWalletActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
                2 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("myFavourite_click")
                    intent = Intent(_context, FavouriteActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
                3 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("my_dream_click")
                    intent = Intent(_context, MyDreamActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
                4 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("ledger_payment_click")
                    intent = Intent(_context, LegerPaymentActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
                5 -> {
                    RetailerSDKApp.getInstance().updateAnalytics("target_click")
                    intent = Intent(_context, CustomerSubCategoryTargetActivity::class.java)
                    _context.startActivity(intent)
                    Utils.fadeTransaction(_context as Activity)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mylist.size
    }


    inner class ViewHolder(var mBinding: MyAccountAdapterBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}