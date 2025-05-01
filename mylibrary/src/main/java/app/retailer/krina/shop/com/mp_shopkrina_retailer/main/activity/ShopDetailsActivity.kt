package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GstInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityShopDetailsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.DocTypeAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.GstUpdateCustomerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.picker.DatePickerFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.places.Places
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.regex.Pattern

open class ShopDetailsActivity : AppCompatActivity(), View.OnClickListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener {
    private val REQUESTLOCATION = 49
    private val CAPTURE_IMAGE_CAMERA = 100
    private val GALLERY_REQUST = 2
    private val WRITE_PERMISSION = 0x01

    private lateinit var mBinding: ActivityShopDetailsBinding

    private lateinit var ivGstDialog: ImageView
    private lateinit var isVerifiedText: TextView
    private var popupProgress: ProgressBar? = null
    private lateinit var verifyGSTDialog: Dialog
    private lateinit var pbDProgress: ProgressBar
    private lateinit var tvDVerifiedMsg: TextView
    private lateinit var tvDVerify: Button

    private var commonClassForAPI: CommonClassForAPI? = null
    private var utils: Utils? = null
    private var gstDialog: Dialog? = null
    private var editProfileModel: EditProfileModel? = null
    private var docTypeList: ArrayList<DocTypeModel>? = null
    private var docTypeAdapter: DocTypeAdapter? = null
    private var fGSTName: String? = ""
    private var fLicence: String? = ""
    private var fShop: String? = ""
    private var fGSTupdate = ""

    private var IsVerified = ""
    private var uploadFilePath: String? = null
    private var fileName: String? = ""
    private var lat = ""
    private var lg = ""
    private var custId = 0
    private var isChoose = 0
    private var customerDocTypeId = 0
    private var isGstDialog = false
    private var nameOnGst: String? = ""
    private var zipCode: String? = ""
    private var gstSearch: String? = ""
    private var gstBillingAddress: String? = null
    private var gstCity: String? = ""
    private var gstState: String? = ""
    private var requestGstNo: String? = ""
    private var requestComment: String? = ""
    private var requestLastEnteredGST: String? = ""
    var shopLat = 0.0
    var shopLng = 0.0

