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
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPanCardUploadBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyUdharDataModel.MyudhcustClass
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.TermsAndConditionActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.Constants
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class PanCardUploadActivity : AppCompatActivity(), View.OnClickListener {
    private val CAPTURE_IMAGE_CAMERA = 111
    private lateinit var mBinding: ActivityPanCardUploadBinding

    private var toolBarBackLL: ImageView? = null
    private var toolbarTittleTV: TextView? = null
    private var uploadTextView: TextView? = null
    private var nextBT: Button? = null
    private var uploadPanCard: RelativeLayout? = null
    private var customerDocUpload: String? = null
    private var uploadFilePath: String? = null
    private var panCardImagePath: String? = null
    private var showImageView: ImageView? = null
    private var CustomerId = 0
    private var panCardNumberET: EditText? = null
    private var GenderSpinner: Spinner? = null
    private var dialog: Dialog? = null
    private var myudhcustClass: MyudhcustClass? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pan_card_upload)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        CustomerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        if (!MyApplication.getInstance().CHECK_FROM_COME) {
            submitDialog()
        } else {
            setDataView()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.leftTransaction(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.bt_next -> uploadPanCardData()
            R.id.rl_upload_pan_txt -> openImageOptionsBottomMenu()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY) && resultCode == RESULT_OK) {
            try {
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
                uploadFilePath = savedImages(bm)
                showImageView!!.visibility = View.GONE
                uploadTextView!!.visibility = View.GONE
                mBinding.ivUploadPan.setImageBitmap(bm)
                uploadMultipart()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == CAPTURE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                val selectedImage = Uri.parse(uploadFilePath)
                showImageView!!.visibility = View.GONE
                uploadTextView!!.visibility = View.GONE
                mBinding.ivUploadPan.setImageURI(selectedImage)
                if (Utils.isNetworkAvailable(
                        applicationContext
                    )
                ) {
                    uploadMultipart()
                } else {
                    Toast.makeText(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.failed_to_capture_the_picture),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
        toolBarBackLL = mBinding.toolbarPancard.back
        toolbarTittleTV = mBinding.toolbarPancard.title
        nextBT = mBinding.btNext
        uploadPanCard = mBinding.rlUploadPanTxt
        showImageView = mBinding.ivShowIv
        uploadTextView = mBinding.tvUplodText
        panCardNumberET = mBinding.etPanNumber
        GenderSpinner = mBinding.spGender
        toolbarTittleTV!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_PAN_Card)
        mBinding.tvGender.text = MyApplication.getInstance().dbHelper.getString(R.string.gender)
        mBinding.tvUploadPan.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_Your_PAN)
        mBinding.tvUplodText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_PAN)
        mBinding.tvUplodText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_PAN)
        mBinding.btNext.text = MyApplication.getInstance().dbHelper.getString(R.string.next)
        toolBarBackLL!!.setOnClickListener(this)
        nextBT!!.setOnClickListener(this)
        uploadPanCard!!.setOnClickListener(this)
        incomeSetData()
        val dataSaved =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.MY_UDHAR_GET_DATA)
        if (dataSaved != null && dataSaved != "") {
            try {
                val jsonObject = JSONObject(dataSaved)
                myudhcustClass = Gson().fromJson(jsonObject.toString(), MyudhcustClass::class.java)
                mBinding.etPanNumber.setText(myudhcustClass?.panCardNo)
                if (myudhcustClass?.gender.equals("Male", ignoreCase = true)) {
                    GenderSpinner!!.setSelection(1)
                } else {
                    GenderSpinner!!.setSelection(2)
                }
                if (myudhcustClass?.panCardurl!!.isNotEmpty()) {
                    Picasso.get().load(myudhcustClass?.panCardurl)
                        .placeholder(R.drawable.logo_grey).into(mBinding.ivUploadPan)
                }
                showImageView!!.visibility = View.GONE
                uploadTextView!!.visibility = View.GONE
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun submitDialog() {
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.dialog_terms_condiction)
        val checkBoxTerms = dialog!!.findViewById<CheckBox>(R.id.cb_tems_and_condiction)
        val buttonAccept = dialog!!.findViewById<Button>(R.id.bt_terms_and_condiction)
        val cancel = dialog!!.findViewById<TextView>(R.id.cancel)
        val tv_terms = dialog!!.findViewById<TextView>(R.id.tv_terms)
        val tvTermsOfUse = dialog!!.findViewById<TextView>(R.id.tvTermsOfUse)
        tv_terms.text =
            MyApplication.getInstance().dbHelper.getString(R.string.terms_and_conditions)
        cancel.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        buttonAccept.text =
            MyApplication.getInstance().dbHelper.getString(R.string.accept_and_continue)
        tvTermsOfUse.text = MyApplication.getInstance().dbHelper.getString(R.string.terms_of_use)
        val termsTV = dialog!!.findViewById<TextView>(R.id.tv_termsstring)
        customTextView(termsTV)
        cancel.setOnClickListener { v: View? ->
            dialog!!.dismiss()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }
        buttonAccept.setOnClickListener { v: View? ->
            if (checkBoxTerms.isChecked) {
                //init view
                initView()
                dialog!!.dismiss()
            } else {
                Toast.makeText(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_check_terms_conditions),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun customTextView(view: TextView) {
        val spanTxt =
            SpannableStringBuilder(MyApplication.getInstance().dbHelper.getString(R.string.agree_sk_terms))
        spanTxt.setSpan(ForegroundColorSpan(Color.BLACK), 32, spanTxt.length, 0)
        spanTxt.append(" " + MyApplication.getInstance().dbHelper.getString(R.string.terms_of_service))
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(applicationContext, TermsAndConditionActivity::class.java))
            }
        }, spanTxt.length - " Term of services".length, spanTxt.length, 0)
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    private fun initView() {
        toolBarBackLL = mBinding.toolbarPancard.back
        toolbarTittleTV = mBinding.toolbarPancard.title
        nextBT = mBinding.btNext
        uploadPanCard = mBinding.rlUploadPanTxt
        showImageView = mBinding.ivShowIv
        uploadTextView = mBinding.tvUplodText
        panCardNumberET = mBinding.etPanNumber
        GenderSpinner = mBinding.spGender
        toolbarTittleTV!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Upload_PAN_Card)
        toolBarBackLL!!.setOnClickListener(this)
        nextBT!!.setOnClickListener(this)
        uploadPanCard!!.setOnClickListener(this)
        incomeSetData()
    }

    private fun incomeSetData() {
        val genderArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, udharList)
        GenderSpinner!!.adapter = genderArrayAdapter
    }

    val udharList: ArrayList<String>
        get() {
            val udharIncomeArray = resources.getStringArray(R.array.udhar_gender)
            return ArrayList(Arrays.asList(*udharIncomeArray))
        }

    private fun uploadPanCardData() {
        val panCardNumberST = panCardNumberET!!.text.toString().trim { it <= ' ' }
        val genderString = GenderSpinner!!.selectedItem.toString()
        if (genderString.equals("Select Gender", ignoreCase = true)) {
            Toast.makeText(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(
                    R.string.Please_Select_gender
                ), Toast.LENGTH_SHORT
            ).show()
        } else if (mBinding.ivUploadPan.drawable == null) {
            Toast.makeText(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.text_plz_up_y_p),
                Toast.LENGTH_SHORT
            ).show()
        } else if (TextUtils.isNullOrEmpty(panCardNumberST)) {
            panCardNumberET!!.error =
                MyApplication.getInstance().dbHelper.getString(R.string.txt_please_enter_pan_card)
            panCardNumberET!!.requestFocus()
        } else {
            if (panCardImagePath != null) {
                startActivity(
                    Intent(applicationContext, UploadDocumentActivity::class.java)
                        .putExtra("panCardNumberST", panCardNumberST)
                        .putExtra("CustomerDocUpload", panCardImagePath)
                        .putExtra("genderString", genderString)
                )
            } else {
                if (myudhcustClass != null) {
                    val intent = Intent(applicationContext, UploadDocumentActivity::class.java)
                    if (myudhcustClass!!.panCardurl?.length!! > 0) {
                        intent.putExtra("CustomerDocUpload", myudhcustClass!!.panCardurl)
                        intent.putExtra("panCardNumberST", panCardNumberST)
                        intent.putExtra("genderString", genderString)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.please_upload_pan_card),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openImageOptionsBottomMenu() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.capture_photo_view)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        val takePhoto = dialog.findViewById<TextView>(R.id.take_photo)
        val gallery = dialog.findViewById<TextView>(R.id.gallery)
        val cancel = dialog.findViewById<TextView>(R.id.liCancel)
        val tvAddPhotoHead = dialog.findViewById<TextView>(R.id.tvAddPhotoHead)
        tvAddPhotoHead!!.text = MyApplication.getInstance().dbHelper.getString(R.string.addphoto)
        takePhoto!!.text = MyApplication.getInstance().dbHelper.getString(R.string.takephoto)
        gallery!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Choose_from_Library)
        takePhoto.setOnClickListener {
            callForImage(CAPTURE_IMAGE_CAMERA)
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            callForImage(resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY))
            dialog.dismiss()
        }
        cancel!!.setOnClickListener { dialog.dismiss() }
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

    fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, resources.getInteger(R.integer.CAPTURE_IMAGE_LIBRARY))
    }

    fun pickFromCamera() {
        val tsLong = System.currentTimeMillis() / 1000
        customerDocUpload = "Rg_pan_card" + CustomerId + "_" + tsLong + ".jpg"
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
        file = File(storageDir, customerDocUpload)
        uploadFilePath = file.absolutePath
        return file
    }

    fun savedImages(bm: Bitmap): String {
        val root = getExternalFilesDir(null).toString()
        var imageUri: Uri? = null
        val fos: OutputStream
        val directory = Environment.DIRECTORY_PICTURES
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                val resolver = this.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, customerDocUpload)
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
                "SavedImages:" + root + File.separator + directory + File.separator + customerDocUpload
            )
            root + File.separator + directory + File.separator + customerDocUpload
        } else {
            val myDir = File(getExternalFilesDir(null).toString() + "/ShopKirana")
            if (!myDir.exists()) myDir.mkdirs()
            val tsLong = System.currentTimeMillis() / 1000
            customerDocUpload = "Rg_pan_card" + CustomerId + "_" + tsLong + ".jpg"
            val file = File(myDir, customerDocUpload)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadFilePath = "$root/ShopKirana/$customerDocUpload"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            "$root/ShopKirana/$customerDocUpload"
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

    fun uploadImagePath(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        Utils.showProgressDialog(this)
        CommonClassForAPI.getInstance(this).uploadPanImage(imageObserver, body)
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
    }

    // upload image
    private val imageObserver: DisposableObserver<ImageResponse> =
        object : DisposableObserver<ImageResponse>() {
            override fun onNext(result: ImageResponse) {
                try {
                    Utils.hideProgressDialog()
                    if (result != null) {
                        panCardImagePath = result.name
                        Log.e("Success", "Success$result")
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