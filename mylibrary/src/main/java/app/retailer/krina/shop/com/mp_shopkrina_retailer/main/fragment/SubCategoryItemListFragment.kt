package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.SubCategoryItemListFragmentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.util.*

class SubCategoryItemListFragment : Fragment() {
    private lateinit var mBinding: SubCategoryItemListFragmentBinding
    var homeActivity = activity as? HomeActivity

    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var itemListAdapter: ItemListAdapter? = null
    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()
    private var lang = ""
    private var custId = 0
    private var subCatId = 0
    private var subSubCattId = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SubCategoryItemListFragmentBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            subCatId = bundle.getInt("SUB_CAT_ID")
            subSubCattId = bundle.getInt("SUB_SUB_CAT_ID")
        }
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialization()
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                mBinding.progressItem.visibility = View.VISIBLE
                commonClassForAPI!!.fetchItemBrandByList(
                    itemMasterDes,
                    custId,
                    subSubCattId,
                    subCatId,
                    lang,
                    ""
                )
            }
        } else {
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName,
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemMasterDes.dispose()
    }

    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }

    fun initialization() {
        lang = LocaleHelper.getLanguage(activity)
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mBinding.rvCategoryItem.layoutManager = mLinearLayoutManager
        mBinding.rvCategoryItem.isNestedScrollingEnabled = false
        itemListAdapter =
            ItemListAdapter(
                homeActivity!!,
                list
            )
        mBinding.rvCategoryItem.adapter = itemListAdapter
        mBinding.txtNoDataFound.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Data_not_found)
    }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    // fetch item master data
    private val itemMasterDes: DisposableObserver<ItemListResponse> =
        object : DisposableObserver<ItemListResponse>() {
            override fun onNext(o: ItemListResponse) {
                mBinding.progressItem.visibility = View.GONE
                val mItemIdList = ArrayList<Int>()
                try {
                    if (o.isStatus) {
                        list.clear()
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
                            mItemIdList.add(itemList[i].itemId)
                        }
                        if (list.size != 0) {
                            Collections.sort(list, ComparatorOfNumericString())
                        }
                        if (inactiveItemList.size != 0) {
                            list.addAll(inactiveItemList)
                        }
                        if (list.size != 0) {
                            itemListAdapter =
                                ItemListAdapter(
                                    homeActivity!!,
                                    list
                                )
                            mBinding.rvCategoryItem.adapter = itemListAdapter
                        }
                    } else {
                        itemListAdapter!!.setItemListCategory(list)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                this.dispose()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                list.clear()
                itemListAdapter!!.setItemListCategory(list)
                this.dispose()
            }

            override fun onComplete() {
                mBinding.progressItem.visibility = View.GONE
            }
        }

    companion object {
        fun newInstance(): SubCategoryItemListFragment {
            return SubCategoryItemListFragment()
        }
    }
}