package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHomeSubCategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.DynamicHtmlInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.FlashDealsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.ItemsOfferInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SubCategoryItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.EndFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StorePref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeViewModelFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.FlashDealItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.viewHolder.DynamicViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class StoreHomeFragment : Fragment(), FlashDealsOfferInterface, ItemsOfferInterface,
    DynamicHtmlInterface {
    private lateinit var appCtx: RetailerSDKApp
    private val TAG = this.javaClass.name
    private var rootView: View? = null
    private var mBinding: FragmentHomeSubCategoryBinding? = null
    private lateinit var appHomeViewModel: AppHomeViewModel
    private var activity: HomeActivity? = null
    private val mFlashDealArrayList = ArrayList<ItemListModel>()
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: SubCategoryItemAdapter? = null
    private var dashboardAdapter: StoreDashAdapter? = null
    private var mFlashDealItemListAdapter: FlashDealItemListAdapter? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var acceptFlashEndTimeDes: Disposable? = null
    private var acceptFlashStartTimeDes: Disposable? = null
    private var lang = ""
    private var itemImage: String? = ""
    private var baseCatId = 0
    private var categoryId = 0
    private var subCatId = 0
    private var subSubCattId = 0
    private var warehouseId = 0
    private var listSize = 0
    private var listSizeFlashDeal = 0
    private var mSectionType: String? = ""
    private var customerId = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as RetailerSDKApp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            baseCatId = bundle.getInt("BaseCategoryId")
            categoryId = bundle.getInt("CATEGORY_ID")
            subCatId = bundle.getInt("SUB_CAT_ID")
            subSubCattId = bundle.getInt("SUB_SUB_CAT_ID")
            itemImage = bundle.getString("ITEM_IMAGE")
            mSectionType = bundle.getString("SectionType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home_sub_category,
                container,
                false
            )
            val appRepository = AppRepository(RetailerSDKApp.application)
            appHomeViewModel =
                ViewModelProvider(
                    activity!!,
                    AppHomeViewModelFactory(RetailerSDKApp.application, appRepository)
                )[AppHomeViewModel::class.java]
            rootView = mBinding!!.root

            observe(appHomeViewModel.getStoreHomeData, ::handleStoreDashboardResult)
            observe(appHomeViewModel.getSubCategoryData, ::handleSubCategoryResult)
            observe(appHomeViewModel.getSubCategoryOfferData, ::handleSubCategoryOfferResult)

            // init view
            initialization()
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE

        // analytics data
        val analyticPost = AnalyticPost()
        analyticPost.baseCatId = "" + baseCatId
        analyticPost.categoryId = categoryId
        analyticPost.subCatId = subCatId
        analyticPost.subSubCatId = subSubCattId
        RetailerSDKApp.getInstance().updateAnalytics("storeView", analyticPost)
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        if (dashboardAdapter != null && dashboardAdapter!!.itemCount > 0) {
            dashboardAdapter!!.notifyDataSetChanged()
        }
        if (mFlashDealItemListAdapter != null && mFlashDealItemListAdapter!!.itemCount > 0) {
            mFlashDealItemListAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (acceptFlashEndTimeDes != null) {
            acceptFlashEndTimeDes!!.dispose()
        }
        if (acceptFlashStartTimeDes != null) {
            acceptFlashStartTimeDes!!.dispose()
        }
    }

    // flash deal offer
    override fun flashDealsOffer(
        vh: RecyclerView.ViewHolder?,
        sectionId: String?,
        flashDealBackImages: String?
    ) {
        val llLoadMore = (vh as StoreDashAdapter.RecyclerViewHolder).mBinding.llLoadItem
        val progressBar = vh.mBinding.progressBarCyclic
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
        vh.mBinding.rvItems.adapter = mFlashDealItemListAdapter
        progressBar.visibility = View.VISIBLE
        callFlashDealApi(sectionId!!, llLoadMore, progressBar)
        handleEndFlashDeal()
        handleStartFlashDeal(sectionId)
    }

    override fun loadDynamicHtml(holder: RecyclerView.ViewHolder, position: Int, url: String) {
        val data = StorePref.getInstance(activity).getString(StorePref.PRODUCT_LIST + url)
        if (data != null) {
            (holder as StoreDashAdapter.DynamicViewHolder).mBinding.webviewItem.visibility =
                View.VISIBLE
            val webView = holder.mBinding.webviewItem
            webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null)
        } else {
            CommonClassForAPI.getInstance(activity)
                .fetchDynamicHtml(object : DisposableObserver<String?>() {
                    override fun onNext(response: String) {
                        if (!TextUtils.isNullOrEmpty(response)) {
                            try {
                                (holder as StoreDashAdapter.DynamicViewHolder).mBinding.webviewItem.visibility =
                                    View.VISIBLE
                                val webView = holder.mBinding.webviewItem
                                webView.loadDataWithBaseURL(
                                    null,
                                    response,
                                    "text/html",
                                    "UTF-8",
                                    null
                                )
                                StorePref.getInstance(activity)
                                    .putString(StorePref.PRODUCT_LIST + url, response)
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

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        (holder as DynamicViewHolder).mBinding.webviewItem.visibility = View.GONE
                    }

                    override fun onComplete() {}
                }, url)

            /*  appHomeViewModel.getDynamicHtm(url)
              appHomeViewModel.getDynamicHtmlData.observe(activity!!) {
                  when (it) {
                      is Response.Loading -> {}
                      is Response.Success -> {
                          it.data?.let {
                              if (!TextUtils.isNullOrEmpty(it)) {
                                  try {
                                      (holder as StoreDashAdapter.DynamicViewHolder).mBinding.webviewItem.visibility =
                                          View.VISIBLE
                                      val webView = holder.mBinding.webviewItem
                                      webView.loadDataWithBaseURL(
                                          null,
                                          it,
                                          "text/html",
                                          "UTF-8",
                                          null
                                      )
                                      StorePref.getInstance(activity)
                                          .putString(StorePref.PRODUCT_LIST + url, it)
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
        llLordMore: Button,
        appHomeItemAdapter: AppHomeItemAdapter,
        tileSlider: Boolean
    ) {
        if (StorePref.getInstance(activity).getString(StorePref.PRODUCT_LIST + sectionId) != null) {
            val model = Gson().fromJson(
                StorePref.getInstance(activity)
                    .getString(StorePref.PRODUCT_LIST + sectionId), AppHomeItemModel::class.java
            )
            progressBar.visibility = View.GONE
            if (model.isStatus) {
                val itemList = model.itemListModels
                val list = appHomeViewModel.getMoqList(itemList)
                if (list.size != 0) {
                    if (!tileSlider) {
                        if (list.size > 2) {
                            listSize = 2
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                            llLordMore.visibility = View.VISIBLE
                        } else {
                            listSize = list.size
                            llLordMore.visibility = View.GONE
                            if (list.size != 0) {
                                appHomeItemAdapter.setItemListCategory(list, listSize)
                            }
                        }
                    } else {
                        listSize = list.size
                        llLordMore.visibility = View.GONE
                        if (list.size != 0) {
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                        }
                    }
                }
            }
        } else {

            CommonClassForAPI.getInstance(activity)
                .getItemBySection(object : DisposableObserver<JsonObject?>() {
                    override fun onNext(jsonObject: JsonObject) {
                        try {
                            progressBar.visibility = View.GONE
                            val appHomeItemModel =
                                Gson().fromJson(jsonObject.toString(), AppHomeItemModel::class.java)
                            if (appHomeItemModel.isStatus) {
                                StorePref.getInstance(activity).putString(
                                    StorePref.PRODUCT_LIST + sectionId,
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
                                            llLordMore.visibility = View.VISIBLE
                                        } else {
                                            listSize = list.size
                                            llLordMore.visibility = View.GONE
                                            if (list.size != 0) {
                                                appHomeItemAdapter.setItemListCategory(
                                                    list,
                                                    listSize
                                                )
                                            }
                                        }
                                    } else {
                                        listSize = list.size
                                        llLordMore.visibility = View.GONE
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

            /*    appHomeViewModel.getItemBySection(customerId, warehouseId, sectionId, lang)
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
                                        StorePref.getInstance(activity).putString(
                                            StorePref.PRODUCT_LIST + sectionId,
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
                                                    llLordMore.visibility = View.VISIBLE
                                                } else {
                                                    listSize = list.size
                                                    llLordMore.visibility = View.GONE
                                                    if (list.size != 0) {
                                                        appHomeItemAdapter.setItemListCategory(
                                                            list,
                                                            listSize
                                                        )
                                                    }
                                                }
                                            } else {
                                                listSize = list.size
                                                llLordMore.visibility = View.GONE
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
        pos: Int,
        appHomeItemAdapter: AppHomeItemAdapter,
        tileSlider: Boolean
    ) {
        val data = StorePref.getInstance(activity).getString(StorePref.PRODUCT_LIST + url)
        if (!TextUtils.isNullOrEmpty(data)) {
            val model = Gson().fromJson(data, AppHomeItemGoldenDealModel::class.java)
            if (model.itemListModels!!.size != 0) {
                val itemList = model.itemListModels
                val list = appHomeViewModel.getMoqList(itemList)
                if (!tileSlider) {
                    if (list.size > 4) {
                        listSize = 4
                        appHomeItemAdapter.setItemListCategory(list, listSize)
                    } else {
                        listSize = list.size
                        if (list.size != 0) {
                            appHomeItemAdapter.setItemListCategory(list, listSize)
                        }
                    }
                } else {
                    listSize = list.size
                    if (list.size != 0) {
                        appHomeItemAdapter.setItemListCategory(list, listSize)
                    }
                }
            }
        } else {
            (vh as StoreDashAdapter.ItemViewHolder).mBinding.progressBarCyclic.visibility =
                View.VISIBLE
            CommonClassForAPI.getInstance(activity).getOtherItemsHome(object :
                DisposableObserver<AppHomeItemGoldenDealModel?>() {
                override fun onNext(@NotNull model: AppHomeItemGoldenDealModel) {
                    try {
                        if (model != null && model.itemListModels!!.size > 0) {
                            StorePref.getInstance(activity)
                                .putString(StorePref.PRODUCT_LIST + url, Gson().toJson(model))
                            vh.mBinding.progressBarCyclic.visibility = View.GONE
                            val itemList = model.itemListModels
                            val list = appHomeViewModel.getMoqList(itemList)
                            // show recycler item
                            val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                            param.height = LinearLayout.LayoutParams.WRAP_CONTENT
                            param.width = LinearLayout.LayoutParams.MATCH_PARENT
                            vh.itemView.visibility = View.VISIBLE
                            vh.itemView.layoutParams = param
                            if (!tileSlider) {
                                if (list.size >= 4) {
                                    listSize = 4
                                    appHomeItemAdapter.setItemListCategory(list, listSize)
                                    vh.mBinding.llLoadItem.visibility = View.VISIBLE
                                } else {
                                    listSize = list.size
                                    vh.mBinding.llLoadItem.visibility = View.GONE
                                    if (list.size != 0) {
                                        appHomeItemAdapter.setItemListCategory(list, listSize)
                                    }
                                }
                            } else {
                                listSize = list.size
                                vh.mBinding.llLoadItem.visibility = View.GONE
                                if (list.size != 0) {
                                    appHomeItemAdapter.setItemListCategory(list, listSize)
                                }
                            }
                        } else {
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
            /*appHomeViewModel.getOtherItemsHome(url)
            appHomeViewModel.getOtherItemsData.observe(activity!!) {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        it.data?.let {
                            try {
                                StorePref.getInstance(activity)
                                    .putString(StorePref.PRODUCT_LIST + url, Gson().toJson(it))
                                vh.mBinding.progressBarCyclic.visibility = View.GONE
                                val itemList = it.itemListModels
                                val list = appHomeViewModel.getMoqList(itemList!!)
                                // show recycler item
                                val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                                param.height = LinearLayout.LayoutParams.WRAP_CONTENT
                                param.width = LinearLayout.LayoutParams.MATCH_PARENT
                                vh.itemView.visibility = View.VISIBLE
                                vh.itemView.layoutParams = param
                                if (!tileSlider) {
                                    if (list.size >= 4) {
                                        listSize = 4
                                        appHomeItemAdapter.setItemListCategory(list, listSize)
                                        vh.mBinding.llLoadItem.visibility = View.VISIBLE
                                    } else {
                                        listSize = list.size
                                        vh.mBinding.llLoadItem.visibility = View.GONE
                                        if (list.size != 0) {
                                            appHomeItemAdapter.setItemListCategory(list, listSize)
                                        }
                                    }
                                } else {
                                    listSize = list.size
                                    vh.mBinding.llLoadItem.visibility = View.GONE
                                    if (list.size != 0) {
                                        appHomeItemAdapter.setItemListCategory(list, listSize)
                                    }
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
                    }

                    is Response.Error -> {
                        vh.mBinding.progressBarCyclic.visibility = View.GONE
                        val param = vh.itemView.layoutParams as RecyclerView.LayoutParams
                        vh.itemView.visibility = View.GONE
                        param.height = 0
                        param.width = 0
                        vh.itemView.layoutParams = param
                    }
                }
            }*/
        }
    }

    fun initialization() {
        lang = LocaleHelper.getLanguage(activity)
        utils = Utils(activity)
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        customerId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)

        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        mBinding!!.recyclerCategories.layoutManager = layoutManager
        adapter = SubCategoryItemAdapter(activity!!, mSectionType)
        mBinding!!.recyclerCategories.adapter = adapter
        val vectorDrawable = AppCompatResources.getDrawable(
            activity!!, R.drawable.logo_grey
        )
        if (!TextUtils.isNullOrEmpty(itemImage)) {
            Glide.with(activity!!).load(itemImage).placeholder(vectorDrawable)
                .into(mBinding!!.topImage)
        } else {
            mBinding!!.topImage.setImageDrawable(vectorDrawable)
        }
        mBinding!!.tvMarquee.isSelected = true
        mBinding!!.swipeRefresh.setOnRefreshListener {
            StorePref.getInstance(activity).clear()
            dashboardAPICall()
        }
        mBinding!!.recyclerCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mBinding!!.swipeRefresh.isEnabled =
                    layoutManager!!.findFirstCompletelyVisibleItemPosition() == 0
            }
        })

        // store dashboard API
        dashboardAPICall()

        appHomeViewModel.getSubCateOffer(customerId, subCatId)

    }

    private fun dashboardAPICall() {
        appHomeViewModel.storeDashboard(customerId, warehouseId, subCatId, lang)
    }

    private fun subCategoryAPICall() {
        appHomeViewModel.getSubCategoryData(customerId, warehouseId, subCatId, lang)
    }

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    private fun callFlashDealApi(id: String, llLoadMore: GifImageView?, progressBar: ProgressBar?) {
        appHomeViewModel.getStoreFlashDeal(
            customerId,
            warehouseId,
            id,
            subCatId,
            lang,
            "Home Flash Deal"
        )
        appHomeViewModel.getFlashDealItemData.observe(activity!!) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {
                        try {
                            if (progressBar != null) progressBar.visibility = View.GONE
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
                                if (llLoadMore != null) llLoadMore.visibility = View.VISIBLE
                                listSizeFlashDeal = 2
                            } else {
                                listSizeFlashDeal = itemListModels.size
                                if (llLoadMore != null) llLoadMore.visibility = View.GONE
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
                    if (progressBar != null) progressBar.visibility = View.GONE
                }
            }
        }
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
                    if (commonClassForAPI != null) {
                        dashboardAPICall()
                    }
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
                    if (commonClassForAPI != null) {
                        callFlashDealApi(sectionId, null, null)
                    }
                    acceptFlashStartTimeDes!!.dispose()
                }
            }
    }


    private fun handleStoreDashboardResult(it: Response<ArrayList<HomeDataModel>>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        mBinding!!.progressSubCat.visibility = View.GONE
                        mBinding!!.swipeRefresh.isRefreshing = false
                        mBinding!!.appbar.visibility = View.GONE
                        mBinding!!.recyclerCategories.layoutManager = layoutManager
                        dashboardAdapter =
                            StoreDashAdapter(
                                activity!!, it,
                                this@StoreHomeFragment, this@StoreHomeFragment,
                                this@StoreHomeFragment
                            )
                        mBinding!!.recyclerCategories.adapter = dashboardAdapter
                        dashboardAdapter!!.subCatId = subCatId
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                subCategoryAPICall()
                mBinding!!.appbar.visibility = View.VISIBLE
                mBinding!!.progressSubCat.visibility = View.GONE
                mBinding!!.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun handleSubCategoryResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        mBinding!!.progressSubCat.visibility = View.GONE
                        Utils.hideProgressDialog()
                        val baseCatModel = Gson().fromJson(it, BaseCategoriesModel::class.java)
                        if (baseCatModel.subsubCategoryDc.size != 0) {
                            // set category
                            mBinding!!.recyclerCategories.layoutManager =
                                GridLayoutManager(activity, 2)
                            adapter!!.setData(baseCatModel.subsubCategoryDc, baseCatModel)
                            Glide.with(activity!!).load(
                                EndPointPref.getInstance(activity)
                                    .getString(EndPointPref.API_ENDPOINT) + baseCatModel.subCategoryDC[0].storeBanner
                            )
                                .placeholder(R.drawable.logo_grey_wide)
                                .error(R.drawable.logo_grey_wide)
                                .into(mBinding!!.topImage)
                            if (baseCatModel.subsubCategoryDc.size < 1) {
                                mBinding!!.llEmpty.visibility = View.VISIBLE
                            }
                        } else {
                            mBinding!!.llEmpty.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                mBinding!!.progressSubCat.visibility = View.GONE
            }
        }
    }

    private fun handleSubCategoryOfferResult(it: Response<BillDiscountListResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    var offerMag = ""
                    val list = it.billDiscountList
                    for (i in list!!.indices) {
                        offerMag = if (list[i].billDiscountOfferOn.equals(
                                "Percentage",
                                ignoreCase = true
                            )
                        ) {
                            val offerText =
                                DecimalFormat("##.##").format(list[i].discountPercentage) + RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.per_of_min_per
                                ) + list[i].billAmount
                            //offerList.add(offerMag);
                            "$offerMag* $offerText  "
                        } else if (list[i].billDiscountOfferOn.equals(
                                "FreeItem",
                                ignoreCase = true
                            )
                        ) {
                            val offerText =
                                RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_free_item) + list[i].billAmount
                            // offerList.add(offerMag);
                            "$offerMag* $offerText  "
                        } else {
                            val msgPostBill = if (list[i].applyOn.equals(
                                    "PostOffer",
                                    ignoreCase = true
                                )
                            ) RetailerSDKApp.getInstance().dbHelper.getString(R.string.post_bill_text) else ""
                            if (list[i].walletType.equals(
                                    "WalletPercentage",
                                    ignoreCase = true
                                )
                            ) {
                                val offerText =
                                    DecimalFormat("##.##").format(list[i].billDiscountWallet) + RetailerSDKApp.getInstance().dbHelper.getString(
                                        R.string.per_of_min_per
                                    ) + DecimalFormat("##.##").format(
                                        list[i].billAmount
                                    ) + msgPostBill
                                // offerList.add(offerMag);
                                "$offerMag* $offerText  "
                            } else {
                                val offerText =
                                    (RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) +
                                            DecimalFormat("##.##").format(convertToAmount(list[i].billDiscountWallet))
                                            + RetailerSDKApp.getInstance().dbHelper.getString(R.string.per_of_min_per_wallet) + DecimalFormat(
                                        "##.##"
                                    ).format(
                                        list[i].billAmount
                                    ) + msgPostBill)
                                //  offerList.add(offerMag);
                                "$offerMag* $offerText  "
                            }
                        }
                    }
                    offerMag = try {
                        offerMag.substring(0, offerMag.length - 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ""
                    }
                    if (!TextUtils.isNullOrEmpty(offerMag)) {
                        mBinding!!.tvMarquee.text = offerMag
                    } else {
                        mBinding!!.llOfferList.visibility = View.GONE
                    }
                }
            }

            is Response.Error -> {
                mBinding!!.llOfferList.visibility = View.GONE
                Utils.hideProgressDialog()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): StoreHomeFragment {
            return StoreHomeFragment()
        }
    }
}