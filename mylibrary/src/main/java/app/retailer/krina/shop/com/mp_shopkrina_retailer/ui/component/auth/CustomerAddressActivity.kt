package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomerAddressBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth.SignupActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.TermOfServicesActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SaveCustomerLocalInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewUtils.Companion.snackbar
import com.google.android.libraries.places.api.model.Place

class CustomerAddressActivity : AppCompatActivity() {
    private var REQUEST_FROM = 1
    private val REQUEST_FOR_ADDRESS = 1001
    private val REQUEST_FOR_CITY = 1002
    private val REQUEST_FOR_AREA = 1003

    private lateinit var mBinding: ActivityCustomerAddressBinding
    private lateinit var viewModel: CustomerRegistrationViewModel

    private var cityPlaceId = ""
    private var city = ""
    private var cityLat = 0.0
    private var cityLng = 0.0

    private var areaPlaceId: String? = ""
    private var areaName: String? = ""
    private var areaLat = 0.0
    private var areaLng = 0.0

    private var addressPlaceId: String? = ""
    private var address: String? = ""
    private var addressLat = 0.0
    private var addressLng = 0.0

    private var zipcode: String? = ""
    private var flatOrFloorNumber: String? = ""
    private var landmark: String? = ""
    private var selectCityId = 0
    private var REDIRECT_FLAG = 0

