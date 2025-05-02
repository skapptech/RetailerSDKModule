package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentOffersTextBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class OffersFragment : Fragment() {
    private lateinit var appCtx: RetailerSDKApp
    private lateinit var viewModel: OfferViewModel
    private var rootView: View? = null
    private lateinit var mBinding: FragmentOffersTextBinding
    var homeActivity = activity as? HomeActivity
    private var list: ArrayList<BillDiscountModel>? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
        appCtx = homeActivity!!.application as RetailerSDKApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            mBinding =
                FragmentOffersTextBinding.inflate(inflater, container, false)
            val appRepository = AppRepository(homeActivity!!.applicationContext)
            viewModel =
                ViewModelProvider(
                    homeActivity!!,
                    OfferViewModelFactory(appCtx, appRepository)
                )[OfferViewModel::class.java]
            rootView = mBinding.root
            initialization()
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        homeActivity!!.bottomNavigationView!!.menu.findItem(R.id.offers).isChecked = true
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName,
            null
        )
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) homeActivity!!.mBinding!!.toolbarH.liStoreH.visibility =
            View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) homeActivity!!.mBinding!!.toolbarH.liStoreH.visibility =
            View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Top item visible and gone
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE
        homeActivity!!.topToolbarTitle!!.visibility = View.GONE
        homeActivity!!.topToolbarTitle!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_offers)
    }
    fun initialization() {
        observe(viewModel.getBillDiscountListData, ::handleOfferResult)
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding.noOfferAvailable.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_offer_available)
        list = ArrayList()
        mBinding.recyclerOffer.adapter =
            OfferAdapter(
                homeActivity!!,
                list!!
            )
        viewModel.getAllBillDiscountOffer(SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),"Home Bottom")

    }

    private fun handleOfferResult(it: Response<BillDiscountListResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    mBinding.rlNoOffer.visibility = View.GONE
                    list = it.billDiscountList
                    mBinding.recyclerOffer.adapter =
                        OfferAdapter(
                            homeActivity!!,
                            list!!
                        )
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.recyclerOffer.visibility = View.GONE
                mBinding.rlNoOffer.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): OffersFragment {
            return OffersFragment()
        }
    }


}