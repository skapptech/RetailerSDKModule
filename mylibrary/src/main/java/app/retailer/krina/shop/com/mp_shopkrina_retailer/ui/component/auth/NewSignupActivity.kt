package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.text.util.Linkify
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityNewSignupBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.TermOfServicesActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.DocTypeAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth.PlacesBillingSearchActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GstInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.picker.DatePickerFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.image.ImageCaptureActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.image.ImageProcessing
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.FusedLocation
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SaveCustomerLocalInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ViewUtils.Companion.snackbar
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.regex.Pattern

class NewSignupActivity : AppCompatActivity(), View.OnClickListener {
    private var REQUEST_FROM = 1
    private var REQUEST_FOR_SHIPPING_ADDRESS = 100
    private var REQUEST_FOR_BILLING_ADDRESS = 200
    private var REQUEST_FOR_CITY = 300
    private var REQUEST_FOR_SHOP_IMAGE = 400
    private var REQUEST_FOR_LICENCE_IMAGE = 500
    private var REQUEST_FOR_GST_IMAGE = 600
    private var REQUEST_FOR_PROFILE_IMAGE = 700
    private var mBinding: ActivityNewSignupBinding? = null
    private lateinit var viewModel: CustomerRegistrationViewModel
    private var pbDProgress: ProgressBar? = null
    private var tvDVerifiedMsg: TextView? = null
    private var custId = 0
    private var customerDocTypeId = 0
    private var selectCityId = 0
    private var fProfile = ""
    private var fGSTName = ""
    private var fShop = ""
    private var fLicenseName = ""
    private var mobile = ""
    private var zipCode = ""
    private var skCode: String? = null
    private var gstSearch = ""
    private var sGstNo = ""
    private var sLicenseNo = ""
    private var ReferralSkCode = ""
    private var nameOnGst = ""
    private var gstBillingAddress: String? = ""
    private var gstCity = ""
    private var gstState: String? = ""
    private var gstZipcode = ""
    private var RefNoGST = false
    private var shopLat = 0.0
    private var shopLng = 0.0
    private var cityList: ArrayList<CityModel>? = null
    private var docTypeList: ArrayList<DocTypeModel>? = null
    private var docTypeAdapter: DocTypeAdapter? = null
    private var mGeocoder: Geocoder? = null
    private var addressModel: AddressModel? = null
    private var verifyGSTDialog: Dialog? = null
    private var referrerClient: InstallReferrerClient? = null
    private var customerAddressModel: CustomerAddressModel? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var fusedLocation: FusedLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_signup)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            CustomerRegistrationViewModelFactory(application, appRepository)
        )[CustomerRegistrationViewModel::class.java]
        mGeocoder = Geocoder(applicationContext, Locale.ENGLISH)
        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient!!.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(i: Int) {
                try {
                    val response = referrerClient!!.installReferrer
                    val referrerUrl = response.installReferrer
                    println("TAG> $referrerUrl")
                    //SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.REFERRAL_BY, referrerUrl);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onInstallReferrerServiceDisconnected() {}
        })
        val bundle = intent.extras
        if (bundle != null) {
            customerAddressModel =
                bundle.getSerializable(Constant.ADDRESS_INFO) as CustomerAddressModel?
            addressModel = AddressModel()
            addressModel!!.billingState = customerAddressModel!!.state
            addressModel!!.cityName = customerAddressModel!!.cityName
            addressModel!!.address = customerAddressModel!!.addressText
            addressModel!!.flateOrFloorNumber = customerAddressModel!!.addressLineOne
            addressModel!!.pincode = customerAddressModel!!.zipCode
            addressModel!!.landmark = customerAddressModel!!.addressLineTwo
            addressModel!!.latitude = customerAddressModel!!.addressLat
            addressModel!!.longitude = customerAddressModel!!.addressLng
            selectCityId = customerAddressModel!!.cityId
            zipCode = customerAddressModel!!.zipCode!!
            gstCity = customerAddressModel!!.cityName!!
            gstState = customerAddressModel!!.state
            gstZipcode = customerAddressModel!!.zipCode!!
            gstBillingAddress = customerAddressModel!!.addressText
            mBinding!!.etCity.setText(customerAddressModel!!.cityName)
            mBinding!!.etAddress.setText(customerAddressModel!!.addressText)
            mBinding!!.etCity.isClickable = false
            mBinding!!.etAddress.isClickable = false
            if (Utils.gpsPermission(this, "runtime")) {
                getShopLocation()
            }
        }
        //All permission
        requestResultHandle()
        askPermission()
        observe(viewModel.uploadImageData, ::handleImageUploadResult)
        observe(viewModel.signupData, ::handleSignUpResult)
        observe(viewModel.gstInfoResponseData, ::handleGSTVerifyResult)

    }

    override fun onStop() {
        super.onStop()
        if (fusedLocation != null) {
            fusedLocation!!.onStop()
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initialization()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnSkip -> {
                startActivity(
                    Intent(applicationContext, SplashScreenActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }

            R.id.iv_shop_photo -> {
                if (Utils.gpsPermission(this, "clicktimer")) {
                    getShopLocation()
                }
                REQUEST_FROM = REQUEST_FOR_SHOP_IMAGE
                fShop = custId.toString() + "_" + System.currentTimeMillis() + "ShopImage.jpg"
                openSomeActivityForResult(fShop, false)
            }

            R.id.iv_gst_photo -> {
                REQUEST_FROM = REQUEST_FOR_GST_IMAGE
                fGSTName = custId.toString() + "_" + System.currentTimeMillis() + "GST.jpg"
                openSomeActivityForResult(fGSTName, true)
            }

            R.id.iv_license_photo -> {
                REQUEST_FROM = REQUEST_FOR_LICENCE_IMAGE
                fLicenseName = custId.toString() + "_" + System.currentTimeMillis() + "License.jpg"
                openSomeActivityForResult(fLicenseName, true)
            }

            R.id.iv_cust_profile -> {
                REQUEST_FROM = REQUEST_FOR_PROFILE_IMAGE
                fProfile = custId.toString() + "_" + System.currentTimeMillis() + "Profile.jpg"
                openSomeActivityForResult(fProfile, true)
            }

            R.id.ivShippingAddress ->
                //                String cityName = mBinding.etCity.getText().toString();
//                if (mBinding.etCity.getText().toString().isEmpty()) {
//                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.select_city_validation));
//                } else {
//                    startActivityForResult(new Intent(getApplicationContext(), PlacesSearchActivity.class)
//                            .putExtra("REDIRECT_FLAG", 1)
//                            .putExtra("cityName", cityName), REQUEST_FOR_SHIPPING_ADDRESS);
//                    Utils.leftTransaction(this);
//                }
                onBackPressed()

            R.id.etAddress ->
                //                cityName = mBinding.etCity.getText().toString();
//                if (mBinding.etCity.getText().toString().isEmpty()) {
//                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.select_city_validation));
//                } else {
//                    startActivityForResult(new Intent(getApplicationContext(), PlacesSearchActivity.class)
//                            .putExtra("REDIRECT_FLAG", 1)
//                            .putExtra("cityName", cityName), REQUEST_FOR_SHIPPING_ADDRESS);
//                    Utils.leftTransaction(this);
//                }
                onBackPressed()

            R.id.etCity, R.id.im_city ->
                //                mBinding.etAddress.setText("");
//                startActivityForResult(new Intent(getApplicationContext(), CustomSearchPlaceActivity.class)
//                        .putExtra("cityname", "")
//                        .putExtra("searchCity", true), REQUEST_FOR_CITY);
//                Utils.leftTransaction(this);
                onBackPressed()

            R.id.imBillingAddress, R.id.etBillingAddress -> {
                /* startActivityForResult(
                     Intent(applicationContext, PlacesBillingSearchActivity::class.java)
                         .putExtra("REDIRECT_FLAG", 1)
                         .putExtra("cityName", ""), REQUEST_FOR_BILLING_ADDRESS
                 )*/
                REQUEST_FROM = REQUEST_FOR_BILLING_ADDRESS
                val intent = Intent(applicationContext, PlacesBillingSearchActivity::class.java)
                intent.putExtra("REDIRECT_FLAG", 1)
                intent.putExtra("cityName", "")
                resultLauncher?.launch(intent)
                Utils.leftTransaction(this)
            }

            R.id.btn_signup -> validateFields()

            R.id.tv_login -> startActivity(
                Intent(
                    applicationContext,
                    MobileSignUpActivity::class.java
                )
            )

            R.id.btnExpiryDate -> {
                val datePickerFragment = DatePickerFragment()
                val bundle = Bundle()
                bundle.putString("minMax", "0")
                datePickerFragment.arguments = bundle
                datePickerFragment.setListener { year: Int, month: Int, day: Int ->
                    mBinding!!.btnExpiryDate.text = day.toString() + "/" + (month + 1) + "/" + year
                }
                datePickerFragment.show(supportFragmentManager, "datePicker")
            }
        }

    }

    private fun getShopLocation() {
        fusedLocation = FusedLocation(applicationContext)
        fusedLocation!!.getLocation()
        fusedLocation!!.locationLiveData.observe(this) {
            it?.let {
                if (it != null) {
                    shopLat = it.latitude
                    shopLng = it.longitude
                    println("shopLat::$shopLat-----shopLng$shopLng")
                }
            }
        }
    }

    private fun openSomeActivityForResult(fileName: String, isGallery: Boolean) {
        val intent = Intent(applicationContext, ImageCaptureActivity::class.java)
        intent.putExtra(Constant.FILE_NAME, fileName)
        intent.putExtra(Constant.IS_GALLERY_OPTION, isGallery)
        resultLauncher?.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mUpdateReceiver,
            IntentFilter("ACTION_UPDATE_DATA")
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("name", mBinding!!.etFullName.text.toString())
        super.onSaveInstanceState(outState)
        outState.putString("shopName", mBinding!!.etShopName.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mBinding!!.etFullName.setText(savedInstanceState.getString("name"))
        mBinding!!.etShopName.setText(savedInstanceState.getString("shopName"))
    }

    private fun initialization() {
        mobile = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER)
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        skCode = SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE)
        mBinding!!.ivShopPhoto.setOnClickListener(this)
        mBinding!!.ivGstPhoto.setOnClickListener(this)
        mBinding!!.ivLicensePhoto.setOnClickListener(this)
        mBinding!!.ivCustProfile.setOnClickListener(this)
        mBinding!!.tvLogin.setOnClickListener(this)
        mBinding!!.btnSignup.setOnClickListener(this)
        mBinding!!.lerAddress.setOnClickListener(this)
        mBinding!!.ivShippingAddress.setOnClickListener(this)
        mBinding!!.etAddress.setOnClickListener(this)
        mBinding!!.etCity.setOnClickListener(this)
        mBinding!!.imCity.setOnClickListener(this)
        mBinding!!.etBillingAddress.setOnClickListener(this)
        mBinding!!.imBillingAddress.setOnClickListener(this)
        mBinding!!.btnSkip.setOnClickListener(this)
        mBinding!!.btnExpiryDate.setOnClickListener(this)
        mBinding!!.tvPD.text =
            MyApplication.getInstance().dbHelper.getString(R.string.personal_details)
        mBinding!!.tilRefCode.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.referral_code)
        mBinding!!.tilName.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.first_name)
        mBinding!!.tilEmail.hint = MyApplication.getInstance().dbHelper.getString(R.string.email)
        mBinding!!.tilPass.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.hint_passwordr)
        mBinding!!.tvShopD.text =
            MyApplication.getInstance().dbHelper.getString(R.string.shop_details)
        mBinding!!.tilGst.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_gstin)
        mBinding!!.tilLicence.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.enter_license_no)
        mBinding!!.tilSPName.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.hint_shopname)
        mBinding!!.tilCity.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.select_city)
        mBinding!!.tilAdd.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.hint_shipping_address)
        mBinding!!.tilBillingAddress.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.hint_billing_address)
        mBinding!!.txtCbCheckSameAddress.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_check_shiping_address)
        mBinding!!.tilBankName.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.bank_name)
        mBinding!!.tilAccountNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.account_number)
        mBinding!!.tilIFSC.hint = MyApplication.getInstance().dbHelper.getString(R.string.ifsc_code)
        mBinding!!.btnSignup.text = MyApplication.getInstance().dbHelper.getString(R.string.next)
        mBinding!!.etReferralCode.setText(
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.REFERRAL_BY)
        )
        mBinding!!.tilPanNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.pannumber)
        mBinding!!.tilAadharNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.aadhar_number)
        val text = (MyApplication.getInstance().dbHelper.getString(R.string.already_account)
                + "<font color=#FF4500>" + MyApplication.getInstance().dbHelper.getString(R.string.login)
                + "</font>")
        mBinding!!.tvLogin.text = Html.fromHtml(text)
        mBinding!!.etGstNo.isFocusable = false
        mBinding!!.etGstNo.setOnClickListener { view: View? -> showGstRequestDialog() }
        mBinding!!.tvChangeGst.setOnClickListener { view: View? -> showGstRequestDialog() }
        customTextView(mBinding!!.tvTerms)
        mBinding!!.cbBillAddressSame.setOnClickListener { v: View? ->
            if (mBinding!!.cbBillAddressSame.isChecked) {
                if (!TextUtils.isNullOrEmpty(
                        mBinding!!.etAddress.text.toString()
                    )
                ) {
                    val add = mBinding!!.etAddress.text.toString().trim { it <= ' ' }
                    if (add != null && add != "" && addressModel != null && addressModel!!.address != null) {
                        mBinding!!.etBillingAddress.setText(add)
                        gstBillingAddress = addressModel!!.address
                        gstCity = addressModel!!.cityName
                        gstState = ""
                        gstZipcode = addressModel!!.pincode
                    } else {
                        mBinding!!.etBillingAddress.setText("")
                    }
                } else {
                    mBinding!!.cbBillAddressSame.isChecked = false
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.enter_shipping_address)
                    )
                }
            }
        }
        mBinding!!.tvRemoveGst.setOnClickListener { v: View? ->
            RefNoGST = false
            nameOnGst = ""
            mBinding!!.imBillingAddress.isEnabled = true
            mBinding!!.etBillingAddress.isEnabled = true
            mBinding!!.LLBillingAddCheck.visibility = View.VISIBLE
            mBinding!!.tvRemoveGst.visibility = View.GONE
            mBinding!!.etShopName.setText("")
            mBinding!!.etBillingAddress.setText("")
            mBinding!!.etGstNo.setText("")
            gstBillingAddress = ""
            gstCity = ""
            gstState = ""
            gstZipcode = ""
            mBinding!!.etPanNo.isEnabled = true
        }
        mBinding!!.spDocType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i != 0) mBinding!!.btnExpiryDate.visibility =
                    View.VISIBLE else mBinding!!.btnExpiryDate.visibility = View.GONE
                if (docTypeList!![i]!!.docType.equals("GST", ignoreCase = true)) {
                    mBinding!!.liGst.visibility = View.VISIBLE
                    mBinding!!.rlChangeGst.visibility = View.VISIBLE
                    mBinding!!.liLicense.visibility = View.GONE
                    // clear
                    mBinding!!.etLicenseNo.setText("")
                } else {
                    mBinding!!.liLicense.visibility = View.VISIBLE
                    mBinding!!.liGst.visibility = View.GONE
                    mBinding!!.rlChangeGst.visibility = View.GONE
                    // clear
                    mBinding!!.etGstNo.setText("")
                    mBinding!!.etShopName.setText("")
                    mBinding!!.etBillingAddress.setText("")
                    mBinding!!.etShopName.isEnabled = true
                    mBinding!!.etShopName.isEnabled = true
                    mBinding!!.LLBillingAddCheck.visibility = View.VISIBLE
                    mBinding!!.tvRemoveGst.visibility = View.GONE
                    mBinding!!.imBillingAddress.isEnabled = true
                    mBinding!!.etBillingAddress.isEnabled = true
                    RefNoGST = false
                    mBinding!!.etPanNo.isEnabled = true
                }
                if (docTypeList!!.size > 0) customerDocTypeId = docTypeList!![i]!!.id
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        viewModel.getCustomerDocType(
            SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID),
            custId
        )
        viewModel.getDocumentTypeData.observe(this) {
            if (it.size != 0) {
                docTypeList = it
                docTypeList!!.add(0, DocTypeModel(0, "Select Document Type"))
                docTypeAdapter = DocTypeAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    docTypeList!!
                )
                mBinding!!.spDocType.adapter = docTypeAdapter
                //            for (int i = 0; i < docTypeList.size(); i++) {
//                if (docTypeList.get(i).docType.equalsIgnoreCase("gst")) {
//                    mBinding.spDocType.setSelection(i);
//                    break;
//                }
//            }
            }
        }

        viewModel.getCity()
        viewModel.getCityData.observe(this) {
            cityList = it
        }
    }

    private fun searchGSTInfo(gstSearch: String) {
        viewModel.getGstStatus(gstSearch, "New customer registration")
    }

    private fun validateFields() {
        val flag: Boolean
        val emailFlag: Boolean
        val fName = mBinding!!.etFullName.text.toString().trim { it <= ' ' }
        val password = mBinding!!.etPassword.text.toString().trim { it <= ' ' }
        val shopName = mBinding!!.etShopName.text.toString().trim { it <= ' ' }
        val shippingAddress = mBinding!!.etAddress.text.toString().trim { it <= ' ' }
        //   String billingAddress = mBinding.etBillingAddress.getText().toString().trim();
        val bankName = mBinding!!.etBankName.text.toString().trim { it <= ' ' }
        val accountNumber = mBinding!!.etAccountNumber.text.toString().trim { it <= ' ' }
        val ifscCode = mBinding!!.etIfscCode.text.toString().trim { it <= ' ' }
        sGstNo = mBinding!!.etGstNo.text.toString().trim { it <= ' ' }
        sLicenseNo = mBinding!!.etLicenseNo.text.toString().trim { it <= ' ' }
        val panNumber = mBinding!!.etPanNo.text.toString().trim { it <= ' ' }
        val aadharNumber = mBinding!!.etAadharNo.text.toString().trim { it <= ' ' }
        val sEmailId = mBinding!!.etEmail.text.toString().trim { it <= ' ' }
        ReferralSkCode = mBinding!!.etReferralCode.text.toString().trim { it <= ' ' }
        var licenseExpiryDate = mBinding!!.btnExpiryDate.text.toString().trim { it <= ' ' }
        if (!licenseExpiryDate.isEmpty()) licenseExpiryDate =
            Utils.getSimpleDateFormat(licenseExpiryDate)
        val hasDoc = sLicenseNo.length + sGstNo.length > 0
        emailFlag = if (!sEmailId.isEmpty()) {
            !Utils.isValidEmail(sEmailId)
        } else {
            false
        }
        flag = if (!sGstNo.isEmpty()) {
            sGstNo.length == 15
        } else {
            true
        }
        if (!flag) {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.gst_length)
            )
        } else if (emailFlag) {
            mBinding!!.etEmail.error =
                MyApplication.getInstance().dbHelper.getString(R.string.valid_email_address)
            mBinding!!.etEmail.requestFocus()
        } else if (TextUtils.isNullOrEmpty(shopName)) {
            mBinding!!.etShopName.error =
                MyApplication.getInstance().dbHelper.getString(R.string.enter_shop_Name)
            mBinding!!.etShopName.requestFocus()
        } else if (mBinding!!.spDocType.selectedItemPosition != 0 && !hasDoc) {
            if (mBinding!!.liGst.visibility == View.VISIBLE) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.gst_length)
                )
            }
            if (mBinding!!.liLicense.visibility == View.VISIBLE) {
                mBinding!!.etLicenseNo.error =
                    MyApplication.getInstance().dbHelper.getString(R.string.enter_license_number)
                mBinding!!.etLicenseNo.requestFocus()
            }
            //        } else if (hasDoc && licenseExpiryDate.isEmpty()) {
