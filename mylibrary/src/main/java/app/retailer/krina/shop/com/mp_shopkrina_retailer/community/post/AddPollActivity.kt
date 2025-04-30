package app.retailer.krina.shop.com.mp_shopkrina_retailer.community.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.ImageObjEntity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddPollBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.ProgressDialog
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddPollActivity : AppCompatActivity(), OnButtonClick {
    private lateinit var binding: ActivityAddPollBinding
    private lateinit var postViewModel: PostViewModel
    private var pollOptionList: ArrayList<PostModel.PollValueEntity>? = null
    private var pollAdapter: PollOptionAdapter? = null

    var uploadFilePath = ""
    var fileName = ""
    private var relativePath = ""
    private var fileFullPath = ""
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var videoResultLauncher: ActivityResultLauncher<Intent>

    val analyticPost = AnalyticPost()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_poll)
        val repository = PostRepository(RestClient.getInstance4().service4)
        postViewModel = ViewModelProvider(this, PostFactory(repository))[PostViewModel::class.java]

        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        pollOptionList?.removeAt(pos)
        pollAdapter?.notifyItemRemoved(pos)
        pollAdapter?.notifyDataSetChanged()
    }


    private fun init() {

        postViewModel.uploadPostImg.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Utils.setToast(applicationContext, it.errorMessage)

                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data != null && !it.data.isJsonNull) {
                        fileName = it.data.get("FileName").asString
                        relativePath = it.data.get("RelativePath").asString
                        fileFullPath = it.data.get("FileFullpath").asString

                        Glide.with(this)
                            .load(
                                SharePrefs.getInstance(MyApplication.getInstance())
                                    .getString(SharePrefs.TRADE_WEB_URL) + fileFullPath
                            )
                            .into(binding.ivAdd)

                    } else {
                        Utils.setToast(applicationContext, "error")
                    }
                }
            }
        }
        postViewModel.newPostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    ProgressDialog.getInstance().show(this)
                }

                is NetworkResult.Failure -> {
                    ProgressDialog.getInstance().dismiss()
                    Utils.setToast(applicationContext, it.errorMessage)

                }

                is NetworkResult.Success -> {
                    ProgressDialog.getInstance().dismiss()
                    if (it.data.get("status").asBoolean) {
                        Utils.setToast(applicationContext, "पोस्ट सफतापूर्वक जोड़ा गया।")
                        startActivity(Intent(applicationContext, FeedActivity::class.java))
                        finish()
                        analyticPost.eventName = "newPostUpload"
                        analyticPost.postType = "Poll"
                        MyApplication.getInstance().updateAnalytics(analyticPost)
                    } else {
                        binding.toolbarPost.btnPost.visibility = View.VISIBLE
                        Utils.setToast(applicationContext, it.data.get("res").asString)
                    }
                }
            }
        }

        pollOptionList = ArrayList()
        pollOptionList?.add(PostModel.PollValueEntity())
        pollOptionList?.add(PostModel.PollValueEntity())
        pollAdapter = PollOptionAdapter(this, pollOptionList!!, this)
        binding.rvPoll.adapter = pollAdapter

        binding.toolbarPost.btnPost.visibility = View.VISIBLE
        binding.toolbarPost.tvBtnToolbarTitle.text = "पोस्ट करे"
        binding.toolbarPost.back.setOnClickListener {
            finish()
        }
        binding.toolbarPost.btnPost.setOnClickListener {
            if (binding.tilTitle.editText?.text.toString().trim().isEmpty()) {
                binding.tilTitle.error = "Enter question"
            } else if (pollOptionList!!.any { it.optionsValue!!.isEmpty() }) {
                binding.tilTitle.error = ""
                Utils.setToast(applicationContext, "प्रश्न का उत्तर लिखें")
            } else {
                binding.tilTitle.isErrorEnabled = false
                binding.toolbarPost.btnPost.visibility = View.GONE
                var imageList: ArrayList<ImageObjEntity>? = null
                if (fileFullPath.isNotEmpty()) {
                    imageList = ArrayList()
                    val imgModel = ImageObjEntity()
                    imgModel.imgFileName = fileName
                    imgModel.imgFileRelativePath = relativePath
                    imgModel.imgFileFullPath = fileFullPath
                    imageList.add(imgModel)
                }
                //
                val model = PostModel()
                model.userId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
                model.userName = SharePrefs.getInstance(this).getString(SharePrefs.USER_NAME)
                model.desc = "" + binding.tilTitle.editText?.text.toString().trim()
                model.title = "" + binding.tilTitle.editText?.text.toString().trim()
                model.imgFileName = fileName
                model.imgFileRelativePath = relativePath
                model.imgFileFullPath = fileFullPath
                model.postType = "Poll"
                model.pollValue = pollOptionList
                model.imageObj = imageList

                postViewModel.newPost(model)
            }
        }
        binding.btnAddItem.setOnClickListener { v ->
            pollOptionList?.add(PostModel.PollValueEntity())
            pollAdapter?.notifyDataSetChanged()
            Utils.hideKeyboard(this, v)
        }
        binding.cvAdd.setOnClickListener { v ->
            Utils.hideKeyboard(this, v)
            fileName = "feed_" + System.currentTimeMillis() + "Img.jpg"
            requestWritePermission()
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
                            this@AddPollActivity,
                            selectedFileUri
                        )!!
                    }
                    val file = File(uploadFilePath)
                    if (file.name.contains("gif")) uploadImagePath(File(uploadFilePath))
                    CoroutineScope(Dispatchers.IO).launch {
                        uploadMultipart()
                    }
                }
            }
    }

    private fun requestWritePermission() {
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
                        showImageUploadPhoto()
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
        tvAddPhotoHead!!.text = MyApplication.getInstance().dbHelper.getString(R.string.addphoto)
        takePhoto!!.text = MyApplication.getInstance().dbHelper.getString(R.string.takephoto)
        gallery!!.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Choose_from_Library)
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
        tvVideo?.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.type = "video/*"
            videoResultLauncher.launch(intent)
        }
        cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun chooseGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun pickFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File
        try {
            photoFile = createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val photoUri = FileProvider.getUriForFile(
            (this@AddPollActivity),
            this.packageName + ".provider",
            photoFile
        )
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        cameraLauncher.launch(pictureIntent)
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, fileName)
        uploadFilePath = file.absolutePath
        return file
    }


    // upload photo
    private fun uploadMultipart() {
        val fileToUpload = File(uploadFilePath)
        Compressor(this).compressToFileAsFlowable(fileToUpload).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file: File -> uploadImagePath(file) }) { throwable: Throwable ->
                throwable.printStackTrace()
                Utils.setToast(applicationContext, throwable.message)
            }
    }

    private fun uploadImagePath(file: File) {
        val requestFile: RequestBody =
            if (file.name.contains("gif")) file.asRequestBody("image/gif".toMediaTypeOrNull()) else file.asRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        postViewModel.uploadPostImg(body)
    }

    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context,
                uri
            )
        ) {
            // DocumentProvider
            if ("com.android.externalstorage.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return "${context.getExternalFilesDir(null)}/${split[1]}"
                }
            } else if ("com.android.providers.media.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context,
                    contentUri,
                    selection,
                    selectionArgs
                )
            }
        } else if ("content" == uri.scheme) {
            return getDataColumn(context, uri, null, null)
        }
        // File
        else if ("file" == uri.scheme) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
}