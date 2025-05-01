package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentEditProfileBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.RateAppActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ShopDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerHoliday
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CommonResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerAddressActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.Constants
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
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

class EditProfileHomeFragment : Fragment(), View.OnClickListener {
    private val WRITE_PERMISSION = 0x01
    private val GALLERY_REQUST = 200
    private val CAPTURE_IMAGE_CAMERA = 100
    private val REQUST_FOR_ADDRESS = 1001

    private lateinit var mBinding: FragmentEditProfileBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private lateinit var activity: EditProfileActivity
    private var custId = 0
    private var lat = ""
    private var lg = ""
    private var fProfile: String? = ""
    private var uploadFilePath: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as EditProfileActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().mFirebaseAnalytics.setCurrentScreen(
            activity!!,
            this.javaClass.simpleName,
            null
        )
        mBinding.btnAddress.isClickable = true
        activity!!.tv_title!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.profile)
    }

    override fun onClick(v: View) {
        val fragment: Fragment
        when (v.id) {
            R.id.btn_personal_info -> {
                val isGPS = Utils.gpsPermission(activity, "clicktime")
                if (isGPS) {
                    fragment = ProfileInfoFragment()
                    activity!!.switchContentWithStack(fragment)
                } else {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getData("gps_permission")
                    )
                }
            }

            R.id.btn_shop_detail -> {
                startActivity(Intent(activity, ShopDetailsActivity::class.java))
                Utils.fadeTransaction(activity)
            }

            R.id.btnHoliday -> {
                showShopHolidayDialog()
            }

            R.id.btn_rate_app -> {
                startActivity(Intent(activity, RateAppActivity::class.java))
                Utils.fadeTransaction(activity)
            }

            R.id.iv_edit_profile_plus_icon -> {
                fProfile = custId.toString() + "_" + System.currentTimeMillis() + ".jpg"
                requestWritePermission()
            }

            R.id.btn_address -> {
                val isGPS = Utils.gpsPermission(activity, "clicktime")
                if (isGPS) {
                    val IsVerified =
                        SharePrefs.getInstance(activity).getString(SharePrefs.CUSTOMER_VERIFY)
                    if (!IsVerified.equals("Full Verified", ignoreCase = true)) {
                        mBinding.btnAddress.isClickable = false
                        val intent = Intent(activity, CustomerAddressActivity::class.java)
                        intent.putExtra("REDIRECT_FLAG", 2)
                        intent.putExtra(
                            "cityName",
                            SharePrefs.getInstance(activity).getString(SharePrefs.CITY_NAME)
                        )
                        intent.putExtra("CUSTOMER_DETAILS", activity!!.editProfileModel)
                        startActivityForResult(intent, REQUST_FOR_ADDRESS)
                        Utils.leftTransaction(activity)
                    } else {
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getData("txt_note_verified")
                        )
                    }
                } else {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getData("gps_permission")
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        requestPermission()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
                val selectedImage = Uri.parse(uploadFilePath)
                mBinding.ivCustProfile.setImageURI(selectedImage)
                Utils.setToast(
                    activity,
                    MyApplication.getInstance().dbHelper.getString(R.string.txt_capture)
                )
                if (utils!!.isNetworkAvailable) {
                    uploadMultipart()
                } else {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                    )
                }
            } else if (requestCode == GALLERY_REQUST && resultCode == Activity.RESULT_OK && null != data) {
                val selectedImageUri = data.data
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                val cursor = activity!!.managedQuery(selectedImageUri, projection, null, null, null)
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
                mBinding.ivCustProfile.setImageBitmap(bm)
                uploadMultipart()
            } else if (requestCode == REQUST_FOR_ADDRESS && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val addressModel =
                        data.getParcelableExtra<AddressModel>(Constant.CUSTOMER_ADDRESS)
                    mBinding.tvCustAddress.text = addressModel!!.address
                    if (addressModel.address != null) {
                        activity!!.editProfileModel!!.shippingAddress = addressModel.address
                        activity!!.editProfileModel!!.lat = addressModel.latitude.toString()
                        activity!!.editProfileModel!!.lg = addressModel.longitude.toString()
                        activity!!.editProfileModel!!.zipCode = addressModel.pincode
                        activity!!.editProfileModel!!.shippingAddress1 =
                            addressModel.flateOrFloorNumber
                        activity!!.editProfileModel!!.landMark = addressModel.landmark
                        updateProfileCall()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun initView() {
        commonClassForAPI = CommonClassForAPI.getInstance(activity)
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        mBinding.chnage.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_change_address)
        mBinding.txtPreferences.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_preferences)
        mBinding.btnPersonalInfo.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_personal_informations)
        mBinding.btnShopDetail.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_shop_details)
        mBinding.btnHoliday.text =
            MyApplication.getInstance().dbHelper.getString(R.string.shop_holiday)
        mBinding.btnRateApp.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_rate_the_app)

        utils = Utils(activity)
        mBinding.btnPremium.setOnClickListener(this)
        mBinding.btnPersonalInfo.setOnClickListener(this)
        mBinding.btnShopDetail.setOnClickListener(this)
        mBinding.btnHoliday.setOnClickListener(this)
        mBinding.btnRateApp.setOnClickListener(this)
        mBinding.ivEditProfilePlusIcon.setOnClickListener(this)
        mBinding.btnAddress.setOnClickListener(this)

        if (SharePrefs.getInstance(activity).getString(SharePrefs.CUSTOMER_VERIFY)
                .equals("Full Verified", ignoreCase = true)
        ) {
            mBinding.txtNote.text =
                MyApplication.getInstance().dbHelper.getString(R.string.txt_note_verified)
            mBinding.txtNote.visibility = View.VISIBLE
        }
        if (activity.editProfileModel!!.mobile == null) {
            mBinding.shimmerViewContainer.startShimmer()
            mBinding.shimmerViewContainer.visibility = View.VISIBLE
            mBinding.scrollView.visibility = View.INVISIBLE
            profiledData()
        } else {
            mBinding.tvCustSkcode.text =
                SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE)
            mBinding.tvCustName.text = activity!!.editProfileModel!!.name
            mBinding.tvCustEmail.text = activity!!.editProfileModel!!.emailid
            mBinding.tvCustMobile.text = activity!!.editProfileModel!!.mobile
            mBinding.tvCustAddress.text = activity!!.editProfileModel!!.shippingAddress
        }
        if (!TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(activity).getString(SharePrefs.USER_PROFILE_IMAGE)
            )
        ) {
            Picasso.get()
                .load(
                    Constant.BASE_URL_PROFILE + SharePrefs.getInstance(activity)
                        .getString(SharePrefs.USER_PROFILE_IMAGE)
                )
                .placeholder(R.drawable.profile_round)
                .error(R.drawable.profile_round)
                .into(mBinding.ivCustProfile)
        } else {
            mBinding.ivCustProfile.setImageResource(R.drawable.profile_round)
        }

        val customerType = SharePrefs.getInstance(activity).getString(SharePrefs.CUSTOMER_TYPE)
        if (!TextUtils.isNullOrEmpty(customerType)) {
            mBinding.tvCustomerType.visibility = View.VISIBLE
            mBinding.tvCustomerType.text = customerType
        } else {
            mBinding.tvCustomerType.visibility = View.GONE
        }

    }

    // Requesting permission
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!MarshmallowPermissions.isPermissionAllowed(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                MarshmallowPermissions.requestPermission(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    101
                )
            }
            if (!MarshmallowPermissions.isPermissionAllowed(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                MarshmallowPermissions.requestPermission(
                    activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    101
                )
            }
            if (!MarshmallowPermissions.isPermissionAllowed(
                    activity,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                MarshmallowPermissions.requestPermission(
                    activity,
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                    101
                )
            }
        }
    }

    private fun profiledData() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.fetchProfileData(
                    getMyProfile,
                    custId,
                    Utils.getDeviceUniqueID(activity)
                )
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().noteRepository.getString(R.string.internet_connection)
            )
        }
    }

    private fun requestWritePermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_PERMISSION
            )
        } else {
            capturePhoto()
        }
    }

    // capture popup
    private fun capturePhoto() {
        if (activity != null) {
            val dialog = BottomSheetDialog(activity)
            dialog.setContentView(R.layout.capture_photo_view)
            dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
                .setBackgroundColor(Color.TRANSPARENT)
            val takePhoto = dialog.findViewById<TextView>(R.id.take_photo)
            val gallery = dialog.findViewById<TextView>(R.id.gallery)
            val tvCancel = dialog.findViewById<TextView>(R.id.liCancel)
            takePhoto!!.setOnClickListener {
                dialog.dismiss()
                callForImage()
            }
            gallery!!.setOnClickListener {
                startActivityForResult(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ), GALLERY_REQUST
                )
                dialog.dismiss()
            }
            tvCancel!!.setOnClickListener { v: View? -> dialog.dismiss() }
            dialog.show()
        }
    }

    private fun callForImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (MarshmallowPermissions.checkPermissionCamera(activity)) {
                if (MarshmallowPermissions.checkPermissionWriteExternalStorage(activity)) {
                    pickFromCamera()
                } else {
                    MarshmallowPermissions.requestPermissionWriteExternalStorage(
                        activity,
                        MarshmallowPermissions.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                    )
                }
            } else {
                MarshmallowPermissions.requestPermissionCameraAndWriteExternalStorage(
                    activity,
                    MarshmallowPermissions.PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE
                )
            }
        } else {
            pickFromCamera()
        }
    }

    private fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        photoFile = createImageFile()
        val photoUri = FileProvider.getUriForFile(
            activity,
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
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, fProfile)
        uploadFilePath = file.absolutePath
        return file
    }

    // Code from StudyKloud
    private fun SavedImages(bm: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                val resolver = activity.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fProfile)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver.openOutputStream(imageUri!!)!!
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.d(
                Constants.TAG,
                "SavedImages:" + root + File.separator + directory + File.separator + fProfile
            )
            root + File.separator + directory + File.separator + fProfile
        } else {
            val myDir = File(Environment.getExternalStorageDirectory().absolutePath + "/ShopKirana")
            myDir.mkdirs()
            val file = File(myDir, fProfile)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath = "$root/ShopKirana/$fProfile"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            "$root/ShopKirana/$fProfile"
        }
    }

    // upload photo
    private fun uploadMultipart() {
        val fileToUpload = File(uploadFilePath)
        lifecycleScope.launch {
            try {
                val compressedFile = Compressor.compress(activity, fileToUpload) {
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

    private fun showError(errorMessage: String?) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(activity)
        commonClassForAPI!!.uploadImage(imageObserver, body)
    }

    private fun updateProfileCall() {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(activity)
                commonClassForAPI!!.editProfile(
                    editProfile,
                    activity.editProfileModel,
                    "Profile photo and address update"
                )
            }
        } else {
            Utils.setToast(
                activity,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun setValue(myProfileResponse: MyProfileResponse) {
        try {
            if (myProfileResponse.isStatus) {
                val customer = myProfileResponse.customers
                if (customer != null) {
                    val sCustName = customer.name
                    val sCustEmail = customer.emailid
                    val sShopName = customer.shopName
                    val sShippingAddress = customer.shippingAddress
                    val sShippingAddress1 = customer.shippingAddress1
                    val sBillingAddress = customer.billingAddress
                    val sContactNumber = customer.mobile
                    val sWhatsappNo = customer.whatsappNumber
                    val sAreaPinCode = customer.zipCode
                    val sAreaName = customer.areaName
                    val sGstNumber = customer.refNo
                    val sDateOfBirth = customer.dOB
                    val sAnnDate = customer.anniversaryDate
                    val sSkCode = customer.skcode
                    val sLicenceNo = customer.licenseNumber
                    val landmark = customer.landMark
                    lat = customer.lat!!
                    lg = customer.lg!!
                    fProfile = customer.uploadProfilePichure
                    val fLicence = customer.uploadLicensePicture
                    val fShop = customer.shopimage
                    val fGSTName = customer.uploadGSTPicture
                    // String CustomerVerify = customer.getCustomerVerify();
                    // set values
                    mBinding.tvCustName.text = customer.name
                    mBinding.tvCustEmail.text = customer.emailid
                    mBinding.tvCustMobile.text = sContactNumber
                    mBinding.tvCustAddress.text = sShippingAddress
                    mBinding.tvCustSkcode.text = sSkCode
                    SharePrefs.getInstance(activity)
                        .putBoolean(SharePrefs.IS_PRIME_MEMBER, customer.isPrimeCustomer)
                    if (!TextUtils.isNullOrEmpty(customer.uploadProfilePichure) && customer.uploadProfilePichure != "") {
                        Picasso.get()
                            .load(Constant.BASE_URL_PROFILE + customer.uploadProfilePichure)
                            .resize(150, 150)
                            .centerCrop()
                            .placeholder(R.drawable.profile_round)
                            .error(R.drawable.profile_round)
                            .into(mBinding.ivCustProfile)
                    } else {
                        mBinding.ivCustProfile.setImageResource(R.drawable.profile_round)
                    }
                    activity!!.editProfileModel = EditProfileModel(
                        custId,
                        sCustEmail,
                        sContactNumber,
                        sDateOfBirth,
                        customer.cityid,
                        customer.city,
                        sGstNumber,
                        sShippingAddress,
                        sShippingAddress1,
                        sShopName,
                        sCustName,
                        fProfile,
                        sBillingAddress,
                        sAreaName,
                        sAreaPinCode,
                        lat,
                        lg,
                        sAnnDate,
                        sWhatsappNo,
                        sLicenceNo,
                        fLicence,
                        fShop,
                        fGSTName,
                        landmark,
                        customer.aadharNo,
                        customer.panNo,
                        myProfileResponse.customers?.nameOnGST,
                        customer.billingAddress,
                        customer.billingCity,
                        customer.billingState,
                        customer.billingZipCode,
                        customer.customerDocTypeId,
                        customer.licenseExpiryDate
                    )
                    activity!!.customerModel = customer
                    if (TextUtils.isNullOrEmpty(fLicence) || TextUtils.isNullOrEmpty(sGstNumber)) if (utils!!.getDay(
                            SharePrefs.getInstance(activity).getBoolean(SharePrefs.DOC_EMPTY)
                        )
                    ) {
                        if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.DOC_DAY_EMPTY)) {
                            showProfileDialog()
                        }
                    }
                }
            } else {
                Utils.setToast(activity, myProfileResponse.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun showProfileDialog() {
        val dialog = Dialog(activity!!)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_edit_info)
        dialog.setCancelable(false)
        val tvTitle = dialog.findViewById<TextView>(R.id.pd_title)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfo)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val cancelBtn = dialog.findViewById<TextView>(R.id.cancel_btn)
        val criticalInfoMissMSG =
            MyApplication.getInstance().dbHelper.getString(R.string.licence_update_msg)
        if (criticalInfoMissMSG.startsWith("Your Critical info")) {
            tvTitle.text =
                MyApplication.getInstance().dbHelper.getString(R.string.critical_info_msg)
        } else {
            tvTitle.text = criticalInfoMissMSG
        }
        tvInfo.text =
            MyApplication.getInstance().dbHelper.getString(R.string.please_update_your_information)
        okBtn.setOnClickListener {
            dialog.dismiss()
            if (activity != null && isAdded) startActivity(
                Intent(
                    activity,
                    ShopDetailsActivity::class.java
                )
            )
        }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun showShopHolidayDialog() {
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(R.layout.dialog_shop_holiday)
        dialog.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val tvDesc = dialog.findViewById<TextView>(R.id.tvDesc)
//        val calendarView = dialog.findViewById<CalendarView>(R.id.calender)
        val spDays = dialog.findViewById<Spinner>(R.id.spDays)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)

        tvTitle!!.text = MyApplication.getInstance().dbHelper.getString(R.string.shop_holiday)
        tvDesc!!.text = MyApplication.getInstance().dbHelper.getString(R.string.shop_holiday_des)

//        calendarView?.minDate = System.currentTimeMillis()
//        calendarView?.maxDate = (System.currentTimeMillis() + 24 * 60 * 60 * 1000 * 6)

        btnSubmit?.setOnClickListener {
            dialog.dismiss()
            Utils.showProgressDialog(activity)
            val model = CustomerHoliday()
            model.id = 0
            model.warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
            model.clusterId =
                SharePrefs.getInstance(activity).getString(SharePrefs.CLUSTER_ID).toInt()
            model.skCode = SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE)
            model.holiday = arrayOf(spDays?.selectedItem.toString())
            model.year = 2023

            commonClassForAPI?.updateCustomerHoliday(holidayObserver, model)
        }

        ivClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    // get user details response
    private val getMyProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(response: MyProfileResponse) {
                Utils.hideProgressDialog()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.scrollView.visibility = View.VISIBLE
                setValue(response)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.scrollView.visibility = View.VISIBLE
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.GONE
                mBinding.scrollView.visibility = View.VISIBLE
            }
        }

    // edit profile response
    private val editProfile: DisposableObserver<MyProfileResponse> =
        object : DisposableObserver<MyProfileResponse>() {
            override fun onNext(model: MyProfileResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (model.isStatus) {
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.USER_PROFILE_IMAGE, fProfile)
                        if (model.customers != null) {
                            val customer = model.customers
                            Log.e("resultAMAN", ">>>>" + customer?.uploadProfilePichure)
                            SharePrefs.getInstance(activity)
                                .putString(SharePrefs.CUSTOMER_NAME, customer?.name)
                            SharePrefs.getInstance(activity)
                                .putString(SharePrefs.CUSTOMER_EMAIL, customer?.emailid)
                            SharePrefs.getInstance(activity)
                                .putString(SharePrefs.SHOP_NAME, customer?.shopName)
                            SharePrefs.getInstance(activity)
                                .putString(SharePrefs.SHIPPING_ADDRESS, customer?.shippingAddress)
                            SharePrefs.getInstance(activity).putString(
                                SharePrefs.USER_PROFILE_IMAGE,
                                customer?.uploadProfilePichure
                            )
                        }
                    } else {
                        Utils.setToast(activity, model.message)
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
    private val imageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(response: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response != null) {
                        SharePrefs.getInstance(activity)
                            .putString(SharePrefs.USER_PROFILE_IMAGE, response.name)
                        activity!!.editProfileModel!!.profilePicture = response.name
                        updateProfileCall()
                    } else {
                        Toast.makeText(activity, "Image Not Uploaded", Toast.LENGTH_SHORT).show()
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

    private val holidayObserver: DisposableObserver<CommonResponse> =
        object : DisposableObserver<CommonResponse>() {
            override fun onNext(response: CommonResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (response.status) {
                        Toast.makeText(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.succesfullSubmitted),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(activity, "Error updating data", Toast.LENGTH_SHORT).show()
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
}