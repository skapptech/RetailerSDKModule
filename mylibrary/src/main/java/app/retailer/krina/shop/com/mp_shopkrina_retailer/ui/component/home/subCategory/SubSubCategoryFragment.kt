package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.RelatedItemsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.RelatedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FiltePopupDilogBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentSubSubCategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CatePopupInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.CategoryPopupListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.CategoryRelatedItemViewAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.FilterListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FilterItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SliderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.CatRelatedItemPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem.SearchItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class SubSubCategoryFragment : Fragment(), SubSubCategoryFilterInterface, SubCategoryInterface,
    CatePopupInterface {
    private lateinit var mBinding: FragmentSubSubCategoryBinding
    private lateinit var activity: HomeActivity
    private lateinit var appCtx: MyApplication
    private lateinit var subSubCatViewModel: SubSubCatViewModel
    private lateinit var popupWindow: PopupWindow
    private var subCategoryFilterAdapter: SubCategoryFilterAdapter? = null
    private var subSubCategoryAdapter: SubSubCategoryAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var filterListAdapter: FilterListAdapter? = null
    private var mRelatedItemAdapter: CategoryRelatedItemViewAdapter? = null
    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()
    private var categoryList = ArrayList<CategoriesModel>()
    private var subCategoryItemList = ArrayList<SubCategoriesModel>()
    private val filterSubCategoryList = ArrayList<SubCategoriesModel>()
    private var subSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private val filterSubSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private val filterList = ArrayList<FilterItemModel>()
    private val relatedItemList = ArrayList<RelatedItemsModel>()
    private var mItemIdList: ArrayList<Int>? = null
    private var lang = ""
    private var warehouseId = 0
    private var custId = 0
    private var basePos = 0
    private var allowrefresh = true
    private var flagCateSelector = false
    private var baseCategoryId = 0
    private var categoryId = 0
    private var subCatId = 0
    private var subSubCattId = 0
    private var storeId = 0
    private var skip = 0
    private var homeFlag = false
    private var isLoadDataForRelatedItem = true
    private var isStore = false
    private var isFirst = false
    private var mSectionType: String? = ""
    private var sortType = "M"
    private var direction = "desc"
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mGuideView: GuideView
    private lateinit var builder: GuideView.Builder
    private lateinit var shortBottomDialog: BottomSheetDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity.application as MyApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sub_sub_category, container, false)
        val appRepository = AppRepository(activity.applicationContext)
        subSubCatViewModel =
            ViewModelProvider(
                activity,
                SubSubCategoryViewModelFactory(appCtx, appRepository)
            )[SubSubCatViewModel::class.java]
        val bundle = this.arguments
        if (bundle != null) {
            baseCategoryId = bundle.getInt("BaseCategoryId")
            categoryId = bundle.getInt("CATEGORY_ID")
            subCatId = bundle.getInt("SUB_CAT_ID")
            storeId = subCatId
            subSubCattId = bundle.getInt("SUB_SUB_CAT_ID")
            homeFlag = bundle.getBoolean("HOME_FLAG")
            mSectionType = bundle.getString("SectionType")
            isStore = bundle.getBoolean("isStore")
        }
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialization()
        // view show/hide
        viewHideUnHide()
        //API calling  Category ,subcategory and sub sub category
        observe(subSubCatViewModel.getCategoriesData, ::handleCategoriesResult)
        observe(subSubCatViewModel.getRelatedItemData, ::handleRelatedItemResult)

        categoryAPICall()
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity,
            this.javaClass.simpleName, null
        )
        if (itemListAdapter != null && itemListAdapter!!.itemCount > 0) {
            itemListAdapter!!.notifyDataSetChanged()
        }
        if (allowrefresh) {
            allowrefresh = false
        }
        activity.lerCateSelected!!.visibility = View.VISIBLE
        activity.spLayout!!.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        activity.lerCateSelected!!.visibility = View.GONE
        activity.spLayout!!.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }


    // category select result
    override fun catePopupSelected(
        pos: Int,
        bannerImage: String?,
        selectedCatId: Int,
        cateName: String
    ) {
        try {
            basePos = pos
            flagCateSelector = false
            activity.tvCateSelected!!.text = cateName
            setSubCategory(selectedCatId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
        loadBanner(bannerImage)
        mBinding.rvSubCategory.scrollToPosition(0)
        subCategoryFilterAdapter!!.notifyDataSetChanged()
    }

    // sub category select
    override fun SubCategoryClicked(SubCategoryId: Int, CategoryId: Int) {
        setSubSubCategory(SubCategoryId, CategoryId)
        mBinding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
    }

    // sub sub category select
    override fun SubSubCategoryFilterClicked(
        pos: Int,
        subsubCatId: Int,
        scateId: Int,
        categoryId: Int
    ) {
        try {
            subSubCattId = subsubCatId
            subCatId = scateId
            this.categoryId = categoryId
            callItemMasterAPI(subsubCatId, scateId, categoryId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun initialization() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        activity.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding.shimmerViewContainer.startShimmer()
        mBinding.RLShimmerLayout.visibility = View.VISIBLE
        mBinding.LLSubMainLayout.visibility = View.INVISIBLE

        mBinding.rvRelatedItem.setHasFixedSize(true)
        mBinding.rvRelatedItem.isNestedScrollingEnabled = false
        mBinding.rvSubSubCategory.setHasFixedSize(true)
        mBinding.rvSubSubCategory.isNestedScrollingEnabled = false

        subSubCategoryAdapter = SubSubCategoryAdapter(activity, filterSubSubCategoriesList, this)
        mBinding.rvSubSubCategory.adapter = subSubCategoryAdapter
        mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvCategoryItem.layoutManager = mLinearLayoutManager
        mBinding.rvCategoryItem.isNestedScrollingEnabled = false

        itemListAdapter =
            ItemListAdapter(
                activity,
                list
            )
        mBinding.rvCategoryItem.adapter = itemListAdapter
        mBinding.rvSubCategory.setHasFixedSize(true)
        subCategoryFilterAdapter = SubCategoryFilterAdapter(activity, filterSubCategoryList, this)
        mBinding.rvSubCategory.adapter = subCategoryFilterAdapter
        mBinding.rvSubCategory.isNestedScrollingEnabled = false
        subCategoryFilterAdapter!!.basCatId = baseCategoryId
        mBinding.filterTitle.requestFocus()
        popupWindow = PopupWindow()
        mRelatedItemAdapter = CategoryRelatedItemViewAdapter(activity, relatedItemList)
        mBinding.rvRelatedItem.adapter = mRelatedItemAdapter
        mBinding.txtSort.text = MyApplication.getInstance().dbHelper.getString(R.string.sort)
        mBinding.noItems.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_items_avl)
        mBinding.tvItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.related_item)

        mBinding.nestedScroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val visibleItemCount = mLinearLayoutManager.childCount
                    val totalItemCount = mLinearLayoutManager.itemCount
                    val pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        Log.i("Nested", "BOTTOM SCROLL")
                        if (isLoadDataForRelatedItem) {
                            if (list.size > 2) {
                                skip += 10
                                if (isStore && basePos == 0) {
                                    subSubCatViewModel.fetchItemList1(
                                        custId,
                                        subSubCattId,
                                        if (isStore) storeId else subCatId,
                                        0,
                                        lang,
                                        skip,
                                        10,
                                        sortType,
                                        direction
                                    )
                                } else {
                                    subSubCatViewModel.fetchItemList1(
                                        custId,
                                        subSubCattId,
                                        if (isStore) storeId else subCatId,
                                        categoryId,
                                        lang,
                                        skip,
                                        10,
                                        sortType,
                                        direction
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!StoryBordSharePrefs.getInstance(activity)
                .getBoolean(StoryBordSharePrefs.SUBSUBCATEGORY)
        ) {
            appStoryView()
        }

        //search icon clicked
        activity.SearchIcon!!.setOnClickListener {
            activity.pushFragments(
                SearchItemFragment.newInstance(),
                true, true, null
            )
        }
        activity.lerCateSelected!!.setOnClickListener {
            if (flagCateSelector && popupWindow.isFocusable && popupWindow.isShowing) {
                flagCateSelector = false
                popupWindow.dismiss()
            } else {
                flagCateSelector = true
                showCategoryPopup()
            }
        }
        // items filter
        mBinding.filter.setOnClickListener {
            showFilterDialog()
        }

        observe(subSubCatViewModel.itemData, ::handleItemListResult)
        observe(subSubCatViewModel.itemData1, ::handleItemListResult1)
    }

    private fun appStoryView() {
        builder = GuideView.Builder(activity)
            .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.category))
            .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.category_detail))
            .setGravity(Gravity.center)
            .setDismissType(DismissType.anywhere)
            .setTargetView(activity.lerCateSelected)
            .setGuideListener { view: View ->
                when (view.id) {
                    R.id.liCategory -> builder.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.subcategory
                        )
                    )
                        .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.subcategory_detail))
                        .setTargetView(mBinding.rvSubCategory).build()

                    R.id.rvSubCategory -> builder.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.brand
                        )
                    )
                        .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.brand_detail))
                        .setTargetView(mBinding.rvSubSubCategory).build()

                    R.id.rvSubSubCategory -> {
                        StoryBordSharePrefs.getInstance(activity)
                            .putBoolean(StoryBordSharePrefs.SUBSUBCATEGORY, true)
                        return@setGuideListener
                    }
                }
                mGuideView = builder.build()
                mGuideView.show()
            }
        mGuideView = builder.build()
        mGuideView.show()
        updatingForDynamicLocationViews()
    }

    private fun updatingForDynamicLocationViews() {
        mBinding.rvSubSubCategory.onFocusChangeListener =
            View.OnFocusChangeListener { view: View?, b: Boolean -> mGuideView.updateGuideViewLocation() }
    }

    private fun viewHideUnHide() {
        activity.tvCateSelected!!.text = ""
        lang = LocaleHelper.getLanguage(activity)
        activity.searchText!!.visibility = View.GONE
        activity.rightSideIcon!!.visibility = View.GONE
        activity.topToolbarTitle!!.visibility = View.GONE
    }

    private fun categoryAPICall() {
        if (isStore) {
            subSubCatViewModel.getCategories(custId, warehouseId, 0, subCatId, lang, true)
        } else {
            val dataSaved = SectionPref.getInstance(activity)
                .getString(SectionPref.CATEGORY_BY_ID + baseCategoryId)
            if (!TextUtils.isNullOrEmpty(dataSaved)) {
                try {
                    val jsonObject = JSONObject(dataSaved)
                    val baseCategoryModel = Gson().fromJson(
                        jsonObject.toString(),
                        BaseCategoriesModel::class.java
                    )
                    categoryList.clear()
                    subCategoryItemList.clear()
                    subSubCategoriesList.clear()
                    categoryList = baseCategoryModel.categoryDC
                    subCategoryItemList = baseCategoryModel.subCategoryDC
                    subSubCategoriesList = baseCategoryModel.subsubCategoryDc
                    // set category
                    setCategoryUsingCategoryId(categoryList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                subSubCatViewModel.getCategories(
                    custId,
                    warehouseId,
                    baseCategoryId,
                    subCatId,
                    lang,
                    false
                )
            }
        }
    }

    private fun setCategoryUsingCategoryId(categoryList: ArrayList<CategoriesModel>) {
        mBinding.shimmerViewContainer.stopShimmer()
        mBinding.RLShimmerLayout.visibility = View.GONE
        mBinding.LLSubMainLayout.visibility = View.VISIBLE
        if (isStore && this.categoryList.size > 1) {
            this.categoryList.add(
                0,
                CategoriesModel(MyApplication.getInstance().dbHelper.getString(R.string.all), 0)
            )
        }
        if (this.categoryList.size != 0) {
            var i = 0
            for (model in categoryList) {
                if (model.categoryid == categoryId) {
                    activity.tvCateSelected!!.text = model.categoryname
                    setSubCategory(model.categoryid)
                    loadBanner(model.categoryImg)
                    i++
                    break
                }
            }
            if (isStore) {
                activity.tvCateSelected!!.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.all)
            }
            if (i == 0) {
                mBinding.noItems.visibility = View.VISIBLE
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.somthing_went_wrong)
            )
        }
    }

    private fun loadBanner(bannerUrl: String?) {
        if (!TextUtils.isNullOrEmpty(bannerUrl)) {
            Picasso.get().load(bannerUrl).into(mBinding.catImg)
        } else {
            Picasso.get()
                .load("https://res.cloudinary.com/shopkirana/image/upload/v1551078737/banners/sk_banner.jpg")
                .into(mBinding.catImg)
        }
    }

    // method for show category
    private fun showCategoryPopup() {
        val mView = LayoutInflater.from(activity).inflate(R.layout.category_sub_category_item, null)
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.contentView = mView
        popupWindow.showAsDropDown(activity.SearchIcon)
        val rvCategoryPopup: RecyclerView = mView.findViewById(R.id.rvCategoryPopup)
        val itemDecor = DividerItemDecoration(activity, ClipDrawable.HORIZONTAL)
        rvCategoryPopup.addItemDecoration(itemDecor)
        val lmCategory = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvCategoryPopup.layoutManager = lmCategory
        if (categoryList.size != 0) {
            val mCatAdapter = CategoryPopupListAdapter(
                activity,
                categoryList, this
            )
            rvCategoryPopup.adapter = mCatAdapter
        }
    }

    private fun showFilterDialog() {
        shortBottomDialog =
            BottomSheetDialog(activity, R.style.Theme_Design_BottomSheetDialog)
        val mFilterDialogBinding: FiltePopupDilogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.filte_popup_dilog, null, false)
        shortBottomDialog.setContentView(mFilterDialogBinding.root)

        mFilterDialogBinding.tvSort.text =
            MyApplication.getInstance().dbHelper.getString(R.string.sort_by)

        mFilterDialogBinding.llcolse.setOnClickListener {
            shortBottomDialog.dismiss()
        }

        val listener = AdapterInterface { value ->
            for (j in filterList.indices) {
                filterList[j].setChecked(value == j)
            }
            filterListAdapter!!.setItemListCategory(filterList)
            val handler = Handler(Looper.myLooper()!!)
            handler.postDelayed(object : Runnable {
                override fun run() {
                    //Do something after 100ms
                    shortBottomDialog.dismiss()
                    handler.postDelayed(this, 200)
                    handler.removeCallbacks(this)
                }
            }, 200)
            when (value) {
                1 -> {
                    sortType = "M"
                    direction = "desc"
                }

                3 -> {
                    sortType = "P"
                    direction = "asc"
                }

                4 -> {
                    sortType = "P"
                    direction = "desc"
                }

                6 -> {
                    sortType = "Q"
                    direction = "asc"
                }

                7 -> {
                    sortType = "Q"
                    direction = "desc"
                }
            }
            skip = 0
            isLoadDataForRelatedItem = true
            if (isStore && basePos == 0) {
                subSubCatViewModel.fetchItemList(
                    custId, subSubCattId, if (isStore) storeId else subCatId,
                    0, lang, 0, 10, sortType, direction
                )
            } else {
                subSubCatViewModel.fetchItemList(
                    custId, subSubCattId, if (isStore) storeId else subCatId,
                    categoryId, lang, skip, 10, sortType, direction
                )
            }
        }
        filterListAdapter = FilterListAdapter(activity, filterList, listener)
        mFilterDialogBinding.listFilter.adapter = filterListAdapter
        shortBottomDialog.show()
    }

    // Call itemMater API
    private fun callItemMasterAPI(sSubCatId: Int, subCatId: Int, categoryId: Int) {
        filterItems()
        if (NetworkUtils.isInternetAvailable(appCtx)) {
            sortType = "M"
            direction = "desc"
            skip = 0
            isLoadDataForRelatedItem = true
            list.clear()
            itemListAdapter!!.notifyDataSetChanged()
            mBinding.proRelatedItem.visibility = View.VISIBLE
            if (isStore && basePos == 0) {
                subSubCatViewModel.fetchItemList(
                    custId, sSubCatId, if (isStore) storeId else subCatId,
                    0, lang, 0, 10, sortType, direction
                )
            } else {
                subSubCatViewModel.fetchItemList(
                    custId, sSubCatId, if (isStore) storeId else subCatId,
                    categoryId, lang, 0, 10, sortType, direction
                )
            }
            // check avail offers
//            checkAvailOffer(categoryId, subCatId, sSubCatId)
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // filter data
    private fun filterItems() {
        filterList.clear()
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.margins),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                MyApplication.getInstance().dbHelper.getString(R.string.margins),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_price),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_low_to_high),
                MyApplication.getInstance().dbHelper.getString(R.string.txt_price),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                MyApplication.getInstance().dbHelper.getString(R.string.txt_price),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.moq),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_low_to_high),
                MyApplication.getInstance().dbHelper.getString(R.string.moq),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                MyApplication.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                MyApplication.getInstance().dbHelper.getString(R.string.moq),
                false
            )
        )
        filterList[1].setChecked(true)
    }

    private fun layoutHideUnHide(value: Boolean) {
        if (value) {
            mBinding.noItems.visibility = View.GONE
            mBinding.progressCategory.visibility = View.GONE
            mBinding.rvCategoryItem.visibility = View.VISIBLE
            mBinding.llRelatedItem.visibility = View.VISIBLE
        } else {
            mBinding.filterTitle.text =
                "0 " + MyApplication.getInstance().dbHelper.getString(R.string.Items)
            mBinding.noItems.visibility = View.VISIBLE
            mBinding.progressCategory.visibility = View.GONE
            mBinding.rvCategoryItem.visibility = View.GONE
            mBinding.llRelatedItem.visibility = View.GONE
        }
    }

    private fun setRelatedAPICall(mItemIdList: ArrayList<Int>?) {
        if (mItemIdList!!.size != 0) {
            subSubCatViewModel.getRelatedItem(
                CatRelatedItemPostModel(
                    warehouseId, custId, mItemIdList, lang, 0, 10
                )
            )
            mBinding.filterTitle.requestFocus()
        }
    }

    // filter sub category
    private fun setSubCategory(categoryId: Int) {
        var subCategoryId = 0
        try {
            filterSubCategoryList.clear()
            for (i in subCategoryItemList.indices) {
                if (subCategoryItemList[i].categoryid == categoryId) {
                    if (subCategoryItemList[i].itemcount != 0) {
                        filterSubCategoryList.add(subCategoryItemList[i])
                    }
                }
            }
            if (filterSubCategoryList.size > 1 || categoryId == 0) {
                filterSubCategoryList.add(
                    0, SubCategoriesModel(
                        false, 0,
                        categoryId, MyApplication.getInstance().dbHelper.getString(R.string.all),
                        "", 10
                    )
                )
            }
            if (filterSubCategoryList.size > 0) {
                if (homeFlag) {
                    subCategoryId = subCatId
                    for (i in filterSubCategoryList.indices) {
                        if (subCategoryId == filterSubCategoryList[i].subcategoryid) {
                            mBinding.rvSubCategory.scrollToPosition(i)
                        }
                    }
                } else {
                    subCategoryId = filterSubCategoryList[0].subcategoryid
                }
                subCategoryFilterAdapter!!.setSubcategoryOrderList(
                    filterSubCategoryList,
                    subCategoryId
                )
                val finalSubCategoryId = subCategoryId
                // check if id exist
                val checkList =
                    filterSubCategoryList.filter { s: SubCategoriesModel -> s.subcategoryid == finalSubCategoryId }
                if (checkList == null || checkList.size == 0) {
                    mBinding.noItems.visibility = View.VISIBLE
                }
            } else {
                layoutHideUnHide(false)
                Utils.setToast(
                    activity,
                    MyApplication.getInstance().dbHelper.getString(R.string.no_data_available)
                )
                subCategoryFilterAdapter!!.setSubcategoryOrderList(
                    filterSubCategoryList,
                    subCategoryId
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // filter sub sub category
    private fun setSubSubCategory(SubCategotyId: Int, CategoryId: Int) {
        try {
            filterSubSubCategoriesList.clear()
            filterSubSubCategoriesList.add(
                0,
                SubSubCategoriesModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.all),
                    baseCategoryId, CategoryId, SubCategotyId, 0
                )
            )
            mBinding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
            if (isStore) {
                if (CategoryId != 0 && isFirst) {
                    for (model in subSubCategoriesList) {
                        if (model.categoryid == CategoryId) {
                            filterSubSubCategoriesList.add(model)
                        }
                    }
                } else {
                    isFirst = true
                    var exist = false
                    for (i in subSubCategoriesList.indices) {
                        for (model in filterSubSubCategoriesList) {
                            if (model.subsubcategoryid == subSubCategoriesList[i].subsubcategoryid) {
                                exist = true
                                break
                            }
                        }
                        if (!exist) {
                            filterSubSubCategoriesList.add(subSubCategoriesList[i])
                        }
                        exist = false
                    }
                }
            } else for (ii in subSubCategoriesList.indices) {
                if (subSubCategoriesList[ii].subcategoryid == SubCategotyId && subSubCategoriesList[ii].categoryid == CategoryId) {
                    if (subSubCategoriesList[ii].itemcount != 0) {
                        filterSubSubCategoriesList.add(subSubCategoriesList[ii])
                    }
                }
            }
            if (filterSubSubCategoriesList.size == 2) {
                filterSubSubCategoriesList.removeAt(0)
            }
            if (filterSubSubCategoriesList.size != 0) {
                var subsubCatId = 0
                if (homeFlag) {
                    homeFlag = false
                    subsubCatId = subSubCattId
                } else {
                    subsubCatId = filterSubSubCategoriesList[0].subsubcategoryid
                }
                filterSubSubCategoriesList[0].isChecked = true
                subSubCategoryAdapter!!.setcategoryOrderFilterList(
                    filterSubSubCategoriesList,
                    subsubCatId
                )
                val finalSubsubCatId = subsubCatId
                val checkList =
                    filterSubSubCategoriesList.filter { s: SubSubCategoriesModel -> s.subsubcategoryid == finalSubsubCatId }
                if (checkList == null || checkList.size == 0) {
                    mBinding.noItems.visibility = View.VISIBLE
                }
                for (i in filterSubSubCategoriesList.indices) {
                    if (filterSubSubCategoriesList[i].subsubcategoryid == subsubCatId) {
                        mBinding.rvSubSubCategory.scrollToPosition(i)
                        break
                    }
                }
            } else {
                subSubCategoryAdapter!!.setcategoryOrderFilterList(filterSubSubCategoriesList, 0)
                layoutHideUnHide(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleItemListResult(it: Response<ItemListResponse>) {
        when (it) {
            is Response.Loading -> {
                mBinding.proRelatedItem.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    mBinding.proRelatedItem.visibility = View.GONE
                    homeFlag = false
                    mItemIdList = ArrayList()
                    try {
                        if (it.isStatus) {
                            layoutHideUnHide(true)
                            relatedItemList.clear()
                            list.clear()
                            inactiveItemList.clear()
                            val itemList = it.itemMasters
                            for (i in itemList!!.indices) {
                                if (itemList[i].active) {
                                    var ispresent = false
                                    for (j in list.indices) {
                                        if (list[j].itemNumber.equals(
                                                itemList[i].itemNumber,
                                                ignoreCase = true
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
                                    }
                                    if (!ispresent) {
                                        list.add(itemList[i])
                                    }
                                } else {
                                    inactiveItemList.add(itemList[i])
                                }
                                mItemIdList!!.add(itemList[i].itemId)
                            }
                            if (inactiveItemList.size != 0) {
                                list.addAll(inactiveItemList)
                            }
                            if (list.size != 0) {
                                mBinding.filterTitle.text =
                                    it.totalItem + " " + MyApplication.getInstance().dbHelper.getString(
                                        R.string.Items
                                    )
                                itemListAdapter =
                                    ItemListAdapter(
                                        activity,
                                        list
                                    )
                                mBinding.rvCategoryItem.adapter = itemListAdapter
                                if (mRelatedItemAdapter != null) {
                                    mRelatedItemAdapter!!.notifyDataSetChanged()
                                }
                                mBinding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
                                // update analytic
                                MyApplication.getInstance().updateAnalyticVIL("categoryItems", list)
                                if (list.size < 5) {
                                    skip += 10
                                    mBinding.progressCategory.visibility = View.VISIBLE
                                    if (isStore && basePos == 0) {
                                        subSubCatViewModel.fetchItemList1(
                                            custId,
                                            subSubCattId,
                                            if (isStore) storeId else subCatId,
                                            0,
                                            lang,
                                            skip,
                                            10,
                                            sortType,
                                            direction
                                        )
                                    } else {
                                        subSubCatViewModel.fetchItemList1(
                                            custId,
                                            subSubCattId,
                                            if (isStore) storeId else subCatId,
                                            categoryId,
                                            lang,
                                            skip,
                                            10,
                                            sortType,
                                            direction
                                        )
                                    }
                                }
                            }
                        } else {
                            layoutHideUnHide(false)
                            itemListAdapter!!.setItemListCategory(list)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                mBinding.proRelatedItem.visibility = View.GONE
                Toast.makeText(activity, it.errorMesssage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleItemListResult1(it: Response<ItemListResponse>) {
        when (it) {
            is Response.Loading -> {
                mBinding.progressCategory.visibility = View.VISIBLE
            }

            is Response.Success -> {
                mBinding.progressCategory.visibility = View.GONE
                it.data?.let {
                    try {
                        if (it.isStatus && it.itemMasters != null) {
                            val itemList = it.itemMasters
                            for (i in itemList!!.indices) {
                                if (itemList[i].active) {
                                    var ispresent = false
                                    for (j in list.indices) {
                                        if (list[j].itemNumber == itemList[i].itemNumber) {
                                            ispresent = true
                                            if (list[j].moqList.size == 0) {
                                                list[j].moqList.add(list[j])
                                                list[j].moqList[0].isChecked = true
                                            }
                                            list[j].moqList.add(itemList[i])
                                            break
                                        }
                                    }
                                    if (!ispresent) {
                                        list.add(itemList[i])
                                    }
                                } else {
                                    inactiveItemList.add(itemList[i])
                                }
                                mItemIdList!!.add(itemList[i].itemId)
                            }
                            if (list.size != 0) {
                                itemListAdapter!!.notifyDataSetChanged()
                                // update analytic
                                MyApplication.getInstance().updateAnalyticVIL("categoryItems", list)
                            }
                        } else {
                            if (subSubCattId == 0)
                                if (!isStore) setRelatedAPICall(mItemIdList)
                            isLoadDataForRelatedItem = false
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                mBinding.progressCategory.visibility = View.GONE
                Toast.makeText(activity, it.errorMesssage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkAvailOffer(categoryId: Int, subCatId: Int, sSubCatId: Int) {
        Observable.fromCallable {
            // will run in background thread (same as doinBackground)
            val discountList = MyApplication.getInstance().billDiscountList
            val list = ArrayList<BillDiscountModel>()
            if (discountList != null && discountList.size > 0) {
                if (subCatId == 0 && sSubCatId == 0)
                    list.addAll(discountList.filter { item -> item.billDiscountType == "category" && item.offerBillDiscountItems!!.any { it.id == categoryId } })
                else {
                    list.addAll(discountList.filter { it.billDiscountType == "subcategory" && it.offerBillDiscountItems!!.any { it.id == subCatId && it.categoryId == categoryId } })
                    list.addAll(discountList.filter { it.billDiscountType == "subsubcategory" || it.billDiscountType == "brand" && it.offerBillDiscountItems!!.any { it.id == sSubCatId && it.categoryId == categoryId && it.subCategoryId == subCatId } })
                }
            }
            list
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("dis " + it.size)
                if (it.size > 0)
                    Utils(activity).showOfferView(
                        mBinding.progressCategory,
                        activity.bottomNavigationView,
                        it[0].description
                    )
            }
    }

    private fun handleCategoriesResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                mBinding.progressCategory.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    try {
                        Utils.hideProgressDialog()
                        mBinding.progressCategory.visibility = View.GONE
                        val baseCategoryModel = Gson().fromJson(it, BaseCategoriesModel::class.java)
                        categoryList.clear()
                        subCategoryItemList.clear()
                        subSubCategoriesList.clear()
                        categoryList = baseCategoryModel.categoryDC
                        subCategoryItemList = baseCategoryModel.subCategoryDC
                        subSubCategoriesList = baseCategoryModel.subsubCategoryDc
                        //set category
                        setCategoryUsingCategoryId(categoryList)
                        if (!isStore && categoryList.size > 0) SectionPref.getInstance(activity)
                            .putString(
                                SectionPref.CATEGORY_BY_ID + baseCategoryId,
                                it.toString()
                            )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.progressCategory.visibility = View.GONE
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.RLShimmerLayout.visibility = View.GONE
                mBinding.LLSubMainLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun handleRelatedItemResult(it: Response<RelatedModel>) {
        when (it) {
            is Response.Loading -> {
                mBinding.progressCategory.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    mBinding.progressCategory.visibility = View.GONE
                    if (it.relatedItemSearch!!.size != 0) {
                        mBinding.tvItem.visibility = View.VISIBLE
                        relatedItemList.addAll(it.relatedItemSearch!!)
                        mRelatedItemAdapter!!.notifyDataSetChanged()
                    }
                    if (relatedItemList.size == 0) {
                        mBinding.tvItem.visibility = View.GONE
                    }
                }
            }

            is Response.Error -> {
                mBinding.progressCategory.visibility = View.GONE
                mBinding.tvItem.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): SubSubCategoryFragment {
            return SubSubCategoryFragment()
        }
    }
}