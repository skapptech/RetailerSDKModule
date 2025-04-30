package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityUploadDocumentBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyUdharDataModel.MyudhcustClass
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyUdharPojo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyUdharResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.Constants
import com.google.gson.Gson
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class UploadDocumentActivity : AppCompatActivity(), View.OnClickListener {
    private val CAPTURE_IMAGE_CAMERA = 111
    private val CAPTURE_IMAGE_CAMERA_BACK = 222

    private lateinit var mBinding: ActivityUploadDocumentBinding

    private var toolBarBackLL: ImageView? = null
    private var toolbarTittleTV: TextView? = null
    private var myudhcustClass: MyudhcustClass? = null
    private var imageName: String? = null
    private var uploadFilePath: String? = null
    private var panCardImagePath: String? = null
    private var UploadDocumentBack: String? = null
    private var CustomerId = 0
    private var panCardNumberST: String? = null
    private var genderString: String? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var utils: Utils? = null
    private var DocumentSpinner: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_upload_document)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        CustomerId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)

        // get intent data
        if (intent.extras != null) {
            panCardNumberST = intent.getStringExtra("panCardNumberST")
            panCardImagePath = intent.getStringExtra("CustomerDocUpload")
            genderString = intent.getStringExtra("genderString")
        }
        if (!MyApplication.getInstance().CHECK_FROM_COME) {
            initView()
            incomeSetData()
        } else {
            setDataView()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.bt_next -> uploadDocumentData()
            R.id.rl_upload_pan_txt -> {
                val otherDoc = DocumentSpinner!!.selectedItem.toString()
                if (otherDoc != "Please Select") {
                    openImageOptionsBottomMenu()
                } else {
                    Toast.makeText(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getData("please_select_document_type"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.rl_upload_doc_back -> openImageOptionsBottomMenuBack()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY) && resultCode == RESULT_OK) {
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
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE
            ) scale *= 2
            options.inSampleSize = scale
            options.inJustDecodeBounds = false
            bm = BitmapFactory.decodeFile(selectedImagePath, options)
            uploadFilePath = SavedImages(bm)
            mBinding.ivShowIv.visibility = View.GONE
            mBinding.tvUplodText.visibility = View.GONE
            mBinding.ivUploadPan.setImageBitmap(bm)
            uploadMultipart()
        } else if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                val selectedImage = Uri.parse(uploadFilePath)
                mBinding.ivShowIv.visibility = View.GONE
                mBinding.tvUplodText.visibility = View.GONE
                mBinding.ivUploadPan.setImageURI(selectedImage)
                if (Utils.isNetworkAvailable(
                        applicationContext
                    )
                ) {
                    uploadMultipart()
                } else {
                    Toast.makeText(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getData("failed_to_capture_picture"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == CAPTURE_IMAGE_CAMERA_BACK && resultCode == RESULT_OK) {
            try {
                val selectedImage = Uri.parse(uploadFilePath)
                mBinding.ivShowIvBack.visibility = View.GONE
                mBinding.tvUplodTextBack.visibility = View.GONE
                mBinding.ivUploadDocBack.setImageURI(selectedImage)
                if (Utils.isNetworkAvailable(
                        applicationContext
                    )
                ) {
                    uploadMultipartBack()
                } else {
                    Toast.makeText(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getData("failed_to_capture_picture"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY_BACK) && resultCode == RESULT_OK && null != data) {
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
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE
            ) scale *= 2
            options.inSampleSize = scale
            options.inJustDecodeBounds = false
            bm = BitmapFactory.decodeFile(selectedImagePath, options)
            uploadFilePath = SavedImagesBack(bm)
            mBinding.ivShowIvBack.visibility = View.GONE
            mBinding.tvUplodTextBack.visibility = View.GONE
            mBinding.ivUploadDocBack.setImageBitmap(bm)
            uploadMultipartBack()
        }
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

    private fun setDataView() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        toolBarBackLL = mBinding.toolbarUploadDocument.back
        toolbarTittleTV = mBinding.toolbarUploadDocument.title
        toolbarTittleTV!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Address_Proof)
        toolBarBackLL!!.setOnClickListener(this)
        mBinding.btNext.setOnClickListener(this)
        mBinding.rlUploadPanTxt.setOnClickListener(this)
        mBinding.rlUploadDocBack.setOnClickListener(this)
        DocumentSpinner = mBinding.spDocument
        incomeSetData()
        val dataSaved = SharePrefs.getInstance(this).getString(SharePrefs.MY_UDHAR_GET_DATA)
        if (dataSaved != null && !TextUtils.isNullOrEmpty(dataSaved)) {
            try {
//                JSONObject jsonObject = new JSONObject(DataSaved);
                myudhcustClass = Gson().fromJson(dataSaved, MyudhcustClass::class.java)
                try {
                    if (myudhcustClass?.othercustdoc.equals("Please Select", ignoreCase = true)) {
                        DocumentSpinner!!.setSelection(0)
                    } else if (myudhcustClass?.othercustdoc.equals(
                            "Driving License",
                            ignoreCase = true
                        )
                    ) {
                        DocumentSpinner!!.setSelection(1)
                    } else if (myudhcustClass?.othercustdoc.equals(
                            "Aadhaar Card",
                            ignoreCase = true
                        )
                    ) {
                        DocumentSpinner!!.setSelection(2)
                    } else if (myudhcustClass?.othercustdoc.equals("Passport", ignoreCase = true)) {
                        DocumentSpinner!!.setSelection(3)
                    } else if (myudhcustClass?.othercustdoc.equals("Voter ID", ignoreCase = true)) {
                        DocumentSpinner!!.setSelection(4)
                    } else if (myudhcustClass?.othercustdoc.equals(
                            "Electricity Bill",
                            ignoreCase = true
                        )
                    ) {
                        DocumentSpinner!!.setSelection(5)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    if (!myudhcustClass?.addressProofUrl!!.isEmpty()) {
                        Glide.with(this).load(myudhcustClass?.addressProofUrl)
                            .placeholder(R.drawable.logo_grey).into(
                                mBinding.ivUploadPan
                            )
                    }
                    if (!myudhcustClass?.backImageUrl!!.isEmpty()) {
                        Glide.with(this).load(myudhcustClass?.backImageUrl)
                            .placeholder(R.drawable.logo_grey).into(
                                mBinding.ivUploadDocBack
                            )
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // set income data
    private fun incomeSetData() {
        val adapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, docList)
        DocumentSpinner!!.adapter = adapter
    }

    private val docList: ArrayList<String>
        private get() {
            val udharDocArray = resources.getStringArray(R.array.udhar_doc)
            return ArrayList(Arrays.asList(*udharDocArray))
        }

    private fun initView() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        toolBarBackLL = mBinding.toolbarUploadDocument.back
        toolbarTittleTV = mBinding.toolbarUploadDocument.title
        toolbarTittleTV!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Address_Proof)
        mBinding.tvUploadPan.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_any_other_doc)
        mBinding.tvUplodText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Document)
        mBinding.txtBck.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Document_back)
        mBinding.tvUplodTextBack.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Document_back)
        mBinding.btNext.text = MyApplication.getInstance().dbHelper.getString(R.string.submit)
        mBinding.toolbarUploadDocument.back.setOnClickListener(this)
        mBinding.btNext.setOnClickListener(this)
        mBinding.rlUploadPanTxt.setOnClickListener(this)
        mBinding.rlUploadDocBack.setOnClickListener(this)
        DocumentSpinner = mBinding.spDocument
    }

    private fun uploadDocumentData() {
        var myUdharPojo: MyUdharPojo? = null
        val otherDoc = DocumentSpinner!!.selectedItem.toString()
        if (mBinding.ivUploadPan.drawable == null || mBinding.ivUploadDocBack.drawable == null) {
            Utils.setToast(
                applicationContext, "Please Upload Your Document "
            )
        } else {
            myUdharPojo = if (panCardImagePath != null && UploadDocumentBack != null) {
                MyUdharPojo(
                    Integer.toString(CustomerId), panCardImagePath,
                    panCardImagePath, "", genderString, "",
                    false, UploadDocumentBack, panCardNumberST, otherDoc
                )
            } else {
                MyUdharPojo(
                    CustomerId.toString(), panCardImagePath,
                    myudhcustClass!!.addressProofUrl, "", genderString, "",
                    false, myudhcustClass!!.backImageUrl, panCardNumberST, otherDoc
                )
            }
            if (utils!!.isNetworkAvailable) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(this)
                    commonClassForAPI!!.postUdharData(getMyUdharResponseResponse, myUdharPojo)
                }
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                )
            }
        }
    }

    private fun submitDialog() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.ic_dialog_bg)
        dialog.setContentView(R.layout.dialog_submit_thanks)
        val OkTv = dialog.findViewById<TextView>(R.id.tv_ok)
        val tvThanksHead = dialog.findViewById<TextView>(R.id.tvThanksHead)
        val tvYouWillReceive = dialog.findViewById<TextView>(R.id.tvYouWillReceive)
        tvThanksHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.thanks_for_uploading)
        tvYouWillReceive.text =
            MyApplication.getInstance().dbHelper.getString(R.string.receive_form_as_eligibility_verified)
        OkTv.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        dialog.setCancelable(false)
        OkTv.setOnClickListener { v: View? ->
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openImageOptionsBottomMenu() {
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
            callForImage(CAPTURE_IMAGE_CAMERA)
            dialog.dismiss()
        }
        gallery.setOnClickListener { v: View? ->
            callForImage(resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY))
            dialog.dismiss()
        }
        Cancel!!.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    private fun openImageOptionsBottomMenuBack() {
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
            callForImageBack(CAPTURE_IMAGE_CAMERA_BACK)
            dialog.dismiss()
        }
        gallery.setOnClickListener { v: View? ->
            callForImageBack(resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY_BACK))
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
                        if (imageResource == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY)) {
                            pickFromGallery()
                        } else if (imageResource == CAPTURE_IMAGE_CAMERA) {
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
                if (imageResource == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY)) {
                    pickFromGallery()
                } else if (imageResource == CAPTURE_IMAGE_CAMERA) {
                    pickFromCamera()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callForImageBack(integer: Int) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (MarshmallowPermissions.checkPermissionCamera(this)) {
                    if (MarshmallowPermissions.checkPermissionWriteExternalStorage(this)) {
                        if (integer == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY_BACK)) {
                            pickFromGalleryBack()
                        } else if (integer == CAPTURE_IMAGE_CAMERA_BACK) {
                            pickFromCameraBack()
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
                if (integer == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY_BACK)) {
                    pickFromGalleryBack()
                } else if (integer == CAPTURE_IMAGE_CAMERA_BACK) {
                    pickFromCameraBack()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pickFromGalleryBack() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            galleryIntent,
            resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY_BACK)
        )
    }

    private fun pickFromCameraBack() {
        val tsLong = System.currentTimeMillis() / 1000
        imageName = "back_image" + CustomerId + "_" + tsLong + ".jpg"
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (pictureIntent.resolveActivity(packageManager) != null) {
        val photoFile = createImageFile()
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
        startActivityForResult(pictureIntent, CAPTURE_IMAGE_CAMERA_BACK)
    }

    fun pickFromGallery() {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY)
        )
    }

    fun pickFromCamera() {
        val tsLong = System.currentTimeMillis() / 1000
        imageName = "front_image" + CustomerId + "_" + tsLong + ".jpg"
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            val photoFile = createImageFile()
            val photoUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile
            )
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
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
        file = File(storageDir, imageName)
        uploadFilePath = file.absolutePath
        return file
    }

    fun SavedImages(bm: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
        val tsLong = System.currentTimeMillis() / 1000
        imageName = "front_image" + CustomerId + "_" + tsLong + ".jpg"
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                val resolver = this.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
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
                "SavedImages:" + root + File.separator + directory + File.separator + imageName
            )
            root + File.separator + directory + File.separator + imageName
        } else {
            val myDir = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
            if (!myDir.exists()) myDir.mkdirs()
            val file = File(myDir, imageName)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath = "$root/ShopKirana/$imageName"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            "$root/ShopKirana/$imageName"
        }
    }

    fun SavedImagesBack(bm: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
        val tsLong = System.currentTimeMillis() / 1000
        imageName = "back_image" + CustomerId + "_" + tsLong + ".jpg"
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                val resolver = this.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
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
                "SavedImages:" + root + File.separator + directory + File.separator + imageName
            )
            root + File.separator + directory + File.separator + imageName
        } else {
            val myDir = File(Environment.getExternalStorageDirectory().toString() + "/ShopKirana")
            if (!myDir.exists()) myDir.mkdirs()
            val file = File(myDir, imageName)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath = "$root/ShopKirana/$imageName"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            "$root/ShopKirana/$imageName"
        }
    }

    /**
     * upload photo
     */
    private fun uploadMultipart() {
        val fileToUpload = File(uploadFilePath)
        Compressor(applicationContext)
            .compressToFileAsFlowable(fileToUpload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File -> uploadImagePath(file) }) { throwable: Throwable ->
                throwable.printStackTrace()
                showError(throwable.message)
            }
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(this)
        commonClassForAPI!!.uploadCustomerImage(imageObserver, body)
    }

    fun uploadMultipartBack() {
        val file = File(uploadFilePath)
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(this)
        commonClassForAPI!!.uploadCustomerBackImage(docImageObserver, body)
    }

    // My Udhar response
    private val getMyUdharResponseResponse: DisposableObserver<MyUdharResponse> =
        object : DisposableObserver<MyUdharResponse>() {
            override fun onNext(myUdharResponse: MyUdharResponse) {
                Utils.hideProgressDialog()
                try {
                    if (myUdharResponse.isStatus) {
                        submitDialog()
                    } else {
                        Utils.setToast(
                            applicationContext, myUdharResponse.message
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.somthing_went_wrong)
                    )
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.somthing_went_wrong)
                )
            }

            override fun onComplete() {}
        }

    // upload image
    private val imageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(result: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (result != null) {
                        //Upload Success
                        UploadDocumentBack = result.name
                        Log.e("Success", "Success$result")
                    } else {
                        //Upload Failed
                        Log.e("Failed", "Failed$result")
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
    private val docImageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(result: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (result != null) {
                        //Upload Success
                        UploadDocumentBack = result.name
                        Log.e("Success", "Success$result")
                    } else {
                        //Upload Failed
                        Log.e("Failed", "Failed$result")
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