package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemDynamicWebviewBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainBucketGamePagerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListBannerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListCartBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListProductBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListSearchBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListSectionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListStoreBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListViewpagerBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BucketGameInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.DynamicHtmlInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.FlashDealsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.ItemsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.StoreApiInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.AppHomeSearchAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SliderAdapterExample
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.AppHomeItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeMaxSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.animation.type.IndicatorAnimationType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.SliderAnimations
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem.SearchItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.BannerHolder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.CartHolder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.DynamicViewHolder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.SearchHolder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.StoreHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import java.text.DecimalFormat

class HomeAdapter(
    val activity: HomeActivity,
    var homeDataList: ArrayList<HomeDataModel>,
    // interface
    private val flashDealsOfferInterface: FlashDealsOfferInterface,
    private val itemsOfferInterface: ItemsOfferInterface,
    private val dynamicHtmlInterface: DynamicHtmlInterface,
    private val storeApiInterface: StoreApiInterface,
    private val bucketGameInterface: BucketGameInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    val PAGER_TYPE = 1
    val SECTION_TYPE = 2
    val BANNER = 3
    val ITEM_TYPE = 4
    val FLASH_TYPE = 5
    val OTHER_TYPE = 6
    val DYNAMIC_HTML = 7
    val NON_CLICKABLE_BANNER = 8
    val STORE = 9
    val CART = 11
    val SEARCH = 12
    private val RECENT = 13
    val BUCKET_GAME_TYPE = 14
    val FREEBIES = 15


    fun setHomeAdapter(homeDataList: ArrayList<HomeDataModel>) {
        this.homeDataList = homeDataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PAGER_TYPE -> ViewPagerViewHolder(
                ItemMainListViewpagerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            FLASH_TYPE -> RecyclerViewHolder(
                ItemMainListSectionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            SECTION_TYPE -> RecyclerViewHolder(
                ItemMainListSectionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            BANNER, NON_CLICKABLE_BANNER -> BannerHolder(
                activity,
                ItemMainListBannerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            ITEM_TYPE, OTHER_TYPE -> ItemViewHolder(
                ItemMainListProductBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            DYNAMIC_HTML -> DynamicViewHolder(
                ItemDynamicWebviewBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            CART -> CartHolder(
                ItemMainListCartBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            SEARCH -> SearchHolder(
                ItemMainListSearchBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            STORE -> StoreHolder(
                ItemMainListStoreBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            BUCKET_GAME_TYPE -> BucketGameViewPagerViewHolder(
                ItemMainBucketGamePagerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            else -> RecyclerViewHolder(
                ItemMainListSectionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val homeDataModel = homeDataList[position]
        when (vh.itemViewType) {
            PAGER_TYPE -> {
                (vh as ViewPagerViewHolder).mBinding.liMenu.tvCategory.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.clearance)
                vh.mBinding.liMenu.tvFreebie.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.freebies)
                vh.mBinding.liMenu.tvTrade.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.trade_offers)
                vh.mBinding.liMenu.tvTarget.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_target)
                vh.mBinding.pager.setSliderAdapter(vh.sliderAdapterExample)
                vh.sliderAdapterExample.setData(
                    homeDataList[position]
                )
                vh.mBinding.pager.setIndicatorAnimation(IndicatorAnimationType.WORM)
                vh.mBinding.pager.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                vh.mBinding.pager.startAutoCycle()
                if (position == 0) {
                    vh.mBinding.liMenu.liImage.visibility = View.VISIBLE
                } else {
                    vh.mBinding.liMenu.liImage.visibility = View.GONE
                }
            }

            BANNER -> {
                if (homeDataModel.appItemsList != null && homeDataModel.appItemsList!!.size > 0 && !TextUtils.isNullOrEmpty(
                        homeDataModel.appItemsList!![0].bannerImage
                    )
                ) {
                    Glide.with(activity).load(homeDataModel.appItemsList!![0].bannerImage?.trim())
                        .placeholder(R.drawable.logo_grey)
                        .into((vh as BannerHolder).mBinding.ivBanner)
                } else {
                    (vh as BannerHolder).mBinding.ivBanner.setImageResource(R.drawable.logo_grey)
                }
                vh.mBinding.ivBanner.setOnClickListener {
                    clickActionPerform(
                        vh.mBinding.ivBanner,
                        homeDataModel.sectionSubType,
                        homeDataModel,
                        homeDataModel.appItemsList!![0]
                    )
                }
                if (position == 0) {
                    vh.mBinding.liMenu.liImage.visibility = View.VISIBLE
                } else {
                    vh.mBinding.liMenu.liImage.visibility = View.GONE
                }
            }

            NON_CLICKABLE_BANNER -> {
                if (homeDataModel.appItemsList != null && homeDataModel.appItemsList!!.size > 0 && !TextUtils.isNullOrEmpty(
                        homeDataModel.appItemsList!![0].bannerImage
                    )
                ) {
                    Picasso.get().load(homeDataModel.appItemsList!![0].bannerImage)
                        .placeholder(R.drawable.logo_grey).error(R.drawable.logo_grey)
                        .into((vh as BannerHolder).mBinding.ivBanner)
                } else {
                    (vh as BannerHolder).mBinding.ivBanner.setImageResource(R.drawable.logo_grey)
                }
                vh.mBinding.ivBanner.setOnClickListener {
                    callActivities(
                        homeDataModel.appItemsList!![0].bannerActivity ?: "",
                        homeDataModel.appItemsList!![0].redirectionUrl ?: ""
                    )
                }

            }

            SECTION_TYPE -> {
                (vh as RecyclerViewHolder).mBinding.liMenu.tvCategory.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.clearance)
                vh.mBinding.liMenu.tvFreebie.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.freebies)
                vh.mBinding.liMenu.tvTrade.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.trade_offers)
                vh.mBinding.liMenu.tvTarget.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_target)
                var listSizeItem = homeDataModel.appItemsList?.size
                vh.mBinding.btnLoadMore.visibility = View.GONE
                vh.mBinding.btnLoadMore.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.load_more)
                if (homeDataModel.headerTextSize > 2) {
                    vh.mBinding.title.textSize = homeDataModel.headerTextSize.toFloat()
                } else {
                    vh.mBinding.title.textSize = 18f
                }
                if (homeDataModel.isTileSlider) {
                    val linearlayoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    vh.mBinding.rvItems.layoutManager = linearlayoutManager
                    // App home Hide Title if TileType is Banner
                    if (homeDataModel.isTile) {
                        vh.mBinding.title.text = homeDataModel.sectionName
                        vh.mBinding.rlHeader.visibility = View.VISIBLE
                        vh.mBinding.rlHeaderHz.visibility = View.GONE
                        vh.mBinding.liMenu.liImage.visibility = View.GONE
                    }
                } else {
                    if (homeDataModel.isTile) {
                        val gridLayoutManager = GridLayoutManager(
                            activity,
                            if (homeDataModel.columnCount > 0) homeDataModel.columnCount else 1
                        )
                        vh.mBinding.rvItems.layoutManager = gridLayoutManager
                        vh.mBinding.title.text = homeDataModel.sectionName
                        vh.mBinding.rlHeader.visibility = View.VISIBLE
                        if (homeDataModel.appItemsList!!.size >= 4 && homeDataModel.sectionSubType.equals(
                                "SubCategory", ignoreCase = true
                            )
                        ) {
                            vh.mBinding.btnLoadMore.visibility = View.VISIBLE
                            // load more Btn clicked
                            listSizeItem = 4
                            vh.mBinding.btnLoadMore.setOnClickListener {
                                val args = Bundle()
                                args.putSerializable("SUB_CAT_MODEL", homeDataModel)
                                args.putString("SectionType", homeDataModel.sectionSubType)
                                args.putString("sectionName", homeDataModel.sectionName)
                                activity.pushFragments(
                                    HomeMaxSubCategoryFragment.newInstance(), true, true, args
                                )
                            }
                        } else {
                            listSizeItem = homeDataModel.appItemsList!!.size
                            vh.mBinding.btnLoadMore.visibility = View.GONE
                        }
                    } else if (homeDataModel.isBanner) {
                        val linearlayoutManager = LinearLayoutManager(
                            activity, LinearLayoutManager.HORIZONTAL, false
                        )
                        vh.mBinding.rvItems.layoutManager = linearlayoutManager
                        if (position == 0) {
                            vh.mBinding.liMenu.liImage.visibility = View.VISIBLE
                        } else {
                            vh.mBinding.liMenu.liImage.visibility = View.GONE
                        }
                        vh.mBinding.title.text = null
                        vh.mBinding.rlHeader.visibility = View.GONE
                    }
                }

                setViewBackground(vh, homeDataModel, homeDataModel.isTile, false)
                val homeSectionAdapter =
                    HomeSectionAdapter(
                        activity, homeDataModel, listSizeItem!!, homeDataModel.sectionSubType
                    )
                vh.mBinding.rvItems.adapter = homeSectionAdapter
            }

            ITEM_TYPE -> {
                (vh as ItemViewHolder).mBinding.btnLoadMore.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.load_more)
                vh.mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.titleHz.text = homeDataModel.sectionName
                vh.mBinding.ivHeader.visibility = View.VISIBLE
                if (homeDataModel.headerTextSize > 2) {
                    vh.mBinding.title.textSize = homeDataModel.headerTextSize.toFloat()
                } else {
                    vh.mBinding.title.textSize = 18f
                }
                if (homeDataModel.isTileSlider) {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    vh.mBinding.rlHeaderHz.visibility = View.VISIBLE
                    vh.mBinding.rlHeader.visibility = View.GONE
                } else {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity
                    )
                    vh.mBinding.rlHeader.visibility = View.VISIBLE
                    vh.mBinding.rlHeaderHz.visibility = View.GONE
                }
                vh.appHomeItemAdapter.setItemsInAdapter(homeDataModel.isTileSlider)
                vh.mBinding.rvItems.adapter = vh.appHomeItemAdapter

                // check item local
                if (SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + homeDataModel.sectionID) != null) {
                    val model = Gson().fromJson(
                        SectionPref.getInstance(
                            activity
                        ).getString(SectionPref.PRODUCT_LIST + homeDataModel.sectionID),
                        AppHomeItemModel::class.java
                    )
                    vh.mBinding.progressBarCyclic.visibility = View.GONE
                    if (model.isStatus) {
                        val itemList = model.itemListModels
                        val list = ArrayList<ItemListModel>()
                        if (itemList.size != 0) {
                            val listSize: Int
                            var i = 0
                            while (i < itemList.size) {
                                var ispresent = false
                                var j = 0
                                while (j < list.size) {
                                    if (list[j].itemNumber.equals(
                                            itemList[i].itemNumber, ignoreCase = true
                                        )
                                    ) {
                                        ispresent = true
                                        if (list[j].moqList.size == 0) {
                                            list[j].moqList.add(list[j])
                                            list[j].moqList[0].isChecked = true
                                        }
                                        list[j].moqList.add(itemList[i])
                                        break
                                    }
                                    j++
                                }
                                if (!ispresent) {
                                    list.add(itemList[i])
                                }
                                i++
                            }
                            if (!homeDataModel.isTileSlider) {
                                if (list.size > 2) {
                                    listSize = 2
                                    vh.appHomeItemAdapter.setItemListCategory(list, listSize)
                                    vh.mBinding.btnLoadMore.visibility = View.VISIBLE
                                } else {
                                    listSize = list.size
                                    vh.mBinding.btnLoadMore.visibility = View.GONE
                                    if (list.size != 0) {
                                        vh.appHomeItemAdapter.setItemListCategory(list, listSize)
                                    }
                                }
                            } else {
                                listSize = list.size
                                vh.mBinding.btnLoadMore.visibility = View.GONE
                                if (list.size != 0) {
                                    vh.appHomeItemAdapter.setItemListCategory(list, listSize)
                                }
                            }
                        }
                    }
                } else {
                    itemsOfferInterface.itemOffers(
                        vh.mBinding.progressBarCyclic,
                        homeDataModel.sectionID.toString(),
                        vh.mBinding.btnLoadMore,
                        vh.appHomeItemAdapter,
                        homeDataModel.isTileSlider
                    )
                }
                // load more Btn clicked
                vh.mBinding.btnLoadMore.setOnClickListener {
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    RetailerSDKApp.getInstance()
                        .updateAnalytics("appHome_loadMore_click", analyticPost)
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType.toString())
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                // View all Button click
                vh.mBinding.btnViewAll.setOnClickListener {
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    RetailerSDKApp.getInstance()
                        .updateAnalytics("appHome_item_viewAll_click", analyticPost)
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType)
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, homeDataModel, homeDataModel.isTileSlider)
            }

            OTHER_TYPE -> {
                (vh as ItemViewHolder).mBinding.btnLoadMore.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.load_more)
                vh.mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.titleHz.text = homeDataModel.sectionName
                vh.mBinding.ivHeader.visibility = View.VISIBLE
                if (homeDataModel.headerTextSize > 2) {
                    vh.mBinding.title.textSize = homeDataModel.headerTextSize.toFloat()
                } else {
                    vh.mBinding.title.textSize = 18f
                }
                if (homeDataModel.isTileSlider) {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity, LinearLayoutManager.HORIZONTAL, false
                    )
                    vh.mBinding.rlHeaderHz.visibility = View.VISIBLE
                    vh.mBinding.rlHeader.visibility = View.GONE
                } else {
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity
                    )
                    vh.mBinding.rlHeader.visibility = View.VISIBLE
                    vh.mBinding.rlHeaderHz.visibility = View.GONE
                    // ((ItemViewHolder) vh).mBinding.rvItems.setPadding(20, 0, 20, 0);
                }
                vh.appHomeItemAdapter.setItemsInAdapter(homeDataModel.isTileSlider)
                vh.mBinding.rvItems.adapter = vh.appHomeItemAdapter
                var url = homeDataModel.webViewUrl
                if (url != null && url.isNotEmpty() && url.contains("[CUSTOMERID]")) {
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
                        vh,
                        url,
                        homeDataModel.sectionID,
                        vh.appHomeItemAdapter,
                        homeDataModel.isTileSlider
                    )
                } else {
                    val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                    vh.itemView.visibility = View.GONE
                    param.height = 0
                    param.width = 0
                    vh.itemView.layoutParams = param
                }
                // load more Btn clicked
                vh.mBinding.btnLoadMore.setOnClickListener { v: View? ->
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.sectionName = homeDataModel.sectionName
                    // update analytics
                    RetailerSDKApp.getInstance()
                        .updateAnalytics("app_home_load_more_click", analyticPost)
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
                    analyticPost.url = homeDataModel.webViewUrl
                    // update analytics
                    RetailerSDKApp.getInstance()
                        .updateAnalytics("appHome_other_viewAll_click", analyticPost)
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString("SECTION_SUB_TYPE", homeDataModel.sectionSubType.toString())
                    args.putString("TITLE", homeDataModel.sectionName)
                    activity.pushFragments(AppHomeItemFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, homeDataModel, homeDataModel.isTileSlider)
            }

            FLASH_TYPE -> {
                (vh as RecyclerViewHolder).mBinding.title.text = homeDataModel.sectionName
                vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                    activity
                )
                vh.mBinding.progressBarCyclic.visibility = View.VISIBLE
                vh.mBinding.rlHeader.visibility = View.VISIBLE
                if (homeDataModel.headerTextSize > 2) {
                    vh.mBinding.title.textSize = homeDataModel.headerTextSize.toFloat()
                } else {
                    vh.mBinding.title.textSize = 18f
                }
                flashDealsOfferInterface.flashDealsOffer(
                    vh, homeDataModel.sectionID.toString(), homeDataModel.tileBackgroundImage
                )
                //blink
                val anim = ObjectAnimator.ofInt(
                    vh.mBinding.llLoadItem,
                    "textColor",
                    Color.WHITE,
                    activity.resources.getColor(R.color.colorAccent),
                    Color.WHITE
                )
                anim.duration = 1500
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
                    RetailerSDKApp.getInstance()
                        .updateAnalytics("app_home_flash_more_click", analyticPost)
                    val args = Bundle()
                    args.putString("SECTION_ID", homeDataModel.sectionID.toString())
                    args.putString(
                        "FlashDealBackImage", homeDataModel.tileBackgroundImage.toString()
                    )
                    activity.pushFragments(FlashDealOfferFragment.newInstance(), true, true, args)
                }
                setViewBackground(vh, homeDataModel, false, false)
            }

            DYNAMIC_HTML -> {
                var url = homeDataModel.webViewUrl
                Log.e("URL", "" + url)
                if (url != null && url.isNotEmpty() && url.contains("[CUSTOMERID]")) {
                    (vh as DynamicViewHolder).mBinding.webviewItem.visibility = View.VISIBLE
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
                        "" + SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER)
                    )
                    dynamicHtmlInterface.loadDynamicHtml(vh, position, url)
                } else {
                    (vh as DynamicViewHolder).mBinding.webviewItem.visibility = View.GONE
                }
            }

            CART -> {
                (vh as CartHolder).mBinding.btnViewAll.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.view_all)
                vh.mBinding.tvTotalH.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_total_amount)
                vh.mBinding.btnCheckout.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkout)
                vh.mBinding.tvTitle.text = homeDataModel.sectionName
                if (!TextUtils.isNullOrEmpty(homeDataModel.tileHeaderBackgroundImage)) {
                    Picasso.get().load(homeDataModel.tileHeaderBackgroundImage)
                        .placeholder(R.drawable.logo_grey).error(R.drawable.logo_grey)
                        .into(vh.mBinding.ivImage)
                } else {
                    vh.mBinding.ivImage.setImageResource(R.drawable.logo_grey)
                }
                vh.mBinding.tvName.text = homeDataModel.headerTextColor
                vh.mBinding.tvPrice.text =
                    "₹" + DecimalFormat("##.##").format(homeDataModel.unitPrice)
                vh.mBinding.tvMrp.text = "₹" + DecimalFormat("##.##").format(homeDataModel.price)
                vh.mBinding.tvMrp.paintFlags =
                    vh.mBinding.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                val decimalFormat = DecimalFormat("##.##")
                decimalFormat.isGroupingUsed = true
                decimalFormat.groupingSize = 3
                vh.mBinding.tvTotal.text = "₹" + decimalFormat.format(homeDataModel.total)
                if (homeDataModel.sequence > 1) {
                    vh.mBinding.tvTotalItems.visibility = View.VISIBLE
                    vh.mBinding.tvTotalItems.text =
                        "+" + (homeDataModel.sequence - 1) + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.more_items
                        )
                } else {
                    vh.mBinding.tvTotalItems.visibility = View.GONE
                }
                // View all Button click
                vh.mBinding.btnViewAll.setOnClickListener {
                    activity.startActivity(Intent(activity, ShoppingCartActivity::class.java))
                    Utils.leftTransaction(
                        activity
                    )
                }
                vh.mBinding.btnCheckout.setOnClickListener {
                    // update analytics
                    RetailerSDKApp.getInstance().updateAnalytics("app_home_checkout_click")
                    activity.startActivity(Intent(activity, ShoppingCartActivity::class.java))
                    Utils.leftTransaction(
                        activity
                    )
                }
            }

            SEARCH -> {
                (vh as SearchHolder).mBinding.tvTitle.text = homeDataModel.sectionName
                vh.mBinding.tvSearch.text = homeDataModel.tileBackgroundImage
                if (homeDataModel.appItemsList!!.size > 3) {
                    vh.mBinding.btnViewAll.visibility = View.VISIBLE
                    vh.mBinding.btnViewAll.text =
                        ("+" + (homeDataModel.appItemsList!!.size - 3) + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.more_items
                        ))
                } else {
                    vh.mBinding.btnViewAll.visibility = View.GONE
                }
                // set adapter
                vh.mBinding.rvSearch.addItemDecoration(
                    DividerItemDecoration(
                        activity, 1
                    )
                )
                val searchAdapter = AppHomeSearchAdapter(activity, homeDataModel.appItemsList!!, 3)
                vh.mBinding.rvSearch.adapter = searchAdapter
                // View all Button click
                vh.mBinding.rlHeader.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("pos", 67)
                    bundle.putString("query", homeDataList[position].tileBackgroundImage)
                    activity.pushFragments(SearchItemFragment.newInstance(), true, true, bundle)
                }
                vh.mBinding.btnViewAll.setOnClickListener {
                    vh.mBinding.btnViewAll.visibility = View.GONE
                    if (searchAdapter != null) {
                        searchAdapter.listSize = homeDataList[position].appItemsList?.size ?: 0
                        searchAdapter.notifyDataSetChanged()
                    }
                }
            }

            STORE -> {
                if (homeDataModel.isTile) {
                    (vh as StoreHolder).mBinding.rlHeader.visibility = View.VISIBLE
                    vh.mBinding.title.text = homeDataModel.sectionName
                    vh.mBinding.rvItems.layoutManager = GridLayoutManager(activity, 3)

                    vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                    setStoreBackground(vh, homeDataModel)
                } else {
                    (vh as StoreHolder).mBinding.rlHeader.visibility = View.GONE
                    vh.mBinding.rvItems.layoutManager = LinearLayoutManager(
                        activity
                    )
                }
                val sItemAdapter = StoreItemAdapter(activity, homeDataModel)
                vh.mBinding.rvItems.adapter = sItemAdapter
                storeApiInterface.callStoreApi(vh, sItemAdapter, homeDataModel.webViewUrl)
            }

            BUCKET_GAME_TYPE -> {
                (vh as BucketGameViewPagerViewHolder)
                val bucketGameViewPagerAdapter = BucketGameViewPagerAdapter(activity, homeDataModel)
                vh.binding.bucketGameViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                vh.binding.bucketGameViewpager.adapter = bucketGameViewPagerAdapter
                vh.binding.bucketGameViewpager.offscreenPageLimit = 3
                bucketGameInterface.callBucketGameApi(
                    vh, bucketGameViewPagerAdapter, homeDataModel.webViewUrl
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (homeDataList[position].sectionSubType) {
            "Slider" -> PAGER_TYPE
            "Base Category", "Brand", "Category", "SubCategory" -> if (homeDataList[position].isBanner) BANNER else SECTION_TYPE
            "Item" -> ITEM_TYPE
            "Flash Deal" -> FLASH_TYPE
            "Other" -> if (homeDataList[position].isBanner) NON_CLICKABLE_BANNER else OTHER_TYPE
            "DynamicHtml" -> DYNAMIC_HTML
            "cart" -> CART
            "search" -> SEARCH
            "recent" -> RECENT
            "Store" -> STORE
            "BucketGame" -> BUCKET_GAME_TYPE
            else -> 9
        }
    }

    override fun getItemCount(): Int {
        return homeDataList.size ?: 0
    }


    inner class ViewPagerViewHolder internal constructor(var mBinding: ItemMainListViewpagerBinding) :
        RecyclerView.ViewHolder(mBinding.root), View.OnClickListener {
        var sliderAdapterExample = SliderAdapterExample(activity)

        init {
            mBinding.pager.setPadding(20, 0, 20, 0)
            mBinding.pager.clipToPadding = false
//            mBinding.pager.setPageMargin(halfGap)
            mBinding.liMenu.ivFreebiesOffer.setOnClickListener(this)
            mBinding.liMenu.ivTradeOffer.setOnClickListener(this)
            mBinding.liMenu.ivCategories.setOnClickListener(this)
            mBinding.liMenu.liMyTarget.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.ivCategories -> {
                    RetailerSDKApp.getInstance().updateAnalytics("app_home_clearance_click")
                    //  activity.pushFragments(HomeCategoryFragment.newInstance(), false, true, null);
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
                            activity, CustomerSubCategoryTargetActivity::class.java
                        )
                    )
                    Utils.fadeTransaction(
                        activity
                    )
                }
            }
        }
    }

    inner class RecyclerViewHolder internal constructor(@kotlin.jvm.JvmField var mBinding: ItemMainListSectionBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ), View.OnClickListener {

        init {
            mBinding.liMenu.ivFreebiesOffer.setOnClickListener(this)
            mBinding.liMenu.ivTradeOffer.setOnClickListener(this)
            mBinding.liMenu.ivCategories.setOnClickListener(this)
            mBinding.liMenu.liMyTarget.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.ivCategories -> {
                    RetailerSDKApp.getInstance().updateAnalytics("app_home_clearance_click")
//                    activity.pushFragments(HomeCategoryFragment.newInstance(), false, true, null);
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
                            activity, CustomerSubCategoryTargetActivity::class.java
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

    inner class ItemViewHolder internal constructor(@kotlin.jvm.JvmField var mBinding: ItemMainListProductBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var appHomeItemAdapter =
            AppHomeItemAdapter(
                activity
            )
    }

    inner class BucketGameViewPagerViewHolder internal constructor(var binding: ItemMainBucketGamePagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun setViewBackground(
        vh: RecyclerViewHolder,
        homeDataModel: HomeDataModel,
        background: Boolean,
        isSlider: Boolean
    ) {
        try {
            val params = vh.mBinding.rlHeader.layoutParams
            if (background) {
                if (homeDataModel.hasBackgroundColor && homeDataModel.tileBackgroundColor != null) {
                    vh.mBinding.rvItems.setBackgroundColor(Color.parseColor(homeDataModel.tileBackgroundColor))
                } else if (homeDataModel.tileBackgroundImage != null && homeDataModel.tileBackgroundImage!!.isNotEmpty()) {
                    Picasso.get().load(
                        homeDataModel.tileBackgroundImage?.replace(" ", "%20")
                    ).into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                            vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                            e.printStackTrace()
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    })
                } else {
                    vh.mBinding.rvItems.setBackgroundColor(Color.WHITE)
                }
                if (homeDataModel.tileHeaderBackgroundColor != null) {
                    vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(homeDataModel.tileHeaderBackgroundColor))
                    vh.mBinding.ivHeader.setImageDrawable(null)
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = 100
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    vh.mBinding.rlHeader.setBackgroundColor(0)
                }
                if (homeDataModel.tileHeaderBackgroundImage != null && homeDataModel.tileHeaderBackgroundImage != "" && homeDataModel.tileHeaderBackgroundImage!!.isNotEmpty()) {
                    Picasso.get().load(
                        homeDataModel.tileHeaderBackgroundImage?.replace(" ".toRegex(), "%20")
                    ).into(vh.mBinding.ivHeader)
                } else {
                    vh.mBinding.ivHeader.setImageDrawable(null)
                }
                if (homeDataModel.headerTextColor != null) {
                    vh.mBinding.title.setTextColor(Color.parseColor(homeDataModel.headerTextColor))
                } else {
                    vh.mBinding.title.setTextColor(Color.WHITE)
                }
            } else {
                if (homeDataModel.tileHeaderBackgroundColor != null) {
                    vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(homeDataModel.tileHeaderBackgroundColor))
                    vh.mBinding.ivHeader.setImageDrawable(null)
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = 100
                } else {
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    vh.mBinding.rlHeader.setBackgroundColor(0)
                }
                if (homeDataModel.tileHeaderBackgroundImage != null && homeDataModel.tileHeaderBackgroundImage != "" && homeDataModel.tileHeaderBackgroundImage!!.isNotEmpty()) {
                    Picasso.get().load(homeDataModel.tileHeaderBackgroundImage?.trim())
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
            if (isSlider) {
                if (model.hasBackgroundColor) {
                    if (model.tileBackgroundColor != null) {
                        vh.mBinding.rlItems.setBackgroundColor(Color.parseColor(model.tileBackgroundColor))
                        vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                    } else {
                        vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                    }
                } else if (model.tileBackgroundImage != null && model.tileBackgroundImage != "" && model.tileBackgroundImage!!.isNotEmpty()) {
                    Picasso.get().load(model.tileBackgroundImage!!.replace(" ".toRegex(), "%20"))
                        .into(object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                                vh.mBinding.rlItems.background = BitmapDrawable(bitmap)
                                vh.mBinding.rvItems.setBackgroundColor(Color.TRANSPARENT)
                            }

                            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                                e.printStackTrace()
                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
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
                } else if (model.tileBackgroundImage != null && model.tileBackgroundImage != "" && model.tileBackgroundImage!!.isNotEmpty()) {
                    Picasso.get().load(model.tileBackgroundImage!!).into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                            vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                            e.printStackTrace()
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    })
                }
            }
            if (!model.tileHeaderBackgroundColor.isNullOrEmpty()) {
                vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(model.tileHeaderBackgroundColor))
                vh.mBinding.ivHeader.setImageDrawable(null)
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = 100
            } else {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                vh.mBinding.rlHeader.setBackgroundColor(0)
            }
            if (model.tileHeaderBackgroundImage != null && !model.tileHeaderBackgroundImage.isNullOrEmpty()) {
                Picasso.get().load(model.tileHeaderBackgroundImage).into(vh.mBinding.ivHeader)
            } else {
                vh.mBinding.ivHeader.setImageDrawable(null)
            }
            if (model.headerTextColor != null && model.headerTextColor!!.isNotEmpty()) {
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

    private fun setStoreBackground(vh: StoreHolder, model: HomeDataModel) {
        if (model.headerTextColor != null) {
            vh.mBinding.title.setTextColor(Color.parseColor(model.headerTextColor))
        } else {
            vh.mBinding.title.setTextColor(Color.WHITE)
        }
        if (model.tileHeaderBackgroundColor != null) {
            vh.mBinding.rlHeader.setBackgroundColor(Color.parseColor(model.tileHeaderBackgroundColor))
            vh.mBinding.title.height = 100
        } else {
            vh.mBinding.rlHeader.setBackgroundColor(0)
            vh.mBinding.title.height = 50
        }
        if (!model.tileHeaderBackgroundImage.isNullOrEmpty()) {
            Picasso.get().load(model.tileHeaderBackgroundImage)
                .into(vh.mBinding.ivHeader)
        } else {
            vh.mBinding.ivHeader.setImageDrawable(null)
        }
        if (model.hasBackgroundColor) {
            if (model.tileBackgroundColor != null) {
                vh.mBinding.rvItems.setBackgroundColor(Color.parseColor(model.tileBackgroundColor))
            }
        } else if (model.tileBackgroundImage != null && model.tileBackgroundImage != "" && model.tileBackgroundImage!!.isNotEmpty()) {
            Picasso.get()
                .load(model.tileBackgroundImage!!.replace(" ".toRegex(), "%20"))
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom) {
                        vh.mBinding.rvItems.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(
                        e: Exception, errorDrawable: Drawable?
                    ) {
                        e.printStackTrace()
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        }
    }

    private fun clickActionPerform(
        vh: ImageView, actionType: String, model: HomeDataModel, appItemModel: AppItemsList
    ) {
        try {
            vh.isEnabled = false
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    // Do something after 100ms
                    vh.isEnabled = true
                    handler.postDelayed(this, 200)
                    handler.removeCallbacks(this)
                    var args = Bundle()
                    args.putString("ItemId", appItemModel.redirectionID.toString())
                    args.putInt("BaseCategoryId", appItemModel.baseCategoryId)
                    args.putString("SectionType", model.sectionSubType)
                    args.putInt("CATEGORY_ID", appItemModel.categoryId)
                    args.putInt("SUB_CAT_ID", appItemModel.subCategoryId)
                    args.putInt("SUB_SUB_CAT_ID", appItemModel.subsubCategoryId)
                    args.putBoolean("HOME_FLAG", true)
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = model.sectionID
                    analyticPost.sectionSubType = model.sectionSubType
                    analyticPost.sectionName = model.sectionName
                    analyticPost.baseCatId = appItemModel.baseCategoryId.toString()
                    analyticPost.categoryId = appItemModel.categoryId
                    analyticPost.subCatId = appItemModel.subCategoryId
                    analyticPost.subSubCatId = appItemModel.subsubCategoryId
                    var url = model.webViewUrl
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
                        model.webViewUrl = url
                    }
                    when (actionType) {
                        "Base Category" -> {
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                }else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                args.putString("cetegoryTittle", appItemModel.tileName)
                                activity.pushFragments(
                                    SubCategoryFragment.newInstance(), false, true, args
                                )
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_basCat_click", analyticPost)
                        }

                        "Brand" -> {
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                } else if (model.webViewUrl!!.startsWith("vAtm") || model.webViewUrl!!.startsWith(
                                        "vatm"
                                    )
                                ) {
                                    activity.callVAtmApi()
                                } else if (model.webViewUrl!!.contains("Udhar/GenerateLead")) {
                                    activity.callLeadApi(model.webViewUrl ?: "")
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0", ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
//                                    args.putBoolean("isStore", true);
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(), false, true, args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(), false, true, args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_brand_click", analyticPost)
                        }

                        "Category" -> {
                            args.putBoolean("HOME_FLAG", false)
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                }  else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (appItemModel.redirectionID != 0) {
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(), false, true, args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(), false, true, args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_category_click", analyticPost)
                        }

                        "SubCategory" -> {
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                }  else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                } else if (model.webViewUrl!!.startsWith("ShowStoreFlashDeal")) {
                                    val id = model.appItemsList!![0].redirectionID
                                    args = Bundle()
                                    args.putString("SECTION_ID", "-1")
                                    args.putInt("subCategoryId", id)
                                    args.putBoolean("isStore", true)
                                    activity.pushFragments(
                                        FlashDealOfferFragment.newInstance(), true, true, args
                                    )
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0", ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
                                    args.putString("ITEM_IMAGE", appItemModel.tileImage)
                                    activity.pushFragments(
                                        HomeSubCategoryFragment.newInstance(), false, true, args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(), false, true, args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_subCat_click", analyticPost)
                        }

                        "Other" -> {
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                }  else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                callActivities(
                                    appItemModel.bannerActivity ?: "",
                                    appItemModel.redirectionUrl ?: ""
                                )
                            }
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
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
                                        SubSubCategoryFragment.newInstance(), false, true, args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(), false, true, args
                                    )
                                }
                            }
                            // update analytics
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_default_click", analyticPost)
                        }

                        else -> {
                            if (model.viewType != null && model.viewType!!.isNotEmpty() && model.viewType.equals(
                                    "webView", ignoreCase = true
                                )
                            ) {
                                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity)
                                            .getBoolean(EndPointPref.showNewSocial)
                                    )
                                        activity.startActivity(
                                            Intent(
                                                activity,
                                                FeedActivity::class.java
                                            )
                                        ) else activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if(model.webViewUrl!!.contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(model.webViewUrl)
                                }  else {
                                    val bundle = Bundle()
                                    bundle.putString("url", model.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity, WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!appItemModel.redirectionID.toString().equals(
                                        "0", ignoreCase = true
                                    ) && appItemModel.redirectionID != 0
                                ) {
                                    activity.pushFragments(
                                        SubSubCategoryFragment.newInstance(), false, true, args
                                    )
                                } else {
                                    args.putString("BRAND_NAME", "Brand")
                                    activity.pushFragments(
                                        ShopbyBrandFragment.newInstance(), false, true, args
                                    )
                                }
                            }
                            RetailerSDKApp.getInstance()
                                .updateAnalytics("appHome_default_click", analyticPost)
                        }
                    }
                }
            }, 250)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callActivities(bannerActivity: String, url: String) {
        var i: Intent? = null
        if (bannerActivity.equals("games", ignoreCase = true)) {
            activity.startActivity(Intent(activity, GamesListActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("target", ignoreCase = true)) {
            activity.startActivity(Intent(activity, CustomerSubCategoryTargetActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("prime", ignoreCase = true)) {
            activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("shoppingcart", ignoreCase = true)) {
            activity.startActivity(Intent(activity, ShoppingCartActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("wallet", ignoreCase = true)) {
            i = Intent(activity, MyWalletActivity::class.java)
            activity.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("category", ignoreCase = true)) {
            activity.pushFragments(
                HomeCategoryFragment(), false, true, null
            )
        } else if (bannerActivity.equals("tradeoffer", ignoreCase = true)) {
            activity.pushFragments(
                TradeOfferFragment.newInstance(), false, true, null
            )
        } else if (bannerActivity.equals("allbrands", ignoreCase = true)) {
            activity.pushFragments(
                AllBrandFragItemList.newInstance(), false, true, null
            )
        } else if (bannerActivity.equals("freebies", ignoreCase = true)) {
            activity.startActivity(Intent(activity, FreebiesOfferActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("myorder", ignoreCase = true)) {
            activity.startActivity(Intent(activity, MyOrderActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("direct", ignoreCase = true)) {
            if (EndPointPref.getInstance(activity)
                    .getBoolean(EndPointPref.showNewSocial)
            )
                activity.startActivity(
                    Intent(
                        activity,
                        FeedActivity::class.java
                    )
                ) else activity.startActivity(
                Intent(
                    activity,
                    TradeActivity::class.java
                )
            )
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("Clearance", ignoreCase = true)) {
            activity.startActivity(Intent(activity, ClearanceActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("ExternalURL", ignoreCase = true)) {
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
}