//            Utils.setToast(
//                    getApplicationContext(),
//                    MyApplication.getInstance().dbHelper.getString(R.string.select_license_expiry_date)
//            );
        } else if (TextUtils.isNullOrEmpty(shippingAddress)) {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.enter_shipping_address)
            )
        } else if (TextUtils.isNullOrEmpty(gstBillingAddress)) {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.enter_billing_address)
            )
        } else if (panNumber != "" && !Utils.isValidPanCardNo(panNumber.uppercase(Locale.getDefault()))) {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.valid_pan_number)
            )
        } else if (RefNoGST && panNumber == "") {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.txt_please_enter_pan_card)
            )
        } else if (aadharNumber != "" && aadharNumber.length < 12) {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.valid_aadhar_number)
            )
            //        } else if (!mBinding.checkbox.isChecked()) {
//            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.check_terms_and_condition));
        } else {
            mBinding!!.etFullName.error = null
            mBinding!!.etShopName.error = null
            mBinding!!.etEmail.error = null
            mBinding!!.etGstNo.error = null
            mBinding!!.etLicenseNo.error = null
            mBinding!!.etReferralCode.error = null
            // API call SignIn
            if (addressModel != null && !TextUtils.isNullOrEmpty(
                    addressModel!!.pincode
                )
            ) {
                zipCode = addressModel!!.pincode
            }
            if (!RefNoGST) {
                sGstNo = ""
            }
            val signupModel = NewSignupRequest(
                custId,
                fName,
                fProfile,
                mobile,
                sEmailId,
                shippingAddress,
                customerAddressModel!!.areaText,
                if (addressModel == null) "" else addressModel!!.flateOrFloorNumber,
                if (addressModel == null) "" else addressModel!!.landmark,
                zipCode,
                password,
                selectCityId,
                if (addressModel == null) "" else addressModel!!.cityName,
                skCode,
                sGstNo,
                fGSTName,
                shopName,
                fShop,
                BuildConfig.VERSION_NAME,
                Build.VERSION.RELEASE,
                Build.MODEL,
                Utils.getDeviceUniqueID(this),
                Utils.getDeviceUniqueID(this),
                addressModel!!.latitude,
                addressModel!!.longitude,
                sLicenseNo,
                fLicenseName,
                bankName,
                accountNumber,
                ifscCode,
                ReferralSkCode,
                false,
                EndPointPref.getInstance(applicationContext).getFcmToken(EndPointPref.FCM_TOKEN),
                aadharNumber,
                panNumber,
                nameOnGst,
                gstBillingAddress,
                gstCity,
                gstState,
                gstZipcode,
                customerDocTypeId,
                shopLat,
                shopLng,
                licenseExpiryDate,
                customerAddressModel,
                "Retailer"
            )
            viewModel.doNewSignup(signupModel)
        }
    }

    private fun askPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {
                getShopLocation()
            }
            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                super.onDenied(context, deniedPermissions)
              askPermission()
            }
        })
    }

    private fun getPlaceInfo(lat: Double, lon: Double) {
        try {
            val addresses = mGeocoder!!.getFromLocation(lat, lon, 1)
            if (addresses!![0].locality != null) {
                val city = addresses[0].locality
                mBinding!!.etCity.setText(city)
                setUpCity(city)
            } else {
                Utils.showProgressDialog(this)
                val url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
                    lat,
                    lon
                )
                FetchCityUsingAPI().execute(url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.showProgressDialog(this)
            val url = String.format(
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1",
                lat,
                lon
            )
            FetchCityUsingAPI().execute(url)
        }
    }

    private fun setUpCity(city: String) {
        if (cityList != null && cityList!!.size != 0) {
            for (model in cityList!!) {
                if (city.equals(model.cityName, ignoreCase = true)) {
                    selectCityId = model.cityid
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

    private fun launchHomeScreen() {
        try {
            MyApplication.getInstance().clearLocalData()
            MyApplication.getInstance().clearCartData()
            // start analytic new session
            MyApplication.getInstance().startAnalyticSession()
            startActivity(
                Intent(applicationContext, SplashScreenActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
            Utils.leftTransaction(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showGstRequestDialog() {
        verifyGSTDialog = Dialog(this)
        verifyGSTDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        verifyGSTDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        verifyGSTDialog!!.setContentView(R.layout.dialog_gst_verify)
        verifyGSTDialog!!.setCancelable(false)
        val tvTitle = verifyGSTDialog!!.findViewById<TextView>(R.id.tvTitle)
        val etDialogGstNumber = verifyGSTDialog!!.findViewById<EditText>(R.id.etDialogGstNumber)
        val tvSkip = verifyGSTDialog!!.findViewById<Button>(R.id.btnSkip)
        val tvVerify = verifyGSTDialog!!.findViewById<Button>(R.id.btnVerify)
        tvDVerifiedMsg = verifyGSTDialog!!.findViewById(R.id.tvDVerifiedMsg)
        pbDProgress = verifyGSTDialog!!.findViewById(R.id.pbDProgress)
        tvSkip.setOnClickListener { view: View? -> verifyGSTDialog!!.dismiss() }
        etDialogGstNumber.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                gstSearch = etDialogGstNumber.text.toString().trim { it <= ' ' }
                if (gstSearch.length >= 15) {
                    MyApplication.getInstance().updateAnalytics("verify_gst_click")
                    pbDProgress!!.visibility = View.VISIBLE
                    tvDVerifiedMsg!!.text = MyApplication.getInstance().dbHelper.getString(R.string.verifying_gst)
                    searchGSTInfo(gstSearch)
                } else {
                    Utils.setToast(
                        applicationContext, "Enter Valid GST Number."
                    )
                }
                return@setOnEditorActionListener true
            }
            false
        }
        tvVerify.setOnClickListener { view: View? ->
            gstSearch = etDialogGstNumber.text.toString().trim { it <= ' ' }
            if (gstSearch.length >= 15) {
                MyApplication.getInstance().updateAnalytics("verify_gst_click")
                pbDProgress!!.visibility = View.VISIBLE
                tvDVerifiedMsg!!.text = MyApplication.getInstance().dbHelper.getString(R.string.verifying_gst)
                searchGSTInfo(gstSearch)
            } else {
                Utils.setToast(
                    applicationContext, "Enter Valid GST Number."
                )
            }
        }
        verifyGSTDialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        verifyGSTDialog!!.show()
    }

    fun showCongratulationDialog(skCode: String?, criMSG: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_signup_confirm)
        dialog.setCancelable(false)
        val txtMessage = dialog.findViewById<TextView>(R.id.txtMessage)
        val titleMsg = dialog.findViewById<TextView>(R.id.title_msg)
        val callus = dialog.findViewById<TextView>(R.id.callus)
        val webside = dialog.findViewById<TextView>(R.id.webside)
        val tvDSkCode = dialog.findViewById<TextView>(R.id.tvDSkCode)
        val tvKnowMore = dialog.findViewById<TextView>(R.id.tvKnowMore)
        val tvSkCode = dialog.findViewById<TextView>(R.id.tv_sk_code)
        val welComeBtn = dialog.findViewById<Button>(R.id.btn_welcome)
        val tvReferralMsg = dialog.findViewById<TextView>(R.id.tvReferralMsg)
        welComeBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.welcome)
        tvDSkCode.text = MyApplication.getInstance().dbHelper.getString(R.string.your_sk_code)
        tvKnowMore.text = MyApplication.getInstance().dbHelper.getString(R.string.know_about_app)
        if (!TextUtils.isNullOrEmpty(ReferralSkCode)) tvReferralMsg.text =
            MyApplication.getInstance().noteRepository.getString(R.string.signup_refer_use_msg) else tvReferralMsg.visibility =
            View.GONE
        val text1 = "Call:" + "  " + "<font color=#FF4500> +91 " + "7828112112" + "</font>"
        callus.text = Html.fromHtml(text1)
        callus.autoLinkMask = Linkify.PHONE_NUMBERS
        if (criMSG) {
            txtMessage.text = MyApplication.getInstance().dbHelper.getString(R.string.sub_prof)
        } else {
            titleMsg.text =
                MyApplication.getInstance().dbHelper.getString(R.string.unregister_member)
            txtMessage.text =
                MyApplication.getInstance().dbHelper.getString(R.string.Congratulation)
            titleMsg.text = MyApplication.getInstance().dbHelper.getString(R.string.register_member)
        }
        tvSkCode.text = skCode
        welComeBtn.setOnClickListener { v: View? ->
            dialog.dismiss()
            launchHomeScreen()
        }
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }


    @Suppress("deprecation")
    @SuppressLint("StaticFieldLeak")
    private inner class FetchCityUsingAPI : AsyncTask<String, Void, JSONObject>() {
        protected override fun doInBackground(vararg strings: String): JSONObject? {
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
                urlConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return resultJson
        }

        override fun onPostExecute(jsonObject: JSONObject?) {
            try {
                Utils.hideProgressDialog()
                if (jsonObject!!.has("city")) {
                    val city = jsonObject.getString("city")
                    mBinding!!.etCity.setText(city)
                    setUpCity(city)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private val mUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("RefTag", intent.extras!!.getString("referrer")!!)
            mBinding!!.etReferralCode.setText(intent.extras!!.getString("referrer"))
        }
    }

    private fun requestResultHandle() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (REQUEST_FROM == REQUEST_FOR_SHIPPING_ADDRESS && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    addressModel = data!!.getParcelableExtra(Constant.CUSTOMER_ADDRESS)
                    mBinding!!.etAddress.setText(addressModel!!.address)
                    Logger.logD("", "AddressData::" + addressModel!!.address)
                } else if (REQUEST_FROM == REQUEST_FOR_BILLING_ADDRESS && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val addressModel = data.getParcelableExtra<AddressModel>(Constant.CUSTOMER_ADDRESS)
                        mBinding!!.etBillingAddress.setText(addressModel!!.billingAddress)
                        gstBillingAddress = addressModel.billingAddress
                        gstCity = addressModel.billingCity
                        gstState = addressModel.billingState
                        gstZipcode = addressModel.billingZipCode
                    }
                } else if (REQUEST_FROM == REQUEST_FOR_CITY && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val place = data.getParcelableExtra<Place>("PlaceResult")
                        getPlaceInfo(place!!.latLng.latitude, place.latLng.longitude)
                        Logger.logD("area", "area:: " + data.getStringExtra("area"))
                        Logger.logD("", "Address:: " + place.address)
                    }
                } else if (REQUEST_FROM == REQUEST_FOR_SHOP_IMAGE && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val filePath = data?.getStringExtra(Constant.FILE_PATH).toString()
                    var newFilePath = ""
                    lifecycleScope.launch {
                        ImageProcessing.getBody.observe(this@NewSignupActivity) {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.txt_capture)
                            )
                            viewModel.uploadImage(it)
                        }
                    }
                    class WorkerTask : AsyncTask<String, String, String>() {
                        override fun onPreExecute() {
                            super.onPreExecute()
                            Utils.showProgressDialog(this@NewSignupActivity)
                        }

                        override fun doInBackground(vararg p0: String?): String {
                            val bitmap = BitmapFactory.decodeFile(filePath)
                            newFilePath = ImageProcessing.addWaterMark(
                                applicationContext, fShop, bitmap,
                                LatLng(shopLat, shopLng)
                            )
                            return ""
                        }

                        override fun onPostExecute(result: String?) {
                            super.onPostExecute(result)
                            lifecycleScope.launch {
                                ImageProcessing.uploadMultipart(newFilePath, applicationContext)
                            }
                            }
                    }
                    WorkerTask().execute()

                } else if (REQUEST_FROM == REQUEST_FOR_LICENCE_IMAGE && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val filePath = data?.getStringExtra(Constant.FILE_PATH).toString()
                    lifecycleScope.launch {
                        ImageProcessing.uploadMultipart(filePath, applicationContext)
                        ImageProcessing.getBody.observe(this@NewSignupActivity) {
                            viewModel.uploadImage(it)
                        }
                    }

                } else if (REQUEST_FROM == REQUEST_FOR_GST_IMAGE && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val filePath = data?.getStringExtra(Constant.FILE_PATH).toString()
                    lifecycleScope.launch {
                        ImageProcessing.uploadMultipart(filePath, applicationContext)
                        ImageProcessing.getBody.observe(this@NewSignupActivity) {
                            viewModel.uploadImage(it)
                        }

                    }
                } else if (REQUEST_FROM == REQUEST_FOR_PROFILE_IMAGE && result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val filePath = data?.getStringExtra(Constant.FILE_PATH).toString()
                    lifecycleScope.launch {
                        ImageProcessing.uploadMultipart(filePath, applicationContext)
                        ImageProcessing.getBody.observe(this@NewSignupActivity) {
                            viewModel.uploadImage(it)
                        }

                    }
                } else if (result.resultCode == RESULT_CANCELED) {
                    Log.d("Result:", "Cancel")
                }
            }
    }

    fun getPANNumber(str: String): String {
        var panNumber = ""
        val strPattern = arrayOf("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        for (i in strPattern.indices) {
            val pattern = Pattern.compile(strPattern[i])
            val matcher = pattern.matcher(str.uppercase(Locale.getDefault()))
            while (matcher.find()) {
                panNumber = matcher.group()
            }
        }
        return panNumber
    }

    private fun handleImageUploadResult(it: Response<String>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (REQUEST_FROM == REQUEST_FOR_SHOP_IMAGE) {
                        mBinding!!.ivShopPhoto.setImageResource(R.drawable.ic_photo_camera_orange_24dp)
                        fShop = it
                    } else if (REQUEST_FROM == REQUEST_FOR_GST_IMAGE) {
                        fGSTName = it
                        mBinding!!.ivGstPhoto.setImageResource(R.drawable.ic_photo_camera_orange_24dp)
                    } else if (REQUEST_FROM == REQUEST_FOR_LICENCE_IMAGE) {
                        fLicenseName = it
                        mBinding!!.ivLicensePhoto.setImageResource(R.drawable.ic_photo_camera_orange_24dp)
                    } else if (REQUEST_FROM == REQUEST_FOR_PROFILE_IMAGE) {
                        val vectorDrawable = AppCompatResources.getDrawable(
                            applicationContext, R.drawable.profile_round
                        )
                        val img: String = Constant.BASE_URL_PROFILE + it
                        fProfile = it
                        Picasso.get().load(img).placeholder(vectorDrawable!!).error(vectorDrawable)
                            .into(mBinding!!.ivCustProfile)
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding!!.root.snackbar(it.errorMesssage.toString())
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
                        showCongratulationDialog(
                            customer.skcode,
                            TextUtils.isNullOrEmpty(sGstNo) || TextUtils.isNullOrEmpty(sLicenseNo)
                        )
                        MyApplication.getInstance().updateAnalytics("sign_up_complete")
                    } else {
                        launchHomeScreen()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

    private fun handleGSTVerifyResult(it: Response<GstInfoResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    val gstMsg = it.message
                    pbDProgress!!.visibility = View.GONE
                    tvDVerifiedMsg!!.text = gstMsg
                    if (it.status) {
                        if (it.custverify != null) {
                            mBinding!!.LLBillingAddCheck.visibility = View.GONE
                            mBinding!!.tvRemoveGst.visibility = View.VISIBLE
                            RefNoGST = true
                            Utils.setToast(
                                applicationContext, "" + gstMsg
                            )
                            mBinding!!.etGstNo.setText(gstSearch)
                            mBinding!!.etPanNo.setText(getPANNumber(gstSearch))
                            mBinding!!.etPanNo.isEnabled = false
                            if (verifyGSTDialog != null) {
                                verifyGSTDialog!!.dismiss()
                            }
                            nameOnGst = it.custverify?.name!!
                            gstZipcode = it.custverify?.zipcode!!
                            gstCity = it.custverify?.city!!
                            gstState = it.custverify?.state
                            if (!TextUtils.isNullOrEmpty(it.custverify?.shopname)) {
                                mBinding!!.etShopName.setText(it.custverify?.shopname)
                                mBinding!!.etBillingAddress.setText(it.custverify?.shippingAddress)
                            }
                            mBinding!!.imBillingAddress.isEnabled = false
                            mBinding!!.etBillingAddress.isEnabled = false
                            //zipCode = model.getCustverify().getZipcode();
                            val isActiveGst = it.custverify?.active
                            gstBillingAddress = it.custverify?.shippingAddress
                            if (isActiveGst == "Active") {
                                mBinding!!.etShopName.isEnabled = false
                            }
                        }
                    } else {
                        RefNoGST = false
                        mBinding!!.etPanNo.isEnabled = true
                        nameOnGst = ""
                        Utils.setToast(
                            applicationContext, "" + gstMsg
                        )
                        mBinding!!.imBillingAddress.isEnabled = true
                        mBinding!!.etBillingAddress.isEnabled = true
                        mBinding!!.etShopName.isEnabled = true
                        mBinding!!.LLBillingAddCheck.visibility = View.VISIBLE
                        mBinding!!.tvRemoveGst.visibility = View.GONE
                        mBinding!!.etGstNo.setText("")
                        mBinding!!.etBillingAddress.setText("")
                        mBinding!!.etShopName.setText("")
                        mBinding!!.cbBillAddressSame.isChecked = false
                        // etGSTNo.requestFocus();
                        // mBinding.etFullName.setText("");
                        // mBinding.etShopName.setText("");
                    }
                }
            }

            is Response.Error -> {
                pbDProgress!!.visibility = View.GONE
                tvDVerifiedMsg!!.text = ""
                mBinding!!.root.snackbar(it.errorMesssage.toString())
            }
        }
    }

}