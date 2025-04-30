package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentAlreadyBoughtItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SearchInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.TargetItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.TargetOrderListActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.TargetResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class AlreadyBoughtItemsFragment : Fragment(), SearchInterface {
    private lateinit var mBinding: FragmentAlreadyBoughtItemsBinding
    private lateinit var activity: TargetOrderListActivity
    private lateinit var appCtx: MyApplication

    var itemListAdapter: TargetItemListAdapter? = null
    private val mList = ArrayList<ItemListModel>()

    private var wareHouseId: Int = 0
    private var utils: Utils? = null
    private var lang = ""
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var take = 10
    private var skip = 0
    private var loading = true
    private var custId: Int = 0
    private var storeId: Int = 0
    private var firstTimeAPICAll = true
    private var keyValue = ""
    private var isSearch = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_already_bought_items,
                container,
                false
            )

        init()

        return mBinding.root
    }

    private fun init() {
        lang = LocaleHelper.getLanguage(activity)!!
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        wareHouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)

        //view Hide UnHide
        utils = Utils(activity)
        storeId = requireArguments().getInt("storeId", 0)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTargetItem.layoutManager = layoutManager

        itemListAdapter = TargetItemListAdapter(
            activity, ArrayList()
        )
        mBinding.rvTargetItem.adapter = itemListAdapter

        mBinding.rvTargetItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //                if (isPermit) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    Log.e("TAG", "onScrolled: loading $loading")
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            skip = skip + 10
                            firstTimeAPICAll = false
                            if (firstTimeAPICAll) {
                                mBinding.firstProgress.visibility = View.VISIBLE
                            } else {
                                mBinding.paginationProgress.visibility = View.VISIBLE
                            }
                            callApi()
                        }
                    }
                }
            }
        })

        callApi()
    }

    override fun onResume() {
        super.onResume()
        activity.setAboutDataListener(this)
        mList.clear()
        skip = 0
        take = 10
        keyValue = ""
        firstTimeAPICAll = true
        activity.etSearch.text.clear()
        itemListAdapter!!.setItemListCategory(mList)
        if (firstTimeAPICAll) {
            mBinding.firstProgress.visibility = View.VISIBLE
        } else {
            mBinding.paginationProgress.visibility = View.VISIBLE
        }
    }

    override fun searchBtn(data: String) {
        mList.clear()
        firstTimeAPICAll = true
        skip = 0
        keyValue = data
        take = 50
        skip = 0
        isSearch = true
        itemListAdapter!!.setItemListCategory(mList)
        if (firstTimeAPICAll) {
            mBinding.firstProgress.visibility = View.VISIBLE
        } else {
            mBinding.paginationProgress.visibility = View.VISIBLE
        }
        callApi()
    }

    fun callApi() {
        CommonClassForAPI.getInstance(activity).getAlreadyBoughtTargetItem(
            object : DisposableObserver<TargetResponseModel>() {
                override fun onNext(response: TargetResponseModel) {
                    mBinding.firstProgress.visibility = View.GONE
                    mBinding.paginationProgress.visibility = View.GONE
                    try {
                        loading = true
                        response.itemMasters?.let {
                            if (!isSearch) {
                                if (it.size > 0) {
                                    loading = true
                                    mList.addAll(it)
                                    itemListAdapter!!.setItemListCategory(mList)
                                } else {
                                    if (firstTimeAPICAll) {
                                        mBinding.noItems.visibility = View.VISIBLE
                                        Utils.setToast(
                                            activity,
                                            resources.getString(R.string.no_item_available)
                                        )
                                    } else {
                                        mBinding.noItems.visibility = View.GONE
                                    }
                                    loading = false
                                }
                            } else {
                                mList.clear()
                                if (it.size > 0) {
                                    mBinding.noItems.visibility = View.GONE
                                    mList.addAll(it)
                                    itemListAdapter!!.setItemListCategory(mList)
                                } else {
                                    mBinding.noItems.visibility = View.VISIBLE
                                }
                            }
                            isSearch = false
                        }
                        itemListAdapter!!.setItemListCategory(mList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    mBinding.firstProgress.visibility = View.GONE
                    mBinding.paginationProgress.visibility = View.GONE
                    loading = false
                }

                override fun onComplete() {
                    mBinding.firstProgress.visibility = View.GONE
                    mBinding.paginationProgress.visibility = View.GONE
                }
            }, activity.companyId, storeId,
            custId,
            wareHouseId,
            0,
            skip,
            take,
            LocaleHelper.getLanguage(activity),
            keyValue
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): AlreadyBoughtItemsFragment {
            return AlreadyBoughtItemsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as TargetOrderListActivity
        appCtx = activity.application as MyApplication
    }
}