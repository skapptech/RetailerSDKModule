package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHomeSubCategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SubCategoryItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class HomeSubCategoryFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeSubCategoryBinding
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: SubCategoryItemAdapter? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var lang = ""
    private var itemImage: String? = ""
    private var baseCatId = 0
    private var categoryId = 0
    private var subCatId = 0
    private var subSubCatId = 0
    private var warehouseId = 0
    private var mSectionType: String? = ""
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_sub_category, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            baseCatId = bundle.getInt("BaseCategoryId")
            categoryId = bundle.getInt("CATEGORY_ID")
            subCatId = bundle.getInt("SUB_CAT_ID")
            subSubCatId = bundle.getInt("SUB_SUB_CAT_ID")
            itemImage = bundle.getString("ITEM_IMAGE")
            mSectionType = bundle.getString("SectionType")
        }
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val analyticPost = AnalyticPost()
        analyticPost.baseCatId = "" + baseCatId
        analyticPost.categoryId = categoryId
        analyticPost.subCatId = subCatId
        MyApplication.getInstance().updateAnalytics("storeDefault", analyticPost)
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE

        // init view
        initialization()
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName, null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        filterCategoryItemdes.dispose()
        offerObserver.dispose()
    }


    private fun initialization() {
        lang = LocaleHelper.getLanguage(activity)
        utils = Utils(activity)
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        mBinding.recyclerCategories.layoutManager = layoutManager
        adapter = SubCategoryItemAdapter(homeActivity!!, mSectionType!!)
        mBinding.recyclerCategories.adapter = adapter
        if (!TextUtils.isNullOrEmpty(itemImage)) {
            Glide.with(homeActivity!!).load(itemImage)
                .placeholder(R.drawable.logo_grey_wide)
                .error(R.drawable.logo_grey_wide)
                .into(mBinding.topImage)
        } else {
            mBinding.topImage.setImageResource(R.drawable.logo_grey_wide)
        }
        mBinding.tvMarquee.isSelected = true

        mBinding.recyclerCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mBinding.swipeRefresh.isEnabled =
                    layoutManager!!.findFirstCompletelyVisibleItemPosition() == 0
            }
        })

        // store dashboard API
        subCategoryAPICall()
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.getSubCatOfferList(
                    offerObserver,
                    SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID), subCatId
                )
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun subCategoryAPICall() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                mBinding.progressSubCat.visibility = View.VISIBLE
                commonClassForAPI!!.fetchSubCategory(
                    filterCategoryItemdes, subCatId, homeActivity!!.custId,
                    warehouseId, lang, mSectionType
                )
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    // Getting all available bill discount list
    private val offerObserver: DisposableObserver<BillDiscountListResponse> =
        object : DisposableObserver<BillDiscountListResponse>() {
            override fun onNext(response: BillDiscountListResponse) {
                var offerMag = ""
                if (response.isStatus) {
                    val list = response.billDiscountList
                    if (list!!.size != 0) {
                        for (i in list.indices) {
                            offerMag = if (list[i].billDiscountOfferOn.equals(
                                    "Percentage",
                                    ignoreCase = true
                                )
                            ) {
                                val offerText =
                                    DecimalFormat("##.##").format(list[i].discountPercentage) + MyApplication.getInstance().dbHelper.getString(
                                        R.string.per_of_min_per
                                    ) + list[i].billAmount
                                //offerList.add(offerMag);
                                "$offerMag* $offerText  "
                            } else if (list[i].billDiscountOfferOn.equals(
                                    "FreeItem",
                                    ignoreCase = true
                                )
                            ) {
                                val offerText =
                                    MyApplication.getInstance().dbHelper.getString(R.string.bill_free_item) + list[i].billAmount
                                // offerList.add(offerMag);
                                "$offerMag* $offerText  "
                            } else {
                                val msgPostBill = if (list[i].applyOn.equals(
                                        "PostOffer",
                                        ignoreCase = true
                                    )
                                ) MyApplication.getInstance().dbHelper.getString(R.string.post_bill_text) else ""
                                if (list[i].walletType.equals(
                                        "WalletPercentage",
                                        ignoreCase = true
                                    )
                                ) {
                                    val offerText =
                                        DecimalFormat("##.##").format(list[i].billDiscountWallet) + MyApplication.getInstance().dbHelper.getString(
                                            R.string.per_of_min_per
                                        ) + DecimalFormat("##.##").format(
                                            list[i].billAmount
                                        ) + msgPostBill
                                    // offerList.add(offerMag);
                                    "$offerMag* $offerText  "
                                } else {
                                    val offerText =
                                        (MyApplication.getInstance().dbHelper.getString(R.string.flat_rs) +
                                                DecimalFormat("##.##").format(convertToAmount(list[i].billDiscountWallet))
                                                + MyApplication.getInstance().dbHelper.getString(R.string.per_of_min_per_wallet) + DecimalFormat(
                                            "##.##"
                                        ).format(
                                            list[i].billAmount
                                        ) + msgPostBill)
                                    //  offerList.add(offerMag);
                                    "$offerMag* $offerText  "
                                }
                            }
                        }
                        offerMag = try {
                            offerMag.substring(0, offerMag.length - 1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ""
                        }
                        if (!TextUtils.isNullOrEmpty(offerMag)) {
                            mBinding.tvMarquee.text = offerMag
                        } else {
                            mBinding.llOfferList.visibility = View.GONE
                        }
                    } else {
                        mBinding.llOfferList.visibility = View.GONE
                    }
                } else {
                    mBinding.llOfferList.visibility = View.GONE
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                this.dispose()
                Utils.hideProgressDialog()
                mBinding.llOfferList.visibility = View.GONE
            }

            override fun onComplete() {}
        }

    // filter cate items
    private val filterCategoryItemdes: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(jsonObject: JsonObject) {
                try {
                    mBinding.appbar.visibility = View.VISIBLE
                    mBinding.progressSubCat.visibility = View.GONE
                    Utils.hideProgressDialog()
                    val baseCatModel = Gson().fromJson(jsonObject, BaseCategoriesModel::class.java)
                    if (baseCatModel.subsubCategoryDc.size != 0) {
                        mBinding.llEmpty.visibility = View.GONE
                        //set category
                        mBinding.recyclerCategories.layoutManager = GridLayoutManager(activity, 2)
                        adapter!!.setData(baseCatModel.subsubCategoryDc, baseCatModel)
                        if (!TextUtils.isNullOrEmpty(
                                baseCatModel.subCategoryDC[0].storeBanner
                            )
                        ) Glide.with(
                            activity!!
                        ).load(
                            EndPointPref.getInstance(activity).getString(EndPointPref.API_ENDPOINT)
                                    + baseCatModel.subCategoryDC[0].storeBanner
                        )
                            .placeholder(R.drawable.logo_grey_wide)
                            .error(R.drawable.logo_grey_wide)
                            .into(mBinding.topImage)
                    } else {
                        mBinding.llEmpty.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                mBinding.progressSubCat.visibility = View.GONE
                this.dispose()
            }

            override fun onComplete() {}
        }

    companion object {
        @JvmStatic
        fun newInstance(): HomeSubCategoryFragment {
            return HomeSubCategoryFragment()
        }
    }
}