package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.Grid3Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.HorizontalTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.OtherTypeOneBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.TileTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class HomeSectionAdapter(
    private val activity: HomeActivity,
    private val dataDetailsList: HomeDataModel?,
    private val listSizeItem: Int,
    private val mSectionSubType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val SCROLLABLE_BANNER = 0
    val SCROLLABLE_TILE = 1
    val OTHER_TYPE_2 = 2
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SCROLLABLE_BANNER -> RecyclerViewHorizontalHolder(
                HorizontalTypeBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            SCROLLABLE_TILE -> RecyclerViewTileTypeHolder(
                TileTypeBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            OTHER_TYPE_2 -> RecyclerView3gridTypeOneHolder(
                Grid3Binding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            else -> RecyclerViewOtherTypeOneHolder(
                OtherTypeOneBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, i: Int) {
        val homeDataDetailsModel = dataDetailsList!!.appItemsList!![i]
        // Banner Image done by harshita
        if (vh.itemViewType == SCROLLABLE_BANNER) {
            if (!TextUtils.isNullOrEmpty(
                    dataDetailsList.appItemsList!![i].bannerImage
                )
            ) {
                Glide.with(activity)
                    .load(dataDetailsList.appItemsList!![i].bannerImage!!.trim { it <= ' ' })
                    .placeholder(R.drawable.logo_grey_wide)
                    .into((vh as RecyclerViewHorizontalHolder).mBinding.ivBanner)
            } else {
                (vh as RecyclerViewHorizontalHolder).mBinding.ivBanner.setImageResource(R.drawable.logo_grey_wide)
            }
            vh.mBinding.catLinearLayout.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        } else if (vh.itemViewType == SCROLLABLE_TILE) {
            (vh as RecyclerViewTileTypeHolder).mBinding.tvTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity)
                    .load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(R.drawable.logo_grey)
                    .into(vh.mBinding.ivTile)
            } else {
                vh.mBinding.ivTile.setImageResource(R.drawable.logo_grey)
            }
            vh.mBinding.liTile.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.liTile,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
            setScrollableBackGroundImage(vh, homeDataDetailsModel)
        } else if (vh.itemViewType == OTHER_TYPE_2) {
            setBackGroundImage3Gird(vh as RecyclerView3gridTypeOneHolder, homeDataDetailsModel)
            vh.mBinding.tvTextTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity).load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(R.drawable.logo_grey)
                    .into(vh.mBinding.ivItemImage)
            } else {
                vh.mBinding.ivItemImage.setImageResource(R.drawable.logo_grey)
            }
            vh.mBinding.liTile.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.liTile,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        } else {
            setBackGroundImage(vh as RecyclerViewOtherTypeOneHolder, homeDataDetailsModel)
            vh.mBinding.tvTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity)
                    .load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(R.drawable.logo_grey)
                    .into(vh.mBinding.ivItemImage)
            } else {
                vh.mBinding.ivItemImage.setImageResource(R.drawable.logo_grey)
            }
            vh.mBinding.catLinearLayout.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val horizontal = dataDetailsList!!.rowCount == 1 && dataDetailsList!!.columnCount > 2
        return if (horizontal && !dataDetailsList!!.isTile) {
            SCROLLABLE_BANNER
        } else if (horizontal && dataDetailsList!!.isTile) {
            SCROLLABLE_TILE
        } else if (dataDetailsList!!.rowCount > 1 && dataDetailsList!!.columnCount > 2) {
            OTHER_TYPE_2
        } else {
            9
        }
    }

    override fun getItemCount(): Int {
        return if (dataDetailsList == null) 0 else listSizeItem
    }

    private fun setScrollableBackGroundImage(
        vh: RecyclerViewTileTypeHolder,
        homeDataDetailsModel: AppItemsList
    ) {
        if (homeDataDetailsModel.tileSectionBackgroundImage != null && homeDataDetailsModel.tileSectionBackgroundImage != "" && !homeDataDetailsModel.tileSectionBackgroundImage!!.isEmpty()) {
            Picasso.get().load(homeDataDetailsModel.tileSectionBackgroundImage!!.trim { it <= ' ' })
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.liTile.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else {
            vh.mBinding.liTile.setBackgroundResource(R.drawable.rectangle_transperent_outline)
        }
    }

    private fun setBackGroundImage(
        vh: RecyclerViewOtherTypeOneHolder,
        homeDataDetailsModel: AppItemsList
    ) {
        if (dataDetailsList!!.sectionBackgroundImage != null && !dataDetailsList.sectionBackgroundImage!!.isEmpty()) {
            Picasso.get().load(dataDetailsList.sectionBackgroundImage.trim { it <= ' ' })
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.catLinearLayout.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else if (homeDataDetailsModel.tileSectionBackgroundImage != null && !homeDataDetailsModel.tileSectionBackgroundImage!!.isEmpty()) {
            Picasso.get().load(homeDataDetailsModel.tileSectionBackgroundImage!!.trim { it <= ' ' })
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.catLinearLayout.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else {
            vh.mBinding.catLinearLayout.setBackgroundResource(R.drawable.rectangle_transperent_outline)
        }
    }

    private fun setBackGroundImage3Gird(
        vh: RecyclerView3gridTypeOneHolder,
        homeDataDetailsModel: AppItemsList
    ) {
        if (dataDetailsList!!.sectionBackgroundImage != null && !dataDetailsList.sectionBackgroundImage!!.isEmpty()) {
            Picasso.get()
                .load(dataDetailsList.sectionBackgroundImage.replace(" ".toRegex(), "%20"))
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.liTile.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else if (homeDataDetailsModel.tileSectionBackgroundImage != null && !homeDataDetailsModel.tileSectionBackgroundImage!!.isEmpty()) {
            Picasso.get()
                .load(
                    homeDataDetailsModel.tileSectionBackgroundImage!!.replace(
                        " ".toRegex(),
                        "%20"
                    )
                )
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.liTile.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else {
            vh.mBinding.liTile.setBackgroundResource(R.drawable.rectangle_transperent_outline)
        }
    }

    private fun clickActionPerform(
        vh: LinearLayout,
        actionType: String,
        appItemModel: AppItemsList
    ) {
        try {
            vh.isEnabled = false
            handler.postDelayed(object : Runnable {
                override fun run() {
                    // Do something after 100ms
                    vh.isEnabled = true
                    handler.postDelayed(this, 500)
                    handler.removeCallbacks(this)
                    var args = Bundle()
                    args.putString("ItemId", appItemModel.redirectionID.toString())
                    args.putInt("BaseCategoryId", appItemModel.baseCategoryId)
                    args.putString("SectionType", mSectionSubType)
                    args.putInt("CATEGORY_ID", appItemModel.categoryId)
                    args.putInt("SUB_CAT_ID", appItemModel.subCategoryId)
                    args.putInt("SUB_SUB_CAT_ID", appItemModel.subsubCategoryId)
                    args.putBoolean("HOME_FLAG", true)
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = dataDetailsList!!.sectionID
                    analyticPost.sectionSubType = dataDetailsList.sectionSubType
                    analyticPost.sectionName = dataDetailsList.sectionName
                    analyticPost.baseCatId = appItemModel.baseCategoryId.toString()
                    analyticPost.categoryId = appItemModel.categoryId
                    analyticPost.subCatId = appItemModel.subCategoryId
                    analyticPost.subSubCatId = appItemModel.subsubCategoryId
                    var url = dataDetailsList.webViewUrl
                    if (url != null && url.length > 0) {
                        url = url.replace(
                            "[CUSTOMERID]",
                            "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
                        )
                        url = url.replace(
                            "[SKCODE]",
                            "" + SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE)
                        )
                        url = url.replace(
                            "[WAREHOUSEID]",
                            "" + SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
                        )
                        url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(activity))
                        url = url.replace(
                            "[MOBILE]",
                            "" + SharePrefs.getInstance(activity)
                                .getString(SharePrefs.MOBILE_NUMBER)
                        )
                        dataDetailsList.webViewUrl = url
                    }
                    when (actionType) {
                        "Base Category" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                args.putString("cetegoryTittle", appItemModel.tileName)
                                activity.pushFragments(
                                    SubCategoryFragment.newInstance(),
                                    false,
                                    true,
                                    args
                                )
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_basCat_click", analyticPost)
                        }

                        "Brand" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.startsWith("vAtm") || dataDetailsList.webViewUrl!!.startsWith(
                                        "vatm"
                                    )
                                ) {
                                    activity.callVAtmApi()
                                } else if (dataDetailsList.webViewUrl!!.contains("Udhar/GenerateLead")) {
                                    activity.callLeadApi(dataDetailsList.webViewUrl!!)
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
//                                    args.putBoolean("isStore", true);
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_brand_click", analyticPost)
                        }

                        "Category" -> {
                            args.putBoolean("HOME_FLAG", false)
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (appItemModel.redirectionID != 0) {
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_category_click", analyticPost)
                        }

                        "SubCategory" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.startsWith("ShowStoreFlashDeal")) {
                                    val id = dataDetailsList.appItemsList!![0].redirectionID
                                    args = Bundle()
                                    args.putString("SECTION_ID", "-1")
                                    args.putInt("subCategoryId", id)
                                    args.putBoolean("isStore", true)
                                    activity.pushFragments(
                                        FlashDealOfferFragment.newInstance(),
                                        true,
                                        true,
                                        args
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
                                    args.putString("ITEM_IMAGE", appItemModel.tileImage)
                                    activity.pushFragments(
                                        HomeSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_subCat_click", analyticPost)
                        }

                        "Other" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                callActivities(
                                    appItemModel.bannerActivity,
                                    appItemModel.redirectionUrl
                                )
                            }
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
                                    // args.putString("BRAND_NAME", "Banner Brand");
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_default_click", analyticPost)
                        }

                        else -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    ) activity.startActivity(
                                        Intent(
                                            activity, FeedActivity::class.java
                                        )
                                    ) else activity.startActivity(
                                        Intent(
                                            activity, TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")) {
                                    activity.callScaleUpApiUsingUrl(url)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(),
                                        false,
                                        true,
                                        args
                                    )
                                }
                            }
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_default_click", analyticPost)
                        }
                    }
                }
            }, 300)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callActivities(type: String?, url: String?) {
        var i: Intent? = null
        if (type.equals("games", ignoreCase = true)) {
            activity.startActivity(Intent(activity, GamesListActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("target", ignoreCase = true)) {
            activity.startActivity(Intent(activity, CustomerSubCategoryTargetActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("prime", ignoreCase = true)) {
            activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("shoppingcart", ignoreCase = true)) {
            activity.startActivity(Intent(activity, ShoppingCartActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("wallet", ignoreCase = true)) {
            i = Intent(activity, MyWalletActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (type.equals("category", ignoreCase = true)) {
            activity.pushFragments(
                HomeCategoryFragment(),
                false,
                true,
                null
            )
        } else if (type.equals("tradeoffer", ignoreCase = true)) {
            activity.pushFragments(
                TradeOfferFragment.newInstance(),
                false,
                true,
                null
            )
        } else if (type.equals("allbrands", ignoreCase = true)) {
            activity.pushFragments(
                AllBrandFragItemList.newInstance(),
                false,
                true,
                null
            )
        } else if (type.equals("freebies", ignoreCase = true)) {
            activity.startActivity(Intent(activity, FreebiesOfferActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("myorder", ignoreCase = true)) {
            activity.startActivity(Intent(activity, MyOrderActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("direct", ignoreCase = true)) {
            if (EndPointPref.getInstance(activity)
                    .getBoolean(EndPointPref.showNewSocial)
            ) activity.startActivity(
                Intent(
                    activity, FeedActivity::class.java
                )
            ) else activity.startActivity(Intent(activity, TradeActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("Clearance", ignoreCase = true)) {
            activity.startActivity(Intent(activity, ClearanceActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (type.equals("ExternalURL", ignoreCase = true)) {
            try {
                val uri = Uri.parse(url) // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Utils.fadeTransaction(activity)
        }
    }

    inner class RecyclerViewHorizontalHolder internal constructor(var mBinding: HorizontalTypeBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )

    inner class RecyclerViewTileTypeHolder internal constructor(var mBinding: TileTypeBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )

    inner class RecyclerViewOtherTypeOneHolder internal constructor(var mBinding: OtherTypeOneBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )

    inner class RecyclerView3gridTypeOneHolder internal constructor(var mBinding: Grid3Binding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}