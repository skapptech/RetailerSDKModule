package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityEpayLaterBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EpayLaterDetail
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GpsUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
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
import java.util.Calendar

class EpayLaterFormActivity : AppCompatActivity(), View.OnClickListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener {
    private val WRITE_PERMISSION = 111
    private val GALLERY_REQUST = 99
    private val CAPTURE_IMAGE_CAMERA = 100

    private lateinit var mBinding: ActivityEpayLaterBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    protected var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var location: Location? = null
    private var skCode: String? = null
    private var uploadFilePath = ""
    private var fileName: String? = ""
    private var licenseImagePath = ""
    private var GSTImagePath = ""
    private var FSSAIImagePath = ""
    private var GovtRegNoImagePath = ""
    private var lat: String? = ""
    private var lng: String? = ""
    private var fLicence: String? = null
    private var fGSTName: String? = null
    private var fFSSAIName: String? = null
    private var fRegNoName: String? = null
    private var custId = 0
    private var isChoose = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_epay_later)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        buildGoogleApiClient()
        initializeViews()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Utils.showProgressDialog(this)
        commonClassForAPI!!.getEPayaterCustInfo(getFormObserver, custId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.et_dob -> clickDOB()
            R.id.btn_partner1 -> {
                startActivityForResult(Intent(baseContext, EPayPartnerActivity::class.java), 1)
                Utils.leftTransaction(this)
            }
            R.id.btn_partner2 -> {
                startActivityForResult(Intent(baseContext, EPayPartnerActivity::class.java), 2)
                Utils.leftTransaction(this)
            }
            R.id.btn_partner3 -> {
                startActivityForResult(Intent(baseContext, EPayPartnerActivity::class.java), 3)
                Utils.leftTransaction(this)
            }
            R.id.btn_partner4 -> {
                startActivityForResult(Intent(baseContext, EPayPartnerActivity::class.java), 4)
                Utils.leftTransaction(this)
            }
            R.id.li_licence -> {
                fLicence = custId.toString() + "_" + System.currentTimeMillis() + "Licence.jpg"
                fileName = fLicence
                isChoose = 1
                requestWritePermission()
            }
            R.id.li_gst -> {
                fGSTName = custId.toString() + "_" + System.currentTimeMillis() + "GST.jpg"
                fileName = fGSTName
                isChoose = 2
                requestWritePermission()
            }
            R.id.li_fssai -> {
                fFSSAIName = custId.toString() + "_" + System.currentTimeMillis() + "FSSAI.jpg"
                fileName = fFSSAIName
                isChoose = 3
                requestWritePermission()
            }
            R.id.li_reg_no -> {
                fRegNoName = custId.toString() + "_" + System.currentTimeMillis() + "GovRegNo.jpg"
                fileName = fRegNoName
                isChoose = 4
                requestWritePermission()
            }
            R.id.btn_submit -> checkFormData()
            else -> {}
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.rightTransaction(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isChoose == 1) {
            fileName = fLicence
        } else if (isChoose == 2) {
            fileName = fGSTName
        } else if (isChoose == 3) {
            fileName = fFSSAIName
        } else if (isChoose == 4) {
            fileName = fRegNoName
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mBinding.btnPartner1.setBackgroundColor(Color.GREEN)
            mBinding.btnPartner1.isEnabled = false
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            mBinding.btnPartner2.setBackgroundColor(Color.GREEN)
            mBinding.btnPartner2.isEnabled = false
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            mBinding.btnPartner4.setBackgroundColor(Color.GREEN)
            mBinding.btnPartner3.isEnabled = false
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            mBinding.btnPartner4.setBackgroundColor(Color.GREEN)
            mBinding.btnPartner4.isEnabled = false
        } else if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            val selectedImage: Uri
            selectedImage = Uri.parse(uploadFilePath)
            if (isChoose == 1) {
                mBinding.ivLicence.setImageURI(selectedImage)
            } else if (isChoose == 2) {
                mBinding.ivGst.setImageURI(selectedImage)
            } else if (isChoose == 3) {
                mBinding.ivFssai.setImageURI(selectedImage)
            } else if (isChoose == 4) {
                mBinding.ivRegNo.setImageURI(selectedImage)
            }
            Utils.setToast(
                this,
                MyApplication.getInstance().dbHelper.getString(R.string.txt_capture)
            )
            if (utils!!.isNetworkAvailable) {
                println("UploadMultipart: $fileName")
                uploadMultipart()
            } else {
                Utils.setToast(
                    this,
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
            val bm: Bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(selectedImagePath, options)
            val REQUIRED_SIZE = 200
            var scale = 1
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2
            options.inSampleSize = scale
            options.inJustDecodeBounds = false
            bm = BitmapFactory.decodeFile(selectedImagePath, options)
            uploadFilePath = SavedImages(bm)
            if (isChoose == 1) {
                mBinding.ivLicence.setImageBitmap(bm)
            } else if (isChoose == 2) {
                mBinding.ivGst.setImageBitmap(bm)
            } else if (isChoose == 3) {
                mBinding.ivFssai.setImageBitmap(bm)
            } else if (isChoose == 4) {
                mBinding.ivRegNo.setImageBitmap(bm)
            }
            val isProfileImage = true
            if (isProfileImage) {
                println("UploadMultipart: $fileName")
                uploadMultipart()
            }
        } else if (requestCode == 55) {
            lat = data!!.getStringExtra("lat")
            lng = data.getStringExtra("lag")
            utils = Utils(this)
        } else if (requestCode == 1001 && resultCode == RESULT_OK) {
            buildGoogleApiClient()
        }
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.fastestInterval = 10
        mLocationRequest!!.interval = 100
        mLocationRequest!!.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient!!,
                mLocationRequest!!,
                this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onLocationChanged(location: Location) {
        this.location = location
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    // Code from StudyCloud
    fun SavedImages(bm: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
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
            Log.d(
                Constants.TAG,
                "SavedImages:" + root + File.separator + directory + File.separator + fileName
            )
            root + File.separator + directory + File.separator + fileName
        } else {
            val myDir = File(Environment.getExternalStorageDirectory().absolutePath + "/ShopKirana")
            myDir.mkdirs()
            val file = File(myDir, fileName)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath = "$root/ShopKirana/$fileName"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            "$root/ShopKirana/$fileName"
        }
    }

    private fun initializeViews() {
        utils = Utils(this)
        GpsUtils(this).turnGPSOn { isGPSEnable: Boolean -> }
        mBinding.tilEnterSK.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.sk_code)
        mBinding.shopName.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_shop_name)
        mBinding.proprietorFN.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.proprietor_first_name)
        mBinding.proprietorLN.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.proprietor_last_name)
        mBinding.mobNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.mobile_number)
        mBinding.whatsAppNo.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_cust_whatsApp_number)
        mBinding.email.hint = MyApplication.getInstance().dbHelper.getString(R.string.email)
        mBinding.dob.hint = MyApplication.getInstance().dbHelper.getString(R.string.dob)
        mBinding.panNo.hint = MyApplication.getInstance().dbHelper.getString(R.string.pannumber)
        mBinding.country.hint = MyApplication.getInstance().dbHelper.getString(R.string.country)
        mBinding.state.hint = MyApplication.getInstance().dbHelper.getString(R.string.state)
        mBinding.city.hint = MyApplication.getInstance().dbHelper.getString(R.string.city)
        mBinding.zipCode.hint = MyApplication.getInstance().dbHelper.getString(R.string.pincode)
        mBinding.tvLicence.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_License)
        mBinding.tvUploadGST.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.upload_gst)
        mBinding.tvFSSAI.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.choose_fssai_image)
        mBinding.tvGovApp.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.choose_govt_approved_reg_no)
        mBinding.btnPartner1.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.partner_1)
        mBinding.btnPartner2.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.partner_2)
        mBinding.btnPartner3.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.partner_3)
        mBinding.btnPartner4.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.partner_4)
        mBinding.btnSubmit.hint = MyApplication.getInstance().dbHelper.getString(R.string.submit)
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        skCode = SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE)
        mBinding.etSkCode.setText(SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE))
        mBinding.etproprietorFN.setText(
            SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_NAME)
        )
        mBinding.etproprietorLN.setText(
            SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_NAME)
        )
        mBinding.etShopName.setText(SharePrefs.getInstance(this).getString(SharePrefs.SHOP_NAME))
        mBinding.etMobNo.setText(SharePrefs.getInstance(this).getString(SharePrefs.MOBILE_NUMBER))
        mBinding.etEmail.setText(
            SharePrefs.getInstance(this).getString(SharePrefs.CUSTOMER_EMAIL)
        )
        mBinding.etcountry.setText("India")
        mBinding.etcity.setText(SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME))
        mBinding.spFirmType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    mBinding.singleMode.visibility = View.GONE
                    mBinding.partnerMode.visibility = View.GONE
                } else if (position == 2) {
                    mBinding.singleMode.visibility = View.GONE
                    mBinding.partnerMode.visibility = View.VISIBLE
                } else {
                    mBinding.singleMode.visibility = View.VISIBLE
                    mBinding.partnerMode.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        mBinding.liLicence.setOnClickListener(this)
        mBinding.liGst.setOnClickListener(this)
        mBinding.liFssai.setOnClickListener(this)
        mBinding.liRegNo.setOnClickListener(this)
        mBinding.etDob.setOnClickListener(this)
        mBinding.btnPartner1.setOnClickListener(this)
        mBinding.btnPartner2.setOnClickListener(this)
        mBinding.btnPartner3.setOnClickListener(this)
        mBinding.btnPartner4.setOnClickListener(this)
        mBinding.btnSubmit.setOnClickListener(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
    }

    private fun requestWritePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_PERMISSION
            )
        } else {
            capturePhoto()
            //UploadImage();
        }
    }

    private fun capturePhoto() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.capture_photo_view)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        val takePhoto = dialog.findViewById<TextView>(R.id.take_photo)
        val gallery = dialog.findViewById<TextView>(R.id.gallery)
        val Cancel = dialog.findViewById<TextView>(R.id.liCancel)
        val tvAddPhotoHead = dialog.findViewById<TextView>(R.id.tvAddPhotoHead)
        tvAddPhotoHead!!.text = MyApplication.getInstance().dbHelper.getString(R.string.addphoto)
        takePhoto!!.text = MyApplication.getInstance().dbHelper.getString(R.string.takephoto)
        gallery!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Choose_from_Library)
        takePhoto.setOnClickListener { v: View? ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
            } else {
                callForImage(CAPTURE_IMAGE_CAMERA)
            }
            dialog.dismiss()
        }
        gallery.setOnClickListener { v: View? ->
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, GALLERY_REQUST)
            dialog.dismiss()
        }
        Cancel!!.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    fun callForImage(imageResource: Int) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (MarshmallowPermissions.checkPermissionCamera(this)) {
                    if (MarshmallowPermissions.checkPermissionWriteExternalStorage(this)) {
                        if (imageResource == CAPTURE_IMAGE_CAMERA) {
                            pickFromCamera()
                        }
                    } else {
                        MarshmallowPermissions.requestPermissionWriteExternalStorage(
                            this,
                            MarshmallowPermissions.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                        )
                    }
                } else {
                    MarshmallowPermissions.requestPermissionCameraAndWriteExternalStorage(
                        this,
                        MarshmallowPermissions.PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE
                    )
                }
            } else {
                if (imageResource == CAPTURE_IMAGE_CAMERA) {
                    pickFromCamera()
                }
            }
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
    }

    fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (pictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File
            photoFile = createImageFile()
            val photoUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile
            )
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        }
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

    private fun checkFormData() {
        mBinding.shopName.error = null
        mBinding.proprietorFN.error = null
        mBinding.proprietorLN.error = null
        mBinding.mobNo.error = null
        mBinding.whatsAppNo.error = null
        mBinding.email.error = null
        mBinding.dob.error = null
        mBinding.panNo.error = null
        mBinding.country.error = null
        mBinding.state.error = null
        mBinding.city.error = null
        mBinding.zipCode.error = null
        val shopName = mBinding.etShopName.text.toString()
        val proprietorFN = mBinding.etproprietorFN.text.toString()
        val proprietorLN = mBinding.etproprietorLN.text.toString()
        val mobNo = mBinding.etMobNo.text.toString()
        val whatsAppNo = mBinding.etWhatsAppNo.text.toString()
        val email = mBinding.etEmail.text.toString()
        val dob = mBinding.etDob.text.toString()
        val panNo = mBinding.etpanNo.text.toString()
        val country = mBinding.etcountry.text.toString()
        val state = mBinding.etstate.text.toString()
        val city = mBinding.etcity.text.toString()
        val zipCode = mBinding.etzipCode.text.toString()
        if (TextUtils.isNullOrEmpty(shopName)) {
            mBinding.shopName.error = "Enter Shop Name"
            mBinding.shopName.requestFocus()
        } else if (TextUtils.isNullOrEmpty(proprietorFN)) {
            mBinding.proprietorFN.error = "Enter proprietor First Name"
            mBinding.proprietorFN.requestFocus()
        } else if (TextUtils.isNullOrEmpty(proprietorLN)) {
            mBinding.proprietorLN.error = "Enter proprietor Last Name"
            mBinding.proprietorLN.requestFocus()
        } else if (TextUtils.isNullOrEmpty(mobNo)) {
            mBinding.mobNo.error = "Enter Mobile"
            mBinding.mobNo.requestFocus()
        } else if (!TextUtils.isValidMobileNo(mobNo)) {
            mBinding.mobNo.error = "Enter Valid Mobile Number"
            mBinding.mobNo.requestFocus()
        } else if (TextUtils.isNullOrEmpty(whatsAppNo)) {
            mBinding.whatsAppNo.error = "Enter WhatsApp Number"
            mBinding.whatsAppNo.requestFocus()
        } else if (!TextUtils.isValidMobileNo(whatsAppNo)) {
            mBinding.whatsAppNo.error = "Enter Valid WhatsApp Number"
            mBinding.whatsAppNo.requestFocus()
        } else if (TextUtils.isNullOrEmpty(email)) {
            mBinding.email.error = "Enter Email"
            mBinding.email.requestFocus()
        } else if (!TextUtils.isValidEmail(email)) {
            mBinding.mobNo.error = "Enter Valid Email Address"
            mBinding.mobNo.requestFocus()
        } else if (TextUtils.isNullOrEmpty(dob)) {
            mBinding.dob.error = "Enter DOB"
            mBinding.dob.requestFocus()
        } else if (TextUtils.isNullOrEmpty(panNo)) {
            mBinding.panNo.error = "Enter PAN Number"
            mBinding.panNo.requestFocus()
        } else if (TextUtils.isNullOrEmpty(country)) {
            mBinding.country.error = "Enter Country"
            mBinding.country.requestFocus()
        } else if (TextUtils.isNullOrEmpty(state)) {
            mBinding.state.error = "Enter State"
            mBinding.state.requestFocus()
        } else if (TextUtils.isNullOrEmpty(city)) {
            mBinding.city.error = "Enter City"
            mBinding.city.requestFocus()
        } else if (TextUtils.isNullOrEmpty(zipCode)) {
            mBinding.zipCode.error = "Enter Postel Code"
            mBinding.zipCode.requestFocus()
        } else if (TextUtils.isNullOrEmpty(licenseImagePath) || TextUtils.isNullOrEmpty(
                licenseImagePath
            ) || TextUtils.isNullOrEmpty(licenseImagePath) || TextUtils.isNullOrEmpty(
                licenseImagePath
            )
        ) {
            Utils.setToast(
                this,
                MyApplication.getInstance().dbHelper.getString(R.string.please_upload_at_least_one_document)
            )
        } else {
            if (location != null) {
                lat = location!!.latitude.toString()
                lng = location!!.longitude.toString()
            }
            val detail = EpayLaterDetail(
                SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE),
                shopName,
                mBinding.spFirmType.selectedItem.toString(),
                proprietorFN,
                proprietorLN,
                mBinding.spGender.selectedItem.toString(),
                mobNo,
                whatsAppNo,
                email,
                dob,
                panNo,
                country,
                state,
                city,
                zipCode,
                licenseImagePath,
                GSTImagePath,
                FSSAIImagePath,
                GovtRegNoImagePath,
                lat,
                lng
            )
            Utils.showProgressDialog(this)
            commonClassForAPI!!.addEPayaterCustInfo(disposableObserver, detail)
        }
    }

    private fun clickDOB() {
        try {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForDob(),
                mYear,
                mMonth,
                mDay
            )
            dialog.datePicker.maxDate = System.currentTimeMillis()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                MyApplication.getInstance().dbHelper.getString(R.string.cancel)
            ) { dialog1: DialogInterface?, which: Int -> mBinding.etDob.setText("") }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(model: EpayLaterDetail) {
        mBinding.etSkCode.setText(if (model.skCode == null) skCode else model.skCode)
        mBinding.etproprietorFN.setText(model.proprietorFirstName)
        mBinding.etproprietorLN.setText(model.proprietorLastName)
        mBinding.etShopName.setText(model.shopName)
        mBinding.etMobNo.setText(model.mobile)
        mBinding.etWhatsAppNo.setText(model.whatsAppNumber)
        mBinding.etEmail.setText(model.email)
        if (model.dob != null) {
            mBinding.etDob.setText(Utils.getDateFormat(model.dob))
        }
        mBinding.etpanNo.setText(model.panNo)
        mBinding.etcountry.setText(model.country)
        mBinding.etstate.setText(model.state)
        mBinding.etcity.setText(model.city)
        mBinding.etzipCode.setText(model.postalCode)
        licenseImagePath = model.licenseImagePath
        GSTImagePath = model.gstImagePath
        FSSAIImagePath = model.fssaiImagePath
        GovtRegNoImagePath = model.govtRegNumberImagePath
        if ("ProprietorShip".equals(model.firmType, ignoreCase = true)) {
            mBinding.spFirmType.setSelection(1)
        } else if ("PartnerShip".equals(model.firmType, ignoreCase = true)) {
            mBinding.spFirmType.setSelection(2)
        } else if ("Private Limited/Limited".equals(model.firmType, ignoreCase = true)) {
            mBinding.spFirmType.setSelection(3)
        } else if ("LLP".equals(model.firmType, ignoreCase = true)) {
            mBinding.spFirmType.setSelection(4)
        }
        if (!TextUtils.isNullOrEmpty(model.licenseImagePath) && model.licenseImagePath != "") Glide.with(
            baseContext
        ).load(Constant.BASE_URL_PROFILE + licenseImagePath).into(
            mBinding.ivLicence
        )
        if (!TextUtils.isNullOrEmpty(model.gstImagePath) && model.gstImagePath != "") Glide.with(
            baseContext
        ).load(Constant.BASE_URL_PROFILE + GSTImagePath).into(
            mBinding.ivGst
        )
        if (!TextUtils.isNullOrEmpty(model.fssaiImagePath) && model.fssaiImagePath != "") Glide.with(
            baseContext
        ).load(Constant.BASE_URL_PROFILE + FSSAIImagePath).into(
            mBinding.ivFssai
        )
        if (!TextUtils.isNullOrEmpty(model.govtRegNumberImagePath) && model.govtRegNumberImagePath != "") Glide.with(
            baseContext
        ).load(Constant.BASE_URL_PROFILE + GovtRegNoImagePath).into(
            mBinding.ivRegNo
        )
    }

    internal inner class mDateSetListenerForDob : OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            try {
                val date = StringBuilder()
                    .append(day).append("/").append(month + 1).append("/")
                    .append(year).toString()
                mBinding.etDob.setText(year.toString() + "-" + Utils.getDateDashFormat(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadMultipart() {
        val fileToUpload = File(uploadFilePath)
        lifecycleScope.launch {
            try {
                val compressedFile = Compressor.compress(applicationContext, fileToUpload) {
                    quality(90)
                    format(Bitmap.CompressFormat.JPEG)
                }
                uploadImagePath(compressedFile)
            } catch (e: Exception) {
                e.printStackTrace()
                //Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImagePath(file: File) {
        Utils.showProgressDialog(this)
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(this)
        commonClassForAPI!!.uploadImage(imageObserver, body)
    }

    // upload image
    private val imageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(response: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null) {
                        Log.e("Success", "" + response)
                        if (isChoose == 1) {
                            licenseImagePath = response.name!!
                        } else if (isChoose == 2) {
                            GSTImagePath = response.name!!
                        } else if (isChoose == 3) {
                            FSSAIImagePath = response.name!!
                        } else if (isChoose == 4) {
                            GovtRegNoImagePath = response.name!!
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.unable_to_uploaded_image),
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

    private val getFormObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(`object`: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    if (`object` != null && `object`.size() > 0) {
                        val jsonObject = `object`.getAsJsonObject("customers")
                        val model =
                            Gson().fromJson(jsonObject.toString(), EpayLaterDetail::class.java)
                        setData(model)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
                disposableObserver.dispose()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    private val disposableObserver: DisposableObserver<JsonObject> =
        object : DisposableObserver<JsonObject>() {
            override fun onNext(`object`: JsonObject) {
                Utils.hideProgressDialog()
                try {
                    if (`object`["Status"].asBoolean) {
                        onBackPressed()
                    }
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.data_saved_successfully)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                e.printStackTrace()
                if (this != null) {
                    dispose()
                }
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}