    var model = CustomerAddressModel()
    var editProfileModel = EditProfileModel()
    private var cityList: ArrayList<CityModel>? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_address)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            CustomerRegistrationViewModelFactory(application, appRepository)
        )[CustomerRegistrationViewModel::class.java]

        if (intent.extras != null) {
            mBinding.liTerms.visibility = View.GONE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            REDIRECT_FLAG = intent.getIntExtra("REDIRECT_FLAG", 0)
            city = intent.getStringExtra("cityName")!!
            if (REDIRECT_FLAG == 2) {
                editProfileModel =
                    intent.getSerializableExtra("CUSTOMER_DETAILS") as EditProfileModel
                selectCityId = editProfileModel.cityid
                address = editProfileModel.shippingAddress
                val pinCode = editProfileModel.zipCode
                flatOrFloorNumber = editProfileModel.shippingAddress1
                landmark = editProfileModel.landMark

                if (!TextUtils.isNullOrEmpty(address)) {
                    mBinding.etAddress.setText(address)
                }
                if (!TextUtils.isNullOrEmpty(pinCode)) {
                    mBinding.etPincode.setText(pinCode)
                }
                if (!TextUtils.isNullOrEmpty(flatOrFloorNumber)) {
                    mBinding.etFlatFloorNo.setText(flatOrFloorNumber)
                }
                if (!TextUtils.isNullOrEmpty(landmark)) {
                    mBinding.etLandmark.setText(landmark)
                }
                val isVerified =
                    SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_VERIFY)
                if (!TextUtils.isNullOrEmpty(isVerified)) {
                    if (isVerified.equals("Full Verified", ignoreCase = true)) {
                        mBinding.etAddress.isEnabled = false
                        mBinding.etCity.isEnabled = false
                        mBinding.etFlatFloorNo.isEnabled = false
                        mBinding.etPincode.isEnabled = false
                        mBinding.etLandmark.isEnabled = false
                    }
                }
                mBinding.etCity.isClickable = false
                mBinding.etCity.isEnabled = false
            }
            mBinding.etCity.setText(city)
        }

        observe(viewModel.editCustomerInfoData, ::handleEditCustomerInfoResult)
        observe(viewModel.signupData, ::handleSignUpResult)
        observe(viewModel.getCustomerAddressData, ::handleCustomerAddressResult)
        requestResultHandle()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun requestResultHandle() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (REQUEST_FROM == REQUEST_FOR_CITY && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val place = data.getParcelableExtra<Place>(Constant.PLACE_RESULT)
                        city = data.getStringExtra(Constant.AREA)!!
                        mBinding.etCity.setText(city)
                        setUpCity(city)
                        cityPlaceId = place?.id ?: ""
                        cityLat = place?.latLng?.latitude ?: 0.0
                        cityLng = place?.latLng?.longitude ?: 0.0

                        Logger.logD("area", "area:: " + data.getStringExtra(Constant.AREA))
                        Logger.logD("", "Address:: " + place!!.address)
                    }
                } else if (REQUEST_FROM == REQUEST_FOR_AREA && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val place = data.getParcelableExtra<Place>(Constant.PLACE_RESULT)
                        areaPlaceId = place?.id
                        areaName = data.getStringExtra(Constant.AREA)
                        mBinding.etArea.setText(areaName)
                        areaLat = place?.latLng?.latitude!!
                        areaLng = place.latLng?.longitude!!

//                getPlaceInfo(place!!.latLng.latitude, place!!.latLng.longitude)
                        Logger.logD("area", "area:: " + data.getStringExtra(Constant.AREA))
                        Logger.logD("", "Address:: " + place.address)
                    }
                } else if (REQUEST_FROM == REQUEST_FOR_ADDRESS && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val place = data?.getParcelableExtra<Place>(Constant.PLACE_RESULT)
                        addressPlaceId = place?.id
                        address = data?.getStringExtra(Constant.ADDRESS)!!
                        mBinding.etAddress.setText(address)
                        addressLat = place?.latLng?.latitude!!
                        addressLng = place.latLng?.longitude!!
                        try {
                            if (place.addressComponents != null)
                                for (component in place.addressComponents!!.asList()) {
                                    if (component.types[0] == "postal_code") {
                                        mBinding.etPincode.setText(component.shortName)
                                        break
                                    }
                                }
                            if (TextUtils.isNullOrEmpty(mBinding.etPincode.text.toString())) {
                                mBinding.etPincode.isFocusable = true
                                mBinding.etPincode.isFocusableInTouchMode = true
                            } else {
                                mBinding.etPincode.isFocusable = false
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                } else if (result.resultCode == RESULT_CANCELED) {
                    Log.d("Result:", "Cancel")
                }
            }
    }
    private fun init() {
        title = MyApplication.getInstance().dbHelper.getString(R.string.title_serach_address)
        mBinding.address.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.search_delivery_address)
        mBinding.city.hint = MyApplication.getInstance().dbHelper.getString(R.string.city)
        mBinding.state.hint = MyApplication.getInstance().dbHelper.getString(R.string.state_astrick)
        mBinding.pincode.hint = MyApplication.getInstance().dbHelper.getString(R.string.pin_code)
        mBinding.flateOrFloorNumber.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.address_field_number)
        mBinding.landmark.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.landmark_optional)
        mBinding.btnSave.hint = MyApplication.getInstance().dbHelper.getString(R.string.save)
        customTextView(mBinding.tvTerms)

        mBinding.etCity.setOnClickListener {
            mBinding.etArea.setText("")
            mBinding.etAddress.setText("")
           /* startActivityForResult(
                Intent(applicationContext, CustomSearchPlaceActivity::class.java)
                    .putExtra(Constant.CITY_NAME, "")
                    .putExtra(Constant.IS_SEARCH_CITY, true), REQUEST_FOR_CITY
            )*/
            REQUEST_FROM = REQUEST_FOR_CITY
            val intent = Intent(applicationContext, CustomSearchPlaceActivity::class.java)
            intent.putExtra(Constant.CITY_NAME, "")
            intent.putExtra(Constant.IS_SEARCH_CITY, true)
            resultLauncher?.launch(intent)
            Utils.leftTransaction(this)
        }
        mBinding.etArea.setOnClickListener {
            if (city.isNotEmpty() && mBinding.etCity.text.toString().isNotEmpty()) {
                mBinding.etArea.setText("")
                mBinding.etAddress.setText("")
               /* startActivityForResult(
                    Intent(applicationContext, AddressSearchActivity::class.java)
                        .putExtra("type", 1)
                        .putExtra("cityname", city)
                        .putExtra("lat", cityLat)
                        .putExtra("lng", cityLng), REQUEST_FOR_AREA
                )*/
                REQUEST_FROM = REQUEST_FOR_AREA
                val intent = Intent(applicationContext, AddressSearchActivity::class.java)
                intent.putExtra(Constant.TYPE, 1)
                intent.putExtra(Constant.IS_SEARCH_CITY, false)
                intent.putExtra(Constant.CITY_NAME, city)
                intent.putExtra(Constant.LATITUDE, cityLat)
                intent.putExtra(Constant.LONGITUDE, cityLng)
                resultLauncher?.launch(intent)
                Utils.leftTransaction(this)
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().noteRepository.getString(R.string.select_city)
                )
            }
        }
        mBinding.etAddress.setOnClickListener {
            if (mBinding.etArea.text != null && mBinding.etArea.text.toString().isNotEmpty()) {
               /* startActivityForResult(
                    Intent(applicationContext, AddressSearchActivity::class.java)
                        .putExtra("type", 2)
                        .putExtra("cityname", city)
                        .putExtra("lat", areaLat)
                        .putExtra("lng", areaLng),
                    REQUEST_FOR_ADDRESS
                )*/
                REQUEST_FROM = REQUEST_FOR_ADDRESS
                val intent = Intent(applicationContext, AddressSearchActivity::class.java)
                intent.putExtra(Constant.TYPE, 2)
                intent.putExtra(Constant.IS_SEARCH_CITY, false)
                intent.putExtra(Constant.CITY_NAME, city)
                intent.putExtra(Constant.LATITUDE, areaLat)
                intent.putExtra(Constant.LONGITUDE, areaLng)
                resultLauncher?.launch(intent)
                Utils.leftTransaction(this)
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().noteRepository.getString(R.string.select_area)
                )
            }
        }
        mBinding.btnSave.setOnClickListener {
            address = mBinding.etAddress.text.toString()
            city = mBinding.etCity.text.toString()
            val state: String = mBinding.etState.text.toString()
            flatOrFloorNumber = mBinding.etFlatFloorNo.text.toString()
            landmark = mBinding.etLandmark.text.toString()
            if (!TextUtils.isNullOrEmpty(mBinding.etPincode.text.toString())) {
                zipcode = mBinding.etPincode.text.toString()
            }
            if (TextUtils.isNullOrEmpty(address)) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.enter_delivery_address)
                )
            } else if (TextUtils.isNullOrEmpty(city) && REDIRECT_FLAG == 1) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.select_city)
                )
            } else if (TextUtils.isNullOrEmpty(zipcode)) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.valid_pincode_number)
                )
            } else if (zipcode?.length!! < 6) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.valid_pincode_number)
                )
            } else if (intent.extras == null && !mBinding.checkbox.isChecked) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.check_terms_and_condition)
                )
            } else {
                model.customerId =
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
                model.cityPlaceId = cityPlaceId
                model.cityName = city
                model.state = state
                model.areaPlaceId = areaPlaceId
                model.areaText = areaName
                model.areaLat = areaLat
                model.areaLng = areaLng
                model.addressPlaceId = addressPlaceId
                model.addressText = address
                model.addressLat = addressLat
                model.addressLng = addressLng
                model.addressLineOne = flatOrFloorNumber
                model.addressLineTwo = landmark
                model.zipCode = zipcode
                if (REDIRECT_FLAG == 2) {
                    editProfileModel.shippingAddress = model.addressText
                    editProfileModel.lat = "" + model.addressLat
                    editProfileModel.lg = "" + model.addressLat
                    editProfileModel.zipCode = "" + model.zipCode
                    editProfileModel.addressModel = model
                    viewModel.editCustomerInfo(editProfileModel, localClassName)
                } else if (REDIRECT_FLAG == 3) {
                    startActivity(
                        Intent(
                            applicationContext,
                            SignupActivity::class.java
                        ).putExtra("model", model)
                    )
                } else {
                    val signupModel = NewSignupRequest(
                        SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                        "",
                        "",
                        SharePrefs.getInstance(applicationContext)
                            .getString(SharePrefs.MOBILE_NUMBER),
                        "",
                        address,
                        areaName,
                        flatOrFloorNumber,
                        landmark,
                        zipcode,
                        "123456",
                        selectCityId,
                        city,
                        SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE),
                        "",
                        "",
                        "",
                        "",
                        BuildConfig.VERSION_NAME,
                        Build.VERSION.RELEASE,
                        Build.MODEL,
                        Utils.getDeviceUniqueID(this),
                        Utils.getDeviceUniqueID(this),
                        addressLat,
                        addressLng,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false,
                        EndPointPref.getInstance(applicationContext)
                            .getFcmToken(EndPointPref.FCM_TOKEN),
                        "",
                        "",
                        "",
                        "",
                        city,
                        state,
                        zipcode,
                        0,
                        addressLat,
                        addressLng,
                        "",
                        model,
                        "Retailer"
                    )
                    viewModel.doNewSignup(signupModel)
                }
            }
        }

        viewModel.getCustAddress(
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )
        viewModel.getCity()
        viewModel.getCityData.observe(this) {
            cityList = it
            if (city.isNotEmpty()) {
                mBinding.etCity.setText(city)
                setUpCity(city)
            }
        }
    }

    private fun setUpCity(city: String) {
        if (cityList != null && cityList?.size != 0) {
            for (model in cityList!!) {
                if (city.equals(model.cityName, ignoreCase = true)) {
                    selectCityId = model.cityid
                    this@CustomerAddressActivity.model.cityId = selectCityId
                    cityLat = model.lat
                    cityLng = model.lng
                    break
                }
            }
        }
    }

    private fun customTextView(view: TextView) {
        val SpanString =
            SpannableString("Click here to accept Terms and conditions of Shopkirana Term of services")
        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val mIntent = Intent(applicationContext, TermOfServicesActivity::class.java)
                mIntent.putExtra("isTermsAndCondition", "isTermsAndCondition")
                startActivity(mIntent)
            }
        }
        // Character starting from 32 - 45 is Terms and condition.
        // Character starting from 49 - 63 is privacy policy.
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val mIntent = Intent(applicationContext, TermOfServicesActivity::class.java)
                mIntent.putExtra("isTermsAndCondition", "isPrivacyPolicy")
                startActivity(mIntent)
            }
        }
        SpanString.setSpan(termsAndCondition, 21, 41, 0)
        SpanString.setSpan(privacy, 56, 72, 0)
        SpanString.setSpan(ForegroundColorSpan(Color.BLUE), 21, 41, 0)
        SpanString.setSpan(ForegroundColorSpan(Color.BLUE), 56, 72, 0)
        SpanString.setSpan(UnderlineSpan(), 21, 41, 0)
        SpanString.setSpan(UnderlineSpan(), 56, 72, 0)
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(SpanString, TextView.BufferType.SPANNABLE)
        view.isSelected = true
    }

    private fun handleEditCustomerInfoResult(it: Response<MyProfileResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.setToast(
                        applicationContext,
                        it.message
                    )
                    Utils.hideProgressDialog()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                    Utils.rightTransaction(this@CustomerAddressActivity)
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    private fun handleSignUpResult(it: Response<SignupResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    val customer = it.customers
                    if (customer != null) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.signup_successfully_done)
                        )
                        MyApplication.getInstance().prefManager.isLoggedIn = true
                        SaveCustomerLocalInfo.saveCustomerInfo(applicationContext, customer,false)
                        MyApplication.getInstance().updateAnalytics("sign_up_complete")
                    }
                    if (intent.extras != null && REDIRECT_FLAG == 2) {
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                        Utils.rightTransaction(this@CustomerAddressActivity)
                    } else {
                        val bundle = Bundle()
                        val i1 = Intent(applicationContext, NewSignupActivity::class.java)
                        bundle.putSerializable(Constant.ADDRESS_INFO, model)
                        i1.putExtras(bundle)
                        startActivity(i1)
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    private fun handleCustomerAddressResult(it: Response<CustomerAddressModel>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    this.model = it
                    mBinding.etArea.setText(it.areaText)
                    mBinding.etAddress.setText(it.addressText)
                    mBinding.etPincode.setText(it.zipCode)
                    mBinding.etFlatFloorNo.setText(it.addressLineOne)
                    mBinding.etLandmark.setText(it.addressLineTwo)
                    areaPlaceId = it.areaPlaceId
                    areaName = it.areaText
                    areaLat = it.areaLat
                    areaLng = it.areaLng
                    addressPlaceId = it.addressPlaceId
                    address = it.addressText
                    addressLat = it.addressLat
                    addressLng = it.addressLng
                    flatOrFloorNumber = it.addressLineOne
                    landmark = it.addressLineTwo
                    zipcode = it.zipCode
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                // mBinding.root.snackbar(it.errorMesssage.toString())
            }
        }
    }


}