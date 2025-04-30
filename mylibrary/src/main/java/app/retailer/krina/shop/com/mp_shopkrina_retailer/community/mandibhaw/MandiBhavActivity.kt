package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMandiBhavBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog

class MandiBhavActivity:AppCompatActivity() {

    lateinit var binding: ActivityMandiBhavBinding
    private lateinit var viewModel: MandiBhavViewModel
    private lateinit var stateName:String
    private lateinit var DistrictName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMandiBhavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stateName= intent.getStringExtra("StateName")as String
        DistrictName= intent.getStringExtra("DistrictName")as String
        initView()
    }

    private fun initView() {
        val repository = MandiBhavRepository(RestClient.getInstance4().service4)
        viewModel = ViewModelProvider(this, MandiBhavFactory(repository))[MandiBhavViewModel::class.java]
        binding.tvCurrentCityName.text= "$stateName | $DistrictName"
        binding.tvOtherArea.setOnClickListener {
            startActivity(Intent(this@MandiBhavActivity,MandiBhavCityActivity::class.java))
        }
        binding.toolbarPost.back.setOnClickListener {
           startActivity(Intent(this@MandiBhavActivity,FeedActivity::class.java))
            finish()
        }

        viewModel.getMandiBhav(stateName,DistrictName)
        viewModel.getMandiData.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()

                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    val adapter=MandiBhavAdapter(this,it.data)
                    binding.rvMandiData.adapter=adapter


                }
            }
        }


    }
}