package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.BasecategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchFilterModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemHistoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemHistoryTitleList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentSearchItemListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SearchFilterPopupBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BaseCatClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BrandClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.CategoryClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.Searchclick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCatClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.NoInternetActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SearchHistoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONObject
import java.util.Collections
import java.util.Locale

class SearchItemFragment : Fragment(), BaseCatClicked, Searchclick, CategoryClicked,
    SubCatClicked, BrandClicked {
    private val SPEECH_REQUEST_CODE = 123
    private var activity: HomeActivity? = null
    private lateinit var appCtx: RetailerSDKApp
    private lateinit var viewModel: SearchItemViewModel
    private var mBinding: FragmentSearchItemListBinding? = null
    private var categoryList: ArrayList<BasecategoryModel>? = null
    private var SubcategoryList: ArrayList<BasecategoryModel>? = null
    private var SubSubcategoryList: ArrayList<BasecategoryModel>? = null
    private var list: ArrayList<ArrayList<BasecategoryModel>?>? = null
    private var baseCateList: ArrayList<BasecategoryModel>? = ArrayList()
    private var brand: MutableList<Int>? = null
    private var subcat: MutableList<Int>? = null
    private var cat: MutableList<Int>? = null
    private var mBaseCategoryIdList: MutableList<Int>? = null
    private var historyItemList: ArrayList<SearchItemHistoryTitleList>? = null
    private var mItemListArrayList: ArrayList<ItemListModel>? = null
    private var searchHistoryList: ArrayList<SearchHistoryModel>? = null
    private var searchHintAdapter: SearchHistoryItemTitleAdapter? = null
    private var searchHistoryAdapter: SearchHintAdapter? = null
    private var searchFilterCatChildAdapter: SearchFilterCatChildAdapter? = null
    private var searchFilterSubCatChildAdapter: SearchFilterSubCatChildAdapter? = null
    private var searchFilterBrandAdapter: SearchFilterBrandAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var popupWindow: PopupWindow? = null
    private var custId = 0
    private var lang = ""
    private var searchString = ""
    private var mSearchFlag = false
    private var baseCateChecked = false
    private var cateChecked = false
    private var subcateChecked = false
    private var maxprice = 10000
    private var minprice = 0
    private var pricefilter = true


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as RetailerSDKApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_item_list, container, false)
        val appRepository = AppRepository(activity!!.applicationContext)
        viewModel =
            ViewModelProvider(
                activity!!,
                SearchItemViewModelFactory(appCtx, appRepository)
            )[SearchItemViewModel::class.java]
        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        if (netConnectionReceiver != null) {
            activity!!.registerReceiver(
                netConnectionReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                , Context.RECEIVER_NOT_EXPORTED
            )
        }
        if (mSearchFlag) {
            if (itemListAdapter != null) {
                itemListAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initialization()
        // set adapter
        searchHintAPICall()
        //CallAPI
        allCategoryApiCall()
        if (arguments != null) {
            val pos = arguments!!.getInt("pos")
            val query = arguments!!.getString("query")
            if (pos == 67) {
                mBinding!!.fragSearchEdt.setText(query)
                callApi()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (netConnectionReceiver != null) {
            activity!!.unregisterReceiver(netConnectionReceiver)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data) {
            var searchString = ""
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchString = result!![0]
            mBinding!!.fragSearchEdt.setText(searchString)
            callApi()
            RetailerSDKApp.getInstance().updateAnalyticSearch(searchString)
            RetailerSDKApp.getInstance().updateAnalytics("voice_search")
        } else if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            callApi()
        }
    }

    override fun getSearchString(word: String) {
        Utils.hideKeyboard(activity, view)
        mBinding!!.fragSearchEdt.setText(word)
        if (mBinding!!.fragSearchEdt.text.length > 1) {
            val searchDataPostModel =
                SearchItemRequestModel(
                    brand!!, subcat!!,
                    cat!!, mBaseCategoryIdList!!, maxprice, minprice, custId, searchString, lang, ""
                )
            viewModel.getSearchData(searchDataPostModel)
            RetailerSDKApp.getInstance().updateAnalyticSearch(searchString)
        }
    }

    override fun getKeyword(word: String) {
        viewModel.deleteSearchHintItem(custId, word)
        viewModel.searchHintItemsDeleteData.observe(this) {
            if (it == "true") {
                searchHistoryAPICall()
            }
        }
    }

    override fun getPosition(position: Int) {
        setCategory(position)
    }

    override fun onCategoryClicked(position: Int) {
        setSubCategory()
    }

    override fun onSubCatClicked(position: Int) {
        setSubSubCategory()
    }

    override fun onBrandClicked(position: Int) {
        brand!!.clear()
        for (i in SubSubcategoryList!!.indices) {
            if (SubSubcategoryList!![i].isChecked) {
                brand!!.add(SubSubcategoryList!![i].subSubCategoryId)
            }
        }
    }

    private fun searchHistoryAPICall() {
        viewModel.getSearchItemHint(
            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
            0,
            10,
            LocaleHelper.getLanguage(activity)
        )
    }

    private fun searchHintAPICall() {
        val dataSaved = SharePrefs.getInstance(activity).getString(SharePrefs.SEARCH_HINT_DATA)
        if (!TextUtils.isNullOrEmpty(dataSaved)) {
            try {
                historyItemList!!.clear()
                val jsonObject = JSONObject(dataSaved)
                val searchHintModel =
                    Gson().fromJson(jsonObject.toString(), SearchItemHistoryModel::class.java)
                prepareSearchHintData(searchHintModel)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            viewModel.getSearchItemHistory(
                SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                0,
                15,
                LocaleHelper.getLanguage(activity)
            )
        }
    }

    private fun initialization() {
        observe(viewModel.getAllCategoriesData, ::handleAllCategoryResult)
        observe(viewModel.getSearchItemHistoryData, ::handleSearchItemHistoryResult)
        observe(viewModel.searchItemHintData, ::handleSearchItemHintResult)
        observe(viewModel.searchItemsData, ::handleSearchItemResult)

        historyItemList = ArrayList()
        mItemListArrayList = ArrayList()
        searchHistoryList = ArrayList()
        categoryList = ArrayList()
        SubcategoryList = ArrayList()
        SubSubcategoryList = ArrayList()
        popupWindow = PopupWindow()
        list = ArrayList()
        brand = ArrayList()
        subcat = ArrayList()
        cat = ArrayList()
        mBaseCategoryIdList = ArrayList()
        searchHistoryAdapter = SearchHintAdapter(activity!!, this)
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        lang = LocaleHelper.getLanguage(activity)
        activity!!.spLayout!!.visibility = View.GONE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        activity!!.searchText!!.visibility = View.GONE
        activity!!.topToolbarTitle!!.visibility = View.GONE
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding!!.fragSearchEdt.isCursorVisible = true
        mBinding!!.fragSearchRv.layoutManager = LinearLayoutManager(activity)
        searchHintAdapter = SearchHistoryItemTitleAdapter(activity!!, historyItemList!!)
        itemListAdapter =
            ItemListAdapter(
                activity!!,
                mItemListArrayList
            )
        mBinding!!.fragSearchEdt.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.hint_search_kisan)
        mBinding!!.txtPrice.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.price)
        mBinding!!.tvFilter.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.Filter)
        mBinding!!.checkoutBtn.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.check_cart)
        mBinding!!.relEmptyItem.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.itemavailable)
        mBinding!!.llFilter.setOnClickListener { v: View? -> SearchFilterPopup() }
        mBinding!!.llPriceFilter.setOnClickListener { view: View? -> priceFilter() }
        mBinding!!.fragSearchEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                searchString = editable.toString().trim { it <= ' ' }
                mBinding!!.pbLoader.visibility = View.GONE
                if (searchString.length == 1) {
                    searchHistoryAPICall()
                }
                if (searchString.length == 0) {
                    mItemListArrayList!!.clear()
                    itemListAdapter!!.notifyDataSetChanged()
                    mBinding!!.priceFilterlayout.visibility = View.GONE
                    mBinding!!.relEmptyItem.visibility = View.GONE
                    mBinding!!.fragSearchRv.visibility = View.VISIBLE
                    searchHintAPICall()
                }
            }
        })
        mBinding!!.fragSearchEdt.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                callApi()
                return@setOnEditorActionListener true
            }
            false
        }
        // voice search
        mBinding!!.ivVoiceSearch.setOnClickListener { v: View? -> showGoogleInputDialog() }
        mBinding!!.fragSearchEdt.requestFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mBinding!!.fragSearchEdt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun allCategoryApiCall() {
        val dataSaved = SharePrefs.getInstance(activity).getString(SharePrefs.ALL_CATEGORY_SERACH)
        if (!TextUtils.isNullOrEmpty(dataSaved)) {
            val searchFilterModel = Gson().fromJson(dataSaved, SearchFilterModel::class.java)
            retrieveData(searchFilterModel)
        } else {
            viewModel.getAllCategories(
                custId,
                SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                LocaleHelper.getLanguage(activity)
            )
        }
    }

    private fun SearchFilterPopup() {
        Utils.hideKeyboard(activity, view)
        val mBindingFilter = DataBindingUtil.inflate<SearchFilterPopupBinding>(
            layoutInflater,
            R.layout.search_filter_popup, null, false
        )
        // set text
        mBindingFilter.tvHeader.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Tiltle_BaseCategories)
        mBindingFilter.tvCatHeader.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_categories)
        mBindingFilter.tvSubCatHeader.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Tiltle_SubCategories)
        mBindingFilter.tvSubSubCatHeader.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Tiltle_brand)
        mBindingFilter.btnApply.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.apply)
        setRangeSeekbar1(mBindingFilter)
        popupWindow!!.height = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow!!.elevation = 20f
        }
        popupWindow!!.contentView = mBindingFilter.root
        popupWindow!!.showAtLocation(mBindingFilter.root, Gravity.TOP or Gravity.END, 0, 0)

        // set Layout Manager
        mBindingFilter.rvBasechild.layoutManager = LinearLayoutManager(activity)
        mBindingFilter.rvCatChild.layoutManager = LinearLayoutManager(activity)
        mBindingFilter.rvSubcatChild.layoutManager = LinearLayoutManager(activity)
        mBindingFilter.rvBrandChild.layoutManager = LinearLayoutManager(activity)
        setAdapterValue(mBindingFilter)
        mBindingFilter.back.setOnClickListener { view: View? ->
            popupWindow!!.dismiss()
            mBaseCategoryIdList!!.clear()
            cat!!.clear()
            subcat!!.clear()
            brand!!.clear()
            maxprice = 10000
            minprice = 0
        }
        mBindingFilter.btnApply.setOnClickListener { view: View? ->
            if (!TextUtils.isNullOrEmpty(searchString)) {
                val searchDataPostModel =
                    SearchItemRequestModel(
                        brand!!,
                        subcat!!,
                        cat!!,
                        mBaseCategoryIdList!!,
                        maxprice,
                        minprice,
                        custId,
                        searchString,
                        lang,
                        ""
                    )
                viewModel.getSearchData(searchDataPostModel)
                RetailerSDKApp.getInstance().updateAnalyticSearch(searchString)
            } else {
                Toast.makeText(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.first_search_something),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        mBindingFilter.clearAllFilter.setOnClickListener { view: View? ->
            //CallAPI
            //allCategoryApiCall();
            clearFilter()
            setAdapterValue(mBindingFilter)
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.clear_filter)
            )
        }
        mBindingFilter.llPrice.setOnClickListener { view: View? ->
            if (mBindingFilter.sliderFilter.visibility == View.VISIBLE) {
                mBindingFilter.sliderFilter.visibility = View.GONE
            } else {
                mBindingFilter.sliderFilter.visibility = View.VISIBLE
            }
        }
        mBindingFilter.baseCatHeader.setOnClickListener { view: View? ->
            if (mBindingFilter.rvBasechild.visibility == View.VISIBLE) {
                mBindingFilter.rvBasechild.visibility = View.GONE
            } else {
                mBindingFilter.rvBasechild.visibility = View.VISIBLE
            }
            if (mBindingFilter.rvCatChild.visibility == View.VISIBLE) {
                mBindingFilter.rvCatChild.visibility = View.GONE
            }
            if (mBindingFilter.rvSubcatChild.visibility == View.VISIBLE) {
                mBindingFilter.rvSubcatChild.visibility = View.GONE
            }
            if (mBindingFilter.rvBrandChild.visibility == View.VISIBLE) {
                mBindingFilter.rvBrandChild.visibility = View.GONE
            }
        }
        mBindingFilter.catHeader.setOnClickListener { view: View? ->
            for (i in baseCateList!!.indices) {
                if (baseCateList!![i].isChecked) {
                    baseCateChecked = true
                    break
                }
            }
            if (!baseCateChecked) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.first_select_base_category)
                )
            } else {
                if (mBindingFilter.rvCatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvCatChild.visibility = View.GONE
                } else {
                    mBindingFilter.rvCatChild.visibility = View.VISIBLE
                }
                if (mBindingFilter.rvBasechild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBasechild.visibility = View.GONE
                }
                if (mBindingFilter.rvSubcatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvSubcatChild.visibility = View.GONE
                }
                if (mBindingFilter.rvBrandChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBrandChild.visibility = View.GONE
                }
            }
        }
        mBindingFilter.subcatHeader.setOnClickListener { view: View? ->
            if (!cateChecked) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.first_select_category)
                )
            } else {
                if (mBindingFilter.rvSubcatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvSubcatChild.visibility = View.GONE
                } else {
                    mBindingFilter.rvSubcatChild.visibility = View.VISIBLE
                }
                if (mBindingFilter.rvBasechild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBasechild.visibility = View.GONE
                }
                if (mBindingFilter.rvCatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvCatChild.visibility = View.GONE
                }
                if (mBindingFilter.rvBrandChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBrandChild.visibility = View.GONE
                }
            }
        }
        mBindingFilter.brandHeader.setOnClickListener { view: View? ->
            if (!subcateChecked) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.first_select_subcat)
                )
            } else {
                if (mBindingFilter.rvBrandChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBrandChild.visibility = View.GONE
                } else {
                    mBindingFilter.rvBrandChild.visibility = View.VISIBLE
                }
                if (mBindingFilter.rvSubcatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvSubcatChild.visibility = View.GONE
                }
                if (mBindingFilter.rvBasechild.visibility == View.VISIBLE) {
                    mBindingFilter.rvBasechild.visibility = View.GONE
                }
                if (mBindingFilter.rvCatChild.visibility == View.VISIBLE) {
                    mBindingFilter.rvCatChild.visibility = View.GONE
                }
            }
        }
    }

    private fun clearFilter() {
        mBaseCategoryIdList!!.clear()
        cat!!.clear()
        subcat!!.clear()
        brand!!.clear()
        maxprice = 10000
        minprice = 0
        for (i in baseCateList!!.indices) {
            baseCateList!![i].isChecked = false
        }
        if (list!!.size > 0) {
            for (i in list!![1]!!.indices) {
                list!![1]!![i].isChecked = false
            }
            for (i in list!![2]!!.indices) {
                list!![2]!![i].isChecked = false
            }
            for (i in list!![3]!!.indices) {
                list!![3]!![i].isChecked = false
            }
        }
    }

    private fun setAdapterValue(mbinding: SearchFilterPopupBinding) {
        var pos = -1
        for (i in baseCateList!!.indices) {
            if (baseCateList!![i].isChecked) {
                baseCateChecked = true
                pos = i
                break
            }
        }
        if (baseCateList!!.size != 0) {
            val searchFilterBaseCatAdapter = SearchFilterBaseCatAdapter(
                baseCateList!!, activity!!, this, pos
            )
            mbinding.rvBasechild.adapter = searchFilterBaseCatAdapter
        }
        searchFilterCatChildAdapter = SearchFilterCatChildAdapter(activity!!, this)
        mbinding.rvCatChild.adapter = searchFilterCatChildAdapter
        if (pos >= 0) {
            setCategory(pos)
        }
        searchFilterSubCatChildAdapter = SearchFilterSubCatChildAdapter(activity!!, this)
        mbinding.rvSubcatChild.adapter = searchFilterSubCatChildAdapter
        setSubCategory()
        searchFilterBrandAdapter = SearchFilterBrandAdapter(activity!!, this)
        mbinding.rvBrandChild.adapter = searchFilterBrandAdapter
        setSubSubCategory()
    }

    private fun setRangeSeekbar1(mBindingFilter: SearchFilterPopupBinding) {
        // get seekbar from view
        val rangeSeekbar = mBindingFilter.rangeSeekbar
        // get min and max text view
        val tvMin = mBindingFilter.tvMinrange
        val tvMax = mBindingFilter.tvMaxrange
        val tvprice = mBindingFilter.tvPrice
        // Set initial range
        rangeSeekbar.values = listOf(minprice.toFloat(), maxprice.toFloat())

        rangeSeekbar.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val min = values[0].toInt()
            val max = values[1].toInt()

            tvMin.text = "$min"
            tvMax.text = "$max"
            tvprice.text = "$min - $max"
        }
