package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.image


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.SingleLiveEvent
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class ImageProcessing {
    companion object {
        val postbody = SingleLiveEvent<MultipartBody.Part>()
        val getBody: SingleLiveEvent<MultipartBody.Part>
            get() = postbody


        @SuppressLint("CheckResult")
        fun uploadMultipart(filePath: String, context: Context) {
           // id.zelory:compressor:3.0.1
           /* val compressedImageFile = Compressor.compress(context, File(filePath)) {
                 quality(90)
                 format(Bitmap.CompressFormat.JPEG)
                 }
                val requestFile: RequestBody = compressedImageFile.asRequestBody("image/*".toMediaTypeOrNull())
             val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file1.name, requestFile)
             */
            */

            Compressor(context)
                .compressToFileAsFlowable(File(filePath))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file: File ->
                    val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    postbody.postValue(
                        MultipartBody.Part.createFormData(
                            "file",
                            file.name,
                            requestFile
                        )
                    )
                }) { throwable: Throwable ->
                    throwable.printStackTrace()
                    Toast.makeText(
                        context,
                        "" + throwable.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        fun addWaterMark(context: Context, fileName: String, bm: Bitmap, latLng: LatLng): String {
            /*class WorkerTask : AsyncTask<String, String, String>() {
                override fun onPreExecute() {
                    super.onPreExecute()
                    ViewUtils.showProgressDialog(this@CustomerOtpVerifyActivity)
                }
                override fun doInBackground(vararg p0: String?): String {
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    newFilePath = ImageProcessing.addWaterMark(
                        applicationContext, shopFileName, bitmap,
                        LatLng(shopLat, shopLng)
                    )
                    return ""
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    ImageProcessing.uploadMultipart(newFilePath, applicationContext)
                }
            }
            WorkerTask().execute()*/
            var finalText = ""
            val time = SimpleDateFormat("dd-MM-yyyy kk:mm:ss", Locale.ENGLISH).format(Date())
            finalText = (time + "  " + latLng.latitude+","+latLng.longitude)
            val mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.BLUE
            paint.textAlign = Paint.Align.RIGHT
            paint.textSize = (bm.width * 4 / 100).toFloat()
            val textRect = Rect()
            paint.getTextBounds(finalText, 0, finalText.length, textRect)
            val canvas = Canvas(mutableBitmap)
            // If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= canvas.width - 4) //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.textSize = 10f //Scaling needs to be used for different dpi's

            // Calculate the positions
            val xPos = canvas.width - finalText.length
            val yPos = canvas.height - 10
            canvas.drawText(finalText, xPos.toFloat(), yPos.toFloat(), paint)
            val realPath = saveImages(context, fileName, mutableBitmap)
            return realPath
        }

        private fun saveImages(context: Context, fileName: String, bm: Bitmap): String {
            val root = Environment.getExternalStorageDirectory().toString()
            var imageUri: Uri? = null
            val fos: OutputStream
            val directory = Environment.DIRECTORY_PICTURES
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                try {
                    val resolver = context.contentResolver
                    val contentValues = ContentValues()
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image")
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                    imageUri = resolver?.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    fos = resolver?.openOutputStream(imageUri!!)!!
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.flush()
                    fos.close()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                Log.d(
                    ContentValues.TAG,
                    "SavedImages:" + root + File.separator + directory + File.separator + fileName
                )
                root + File.separator + directory + File.separator + fileName
            } else {
                val file: File
                val folderPath = root + "/ShopKirana"
                val folder = File(folderPath)
                if (!folder.exists()) {
                    folder.mkdirs()
                    file = File(folder, fileName)
                } else {
                    file = File(folder, fileName)
                    if (file.exists()) file.delete()
                }
                try {
                    val out = FileOutputStream(file)
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                    //  val  uploadFilePath = "$root/ShopKirana/$fileName"
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                "$root/ShopKirana/$fileName"
            }
        }
    }
}

