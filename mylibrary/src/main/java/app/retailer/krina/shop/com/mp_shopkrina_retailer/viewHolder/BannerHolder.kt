package app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListBannerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class BannerHolder internal constructor(
    var activity: HomeActivity,
    var mBinding: ItemMainListBannerBinding
) :
    RecyclerView.ViewHolder(mBinding.root), View.OnClickListener {

    init {

        mBinding.liMenu.tvCategory.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.clearance)
        mBinding.liMenu.tvFreebie.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.freebies)
        mBinding.liMenu.tvTrade.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.trade_offers)
        mBinding.liMenu.tvTarget.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_target)
        mBinding.liMenu.ivCategories.setOnClickListener(this)
        mBinding.liMenu.ivFreebiesOffer.setOnClickListener(this)
        mBinding.liMenu.ivTradeOffer.setOnClickListener(this)
        mBinding.liMenu.liMyTarget.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivCategories -> {
                RetailerSDKApp.getInstance().updateAnalytics("app_home_clearance_click")
                activity.startActivity(Intent(activity, ClearanceActivity::class.java))
            }

            R.id.ivFreebiesOffer -> {
                RetailerSDKApp.getInstance().updateAnalytics("app_home_freebie_click")
                activity.startActivity(Intent(activity, FreebiesOfferActivity::class.java))
                Utils.fadeTransaction(
                    activity
                )
            }

            R.id.ivTradeOffer -> {
                RetailerSDKApp.getInstance().updateAnalytics("app_home_trade_click")
                activity.pushFragments(TradeOfferFragment.newInstance(), false, true, null)
            }

            R.id.liMyTarget -> {
                RetailerSDKApp.getInstance().updateAnalytics("app_home_target_click")
                activity.startActivity(
                    Intent(
                        activity,
                        CustomerSubCategoryTargetActivity::class.java
                    )
                )
                Utils.fadeTransaction(
                    activity
                )
            }

            else -> {}
        }
    }
}