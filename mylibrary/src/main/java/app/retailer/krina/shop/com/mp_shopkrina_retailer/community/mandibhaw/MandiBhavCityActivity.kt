package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.mandibhaw

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMandiBhavCityBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog

class MandiBhavCityActivity : AppCompatActivity(), MandiBhavLisner {

    lateinit var binding: ActivityMandiBhavCityBinding
    private lateinit var viewModel:    MandiBhavViewModel
    private lateinit var districtList: ArrayList<String>
    private lateinit var stateAdapter: MandiCityAdapter
    private lateinit var stateName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMandiBhavCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        apiCall()
    }

    private fun apiCall() {
        viewModel.getState()
        viewModel.getState.observe(this) {
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
                    if (it.data.size > 0) {
                        stateAdapter = MandiCityAdapter(this, it.data, this)
                        binding.rvMandiCity.adapter = stateAdapter
                    }
                }
            }
        }
    }

    private fun initView() {
        val repository = MandiBhavRepository(RestClient.getInstance4().service4)
        viewModel = ViewModelProvider(this, MandiBhavFactory(repository))[MandiBhavViewModel::class.java]
        binding.toolbarPost.tvBtnPost.text = "अन्य क्षेत्र चुने"
        binding.toolbarPost.back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun cityClick(cityName: String) {
        stateName = cityName
        viewModel.getDistrict(cityName)
        viewModel.getDistrictResponse.observe(this) {
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
                    if (it.data.size > 0) {
                        districtList =it.data
                        stateAdapter.submitDistrictList(districtList)
                        stateAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "City Not Available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun districtName(district: String) {
        viewModel.addUser(
            MandiBhavModel(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID).toString(),
                stateName,
                district
            )
        )
        viewModel.addUserResponse.observe(this) {
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

                    if (it.data.status) {
                        startActivity(
                            Intent(
                                this,
                                MandiBhavActivity::class.java
                            ).putExtra("StateName", it.data.res.StateName)
                                .putExtra("DistrictName", it.data.res.DistrictName)
                        )
                        finish()
                    }
                }
            }
        }
    }
}