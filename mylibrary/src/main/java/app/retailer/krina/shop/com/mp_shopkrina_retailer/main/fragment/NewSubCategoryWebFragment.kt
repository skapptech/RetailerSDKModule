package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FiltePopupDilogBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.NewSubSubCategoryFragmentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.FilterListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.NewSubCategoryFilterAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FilterItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem.SearchItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.util.Collections

class NewSubCategoryWebFragment : Fragment(), SubSubCategoryFilterInterface,
    NewSubCategoryFilterAdapter.SubCategoryInterface {

    private lateinit var mBinding: NewSubSubCategoryFragmentBinding
    var homeActivity = activity as? HomeActivity
    private lateinit var utils: Utils
    private lateinit var commonClassForAPI: CommonClassForAPI

    private var subSubCategoryAdapter: SubSubCategoryAdapter? = null
    private var subCategoryFilterAdapter: NewSubCategoryFilterAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var filterListAdapter: FilterListAdapter? = null

    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()
    private var SubCategoryItemList = ArrayList<SubCategoriesModel>()
    private var FilterSubCategoryList = ArrayList<SubCategoriesModel>()
    private var SubSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private val FilterSubSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private val filterList = ArrayList<FilterItemModel>()

    private val handler = Handler()
    private var lang = ""
    private var custId = 0
    private var SelectedSSCIdFlag = false
    private var allowrefresh = true
    private var subCattId = 0
    private val subSubCattId = 0
    private val mSectionType = ""
    private var baseCategoryModel: BaseCategoriesModel? = null


    override fun onAttach(_context: Context) {
        super.onAttach(_context)
        homeActivity = _context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = NewSubSubCategoryFragmentBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            subCattId = bundle.getInt("SUB_CAT_ID")
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialization()
        viewHideUnHide()
        //search icon clicked
        homeActivity!!.SearchIcon!!.setOnClickListener {
            homeActivity!!.pushFragments(
                SearchItemFragment.newInstance(),
                true,
                true,
                null
            )
        }
        // prepareListData();
        //items filter
        mBinding.filter.setOnClickListener {
            val shortBottomDialog =
                BottomSheetDialog(homeActivity!!, R.style.Theme_Design_BottomSheetDialog)
            val mFilterDialogBinding: FiltePopupDilogBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.filte_popup_dilog, null, false)
            shortBottomDialog.setContentView(mFilterDialogBinding.root)

            mFilterDialogBinding.tvSort.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.sort_by)

            mFilterDialogBinding.llcolse.setOnClickListener {
                shortBottomDialog.dismiss()
            }

            val listViewFilter = shortBottomDialog.findViewById<ListView>(R.id.listFilter)
            val listener = AdapterInterface { value ->
                for (j in filterList.indices) {
                    filterList[j].setChecked(value == j)
                }
                filterListAdapter!!.setItemListCategory(filterList)
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        //Do something after 100ms
                        shortBottomDialog.dismiss()
                        handler.postDelayed(this, 300)
                        handler.removeCallbacks(this)
                    }
                }, 300)
                when (value) {
                    1 -> {
                        Collections.sort(list, ComparatorMarginDes())
                        itemListAdapter!!.setItemListCategory(list)
                    }

                    3 -> {
                        Collections.sort(list, ComparatorPriceAsc())
                        itemListAdapter!!.setItemListCategory(list)
                    }

                    4 -> {
                        Collections.sort(list, ComparatorPriceDes())
                        itemListAdapter!!.setItemListCategory(list)
                    }

                    6 -> {
                        Collections.sort(list, ComparatorMoqAsc())
                        itemListAdapter!!.setItemListCategory(list)
                    }

                    7 -> {
                        Collections.sort(list, ComparatorMoqDes())
                        itemListAdapter!!.setItemListCategory(list)
                    }
                }
            }
            filterListAdapter = FilterListAdapter(activity, filterList, listener)
            listViewFilter?.adapter = filterListAdapter
            shortBottomDialog.show()
        }
        //API calling   subcategory and sub sub category
        subCategoryItemAPICall()
    }

    fun initialization() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)

        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding!!.noItems.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.items_not_available)
        mBinding!!.DataNotFound.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Data_not_found)


        mBinding!!.rvSubSubCategory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mBinding!!.rvSubSubCategory.setHasFixedSize(true)
        mBinding!!.rvSubSubCategory.isNestedScrollingEnabled = false
        subSubCategoryAdapter = SubSubCategoryAdapter(homeActivity!!, FilterSubSubCategoriesList, this)
        mBinding!!.rvSubSubCategory.adapter = subSubCategoryAdapter


        val mLinearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvCategoryItem.layoutManager = mLinearLayoutManager
        mBinding!!.rvCategoryItem.isNestedScrollingEnabled = false
        itemListAdapter = ItemListAdapter(homeActivity!!, list)
        mBinding!!.rvCategoryItem.adapter = itemListAdapter


        mBinding!!.rvSubCategory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mBinding!!.rvSubCategory.setHasFixedSize(true)
        subCategoryFilterAdapter =
            NewSubCategoryFilterAdapter(homeActivity!!, FilterSubCategoryList, this)
        mBinding!!.rvSubCategory.adapter = subCategoryFilterAdapter
        mBinding!!.rvSubCategory.isNestedScrollingEnabled = false

        mBinding!!.filterTitle.requestFocus()
    }

    private fun viewHideUnHide() {
        lang = LocaleHelper.getLanguage(activity)
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE
        homeActivity!!.topToolbarTitle!!.visibility = View.GONE
    }

    private fun subCategoryAPICall() {
        SubCategoryItemList.clear()
        SubSubCategoriesList.clear()
        SubCategoryItemList = baseCategoryModel!!.subCategoryDC
        SubSubCategoriesList = baseCategoryModel!!.subsubCategoryDc
        if (SubCategoryItemList.size != 0) {
            mBinding!!.llAvlData.visibility = View.VISIBLE
            mBinding!!.llNoData.visibility = View.GONE
            //set  sub category
            if (SubCategoryItemList.size != 0) {
                val subCatId = SubCategoryItemList[0].subcategoryid
                subCategoryFilterAdapter!!.setSubcategoryOrderList(SubCategoryItemList)
            } else {
                layoutHideUnHide(false)
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_data_available)
                )
            }
        } else {
            mBinding!!.llAvlData.visibility = View.GONE
            mBinding!!.llNoData.visibility = View.VISIBLE
        }
    }


    /***
     * Call itemMater API
     */
    private fun callItemMasterAPI(sscatid: Int, scateId: Int, categoryId: Int) {
        filterItems()
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                mBinding!!.proRelatedItem.visibility = View.VISIBLE
                commonClassForAPI!!.fetchItemBrandByList(
                    itemMasterDes,
                    custId,
                    sscatid,
                    scateId,
                    lang,
                    mSectionType
                )
            }
        } else {
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // filter data
    private fun filterItems() {
        filterList.clear()
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.margins),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.margins),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_price),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_low_to_high),
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_price),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_price),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq),
                "",
                true
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_low_to_high),
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq),
                false
            )
        )
        filterList.add(
            FilterItemModel(
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_high_to_low),
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq),
                false
            )
        )
        filterList[1].setChecked(true)
    }


    private fun layoutHideUnHide(value: Boolean) {
        if (value) {
            mBinding!!.noItems.visibility = View.GONE
            mBinding!!.rvCategoryItem.visibility = View.VISIBLE
        } else {
            mBinding!!.filterTitle.text =
                "0 " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.Items)
            mBinding!!.noItems.visibility = View.VISIBLE
            mBinding!!.rvCategoryItem.visibility = View.GONE
        }
    }

    // filter sub category
    private fun setSubCategory(SubCategoryItemList: ArrayList<SubCategoriesModel>) {
        FilterSubCategoryList = SubCategoryItemList
        var subCategoryId = 0
        if (FilterSubCategoryList.size != 0) {
            subCategoryId = FilterSubCategoryList[0].subcategoryid
            subCategoryFilterAdapter!!.setSubcategoryOrderList(FilterSubCategoryList)
        } else {
            layoutHideUnHide(false)
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_data_available)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemMasterDes?.dispose()
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName,
            null
        )
        try {
            if (SelectedSSCIdFlag) {
                if (itemListAdapter != null) {
                    itemListAdapter!!.notifyDataSetChanged()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (allowrefresh) {
            allowrefresh = false
        }
    }

    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }

    // sub category select
    override fun SubCategoryClicked(SubCategoryId: Int, CategoryId: Int) {
        if (SubSubCategoriesList.size != 0) {
            var subsubCatId = 0
            if (subSubCattId != 0) {
                subsubCatId = subSubCattId
                for (i in SubSubCategoriesList.indices) {
                    if (subSubCattId == SubSubCategoriesList[i].subsubcategoryid) {
                        mBinding!!.rvSubSubCategory.scrollToPosition(i)
                    }
                }
            } else {
                subsubCatId = SubSubCategoriesList[0].subsubcategoryid
            }
            SubSubCategoriesList[0].isChecked = true
            subSubCategoryAdapter!!.setcategoryOrderFilterList(SubSubCategoriesList, subsubCatId)
        } else {
            subSubCategoryAdapter!!.setcategoryOrderFilterList(SubSubCategoriesList, 0)
            layoutHideUnHide(false)
        }
        mBinding!!.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
    }

    // sub sub category select
    override fun SubSubCategoryFilterClicked(
        pos: Int,
        subsubCatId: Int,
        scateId: Int,
        categoryId: Int
    ) {
        try {
            SelectedSSCIdFlag = true
            callItemMasterAPI(subsubCatId, scateId, categoryId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAnalytics(list: ArrayList<ItemListModel>) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, list.toString())
        RetailerSDKApp.getInstance().mFirebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM_LIST,
            bundle
        )
    }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    inner class ComparatorMoqAsc : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    inner class ComparatorMoqDes : Comparator<ItemListModel> {
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i2, i1)
        }
    }

    inner class ComparatorPriceAsc : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.unitPrice
            val i2 = rhs.unitPrice
            return java.lang.Double.compare(i1, i2)
        }
    }

    inner class ComparatorPriceDes : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.unitPrice
            val i2 = rhs.unitPrice
            return java.lang.Double.compare(i2, i1)
        }
    }

    inner class ComparatorMarginDes : Comparator<ItemListModel> {
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1: Double = if (lhs.marginPoint != null) lhs.marginPoint!!.toDouble() else 0.0
            val i2: Double = if (rhs.marginPoint != null) rhs.marginPoint!!.toDouble() else 0.0
            return java.lang.Double.compare(i2, i1)
        }
    }


    /**
     * fetch item master data
     */
    private val itemMasterDes: DisposableObserver<ItemListResponse>? =
        object : DisposableObserver<ItemListResponse>() {
            override fun onNext(o: ItemListResponse) {
                mBinding!!.proRelatedItem.visibility = View.GONE
                try {
                    if (o.isStatus) {
                        layoutHideUnHide(true)
                        list.clear()
                        inactiveItemList.clear()
                        val itemList = o.itemMasters
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
                        }
                        if (list.size != 0) {
                            Collections.sort(list, ComparatorOfNumericString())
                        }
                        if (inactiveItemList.size != 0) {
                            list.addAll(inactiveItemList)
                        }
                        if (list.size != 0 && inactiveItemList.size != 0) {
                            mBinding!!.filterTitle.text =
                                list.size.toString() + " " + RetailerSDKApp.getInstance()
                                    .dbHelper.getString(R.string.Items)
                            itemListAdapter = ItemListAdapter(homeActivity!!, list)
                            mBinding!!.rvCategoryItem.adapter = itemListAdapter
                            mBinding!!.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
                            updateAnalytics(list)
                        }
                    } else {
                        layoutHideUnHide(false)
                        itemListAdapter!!.setItemListCategory(list)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                SelectedSSCIdFlag = true
                this.dispose()
            }

            override fun onError(e: Throwable) {
                list.clear()
                itemListAdapter!!.setItemListCategory(list)
                // utils.setToast(activity,"Item not found!");
                e.printStackTrace()
                this.dispose()
                layoutHideUnHide(false)
            }

            override fun onComplete() {
                mBinding!!.proRelatedItem.visibility = View.GONE
            }
        }

    private fun subCategoryItemAPICall() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                mBinding!!.progressSubCat.visibility = View.VISIBLE
                commonClassForAPI!!.fetchSubCategory(
                    filterCategoryItemdes,
                    subCattId,
                    custId,
                    SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                    lang,
                    mSectionType
                )
            }
        } else {
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private val filterCategoryItemdes: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(jsonObject: JsonObject) {
                try {
                    Utils.hideProgressDialog()
                    val baseCategoryModel1 =
                        Gson().fromJson(jsonObject, BaseCategoriesModel::class.java)
                    if (baseCategoryModel1.subCategoryDC.size != 0 || baseCategoryModel1.subsubCategoryDc.size != 0) {
                        baseCategoryModel = baseCategoryModel1
                        subCategoryAPICall()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
            }

            override fun onComplete() {
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(): NewSubCategoryWebFragment {
            return NewSubCategoryWebFragment()
        }
    }
}