    // location
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private var location: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop_details)
        editProfileModel = EditProfileModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.getCustomerDocType(
                    docTypeObserver,
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                    custId
                )
                commonClassForAPI!!.fetchProfileData(
                    getMyProfile,
                    custId,
                    Utils.getDeviceUniqueID(this)
                )
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5
        locationRequest.fastestInterval = 1
        locationRequest.numUpdates = 2
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivShopPhoto -> {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    isChoose = 1
                    if (mGoogleApiClient.isConnected) {
                        if (!IsVerified.equals("Full Verified", ignoreCase = true)) {
                            fShop =
                                custId.toString() + "_" + System.currentTimeMillis() + "ShopImage.jpg"
                            fileName = fShop
                            isChoose = 1
                            requestWritePermission()
                        }
                    } else {
                        mGoogleApiClient.connect()
                    }
                } else {
                    requestLocationPermissions()
                }
            }

            R.id.ivGstPhoto -> {
                if (!IsVerified.equals("Full Verified", ignoreCase = true)) {
                    fGSTName =
                        custId.toString() + "_" + System.currentTimeMillis() + "GST.jpg"
                    fileName = fGSTName
                    isChoose = 2
                    requestWritePermission()
                }
            }

            R.id.iv_license_photo -> if (!IsVerified.equals("Full Verified", ignoreCase = true)) {
                fLicence = custId.toString() + "_" + System.currentTimeMillis() + "Licence.jpg"
                fileName = fLicence
                isChoose = 3
                requestWritePermission()
            }

            R.id.btn_save -> {
                val sShopName = mBinding.etShopName.text.toString().trim { it <= ' ' }
                val sGstNo = mBinding.etGstNo.text.toString().trim { it <= ' ' }
                val sLicenseNo = mBinding.etLicenseNo.text.toString().trim { it <= ' ' }
                var licenseExpiryDate = mBinding.btnExpiryDate.text.toString().trim()
                var panNumber = mBinding.etPanNo.text.toString().trim()
                if (licenseExpiryDate.isNotEmpty())
                    licenseExpiryDate = Utils.getSimpleDateFormat(licenseExpiryDate)

                val hasDoc = sLicenseNo.length + sGstNo.length > 0
                // check GPS
                val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                if (sShopName.isEmpty()) {
                    mBinding.etShopName.error =
                        MyApplication.getInstance().dbHelper.getString(R.string.enter_shop_Name)
                    mBinding.etShopName.requestFocus()
                } else if (TextUtils.isNullOrEmpty(fShop)) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.txt_Choose_Shop_Photo)
                    )
                } else if (mBinding.spDocType.selectedItemPosition == 0) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.select_doc_or_enter_license)
                    )
                } else if (mBinding.spDocType.selectedItemPosition != 0 && !hasDoc) {
                    if (mBinding.liGst.visibility == View.VISIBLE) {
                        mBinding.etGstNo.error =
                            MyApplication.getInstance().dbHelper.getString(R.string.enter_gstin)
                        mBinding.etGstNo.requestFocus()
                    }
                    if (mBinding.liLicense.visibility == View.VISIBLE) {
                        mBinding.etLicenseNo.error =
                            MyApplication.getInstance().dbHelper.getString(R.string.enter_license_number)
                        mBinding.etLicenseNo.requestFocus()
                    }
                } else if (hasDoc && (fGSTName!!.isEmpty() && fLicence!!.isEmpty())) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.upload_document_image)
                    )
                } else if (!TextUtils.isNullOrEmpty(fShop) && !locationManager.isProviderEnabled(
                        LocationManager.GPS_PROVIDER
                    )
                ) {
                    Utils.gpsPermission(this, "clicktime")
                } else {
                    // remove error
                    mBinding.etShopName.error = null
                    mBinding.etLicenseNo.error = null
                    mBinding.etGstNo.error = null
                    // set data
                    editProfileModel!!.shopName = sShopName
                    editProfileModel!!.refNo = sGstNo
                    editProfileModel!!.licenseNumber = sLicenseNo
                    editProfileModel!!.shopimage = fShop
                    editProfileModel!!.uploadRegistration = fLicence
                    editProfileModel!!.uploadGSTPicture = fGSTName
                    editProfileModel!!.nameOnGST = nameOnGst
                    editProfileModel!!.billingAddress = gstBillingAddress
                    editProfileModel!!.billingCity = gstCity
                    editProfileModel!!.billingState = gstState
                    editProfileModel!!.billingZipCode = zipCode
                    editProfileModel!!.customerDocTypeId = customerDocTypeId
                    editProfileModel!!.shopLat = shopLat
                    editProfileModel!!.shopLng = shopLng
                    editProfileModel!!.licenseExpiryDate = licenseExpiryDate
                    editProfileModel!!.panNo = panNumber

                    val gpsTracker = GPSTracker(this)
                    if (gpsTracker != null) {
                        val latitude = gpsTracker.latitude
                        val longitude = gpsTracker.longitude
                        editProfileModel!!.lat = "" + latitude
                        editProfileModel!!.lg = "" + longitude
                    }

                    if (utils!!.isNetworkAvailable) {
                        if (commonClassForAPI != null) {
                            Utils.showProgressDialog(this)
                            commonClassForAPI!!.editProfile(
                                updateProfile,
                                editProfileModel,
                                "Shop Details"
                            )
                        }
                    } else {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                        )
                    }
                }
            }

            R.id.btnExpiryDate -> {
                val datePickerFragment = DatePickerFragment()
                val bundle = Bundle()
                bundle.putString("minMax", "0")
                datePickerFragment.arguments = bundle
                datePickerFragment.setListener { year, month, day ->
                    mBinding.btnExpiryDate.text = "" + day + "/" + (month + 1) + "/" + year
                }
                datePickerFragment.show(supportFragmentManager, "datePicker")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isGstDialog) {
            isGstDialog = false
            gstDialog!!.dismiss()
        } else {
            finish()
        }
        Utils.fadeTransaction(this)
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (isChoose == 2) {
                fileName = if (isGstDialog) {
                    fGSTupdate
                } else {
                    fGSTName
                }
            } else if (isChoose == 3) {
                fileName = fLicence
            } else if (isChoose == 1) {
                fileName = fShop
            }
            if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == RESULT_OK) {
                val selectedImage = Uri.parse(uploadFilePath)
                if (isChoose == 2) {
                    if (isGstDialog) {
                        ivGstDialog.setImageURI(selectedImage)
                        ivGstDialog.setPadding(0, 0, 0, 0)
                    } else {
                        mBinding.ivGstPhoto.setImageURI(selectedImage)
                    }
                } else if (isChoose == 3) {
                    mBinding.ivLicensePhoto.setImageURI(selectedImage)
                } else if (isChoose == 1) {
                    mBinding.ivShopPhoto.setImageURI(selectedImage)
                    var bitmap = BitmapFactory.decodeFile(uploadFilePath)
                    bitmap = addWaterMark(
                        bitmap, SimpleDateFormat(
                            "dd-MM-yyyy kk:mm:ss",
                            Locale.ENGLISH
                        ).format(Date()) + " " + shopLat + "," + shopLng
                    )
                    uploadFilePath = savedImages(bitmap)
                    mBinding.ivShopPhoto.setImageBitmap(bitmap)
                }
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.txt_capture)
                )
                if (utils!!.isNetworkAvailable) {
                    uploadMultipart()
                } else {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
                Log.e("TAG", selectedImage.toString() + "")
            } else if (requestCode == GALLERY_REQUST && resultCode == RESULT_OK && null != data) {
                val selectedImageUri = data.data
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                val cursor = managedQuery(selectedImageUri, projection, null, null, null)
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                cursor.moveToFirst()
                val selectedImagePath = cursor.getString(column_index)
                val bm = BitmapFactory.decodeFile(selectedImagePath)
                uploadFilePath = savedImages(bm)
                if (isChoose == 2) {
                    if (isGstDialog) {
                        ivGstDialog.setImageBitmap(bm)
                        ivGstDialog.setPadding(0, 0, 0, 0)
                    } else {
                        mBinding.ivGstPhoto.setImageBitmap(bm)
                    }
                } else if (isChoose == 3) {
                    mBinding.ivLicensePhoto.setImageBitmap(bm)
                } else if (isChoose == 1) {
                    mBinding.ivShopPhoto.setImageBitmap(bm)
                }
                uploadMultipart()
            } else if (requestCode == REQUESTLOCATION) {
                when (resultCode) {
                    RESULT_OK -> {
                        // All required changes were successfully made
//                        LocationServices.FusedLocationApi.requestLocationUpdates(
//                            mGoogleApiClient,
//                            locationRequest, this
//                        )
//                        if (location != null) {
//                            shopLat = location?.latitude!!
//                            shopLng = location?.longitude!!
//                        } else {
//                            getPreciseLocation()
//                        }
                    }

                    RESULT_CANCELED -> {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.allow_location_access),
                            Toast.LENGTH_LONG
                        ).show()
                        mGoogleApiClient.disconnect()
                    }

                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUESTLOCATION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                requestLocation()
