package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPlacesSearchBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomSearchPlaceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerRegistrationViewModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerRegistrationViewModelFactory
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class PlacesBillingSearchActivity : AppCompatActivity() {
    private var REQUEST_FROM = 1
    private val REQUEST_FOR_ADDRESS = 1001
    private var mBinding: ActivityPlacesSearchBinding? = null
    private lateinit var viewModel: CustomerRegistrationViewModel
    private var cityList: ArrayList<CityModel>? = null
    private var mGeocoder: Geocoder? = null
    private var address: String? = ""
    private var pinCode: String? = ""
    private var state = ""
    private var city = ""
    private var REDIRECT_FLAG = 0
    private var zipcode = ""
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_places_search)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            CustomerRegistrationViewModelFactory(application, appRepository)
        )[CustomerRegistrationViewModel::class.java]
        mBinding!!.etSearchKeyword.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_serach_address)
        mBinding!!.address.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_delivery_address)
        mBinding!!.city.hint = RetailerSDKApp.getInstance().dbHelper.getString(R.string.city)
        mBinding!!.state.hint = RetailerSDKApp.getInstance().dbHelper.getString(R.string.state_astrick)
        mBinding!!.pincode.hint = RetailerSDKApp.getInstance().dbHelper.getString(R.string.pin_code)
        mBinding!!.flateOrFloorNumber.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.address_field_number)
        mBinding!!.landmark.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.landmark_optional)
        mBinding!!.btnSave.hint = RetailerSDKApp.getInstance().dbHelper.getString(R.string.save)
        mBinding!!.landmark.visibility = View.GONE
        mBinding!!.flateOrFloorNumber.visibility = View.GONE
        mGeocoder = Geocoder(applicationContext, Locale.ENGLISH)
        if (intent != null) {
            REDIRECT_FLAG = intent.getIntExtra("REDIRECT_FLAG", 0)
            val intentCityName = intent.getStringExtra("cityName")
            if (REDIRECT_FLAG != 1) {
                val editProfileModel =
                    intent.getSerializableExtra("CUSTOMER_DETAILS") as EditProfileModel?
                address = editProfileModel!!.billingAddress
                //                IsVerified=editProfileModel.getCustomerVerify();
                pinCode = editProfileModel.billingZipCode
                if (!TextUtils.isNullOrEmpty(address)) {
                    mBinding!!.etAddress.setText(address)
                }
                if (!TextUtils.isNullOrEmpty(pinCode)) {
                    mBinding!!.etPincode.setText(pinCode)
                }
                if (!TextUtils.isNullOrEmpty(
                        editProfileModel.billingCity
                    )
                ) {
                    mBinding!!.etCity.setText(editProfileModel.billingCity)
                }
                if (!TextUtils.isNullOrEmpty(
                        editProfileModel.billingState
                    )
                ) {
                    mBinding!!.etState.setText(editProfileModel.billingState)
                }
                if (TextUtils.isNullOrEmpty(mBinding!!.etPincode.text.toString())) {
                    mBinding!!.etPincode.isFocusable = true
                    mBinding!!.etPincode.isFocusableInTouchMode = true
                } else {
                    mBinding!!.etPincode.isFocusable = false
                }
            }
            val isVerified =
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_VERIFY)
            if (!TextUtils.isNullOrEmpty(isVerified)) {
                if (isVerified.equals("Full Verified", ignoreCase = true)) {
                    mBinding!!.etAddress.isEnabled = false
                    mBinding!!.etCity.isEnabled = false
                    mBinding!!.etFlateOrFloorNumber.isEnabled = false
                    mBinding!!.etPincode.isEnabled = false
                    mBinding!!.etLandmark.isEnabled = false
                }
            }
        }
        init()
        //get city
        if (REDIRECT_FLAG == 1) {
            viewModel.getCity()
            viewModel.getCityData.observe(this) {
                cityList = it
            }
        }
        mBinding!!.btnSave.setOnClickListener { view: View? ->
            try {
                address = mBinding!!.etAddress.text.toString()
                city = mBinding!!.etCity.text.toString()
                state = mBinding!!.etState.text.toString()
                if (!TextUtils.isNullOrEmpty(mBinding!!.etPincode.text.toString())) {
                    zipcode = mBinding!!.etPincode.text.toString()
                }
                if (TextUtils.isNullOrEmpty(address)) {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_delivery_address)
                    )
                } else if (TextUtils.isNullOrEmpty(city) && REDIRECT_FLAG == 1) {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_city)
                    )
                } else if (TextUtils.isNullOrEmpty(zipcode)) {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.valid_pincode_number)
                    )
                } else if (zipcode.length < 6) {
                    Utils.setToast(
                        applicationContext,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.valid_pincode_number)
                    )
                } else {
                    val model = AddressModel(
                        0,
                        "", "",
                        "", "", 0.0, 0.0, "", "", address, city, state, zipcode
                    )
                    val intent = Intent()
                    intent.putExtra(Constant.CUSTOMER_ADDRESS, model)
                    setResult(RESULT_OK, intent)
                    Utils.rightTransaction(this@PlacesBillingSearchActivity)
                    finish()
                }
            } catch (e: Exception) {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.not_getting_proper_address)
                )
                e.printStackTrace()
            }
        }
        mBinding!!.back.setOnClickListener { view: View? -> onBackPressed() }

        requestResultHandle();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
        finish()
    }

    fun findPlace() {
        if (!Places.isInitialized()) {
            Places.initialize(
                applicationContext,
                resources.getString(R.string.google_maps_key),
                Locale.ENGLISH
            )
        }
      /*  startActivityForResult(
            Intent(applicationContext, CustomSearchPlaceActivity::class.java)
                .putExtra("cityname", "").putExtra("searchCity", false), REQUEST_FOR_ADDRESS
        )*/
        REQUEST_FROM = REQUEST_FOR_ADDRESS
        val intent = Intent(applicationContext, CustomSearchPlaceActivity::class.java)
        intent.putExtra(Constant.IS_SEARCH_CITY, false)
        intent.putExtra(Constant.CITY_NAME, "")
        resultLauncher?.launch(intent)
        Utils.leftTransaction(this)
    }

    private fun init() {
        mBinding!!.etAddress.setOnClickListener { view: View? -> findPlace() }
    }

    private fun getPlaceInfo(lat: Double, lon: Double) {
        try {
            val addresses = mGeocoder!!.getFromLocation(lat, lon, 1)
            if (addresses!![0].locality != null) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val pin = addresses[0].postalCode
                mBinding!!.etCity.setText(city)
                mBinding!!.etState.setText(state)
                mBinding!!.etPincode.setText(pin)
                if (TextUtils.isNullOrEmpty(
                        mBinding!!.etPincode.text.toString()
                    )
                ) {
                    mBinding!!.etPincode.isFocusable = true
                    mBinding!!.etPincode.isFocusableInTouchMode = true
                } else {
                    mBinding!!.etPincode.isFocusable = false
                }
                //setUpCity(city, state);
            } else {
                Utils.showProgressDialog(this@PlacesBillingSearchActivity)
                val url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
                    lat,
                    lon
                )
                FetchCityUsingAPI().execute(url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.showProgressDialog(this@PlacesBillingSearchActivity)
            val url = String.format(
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
                lat,
                lon
            )
            FetchCityUsingAPI().execute(url)
        }
    }

    private fun setUpCity(city: String, state: String) {
        var isCity = false
        if (cityList!!.size != 0) {
            for (model in cityList!!) {
                if (city.equals(model.cityName, ignoreCase = true)) {
                    val selectCityId = model.cityid
                    mBinding!!.city.editText!!.setText(city)
                    mBinding!!.state.editText!!.setText(state)
                    isCity = true
                    break
                }
            }
        }
        if (!isCity) {
            //Utils.setToast(this,"We not serve in your Area!");
            // mBinding.etAddress.setText("");
            //  mBinding.etCity.setText("");
            //  mBinding.etState.setText("");
            mBinding!!.city.editText!!.setText(city)
            mBinding!!.state.editText!!.setText(state)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class FetchCityUsingAPI : AsyncTask<String, Void, JSONObject>() {
        protected override fun doInBackground(vararg strings: String): JSONObject {
            var resultJson: JSONObject? = null
            try {
                val url = URL(strings[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                val stream: InputStream = BufferedInputStream(urlConnection.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(stream))
                val builder = StringBuilder()
                var inputString: String?
                while (bufferedReader.readLine().also { inputString = it } != null) {
                    builder.append(inputString)
                }
                val topLevel = JSONObject(builder.toString())
                resultJson = topLevel.getJSONObject("address")
                // weather = String.valueOf(main.getDouble("temp"));
                urlConnection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return resultJson!!
        }

        protected override fun onPostExecute(jsonObject: JSONObject) {
            try {
                Utils.hideProgressDialog()
                if (jsonObject.has("city")) {
                    val city = jsonObject.getString("city")
                    setUpCity(city, jsonObject.getString("state"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //textView.setText("Current Weather: " + temp);
        }
    }

    private fun requestResultHandle() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
               if (REQUEST_FROM == REQUEST_FOR_ADDRESS && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val place = data!!.getParcelableExtra<Place>(Constant.PLACE_RESULT)
                        mBinding!!.etAddress.setText(place!!.address)
                        try {
                            val latLng = place.latLng
                            getPlaceInfo(latLng.latitude, latLng.longitude)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else if (result.resultCode == RESULT_CANCELED) {
                    Log.d("Result:", "Cancel")
                }
            }
    }
}