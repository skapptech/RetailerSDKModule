package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentAppHomeItemBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.AppHomeItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.Gson
import io.reactivex.observers.DisposableObserver
import org.json.JSONException
import org.json.JSONObject

class AppHomeItemFragment : Fragment() {
    private lateinit var mBinding: FragmentAppHomeItemBinding
    private val list = ArrayList<ItemListModel>()
    private var commonClassForAPI: CommonClassForAPI? = null
    private var utils: Utils? = null
    private var appHomeItemAdapter: AppHomeItemAdapter? = null
    private var sSectionId: String? = ""
    private var sSectionSubType: String? = ""
    private var sURL: String? = ""
    private var sTITLE: String? = ""
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var skipCount = 0
    private val takeCount = 10
    private var loading = true
    var homeActivity = activity as? HomeActivity

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_app_home_item, container, false)
        sSectionId = arguments?.getString("SECTION_ID")
        sSectionSubType = arguments?.getString("SECTION_SUB_TYPE")
        sURL = arguments?.getString("URL")
        sTITLE = arguments?.getString("TITLE")
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE
        if (!TextUtils.isNullOrEmpty(sTITLE)) {
            mBinding.title.text = sTITLE
        } else {
            mBinding.title.visibility = View.GONE
        }
        initialization()

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName, null
        )
        //skipCount = 0;
        if (appHomeItemAdapter != null) {
            appHomeItemAdapter!!.notifyDataSetChanged()
        }
    }

    private fun initialization() {
        mBinding.noItem.text = RetailerSDKApp.getInstance().dbHelper.getData("items_not_available")
        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        val itemrecycleView = mBinding.rvBrandItem
        val linearlayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        itemrecycleView.layoutManager = linearlayoutManager
        itemrecycleView.setHasFixedSize(true)
        appHomeItemAdapter =
            AppHomeItemAdapter(
                homeActivity!!,
                list,
                list.size
            )
        itemrecycleView.adapter = appHomeItemAdapter
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE

        if (sSectionSubType.equals("Other", ignoreCase = true)) {
            itemrecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        visibleItemCount = linearlayoutManager.childCount
                        totalItemCount = linearlayoutManager.itemCount
                        pastVisiblesItems = linearlayoutManager.findFirstVisibleItemPosition()
                        if (loading) {
                            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                loading = false
                                skipCount += takeCount
                                mBinding.progressLoad.visibility = View.VISIBLE
                                itemData()
                            }
                        }
                    }
                }
            })
        }

        Utils.showProgressDialog(activity)
        itemData()
    }

    private fun itemData() {
        if (sSectionSubType.equals("Other", ignoreCase = true)) {
            var url = sURL
            try {
                url = url!!.replace(
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
                url = "$url&skip=$skipCount&take=$takeCount"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (utils!!.isNetworkAvailable) {
                if (commonClassForAPI != null) {
                    commonClassForAPI!!.getOtherItemsHome(object :
                        DisposableObserver<AppHomeItemGoldenDealModel?>() {
                        override fun onNext(model: AppHomeItemGoldenDealModel) {
                            Utils.hideProgressDialog()
                            mBinding.progressLoad.visibility = View.GONE
                            try {
                                if (model != null) {
                                    if (model.itemListModels!!.size != 0) {
                                        setItemAdapter(model.itemListModels)
                                    } else {
                                        loading = false
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onError(e: Throwable) {
                            Utils.hideProgressDialog()
                            mBinding.progressLoad.visibility = View.GONE
                        }

                        override fun onComplete() {
                            mBinding.progressLoad.visibility = View.GONE
                        }
                    }, url)
                }
            } else {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getData("internet_connection")
                )
                mBinding.progressLoad.visibility = View.GONE
            }
        } else {
            Utils.hideProgressDialog()
            val jsonString12 =
                SectionPref.getInstance(activity).getString(SectionPref.PRODUCT_LIST + sSectionId)
            try {
                val jsonObject: JSONObject
                jsonObject = if (jsonString12 == null || jsonString12.isEmpty()) {
                    JSONObject()
                } else {
                    JSONObject(jsonString12)
                }
                val appHomeItemModel =
                    Gson().fromJson(jsonObject.toString(), AppHomeItemModel::class.java)
                if (appHomeItemModel.isStatus) {
                    list.clear()
                    setItemAdapter(appHomeItemModel.itemListModels)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun setItemAdapter(itemList: ArrayList<ItemListModel>) {
        if (itemList.size != 0) {
            for (i in itemList.indices) {
                var ispresent = false
                for (j in list.indices) {
                    if (list[j].itemNumber.equals(itemList[i].itemNumber, ignoreCase = true)) {
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
            }
            if (list.size > 0) {
                loading = true
                appHomeItemAdapter!!.setItemListCategory(list, list.size)
            } else {
                loading = false
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): AppHomeItemFragment {
            return AppHomeItemFragment()
        }
    }
}