//            }
//        }
//    }


    override fun onConnected(bundle: Bundle?) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        // this is the key ingredient
        val result = LocationServices.SettingsApi
            .checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result1: LocationSettingsResult ->
            val status = result1.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@setResultCallback
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        locationRequest, this
                    )
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                    if (location == null) {
                        getPreciseLocation()
                    } else {
                        shopLat = location!!.latitude
                        shopLng = location!!.longitude
                        if (gstDialog != null) {
                            ivGstDialog.callOnClick()
                        } else {
                            mBinding.ivShopPhoto.callOnClick()
                        }
                    }
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be
                    // fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(this, REQUESTLOCATION)
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onLocationChanged(location: Location) {
        shopLat = location.latitude
        shopLng = location.longitude
    }


    private fun initView() {
        mBinding.toolbarSd.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_shop_details)
        mBinding.TILayout.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_shop_name)
        mBinding.TilGST.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_gst_no)
        mBinding.btnUpdateGst.text =
            MyApplication.getInstance().dbHelper.getString(R.string.request_to_update_gst)
        mBinding.TilLicence.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_license_no)
        mBinding.btnSave.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_Save)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        IsVerified =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_VERIFY)
        docTypeList = ArrayList()

        mBinding.spDocType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (i != 0)
                        mBinding.btnExpiryDate.visibility = View.VISIBLE
                    else
                        mBinding.btnExpiryDate.visibility = View.GONE
                    if (docTypeList!![i]?.docType.equals("GST", ignoreCase = true)) {
                        mBinding.liGst.visibility = View.VISIBLE
                        mBinding.btnUpdateGst.visibility = View.VISIBLE
                        mBinding.liLicense.visibility = View.GONE
                        mBinding.liPanCard.visibility = View.VISIBLE
                    } else {
                        mBinding.liLicense.visibility = View.VISIBLE
                        mBinding.liGst.visibility = View.GONE
                        mBinding.btnUpdateGst.visibility = View.GONE
                        mBinding.liPanCard.visibility = View.GONE
                    }
                    if (docTypeList!!.size > 0) customerDocTypeId = docTypeList!![i]!!.id
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        mBinding.toolbarSd.back.setOnClickListener { onBackPressed() }
        mBinding.etGstNo.setOnClickListener { showGstRequestDialog() }
        mBinding.ivShopPhoto.setOnClickListener(this)
        mBinding.ivGstPhoto.setOnClickListener(this)
        mBinding.ivLicensePhoto.setOnClickListener(this)
        mBinding.btnSave.setOnClickListener(this)
        mBinding.btnExpiryDate.setOnClickListener(this)
        val IsVerified =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_VERIFY)
        if (IsVerified != null && IsVerified.equals("Full Verified", ignoreCase = true)) {
            mBinding.btnSave.visibility = View.INVISIBLE
        }
    }

    private fun setView(editProfileModel: EditProfileModel?) {
        if (editProfileModel != null) {
            if (!TextUtils.isNullOrEmpty(editProfileModel.uploadRegistration) && editProfileModel.uploadRegistration != "") {
                fLicence = editProfileModel.uploadRegistration
                Picasso.get()
                    .load(Constant.BASE_URL_PROFILE + fLicence)
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.ic_photo_camera_orange_24dp)
                    .error(R.drawable.ic_photo_camera_orange_24dp)
                    .into(mBinding.ivLicensePhoto)
            }
            if (!TextUtils.isNullOrEmpty(editProfileModel.shopimage) && editProfileModel.shopimage != "") {
                fShop = editProfileModel.shopimage
                Picasso.get()
                    .load(Constant.BASE_URL_PROFILE + fShop)
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.ic_photo_camera_orange_24dp)
                    .error(R.drawable.ic_photo_camera_orange_24dp)
                    .into(mBinding.ivShopPhoto)
            }
            if (!TextUtils.isNullOrEmpty(editProfileModel.uploadGSTPicture) && editProfileModel.uploadGSTPicture != "") {
                fGSTName = editProfileModel.uploadGSTPicture
                Picasso.get()
                    .load(Constant.BASE_URL_PROFILE + fGSTName)
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.ic_photo_camera_orange_24dp)
                    .error(R.drawable.ic_photo_camera_orange_24dp)
                    .into(mBinding.ivGstPhoto)
            }
            if (!TextUtils.isNullOrEmpty(editProfileModel.panNo) && editProfileModel.panNo != "") {
                mBinding.etPanNo.setText(editProfileModel.panNo)
            }
            mBinding.etShopName.setText(editProfileModel.shopName)
            mBinding.etLicenseNo.setText(editProfileModel.licenseNumber)
            mBinding.etGstNo.setText(editProfileModel.refNo)

            mBinding.btnExpiryDate.text = Utils.getDateFormat(editProfileModel.licenseExpiryDate)

            if (!TextUtils.isNullOrEmpty(editProfileModel.refNo) && editProfileModel.refNo != "NA") {
                mBinding.etGstNo.isFocusable = false
                mBinding.etGstNo.isEnabled = false
                mBinding.etShopName.isEnabled = false
                mBinding.etShopName.isFocusable = false
                mBinding.btnUpdateGst.visibility = View.VISIBLE
                mBinding.btnUpdateGst.setOnClickListener {
                    popupUpdateGST()
                    mGoogleApiClient.connect()
                }
                mBinding.btnExpiryDate.text = editProfileModel.licenseExpiryDate
            } else {
                mBinding.etGstNo.isFocusable = false
                mBinding.etGstNo.isFocusableInTouchMode = false
                mBinding.ivGstPhoto.setOnClickListener(this)
                mBinding.btnUpdateGst.visibility = View.GONE
            }
            Handler(Looper.getMainLooper()).postDelayed({
                for (i in docTypeList!!.indices) {
                    if (docTypeList!![i]?.id == customerDocTypeId) {
                        mBinding.spDocType.setSelection(editProfileModel.customerDocTypeId)
                        break
                    }
                }
            }, 1500)
            val isVerified = SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_VERIFY)
            if (isVerified.equals(
                    "Full Verified",
                    ignoreCase = true
                ) || isVerified.equals("Partial Verified", ignoreCase = true)
            ) {
                mBinding.etShopName.isEnabled = false
                mBinding.etLicenseNo.isEnabled = false
                mBinding.etGstNo.isEnabled = false
                mBinding.spDocType.isEnabled = false
            }
        }
    }

    private fun setValue(myProfileResponse: MyProfileResponse) {
        try {
            if (myProfileResponse.isStatus) {
                val customer = myProfileResponse.customers
                if (customer != null) {
                    lat = customer.lat!!
                    lg = customer.lg!!
                    nameOnGst = customer.nameOnGST
                    customerDocTypeId = customer.customerDocTypeId
                    editProfileModel = EditProfileModel(
                        custId,
                        customer.emailid,
                        customer.mobile,
                        customer.dOB,
                        customer.cityid,
                        customer.city,
                        customer.refNo,
                        customer.shippingAddress,
                        customer.shippingAddress1,
                        customer.shopName,
                        customer.name,
                        customer.uploadProfilePichure,
                        customer.billingAddress,
                        customer.areaName,
                        customer.zipCode,
                        lat,
                        lg,
                        customer.anniversaryDate,
                        customer.whatsappNumber,
                        customer.licenseNumber,
                        customer.uploadLicensePicture,
                        customer.shopimage,
                        customer.uploadGSTPicture,
                        customer.landMark,
                        customer.aadharNo,
                        customer.panNo,
                        nameOnGst,
                        customer.billingAddress,
                        customer.billingCity,
                        customer.billingState,
                        customer.billingZipCode,
                        customerDocTypeId,
                        customer.licenseExpiryDate
                    )
                    setView(editProfileModel)
                }
            } else {
                Utils.setToast(
                    applicationContext, myProfileResponse.message
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun requestWritePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        Permissions.check(
            this,
            permissions,
            null,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    if (isChoose == 1 && shopLat != 0.0) {
                        showImageUploadPhoto()
                    } else {
                        if (isChoose != 1) {
                            showImageUploadPhoto()
                        } else
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().noteRepository.getString(R.string.not_getting_proper_address)
                            )
                    }
                }

                override fun onDenied(
                    context: Context,
                    deniedPermissions: ArrayList<String>
                ) {
                }
            })
    }

    private fun showImageUploadPhoto() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.capture_photo_view)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        val tvAddPhotoHead = dialog.findViewById<TextView>(R.id.tvAddPhotoHead)
        val takePhoto = dialog.findViewById<TextView>(R.id.take_photo)
        val gallery = dialog.findViewById<TextView>(R.id.gallery)
        val cancel = dialog.findViewById<TextView>(R.id.liCancel)
        tvAddPhotoHead!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.addphoto)
        takePhoto!!.text = MyApplication.getInstance().dbHelper.getString(R.string.takephoto)
        gallery!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Choose_from_Library)
        if (isChoose == 1) {
            gallery.visibility = View.GONE
            tvAddPhotoHead.text =
                MyApplication.getInstance().dbHelper.getString(R.string.please_take_shop_image)
        }
        takePhoto.setOnClickListener {
            dialog.dismiss()
            // callForImage()
            pickFromCamera()

        }
        gallery.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, GALLERY_REQUST)
            dialog.dismiss()
        }
        cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun searchGSTInfo(gstSearch: String?) {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.getGstCustInfo(callGstDes, gstSearch, "Update customer")
            }
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            photoFile
        )
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(pictureIntent, CAPTURE_IMAGE_CAMERA)
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var file = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
        if (!file.exists()) {
            file.mkdirs()
        }
        file = File(storageDir, fileName)
        uploadFilePath = file.absolutePath
        return file
    }

    private fun addWaterMark(bm: Bitmap, text: String): Bitmap {
        val mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = (bm.width * 4 / 100).toFloat()
        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)
        val canvas = Canvas(mutableBitmap)
        // If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= canvas.width - 4) //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.textSize = 10f //Scaling needs to be used for different dpi's

        // Calculate the positions
        val xPos = canvas.width - text.length
        val yPos = canvas.height - 10
        canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), paint)
        return mutableBitmap
    }

    // upload photo
    private fun uploadMultipart() {
        val fileToUpload = File(uploadFilePath!!)
        lifecycleScope.launch {
            try {
                val compressedFile = Compressor.compress(applicationContext, fileToUpload) {
                    quality(90)
                    format(Bitmap.CompressFormat.JPEG)
                }
                uploadImagePath(compressedFile)
            } catch (e: Exception) {
                e.printStackTrace()
                showError(e.message)
            }
        }
    }

    private fun savedImages(bm: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
        val file = File(root + File.separator + Environment.DIRECTORY_PICTURES, fileName)
        if (file.exists()) file.delete()
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                val resolver = this.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver.openOutputStream(imageUri!!)!!
                val saved = bm.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            root + File.separator + directory + File.separator + fileName
        } else {
            val myDir =
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES)
            myDir.mkdirs()
            val file = File(myDir, fileName)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath =
                    root + File.separator + Environment.DIRECTORY_PICTURES + File.separator + fileName
            } catch (e: Exception) {
                e.printStackTrace()
            }
            root + File.separator + Environment.DIRECTORY_PICTURES + File.separator + fileName
        }
    }

    private fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        //
        Utils.showProgressDialog(this)
        commonClassForAPI!!.uploadImage(imageObserver, body)
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showGstRequestDialog() {
        verifyGSTDialog = Dialog(this)
        verifyGSTDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        verifyGSTDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        verifyGSTDialog.setContentView(R.layout.dialog_gst_verify)
        verifyGSTDialog.setCancelable(false)
        verifyGSTDialog.setCanceledOnTouchOutside(false)
        val tvTitle = verifyGSTDialog.findViewById<TextView>(R.id.tvTitle)
        val etDialogGstNumber = verifyGSTDialog.findViewById<EditText>(R.id.etDialogGstNumber)
        val tvSkip = verifyGSTDialog.findViewById<Button>(R.id.btnSkip)
        tvDVerify = verifyGSTDialog.findViewById(R.id.btnVerify)
        tvDVerifiedMsg = verifyGSTDialog.findViewById(R.id.tvDVerifiedMsg)
        pbDProgress = verifyGSTDialog.findViewById(R.id.pbDProgress)
        tvSkip.setOnClickListener { verifyGSTDialog.dismiss() }
        etDialogGstNumber.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                gstSearch = etDialogGstNumber.text.toString().trim { it <= ' ' }
                if (gstSearch!!.length >= 15) {
                    MyApplication.getInstance().updateAnalytics("verify_gst_click")
                    pbDProgress.visibility = View.VISIBLE
                    tvDVerifiedMsg.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.verifying_gst)
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
        tvDVerify.setOnClickListener {
            gstSearch = etDialogGstNumber.text.toString().trim { it <= ' ' }
            if (gstSearch!!.length >= 15) {
                MyApplication.getInstance().updateAnalytics("verify_gst_click")
                pbDProgress.visibility = View.VISIBLE
                tvDVerifiedMsg.text =
                    MyApplication.getInstance().dbHelper.getString(R.string.verifying_gst)
                tvDVerify.isEnabled = false
                searchGSTInfo(gstSearch)
            } else {
                Utils.setToast(applicationContext, "Enter Valid GST Number.")
            }
        }
        verifyGSTDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        verifyGSTDialog.show()
    }

    // GST popup
    private fun popupUpdateGST() {
        isGstDialog = true
        var verifyId = 0
        val mView = layoutInflater.inflate(R.layout.popup_update_gst, null)
        gstDialog = Dialog(this, R.style.CustomDialog)
        gstDialog!!.setContentView(mView)
        val popupEtGstNo = mView.findViewById<EditText>(R.id.popup_et_gst_no)
        val etPanNumber = mView.findViewById<EditText>(R.id.etPanNo)
        ivGstDialog = mView.findViewById(R.id.popup_iv_gst_photo)
        val etComment = mView.findViewById<TextInputEditText>(R.id.popup_et_comment)
        val submitReq = mView.findViewById<Button>(R.id.popup_submit_request_btn)
        popupProgress = mView.findViewById(R.id.popup_progress_bar_gst)
        isVerifiedText = mView.findViewById(R.id.tv_isverified)
        val cancelRequest = mView.findViewById<Button>(R.id.popup_cancel_request_btn)
        val tvRequestGSTHead = mView.findViewById<TextView>(R.id.tvRequestGSTHead)
        val TilGstNo = mView.findViewById<TextInputLayout>(R.id.TilGstNo)
        val tilComment = mView.findViewById<TextInputLayout>(R.id.tilComment)

        TilGstNo.hint = MyApplication.getInstance().dbHelper.getString(R.string.txt_gst_no)
        tilComment.hint = MyApplication.getInstance().dbHelper.getString(R.string.comment)
        cancelRequest.text =
            MyApplication.getInstance().dbHelper.getString(R.string.cancel_request_gst)
        submitReq.text = MyApplication.getInstance().dbHelper.getString(R.string.submit_request)
        tvRequestGSTHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.request_gst_update)
        cancelRequest.setOnClickListener { onBackPressed() }
        ivGstDialog.setOnClickListener {
            fGSTupdate =
                custId.toString() + "_gstUpdate_" + System.currentTimeMillis() + "GST.jpg"
            fileName = fGSTupdate
            isChoose = 2
            requestWritePermission()
        }
        popupEtGstNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val etGst = editable.toString().trim { it <= ' ' }
                if (etGst.length >= 15) {
                    if (requestLastEnteredGST != etGst) {
                        popupProgress?.visibility = View.VISIBLE
                        isVerifiedText.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.verifying_gst)
                        if (utils!!.isNetworkAvailable) {
                            if (commonClassForAPI != null) {
                                commonClassForAPI!!.getGstCustInfo(
                                    object : DisposableObserver<GstInfoResponse>() {
                                        override fun onNext(model: GstInfoResponse) {
                                            if (popupProgress != null) {
                                                popupProgress?.visibility = View.GONE
                                            }
                                            if (model.status) {
                                                if (model.custverify != null) {
                                                    etPanNumber.setText(getPANNumber(etGst))
                                                    requestLastEnteredGST = requestGstNo
                                                    isVerifiedText.text = model.message
                                                    verifyId = model.custverify?.id!!
                                                }
                                            } else {
                                                requestLastEnteredGST = requestGstNo
                                                isVerifiedText.text = model.message
                                                isVerifiedText.setTextColor(Color.RED)
                                            }
                                        }

                                        override fun onError(e: Throwable) {
                                            e.printStackTrace()
                                            if (popupProgress != null) {
                                                popupProgress?.visibility = View.GONE
                                            }
                                        }

                                        override fun onComplete() {

                                        }
                                    },
                                    etGst,
                                    "Update customer"
                                )
                            }
                        } else {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                            )
                        }
                    } else {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().noteRepository.getString(R.string.retry_with_different_gst)
                        )
                    }
                } else {
                    isVerifiedText.text =
                        MyApplication.getInstance().dbHelper.getString(R.string.gst_length)
                    isVerifiedText.setTextColor(Color.RED)
                }
            }
        })
        submitReq.setOnClickListener {
            requestGstNo = popupEtGstNo.text.toString()
            requestComment = etComment.text.toString()
            val sPanNumber = etPanNumber.text.toString()
            if (utils!!.isNetworkAvailable) {
                Utils.showProgressDialog(this@ShopDetailsActivity)
                commonClassForAPI!!.requestGstUpdate(
                    requestGstUpdateDis,
                    GstUpdateCustomerModel(
                        verifyId, requestComment,
                        fGSTupdate, "pending", requestGstNo, custId, sPanNumber
                    )
                )
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                )
            }
        }
        gstDialog!!.setOnDismissListener {
            if (mGoogleApiClient.isConnected)
                mGoogleApiClient.disconnect()
        }
        gstDialog!!.show()
    }


    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 99
        )
    }

    private fun requestLocation() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) // this is the key ingredient
        val result = LocationServices.SettingsApi
            .checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result1: LocationSettingsResult ->
            val status = result1.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@setResultCallback
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        locationRequest, this@ShopDetailsActivity
                    )
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                    getPreciseLocation()
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(this, REQUESTLOCATION)
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    private fun getPreciseLocation() {
        val timer = Timer()
        val timerTask1: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (location != null) {
                        timer.cancel()
                        shopLat = location!!.latitude
                        shopLng = location!!.longitude
                        mBinding.ivShopPhoto.callOnClick()
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask1, 500, 5000)
    }


    // get Customer Doc Type
    var docTypeObserver: DisposableObserver<ArrayList<DocTypeModel>> =
        object : DisposableObserver<ArrayList<DocTypeModel>>() {
            override fun onNext(list: ArrayList<DocTypeModel>) {
                docTypeList = list
                docTypeList!!.add(0, DocTypeModel(0, "Select Document Type"))
                docTypeAdapter = DocTypeAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    docTypeList!!
                )
                mBinding.spDocType.adapter = docTypeAdapter
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {}
        }

    // gst info c
    var callGstDes: DisposableObserver<GstInfoResponse> =
        object : DisposableObserver<GstInfoResponse>() {
            override fun onNext(model: GstInfoResponse) {
                tvDVerify.isEnabled = true
                if (popupProgress != null) {
                    popupProgress?.visibility = View.GONE
                }
                if (model.status) {
                    if (model.custverify != null) {
                        mBinding.etPanNo.setText(getPANNumber(gstSearch!!))

                        if (verifyGSTDialog != null) {
                            verifyGSTDialog.dismiss()
                        }
                        if (pbDProgress != null && tvDVerifiedMsg != null) {
                            pbDProgress.visibility = View.GONE
                            tvDVerifiedMsg.text = model.message
                        }
                        if (isGstDialog) {
                            isVerifiedText.text = model.message
                            isVerifiedText.setTextColor(resources.getColor(R.color.green_50))
                        } else {
                            mBinding.etGstNo.setText(gstSearch)
                        }
                        nameOnGst = model.custverify?.name
                        zipCode = model.custverify?.zipcode
                        gstBillingAddress = model.custverify?.shippingAddress
                        gstCity = model.custverify?.city
                        gstState = model.custverify?.state
                        if (!TextUtils.isNullOrEmpty(model.custverify?.shopname)) {
                            mBinding.etShopName.setText(model.custverify?.shopname)
                        }
                        mBinding.etShopName.isEnabled = false
                    }
                } else {
                    if (isGstDialog) {
                        isVerifiedText.text = model.message
                        isVerifiedText.setTextColor(Color.RED)
                    } else {
                        if (pbDProgress != null && tvDVerifiedMsg != null) {
                            pbDProgress.visibility = View.GONE
                            tvDVerifiedMsg.text = model.message
                        }
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                tvDVerify.isEnabled = true
                if (popupProgress != null) {
                    popupProgress?.visibility = View.GONE
                }
                if (pbDProgress != null && tvDVerifiedMsg != null) {
                    pbDProgress.visibility = View.GONE
                    tvDVerifiedMsg.text = ""
                }
            }

            override fun onComplete() {

            }
        }

    var getMyProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(response: MyProfileResponse) {
                Utils.hideProgressDialog()
                setValue(response)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    // Request GST update response
    var requestGstUpdateDis: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(o: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    val message = o["Message"].asString
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    if (o["CheckApproval"].asBoolean || !o["CheckStatus"].asBoolean) {
                        onBackPressed()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    // update Profile
    var updateProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(model: MyProfileResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (model.isStatus) {
                        if (model.customers != null) {
                            val customer = model.customers
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CUSTOMER_NAME, customer?.name)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.CUSTOMER_EMAIL, customer?.emailid)
                            SharePrefs.getInstance(applicationContext)
                                .putString(SharePrefs.SHOP_NAME, customer?.shopName)
                            SharePrefs.getInstance(applicationContext).putString(
                                    SharePrefs.SHIPPING_ADDRESS,
                                    customer?.shippingAddress
                                )
                            SharePrefs.getInstance(applicationContext).putString(
                                SharePrefs.USER_PROFILE_IMAGE,
                                customer?.uploadProfilePichure
                            )
                            SharePrefs.getInstance(applicationContext).putString(
                                SharePrefs.USER_PROFILE_IMAGE,
                                customer?.uploadProfilePichure
                            )

                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.toast_succesfull)
                            )
                            if (TextUtils.isNullOrEmpty(
                                    SharePrefs.getInstance(
                                        applicationContext
                                    ).getString(SharePrefs.CLUSTER_ID)
                                )
                            ) {
                                val i = Intent(applicationContext, SplashScreenActivity::class.java)
                                startActivity(i)
                            } else {
                                val i = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(i)
                            }
                            finish()
                        } else {
                            Utils.setToast(
                                applicationContext, model.message
                            )
                        }
                    } else {
                        Utils.setToast(
                            applicationContext, model.message
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }


    // upload image
    var imageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(result: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (result != null) {
                        Log.e("TAG", "Success $result")
                        if (isChoose == 2) {
                            if (isGstDialog) {
                                fGSTupdate = result.name!!
                            } else {
                                fGSTName = result.name
                            }
                        } else if (isChoose == 3) {
                            fLicence = result.name
                        } else if (isChoose == 1) {
                            fShop = result.name
                        }
                    } else {
                        Log.e("TAG", "Failed $result")
                        Toast.makeText(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.image_not_uploaded),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    open fun getPANNumber(str: String): String? {
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
}