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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AllBrandsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class AllBrandFragItemList : Fragment() {
    private lateinit var mBinding: FragmentAllbrandsBinding
    private lateinit var viewModel: AllBrandViewModel
    private lateinit var appCtx: MyApplication
    private var mAllBrandListView: GridView? = null
    private var activity: HomeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as MyApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_allbrands, container, false)
        val appRepository = AppRepository(activity!!.applicationContext)
        viewModel =
            ViewModelProvider(
                activity!!,
                AllBrandViewModelFactory(appCtx, appRepository)
            )[AllBrandViewModel::class.java]
        // init view
        initView()

        // All brand API calling
        viewModel.getAllBrands(
            activity!!.custId,
            activity!!.wId,
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
                            activity!!,
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
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        activity!!.mBinding!!.toolbarH.ivBrands.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        activity!!.mBinding!!.toolbarH.ivBrands.isEnabled = true
    }


    private fun initView() {
        activity!!.searchText!!.visibility = View.VISIBLE
        mAllBrandListView = mBinding.allBrandList
        mAllBrandListView!!.isFastScrollEnabled = true
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
    }


    companion object {
        @JvmStatic
        fun newInstance(): AllBrandFragItemList {
            return AllBrandFragItemList()
        }
    }
}