package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store.StoreItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.Grid3Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.OtherTypeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.store.StoreHomeFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class StoreItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    val TILE = 1
    val BANNER = 2
    private val activity: HomeActivity
    private val homeDataModel: HomeDataModel
    private var list: ArrayList<StoreItemModel>? = null
    private val handler = Handler(Looper.getMainLooper())
    private var mSectionSubType: String? = null


    constructor(activity: HomeActivity, homeDataModel: HomeDataModel) {
        this.activity = activity
        this.homeDataModel = homeDataModel
        list = ArrayList()
    }

    constructor(activity: HomeActivity, homeDataModel: HomeDataModel, sectionSubType: String?) {
        this.homeDataModel = homeDataModel
        this.activity = activity
        this.mSectionSubType = sectionSubType
    }

    fun setData(list: ArrayList<StoreItemModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType) {
            BANNER -> RecyclerViewOtherTypeHolder(
                OtherTypeBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )
            TILE -> RecyclerView3gridTypeOneHolder(
                Grid3Binding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )
            else -> RecyclerViewOtherTypeHolder(
                OtherTypeBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, i: Int) {
        val model = list!![i]
        if (vh.itemViewType == BANNER) {
            if (!TextUtils.isNullOrEmpty(model.logo)) {
                Picasso.get()
                    .load(EndPointPref.getInstance(RetailerSDKApp.getInstance()).baseUrl + model.logo!!.trim())
                    .placeholder(R.drawable.logo_grey)
                    .into((vh as RecyclerViewOtherTypeHolder).mBinding.ivItem)
            } else {
                (vh as RecyclerViewOtherTypeHolder).mBinding.ivItem.setImageResource(
                    R.drawable.logo_grey
                )
            }
            vh.mBinding.catLinearLayout.setOnClickListener {
                clickActionPerform(
                    vh.mBinding.catLinearLayout,
                    homeDataModel.sectionSubType, model
                )
            }
        } else if (vh.itemViewType == TILE) {
            (vh as RecyclerView3gridTypeOneHolder).mBinding.tvTextTitle.text = model.subCategoryName
            if (!TextUtils.isNullOrEmpty(model.logo)) {
                Picasso.get()
                    .load(EndPointPref.getInstance(RetailerSDKApp.getInstance()).baseUrl + model.logo!!.trim())
                    .placeholder(R.drawable.logo_grey)
                    .into(vh.mBinding.ivItemImage)
            } else {
                vh.mBinding.ivItemImage.setImageResource(R.drawable.logo_grey)
            }
            setBackGroundImage3Gird(vh)
            vh.mBinding.liTile.setOnClickListener {
                clickActionPerform(
                    vh.mBinding.liTile,
                    homeDataModel.sectionSubType, model
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (homeDataModel.isTile) {
            TILE
        } else {
            BANNER
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    private fun clickActionPerform(
        vh: LinearLayout,
        actionType: String,
        storeItemModel: StoreItemModel
    ) {
        try {
            vh.isEnabled = false
            handler.postDelayed(object : Runnable {
                override fun run() {
                    // Do something after 100ms
                    vh.isEnabled = true
                    handler.postDelayed(this, 500)
                    handler.removeCallbacks(this)
                    val args = Bundle()
                    // args.putString("ItemId", String.valueOf(storeItemModel.getRedirectionID()));
                    // args.putString("BaseCategoryId", String.valueOf(storeItemModel.getBaseCategoryId()));
                    args.putString("SectionType", mSectionSubType)
                    // args.putInt("CATEGORY_ID", storeItemModel.getCategoryId());
                    args.putInt("SUB_CAT_ID", storeItemModel.subCategoryId)
                    // args.putInt("SUB_SUB_CAT_ID", storeItemModel.getSubsubCategoryId());
                    args.putBoolean("HOME_FLAG", true)

                    // analytics data
                    val analyticPost = AnalyticPost()
                    analyticPost.sectionId = homeDataModel.sectionID
                    analyticPost.sectionName = homeDataModel.sectionName
                    analyticPost.sectionSubType = homeDataModel.sectionSubType
                    analyticPost.subCatId = storeItemModel.subCategoryId
                    analyticPost.subSubCatName = storeItemModel.subCategoryName
                    // update analytics
                    RetailerSDKApp.getInstance().updateAnalytics("storeSectionClick", analyticPost)
                    activity.pushFragments(StoreHomeFragment.newInstance(), false, true, args)
                }
            }, 300)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class RecyclerViewOtherTypeHolder internal constructor(var mBinding: OtherTypeBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    inner class RecyclerView3gridTypeOneHolder internal constructor(var mBinding: Grid3Binding) :
        RecyclerView.ViewHolder(mBinding.root)



    private fun setBackGroundImage3Gird(vh: RecyclerView3gridTypeOneHolder) {
        if (!homeDataModel.sectionBackgroundImage.isNullOrEmpty()) {
            Picasso.get()
                .load(homeDataModel.sectionBackgroundImage.replace(" ".toRegex(), "%20"))
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom) {
                        vh.mBinding.liTile.background = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: java.lang.Exception, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }
                })
        } else if (homeDataModel.sectionBackgroundImage != null && homeDataModel.sectionBackgroundImage!!.isNotEmpty()) {
            Picasso.get()
                .load(
                    homeDataModel.sectionBackgroundImage!!.replace(
                        " ".toRegex(),
                        "%20"
                    )
                )
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                        vh.mBinding.liTile.setBackground(BitmapDrawable(bitmap))
                    }

                    override fun onBitmapFailed(e: java.lang.Exception, errorDrawable: Drawable) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                    }
                })
        } else {
            vh.mBinding.liTile.setBackgroundResource(R.drawable.rectangle_transperent_outline)
        }
    }
}