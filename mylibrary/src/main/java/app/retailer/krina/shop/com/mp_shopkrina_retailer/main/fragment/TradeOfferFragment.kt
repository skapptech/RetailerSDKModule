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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentTradeOffersBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TradeOfferResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.util.*

class TradeOfferFragment : Fragment() {
    private lateinit var mbinding: FragmentTradeOffersBinding
    private var activity: HomeActivity? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var mTradeFlag = false
    private var custId = 0
    private var WarehouseId = 0
    private val list = ArrayList<ItemListModel>()
    private val inactiveItemList = ArrayList<ItemListModel>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mbinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade_offers, container, false)
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        // init view
        initView()
        return mbinding.root
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        if (mTradeFlag) {
            if (itemListAdapter != null) {
                itemListAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        WarehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        utils = Utils(activity)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        mbinding.rvOffers.layoutManager = LinearLayoutManager(getActivity())
        mbinding.rvOffers.setHasFixedSize(true)
        itemListAdapter =
            ItemListAdapter(
                activity!!,
                list
            )
        mbinding.rvOffers.adapter = itemListAdapter
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mbinding.noOfferAvailable.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available)
        allOffer()
    }

    private fun allOffer() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                mbinding.progressTrade.visibility = View.VISIBLE
                commonClassForAPI!!.getTreadOffersFromApi(
                    tradeOfferDes,
                    custId,
                    WarehouseId,
                    LocaleHelper.getLanguage(activity)
                )
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // trade offer API response
    private val tradeOfferDes: DisposableObserver<TradeOfferResponse> =
        object : DisposableObserver<TradeOfferResponse>() {
            override fun onNext(tradeOfferResponse: TradeOfferResponse) {
                mbinding.progressTrade.visibility = View.GONE
                list.clear()
                inactiveItemList.clear()
                try {
                    if (tradeOfferResponse.isStatus) {
                        mTradeFlag = true
                        mbinding.rlNoOffer.visibility = View.GONE
                        mbinding.llOfferList.visibility = View.VISIBLE
                        val itemList = tradeOfferResponse.itemListModels
                        Collections.sort(itemList, ComparatorOfNumericString())
                        if (itemList.size != 0) {
                            for (i in itemList.indices) {
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
                        }
                        if (inactiveItemList.size != 0) {
                            list.addAll(inactiveItemList)
                        }
                        if (list.size != 0) {
                            list.addAll(inactiveItemList)
                            itemListAdapter!!.setItemListCategory(list)
                        }
                    } else {
                        mbinding.rlNoOffer.visibility = View.VISIBLE
                        mbinding.llOfferList.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                list.clear()
                itemListAdapter!!.setItemListCategory(list)
            }

            override fun onComplete() {}
        }

    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1: Double =
                if (lhs.marginPoint == null) 0.0 else lhs.marginPoint!!.toDouble()
            val i2: Double =
                if (rhs.marginPoint == null) 0.0 else rhs.marginPoint!!.toDouble()
            return i2.compareTo(i1)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): TradeOfferFragment {
            return TradeOfferFragment()
        }
    }
}