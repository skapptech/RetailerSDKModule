package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentAllOrderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyOrderAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PaylaterLimitsAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.PaylaterLimitsResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog

class AllOrderFragment : Fragment(), OnRefreshListener {
    private lateinit var viewModel: OrderViewModel

    private lateinit var mBinding: FragmentAllOrderBinding
    private var activity: MyOrderActivity? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var myOrderAdapter: MyOrderAdapter? = null

    private var paylaterLimitsAdapter: PaylaterLimitsAdapter? = null
    private var custId = 0
    private val list = ArrayList<ConformOrderModel>()
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pageCount = 1
    private var loading = true
    private var type = ""
    private var rvPaylaterLimits: RecyclerView? = null
    private var progressbarPayLaterLimits: ProgressBar? = null
    private var notAvailableLimit: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) type = arguments!!.getString("type")!!

        val appRepository = AppRepository(context!!)
        viewModel = ViewModelProvider(
            this, OrderViewModelFactory(activity!!.application, appRepository)
        )[OrderViewModel::class.java]
        observe(viewModel.orderData, ::handleMyOrderResult)
        observe(viewModel.payLaterLimitData, ::handlePayLaterLimitResult)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MyOrderActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_all_order, container, false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    override fun onRefresh() {
        list.clear()
        myOrderAdapter!!.notifyDataSetChanged()
        pageCount = 1
        callApi()
    }

    override fun onResume() {
        super.onResume()
        if (myOrderAdapter != null) {
            myOrderAdapter!!.notifyDataSetChanged()
        }
    }

    fun onButtonClick() {
        mBinding.swiperefresh.isRefreshing = true
        onRefresh()
    }


    private fun initialization() {
        mBinding.swiperefresh.setOnRefreshListener(this)
        val layoutManager = LinearLayoutManager(activity)
        mBinding.rMyOrder.layoutManager = layoutManager
        Log.e("TAG", "initialization: " + type)

        if (type == "PayLater") {
            mBinding.cvPaylatter.visibility = View.VISIBLE
        } else {
            mBinding.cvPaylatter.visibility = View.GONE
        }

        mBinding.cvPaylatter.setOnClickListener {
            if (type == "PayLater") {
                openPayLaterLimitsDialog()
            }
        }
        mBinding.btnStartShopping.setOnClickListener {
            startActivity(
                Intent(
                    activity, HomeActivity::class.java
                )
            )
        }

        myOrderAdapter = MyOrderAdapter(activity!!, list)
        mBinding.rMyOrder.adapter = myOrderAdapter
        mBinding.shimmerViewContainer.startShimmer()
        mBinding.LLMain.visibility = View.INVISIBLE
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        utils = Utils(activity)
        mBinding.rMyOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mBinding.swiperefresh.isEnabled =
                    layoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            pageCount++
                            callApi()
                        }
                    }
                }
            }
        })

        callApi()
    }

    private fun openPayLaterLimitsDialog() {
        val dialog = BottomSheetDialog(activity!!)
        dialog.setContentView(R.layout.dialog_show_paylater_limits)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(false)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        progressbarPayLaterLimits = dialog.findViewById(R.id.progressbarPayLaterLimits)
        notAvailableLimit = dialog.findViewById(R.id.notAvailableLimit)
        progressbarPayLaterLimits?.visibility = View.VISIBLE
        rvPaylaterLimits = dialog.findViewById(R.id.rvPaylaterLimits)
        rvPaylaterLimits?.layoutManager = LinearLayoutManager(activity)

        viewModel.getPayLaterLimits(custId)

        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun callApi() {
        if (utils!!.isNetworkAvailable) {
            mBinding.shimmerViewContainer.visibility = View.VISIBLE
            mBinding.progressBar.visibility = View.VISIBLE
            viewModel.getOrdersWithPages(custId, pageCount, 10, type)
        } else {
            Utils.setToast(
                activity,
                getString(R.string.internet_connection)
            )
        }
    }

    private fun handleMyOrderResult(result: Response<ArrayList<ConformOrderModel>>?) {
        mBinding.swiperefresh.isRefreshing = false
        try {
            mBinding.shimmerViewContainer.stopShimmer()
            mBinding.shimmerViewContainer.visibility = View.GONE
            mBinding.LLMain.visibility = View.VISIBLE
            mBinding.progressBar.visibility = View.GONE
            mBinding.liEmpty.visibility = View.GONE
            mBinding.rMyOrder.visibility = View.VISIBLE
            val orderList = result?.data
            if (orderList != null) {
                if (orderList.isNotEmpty()) {
                    list.addAll(orderList)
                    myOrderAdapter!!.notifyDataSetChanged()
                    loading = true
                } else {
                    loading = false
                }
            }
            if (list.size == 0 && list.isEmpty()) {
                mBinding.liEmpty.visibility = View.VISIBLE
                mBinding.rMyOrder.visibility = View.GONE
                mBinding.cvPaylatter.visibility = View.GONE
                mBinding.shimmerViewContainer.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mBinding.shimmerViewContainer.stopShimmer()
            mBinding.shimmerViewContainer.visibility = View.GONE
            mBinding.LLMain.visibility = View.VISIBLE
            if (list.size <= 0) {
                mBinding.liEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun handlePayLaterLimitResult(result: Response<PaylaterLimitsResponseModel>) {
        try {
            progressbarPayLaterLimits?.visibility = View.GONE
            val data = result.data
            if (data != null && data.Status) {
                notAvailableLimit?.visibility = View.GONE
                paylaterLimitsAdapter =
                    PaylaterLimitsAdapter(activity!!, data.payLaterCollectionLimitDCs)
                rvPaylaterLimits?.adapter = paylaterLimitsAdapter
            } else {
                notAvailableLimit?.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            progressbarPayLaterLimits?.visibility = View.GONE
            notAvailableLimit?.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(type: String?): Fragment {
            val fragment = AllOrderFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}