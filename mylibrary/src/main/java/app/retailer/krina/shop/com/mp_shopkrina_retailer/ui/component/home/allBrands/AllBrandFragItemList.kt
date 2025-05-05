package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentAllbrandsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHome1Binding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AllBrandsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class AllBrandFragItemList : Fragment() {
    private lateinit var mBinding: FragmentAllbrandsBinding
    private lateinit var viewModel: AllBrandViewModel
    private lateinit var appCtx: RetailerSDKApp
    private var mAllBrandListView: GridView? = null
    var homeActivity = activity as? HomeActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
        appCtx = homeActivity!!.application as RetailerSDKApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAllbrandsBinding.inflate(inflater, container, false)
        val appRepository = AppRepository(homeActivity!!.applicationContext)
        viewModel =
            ViewModelProvider(
                homeActivity!!,
                AllBrandViewModelFactory(RetailerSDKApp.application, appRepository)
            )[AllBrandViewModel::class.java]
        // init view
        initView()

        // All brand API calling
        viewModel.getAllBrands(
            homeActivity!!.custId,
            homeActivity!!.wId,
            LocaleHelper.getLanguage(activity)
        )
        observe(viewModel.getAllBrandsData, ::handleAllBrandsResult)
        return mBinding.root
    }

    private fun handleAllBrandsResult(it: Response<ArrayList<AllBrandsModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    val list = it.sortedBy { it.subsubcategoryName }
                    mAllBrandListView!!.adapter =
                        AllBrandsAdapter(
                            homeActivity!!,
                            R.layout.all_brands_item,
                            list
                        )

                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(
                    activity,
                    it.errorMesssage.toString()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName,
            null
        )
        homeActivity!!.mBinding!!.toolbarH.ivBrands.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        homeActivity!!.mBinding!!.toolbarH.ivBrands.isEnabled = true
    }


    private fun initView() {
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        mAllBrandListView = mBinding.allBrandList
        mAllBrandListView!!.isFastScrollEnabled = true
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
    }


    companion object {
        @JvmStatic
        fun newInstance(): AllBrandFragItemList {
            return AllBrandFragItemList()
        }
    }
}