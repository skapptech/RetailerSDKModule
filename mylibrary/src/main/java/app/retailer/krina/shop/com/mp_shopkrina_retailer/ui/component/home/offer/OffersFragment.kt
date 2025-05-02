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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class OffersFragment : Fragment() {
    private lateinit var appCtx: MyApplication
    private lateinit var viewModel: OfferViewModel
    private var rootView: View? = null
    private lateinit var mBinding: FragmentOffersTextBinding
    private var activity: HomeActivity? = null
    private var list: ArrayList<BillDiscountModel>? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as MyApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_offers_text, container, false)
            val appRepository = AppRepository(activity!!.applicationContext)
            viewModel =
                ViewModelProvider(
                    activity!!,
                    OfferViewModelFactory(appCtx, appRepository)
                )[OfferViewModel::class.java]
            rootView = mBinding.root
            initialization()
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        activity!!.bottomNavigationView!!.menu.findItem(R.id.offers).isChecked = true
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) activity!!.mBinding!!.toolbarH.liStoreH.visibility =
            View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        if (SharePrefs.getInstance(activity)
                .getBoolean(SharePrefs.IS_SELLER_AVAIL)
        ) activity!!.mBinding!!.toolbarH.liStoreH.visibility =
            View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Top item visible and gone
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        activity!!.topToolbarTitle!!.visibility = View.GONE
        activity!!.topToolbarTitle!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.title_offers)
    }
    fun initialization() {
        observe(viewModel.getBillDiscountListData, ::handleOfferResult)
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        mBinding.noOfferAvailable.text = MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available)
        list = ArrayList()
        mBinding.recyclerOffer.adapter =
            OfferAdapter(
                activity!!,
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
                            activity!!,
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