package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.agent

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyAgentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.MyAgentListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyAgentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import com.google.android.material.bottomsheet.BottomSheetDialog

class MyAgentActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMyAgentBinding
    private lateinit var viewModel: MyAgentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_agent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.my_agents)

        val appRepository = AppRepository(this)
        viewModel = ViewModelProvider(
            this, MyAgentModelFactory(application, appRepository)
        )[MyAgentViewModel::class.java]
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mBinding.progressRating.visibility = View.VISIBLE
        observe(viewModel.myAgentData, ::handleMyAgentResult)
        viewModel.getCustomerSalesPersons(
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun showRatingDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(layoutInflater.inflate(R.layout.dialog_rate_agent, null))

//        val ivImage: ImageView = dialog.findViewById(R.id.iv_image)!!
//        val tvName: TextView = dialog.findViewById(R.id.iv_image)!!
        val btnSend: Button = dialog.findViewById(R.id.btnSend)!!


        btnSend.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun handleMyAgentResult(result: Response<ArrayList<MyAgentModel>>) {
        mBinding.progressRating.visibility = View.GONE
        val list = result.data
        if (list != null) {
            val adapter = MyAgentListAdapter(this@MyAgentActivity, list)
            mBinding.rvMyAgent.adapter = adapter
        }
    }
}