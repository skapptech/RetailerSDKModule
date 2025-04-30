package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.webkit.WebSettings
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db.SearchItemDTO
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.GameBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel.AppItemsList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeOfferFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store.StoreItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHome1Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BucketGameInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.DynamicHtmlInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.FlashDealsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.ItemsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.JavaScriptInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.StoreApiInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.EndFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GpsUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.DynamicViewHolder
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.json.JSONObject
import java.util.Collections

class HomeFragment : Fragment(), FlashDealsOfferInterface, ItemsOfferInterface,
    DynamicHtmlInterface, StoreApiInterface, BucketGameInterface {
    private val TAG = this.javaClass.name
    private lateinit var appCtx: MyApplication
    private var mBinding: FragmentHome1Binding? = null
    private lateinit var appHomeViewModel: AppHomeViewModel
    private var activity: HomeActivity? = null
    private var rlHeader: RelativeLayout? = null
    private var llLoadMore: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var mFlashDealItemListAdapter: FlashDealItemListAdapter? = null
    private var mHomeAdapter: HomeAdapter? = null
    private var homeDataList = ArrayList<HomeDataModel>()
    private val mFlashDealArrayList = ArrayList<ItemListModel>()
    private var acceptFlashEndTimeDes: Disposable? = null
    private var acceptFlashStartTimeDes: Disposable? = null
    private var cancellationTokenSource: CancellationTokenSource? = null
    private var warehouseId = 0
    private var customerId = 0
    private var lang = ""
    private var listSize = 0
    private var listSizeFlashDeal = 0
    private var lat = 0.0
    private var lng = 0.0
    private var mFlashHome = false
    private var sectionName: String? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var appHomeItemAdapter1: AppHomeItemAdapter? = null
    private var viewHolder:RecyclerView.ViewHolder?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as MyApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sectionName = arguments!!.getString("sectionName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home1, container, false)
        val appRepository = AppRepository(activity!!.applicationContext)
        appHomeViewModel =
            ViewModelProvider(
                activity!!,
                AppHomeViewModelFactory(appCtx, appRepository)
            )[AppHomeViewModel::class.java]
        initialization()

        observe(appHomeViewModel.getAppHomeSectionData, ::handleAppHomeSectionResult)
        observe(appHomeViewModel.getFlashDealItemData, ::handleFlashDealItemResult)

        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().logScreenAnalytics(this.javaClass)
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            this.javaClass.simpleName
        )
        activity!!.bottomNavigationView!!.menu.findItem(R.id.home).setChecked(true)
        if (mFlashHome) {
            if (mFlashDealItemListAdapter != null) {
                mFlashDealItemListAdapter!!.notifyDataSetChanged()
            }
        }
        if (mHomeAdapter != null) {
            mHomeAdapter!!.notifyDataSetChanged()
        }
        if (MyApplication.getInstance().isReloadCart) {
            MyApplication.getInstance().isReloadCart = false
            updateCartInList()
        }
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.spLayout!!.visibility = View.GONE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) activity!!.mBinding!!.toolbarH.liStoreH.visibility = View.VISIBLE
        if (!EndPointPref.getInstance(activity).getBoolean(EndPointPref.IS_SHOW_SELLER)) {
            activity!!.mBinding!!.toolbarH.liStoreH.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) activity!!.mBinding!!.toolbarH.liStoreH.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            callApi()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (acceptFlashEndTimeDes != null) {
            acceptFlashEndTimeDes!!.dispose()
        }
        if (acceptFlashStartTimeDes != null) {
            acceptFlashStartTimeDes!!.dispose()
        }
        if (cancellationTokenSource != null) {
            cancellationTokenSource!!.cancel()
        }
    }

    // flash deal offer
    override fun flashDealsOffer(
        vh: RecyclerView.ViewHolder?,
        sectionId: String?,
        flashDealBackImages: String?
    ) {
        rlHeader = (vh as HomeAdapter.RecyclerViewHolder).mBinding.rlHeader
        llLoadMore = vh.mBinding.llLoadItem
        progressBar = vh.mBinding.progressBarCyclic
        listSizeFlashDeal = 0
        // Offer API call
        val marginLayoutParams = MarginLayoutParams(vh.mBinding.rvItems.layoutParams)
        marginLayoutParams.topMargin = 50
        mFlashDealItemListAdapter =
            FlashDealItemListAdapter(
                activity!!,
                mFlashDealArrayList,
                listSizeFlashDeal,
                flashDealBackImages
            )
        vh.mBinding.rvItems.adapter =
            mFlashDealItemListAdapter
        progressBar!!.visibility = View.VISIBLE
        callFlashDealApi(sectionId!!)
        handleEndFlashDeal()
        handleStartFlashDeal(sectionId)
    }

    override fun loadDynamicHtml(holder: RecyclerView.ViewHolder, position: Int, url: String) {
        val data = SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + url)
        if (data != null) {
            (holder as DynamicViewHolder).mBinding.webviewItem.visibility = View.VISIBLE
            val webView = holder.mBinding.webviewItem
            // webView.clearCache(true);
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = false
            webView.settings.javaScriptEnabled = true
            webView.settings.pluginState = WebSettings.PluginState.ON
            webView.addJavascriptInterface(JavaScriptInterface(activity), "Android")
            webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null)
        } else {
            commonClassForAPI!!.fetchDynamicHtml(object : DisposableObserver<String?>() {
                override fun onNext(response: String) {
                    if (!TextUtils.isNullOrEmpty(response)) {
                        try {
                            (holder as DynamicViewHolder).mBinding.webviewItem.visibility =
                                View.VISIBLE
                            val webView = holder.mBinding.webviewItem
                            webView.clearCache(true)
                            webView.settings.loadWithOverviewMode = true
                            webView.settings.useWideViewPort = false
                            webView.settings.javaScriptEnabled = true
                            webView.settings.pluginState = WebSettings.PluginState.ON
                            webView.addJavascriptInterface(JavaScriptInterface(activity), "Android")
                            webView.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null)
                            SectionPref.getInstance(activity)
                                .putString(SectionPref.PRODUCT_LIST + url, response)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            (holder as DynamicViewHolder).mBinding.webviewItem.visibility =
                                View.GONE
                        }
                    } else {
                        (holder as DynamicViewHolder).mBinding.webviewItem.visibility = View.GONE
                    }
                }
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    (holder as DynamicViewHolder).mBinding.webviewItem.visibility = View.GONE
                }
                override fun onComplete() {}
            }, url)
           /* appHomeViewModel.getDynamicHtm(url)
            appHomeViewModel.getDynamicHtmlData.observe(activity!!) {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        it.data?.let {
                            if (!TextUtils.isNullOrEmpty(it)) {
                                try {
                                    (holder as DynamicViewHolder).mBinding.webviewItem.visibility =
                                        View.VISIBLE
                                    val webView = holder.mBinding.webviewItem
                                    webView.clearCache(true)
                                    webView.settings.loadWithOverviewMode = true
                                    webView.settings.useWideViewPort = false
                                    webView.settings.javaScriptEnabled = true
                                    webView.settings.pluginState = WebSettings.PluginState.ON
                                    webView.addJavascriptInterface(
                                        JavaScriptInterface(activity),
                                        "Android"
                                    )
                                    webView.loadDataWithBaseURL(
                                        null,
                                        it,
                                        "text/html",
                                        "UTF-8",
                                        null
                                    )
                                    SectionPref.getInstance(activity).putString(SectionPref.PRODUCT_LIST + url, it)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    (holder as DynamicViewHolder).mBinding.webviewItem.visibility =
                                        View.GONE
                                }
                            } else {
                                (holder as DynamicViewHolder).mBinding.webviewItem.visibility =
                                    View.GONE
                            }
                        }
                    }

                    is Response.Error -> {
                        (holder as DynamicViewHolder).mBinding.webviewItem.visibility = View.GONE
                    }
                }

            }*/
        }
    }

    override fun itemOffers(
        progressBar: ProgressBar,
        sectionId: String,
        btnLordMore: Button,
        appHomeItemAdapter: AppHomeItemAdapter,
        tileSlider: Boolean
    ) {
        val data = SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + sectionId)
        if (!TextUtils.isNullOrEmpty(data)) {
            val model = Gson().fromJson(data, AppHomeItemModel::class.java)
            progressBar.visibility = View.GONE
            if (model.isStatus) {
                val itemList = model.itemListModels
                val list = appHomeViewModel.getMoqList(itemList)
                if (list.size != 0) {
                    if (!tileSlider) {
                        if (list.size > 2) {
                            listSize = 2
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                            btnLordMore.visibility = View.VISIBLE
                        } else {
                            listSize = list.size
                            btnLordMore.visibility = View.GONE
                            if (list.size != 0) {
                                appHomeItemAdapter.setItemListCategory(list, listSize)
                            }
                        }
                    } else {
                        listSize = list.size
                        btnLordMore.visibility = View.GONE
                        if (list.size != 0) {
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                        }
                    }
                }
            }
        } else {
            commonClassForAPI!!.getItemBySection(object : DisposableObserver<JsonObject?>() {
                override fun onNext(jsonObject: JsonObject) {
                    try {
                        progressBar.visibility = View.GONE
                        val appHomeItemModel =
                            Gson().fromJson(jsonObject.toString(), AppHomeItemModel::class.java)
                        if (appHomeItemModel.isStatus) {
                            SectionPref.getInstance(activity).putString(
                                SectionPref.PRODUCT_LIST + sectionId,
                                jsonObject.toString()
                            )
                            val itemList = appHomeItemModel.itemListModels
                            val list = appHomeViewModel.getMoqList(itemList)
                            if (list.size != 0) {
                                if (!tileSlider) {
                                    if (list.size > 2) {
                                        listSize = 2
                                        appHomeItemAdapter.setItemListCategory(
                                            list,
                                            listSize
                                        )
                                        btnLordMore.visibility = View.VISIBLE
                                    } else {
                                        listSize = list.size
                                        btnLordMore.visibility = View.GONE
                                        if (list.size != 0) {
                                            appHomeItemAdapter.setItemListCategory(
                                                list,
                                                listSize
                                            )
                                        }
                                    }
                                } else {
                                    listSize = list.size
                                    btnLordMore.visibility = View.GONE
                                    if (list.size != 0) {
                                        appHomeItemAdapter.setItemListCategory(
                                            list,
                                            listSize
                                        )
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    progressBar.visibility = View.GONE
                }
                override fun onComplete() {}
            }, customerId, warehouseId, sectionId, lang)

           /* appHomeViewModel.getItemBySection(customerId, warehouseId, sectionId, lang)
            appHomeViewModel.getItemBySectionData.observe(activity!!) {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        it.data?.let {
                            try {
                                progressBar.visibility = View.GONE
                                val appHomeItemModel =
                                    Gson().fromJson(it.toString(), AppHomeItemModel::class.java)
                                if (appHomeItemModel.isStatus) {
                                    SectionPref.getInstance(activity).putString(
                                        SectionPref.PRODUCT_LIST + sectionId,
                                        it.toString()
                                    )
                                    val itemList = appHomeItemModel.itemListModels
                                    val list = appHomeViewModel.getMoqList(itemList)
                                    if (list.size != 0) {
                                        if (!tileSlider) {
                                            if (list.size > 2) {
                                                listSize = 2
                                                appHomeItemAdapter.setItemListCategory(
                                                    list,
                                                    listSize
                                                )
                                                btnLordMore.visibility = View.VISIBLE
                                            } else {
                                                listSize = list.size
                                                btnLordMore.visibility = View.GONE
                                                if (list.size != 0) {
                                                    appHomeItemAdapter.setItemListCategory(
                                                        list,
                                                        listSize
                                                    )
                                                }
                                            }
                                        } else {
                                            listSize = list.size
                                            btnLordMore.visibility = View.GONE
                                            if (list.size != 0) {
                                                appHomeItemAdapter.setItemListCategory(
                                                    list,
                                                    listSize
                                                )
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Response.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }

            }*/
        }
    }

    override fun getOtherItems(
        vh: RecyclerView.ViewHolder,
        url: String,
        sectionId: Int,
        appHomeItemAdapter: AppHomeItemAdapter,
        tileSlider: Boolean
    ) {
        this.viewHolder = vh
        this.appHomeItemAdapter1 = appHomeItemAdapter
        val data = SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + sectionId)
        if (!TextUtils.isNullOrEmpty(data)) {
            val model = Gson().fromJson(data, AppHomeItemGoldenDealModel::class.java)
            if (model.itemListModels!!.size != 0) {
                val itemList = model.itemListModels
                val list = appHomeViewModel.getMoqList(itemList)
                if (list.size != 0) {
                    if (!tileSlider) {
                        if (list.size >= 4) {
                            listSize = 4
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                            (vh as HomeAdapter.ItemViewHolder).mBinding.btnLoadMore.visibility =
                                View.VISIBLE
                        } else {
                            listSize = list.size
                            (vh as HomeAdapter.ItemViewHolder).mBinding.btnLoadMore.visibility =
                                View.GONE
                            if (list.size != 0) {
                                appHomeItemAdapter.setItemListCategory(list, listSize)
                            }
                        }
                    } else {
                        listSize = list.size
                        appHomeItemAdapter.setItemListCategory(list, listSize)
                    }
                }
            }
        } else {
            (vh as HomeAdapter.ItemViewHolder).mBinding.progressBarCyclic.visibility = View.VISIBLE
            commonClassForAPI!!.getOtherItemsHome(object :
                DisposableObserver<AppHomeItemGoldenDealModel?>() {
                override fun onNext(@NotNull model: AppHomeItemGoldenDealModel) {
                    try {
                        if (model != null && model.itemListModels!!.size > 0) {
                        SectionPref.getInstance(activity).putString(
                            SectionPref.PRODUCT_LIST + sectionId,
                            Gson().toJson(model)
                        )
                        vh.mBinding.progressBarCyclic.visibility = View.GONE
                        val itemList = model.itemListModels
                        val list = appHomeViewModel.getMoqList(itemList)
                        val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                        param.height = LinearLayout.LayoutParams.WRAP_CONTENT
                        param.width = LinearLayout.LayoutParams.MATCH_PARENT
                        vh.itemView.visibility = View.VISIBLE
                        vh.itemView.layoutParams = param
                        if (!tileSlider) {
                            if (list.size >= 4) {
                                listSize = 4
                                appHomeItemAdapter.setItemListCategory(
                                    list,
                                    listSize
                                )
                                vh.mBinding.btnLoadMore.visibility = View.VISIBLE
                            } else {
                                listSize = list.size
                                vh.mBinding.btnLoadMore.visibility = View.GONE
                                if (list.size != 0) {
                                    appHomeItemAdapter.setItemListCategory(
                                        list,
                                        listSize
                                    )
                                }
                            }
                        } else {
                            listSize = list.size
                            vh.mBinding.btnLoadMore.visibility = View.GONE
                            if (list.size != 0) {
                                appHomeItemAdapter.setItemListCategory(
                                    list,
                                    listSize
                                )
                            }
                        }}else{
                            val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                            vh.itemView.visibility = View.GONE
                            param.height = 0
                            param.width = 0
                            vh.itemView.layoutParams = param
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // hide recycler item
                        val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                        vh.itemView.visibility = View.GONE
                        param.height = 0
                        param.width = 0
                        vh.itemView.layoutParams = param
                    }
                }
                override fun onError(@NotNull e: Throwable) {
                    vh.mBinding.progressBarCyclic.visibility = View.GONE
                    val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                    vh.itemView.visibility = View.GONE
                    param.height = 0
                    param.width = 0
                    vh.itemView.layoutParams = param
                }
                override fun onComplete() {}
            }, url)

           /* appHomeViewModel.getOtherItemsHome(url)
            appHomeViewModel.getOtherItemsData.observe(activity!!) {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        it.data?.let {
                            try {
                                SectionPref.getInstance(activity).putString(
                                    SectionPref.PRODUCT_LIST + sectionId,
                                    Gson().toJson(it)
                                )
                                (viewHolder as HomeAdapter.ItemViewHolder).mBinding.progressBarCyclic.visibility = View.GONE
                                val itemList = it.itemListModels
                                val list = appHomeViewModel.getMoqList(itemList!!)
                                val param = viewHolder!!.itemView.layoutParams as RecyclerView.LayoutParams
                                param.height = LinearLayout.LayoutParams.WRAP_CONTENT
                                param.width = LinearLayout.LayoutParams.MATCH_PARENT
                                viewHolder!!.itemView.visibility = View.VISIBLE
                                viewHolder!!.itemView.layoutParams = param
                                if (!tileSlider) {
                                    if (list.size >= 4) {
                                        listSize = 4
                                        appHomeItemAdapter1!!.setItemListCategory(
                                            list,
                                            listSize
                                        )
                                        (viewHolder as HomeAdapter.ItemViewHolder).mBinding.btnLoadMore.visibility = View.VISIBLE
                                    } else {
                                        listSize = list.size
                                        (viewHolder as HomeAdapter.ItemViewHolder).mBinding.btnLoadMore.visibility = View.GONE
                                        if (list.size != 0) {
                                            appHomeItemAdapter1!!.setItemListCategory(
                                                list,
                                                listSize
                                            )
                                        }
                                    }
                                } else {
                                    listSize = list.size
                                    (viewHolder as HomeAdapter.ItemViewHolder).mBinding.btnLoadMore.visibility = View.GONE
                                    if (list.size != 0) {
                                        appHomeItemAdapter1!!.setItemListCategory(
                                            list,
                                            listSize
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                // hide recycler item
                                val param = (viewHolder as HomeAdapter.ItemViewHolder).itemView.layoutParams as RecyclerView.LayoutParams
                                (viewHolder as HomeAdapter.ItemViewHolder).itemView.visibility = View.GONE
                                param.height = 0
                                param.width = 0
                                (viewHolder as HomeAdapter.ItemViewHolder).itemView.layoutParams = param
                            }
                        }
                    }
                    is Response.Error -> {
                        (viewHolder as HomeAdapter.ItemViewHolder).mBinding.progressBarCyclic.visibility = View.GONE
                        val param = (viewHolder as HomeAdapter.ItemViewHolder).itemView.layoutParams as RecyclerView.LayoutParams
                        (viewHolder as HomeAdapter.ItemViewHolder).itemView.visibility = View.GONE
                        param.height = 0
                        param.width = 0
                        (viewHolder as HomeAdapter.ItemViewHolder).itemView.layoutParams = param
                    }
                }
            }*/
        }
    }

    override fun callStoreApi(
        holder: RecyclerView.ViewHolder,
        sItemAdapter: StoreItemAdapter,
        url: String
    ) {
        val data = SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + url)
        if (!TextUtils.isNullOrEmpty(data)) {
            val storeList = Gson().fromJson<ArrayList<StoreItemModel>>(
                data,
                object : TypeToken<ArrayList<StoreItemModel?>?>() {}.type
            )
            if (!storeList.isEmpty()) {
                sItemAdapter.setData(storeList)
            }
        } else {
            commonClassForAPI!!.getAllStore(object :
                DisposableObserver<ArrayList<StoreItemModel>>() {
                override fun onNext(storeList: ArrayList<StoreItemModel>) {
                    try {
                        if (storeList.size!=0) {
                            SectionPref.getInstance(activity).putString(
                                SectionPref.PRODUCT_LIST
                                        + url, Gson().toJson(storeList)
                            )
                            sItemAdapter.setData(storeList)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            }, customerId, warehouseId, lang)

         /*   appHomeViewModel.getAllStore(customerId, warehouseId, lang)
            appHomeViewModel.getAllStoreData.observe(activity!!) {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        it.data?.let {
                            SectionPref.getInstance(activity).putString(
                                SectionPref.PRODUCT_LIST
                                        + url, Gson().toJson(it)
                            )
                            sItemAdapter.setData(it)
                        }
                    }

                    is Response.Error -> {
                    }
                }

            }*/

        }
    }

    override fun callBucketGameApi(
        holder: RecyclerView.ViewHolder,
        bucketGameViewPagerAdapter: BucketGameViewPagerAdapter,
        url: String
    ) {
        appHomeViewModel.getGameBanners(customerId)
        appHomeViewModel.getGameBannersData.observe(activity!!) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {
                        val gameBannerList = ArrayList<GameBannerModel>()
                        gameBannerList.add(it)
                        bucketGameViewPagerAdapter.setData(gameBannerList)
                    }
                }

                is Response.Error -> {
                }
            }
        }
    }

    private fun initialization() {
        // view
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        customerId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        lang = LocaleHelper.getLanguage(activity)
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(activity)
        mBinding!!.rvHome.layoutManager = layoutManager
        mBinding!!.thisSiteIsUnderMaintenance.text =
            MyApplication.getInstance().dbHelper.getString(R.string.this_site_is_under_maintenance)
        mBinding!!.weArePreparingToServeYouBetter.text =
            MyApplication.getInstance().dbHelper.getString(R.string.we_are_preparing_to_serve_you_better)
        mHomeAdapter = HomeAdapter(
            activity!!, homeDataList, this,
            this, this, this, this
        )
        mBinding!!.rvHome.adapter = mHomeAdapter
        mBinding!!.swipeContainer.setColorSchemeResources(R.color.colorAccent)
        mBinding!!.swipeContainer.setOnRefreshListener {
            SectionPref.getInstance(activity).clear()
            callApi()
        }
        mBinding!!.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mBinding!!.swipeContainer.isEnabled =
                    layoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        if (activity != null && isAdded) {
            callApi()
        }
    }

    private fun callApi() {
        if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_REQUIRED_LOCATION)) {
            cancellationTokenSource = CancellationTokenSource()
            // check GPS
            val locationManager =
                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val client = LocationServices.getFusedLocationProviderClient(
                    activity!!
                )
                client.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource!!.token
                )
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            lat = location.latitude
                            lng = location.longitude
                        } else {
                            val gpsTracker = GPSTracker(activity)
                            lat = gpsTracker.latitude
                            lng = gpsTracker.longitude
                        }
                        callAppHome()
                    }.addOnCanceledListener { callAppHome() }
                    .addOnFailureListener { e: Exception? -> callAppHome() }
            } else {
                GpsUtils(activity).turnGPSOn { isGPSEnable: Boolean -> callApi() }
                callAppHome()
            }
        } else {
            if (SectionPref.getInstance(activity).getString(SectionPref.APP_HOME) != null) {
                homeDataList = Gson().fromJson(
                    SectionPref.getInstance(activity)
                        .getString(SectionPref.APP_HOME),
                    object : TypeToken<ArrayList<HomeDataModel?>?>() {}.type
                )
                mHomeAdapter!!.setHomeAdapter(homeDataList)
            } else {
                callAppHome()
            }
        }
    }

    private fun callAppHome() {
        appHomeViewModel.getAppHomeSection("Retailer App", customerId, warehouseId, lang, lat, lng)
        if (lat != 0.0) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_REQUIRED_LOCATION, false)
        }
    }

    private fun callFlashDealApi(id: String) {
        appHomeViewModel.getFlashDealItem(customerId, warehouseId, id, lang, "Home Flash Deal")
    }

    /**
     * get accept assigned condition
     */
    private fun handleEndFlashDeal() {
        acceptFlashEndTimeDes = RxBus.getInstance().flashDealEndEvent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o: EndFlashDealModel ->
                Logger.logD(TAG, "End Flash deal time")
                if (o.isStatus) {
                    callApi()
                    acceptFlashEndTimeDes!!.dispose()
                }
            }
    }

    /**
     * get accept assigned condition
     */
    private fun handleStartFlashDeal(sectionId: String) {
        acceptFlashStartTimeDes = RxBus.getInstance().flashDealStartEvent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o: EndFlashDealModel ->
                Logger.logD(TAG, "Start Flash deal time")
                if (o.isStatus) {
                    callFlashDealApi(sectionId)
                    acceptFlashStartTimeDes!!.dispose()
                }
            }
    }

    private fun showDialog(model: HomeDataModel) {
        try {
            if (!TextUtils.isNullOrEmpty(
                    model.appItemsList!![0].bannerImage
                )
            ) {
                val dialog = Dialog(activity!!)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.setContentView(R.layout.dialog_offer)
                val imageView = dialog.findViewById<ImageView>(R.id.iv_image)
                dialog.findViewById<View>(R.id.iv_close)
                    .setOnClickListener { dialog.dismiss() }
                Picasso.get().load(model.appItemsList!![0].bannerImage).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                        try {
                            dialog.show()
                            imageView.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        SharePrefs.getInstance(activity)
                            .putBoolean(SharePrefs.IS_DIALOG_SHOWN, true)
                        MyApplication.getInstance().updateAnalytics("popup_view")
                    }

                    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                        e.printStackTrace()
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
                imageView.setOnClickListener {
                    dialog.dismiss()
                    clickActionPerform(model.sectionSubType, model, model.appItemsList!![0])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickActionPerform(
        actionType: String,
        model: HomeDataModel,
        appItemModel: AppItemsList
    ) {
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
                "" + SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER)
            )
            model.webViewUrl = url
        }
        when (actionType) {
            "Base Category" -> {
                if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                        "webView",
                        ignoreCase = true
                    )
                ) {
                    if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                        if (EndPointPref.getInstance(activity)
                                .getBoolean(EndPointPref.showNewSocial)
                        ) startActivity(
                            Intent(
                                activity,
                                FeedActivity::class.java
                            )
                        ) else startActivity(Intent(activity, TradeActivity::class.java))
                        Utils.leftTransaction(activity)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("url", model.webViewUrl)
                        activity!!.startActivity(
                            Intent(
                                activity,
                                WebViewActivity::class.java
                            ).putExtras(bundle)
                        )
                    }
                } else {
                    args.putString("cetegoryTittle", appItemModel.tileName)
                    activity!!.pushFragments(SubCategoryFragment.newInstance(), false, true, args)
                }
                // update analytics
                MyApplication.getInstance().updateAnalytics("appHome_basCat_click", analyticPost)
            }

            "Brand" -> if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                    "webView",
                    ignoreCase = true
                )
            ) {
                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                    if (EndPointPref.getInstance(activity)
                            .getBoolean(EndPointPref.showNewSocial)
                    ) startActivity(Intent(activity, FeedActivity::class.java)) else startActivity(
                        Intent(activity, TradeActivity::class.java)
                    )
                    Utils.leftTransaction(activity)
                } else if (model.webViewUrl!!.startsWith("vAtm") || model.webViewUrl!!.startsWith("vatm")) {
                    activity!!.callVAtmApi()
                } else if (model.webViewUrl!!.contains("Udhar/GenerateLead")) {
                    activity!!.callLeadApi(model.webViewUrl!!)
                } else {
                    val bundle = Bundle()
                    bundle.putString("url", model.webViewUrl)
                    activity!!.startActivity(
                        Intent(
                            activity,
                            WebViewActivity::class.java
                        ).putExtras(bundle)
                    )
                }
            } else {
                if (!appItemModel.redirectionID.toString()
                        .equals("0", ignoreCase = true) && appItemModel.redirectionID != 0
                ) {
                    activity!!.pushFragments(
                        SubSubCategoryFragment.newInstance(),
                        false,
                        true,
                        args
                    )
                } else {
                    args.putString("BRAND_NAME", "Brand")
                    activity!!.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args)
                }
            }

            "Category" -> {
                args.putBoolean("HOME_FLAG", false)
                if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                        "webView",
                        ignoreCase = true
                    )
                ) {
                    if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                        if (EndPointPref.getInstance(activity)
                                .getBoolean(EndPointPref.showNewSocial)
                        ) startActivity(
                            Intent(
                                activity,
                                FeedActivity::class.java
                            )
                        ) else startActivity(Intent(activity, TradeActivity::class.java))
                        Utils.leftTransaction(activity)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("url", model.webViewUrl)
                        activity!!.startActivity(
                            Intent(
                                activity,
                                WebViewActivity::class.java
                            ).putExtras(bundle)
                        )
                    }
                } else {
                    if (appItemModel.redirectionID != 0) {
                        activity!!.pushFragments(
                            SubSubCategoryFragment.newInstance(),
                            false,
                            true,
                            args
                        )
                    } else {
                        args.putString("BRAND_NAME", "Brand")
                        activity!!.pushFragments(
                            ShopbyBrandFragment.newInstance(),
                            false,
                            true,
                            args
                        )
                    }
                }
            }

            "SubCategory" -> if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                    "webView",
                    ignoreCase = true
                )
            ) {
                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                    if (EndPointPref.getInstance(activity)
                            .getBoolean(EndPointPref.showNewSocial)
                    ) startActivity(Intent(activity, FeedActivity::class.java)) else startActivity(
                        Intent(activity, TradeActivity::class.java)
                    )
                    Utils.leftTransaction(activity)
                } else if (model.webViewUrl!!.startsWith("ShowStoreFlashDeal")) {
                    val id = model.appItemsList!![0].redirectionID
                    args = Bundle()
                    args.putString("SECTION_ID", "-1")
                    args.putInt("subCategoryId", id)
                    args.putBoolean("isStore", true)
                    activity!!.pushFragments(FlashDealOfferFragment.newInstance(), true, true, args)
                } else {
                    val bundle = Bundle()
                    bundle.putString("url", model.webViewUrl)
                    activity!!.startActivity(
                        Intent(
                            activity,
                            WebViewActivity::class.java
                        ).putExtras(bundle)
                    )
                }
            } else {
                if (!appItemModel.redirectionID.toString()
                        .equals("0", ignoreCase = true) && appItemModel.redirectionID != 0
                ) {
                    args.putString("ITEM_IMAGE", appItemModel.tileImage)
                    activity!!.pushFragments(
                        HomeSubCategoryFragment.newInstance(),
                        false,
                        true,
                        args
                    )
                } else {
                    args.putString("BRAND_NAME", "Brand")
                    activity!!.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args)
                }
            }

            "Other" -> if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                    "webView",
                    ignoreCase = true
                )
            ) {
                if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                    if (EndPointPref.getInstance(activity)
                            .getBoolean(EndPointPref.showNewSocial)
                    ) startActivity(Intent(activity, FeedActivity::class.java)) else startActivity(
                        Intent(activity, TradeActivity::class.java)
                    )
                    Utils.leftTransaction(activity)
                } else {
                    val bundle = Bundle()
                    bundle.putString("url", model.webViewUrl)
                    activity!!.startActivity(
                        Intent(
                            activity,
                            WebViewActivity::class.java
                        ).putExtras(bundle)
                    )
                }
            } else {
                callActivities(appItemModel.bannerActivity ?: "", appItemModel.redirectionUrl)
            }

            else -> {
                if (model.viewType != null && !model.viewType!!.isEmpty() && model.viewType.equals(
                        "webView",
                        ignoreCase = true
                    )
                ) {
                    if (model.webViewUrl!!.startsWith("https://trade.er15.xyz:4436")) {
                        if (EndPointPref.getInstance(activity)
                                .getBoolean(EndPointPref.showNewSocial)
                        ) startActivity(
                            Intent(
                                activity,
                                FeedActivity::class.java
                            )
                        ) else startActivity(Intent(activity, TradeActivity::class.java))
                        Utils.leftTransaction(activity)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("url", model.webViewUrl)
                        activity!!.startActivity(
                            Intent(
                                activity,
                                WebViewActivity::class.java
                            ).putExtras(bundle)
                        )
                    }
                } else {
                    if (!appItemModel.redirectionID.toString()
                            .equals("0", ignoreCase = true) && appItemModel.redirectionID != 0
                    ) {
                        activity!!.pushFragments(
                            SubSubCategoryFragment.newInstance(),
                            false,
                            true,
                            args
                        )
                    } else {
                        args.putString("BRAND_NAME", "Brand")
                        activity!!.pushFragments(
                            ShopbyBrandFragment.newInstance(),
                            false,
                            true,
                            args
                        )
                    }
                }
                // update analytics
                MyApplication.getInstance().updateAnalytics("popup_click", analyticPost)
            }
        }
    }

    private fun callActivities(bannerActivity: String?, url: String?) {
        var i: Intent? = null
        if (bannerActivity.equals("games", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, GamesListActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("target", ignoreCase = true)) {
            activity!!.startActivity(
                Intent(
                    activity,
                    CustomerSubCategoryTargetActivity::class.java
                )
            )
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("prime", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("shoppingcart", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, ShoppingCartActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("wallet", ignoreCase = true)) {
            i = Intent(activity, MyWalletActivity::class.java)
            activity!!.startActivity(i)
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("category", ignoreCase = true)) {
            activity!!.pushFragments(
                HomeCategoryFragment(),
                false,
                true,
                null
            )
        } else if (bannerActivity.equals("tradeoffer", ignoreCase = true)) {
            activity!!.pushFragments(
                TradeOfferFragment.newInstance(),
                false,
                true,
                null
            )
        } else if (bannerActivity.equals("allbrands", ignoreCase = true)) {
            activity!!.pushFragments(
                AllBrandFragItemList.newInstance(),
                true,
                true,
                null
            )
        } else if (bannerActivity.equals("freebies", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, FreebiesOfferActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("myorder", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, MyOrderActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("direct", ignoreCase = true)) {
            if (EndPointPref.getInstance(activity)
                    .getBoolean(EndPointPref.showNewSocial)
            ) startActivity(Intent(activity, FeedActivity::class.java)) else startActivity(
                Intent(
                    activity,
                    TradeActivity::class.java
                )
            )
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("Clearance", ignoreCase = true)) {
            activity!!.startActivity(Intent(activity, ClearanceActivity::class.java))
            Utils.fadeTransaction(activity)
        } else if (bannerActivity.equals("ExternalURL", ignoreCase = true)) {
            try {
                val uri = Uri.parse(url) // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                activity!!.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Utils.fadeTransaction(activity)
        }
    }

    private fun saveBaseCat(HomeDataList: ArrayList<HomeDataModel>) {
        val baseCatList = ArrayList<HomeDataModel>()
        for (i in HomeDataList.indices) {
            if (HomeDataList[i].sectionSubType.equals("Base Category", ignoreCase = true)) {
                baseCatList.add(HomeDataList[i])
            }
        }
        SharePrefs.setStringSharedPreference(
            activity,
            SharePrefs.BASE_CAT,
            Gson().toJson(baseCatList)
        )
    }

    // add Cart To AppHome
    private fun addCartToList() {
        if (homeDataList.size > 1 && EndPointPref.getInstance(activity).cartPosition != 0) {
            val cartData = MyApplication.getInstance().noteRepository.cart
            val observer: Observer<List<ItemListModel>> = object : Observer<List<ItemListModel>> {
                override fun onChanged(list: List<ItemListModel>) {
                    println("addCartToList ")
                    if (list != null && list.size > 0) {
                        var total = MyApplication.getInstance().noteRepository.cartValue1
                        if (total == null) {
                            total = 0.0
                        }
                        homeDataList.add(
                            EndPointPref.getInstance(activity).cartPosition, HomeDataModel(
                                "cart",
                                MyApplication.getInstance().dbHelper.getString(R.string.resume_your_purchase),
                                list[0].itemname,
                                list[0].unitPrice,
                                list[0].price,
                                list[0].logoUrl,
                                total,
                                list.size
                            )
                        )
                        mHomeAdapter!!.notifyDataSetChanged()
                    }
                    cartData.removeObserver(this)
                }
            }
            cartData.observe(activity!!, observer)
        }
    }

    // update Cart In AppHome
    private fun updateCartInList() {
        if (homeDataList.size > 1 && EndPointPref.getInstance(activity).cartPosition != 0) {
            val cartData = MyApplication.getInstance().noteRepository.cart
            val observer: Observer<List<ItemListModel>> = object : Observer<List<ItemListModel>> {
                override fun onChanged(list: List<ItemListModel>) {
                    println("updateCartInList")
                    if (list != null && list.size > 0) {
                        var total = MyApplication.getInstance().noteRepository.cartValue1
                        if (total == null) {
                            total = 0.0
                        }
                        homeDataList[EndPointPref.getInstance(activity).cartPosition] =
                            HomeDataModel(
                                "cart",
                                MyApplication.getInstance().dbHelper.getString(R.string.resume_your_purchase),
                                list[0].itemname,
                                list[0].unitPrice,
                                list[0].price,
                                list[0].logoUrl,
                                total,
                                list.size
                            )
                    } else {
                        if (homeDataList.size > 2) homeDataList.removeAt(2)
                    }
                    mHomeAdapter!!.notifyDataSetChanged()
                    cartData.removeObserver(this)
                }
            }
            cartData.observe(activity!!, observer)
        }
    }

    // add Recent Search Items To AppHome
    private fun addRecentSearchToList() {
        if (EndPointPref.getInstance(activity).searchPosition != 0) {
            val searchData = MyApplication.getInstance().noteRepository.searchItem
            val searchObserver: Observer<List<SearchItemDTO>> =
                object : Observer<List<SearchItemDTO>> {
                    override fun onChanged(searchList: List<SearchItemDTO>) {
                        if (searchList != null && searchList.size > 0) {
                            val itemsArrayList = ArrayList<AppItemsList>()
                            for (dto in searchList) {
                                val itemsModel = AppItemsList(
                                    99,
                                    dto.logoUrl, "", dto.itemName,
                                    dto.query, dto.unitPrice, dto.price
                                )
                                itemsArrayList.add(itemsModel)
                            }
                            homeDataList.add(
                                HomeDataModel(
                                    "search",
                                    MyApplication.getInstance().dbHelper.getString(R.string.continue_your_search),
                                    searchList[0].itemName,
                                    searchList[0].unitPrice,
                                    searchList[0].price,
                                    searchList[0].logoUrl,
                                    99,
                                    searchList.size,
                                    searchList[0].query,
                                    itemsArrayList
                                )
                            )
                            mHomeAdapter!!.notifyDataSetChanged()
                        }
                        searchData.removeObserver(this)
                    }
                }
            searchData.observe(activity!!, searchObserver)
        }
    }

    private fun handleAppHomeSectionResult(it: Response<ArrayList<HomeDataModel>>) {
        when (it) {
            is Response.Loading -> {
                //Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                it.data?.let {
                    //  Utils.hideProgressDialog()
                    mBinding!!.rvHome.visibility = View.VISIBLE
                    mBinding!!.liEmpty.visibility = View.GONE
                    mBinding!!.swipeContainer.isRefreshing = false
                    Collections.sort(it)
                    saveBaseCat(it)
                    homeDataList.clear()
                    homeDataList.addAll(it)
                    if (homeDataList.size != 0) {
                        var pos = 0
                        for (i in homeDataList.indices) {
                            if (!SharePrefs.getInstance(activity)
                                    .getBoolean(SharePrefs.IS_DIALOG_SHOWN)
                            ) {
                                if (homeDataList[i].isPopUp) {
                                    showDialog(homeDataList[i])
                                }
                            }
                            if (homeDataList[i].sectionName != null && homeDataList[i].sectionName == sectionName) {
                                pos = i
                                sectionName = null
                            }
                            if (homeDataList[i].isPopUp) {
                                homeDataList.removeAt(i)
                                break
                            }
                        }
                        mBinding!!.rvHome.setItemViewCacheSize(homeDataList.size)
                        mHomeAdapter!!.setHomeAdapter(homeDataList)
                        if (pos != 0) {
                            mBinding!!.rvHome.scrollToPosition(pos)
                            val finalPos = pos
                            Handler(Looper.getMainLooper()).postDelayed({
                                activity!!.runOnUiThread {
                                    mBinding!!.rvHome.scrollToPosition(
                                        finalPos
                                    )
                                }
                            }, 900)
                        }
                        addCartToList()
                        addRecentSearchToList()
                        SectionPref.getInstance(activity)
                            .putString(SectionPref.APP_HOME, Gson().toJson(it))

                    }
                }
            }

            is Response.Error -> {
                //  Utils.hideProgressDialog()
                mBinding!!.liEmpty.visibility = View.VISIBLE
                mBinding!!.rvHome.visibility = View.GONE
                mBinding!!.swipeContainer.isRefreshing = false
            }
        }
    }

    private fun handleFlashDealItemResult(it: Response<HomeOfferFlashDealModel>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        progressBar!!.visibility = View.GONE
                        mFlashHome = true
                        val itemListModels = it.itemListModels
                        val jsonObjectFlashUsed = JSONObject()
                        for (itemListModel in itemListModels!!) {
                            if (itemListModel.isFlashDealUsed) {
                                jsonObjectFlashUsed.put("" + itemListModel.itemId, "1")
                            }
                        }
                        SharePrefs.setStringSharedPreference(
                            activity,
                            SharePrefs.ITEM_FLASH_DEAL_USED_JSON,
                            jsonObjectFlashUsed.toString()
                        )
                        if (itemListModels.size > 2) {
                            llLoadMore!!.visibility = View.VISIBLE
                            listSizeFlashDeal = 2
                        } else {
                            listSizeFlashDeal = itemListModels.size
                            llLoadMore!!.visibility = View.GONE
                        }
                        mFlashDealItemListAdapter!!.setItemListCategory(
                            itemListModels,
                            listSizeFlashDeal
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                rlHeader!!.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance(sectionName: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("sectionName", sectionName)
            fragment.arguments = args
            return fragment
        }
    }
}