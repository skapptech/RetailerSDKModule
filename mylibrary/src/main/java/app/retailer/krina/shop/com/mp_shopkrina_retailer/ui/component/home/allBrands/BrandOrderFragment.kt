package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentOrderBrandsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ViewPagerAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SliderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubCatImageModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class BrandOrderFragment : Fragment() {
    private lateinit var mBinding: FragmentOrderBrandsBinding
    private lateinit var viewModel: AllBrandViewModel
    private lateinit var appCtx: MyApplication
    private lateinit var activity: HomeActivity
    private var adapter: ViewPagerAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var subCatId = 0
    private var categoryId = 0
    private var lang = ""
    private var selectSSCIdFlag = false
    private val list = ArrayList<ItemListModel>()
    private val sliderList = ArrayList<SliderModel>()


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
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_brands, container, false)
        val appRepository = AppRepository(activity.applicationContext)
        viewModel =
            ViewModelProvider(
                activity,
                AllBrandViewModelFactory(appCtx, appRepository)
            )[AllBrandViewModel::class.java]
        // get Argument
        if (arguments != null) {
            subCatId = arguments!!.getInt("subCatId")
            categoryId = arguments!!.getInt("Categoryid")
        }
        lang = LocaleHelper.getLanguage(activity)
        // show and hide view
        activity.topToolbarTitle!!.visibility = View.VISIBLE
        activity.searchText!!.visibility = View.VISIBLE
        activity.notification!!.visibility = View.VISIBLE

        // init view
        initialization()
        // API call
        observe(viewModel.getBrandItemData, ::handleBrandItemsResult)
        observe(viewModel.getCategoryImageData, ::handleCategoryImageResult)
        brandItemApi()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity,
            this.javaClass.simpleName,
            null
        )
        try {
            if (selectSSCIdFlag) {
                brandItemApi()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        brandItemdes.dispose()
    }


    fun initialization() {
        mBinding.noItems.text =
            MyApplication.getInstance().dbHelper.getString(R.string.items_not_available)
        mBinding.pager.startAutoScroll(4000)
        mBinding.pager.interval = 4000
        mBinding.pager.isCycle = true
        mBinding.pager.isStopScrollWhenTouch = true
        itemListAdapter =
            ItemListAdapter(
                activity,
                list
            )
        mBinding.rvBrandOrderItem.adapter = itemListAdapter
        itemListAdapter!!.notifyDataSetChanged()
        activity.bottomNavigationView!!.visibility = View.VISIBLE
    }

    fun setupViewpager(sliderList: ArrayList<SliderModel>) {
        try {
            if (sliderList.size > 0 && activity != null) {
                adapter = ViewPagerAdapter(activity, sliderList)
                mBinding.pager.adapter = adapter
                val density = resources.displayMetrics.density
                //mBinding.indicator.radius = 3 * density
                //mBinding.indicator.fillColor = -0x6600bb00
                mBinding.indicator.setViewPager(mBinding.pager)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun brandItemApi() {
        viewModel.getBrandItem(
            SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
            SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
            subCatId,
            lang
        )
        viewModel.getImage(categoryId)
    }

    private fun handleBrandItemsResult(it: Response<ItemListResponse>) {
        when (it) {
            is Response.Loading -> {
                mBinding.progressBar.visibility = View.VISIBLE
            }
            is Response.Success -> {
                it.data?.let {
                    mBinding.progressBar.visibility = View.GONE
                    selectSSCIdFlag = true
                    mBinding.relEmptyItem.visibility = View.GONE
                    list.clear()
                    val itemList = it.itemMasters
                    val list = viewModel.getMoqList(itemList!!)
                    if (list.size != 0) {
                        itemListAdapter!!.setItemListCategory(list)
                        MyApplication.getInstance().updateAnalyticVIL("brandItems", list)
                    }
                }
            }
            is Response.Error -> {
                mBinding.progressBar.visibility = View.GONE
                Utils.setToast(
                    activity,
                    it.errorMesssage.toString()
                )
                itemListAdapter!!.setItemListCategory(list)
                mBinding.relEmptyItem.visibility = View.VISIBLE
            }
        }
    }

    private fun handleCategoryImageResult(it: Response<ArrayList<SubCatImageModel>>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    try {
                        sliderList.clear()
                        for (i in it.indices) {
                            sliderList.add(
                                SliderModel(
                                    it[i].categoryimageid,
                                    it[i].categoryimg
                                )
                            )
                        }
                        setupViewpager(sliderList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                sliderList.add(
                    SliderModel(
                        4154,
                        "https://res.cloudinary.com/shopkirana/image/upload/v1551078737/banners/sk_banner.jpg"
                    )
                )
            }
        }
    }


    // Getting item response
    private val brandItemdes: DisposableObserver<ItemListResponse> =
        object : DisposableObserver<ItemListResponse>() {
            override fun onNext(o: ItemListResponse) {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()

            }

            override fun onComplete() {
                Utils.hideProgressDialog()
                mBinding.progressBar.visibility = View.GONE
            }
        }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): BrandOrderFragment {
            return BrandOrderFragment()
        }
    }
}