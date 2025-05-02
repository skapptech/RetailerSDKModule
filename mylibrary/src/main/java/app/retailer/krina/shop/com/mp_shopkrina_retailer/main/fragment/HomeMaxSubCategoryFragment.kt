package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentMaxHomeSubCategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SubCategoryMaxItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication

class HomeMaxSubCategoryFragment : Fragment() {
    private lateinit var mBinding: FragmentMaxHomeSubCategoryBinding

    var homeActivity = activity as? HomeActivity

    private var subCateDataModel: HomeDataModel? = null
    private var sSectionType: String? = null
    private var sectionName: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_max_home_sub_category,
            container,
            false
        )
        val bundle = this.arguments
        if (bundle != null) {
            subCateDataModel = bundle.getSerializable("SUB_CAT_MODEL") as HomeDataModel?
            sSectionType = bundle.getString("SectionType")
            sectionName = bundle.getString("sectionName")
        }
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    fun initialization() {
        mBinding.title.text = sectionName
        homeActivity?.bottomNavigationView?.visibility = View.VISIBLE
        mBinding.rvSubCat.layoutManager = GridLayoutManager(activity, 2)
        val adapter = SubCategoryMaxItemAdapter(
            homeActivity!!,
            subCateDataModel!!.appItemsList, sSectionType!!
        )
        mBinding.rvSubCat.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeMaxSubCategoryFragment {
            return HomeMaxSubCategoryFragment()
        }
    }
}