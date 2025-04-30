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
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.FeedPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.feed.ImageObjEntity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddPostBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.DateUtilskotlin.Companion.getPath
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.NetworkResult
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
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

class AddPostActivity : AppCompatActivity(), ImageAdapter.PostImageFromGallery {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var editFeedModel: FeedPostModel
    val analyticPost = AnalyticPost()

    var fileName = ""
    var uploadFilePath = ""
    var postType = 0
    private var comeFrom: String? = null

    private var imageList: ArrayList<ImageObjEntity>? = null
    private var adapter: ImageAdapter? = null

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var videoResultLauncher: ActivityResultLauncher<Intent>

    //    private val BUCKET_NAME = "social-kirana-videos"
    private var s3Client: AmazonS3Client? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post)
        val repository = PostRepository(RestClient.getInstance4().service4)
        postViewModel = ViewModelProvider(this, PostFactory(repository))[PostViewModel::class.java]

        if (intent.extras != null) comeFrom = intent.getStringExtra("comeFrom")

        init()

        if (comeFrom.equals("Feed")) {
            uploadFilePath = intent.getStringExtra("imagePath")!!
            uploadMultipart(false)
            imageList?.add(ImageObjEntity(uploadFilePath))
            adapter?.notifyDataSetChanged()
        } else if (comeFrom.equals("EditProfile")) {
            editFeedModel = intent.getSerializableExtra("model") as FeedPostModel
            binding.tilDes.editText?.setText(editFeedModel.desc)
            //
            imageList?.addAll(editFeedModel.imageObj!!)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    override fun postImagesFromGalley(path: String?, pos: Int) {
        try {
            imageList?.removeAt(pos)
            adapter?.notifyDataSetChanged()
            uploadFilePath = ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun init() {
        val intent = intent
        val action = intent.action
        val type = intent.type

        imageList = ArrayList()
        adapter = ImageAdapter(this, imageList, this)
        binding.rvImage.adapter = adapter

        if (Intent.ACTION_SEND == action && type != null) {
            if (type.startsWith("image/")) {
                handleSharedImage(intent, binding.ivAdd)
            }
        }


        postViewModel.uploadPostImg.observe(this) {
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
                        val model = ImageObjEntity()
                        model.imgFileName = it.data.get("FileName").asString
                        model.imgFileRelativePath = it.data.get("RelativePath").asString
                        model.imgFileFullPath = it.data.get("FileFullpath").asString
                        imageList?.add(model)
                        adapter = ImageAdapter(this, imageList, this)
                        binding.rvImage.adapter = adapter
//                        Picasso.get().load(Constant.COMMUNITY_URL + fileFullPath)
//                            .into(binding.ivAdd)

                    } else {
                        Utils.setToast(applicationContext, "error")
                    }
                }
            }
        }
        postViewModel.newPostResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCom.visibility = View.VISIBLE
                }

                is NetworkResult.Failure -> {
                    binding.progressCom.visibility = View.GONE
                    Utils.setToast(applicationContext, it.errorMessage)

                }

                is NetworkResult.Success -> {
                    binding.progressCom.visibility = View.GONE
                    if (it.data.get("status").asBoolean) {
                        Utils.setToast(applicationContext, "पोस्ट सफतापूर्वक जोड़ा गया।")
                        startActivity(Intent(applicationContext, FeedActivity::class.java))
                        finish()
                        if (comeFrom.equals("EditProfile")) {
                            analyticPost.eventName = "editPost"
                        } else {
                            analyticPost.eventName = "newPostUpload"
                        }

                        if (postType == 1) {
                            analyticPost.postType = "Video"
                        } else {
                            analyticPost.postType = "Post"
                        }
                        MyApplication.getInstance().updateAnalytics(analyticPost)
                    } else {
                        binding.toolbarPost.btnPost.visibility = View.VISIBLE
                        Utils.setToast(applicationContext, it.data.get("res").asString)
                    }
                }
            }
        }

        binding.toolbarPost.btnPost.visibility = View.VISIBLE
        binding.toolbarPost.tvBtnToolbarTitle.text = "पोस्ट करे"

        binding.cvAdd.setOnClickListener {
            fileName = "feed_" + System.currentTimeMillis() + "Img.jpg"
            requestWritePermission()
        }
        binding.toolbarPost.back.setOnClickListener {
            finish()
        }
        binding.toolbarPost.btnPost.setOnClickListener {
            val des = binding.tilDes.editText?.text
            if (des.toString().trim().isEmpty() && uploadFilePath.isEmpty()) {
                Utils.setToast(applicationContext, " विवरण भरें या फोटो अपलोड करें")
                return@setOnClickListener
            } else {
                binding.toolbarPost.btnPost.visibility = View.GONE
                binding.progressCom.visibility = View.VISIBLE
                val model = PostModel()
                model.userId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
                model.userName = SharePrefs.getInstance(this).getString(SharePrefs.USER_NAME)
                model.desc = (des?.trim() ?: "").toString()
                model.title = ""
                model.imageObj = imageList
                model.pollValue = null
                if (postType == 1) {
                    model.postType = "Video"
                } else {
                    model.postType = "Post"
                }

                if (comeFrom.equals("EditProfile")) {
                    model.postId = editFeedModel.postId
                    postViewModel.editPost(model)
                } else {
                    postViewModel.newPost(model)
                }
            }
        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        uploadMultipart(true)
                    }
