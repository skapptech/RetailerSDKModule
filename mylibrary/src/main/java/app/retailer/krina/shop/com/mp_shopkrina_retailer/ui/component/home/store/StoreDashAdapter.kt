package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.store

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ApphomeBannerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemBrandDashViewpagerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemDynamicWebviewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemStoreProductBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemStoreSectionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.DynamicHtmlInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.FlashDealsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.ItemsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.JavaScriptInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.StoreDashSliderAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.AppHomeItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeMaxSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.animation.type.IndicatorAnimationType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.SliderAnimations
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.SliderView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.FlashDealOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class StoreDashAdapter(
    private val activity: HomeActivity,
    private var homeDataList: ArrayList<HomeDataModel>,
    private val flashDealsOfferInterface: FlashDealsOfferInterface,
    private val itemsOfferInterface: ItemsOfferInterface,
    private val dynamicHtmlInterface: DynamicHtmlInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CategoryInterface {
    val PAGER_TYPE = 0
    val SECTION_TYPE = 1
    val ITEM_TYPE = 4
    val FLASH_TYPE = 5
    val OTHER_TYPE = 6
    val DYNAMIC_HTML = 7
    val NON_CLICKABLE_BANNER = 8
    val CART = 11
    val SEARCH = 12
    val RECENT = 13
    var subCatId = 0
    fun setHomeAdapter(homeDataList: ArrayList<HomeDataModel>) {
        this.homeDataList = homeDataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PAGER_TYPE -> ViewPagerViewHolder(
                DataBindingUtil.inflate<ItemBrandDashViewpagerBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_brand_dash_viewpager, viewGroup, false
                )
            )

            ITEM_TYPE -> ItemViewHolder(
                DataBindingUtil.inflate<ItemStoreProductBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_store_product, viewGroup, false
                )
            )

            OTHER_TYPE -> ItemViewHolder(
                DataBindingUtil.inflate<ItemStoreProductBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_store_product, viewGroup, false
                )
            )

            NON_CLICKABLE_BANNER -> BannerViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.apphome_banner, viewGroup, false
                )
            )

            DYNAMIC_HTML -> DynamicViewHolder(
                DataBindingUtil.inflate<ItemDynamicWebviewBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_dynamic_webview, viewGroup, false
                )
            )

            SECTION_TYPE -> RecyclerViewHolder(
                DataBindingUtil.inflate<ItemStoreSectionBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_store_section, viewGroup, false
                )
            )

            else -> RecyclerViewHolder(
                DataBindingUtil.inflate<ItemStoreSectionBinding>(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_store_section, viewGroup, false
                )
            )
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val homeDataModel = homeDataList[position]
        when (vh.itemViewType) {
            PAGER_TYPE -> {
                (vh as ViewPagerViewHolder).mBinding.pager.setSliderAdapter(
                    vh.storeDashSliderAdapter
                )
                vh.storeDashSliderAdapter.setData(
                    homeDataList[position]
                )
                vh.mBinding.pager.setIndicatorAnimation(IndicatorAnimationType.WORM)
                vh.mBinding.pager.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                vh.mBinding.pager.startAutoCycle()
                if (position == 0) {
                    vh.mBinding.liImage.visibility = View.VISIBLE
                } else {
                    vh.mBinding.liImage.visibility = View.GONE
                }
            }

            SECTION_TYPE -> {
                var listSizeItem = homeDataModel.appItemsList!!.size
                if (homeDataModel.rowCount == 1 && homeDataModel.columnCount > 2) {
                    val linearlayoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    (vh as RecyclerViewHolder).mBinding.rvItems.layoutManager = linearlayoutManager
                    // App home Hide Title if TileType is Banner
                    if (homeDataModel.isTile) {
                        vh.mBinding.title.text = homeDataModel.sectionName
                        vh.mBinding.rlHeader.visibility = View.VISIBLE
                        vh.mBinding.rlHeaderHz.visibility = View.GONE
                        setViewBackground(vh, position, homeDataModel, true, false)
                        vh.mBinding.liImage.visibility = View.GONE
                        val marginLayoutParams =
                            MarginLayoutParams(vh.mBinding.rvItems.layoutParams)
                        marginLayoutParams.topMargin = 0
                    }
                } else {
                    if (homeDataModel.isTile) {
                        val gridSize: Int
                        gridSize = if (listSizeItem == 2) {
                            2
                        } else {
                            if (homeDataModel.columnCount > 0) homeDataModel.columnCount else 1
                        }
                        val gridLayoutManager = GridLayoutManager(
                            activity, gridSize
                        )
                        (vh as RecyclerViewHolder).mBinding.rvItems.layoutManager =
                            gridLayoutManager
                        vh.mBinding.title.text = homeDataModel.sectionName
                        vh.mBinding.rlHeader.visibility = View.VISIBLE
                        if (homeDataModel.appItemsList!!.size >= 4 && homeDataModel.sectionSubType.equals(
                                "SubCategory",
                                ignoreCase = true
                            )
                        ) {
                            vh.mBinding.llLoadSubCategory.visibility = View.VISIBLE
                            // load more Btn clicked
                            listSizeItem = 4
                            vh.mBinding.llLoadSubCategory.setOnClickListener { v: View? ->
                                val args = Bundle()
                                args.putSerializable("SUB_CAT_MODEL", homeDataModel)
                                args.putString("SectionType", homeDataModel.sectionSubType)
                                args.putString("sectionName", homeDataModel.sectionName)
                                activity.pushFragments(
                                    HomeMaxSubCategoryFragment.newInstance(),
                                    true,
                                    true,
                                    args
                                )
                            }
                        } else {
                            listSizeItem = homeDataModel.appItemsList!!.size
                            vh.mBinding.llLoadSubCategory.visibility = View.GONE
                        }
                        setViewBackground(vh, position, homeDataModel, true, false)
                    } else if (homeDataModel.isBanner) {
                        val linearlayoutManager = LinearLayoutManager(
                            activity, LinearLayoutManager.HORIZONTAL, false
                        )
                        (vh as RecyclerViewHolder).mBinding.rvItems.layoutManager =
                            linearlayoutManager
                        val paramMargin = vh.mBinding.rvItems.layoutParams as MarginLayoutParams
                        if (position == 0) {
                            paramMargin.topMargin = 0
                            vh.mBinding.liImage.visibility = View.VISIBLE
                        } else {
                            paramMargin.topMargin = 10
                            vh.mBinding.liImage.visibility = View.GONE
                        }
                        vh.mBinding.title.text = null
                        vh.mBinding.rlHeader.visibility = View.GONE
                        setViewBackground(vh, position, homeDataModel, false, false)
                    }
                }
                val adapter =
                    StoreSectionAdapter(
                        activity,
                        homeDataModel,
                        listSizeItem,
                        homeDataModel.sectionSubType)
                (vh as RecyclerViewHolder).mBinding.rvItems.adapter = adapter
            }

            ITEM_TYPE -> {
                (vh as ItemViewHolder).mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.titleHz.text = homeDataModel.sectionName
                vh.mBinding.ivHeader.visibility = View.VISIBLE
                if (homeDataModel.isTileSlider) {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    vh.mBinding.rlHeaderHz.visibility = View.VISIBLE
                    vh.mBinding.rlHeader.visibility = View.GONE
                    vh.mBinding.rvItems.setPadding(0, 0, 0, 0)
                } else {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity
                    )
                    vh.mBinding.rlHeader.visibility = View.VISIBLE
                    vh.mBinding.rlHeaderHz.visibility = View.GONE
                    vh.mBinding.rvItems.setPadding(10, 0, 10, 0)
                }
                vh.appHomeItemAdapter.setItemsInAdapter(homeDataModel.isTileSlider)
                vh.mBinding.rvItems.adapter = vh.appHomeItemAdapter
                itemsOfferInterface.itemOffers(
                    vh.mBinding.progressBarCyclic, homeDataModel.sectionID.toString(),
                    vh.mBinding.llLoadItem, vh.appHomeItemAdapter, homeDataModel.isTileSlider
                )
                // load more Btn clicked
                vh.mBinding.llLoadItem.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    MyApplication.getInstance().updateAnalytics(
                        "store_appHome_loadMore_click",
                        analyticPost
                    )
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType)
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                // View all Button click
                vh.mBinding.btnViewAll.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    MyApplication.getInstance().updateAnalytics(
                        "store_appHome_viewAll_click",
                        analyticPost
                    )
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType)
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, homeDataModel, homeDataModel.isTileSlider)
            }

            OTHER_TYPE -> {
                (vh as ItemViewHolder).mBinding.llLoadItem.visibility = View.GONE
                vh.mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.titleHz.text = homeDataModel.sectionName
                vh.mBinding.ivHeader.visibility = View.VISIBLE
                if (homeDataModel.isTileSlider) {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    vh.mBinding.rlHeaderHz.visibility = View.VISIBLE
                    vh.mBinding.rlHeader.visibility = View.GONE
                    vh.mBinding.rvItems.setPadding(0, 0, 0, 0)
                } else {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity
                    )
                    vh.mBinding.rlHeader.visibility = View.VISIBLE
                    vh.mBinding.rlHeaderHz.visibility = View.GONE
                    vh.mBinding.rvItems.setPadding(10, 0, 10, 0)
                }
                vh.appHomeItemAdapter.setItemsInAdapter(homeDataModel.isTileSlider)
                vh.mBinding.rvItems.adapter = vh.appHomeItemAdapter
                // ((ItemViewHolder) vh).mBinding.rvItems.setItemViewCacheSize(100);
                var url = homeDataModel.webViewUrl
                if (url != null && url.length > 0 && url.contains("[CUSTOMERID]")) {
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
                    url = "$url&skip=0&take=4"
                    itemsOfferInterface.getOtherItems(
                        vh, url,
                        position, vh.appHomeItemAdapter, homeDataModel.isTileSlider
                    )
                } else {
                    val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                    vh.itemView.visibility = View.GONE
                    param.height = 0
                    param.width = 0
                    vh.itemView.layoutParams = param
                }
                // load more Btn clicked
                vh.mBinding.llLoadItem.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    MyApplication.getInstance()
                        .updateAnalytics("store_appHome_loadMore_click", analyticPost)
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType)
                    args.putString("URL", homeDataModel.webViewUrl)
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                // View all Button click
                vh.mBinding.btnViewAll.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    MyApplication.getInstance().updateAnalytics(
                        "store_appHome_viewAll_click",
                        analyticPost
                    )
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType)
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, homeDataModel, homeDataModel.isTileSlider)
            }

            NON_CLICKABLE_BANNER -> if (homeDataModel.appItemsList!!.size > 0 && !TextUtils.isNullOrEmpty(
                    homeDataModel.appItemsList!![0].bannerImage
                )
            ) {
                Glide.with(activity)
                    .load(homeDataModel.appItemsList!![0].bannerImage!!.trim { it <= ' ' })
                    .placeholder(R.drawable.logo_grey).into((vh as BannerViewHolder).ivItems)
            }

            FLASH_TYPE -> {
                (vh as RecyclerViewHolder).mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                    activity
                )
                vh.mBinding.progressBarCyclic.visibility = View.VISIBLE
                vh.mBinding.rlHeader.visibility = View.VISIBLE
                flashDealsOfferInterface.flashDealsOffer(
                    vh,
                    homeDataModel.sectionID.toString(),
                    homeDataModel.tileBackgroundImage
                )
                //blink
                val anim = ObjectAnimator.ofInt(
                    vh.mBinding.llLoadItem, "textColor",
                    Color.WHITE, activity.resources.getColor(R.color.colorAccent), Color.WHITE
                )
                anim.setDuration(1500)
                anim.setEvaluator(ArgbEvaluator())
                anim.repeatMode = ValueAnimator.REVERSE
                anim.repeatCount = Animation.INFINITE
                anim.start()
                // load more Button click
                vh.mBinding.llLoadItem.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    MyApplication.getInstance().updateAnalytics(
                        "store_appHome_flash_LoadMore_click",
                        analyticPost
                    )
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString(
                        "FlashDealBackImage",
                        homeDataModel.tileBackgroundImage.toString()
                    )
                    args.putInt("subCategoryId", subCatId)
                    args.putBoolean("isStore", true)
                    activity.pushFragments(FlashDealOfferFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, position, homeDataModel, false, false)
            }

            DYNAMIC_HTML -> {
                (vh as DynamicViewHolder).mBinding.webviewItem.visibility = View.VISIBLE
                val webView = vh.mBinding.webviewItem
                webView.clearCache(true)
                webView.settings.loadWithOverviewMode = true
                webView.settings.useWideViewPort = false
                webView.settings.javaScriptEnabled = true
                webView.settings.pluginState = WebSettings.PluginState.ON
                webView.addJavascriptInterface(
                    JavaScriptInterface(
                        activity
                    ), "Android"
                )
               var url = homeDataModel.webViewUrl
                Log.e("URL", "" + url)
                if (url != null && url.length > 0 && url.contains("[CUSTOMERID]")) {
                    vh.mBinding.webviewItem.visibility = View.VISIBLE
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
                        "" + SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE)
                    )
                    dynamicHtmlInterface.loadDynamicHtml(vh, position, url)
                } else {
                    vh.mBinding.webviewItem.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (homeDataList[position].sectionSubType) {
            "Slider" -> PAGER_TYPE
            "Base Category", "Brand", "Category", "SubCategory" -> SECTION_TYPE
            "Item" -> ITEM_TYPE
            "Flash Deal" -> FLASH_TYPE
            "Other" -> if (homeDataList[position].isBanner) NON_CLICKABLE_BANNER else OTHER_TYPE
            "DynamicHtml" -> DYNAMIC_HTML
            "cart" -> CART
            "search" -> SEARCH
            "recent" -> RECENT
            else -> 9
        }
    }

    override fun getItemCount(): Int {
        return homeDataList.size
    }

    inner class RecyclerViewHolder internal constructor(var mBinding: ItemStoreSectionBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ), View.OnClickListener {
        init {
            mBinding.ivFreebiesOffer.setOnClickListener(this)
            mBinding.ivCategoties.setOnClickListener(this)
            mBinding.llMyTarget.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.iv_categoties -> {
                    MyApplication.getInstance().updateAnalytics("store_category_click")
                    val args = Bundle()
                    args.putString("ItemId", "")
                    args.putInt("BaseCategoryId", 0)
                    args.putString("SectionType", "")
                    args.putInt("CATEGORY_ID", subCatId)
                    args.putInt("SUB_CAT_ID", subCatId)
                    args.putInt("SUB_SUB_CAT_ID", subCatId)
                    args.putBoolean("HOME_FLAG", false)
                    activity.pushFragments(HomeSubCategoryFragment.newInstance(), false, true, args)
                }

                R.id.iv_freebies_offer -> {
                    MyApplication.getInstance().updateAnalytics("store_freebie_click")
                    activity.startActivity(
                        Intent(
                            activity,
                            StoreFreebiesActivity::class.java
                        ).putExtra("subCatId", subCatId)
                    )
                    Utils.fadeTransaction(
                        activity
                    )
                }

                R.id.ll_My_target -> {
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

    inner class ItemViewHolder internal constructor(var mBinding: ItemStoreProductBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var appHomeItemAdapter: AppHomeItemAdapter

        init {
            appHomeItemAdapter = AppHomeItemAdapter(activity)
        }
    }

    inner class BannerViewHolder internal constructor(var mBinding: ApphomeBannerBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivItems: ImageView

        init {
            ivItems = mBinding.ivItem
        }
    }

    inner class DynamicViewHolder internal constructor(var mBinding: ItemDynamicWebviewBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )

    inner class ViewPagerViewHolder internal constructor(var mBinding: ItemBrandDashViewpagerBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ), View.OnClickListener {
        var pager: SliderView
        var storeDashSliderAdapter: StoreDashSliderAdapter

        init {
            pager = mBinding.pager
            storeDashSliderAdapter = StoreDashSliderAdapter(activity)
            mBinding.ivFreebiesOffer.setOnClickListener(this)
            mBinding.ivCategoties.setOnClickListener(this)
            mBinding.llMyTarget.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.iv_categoties -> {
                    MyApplication.getInstance().updateAnalytics("store_category_click")
                    val args = Bundle()
                    args.putString("ItemId", "")
                    args.putString("BaseCategoryId", "")
                    args.putString("SectionType", "")
                    args.putInt("CATEGORY_ID", 0)
                    args.putInt("SUB_CAT_ID", subCatId)
                    args.putInt("SUB_SUB_CAT_ID", 0)
                    args.putBoolean("HOME_FLAG", false)
                    activity.pushFragments(HomeSubCategoryFragment.newInstance(), false, true, args)
                }

                R.id.iv_freebies_offer -> {
                    MyApplication.getInstance().updateAnalytics("store_freebie_click")
                    activity.startActivity(
                        Intent(
                            activity,
                            StoreFreebiesActivity::class.java
                        ).putExtra("subCatId", subCatId)
                    )
                    Utils.fadeTransaction(
                        activity
                    )
                }

                R.id.ll_My_target -> {
                    MyApplication.getInstance().updateAnalytics("store_target_click")
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

    private fun setViewBackground(
        vh: RecyclerViewHolder,
        i: Int,
        homeDataModel: HomeDataModel,
        background: Boolean,
        isSlider: Boolean
    ) {
        try {
            val params = vh.mBinding.rlHeader.layoutParams
            val param = vh.mBinding.rlHeader.layoutParams as MarginLayoutParams
            if (background) {
                if (homeDataList[i].hasBackgroundColor) {
                    if (homeDataModel.tileBackgroundColor != null) {
                        vh.mBinding.rvItems.setBackgroundColor(Color.parseColor(homeDataModel.tileBackgroundColor))
                    }
                } else if (homeDataList[i].tileBackgroundImage != null && homeDataList[i].tileBackgroundImage != "" && !homeDataList[i].tileBackgroundImage!!.isEmpty()) {
                    Picasso.get().load(homeDataList[i].tileBackgroundImage!!.replace(" ".toRegex(), "%20"))
                        .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                            vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                            e.printStackTrace()
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    })
                }
                if (homeDataModel.tileHeaderBackgroundColor != null) {
                    vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(homeDataModel.tileHeaderBackgroundColor))
                    vh.mBinding.ivHeader.setImageDrawable(null)
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = 100
                    param.bottomMargin = 0
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    vh.mBinding.rlHeader.setBackgroundColor(0)
                    param.bottomMargin = 0
                }
                if (homeDataList[i].tileHeaderBackgroundImage != null && homeDataList[i].tileHeaderBackgroundImage != "" && !homeDataList[i].tileHeaderBackgroundImage!!.isEmpty()) {
                    Picasso.get().load(
                        homeDataList[i].tileHeaderBackgroundImage!!.replace(
                            " ".toRegex(),
                            "%20"
                        )
                    ).into(vh.mBinding.ivHeader)
                } else {
                    vh.mBinding.ivHeader.setImageDrawable(null)
                }
                if (homeDataModel.headerTextColor != null) {
                    vh.mBinding.title.setTextColor(Color.parseColor(homeDataModel.headerTextColor))
                } else {
                    vh.mBinding.title.setTextColor(activity.resources.getColor(R.color.White))
                }
            } else {
                if (homeDataModel.tileHeaderBackgroundColor != null) {
                    vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(homeDataModel.tileHeaderBackgroundColor))
                    vh.mBinding.ivHeader.setImageDrawable(null)
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = 100
                    param.bottomMargin = 0
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    vh.mBinding.rlHeader.setBackgroundColor(0)
                    param.bottomMargin = 0
                }
                if (homeDataList[i].tileHeaderBackgroundImage != null && homeDataList[i].tileHeaderBackgroundImage != "" && !homeDataList[i].tileHeaderBackgroundImage!!.isEmpty()) {
                    Picasso.get()
                        .load(homeDataList[i].tileHeaderBackgroundImage!!.trim { it <= ' ' })
                        .into(vh.mBinding.ivHeader)
                } else {
                    vh.mBinding.ivHeader.setImageDrawable(null)
                }
                if (homeDataModel.headerTextColor != null) {
                    vh.mBinding.title.setTextColor(Color.parseColor(homeDataModel.headerTextColor))
                } else {
                    vh.mBinding.title.setTextColor(activity.resources.getColor(R.color.White))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewBackground(vh: ItemViewHolder, model: HomeDataModel, isSlider: Boolean) {
        try {
            val params = vh.mBinding.rlHeader.layoutParams
            val param = vh.mBinding.rlHeader.layoutParams as MarginLayoutParams
            if (isSlider) {
                if (model.hasBackgroundColor) {
                    if (model.tileBackgroundColor != null) {
                        vh.mBinding.rlItems.setBackgroundColor(Color.parseColor(model.tileBackgroundColor))
                        vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                    } else {
                        vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                    }
                } else if (model.tileBackgroundImage != null && model.tileBackgroundImage != "" && !model.tileBackgroundImage!!.isEmpty()) {
                    Picasso.get().load(model.tileBackgroundImage!!.replace(" ".toRegex(), "%20"))
                        .into(object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                                vh.mBinding.rlItems.background = BitmapDrawable(bitmap)
                                vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                            }

                            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {}
                            override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
                        })
                } else {
                    vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                }
            } else {
                vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                if (model.hasBackgroundColor) {
                    if (model.tileBackgroundColor != null) {
                        vh.mBinding.rvItems.setBackgroundColor(Color.parseColor(model.tileBackgroundColor))
                    }
                } else if (model.tileBackgroundImage != null && model.tileBackgroundImage != "" && !model.tileBackgroundImage!!.isEmpty()) {
                    Picasso.get().load(model.tileBackgroundImage!!.replace(" ".toRegex(), "%20"))
                        .into(object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                                vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                            }

                            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {}
                            override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
                        })
                }
            }
            if (model.tileHeaderBackgroundColor != null) {
                vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(model.tileHeaderBackgroundColor))
                vh.mBinding.ivHeader.setImageDrawable(null)
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = 100
                param.bottomMargin = 0
            } else {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                vh.mBinding.rlHeader.setBackgroundColor(0)
                param.bottomMargin = 0
            }
            if (model.tileHeaderBackgroundImage != null && model.tileHeaderBackgroundImage != "" && !model.tileHeaderBackgroundImage!!.isEmpty()) {
                Picasso.get().load(model.tileHeaderBackgroundImage!!.replace(" ".toRegex(), "%20"))
                    .into(vh.mBinding.ivHeader)
            } else {
                vh.mBinding.ivHeader.setImageDrawable(null)
            }
            if (model.headerTextColor != null) {
                vh.mBinding.title.setTextColor(Color.parseColor(model.headerTextColor))
                vh.mBinding.titleHz.setTextColor(Color.parseColor(model.headerTextColor))
                vh.mBinding.btnViewAll.setTextColor(Color.parseColor(model.headerTextColor))
                vh.mBinding.next.setColorFilter(Color.parseColor(model.headerTextColor))
            } else {
                vh.mBinding.title.setTextColor(activity.resources.getColor(R.color.White))
                vh.mBinding.titleHz.setTextColor(activity.resources.getColor(R.color.White))
                vh.mBinding.btnViewAll.setTextColor(activity.resources.getColor(R.color.White))
                vh.mBinding.next.setColorFilter(activity.resources.getColor(R.color.black))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun CategoryClicked(position: Int) {
        val args = Bundle()
        activity.pushFragments(SubCategoryFragment.newInstance(), false, false, args)
    }

    private inner class GetTarget(private val vh: RecyclerViewHolder) {
        var target: Target? = null
            private set
        var target1: Target? = null
            private set

        operator fun invoke(): GetTarget {
            target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                    vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                }

                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                    vh.mBinding.rvItems.setBackgroundColor(activity.resources.getColor(R.color.white))
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                    vh.mBinding.rvItems.background = placeHolderDrawable
                }
            }
            target1 = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                    vh.mBinding.ivHeader.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                    vh.mBinding.ivHeader.setBackgroundColor(activity.resources.getColor(R.color.white))
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                    vh.mBinding.ivHeader.setImageDrawable(placeHolderDrawable)
                }
            }
            return this
        }
    }
}