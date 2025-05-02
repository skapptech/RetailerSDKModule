package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityUserEditProfileBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomSearchPlaceActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    private val REQUEST_FOR_CITY = 1002

    lateinit var mBinding: ActivityUserEditProfileBinding
    private lateinit var userProfileModel: UserProfileModel
    lateinit var  editProfileViewMode: EditProfileViewMode
    private var mGeocoder: Geocoder? = null

    var fileName = ""
    var uploadFilePath = ""
    var fileFullPath = ""
    var relativePath = ""
    var  stateName = ""

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    val analyticPost = AnalyticPost()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        userProfileModel = intent.getSerializableExtra("UserData") as UserProfileModel
        initView()
    }

    private fun initView() {
        mGeocoder = Geocoder(applicationContext, Locale.ENGLISH)
        val repository = EditProfileRepository(RestClient.getInstance4().service4)
        editProfileViewMode = ViewModelProvider(this, EditProfileFactory(repository))[EditProfileViewMode::class.java]

        mBinding.UserName.text = userProfileModel.Name
        mBinding.TvShopName.text = userProfileModel.ShopName
        mBinding.tvCity.text = userProfileModel.City
        mBinding.tvMobileNumber.text = userProfileModel.Mobile
        mBinding.etUserName.setText(userProfileModel.Name)
        mBinding.etShopName.setText(userProfileModel.ShopName)
        mBinding.etCity.setText(userProfileModel.City)
        mBinding.etMobileNo.setText(userProfileModel.Mobile)
        fileFullPath = userProfileModel.img
        stateName = userProfileModel.State

        if (userProfileModel.img.isNotEmpty()) {
            Picasso.get().load(userProfileModel.img)
                .into(mBinding.profileImage)
        } else {
            mBinding.profileImage.setImageResource(R.drawable.profile)
        }

        mBinding.etCity.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, CustomSearchPlaceActivity::class.java)
                    .putExtra("cityname", "")
                    .putExtra("type", 1)
                    .putExtra("searchCity", true), REQUEST_FOR_CITY
            )
            Utils.leftTransaction(this)
        }

        mBinding.rlProfileUpload.setOnClickListener { v ->
            Utils.hideKeyboard(this@EditProfileActivity, v)
            fileName = "feed_" + System.currentTimeMillis() + "Img.jpg"
            showImageUploadPhoto()
        }

        mBinding.toolbarPost.back.setOnClickListener {
            onBackPressed()
        }


        mBinding.toolbarPost.btnPost.setOnClickListener {
            if (mBinding.etUserName.text.trim().isNullOrEmpty()) {
                Utils.setToast(applicationContext, "कृपया अपना नाम दर्ज करें ")
            } else if (mBinding.etShopName.text.trim().isNullOrEmpty()) {
                Utils.setToast(applicationContext, "कृपया दुकान का नाम दर्ज करें ")
            } else if (mBinding.etCity.text.trim().isNullOrEmpty()) {
                Utils.setToast(applicationContext, "कृपया शहर का नाम दर्ज करें ")
            } else if(fileFullPath.isNullOrEmpty()) {
                Utils.setToast(applicationContext, "कृपया फोटो अपलोड करें ")
            } else{
                val updateUser = UserUpdate(userProfileModel.CustomerId,
                    mBinding.etUserName.text.toString().trim(),
                    mBinding.etShopName.text.toString().trim(),
                    mBinding.etCity.text.toString().trim(),stateName,
                    "",
                    fileFullPath
                )

                editProfileViewMode.getSubmitUser(updateUser)
            }
        }

        editProfileViewMode.updateResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    Utils.showProgressDialog(this)
                }

                is NetworkResult.Failure -> {
                    Utils.hideProgressDialog()
                    // Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    Utils.hideProgressDialog()
                    Toast.makeText(this, "आपकी प्रोफाइल सफलतापूर्वक अपडेट हो गयी है ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext,FeedActivity::class.java))
                    finish()
                }
            }
        }
        editProfileViewMode.uploadPostImg.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    Utils.showProgressDialog(this)
                }

                is NetworkResult.Failure -> {
                    Utils.hideProgressDialog()
                    Utils.setToast(applicationContext, it.errorMessage)

                }

                is NetworkResult.Success -> {
                    Utils.hideProgressDialog()
                    if (it.data != null && !it.data.isJsonNull) {
//                        Utils.setToast(applicationContext, "Image uploaded successfully")
                        fileName = it.data.get("FileName").asString
                        relativePath = it.data.get("RelativePath").asString
                        fileFullPath = it.data.get("FileFullpath").asString
                        Picasso.get().load(SharePrefs.getInstance(RetailerSDKApp.getInstance()).getString(SharePrefs.TRADE_WEB_URL) + fileFullPath)
                            .into(mBinding.profileImage)
                    } else {
                        Utils.setToast(applicationContext, "error")
                    }
                }
            }
        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        uploadMultipart()
                    }
                }
            }
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val selectedFileUri = result.data?.data
                        uploadFilePath = DateUtilskotlin.getPath(
                            this@EditProfileActivity,
                            selectedFileUri
                        )!!
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        uploadMultipart()
                    }
                }
            }

        //analysis
        RetailerSDKApp.getInstance().mixpanel.timeEvent("editProfileExit")
        RetailerSDKApp.getInstance().updateAnalytics("editProfileView")
    }

    private fun showImageUploadPhoto() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.capture_photo_view)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        val tvAddPhotoHead = dialog.findViewById<TextView>(R.id.tvAddPhotoHead)
        val takePhoto = dialog.findViewById<TextView>(R.id.take_photo)
        val gallery = dialog.findViewById<TextView>(R.id.gallery)
        val tvVideo = dialog.findViewById<TextView>(R.id.tvVideo)
        val cancel = dialog.findViewById<TextView>(R.id.liCancel)
        tvVideo?.visibility = View.GONE
        tvAddPhotoHead!!.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.addphoto)
        takePhoto!!.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.takephoto)
        gallery!!.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_Choose_from_Library)
        takePhoto.setOnClickListener {
            try {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA,
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
                            pickFromCamera()
                            dialog.dismiss()
                        }

                        override fun onDenied(
                            context: Context,
                            deniedPermissions: ArrayList<String>
                        ) {
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        gallery.setOnClickListener {
            try {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA,
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
                            dialog.dismiss()
                            chooseGallery()
                        }

                        override fun onDenied(
                            context: Context,
                            deniedPermissions: ArrayList<String>
                        ) {
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun chooseGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun pickFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        try {
            photoFile = createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri = FileProvider.getUriForFile(
            (this@EditProfileActivity),
            this.packageName + ".provider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
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
                Utils.setToast(applicationContext, e.message)
            }
        }
    }

    private fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        editProfileViewMode.uploadPostImg(body)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FOR_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                val place = data.getParcelableExtra<Place>("PlaceResult")
                val latLng = place!!.latLng
                getPlaceInfo(latLng.latitude,latLng.longitude)

            }
        }
    }

    private fun getPlaceInfo(lat: Double, lon: Double) {
        try {
            val addresses: List<Address> = mGeocoder!!.getFromLocation(lat, lon, 1)!!
            if (addresses[0].locality != null) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                mBinding.etCity.setText(city)
                stateName = state

            }
        } catch (e: java.lang.Exception) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RetailerSDKApp.getInstance().updateAnalytics("editProfileExit")
    }

}