package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.store

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.Grid3Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.HorizontalTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.OtherTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.OtherTypeOneBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.TileTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class StoreSectionAdapter(
    private val activity: HomeActivity,
    private val dataDetailsList: HomeDataModel?,
    private val listSizeItem: Int,
    private val mSectionSubType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val SCROLLABLE_BANNER = 0
    val SCROLLABLE_TILE = 1
    val BANNER = 2
    val OTHER_TYPE_1 = 3
    val OTHER_TYPE_2 = 4
    private val handler = Handler(Looper.getMainLooper())
    private var horizontal = false
    private var SlidervectorDrawable: Drawable? = null
    private var TilevectorDrawable: Drawable? = null

    init {
        SlidervectorDrawable = AppCompatResources.getDrawable(
            activity, R.drawable.logo_grey
        )
        TilevectorDrawable = AppCompatResources.getDrawable(activity, R.drawable.logo_grey)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType) {
            SCROLLABLE_BANNER -> RecyclerViewHorizontalHolder(
                HorizontalTypeBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )

            SCROLLABLE_TILE -> RecyclerViewTileTypeHolder(
                TileTypeBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )

            BANNER -> RecyclerViewOtherTypeHolder(
                OtherTypeBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )

            OTHER_TYPE_2 -> RecyclerView3gridTypeOneHolder(
                Grid3Binding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )

            else -> RecyclerViewOtherTypeOneHolder(
                OtherTypeOneBinding.inflate(
                    inflater,
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
                    .placeholder(SlidervectorDrawable)
                    .into((vh as RecyclerViewHorizontalHolder).ivBanner)
            } else {
                (vh as RecyclerViewHorizontalHolder).ivBanner.setImageDrawable(SlidervectorDrawable)
            }
            vh.mBinding.catLinearLayout.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    dataDetailsList.sectionSubType, homeDataDetailsModel
                )
            }
        } else if (vh.itemViewType == SCROLLABLE_TILE) {
            (vh as RecyclerViewTileTypeHolder).tvTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity).load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(TilevectorDrawable).into(vh.ivTile)
            } else {
                vh.ivTile.setImageDrawable(TilevectorDrawable)
            }
            vh.mBinding.liTile.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.liTile,
                    dataDetailsList.sectionSubType, homeDataDetailsModel
                )
            }
            setScrollableBackGroundImage(vh, homeDataDetailsModel)
        } else if (vh.itemViewType == BANNER) {
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.bannerImage)) {
                Glide.with(activity).load(homeDataDetailsModel.bannerImage!!.trim { it <= ' ' })
                    .placeholder(TilevectorDrawable).dontAnimate()
                    .into((vh as RecyclerViewOtherTypeHolder).ivItems)
            } else {
                (vh as RecyclerViewOtherTypeHolder).ivItems.setImageDrawable(TilevectorDrawable)
            }
            vh.mBinding.catLinearLayout.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        } else if (vh.itemViewType == OTHER_TYPE_1) {
            setBackGroundImage(vh as RecyclerViewOtherTypeOneHolder, homeDataDetailsModel)
            vh.mBinding.tvTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity)
                    .load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(TilevectorDrawable)
                    .into(vh.ivItems)
            } else {
                vh.ivItems.setImageDrawable(TilevectorDrawable)
            }
            vh.mBinding.catLinearLayout.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        } else if (vh.itemViewType == OTHER_TYPE_2) {
            setBackGroundImage3Gird(vh as RecyclerView3gridTypeOneHolder, homeDataDetailsModel)
            vh.tvTitle.text = homeDataDetailsModel.tileName
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.tileImage)) {
                Glide.with(activity).load(homeDataDetailsModel.tileImage!!.trim { it <= ' ' })
                    .placeholder(TilevectorDrawable).into(
                    vh.ivItems
                )
            } else {
                vh.ivItems.setImageDrawable(TilevectorDrawable)
            }
            vh.mBinding.liTile.setOnClickListener { v: View? ->
                clickActionPerform(
                    vh.mBinding.liTile,
                    dataDetailsList.sectionSubType,
                    homeDataDetailsModel
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        horizontal = dataDetailsList!!.rowCount == 1 && dataDetailsList!!.columnCount > 2
        return if (horizontal && !dataDetailsList!!.isTile) {
            SCROLLABLE_BANNER
        } else if (horizontal && dataDetailsList!!.isTile) {
            SCROLLABLE_TILE
        } else if (dataDetailsList!!.isBanner) {
            BANNER
        } else if (dataDetailsList.rowCount > 1 && dataDetailsList.columnCount > 2) {
            OTHER_TYPE_2
        } else {
            OTHER_TYPE_1
        }
    }

    override fun getItemCount(): Int {
        return if (dataDetailsList == null) 0 else listSizeItem
    }

    private fun setScrollableBackGroundImage(
        vh: RecyclerViewTileTypeHolder,
        homeDataDetailsModel: AppItemsList
    ) {
        if (homeDataDetailsModel.tileSectionBackgroundImage != null && !homeDataDetailsModel.tileSectionBackgroundImage!!.isEmpty()) {
            Picasso.get()
                .load(homeDataDetailsModel.tileSectionBackgroundImage!!.trim { it <= ' ' })
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
            Picasso.get()
                .load(dataDetailsList.sectionBackgroundImage!!.trim { it <= ' ' })
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                        vh.mBinding.catLinearLayout.background = BitmapDrawable(bitmap)
                    }
                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
        } else if (homeDataDetailsModel.tileSectionBackgroundImage != null && !homeDataDetailsModel.tileSectionBackgroundImage!!.isEmpty()) {
            Picasso.get()
                .load(homeDataDetailsModel.tileSectionBackgroundImage!!.trim { it <= ' ' })
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
        homeDataDetailsModel: AppItemsList
    ) {
        try {
            vh.isEnabled = false
            handler.postDelayed(object : Runnable {
                override fun run() {

                    // Do something after 100ms
                    vh.isEnabled = true
                    handler.postDelayed(this, 400)
                    handler.removeCallbacks(this)
                    val args = Bundle()
                    args.putString("ItemId", homeDataDetailsModel.redirectionID.toString())
                    args.putInt("BaseCategoryId", homeDataDetailsModel.baseCategoryId)
                    args.putString("SectionType", mSectionSubType)
                    args.putInt("CATEGORY_ID", homeDataDetailsModel.categoryId)
                    args.putInt("SUB_CAT_ID", homeDataDetailsModel.subCategoryId)
                    args.putInt("SUB_SUB_CAT_ID", homeDataDetailsModel.subsubCategoryId)
                    args.putBoolean("HOME_FLAG", true)
                    args.putBoolean("isStore", true)
                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = dataDetailsList!!.sectionID
                    analyticPost.sectionSubType = dataDetailsList.sectionSubType
                    analyticPost.sectionName = dataDetailsList.sectionName
                    analyticPost.baseCatId = homeDataDetailsModel.baseCategoryId.toString()
                    analyticPost.categoryId = homeDataDetailsModel.categoryId
                    analyticPost.subCatId = homeDataDetailsModel.subCategoryId
                    analyticPost.subSubCatId = homeDataDetailsModel.subsubCategoryId
                    when (actionType) {
                        "Base Category" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    activity.startActivity(
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
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                args.putString("cetegoryTittle", homeDataDetailsModel.tileName)
                                activity.pushFragments(
                                    SubCategoryFragment.newInstance(),
                                    false,
                                    true,
                                    args
                                )
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics(
                                "store_app_home_basCat_"
                                        + homeDataDetailsModel.tileName + "_click", analyticPost
                            )
                        }

                        "Brand" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            TradeActivity::class.java
                                        )
                                    )
                                    Utils.leftTransaction(
                                        activity
                                    )
                                } else if (dataDetailsList.webViewUrl!!.startsWith("FinBox")) {
                                    //  brandClicked.onBrandClicked(0);
                                } else if (dataDetailsList.webViewUrl!!.startsWith("CreditLine")) {
                                    //  brandClicked.onBrandClicked(1);
                                } else if (dataDetailsList.webViewUrl!!.startsWith("vAtm") || dataDetailsList.webViewUrl!!.startsWith(
                                        "vatm"
                                    )
                                ) {
                                    activity.callVAtmApi()
                                } else if (dataDetailsList.webViewUrl!!.contains("Udhar/GenerateLead")) {
                                    activity.callLeadApi(dataDetailsList.webViewUrl!!)
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
                                if (!homeDataDetailsModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && homeDataDetailsModel.redirectionID != 0
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
                            // update analytics
                            MyApplication.getInstance().updateAnalytics(
                                "store_app_home_brand_"
                                        + homeDataDetailsModel.tileName + "_click", analyticPost
                            )
                        }

                        "Category" -> {
                            args.putBoolean("HOME_FLAG", false)
                            // args.putString("BRAND_NAME", "Category");
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    activity.startActivity(
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
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!homeDataDetailsModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && homeDataDetailsModel.redirectionID != 0
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
                            // update analytics
                            MyApplication.getInstance().updateAnalytics(
                                "store_app_home_category_"
                                        + homeDataDetailsModel.tileName + "_click", analyticPost
                            )
                        }

                        "SubCategory" -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    activity.startActivity(
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
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!homeDataDetailsModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && homeDataDetailsModel.redirectionID != 0
                                ) {
                                    args.putString("ITEM_IMAGE", homeDataDetailsModel.tileImage)
                                    activity.pushFragments(
                                        StoreHomeFragment.newInstance(),
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
                            MyApplication.getInstance().updateAnalytics(
                                "store_app_home_subCat_"
                                        + homeDataDetailsModel.tileName + "_click", analyticPost
                            )
                        }

                        else -> {
                            if (dataDetailsList.viewType != null && !dataDetailsList.viewType!!.isEmpty() && dataDetailsList.viewType.equals(
                                    "webView",
                                    ignoreCase = true
                                )
                            ) {
                                if (dataDetailsList.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                                    activity.startActivity(
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
                                    bundle.putString("url", dataDetailsList.webViewUrl)
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            WebViewActivity::class.java
                                        ).putExtras(bundle)
                                    )
                                }
                            } else {
                                if (!homeDataDetailsModel.redirectionID.toString().equals(
                                        "0",
                                        ignoreCase = true
                                    ) && homeDataDetailsModel.redirectionID != 0
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
                            MyApplication.getInstance().updateAnalytics(
                                "store_app_home_default_"
                                        + homeDataDetailsModel.tileName + "_click", analyticPost
                            )
                        }
                    }
                }
            }, 300)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class RecyclerViewHorizontalHolder internal constructor(var mBinding: HorizontalTypeBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivBanner: ImageView

        init {
            ivBanner = mBinding.ivBanner
        }
    }

    inner class RecyclerViewTileTypeHolder internal constructor(var mBinding: TileTypeBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivTile: ImageView
        var tvTitle: TextView

        init {
            ivTile = mBinding.ivTile
            tvTitle = mBinding.tvTitle
        }
    }

    inner class RecyclerViewOtherTypeHolder internal constructor(var mBinding: OtherTypeBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivItems: ImageView

        init {
            ivItems = mBinding.ivItem
        }
    }

    inner class RecyclerViewOtherTypeOneHolder internal constructor(var mBinding: OtherTypeOneBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivItems: ImageView

        init {
            ivItems = mBinding.ivItemImage
        }
    }

    inner class RecyclerView3gridTypeOneHolder internal constructor(var mBinding: Grid3Binding) :
        RecyclerView.ViewHolder(
            mBinding.root
        ) {
        var ivItems: ImageView
        var tvTitle: TextView

        init {
            ivItems = mBinding.ivItemImage
            tvTitle = mBinding.tvTextTitle
        }
    }
}