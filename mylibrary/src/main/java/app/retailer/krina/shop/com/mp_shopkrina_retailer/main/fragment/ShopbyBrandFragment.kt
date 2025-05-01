package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentShopByBrandBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.util.*

class ShopbyBrandFragment : Fragment() {
    private lateinit var mBinding: FragmentShopByBrandBinding
    var homeActivity = activity as? HomeActivity

    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()
    private var itemListAdapter: ItemListAdapter? = null

    private var commonClassForAPI: CommonClassForAPI? = null
    private var BaseCategoryId = 0
    private var custId = 0
    private var sBrandNames: String? = null
    private var lang = ""
    private var mSectionType: String? = ""
    private var SelectedSSCIdFlag = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shop_by_brand, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            BaseCategoryId = arguments?.getString("ItemId")!!.toInt()
            sBrandNames = arguments?.getString("BRAND_NAME")
            mSectionType = arguments?.getString("SectionType")
        }
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE

        // Init view
        initialization()
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemlistdes.dispose()
    }

    private fun brandItemApi() {
        mBinding.progressBrand.visibility = View.VISIBLE
        if (commonClassForAPI != null) {
            if (sBrandNames.equals("Brand", ignoreCase = true) || sBrandNames.equals(
                    "Banner Brand",
                    ignoreCase = true
                )
            ) {
                commonClassForAPI!!.FetchShopByBrand(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    lang,
                    "$mSectionType ShopbyBrandFragment"
                )
            } else if (sBrandNames.equals("Brand", ignoreCase = true)) {
                commonClassForAPI!!.fetchBannerItems(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    "$mSectionType ShopbyBrandFragment"
                )
            } else if (sBrandNames.equals("Item", ignoreCase = true)) {
                commonClassForAPI!!.fetchBannerItemDetail(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    "$mSectionType ShopbyBrandFragment"
                )
            } else if (sBrandNames.equals("BannerClickOffer", ignoreCase = true)) {
                commonClassForAPI!!.fetchBannerOffers(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    "$mSectionType ShopbyBrandFragment"
                )
            } else if (sBrandNames.equals("PopupClick", ignoreCase = true)) {
                commonClassForAPI!!.fetchBannerItems(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    "$mSectionType ShopbyBrandFragment"
                )
            } else {
                commonClassForAPI!!.FetchSpecialCategory(
                    itemlistdes,
                    custId,
                    BaseCategoryId,
                    lang,
                    "$mSectionType ShopbyBrandFragment"
                )
            }
        }
    }

    fun initialization() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        lang = LocaleHelper.getLanguage(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        itemListAdapter =
            ItemListAdapter(
                homeActivity!!,
                list
            )
        mBinding.rvBrandItem.adapter = itemListAdapter
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding.noItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.items_not_available)

        // Swipe refresh
        mBinding.swipeContainer.setOnRefreshListener { brandItemApi() }

        // Brand API
        if (Utils.isNetworkAvailable(activity)) {
            brandItemApi()
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            return lhs.minOrderQty.compareTo(rhs.minOrderQty)
        }
    }

    /**
     * item details
     */
    private val itemlistdes: DisposableObserver<ItemListResponse> =
        object : DisposableObserver<ItemListResponse>() {
            override fun onNext(o: ItemListResponse) {
                mBinding.progressBrand.visibility = View.GONE
                mBinding.swipeContainer.isRefreshing = false
                list.clear()
                inactiveItemList.clear()
                try {
                    if (o.itemMasters != null) {
                        SelectedSSCIdFlag = true
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
                            itemListAdapter!!.setItemListCategory(list)
                        }
                    } else {
                        mBinding.noItem.visibility = View.VISIBLE
                        itemListAdapter!!.setItemListCategory(list)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                this.dispose()
                mBinding.noItem.visibility = View.VISIBLE
                mBinding.progressBrand.visibility = View.GONE
                mBinding.swipeContainer.isRefreshing = false
            }

            override fun onComplete() {
                mBinding.progressBrand.visibility = View.GONE
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(): ShopbyBrandFragment {
            return ShopbyBrandFragment()
        }
    }
}