//        rangeSeekbar.setRangeValues(minprice, maxprice)
//        // set listener
//        rangeSeekbar.setOnRangeSeekBarChangeListener(fun(
//            bar: RangeSeekBar<*>,
//            minValue: Any,
//            maxValue: Any
//        ) {
//            tvMin.text = "" + minValue
//            tvMax.text = "" + maxValue
//            maxprice = maxValue as Int
//            minprice = minValue as Int
//            tvprice.text = "$minprice-$maxprice"
//        })
    }

    private fun setCategory(pos: Int) {
        categoryList!!.clear()
        mBaseCategoryIdList!!.clear()
        if (list!![1]!!.size != 0) {
            for (k in list!![0]!!.indices) {
                if (list!![0]!![k].isChecked) {
                    val id = list!![0]!![k].baseCategoryId
                    mBaseCategoryIdList!!.add(id)
                    for (i in list!![1]!!.indices) {
                        if (id == list!![1]!![i].baseCategoryId) {
                            categoryList!!.add(list!![1]!![i])
                        }
                    }
                }
            }
        }
        searchFilterCatChildAdapter!!.setData(categoryList)
    }

    private fun setSubCategory() {
        try {
            cateChecked = false
            SubcategoryList!!.clear()
            cat!!.clear()
            if (categoryList!!.size != 0) {
                for (i in categoryList!!.indices) {
                    // list.get(2).get(i).setChecked(false);
                    if (categoryList!![i].isChecked) {
                        cat!!.add(categoryList!![i].categoryid)
                        for (j in list!![2]!!.indices) {
                            if (categoryList!![i].categoryid == list!![2]!![j].categoryid) {
                                SubcategoryList!!.add(list!![2]!![j])
                            }
                        }
                        cateChecked = true
                    }
                }
            }
            //if (SubSubcategoryList.size() != 0) {
            searchFilterSubCatChildAdapter!!.setData(SubcategoryList)
            // }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSubSubCategory() {
        try {
            subcateChecked = false
            SubSubcategoryList!!.clear()
            subcat!!.clear()
            for (i in SubcategoryList!!.indices) {
                //  list.get(3).get(i).setChecked(false);
                if (SubcategoryList!![i].isChecked) {
                    subcat!!.add(SubcategoryList!![i].subCategoryId)
                    for (j in list!![3]!!.indices) {
                        if (SubcategoryList!![i].subCategoryId == list!![3]!![j].subCategoryId && SubcategoryList!![i].categoryid == list!![3]!![j].categoryid) {
                            SubSubcategoryList!!.add(list!![3]!![j])
                            //SubSubcategoryList.get(0).setChecked(true);
                            val gson = Gson()
                            SharePrefs.setStringSharedPreference(
                                activity,
                                SharePrefs.CITY_ID,
                                gson.toJson(SubSubcategoryList)
                            )
                            val id =
                                SharePrefs.getStringSharedPreferences(activity, SharePrefs.CITY_ID)
                        }
                    }
                    subcateChecked = true
                }
            }
            // if (SubSubcategoryList.size() != 0) {
            searchFilterBrandAdapter!!.setData(SubSubcategoryList)
            //}
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun retrieveData(searchFilterModel: SearchFilterModel) {
        list!!.clear()
        list!!.add(searchFilterModel.baseCatList)
        list!!.add(searchFilterModel.categoriesList)
        list!!.add(searchFilterModel.subCategoriesModelList)
        list!!.add(searchFilterModel.subSubCategoriesModelList)
        baseCateList = searchFilterModel.baseCatList
    }

    private fun callApi() {
        if (mBinding!!.fragSearchEdt.text.length > 1) {
            val searchDataPostModel =
                SearchItemRequestModel(
                    brand!!,
                    subcat!!,
                    cat!!,
                    mBaseCategoryIdList!!,
                    maxprice,
                    minprice,
                    custId,
                    searchString,
                    lang,
                    ""
                )
            viewModel.getSearchData(searchDataPostModel)
            RetailerSDKApp.getInstance().updateAnalyticSearch(searchString)

        }
    }

    private fun showGoogleInputDialog() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
            RetailerSDKApp.getInstance().updateAnalytics("voice_search_click")
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                context,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.your_device_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun priceFilter() {
        try {
            if (pricefilter) {
                mBinding!!.ivPricefilter.setImageResource(R.drawable.ic_price_down_filter)
                pricefilter = false
                Collections.sort(mItemListArrayList, ComparatorOfDescending())
                itemListAdapter!!.setItemListCategory(mItemListArrayList)
            } else {
                pricefilter = true
                mBinding!!.ivPricefilter.setImageResource(R.drawable.ic_price_up_filter)
                Collections.sort(mItemListArrayList, ComparatorOfAscending())
                itemListAdapter!!.setItemListCategory(mItemListArrayList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareSearchHintData(searchHintModel: SearchItemHistoryModel) {
        historyItemList!!.clear()
        if (!TextUtils.isNullOrEmpty(searchHintModel.recentSearchItem)) {
            historyItemList!!.add(
                SearchItemHistoryTitleList(
                    createMOQ(searchHintModel.recentSearchItem),
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_recent_search)
                )
            )
        }
        if (!TextUtils.isNullOrEmpty(searchHintModel.mostSellingProduct)) {
            historyItemList!!.add(
                SearchItemHistoryTitleList(
                    createMOQ(searchHintModel.mostSellingProduct),
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_most_selling)
                )
            )
        }
        if (!TextUtils.isNullOrEmpty(searchHintModel.recentPurchase)) {
            historyItemList!!.add(
                SearchItemHistoryTitleList(
                    createMOQ(searchHintModel.recentPurchase),
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_recent_purchase)
                )
            )
        }
        if (!TextUtils.isNullOrEmpty(searchHintModel.custFavoriteItem)) {
            historyItemList!!.add(
                SearchItemHistoryTitleList(
                    createMOQ(searchHintModel.custFavoriteItem),
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_your_favourite)
                )
            )
        }
        mBinding!!.fragSearchRv.adapter = searchHintAdapter
        searchHintAdapter!!.setData(historyItemList!!)
    }

    private fun createMOQ(responseList: ArrayList<ItemListModel>?): ArrayList<ItemListModel> {
        val itemList = ArrayList<ItemListModel>()
        for (i in responseList!!.indices) {
            var ispresent = false
            for (j in itemList.indices) {
                if (responseList[i].itemNumber != null && itemList[j]
                        .itemNumber.equals(responseList[i].itemNumber, ignoreCase = true)
                ) {
                    ispresent = true
                    if (itemList[j].moqList.size == 0) {
                        itemList[j].moqList.add(itemList[j])
                        itemList[j].moqList[0].isChecked = true
                    }
                    itemList[j].moqList.add(responseList[i])
                    break
                }
            }
            if (!ispresent) {
                itemList.add(responseList[i])
            }
        }
        return itemList
    }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    inner class ComparatorOfAscending : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.unitPrice
            val i2 = rhs.unitPrice
            return java.lang.Double.compare(i1, i2)
        }
    }

    inner class ComparatorOfDescending : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.unitPrice
            val i2 = rhs.unitPrice
            return java.lang.Double.compare(i2, i1)
        }
    }

    private val netConnectionReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var intent = intent
            val status = Utils.getConnectivityStatusString(context)
            intent = Intent("netStatus")
            intent.putExtra("status", status)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            if (!status) {
                startActivityForResult(Intent(activity, NoInternetActivity::class.java), 222)
            }
        }
    }

    private fun handleSearchItemHistoryResult(it: Response<SearchItemHistoryModel>) {
        when (it) {
            is Response.Loading -> {
                mBinding!!.pbLoader.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    mBinding!!.pbLoader.visibility = View.GONE
                    historyItemList!!.clear()
                    val jsonInString = Gson().toJson(it)
                    val mJSONObject = JSONObject(jsonInString)
                    SharePrefs.getInstance(activity)
                        .putString(SharePrefs.SEARCH_HINT_DATA, mJSONObject.toString())
                    prepareSearchHintData(it)
                }
            }

            is Response.Error -> {
                mBinding!!.pbLoader.visibility = View.GONE
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_response)
                )
            }
        }
    }

    private fun handleAllCategoryResult(it: Response<SearchFilterModel>) {
        when (it) {
            is Response.Loading -> {}

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    SharePrefs.getInstance(activity)
                        .putString(SharePrefs.ALL_CATEGORY_SERACH, Gson().toJson(it))
                    retrieveData(it)
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_response)
                )
            }
        }
    }

    private fun handleSearchItemHintResult(it: Response<JsonArray>) {
        when (it) {
            is Response.Loading -> {}

            is Response.Success -> {
                it.data?.let {
                    searchHistoryList!!.clear()
                    for (i in 0 until it.size()) {
                        searchHistoryList!!.add(SearchHistoryModel(it[i].asString))
                    }
                    searchHistoryAdapter!!.setData(searchHistoryList)
                    mBinding!!.fragSearchRv.adapter = searchHistoryAdapter
                }
            }

            is Response.Error -> {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_response)
                )
            }
        }
    }

    private fun handleSearchItemResult(it: Response<ItemListResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    try {
                        mItemListArrayList!!.clear()
                        val itemList = it.itemMasters
                        if (it.isStatus && itemList != null && itemList.size > 0) {
                            clearFilter()
                            popupWindow!!.dismiss()
                            SubcategoryList!!.clear()
                            categoryList!!.clear()
                            mItemListArrayList!!.clear()
                            SubSubcategoryList!!.clear()
                            mBinding!!.relEmptyItem.visibility = View.GONE
                            mBinding!!.fragSearchRv.visibility = View.VISIBLE
                            mSearchFlag = true
                            mBinding!!.fragSearchRv.adapter = itemListAdapter
                            if (itemList.size != 0) {
                                Collections.sort(itemList, ComparatorOfNumericString())
                            }
                            mItemListArrayList = viewModel.getMoqList(itemList)

                            if (mItemListArrayList!!.size != 0) {
                                itemListAdapter!!.setItemListCategory(mItemListArrayList)
                                mBinding!!.priceFilterlayout.visibility = View.VISIBLE
                            }
                            val baseCatID = ArrayList<Int>()
                            val catID = ArrayList<Int>()
                            val subCatID = ArrayList<Int>()
                            val subSubCatID = ArrayList<Int>()
                            for (i in mItemListArrayList!!.indices) {
                                if (!baseCatID.contains(Integer.valueOf(mItemListArrayList!![i].baseCategoryId))) {
                                    baseCatID.add(Integer.valueOf(mItemListArrayList!![i].baseCategoryId))
                                }
                                if (!catID.contains(Integer.valueOf(mItemListArrayList!![i].categoryid))) {
                                    catID.add(Integer.valueOf(mItemListArrayList!![i].categoryid))
                                }
                                if (!subCatID.contains(Integer.valueOf(mItemListArrayList!![i].subCategoryId))) {
                                    subCatID.add(Integer.valueOf(mItemListArrayList!![i].subCategoryId))
                                }
                                if (!subSubCatID.contains(Integer.valueOf(mItemListArrayList!![i].subsubCategoryid))) {
                                    subSubCatID.add(Integer.valueOf(mItemListArrayList!![i].subsubCategoryid))
                                }
                            }
                            if (list!!.size > 0) {
                                for (j in baseCatID.indices) {
                                    for (i in list!![0]!!.indices) {
                                        if (baseCatID[j] == list!![0]!![i].baseCategoryId) {
                                            list!![0]!![i].isChecked = true
                                        }
                                    }
                                }
                                for (j in catID.indices) {
                                    for (i in list!![1]!!.indices) {
                                        if (catID[j] == list!![1]!![i].categoryid) {
                                            list!![1]!![i].isChecked = true
                                        }
                                    }
                                }
                                for (j in subCatID.indices) {
                                    for (i in list!![2]!!.indices) {
                                        if (subCatID[j] == list!![2]!![i].subCategoryId) {
                                            list!![2]!![i].isChecked = true
                                        }
                                    }
                                }
                                for (j in subSubCatID.indices) {
                                    for (i in list!![3]!!.indices) {
                                        if (subSubCatID[j] == list!![3]!![i].subSubCategoryId) {
                                            list!![3]!![i].isChecked = true
                                        }
                                    }
                                }
                            }
                            // analytics data
                            if (mItemListArrayList!!.size > 0) RetailerSDKApp.getInstance()
                                .updateAnalyticVSR(mItemListArrayList)
                            // add item to database
                            RetailerSDKApp.getInstance().noteRepository.addToSearch(
                                mItemListArrayList,
                                searchString
                            )
                        } else {
                            itemListAdapter!!.setItemListCategory(mItemListArrayList)
                            mBinding!!.relEmptyItem.visibility = View.VISIBLE
                            mBinding!!.fragSearchRv.visibility = View.GONE
                            mBinding!!.fragSearchRv.adapter = null
                            mBinding!!.priceFilterlayout.visibility = View.GONE
                            popupWindow!!.dismiss()
                            popupWindow!!.dismiss()
                            mBaseCategoryIdList!!.clear()
                            cat!!.clear()
                            subcat!!.clear()
                            brand!!.clear()
                            maxprice = 10000
                            minprice = 0
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchItemFragment {
            return SearchItemFragment()
        }
    }
}