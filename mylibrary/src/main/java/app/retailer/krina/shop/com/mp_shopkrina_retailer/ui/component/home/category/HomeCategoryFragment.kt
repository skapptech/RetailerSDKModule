package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.AllCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.CategoriesResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.HomeCategoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentHomeCategoryBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.Gson
import org.json.JSONObject

open class HomeCategoryFragment : Fragment() {
    private lateinit var appCtx: RetailerSDKApp
    private var rootView: View? = null
    private lateinit var mBinding: FragmentHomeCategoryBinding
    private lateinit var homeCategoryViewModel: HomeCategoryViewModel
    private var activity: HomeActivity? = null
    private var list: ArrayList<AllCategoryModel>? = null
    private var adapter: CategoriesAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity!!.application as RetailerSDKApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home_category, container, false)
            val appRepository = AppRepository(activity!!.applicationContext)
            homeCategoryViewModel =
                ViewModelProvider(
                    activity!!,
                    HomeCategoryViewModelFactory(RetailerSDKApp.application, appRepository)
                )[HomeCategoryViewModel::class.java]
        }
        return mBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.searchText!!.visibility = View.VISIBLE
        activity!!.rightSideIcon!!.visibility = View.VISIBLE
        observe(homeCategoryViewModel.getCategoryData, ::handleCategoryResult)
        // init view
        initialization()
    }

    override fun onResume() {
        super.onResume()
        activity!!.bottomNavigationView!!.menu.findItem(R.id.category).isChecked = true
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName, null
        )
    }

    fun initialization() {
        activity!!.bottomNavigationView!!.visibility = View.VISIBLE
        list = ArrayList()
        adapter = CategoriesAdapter(activity!!, list)
        mBinding.recyclerCategories.adapter = adapter
        // local storage
        val dataSaved = SharePrefs.getInstance(activity).getString(SharePrefs.CATEGORY_BY_ALL)
        if (dataSaved.isNotEmpty()) {
            object : AsyncTask<Void?, Void?, HomeCategoryResponse?>() {
                override fun onPreExecute() {
                    super.onPreExecute()
                    mBinding.progressBar.visibility = View.VISIBLE
                }

                override fun doInBackground(vararg voids: Void?): HomeCategoryResponse? {
                    var response: HomeCategoryResponse? = null
                    try {
                        val jsonObject = JSONObject(dataSaved)
                        response = Gson().fromJson(
                            jsonObject.toString(),
                            HomeCategoryResponse::class.java
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return response
                }

                override fun onPostExecute(response: HomeCategoryResponse?) {
                    super.onPostExecute(response)
                    mBinding.progressBar.visibility = View.INVISIBLE
                    setValueInAdapter(response)
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            homeCategoryViewModel.getCategory(activity!!.custId,
                SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                LocaleHelper.getLanguage(activity))
        }
    }

    // setAdapter
    private fun setValueInAdapter(homeCategoryResponse: HomeCategoryResponse?) {
        list!!.clear()
        if (homeCategoryResponse!!.basecatlist != null && homeCategoryResponse.basecatlist!!.size != 0 && homeCategoryResponse.categoriesList != null && homeCategoryResponse.categoriesList.size != 0) {
            val basecatlist = homeCategoryResponse.basecatlist
            val categoriesList = homeCategoryResponse.categoriesList
            for (i in basecatlist.indices) {
                val categoryArrayList = ArrayList<CategoriesResponse>()
                for (j in categoriesList.indices) {
                    if (basecatlist[i].baseCategoryId == categoriesList[j].baseCategoryId) {
                        categoryArrayList.add(categoriesList[j])
                    }
                }
                val model =
                    AllCategoryModel()
                model.baseCategoryId = basecatlist[i].baseCategoryId
                model.baseCategoryName = basecatlist[i].baseCategoryName
                model.logoUrl = basecatlist[i].logoUrl
                model.categoriesList = categoryArrayList
                list!!.add(model)
            }
            adapter!!.notifyDataSetChanged()
        }
    }
    private fun handleCategoryResult(it: Response<HomeCategoryResponse>) {
        when (it) {
            is Response.Loading -> {
                mBinding.progressBar.visibility = View.VISIBLE
            }
            is Response.Success -> {
                it.data?.let {
                        mBinding.progressBar.visibility = View.GONE
                            setValueInAdapter(it)
                            SharePrefs.getInstance(activity)
                                .putString(SharePrefs.CATEGORY_BY_ALL, Gson().toJson(it))
                }
            }
            is Response.Error -> {
                mBinding.progressBar.visibility = View.GONE
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_response)
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeCategoryFragment {
            return HomeCategoryFragment()
        }
    }

}