//                    imageList?.add(ImageObjEntity(uploadFilePath))
//                    adapter?.notifyDataSetChanged()
                }
            }
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val selectedFileUri = result.data?.data
                        uploadFilePath = getPath(this, selectedFileUri)!!
                    }
//                    imageList?.add(ImageObjEntity(uploadFilePath))
//                    adapter?.notifyDataSetChanged()

                    val file = File(uploadFilePath)
                    if (file.name.contains("gif")) uploadImagePath(File(uploadFilePath))
                    else CoroutineScope(Dispatchers.IO).launch {
                        uploadMultipart(false)
                    }
                }
            }
        videoResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    if (data == null) {
                        Log.d("TAG", "Data returned is null")
                        return@registerForActivityResult
                    }
                    val videoUri: Uri = data.data!!
                    val videoPath = parsePath(videoUri)
                    println("path $videoPath")
                    if (videoPath != null) {
                        uploadImage()
                    }
                }
            }
    }

    private fun parsePath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close() // Make sure you close cursor after use

            path
        } else null
    }

    private fun requestWritePermission() {
        try {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            Permissions.check(this, permissions, null, null, object : PermissionHandler() {
                override fun onGranted() {
                    showImageUploadPhoto()
                }

                override fun onDenied(
                    context: Context, deniedPermissions: ArrayList<String>
                ) {
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun handleSharedImage(intent: Intent, imageView: ImageView) {
        val imageUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        if (imageUri != null) {
            try {
                fileName = "feed_" + System.currentTimeMillis() + "Img.jpg"
                uploadFilePath = getFilePathFromUri(this, imageUri)!!
                imageView.setImageURI(imageUri)
                uploadMultipart(true)
                imageList?.add(ImageObjEntity(uploadFilePath))
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {

                e.printStackTrace()
            }
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
                        Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
                Permissions.check(this, permissions, null, null, object : PermissionHandler() {
                    override fun onGranted() {
                        pickFromCamera()
                        dialog.dismiss()
                    }

                    override fun onDenied(
                        context: Context, deniedPermissions: ArrayList<String>
                    ) {
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        gallery.setOnClickListener {
            postType = 0
            try {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
                Permissions.check(this, permissions, null, null, object : PermissionHandler() {
                    override fun onGranted() {
                        dialog.dismiss()
                        chooseGallery()
                    }

                    override fun onDenied(
                        context: Context, deniedPermissions: ArrayList<String>
                    ) {
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        tvVideo?.setOnClickListener {
            dialog.dismiss()
            postType = 1
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
            this, this.packageName + ".provider", photoFile
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
    private fun uploadMultipart(camera: Boolean) {
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
        Utils.showProgressDialog(this)
        postViewModel.uploadPostImg(body)
    }

    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context, uri
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
            }
            // MediaProvider
            else if ("com.android.providers.media.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
        // MediaStore (and general)
        else if ("content" == uri.scheme) {
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

    private fun uploadImage() {
        binding.progressUpload.visibility = View.VISIBLE
        val key = "Vid_" + System.currentTimeMillis()
        val trans = TransferUtility.builder().context(applicationContext).s3Client(s3Client).build()
//        val observer: TransferObserver =
//            trans.upload(BUCKET_NAME, key, file) // manual storage permission
//        observer.setTransferListener(object : TransferListener {
//            override fun onStateChanged(id: Int, state: TransferState) {
//                if (state == TransferState.COMPLETED) {
//                    Log.d("msg", "success")
//                    uploadFilePath = "https://dbwqc84hfehkv.cloudfront.net/$id"
//                    //
//                    imageList?.add(ImageObjEntity(file.path))
//                    val model = ImageObjEntity()
//                    model.imgFileName = "" + id
//                    model.imgFileRelativePath = "https://dbwqc84hfehkv.cloudfront.net/"
//                    model.imgFileFullPath = uploadFilePath
//                    imageList?.add(model)
//                    adapter?.notifyDataSetChanged()
//                } else if (state == TransferState.FAILED) {
//                    Log.d("msg", "fail")
//                }
//            }
//
//            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//                if (bytesCurrent == bytesTotal) {
//                    binding.ivAdd.setImageResource(R.drawable.logo_grey)
//                    binding.progressUpload.visibility = View.INVISIBLE
//                }
//                binding.progressUpload.max = bytesTotal.toInt()
//                binding.progressUpload.progress = bytesCurrent.toInt()
//                println(" pro $")
//            }
//
//            override fun onError(id: Int, e: Exception) {
//                e.printStackTrace()
//                Log.d("error", e.toString())
//                binding.progressUpload.visibility = View.INVISIBLE
//            }
//        })
    }
}