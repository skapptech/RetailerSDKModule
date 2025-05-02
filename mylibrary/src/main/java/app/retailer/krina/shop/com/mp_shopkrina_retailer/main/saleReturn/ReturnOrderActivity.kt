package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnOrderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class ReturnOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReturnOrderBinding
    private lateinit var returnOrderViewModel: ReturnOrderViewModel
    private var returnOrderAdapter: ReturnOrderAdapter? = null
    private var customerId = 0
    private var wareHouseId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_order)
        val appRepository = AppRepository(applicationContext)
        returnOrderViewModel = ViewModelProvider(
            this,
            ReturnOrderViewModelFactory(application, appRepository)
        )[ReturnOrderViewModel::class.java]
        init()
        observe(returnOrderViewModel.salesReturnRequestData, ::mySalesReturnRequestListResult)
        returnOrderViewModel.getSalesReturnList(customerId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        Utils.rightTransaction(this)
    }

    private fun init() {
        customerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        wareHouseId = SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        binding.imBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvAddRequest.setOnClickListener {
            startActivity(Intent(this, AddReturnOrderRequestActivity::class.java))
            Utils.leftTransaction(this)
        }
        returnOrderAdapter = ReturnOrderAdapter(this,ArrayList())
        val layoutManager = LinearLayoutManager(this)
        binding.rvReturnOrder.layoutManager = layoutManager
        binding.rvReturnOrder.setHasFixedSize(true)
        binding.rvReturnOrder.adapter = returnOrderAdapter

        binding.tvReturnOrder.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.return_order)
        binding.tvAddRequest.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_add_request)
        binding.tvOrderId.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.request_id)
        binding.tvOrderValue.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_value)
        binding.tvOrderDate.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_date)
        binding.tvStatus.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.status)
        binding.tvNoDataFound.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_request_found)
    }


    private fun mySalesReturnRequestListResult(it: Response<ArrayList<SalesReturnRequestListModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it.size > 0) {
                        returnOrderAdapter!!.submitList(it)
                    }
                    binding.rlRecycleLayout.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                returnOrderAdapter!!.submitList(ArrayList())
                binding.rlRecycleLayout.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
            }
        }
    }
}