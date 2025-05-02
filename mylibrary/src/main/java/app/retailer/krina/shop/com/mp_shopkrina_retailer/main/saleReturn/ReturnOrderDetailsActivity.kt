package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.saleReturn
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnOrderDetailsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils

class ReturnOrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReturnOrderDetailsBinding
    private lateinit var returnOrderViewModel: ReturnOrderViewModel
    private var returnOrderDetailsAdapter: ReturnOrderDetailsAdapter? = null
    private var requestId = 0
    private var orderId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReturnOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        val appRepository = AppRepository(applicationContext)
        returnOrderViewModel = ViewModelProvider(
            this,
            ReturnOrderViewModelFactory(application, appRepository)
        )[ReturnOrderViewModel::class.java]
        if (intent.extras != null) {
            requestId = intent.extras!!.getInt("REQUEST_ID")
            orderId = intent.extras!!.getInt("ORDER_ID")
            binding.tvOrderId.text = orderId.toString()
        }
        init()
        observe(
            returnOrderViewModel.salesReturnRequestDetailsData,
            ::mySalesReturnRequestDetailsListResult
        )
        returnOrderViewModel.getSalesReturnDetailsList(requestId)
    }


    private fun init() {
        // observe(returnOrderViewModel.myDreamData, ::myDreamResult)
        binding.imBack.setOnClickListener {
            onBackPressed()
        }
        returnOrderDetailsAdapter = ReturnOrderDetailsAdapter(ArrayList())
        val layoutManager = LinearLayoutManager(this)
        binding.rvReturnOrderItemDetails.layoutManager = layoutManager
        binding.rvReturnOrderItemDetails.setHasFixedSize(true)
        binding.rvReturnOrderItemDetails.adapter = returnOrderDetailsAdapter

        binding.tvItems.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.Items)
        binding.tvQty.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.qty)
        binding.tvRate.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.rate)
        binding.tvTotalValue.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_value)
        binding.tvOrderIdH.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id)
    }


    private fun mySalesReturnRequestDetailsListResult(it: Response<ArrayList<SalesReturnRequestListDetailsModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it.size > 0) {
                        returnOrderDetailsAdapter!!.submitList(it)
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                returnOrderDetailsAdapter!!.submitList(ArrayList())
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
    }
}