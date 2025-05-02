package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ViewPagerItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.SliderViewAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso

class SliderAdapterExample(private val activity: HomeActivity) :
    SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>() {

    private var homeDataModel: HomeDataModel? = null
    private var list: ArrayList<HomeDataModel.AppItemsList>? = null

    fun setData(model: HomeDataModel?) {
        this.homeDataModel = model
        this.list = model?.appItemsList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(
            ViewPagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val itemView = viewHolder.mBinding.root
        try {
            val model = list!![position]
            if (!TextUtils.isNullOrEmpty(
                    model.bannerImage
                )
            ) {
                Picasso.get().load(model.bannerImage)
                    .placeholder(R.drawable.logo_grey).into(viewHolder.mBinding.ivSlider)
            } else {
                viewHolder.mBinding.ivSlider.setImageResource(R.drawable.logo_grey)
            }
            itemView.setOnClickListener {
                // analytics data
                val analyticPost = AnalyticPost()
                analyticPost.sectionId = homeDataModel?.sectionID
                analyticPost.sectionSubType = homeDataModel?.sectionSubType
                analyticPost.sectionName = homeDataModel?.sectionName
                analyticPost.url = homeDataModel?.webViewUrl

                if (homeDataModel!!.viewType != null && homeDataModel!!.viewType!!.isNotEmpty() && homeDataModel!!.viewType.equals(
                        "webView",
                        ignoreCase = true
                    )
                ) {
                    if (homeDataModel!!.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                        activity.startActivity(Intent(activity, TradeActivity::class.java))
                        Utils.leftTransaction(
                            activity
                        )
                    } else if (homeDataModel!!.webViewUrl!!.startsWith("https://skdirectbuyer.shopkirana.in")) {
                        activity.startActivity(Intent(activity, TradeActivity::class.java))
                        Utils.leftTransaction(
                            activity
                        )
                    } else {
                        activity.startActivity(
                            Intent(
                                activity,
                                WebViewActivity::class.java
                            ).putExtra("url", homeDataModel!!.webViewUrl)
                        )
                    }
                    // update analytics
                    RetailerSDKApp.getInstance().updateAnalytics(
                        "app_home_slider_click",
                        analyticPost
                    )
                } else {
                    if (homeDataModel!!.sectionSubType.equals("Slider", ignoreCase = true)) {
                        if (model.redirectionType != null && model.redirectionType.equals(
                                "Other",
                                ignoreCase = true
                            )
                        ) {
                            if (model.bannerActivity != null)
                                callActivities(
                                    model.bannerActivity,
                                    model.redirectionUrl
                                )
                        } else {
                            analyticPost.baseCatId =
                                "" + model.baseCategoryId
                            analyticPost.categoryId = model.categoryId
                            analyticPost.subCatId = model.subCategoryId
                            analyticPost.subSubCatId =
                                model.subsubCategoryId
                            // update analytics
                            RetailerSDKApp.getInstance().updateAnalytics(
                                "app_home_slider_click",
                                analyticPost
                            )
                            val args = Bundle()
                            args.putString(
                                "BRAND_NAME",
                                model.redirectionType
                            )
                            args.putInt(
                                "BaseCategoryId",
                                model.baseCategoryId
                            )
                            args.putInt("CATEGORY_ID", model.categoryId)
                            args.putInt("SUB_CAT_ID", model.subCategoryId)
                            args.putInt(
                                "SUB_SUB_CAT_ID",
                                model.subsubCategoryId
                            )
                            args.putBoolean("HOME_FLAG", true)
                            args.putString("SectionType", homeDataModel!!.sectionSubType)
                            if (model.redirectionID != 0) {
                                args.putString(
                                    "ItemId",
                                    model.redirectionID.toString() + ""
                                )
                                if (model.redirectionID != 0) {
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class SliderAdapterVH(var mBinding: ViewPagerItemBinding) : ViewHolder(
        mBinding.root
    )

    private fun callActivities(type: String, url: String?) {
        var i: Intent? = null
        if (type.equals("games", ignoreCase = true)) {
            i = Intent(activity, GamesListActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("target", ignoreCase = true)) {
            i = Intent(activity, CustomerSubCategoryTargetActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("prime", ignoreCase = true)) {
            i = Intent(activity, MembershipPlanActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("shoppingcart", ignoreCase = true)) {
            i = Intent(activity, ShoppingCartActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("wallet", ignoreCase = true)) {
            i = Intent(activity, MyWalletActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("category", ignoreCase = true)) {
            activity.runOnUiThread {
                activity.pushFragments(
                    HomeCategoryFragment(),
                    false,
                    true,
                    null
                )
            }
        } else if (type.equals("tradeoffer", ignoreCase = true)) {
            activity.runOnUiThread {
                activity.pushFragments(
                    TradeOfferFragment.newInstance(),
                    false,
                    true,
                    null
                )
            }
        } else if (type.equals("allbrands", ignoreCase = true)) {
            activity.runOnUiThread {
                activity.pushFragments(
                    AllBrandFragItemList.newInstance(),
                    false,
                    true,
                    null
                )
            }
        } else if (type.equals("freebies", ignoreCase = true)) {
            activity.startActivity(Intent(activity, FreebiesOfferActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("myorder", ignoreCase = true)) {
            activity.startActivity(Intent(activity, MyOrderActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type == "direct") {
            activity.startActivity(Intent(activity, TradeActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("clearance", ignoreCase = true)) {
            activity.startActivity(Intent(activity, ClearanceActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type == "ExternalURL") {
            try {
                val uri =
                    Uri.parse(url) // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Utils.fadeTransaction(activity)
        }
    }
}