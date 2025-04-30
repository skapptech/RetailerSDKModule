package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.offer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityOfferItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubCategoryFilterAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

open class OfferItemFragment : Fragment(), SubSubCategoryFilterInterface, SubCategoryInterface {
    lateinit var binding: ActivityOfferItemBinding

    private lateinit var activity: HomeActivity
    private lateinit var utils: Utils
    private lateinit var commonClassForAPI: CommonClassForAPI
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()
    private var categoryList = ArrayList<CategoriesModel>()
    private var subCategoryItemList = ArrayList<SubCategoriesModel>()
    private val filterSubCategoryList = ArrayList<SubCategoriesModel>()
    private var subSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private val filterSubSubCategoriesList = ArrayList<SubSubCategoriesModel>()

    private var subCategoryFilterAdapter: SubCategoryFilterAdapter? = null
    private var subSubCategoryAdapter: SubSubCategoryAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null

    private var lang = ""
    private var warehouseId = 0
    private var custId = 0
    private var baseCategoryId = 0
    private var categoryId = 0
    private var subCatId = 0
    private var subSubCattId = 0
    private var skip = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.activity_offer_item, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
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
    }


    // sub category select
    override fun SubCategoryClicked(SubCategoryId: Int, CategoryId: Int) {
        setSubSubCategory(SubCategoryId, CategoryId)
        binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
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


    private fun init() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)

        binding.rvSubSubCategory.setHasFixedSize(true)
        binding.rvSubSubCategory.isNestedScrollingEnabled = false

        subSubCategoryAdapter = SubSubCategoryAdapter(activity, filterSubSubCategoriesList, this)
        binding.rvSubSubCategory.adapter = subSubCategoryAdapter
        mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvCategoryItem.layoutManager = mLinearLayoutManager
        binding.rvCategoryItem.isNestedScrollingEnabled = false

        itemListAdapter =
            ItemListAdapter(
                activity,
                list
            )
        binding.rvCategoryItem.adapter = itemListAdapter
        binding.rvSubCategory.setHasFixedSize(true)
        subCategoryFilterAdapter = SubCategoryFilterAdapter(activity, filterSubCategoryList, this)
        binding.rvSubCategory.adapter = subCategoryFilterAdapter
        binding.rvSubCategory.isNestedScrollingEnabled = false
        subCategoryFilterAdapter!!.basCatId = baseCategoryId

        binding.noItems.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_items_avl)

        binding.nestedScroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val visibleItemCount = mLinearLayoutManager.childCount
                    val totalItemCount = mLinearLayoutManager.itemCount
                    val pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        Log.i("Nested", "BOTTOM SCROLL")
//                        if (isLoadDataForRelatedItem) {
//                            if (list.size > 2) {
//                                skip += 10
//                                subSubCatViewModel.fetchItemList1(
//                                    custId,
//                                    subSubCattId,
//                                    subCatId,
//                                    categoryId,
//                                    lang,
//                                    skip,
//                                    10,
//                                    "",
//                                    ""
//                                )
//                            }
//                        }
                    }
                }
            }
        }
    }

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
            binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
            for (ii in subSubCategoriesList.indices) {
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
                val subsubCatId = filterSubSubCategoriesList[0].subsubcategoryid

                filterSubSubCategoriesList[0].isChecked = true
                subSubCategoryAdapter!!.setcategoryOrderFilterList(
                    filterSubSubCategoriesList,
                    subsubCatId
                )
                val finalSubsubCatId = subsubCatId
                val checkList =
                    filterSubSubCategoriesList.filter { s: SubSubCategoriesModel -> s.subsubcategoryid == finalSubsubCatId }
                if (checkList == null || checkList.isEmpty()) {
                    binding.noItems.visibility = View.VISIBLE
                }
                for (i in filterSubSubCategoriesList.indices) {
                    if (filterSubSubCategoriesList[i].subsubcategoryid == subsubCatId) {
                        binding.rvSubSubCategory.scrollToPosition(i)
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

    private fun layoutHideUnHide(value: Boolean) {
        if (value) {
            binding.noItems.visibility = View.GONE
            binding.rvCategoryItem.visibility = View.VISIBLE
        } else {
            binding.noItems.visibility = View.VISIBLE
            binding.rvCategoryItem.visibility = View.GONE
        }
    }

    private fun callItemMasterAPI(sSubCatId: Int, subCatId: Int, categoryId: Int) {
        if (utils.isNetworkAvailable) {
            skip = 0
            list.clear()
            itemListAdapter!!.notifyDataSetChanged()
            binding.proRelatedItem.visibility = View.VISIBLE
//            subSubCatViewModel.fetchItemList(
//                custId, sSubCatId, if (isStore) storeId else subCatId,
//                categoryId, lang, 0, 10, sortType, direction
//            )
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): OfferItemFragment {
            return OfferItemFragment()
        }
    }

}