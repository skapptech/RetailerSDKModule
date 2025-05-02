package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddressSearchBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.AddressSearchAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewUtils.Companion.snackbar
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient

class AddressSearchActivity : AppCompatActivity(), AddressSearchAdapter.ClickListener {
    private lateinit var mBinding: ActivityAddressSearchBinding
    private lateinit var viewModel: CustomerRegistrationViewModel
    private var cityName: String? = ""
    private var searchCity = false
    private var placesClient: PlacesClient? = null
    private var lat = 0.0
    private var lng = 0.0
    private var type = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_search)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            CustomerRegistrationViewModelFactory(application, appRepository)
        )[CustomerRegistrationViewModel::class.java]
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            cityName = intent.getStringExtra(Constant.CITY_NAME)
            searchCity = intent.getBooleanExtra(Constant.IS_SEARCH_CITY, false)
            lat = intent.getDoubleExtra(Constant.LATITUDE, 0.0)
            lng = intent.getDoubleExtra(Constant.LONGITUDE, 0.0)
            type = intent.getIntExtra(Constant.TYPE, 0)
        }
        if (searchCity) {
            mBinding.address.hint =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_city_hint)
        } else if (type == 1) {
            title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_area)
            mBinding.address.hint =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_area)
        } else {
            title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_serach_address)
            mBinding.address.hint =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_delivery_address)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun click(place: String, address: CharSequence?, area: CharSequence?) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
//            Place.Field.VIEWPORT
        )
        val request = FetchPlaceRequest.builder(place, placeFields).build()
        placesClient?.fetchPlace(request)
            ?.addOnSuccessListener { response: FetchPlaceResponse ->
                println(response)
                println(response.place)
                println(response.place.name)
                println(response.place.address)
                println(response.place.viewport)

                val intent = Intent()
                intent.putExtra(Constant.PLACE_RESULT, response.place)
                intent.putExtra(Constant.ADDRESS, address.toString())
                intent.putExtra(Constant.AREA, area.toString())
                setResult(RESULT_OK, intent)
                Utils.rightTransaction(this)
                finish()
            }?.addOnFailureListener { exception: java.lang.Exception ->
                if (exception is ApiException) {
                    Toast.makeText(applicationContext, exception.message + "", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    private fun init() {
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        placesClient = Places.createClient(applicationContext)

        mBinding.etSearchPlace.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPlace()
                return@setOnEditorActionListener true
            }
            false
        }
        mBinding.imSearchPlace.setOnClickListener { searchPlace() }
    }

    private fun searchPlace() {
        val s = mBinding.etSearchPlace.text.toString()

        if (TextUtils.isNullOrEmpty(s)) {
            if (searchCity) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_enter_city_name)
                )
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.please_enter_address_p)
                )
            }
        } else {
            mBinding.imSearchPlace.visibility = View.INVISIBLE
            mBinding.progressSearch.visibility = View.VISIBLE

            var url = ""
            if (type == 1)
                url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s +
                        "&key=" + getString(R.string.google_maps_key) +
                        "&location=" + lat + ',' + lng +
                        "&radius=" + EndPointPref.getInstance(applicationContext).areaRadius +
                        "&strictbounds=true&components=country:in" +
                        "&origin=" + lat + ',' + lng
            else
                url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s +
                        "&key=" + getString(R.string.google_maps_key) +
                        "&location=" + lat + ',' + lng +
                        "&radius=" + EndPointPref.getInstance(applicationContext).addressRadius +
                        "&strictbounds=true&components=country:in" +
                        "&origin=" + lat + ',' + lng

            viewModel.searchAddress(url)
            viewModel.searchAddressData.observe(this) {
                when (it) {
                    is Response.Loading -> {
                        Utils.showProgressDialog(this)
                    }
                    is Response.Success -> {
                        it.data?.let {
                            Utils.hideProgressDialog()
                            mBinding.progressSearch.visibility = View.INVISIBLE
                            mBinding.progressSearch.visibility = View.INVISIBLE
                            try {
                                val array = it.getAsJsonArray("predictions")
                                val adapter = AddressSearchAdapter(applicationContext, array)
                                mBinding.placesRecyclerView.adapter = adapter
                                adapter.setClickListener(this@AddressSearchActivity)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Response.Error -> {
                        Utils.hideProgressDialog()
                        mBinding.progressSearch.visibility = View.INVISIBLE
                        mBinding.imSearchPlace.visibility = View.VISIBLE
                        mBinding.root.snackbar(RetailerSDKApp.getInstance().noteRepository.getString(R.string.no_result_found))
                    }
                }
            }
        }
    }
}